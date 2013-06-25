package uk.ac.ebi.pamela.layoutpipeline.pathway;

import com.sri.biospice.warehouse.database.PooledWarehouseManager;
import com.sri.biospice.warehouse.database.Warehouse;
import com.sri.biospice.warehouse.schema.DataSet;
import com.sri.biospice.warehouse.schema.TableFactory;
import com.sri.biospice.warehouse.schema.object.Chemical;
import com.sri.biospice.warehouse.schema.object.Pathway;
import com.sri.biospice.warehouse.schema.object.Reaction;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.ReconstructionImpl;
import uk.ac.ebi.mdk.domain.entity.reaction.BiochemicalReaction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.BioWarehouseChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.metabolomes.biowh.BioChemicalReactionSetProviderFactory;
import uk.ac.ebi.metabolomes.biowh.BiochemicalReactionSetProvider;
import uk.ac.ebi.pamela.layoutpipeline.reaction.CurrencyCompoundDecider;
import uk.ac.ebi.pamela.layoutpipeline.reaction.RedundantMetabProcessor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 20/6/13
 * Time: 16:58
 *
 * {@inheritDoc}
 *
 * This implementation of the {@link Pathway2Reconstruction} interface produces reconstructions based on pathways defined
 * in data sets that are source data sets to the unified sets used.
 *
 */
public class PAMELAPath2ReconsPreUnif implements Pathway2Reconstruction<Pathway>{

    private static final Logger LOGGER = Logger.getLogger(PAMELAPath2ReconsPreUnif.class);

    private DataSet ds;
    private BiochemicalReactionSetProvider provider;
    private CurrencyCompoundDecider<Chemical,Reaction> currencyDecider;

    public PAMELAPath2ReconsPreUnif(DataSet ds, Taxonomy orgIdentifier, CurrencyCompoundDecider<Chemical,Reaction> currencyDecider) {
        this.ds = ds;
        this.provider = BioChemicalReactionSetProviderFactory.getBiochemicalReactionSetProvider(ds);
        this.provider.setSpecieForProvider(orgIdentifier.getTaxon());
        this.currencyDecider = currencyDecider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reconstruction getReconstruction(Pathway pathway) {
        /**
         * First get the reactions;
         */
        Collection<Reaction> reactions = getReactionForPathway(pathway);
        /**
         * Make a new reconstruction and add the reactions to it.
         */
        Reconstruction recons = new ReconstructionImpl();

        RedundantMetabProcessor proc = new RedundantMetabProcessor();
        for (Reaction rxn : reactions) {
            MetabolicReaction mRxn = convertRxn(rxn);
            proc.processRedundantMetabolites(mRxn);
            recons.addReaction(mRxn);
        }

        return recons;
    }

    private CurrencyCompoundDecider getCurrencyDecider() {
        return currencyDecider;
    }


    /**
     * Produces an MDK {@link MetabolicReaction} starting from a {@link Reaction}.
     *
     * @param rxn the BioWarehouse reaction object to translate into a mdk reaction.
     * @return the resulting mdk reaction.
     */
    private MetabolicReaction convertRxn(Reaction rxn) {
        try {
            BiochemicalReaction biochemrxn = provider.getBioChemicalRXNFromRxn(rxn);
            Collection<Chemical> currencies = getCurrencyDecider().getCurrencyMetabolites(rxn);
            Collection<BioWarehouseChemicalIdentifier> currenciesIdents = getCurrencyBWHIdents(currencies);
            for (MetabolicParticipant part : biochemrxn.getParticipants()) {
                Metabolite m = part.getMolecule();
                if(currenciesIdents.contains(m.getIdentifier())) {
                    part.setSideCompound(Boolean.TRUE);
                }
            }
            return biochemrxn;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<BioWarehouseChemicalIdentifier> getCurrencyBWHIdents(Collection<Chemical> currencies) {
        Collection<BioWarehouseChemicalIdentifier> idents = new LinkedList<BioWarehouseChemicalIdentifier>();
        Iterator<Chemical> it = currencies.iterator();
        while(it.hasNext()) {
            Chemical chem = it.next();
            BioWarehouseChemicalIdentifier ident = new BioWarehouseChemicalIdentifier(chem.getWID(), chem.getDataSetWID());
            idents.add(ident);
        }
        return idents;
    }

    /**
     * Given a pathway (from an older data set from which the current data set was integrated), the set of reaction in
     * the current data set that participate in that pathway is provided. This link between data sets relies on the
     * CrossReference table, and it was made as a quick trick to circumvent the lack of proper pathways unifications.
     *
     * @param pathway from source data sets
     * @return a collection of biowarehouse reactions at the unified data set.
     */
    private Collection<Reaction> getReactionForPathway(Pathway pathway) {
        Collection<Reaction> reactions = new LinkedList<Reaction>();

        String query = "SELECT DISTINCT r.* FROM Reaction r " +
                "JOIN CrossReference cr ON r.WID = cr.OtherWID " +
                "JOIN Reaction ro ON ro.WID = cr.CrossWID " +
                "JOIN PathwayReaction pro ON pro.ReactionWID = ro.WID " +
                "WHERE pro.PathwayWID = ? AND r.DataSetWID = ?;";

        try {
            Warehouse bwh = PooledWarehouseManager.getWarehouse();
            PreparedStatement ps = bwh.createPreparedStatement(query);
            ps.setLong(1,pathway.getWID());
            ps.setLong(2,this.ds.getWID());

            reactions.addAll(TableFactory.loadTables(ps.executeQuery(),TableFactory.REACTION));
            bwh.close();
        } catch (SQLException e) {
            LOGGER.error("Could not retrieve reactions for pathway "+ pathway.getName()+" from PAMELA database",e);
            throw new RuntimeException(e);
        }

        return reactions;
    }
}

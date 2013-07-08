package uk.ac.ebi.pamela.layoutpipeline;

import com.sri.biospice.warehouse.schema.DataSet;
import com.sri.biospice.warehouse.schema.object.Chemical;
import com.sri.biospice.warehouse.schema.object.Pathway;
import com.sri.biospice.warehouse.schema.object.Reaction;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.pamela.layoutpipeline.bwh.DataSetSelector;
import uk.ac.ebi.pamela.layoutpipeline.pathway.*;
import uk.ac.ebi.pamela.layoutpipeline.reaction.BWHConnectivityBasedCurrencyDecider;
import uk.ac.ebi.pamela.layoutpipeline.reaction.CurrencyCompoundDecider;
import uk.ac.ebi.pamela.layoutpipeline.utils.PathwayMonitor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the PathwayListRetriever and the ReactionListRetriever for PAMELA. The retrieval
 * of data is based on pathways rather than small molecules. Given a ReactionListRetriever type invocation, where the
 * query is a pair small molecule-organism, this class retrieves the pathways in that organism where the small molecule
 * participates.
 *
 * Currently this RxnListRetriever is bound to use the @{link PAMELAPath2ReconsPreUnif} pathway to reconstruction maker
 * which is based on the pathways of non-unified sets. This operates by retrieving small molecules and reactions from
 * the organism unified set, but then retrieving the pathways from the source sets of that unification (as we currently
 * don't have a good unification of pathways). The currency decider and the pathway getter are also hard coded.
 *
 * TODO These hard coded dependencies should be solved by dependency injection.
 *
 * For the filtering of pathways retrieved, and {@link PathwayNoFlightChecker} implementation can be set (currently the
 * only one is based on size). The pathway checker should be more of a list than a single entity (TODO).
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 20/6/13
 * Time: 16:20
 */
public class PAMELAPathwayBasedRxnListRetriever implements ReactionListRetriever, PathwayListRetriever<Pathway> {

    private static final Logger LOGGER = Logger.getLogger(PAMELAPathwayBasedRxnListRetriever.class);

    private DataSet ds;
    private DataSetSelector dsSel;
    private CurrencyCompoundDecider<Chemical, Reaction> currencyDecider;
    private PathwayGetter<Pathway> pathwayGetter;
    private Pathway2Reconstruction<Pathway> reconsMaker;
    private PathwayNoFlightChecker noFlightChecker;

    /**
     * Initializes the rxn list retriever with a data set selector and a Taxonomy identifier for the desired organism.
     *
     * @param dsSel data set selector
     * @param organism {@link Taxonomy} identifier for the organism.
     */
    public PAMELAPathwayBasedRxnListRetriever(DataSetSelector dsSel, Taxonomy organism) {
        this.dsSel = dsSel;
        if (this.dsSel.hasDataSetForOrganism(organism)) {
            this.ds = dsSel.getDataSetForOrganism(organism);
            this.currencyDecider = new BWHConnectivityBasedCurrencyDecider(ds);
            this.pathwayGetter = new PAMELAPreUnificationPathwayGetter(ds);
            this.reconsMaker = new PAMELAPath2ReconsPreUnif(ds,organism,this.currencyDecider);
        }
    }

    /**
     * Sets the {@link PathwayNoFlightChecker}, which allows to ban pathways from being included in the final layout,
     * due to criteria implemented in the checker.
     *
     * @param checker
     */
    public void setPathwayNoFlightChecker(PathwayNoFlightChecker<Pathway> checker) {
        this.noFlightChecker = checker;
    }


    /**
     * {@inheritDoc}
     *
     * This implementation retrieves all pathways for the given query (small molecule-organism) and turns them into
     * individual {@link Reconstruction}s.
     */
    @Override
    public List<Reconstruction> getReactionsAsReconstructions(Query query) {
        // first query the database with the query, as cross reference, to obtain the pre unification pathways.
        Collection<Pathway> pathways = pathwayGetter.getPathways(query);
        // then for each pathway resulting, make a new reconstruction
        List<Reconstruction> reconstructions = new LinkedList<Reconstruction>();
        int index = 0;
        for (Pathway pathway : pathways) {
            if(!isPathwaySuitableForLayout(pathway)) {
                LOGGER.info("Skipping pathway "+pathway.getName()+" due to no flight checker.");
                continue;
            }
            LOGGER.info("Processing pathway : "+pathway.getName());

            reconstructions.add(reconsMaker.getReconstruction(pathway));
            PathwayMonitor.getMonitor().register(query,pathway,index++);
        }
        return reconstructions;  //To change body of implemented methods use File | Settings | File Templates.
    }


    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Reconstruction getPathwayAsReconstructions(Pathway pathway) {
        Reconstruction recons = reconsMaker.getReconstruction(pathway);
        return recons;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Uses the internally set {@link PathwayNoFlightChecker} to check whether the pathway is suitable for layout.
     *
     * TODO this probably shouldn't be a responsaility of the class, and should be refactored out. It currently gives
     * TODO congruence in terms of allowing to use the same checker for this class and others involved in the process.
     *
     * @param pathway
     * @return
     */
    @Override
    public boolean isPathwaySuitableForLayout(Pathway pathway) {
        return !(noFlightChecker!=null && noFlightChecker.checkNoFlight(pathway));
    }
}

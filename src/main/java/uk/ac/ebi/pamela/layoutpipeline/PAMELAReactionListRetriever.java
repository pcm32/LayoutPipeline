/*
 * Copyright (C) 2013 EMBL-EBI
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.pamela.layoutpipeline;

import org.apache.log4j.Logger;
import uk.ac.ebi.pamela.layoutpipeline.bwh.DataSetSelector;
import com.sri.biospice.warehouse.schema.DataSet;
import com.sri.biospice.warehouse.schema.object.Chemical;
import com.sri.biospice.warehouse.schema.object.Reaction;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.pamela.layoutpipeline.reaction.BWHConnectivityBasedCurrencyDecider;
import uk.ac.ebi.pamela.layoutpipeline.reaction.BWHDummyMainCompDecider;
import uk.ac.ebi.pamela.layoutpipeline.reaction.CurrencyCompoundDecider;
import uk.ac.ebi.pamela.layoutpipeline.reaction.MainCompoundDecider;
import uk.ac.ebi.pamela.layoutpipeline.reaction.PAMELARecursiveReactionGetter;
import uk.ac.ebi.pamela.layoutpipeline.utils.ReactionRecursionDepthMonitor;
import uk.ac.ebi.warehouse.util.ChemicalUtil;

/**
 * @name PAMELAReactionListRetriever
 * @date 2013.03.19
 * @version $Rev$ : Last Changed $Date$
 * @author Pablo Moreno <pablacious at users.sf.net>
 * @author $Author$ (this version)
 * @brief ...class description...
 *
 */
public class PAMELAReactionListRetriever extends AbstractReactionListRetriever implements ReactionListRetriever {

    private static final Logger LOGGER = Logger.getLogger(PAMELAReactionListRetriever.class);

    private DataSet ds;
    private DataSetSelector dsSel;
    private Integer reactionDepth;
    private CurrencyCompoundDecider<Chemical, Reaction> currencyDecider;
    private MainCompoundDecider<Chemical, Reaction> mainCompoundDecider;

    public PAMELAReactionListRetriever(DataSetSelector dsSel, Taxonomy organism, Integer reactionDepth) throws IOException, SQLException {        
        this.dsSel = dsSel;
        this.reactionDepth = reactionDepth;
        if (this.dsSel.hasDataSetForOrganism(organism)) {
            this.ds = dsSel.getDataSetForOrganism(organism);
            this.currencyDecider = new BWHConnectivityBasedCurrencyDecider(ds);
            this.mainCompoundDecider = new BWHDummyMainCompDecider();
        }
    }

    Iterable<MetabolicReaction> getReactions(Query query) {

        List<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();

        // Search for chemical identifier in data set
        if (ds != null) {
            // Get small molecule that has the provided Cross reference.
            try {
                List<Chemical> chemsWithId = ChemicalUtil.getChemicalWithCrossReference(query.getChemicalIdentifier().getAccession(),
                        query.getChemicalIdentifier().getShortDescription(), ds.getWID());

                PAMELARecursiveReactionGetter rxnGetter = new PAMELARecursiveReactionGetter(ds, query.getOrganismIdentifier(), reactionDepth, currencyDecider, mainCompoundDecider);
                rxnGetter.setMaxReactions(30);

                if(chemsWithId.size()>1) {
                    LOGGER.warn("Retrieved more than one BWH Chemical for the defined query : "+query.getChemicalIdentifier().getAccession());
                }
                for (Chemical chemical : chemsWithId) {
                    if(chemsWithId.size()>1)
                        LOGGER.warn("Retrieving reactions for chemical WID "+chemical.getWID());
                    reactions.addAll(rxnGetter.getReactions(chemical));
                    ReactionRecursionDepthMonitor.register(query,rxnGetter.getLastDepthUsed());
                }
            } catch (SQLException e) {
                LOGGER.error("Problems retrieving reactions : ",e);
            }
        }

        return reactions;
    }
}

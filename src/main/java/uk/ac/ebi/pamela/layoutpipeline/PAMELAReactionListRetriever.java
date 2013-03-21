/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
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

import uk.ac.ebi.pamela.layoutpipeline.pamela.DataSetSelector;
import com.sri.biospice.warehouse.database.Warehouse;
import com.sri.biospice.warehouse.schema.DataSet;
import com.sri.biospice.warehouse.schema.object.Chemical;
import com.sri.biospice.warehouse.schema.object.Reaction;
import uk.ac.ebi.metabolomes.biowh.BioChemicalReactionSetProviderFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.metabolomes.biowh.BiochemicalReactionSetProvider;
import uk.ac.ebi.metabolomes.biowh.BiowhPooledConnection;
import uk.ac.ebi.pamela.layoutpipeline.reaction.PAMELARecursiveReactionGetter;
import uk.ac.ebi.warehouse.util.ChemicalUtil;

/**
 * @name    PAMELAReactionListRetriever
 * @date    2013.03.19
 * @version $Rev$ : Last Changed $Date$
 * @author  Pablo Moreno <pablacious at users.sf.net>
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class PAMELAReactionListRetriever extends AbstractReactionListRetriever implements ReactionListRetriever {

    private DataSet ds;
    private DataSetSelector dsSel;
    private Integer reactionDepth;
    
    
    public PAMELAReactionListRetriever(DataSetSelector dsSel) throws IOException, SQLException {
        BiowhPooledConnection bwhc = new BiowhPooledConnection();
        Warehouse bwh = bwhc.getWarehouseObject();
        this.dsSel = dsSel;
    }
    
    
    protected Iterable<MetabolicReaction> getReactions(Query query) {
        this.ds = dsSel.getDataSetForOrganism(query.getOrganismIdentifier());
        
        List<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();
        
        // Search for chemical identifier in data set
        
        BiochemicalReactionSetProvider provider = 
                BioChemicalReactionSetProviderFactory.getBiochemicalReactionSetProvider(ds);
        
        // Get small molecule that has the provided Cross reference.
        /*List<Chemical> chemsWithId = ChemicalUtil.getChemicalWithCrossReference(query.getChemicalIdentifier().getAccession(), 
                query.getChemicalIdentifier().getShortDescription(), ds.getWID());
        
        PAMELARecursiveReactionGetter rxnGetter = new PAMELARecursiveReactionGetter(3,);
        
        for (Chemical chemical : chemsWithId) {
            reactions.addAll(rxnGetter.getReactions(chemical));
        }*/
        
        return null;
        
    }


}

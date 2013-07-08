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
package uk.ac.ebi.pamela.layoutpipeline.reaction;

import com.sri.biospice.warehouse.schema.DataSet;
import com.sri.biospice.warehouse.schema.object.Chemical;
import com.sri.biospice.warehouse.schema.object.Reaction;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.metabolomes.biowh.BiowhPooledConnection;
import uk.ac.ebi.metabolomes.biowh.DataSetProvider;
import uk.ac.ebi.pamela.layoutpipeline.bwh.DataSetSelector;
import uk.ac.ebi.pamela.layoutpipeline.bwh.NewestUnifiedDataSetSelector;
import uk.ac.ebi.warehouse.util.ChemicalUtil;

/**
 *
 * @author Pablo Moreno <pablacious at users.sf.net>
 */
public class PAMELARecursiveReactionGetterTest {
    
    public PAMELARecursiveReactionGetterTest() throws IOException, SQLException {
        BiowhPooledConnection bwhc = new BiowhPooledConnection();
        DataSetProvider.loadPropsForCurrentSchema();
    }

    @Test
    public void testGetReactions() throws SQLException {
        System.out.println("getReactions");
        DataSetSelector dsSel = new NewestUnifiedDataSetSelector();
        Taxonomy ident = new Taxonomy();
        ident.setAccession("9606");
        if (dsSel.hasDataSetForOrganism(ident)) {
            
            DataSet ds = dsSel.getDataSetForOrganism(ident);
            CurrencyCompoundDecider<Chemical,Reaction> currencyDec = new BWHConnectivityBasedCurrencyDecider(ds);
            //DataSet ds = new DataSet(5l);
            List<Chemical> desmosterolInstances = ChemicalUtil.getChemicalWithCrossReference("CHEBI:17737",
                    "CHEBI", ds.getWID());
            Chemical chem = desmosterolInstances.get(0);
            
            PAMELARecursiveReactionGetter getter = new PAMELARecursiveReactionGetter(ds,ident, 3, currencyDec, new BWHDummyMainCompDecider());
            
            Collection<MetabolicReaction> rxns = getter.getReactions(chem);
            
            assertNotNull(rxns);
            assertTrue(rxns.size()>0);
            
            System.out.println("Reactions retrieved for demosterol : "+rxns.size());
        }
        
    }
}
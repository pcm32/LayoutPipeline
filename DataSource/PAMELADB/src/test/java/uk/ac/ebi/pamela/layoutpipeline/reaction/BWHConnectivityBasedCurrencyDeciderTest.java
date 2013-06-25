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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.metabolomes.biowh.BiowhPooledConnection;
import uk.ac.ebi.pamela.layoutpipeline.bwh.DataSetSelector;
import uk.ac.ebi.pamela.layoutpipeline.bwh.NewestUnifiedDataSetSelector;
import uk.ac.ebi.warehouse.util.ChemicalUtil;
import uk.ac.ebi.warehouse.util.ReactionUtil;

/**
 *
 * @author Pablo Moreno <pablacious at users.sf.net>
 */
public class BWHConnectivityBasedCurrencyDeciderTest {

    public BWHConnectivityBasedCurrencyDeciderTest() throws IOException, SQLException {
        BiowhPooledConnection bwhc = new BiowhPooledConnection();
    }

    /**
     * Test of getCurrencyMetabolites method, of class BWHConnectivityBasedCurrencyDecider.
     */
    @Test
    public void testGetCurrencyMetabolites() throws SQLException {
        System.out.println("getCurrencyMetabolites");
        DataSetSelector dsSel = new NewestUnifiedDataSetSelector();
        Taxonomy ident = new Taxonomy();
        ident.setAccession("9606");
        if (dsSel.hasDataSetForOrganism(ident)) {
            DataSet ds = dsSel.getDataSetForOrganism(ident);
            //DataSet ds = new DataSet(5l);
            List<Chemical> pyruvateInstances = ChemicalUtil.getChemicalWithCrossReference("CHEBI:15361",
                    "CHEBI", ds.getWID());
            Chemical chem = pyruvateInstances.get(0);
            List<Reaction> rxns = new ArrayList<Reaction>(ReactionUtil.getBWHReactionsForChemical(chem));
            for (Reaction rxn : rxns) {
                BWHConnectivityBasedCurrencyDecider instance = new BWHConnectivityBasedCurrencyDecider(ds);

                Collection<Chemical> result = instance.getCurrencyMetabolites(rxn);
                assertNotNull(result);
                assertTrue(result.size() > 0);

                System.out.println("For reaction WID "+rxn.getWID()+" "+rxn.getName());
                
                for (Chemical chemical : result) {
                    System.out.println(chemical.getName());
                }
            }
        }
    }
}
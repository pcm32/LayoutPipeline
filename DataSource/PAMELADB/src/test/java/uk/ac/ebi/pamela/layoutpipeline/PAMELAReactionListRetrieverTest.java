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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.metabolomes.biowh.BiowhPooledConnection;
import uk.ac.ebi.metabolomes.biowh.DataSetProvider;
import uk.ac.ebi.pamela.layoutpipeline.bwh.DataSetSelector;
import uk.ac.ebi.pamela.layoutpipeline.bwh.NewestUnifiedDataSetSelector;

/**
 *
 * @author Pablo Moreno <pablacious at users.sf.net>
 */
public class PAMELAReactionListRetrieverTest {
    
    public PAMELAReactionListRetrieverTest() throws IOException, SQLException {
        BiowhPooledConnection bwhc = new BiowhPooledConnection();
        DataSetProvider.loadPropsForCurrentSchema();
    }

    @Test
    public void testSomeMethod() throws IOException, SQLException {
        Taxonomy ident = new Taxonomy();
        ident.setAccession("9606");
        PAMELAReactionListRetriever ret = new PAMELAReactionListRetriever(new NewestUnifiedDataSetSelector(), ident, 3);
        Query query = new SimpleOrgMolQuery("CHEBI:17737", "9606");
        List<Reconstruction> recs = ret.getReactionsAsReconstructions(query);
        
        assertNotNull(recs);
        assertTrue(recs.size()>0);
        System.out.println("Reactions: "+recs.get(0).reactome().size());
    }
}
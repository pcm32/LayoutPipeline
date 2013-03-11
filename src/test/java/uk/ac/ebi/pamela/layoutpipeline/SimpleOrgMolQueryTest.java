package uk.ac.ebi.pamela.layoutpipeline;


import org.junit.Test;

import static junit.framework.Assert.* ;
/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 27/02/2013
 * Time: 09:58
 * To change this template use File | Settings | File Templates.
 */
public class SimpleOrgMolQueryTest {
    @Test
    public void testGetters() throws Exception {

        SimpleOrgMolQuery query = new SimpleOrgMolQuery("chebiid", "3130");

        assertEquals("CHEBI ID initialization test" , "chebiid", query.getChemicalIdentifier().getAccession());

        assertEquals("TAX ID initialization test" , "3130", query.getOrganismIdentifier().getAccession());

    }
}

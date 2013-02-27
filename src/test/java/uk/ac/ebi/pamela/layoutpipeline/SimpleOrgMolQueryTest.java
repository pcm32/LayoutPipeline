package uk.ac.ebi.pamela.layoutpipeline;


import static junit.framework.Assert.* ;
/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 27/02/2013
 * Time: 09:58
 * To change this template use File | Settings | File Templates.
 */
public class SimpleOrgMolQueryTest {
    public void testGetters() throws Exception {

        SimpleOrgMolQuery query = new SimpleOrgMolQuery("chebiId", "taxid");

        assertEquals("CHEBI ID initialization test" , "chebiid", query.getChemicalIdentifier());

        assertEquals("TAX ID initialization test" , "taxid", query.getOrganismIdentifier());

    }
}

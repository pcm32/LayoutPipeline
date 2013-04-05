package uk.ac.ebi.pamela.layoutpipeline.exec;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.pamela.layoutpipeline.*;
import uk.ac.ebi.pamela.layoutpipeline.utils.PropertiesUtil;

/**
 * User: conesa
 * Date: 05/04/2013
 * Time: 14:56
 */
public class PipelineExecRheaTest {
    @Test
    public void testRun() throws Exception {

        Query query = new SimpleOrgMolQuery("CHEBI:71045","");

        PipelineExec pipeline = new PipelineExec(query);

        ReactionListRetriever retriever = new RheaReactionListRetriever(null);


        LayoutAlgorithm layoutAlg = SBWAutoLayouterAlgorithm() ;

        LayoutRenderer layoutRend = SBWRenderer();


        String imageOutputPath = PropertiesUtil.getPreference("PipeLineExecRheaTestOutputPath", "/Users/conesa");

        




    }
}

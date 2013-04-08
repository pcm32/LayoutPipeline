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

        Query query = new SimpleOrgMolQuery("CHEBI:27732","1");

        String imageOutputPath = PropertiesUtil.getPreference("PipeLineExecRheaTestOutputPath", "/Users/conesa/");

        String layoutExe = PropertiesUtil.getPreference("pathToSaveLayoutEXE","/Users/conesa/Development/AutoLayoutWithoutLibSBML/SaveLayout.exe");
        String renderExe = PropertiesUtil.getPreference("pathToRendererEXE" , "/Applications/SBW/lib/SBMLLayoutReader.exe");

        // No specie information so far...
        ReactionListRetriever retriever = new RheaReactionListRetriever(null);

        LayoutAlgorithm layoutAlg = new SBWAutoLayouterAlgorithm(layoutExe) ;

        SBWRendererOptions opts = new SBWRendererOptions();
        opts.setOutputFormat(SBWRendererOptions.Format.svg);

        LayoutRenderer layoutRend = new SBWRenderer(renderExe, opts);

        PipelineExec pipeline = new PipelineExec(query, retriever,layoutAlg,layoutRend,2, 3, imageOutputPath);

        pipeline.run();








    }
}

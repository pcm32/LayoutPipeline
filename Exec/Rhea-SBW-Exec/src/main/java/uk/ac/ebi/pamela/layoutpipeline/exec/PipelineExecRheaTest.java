package uk.ac.ebi.pamela.layoutpipeline.exec;

import com.google.common.io.Files;
import uk.ac.ebi.pamela.layoutpipeline.*;
import uk.ac.ebi.pamela.layoutpipeline.RheaReactionListRetriever;
import uk.ac.ebi.pamela.layoutpipeline.utils.PropertiesUtil;
import uk.ac.ebi.pamela.layoutpipeline.utils.SBWAlgorithmPrefsSetter;
import uk.ac.ebi.pamela.layoutpipeline.utils.SBWRendererPrefsSetter;

/**
 * User: conesa
 * Date: 05/04/2013
 * Time: 14:56
 */
public class PipelineExecRheaTest {

    public static void main() throws Exception {

        Query query = new SimpleOrgMolQuery("CHEBI:57972","303");

        String imageOutputPath = Files.createTempDir().getAbsolutePath();

        //String layoutExe = PropertiesUtil.getPreference("pathToSaveLayoutEXE","/Users/conesa/Development/AutoLayoutWithoutLibSBML/SaveLayout.exe");
        String layoutExe = PropertiesUtil.getPreference(SBWAlgorithmPrefsSetter.SBWAlgPrefsField.pathToSaveLayoutEXE,"/Users/conesa/Development/AutoLayoutWithoutLibSBML/SaveLayout.exe");
        String renderExe = PropertiesUtil.getPreference(SBWRendererPrefsSetter.SBWRendererPrefsField.pathToRendererEXE , "/Applications/SBW/lib/SBMLLayoutReader.exe");

        // No specie information so far...
        ReactionListRetriever retriever = new RheaReactionListRetriever();

        LayoutAlgorithm layoutAlg = new SBWAutoLayouterAlgorithm(layoutExe) ;

        SBWRendererOptions opts = new SBWRendererOptions();
        opts.setOutputFormat(SBWRendererOptions.Format.svg);

        LayoutRenderer layoutRend = new SBWRenderer(renderExe, opts);

        PipelineExec pipeline = new PipelineExec(query, retriever,layoutAlg,layoutRend,2, 3, imageOutputPath);

        pipeline.run();








    }
}

package uk.ac.ebi.pamela.layoutpipeline.exec;

import com.google.common.io.Files;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.pamela.layoutpipeline.*;
import uk.ac.ebi.pamela.layoutpipeline.RheaReactionListRetriever;
import uk.ac.ebi.pamela.layoutpipeline.utils.PropertiesUtil;
import uk.ac.ebi.pamela.layoutpipeline.utils.ReactionRecursionDepthMonitor;
import uk.ac.ebi.pamela.layoutpipeline.utils.SBWAlgorithmPrefsSetter;
import uk.ac.ebi.pamela.layoutpipeline.utils.SBWRendererPrefsSetter;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * User: conesa
 * Date: 05/04/2013
 * Time: 14:56
 */
public class PipelineExecRhea {

    public static void main(String[] args) throws Exception {

        String pathToOut = args[0];
        String pathToChemicalIdentifierList = args[1];

        //String layoutExe = PropertiesUtil.getPreference("pathToSaveLayoutEXE","/Users/conesa/Development/AutoLayoutWithoutLibSBML/SaveLayout.exe");
        String layoutExe = PropertiesUtil.getPreference(SBWAlgorithmPrefsSetter.SBWAlgPrefsField.pathToSaveLayoutEXE,"/Users/conesa/Development/AutoLayoutWithoutLibSBML/SaveLayout.exe");
        String renderExe = PropertiesUtil.getPreference(SBWRendererPrefsSetter.SBWRendererPrefsField.pathToRendererEXE , "/Applications/SBW/lib/SBMLLayoutReader.exe");

        // No specie information so far...
        ReactionListRetriever retriever = new RheaReactionListRetriever(2);
        SBWAutoLayouterAlgorithmOptions optionLayout = new SBWAutoLayouterAlgorithmOptions();
        optionLayout.setGravityFactor(120);
        optionLayout.setMagnetism(true);

        LayoutAlgorithm layoutAlg = new SBWAutoLayouterAlgorithm(layoutExe,optionLayout) ;

        SBWRendererOptions opts = new SBWRendererOptions();
        opts.setOutputFormat(SBWRendererOptions.Format.png);

        LayoutRenderer layoutRend = new SBWRenderer(renderExe, opts);

        ReactionRecursionDepthMonitor.getMonitor("Rhea").setOutputPath(pathToOut);

        BufferedReader reader = new BufferedReader(new FileReader(pathToChemicalIdentifierList));

        String line;
        int count = 0;
        Long startTime = System.currentTimeMillis();
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split("\t");
            ChEBIIdentifier identObj = new ChEBIIdentifier(tokens[0]);
            String organismTaxId = tokens[1];
            Query query = new SimpleOrgMolQuery(identObj.getAccession(), organismTaxId);
            PipelineExec pipeline = new PipelineExec(query, retriever,layoutAlg,layoutRend,2, 3, pathToOut);
            pipeline.run();
            count++;
            Float speed = (count + 0f) / ((System.currentTimeMillis() - startTime) / 1000);
            if (count % 10 == 0) {
                System.out.println("Done " + count + " identifiers " + speed + " [idents/sec]");
            }
        }
        Float speed = (count + 0f) / ((System.currentTimeMillis() - startTime) / 1000);
        System.out.println("Done " + count + " identifiers " + speed + " [idents/sec]");
        ReactionRecursionDepthMonitor.getMonitor().close();




    }
}

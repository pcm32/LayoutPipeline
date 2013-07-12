package uk.ac.ebi.pamela.layoutpipeline.renderer.batik;

import com.google.common.io.Files;
import org.junit.Test;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

import java.io.File;
import java.io.InputStream;

/**
 * User: conesa
 * Date: 09/07/2013
 * Time: 10:01
 */
public class BatikRendererTest {
    @Test
    public void testProduceRender() throws Exception {

        BatikRenderer batikRenderer = new BatikRenderer();

        File tempDir = new File ("/opt/apache-tomcat-7.0.42/webapps/svg/");//Files.createTempDir();

        // Read the input SBML document from the resources folder...
        InputStream sbmlLayout = BatikRendererTest.class.getClassLoader().getResourceAsStream("sbml_layout_1.xml");

        SBMLReader reader = new SBMLReader();

        SBMLDocument sbmlDoc = reader.readSBMLFromStream(sbmlLayout);

        batikRenderer.produceRender(sbmlDoc,tempDir.getAbsolutePath());


    }
}

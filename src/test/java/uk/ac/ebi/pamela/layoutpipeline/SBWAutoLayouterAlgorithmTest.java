package uk.ac.ebi.pamela.layoutpipeline;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 28/02/2013
 * Time: 09:57
 * To change this template use File | Settings | File Templates.
 */
public class SBWAutoLayouterAlgorithmTest {
    static Properties props = new Properties();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        InputStream fileIn = SBWAutoLayouterAlgorithmTest.class.getClassLoader().getResourceAsStream("test.properties");
        props.load(fileIn);

    }

    @Test
    public void testGetLayoutedSBML() throws Exception {


        // Create the options for the layouting
        SBWAutoLayouterAlgorithmOptions options = new SBWAutoLayouterAlgorithmOptions("");

        // Read the input SBML document from the resources folder...
        InputStream sbmlNoLayout = SBWAutoLayouterAlgorithmTest.class.getClassLoader().getResourceAsStream("sbml_noLayout.xml");

        SBMLReader reader = new SBMLReader();

        SBMLDocument sbmlDoc = reader.readSBMLFromStream(sbmlNoLayout);

        SBWAutoLayouterAlgorithm alg = new SBWAutoLayouterAlgorithm(props.getProperty("pathToSaveLayoutEXE"),options);

        // Call the Layout method
        SBMLDocument sbmlLayouted = alg.getLayoutedSBML(sbmlDoc);

        // Verify is not null
        assertNotNull(sbmlLayouted);

        String layoutinfo = sbmlLayouted.getModel().getAnnotation().getNonRDFannotation();

        // Verify it has layout information
        assertTrue("Check if layouted SBML has layout information", layoutinfo.indexOf("listOfLayouts") != -1);


    }

    @Test
    public void testMonoTest(){


        assertTrue(SBWAutoLayouterAlgorithm.testMono());

    }

    @Test
    public void testLayouterTest(){


        assertTrue(SBWAutoLayouterAlgorithm.testLayouter(props.getProperty("pathToSaveLayoutEXE")));

    }
}

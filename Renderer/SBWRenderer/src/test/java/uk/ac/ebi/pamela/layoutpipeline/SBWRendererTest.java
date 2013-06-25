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

import org.junit.BeforeClass;
import org.junit.Test;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.sbml.jsbml.SBMLWriter;

/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 28/02/2013
 * Time: 09:57
 * To change this template use File | Settings | File Templates.
 */
public class SBWRendererTest {
    static Properties props = new Properties();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        InputStream fileIn = SBWRendererTest.class.getClassLoader().getResourceAsStream("test.properties");
        props.load(fileIn);

    }

    @Test
    public void testGetRenderedSBML() throws Exception {


        // Create the options for the layouting
        SBWRendererOptions options = new SBWRendererOptions("") ;

        // Read the input SBML document from the resources folder...
        InputStream sbmlNoLayout = SBWRendererTest.class.getClassLoader().getResourceAsStream("sbml_Layout.xml");
        File outPutFile = new File("./RenderingOutput.png");

        if (outPutFile.exists()) outPutFile.delete();

        SBMLReader reader = new SBMLReader();

        SBMLDocument sbmlDoc = reader.readSBMLFromStream(sbmlNoLayout);

        SBWRenderer alg = new SBWRenderer(props.getProperty("pathToRendererEXE"),options) ;

        // Call the Layout method
        //alg.produceRender(sbmlDoc, outPutFile.getAbsolutePath());

        // Verify there is a file
        //assertTrue("Check if the output file has been generated", outPutFile.exists());


    }
    
//    @Test
//    public void testReadModel() throws XMLStreamException, IOException {
//        InputStream sbmlLayoutSideComps = SBWRendererTest.class.getClassLoader().getResourceAsStream("LayoutWithSideComps.xml");
//        SBMLReader reader = new SBMLReader();
//        SBMLDocument doc = reader.readSBMLFromStream(sbmlLayoutSideComps);
//        File output = File.createTempFile("modelWithLayoutAndSide", ".xml");
//        System.out.println("Writing to :"+output.getAbsolutePath());
//        SBMLWriter writer = new SBMLWriter();
//        writer.write(doc, output);
//    }

    @Test
    public void testMonoTest(){


        assertTrue(SBWAutoLayouterAlgorithm.testMono());

    }

    @Test
    public void testLayouterTest(){


        assertTrue(SBWAutoLayouterAlgorithm.testLayouter(props.getProperty("pathToSaveLayoutEXE")));

    }
}

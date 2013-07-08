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

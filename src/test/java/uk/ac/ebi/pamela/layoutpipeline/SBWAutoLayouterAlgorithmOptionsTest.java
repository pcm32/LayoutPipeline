package uk.ac.ebi.pamela.layoutpipeline;


import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 27/02/2013
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class SBWAutoLayouterAlgorithmOptionsTest {
    @Test
    public void testOptionFileConstructor() throws Exception {

        SBWAutoLayouterAlgorithmOptions options = new SBWAutoLayouterAlgorithmOptions("file");

        assertEquals("Test options for only file constructor", " -f file", options.toString());

    }

    @Test
    public void testOptionFileOutputAndDimensionsDirectoryConstructor() throws Exception {

        SBWAutoLayouterAlgorithmOptions options = new SBWAutoLayouterAlgorithmOptions("file", "output",10,20);

        assertEquals("Test options for constructor with dimensions", " -f file -o output -w 10 -h 20", options.toString());

    }

    @Test
    public void testOptionConstructorFull(){

        SBWAutoLayouterAlgorithmOptions options = new SBWAutoLayouterAlgorithmOptions("file", "output", null, 0, 0, 0, 0, false, false, false, false, false, false, false,false);

        assertEquals("Test options for full constructor with few parameters", " -f file -o output", options.toString());

    }

}

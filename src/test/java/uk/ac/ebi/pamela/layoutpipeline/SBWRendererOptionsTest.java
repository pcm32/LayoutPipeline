package uk.ac.ebi.pamela.layoutpipeline;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 27/02/2013
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class SBWRendererOptionsTest {
    @Test
    public void testOptionFileConstructor() throws Exception {

        SBWRendererOptions options = new SBWRendererOptions("file");

        assertEquals("Test options for only file constructor", " --f file", options.toString());

    }

    @Test
    public void testOptionFileOutputAndDimensionsDirectoryConstructor() throws Exception {

        SBWRendererOptions options = new SBWRendererOptions("file", "output",10,20);

        assertEquals("Test options for constructor with dimensions", " --f file --out output --dimensions 10.0 20.0", options.toString());

    }


}

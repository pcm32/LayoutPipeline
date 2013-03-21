package uk.ac.ebi.pamela.layoutpipeline.utils;

import static org.junit.Assert.*;
import org.junit.Test;
/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 20/03/2013
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesUtilsTest {

    @Test
    public void testProperties(){

        String user = PropertiesUtil.getProperty("rhea.username");
        assertTrue(PropertiesUtil.getProperty("rhea.username") != null);
        assertTrue(PropertiesUtil.getProperty("rhea.password") != null);
        assertTrue(PropertiesUtil.getProperty("rhea.url") != null);

    }

}

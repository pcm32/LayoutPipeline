package uk.ac.ebi.pamela.layoutpipeline.utils;

import com.google.common.io.Files;
import junit.framework.Assert;
import org.junit.Test;
import uk.ac.ebi.pamela.layoutpipeline.SimpleOrgMolQuery;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 21/6/13
 * Time: 08:28
 * To change this template use File | Settings | File Templates.
 */
public class PathwayMonitorTest {
    @Test
    public void testRegister() throws Exception {
        String path = Files.createTempDir().getAbsolutePath();
        System.out.println("Output path for pathway monitor : "+path);
        PathwayMonitor.getMonitor().setOutputPath(path);
        PathwayMonitor.getMonitor().register(new SimpleOrgMolQuery("CHEBI:1234", "9606"), "TestPathway1", 0);
        PathwayMonitor.getMonitor().register(new SimpleOrgMolQuery("CHEBI:12345","9606"),"TestPathway2",2);
        PathwayMonitor.getMonitor().close();
        String line = Files.readFirstLine(new File(path + File.separator + PathwayMonitor.getMonitor().getFileName()), Charset.defaultCharset());
        Assert.assertNotNull(line);
        System.out.println(line);
    }

}

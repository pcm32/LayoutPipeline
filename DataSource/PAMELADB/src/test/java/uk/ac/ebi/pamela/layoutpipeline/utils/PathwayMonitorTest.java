package uk.ac.ebi.pamela.layoutpipeline.utils;

import com.google.common.io.Files;
import com.sri.biospice.warehouse.database.PooledWarehouseManager;
import com.sri.biospice.warehouse.database.Warehouse;
import com.sri.biospice.warehouse.schema.TableFactory;
import com.sri.biospice.warehouse.schema.object.Pathway;
import junit.framework.Assert;
import org.junit.Test;
import uk.ac.ebi.metabolomes.biowh.BiowhPooledConnection;
import uk.ac.ebi.pamela.layoutpipeline.SimpleOrgMolQuery;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

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
        BiowhPooledConnection con = new BiowhPooledConnection();
        Warehouse wh = PooledWarehouseManager.getWarehouse();
        PreparedStatement ps = wh.createPreparedStatement("SELECT * FROM Pathway LIMIT 2");
        List<Pathway> paths = new ArrayList<Pathway>(TableFactory.loadTables(ps.executeQuery(),TableFactory.PATHWAY));
        PathwayMonitor.getMonitor().register(new SimpleOrgMolQuery("CHEBI:1234", "9606"),paths.get(0),0);
        PathwayMonitor.getMonitor().register(new SimpleOrgMolQuery("CHEBI:12345","9606"),paths.get(1),2);
        PathwayMonitor.getMonitor().close();
        String line = Files.readFirstLine(new File(path + File.separator + PathwayMonitor.getMonitor().getFileName()), Charset.defaultCharset());
        Assert.assertNotNull(line);
        System.out.println(line);
        wh.close();
    }

}

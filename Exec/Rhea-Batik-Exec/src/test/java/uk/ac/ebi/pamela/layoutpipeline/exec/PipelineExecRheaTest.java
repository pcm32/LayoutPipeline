package uk.ac.ebi.pamela.layoutpipeline.exec;

import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * User: conesa
 * Date: 08/08/2013
 * Time: 11:42
 */
public class PipelineExecRheaTest {
    @Test
    public void testMain() throws Exception {



        URL ids = PipelineExecRheaTest.class.getClassLoader().getResource("chebiids.txt");

        File output = Files.createTempDir();


        System.out.println("Output folder: " + output.getAbsolutePath());


        PipelineExecRhea.main(new String[]{output.getAbsolutePath()+ "/", ids.getFile()});

    }
}

/**
 * SBWAutoLayouterAlgorithm.java
 *
 * 2013.02.25
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.pamela.layoutpipeline;


import com.google.common.io.Files;
import org.apache.log4j.Logger;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;

import javax.xml.stream.XMLStreamException;
import java.io.*;

/**
 * @name    SBWAutoLayouterAlgorithm
 * @date    2013.02.25
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   This implementation uses the SBW mono based executable through a nasty linux exec to process the given 
 *          non layouted SBML doc.
 *
 */
public class SBWRenderer implements LayoutRenderer {

    private static final Logger LOGGER = Logger.getLogger( SBWRenderer.class );


    private String pathToRendererEXE;
    private String outputFilePrefix;
    private SBWRendererOptions options;

    /**
     * Executed example
     mono SBMLLayoutReader
     -f || --file <sbml file with layout>
     -o || --out <output filename>
     -s || --scale <scalefactor>
     -d || --dimensions <xdimensions> <ydimensions>

     If you don’t enter input and output file, the SBW module will be started.
     If the output filename ends in svg, an svg will be exported using the stylesheet.
     Otherwise an image will be saved (I recommend to use .png as extension for a lossless image)
     in the default size. That is equivalent to saving it with -s 1.
     Of course you are free to enter any float scaling factor.
     Instead of scale you can also enter the image dimensions that you would like to have.
     */


    /**
     *
     * @param sbmlWithLayout
     * @param outputFilePrefix
     */
    public void produceRender(SBMLDocument sbmlWithLayout, String outputFilePrefix){
        /**
         * This class needs to write the nonLayoutedSBMLDoc SBML document to a temporary directory, set up the command
         * line call for the SBW AutoLayouter, execute the call, and parse the resulting SBML document with layout,
         * producing an SBMLDocument object that has the layout on it.
         */

        this.outputFilePrefix = outputFilePrefix;

        // Test the environment is setup properly:
        if (!testSetup(pathToRendererEXE)){
            LOGGER.error("System is not properly setup. Either mono is not installed or Renderer EXE path is wrong. See log.");
            // TODO Throw an exception...otherwise how are we going to distinguish an error from a success.
        }

        // Request for a temporary folder
        File tempFolder = Files.createTempDir();

        LOGGER.info("Working on temporary folder: " + tempFolder.getAbsolutePath());
        File tempFileIn  = new File (tempFolder.getAbsolutePath() + "/sbmlIn.xml");

        // Set the output directory to the temporary directory...
        options.setDirectoryToSaveTo(outputFilePrefix);

        SBMLDocument output = null;

        // Save the SBMLDocument (without any layout information) in the filesystem to use it in the command line
        try {

            LOGGER.info("Writing sbml document to " + tempFileIn);
            // Write the Document object to the file system
            SBMLWriter.write(sbmlWithLayout,tempFileIn.getAbsolutePath(), this.getClass().getName(),"1.0");

            // Fill the file option with the saved file
            options.setFileToConvert(tempFileIn.getAbsolutePath());

            // Invoke the layouter through the command line
            makeTheCall();

            LOGGER.info("Output generated:  " + options.getDirectoryToSaveTo());


        } catch (XMLStreamException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    /**
     * Makes the command line call
     */
    private void makeTheCall() {

        Runtime rt = Runtime.getRuntime();
        Process proc;
        String commandLine = "mono " + pathToRendererEXE + options.toString();

        LOGGER.info("Command line to execute: " + commandLine);

        try {

            proc = rt.exec(commandLine);

            // Wait for the command to complete.
            int exitVal = proc.waitFor();

        } catch (Exception e) {
            LOGGER.info("Can't invoke command line --> " + commandLine + "\n" + e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public SBWRenderer(String pathToRendererEXE) {
        this(pathToRendererEXE, new SBWRendererOptions());
    }
    
    
    /**
     * default constructor
     */
    public SBWRenderer(String pathToRendererEXE, SBWRendererOptions options){

        this.pathToRendererEXE = pathToRendererEXE;
        this.options = options;

    }

    public static boolean testSetup(String pathToRenderer){

        if (testMono()){
            if (testLayouter(pathToRenderer)){
                return true;
            }
        }

        return false;
    }

    public static boolean testMono(){
        return testcommand("mono" , "--version");
    }

    public static boolean testLayouter(String pathToRenderer){        
        return true;
        //return testcommand("mono" , pathToRenderer);
    }

    public static boolean testcommand(String command, String argument) {

        ProcessBuilder proc = new ProcessBuilder(command, argument);

        LOGGER.info("Testing command: " + command + " " + argument);

        try {

            Process shell = proc.start();

            // To capture output from the shell
            InputStream shellIn = shell.getInputStream();

            // Wait for the shell to finish and get the return code
            int shellExitStatus = shell.waitFor();
            LOGGER.debug("call exit status" + shellExitStatus);

            String response = convertStreamToStr(shellIn);

            LOGGER.info(command + " " + argument + " successfully run. Test passed.");
            LOGGER.debug("\n" + response);
            shellIn.close();

            return true;

        } catch (Exception e) {
            LOGGER.info("Can't invoke command line --> " + command + " " + argument + "\n" + e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return false;

    }



    public static String convertStreamToStr(InputStream is) throws IOException {

        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        }
        else {
            return "";
        }
    }

}
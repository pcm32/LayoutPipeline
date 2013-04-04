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


import org.apache.log4j.Logger;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;

import com.google.common.io.Files;
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
public class SBWAutoLayouterAlgorithm implements LayoutAlgorithm {

    private static final Logger LOGGER = Logger.getLogger( SBWAutoLayouterAlgorithm.class );
    

    private String pathToSaveLayoutEXE;
    private SBWAutoLayouterAlgorithmOptions options;

    /**
     * Executed example
     * mono SaveLayout.exe -f /Applications/SBW/SBML\ Models/Jana_WolfGlycolysis.xml -l 20
     * 
     * produced:
     * sbw_auto_layouter_opt20.xml
     * 
     * Set up needed within user account.
     * 
     * SaveLayout.exe is part of AutoLayoutWithoutLibSBML.zip, this needs to be unziped and then copy libsbml* from 
     * the linux SBW/lib installation into the directory (SBW needs to be downloaded of course).
     * 
     * mono framework needs to be installed as well: www.mono-project.com/
     * 
     * This object should be configured with the desired parameters for SaveLayout.exe:
     * 
     * usage: SaveLayout -f <file to convert>
                 [-o <directory to save to>         ]
                 [-a <degree for auto aliasing>     ]
                 [-w <width     of the image to create>]
                 [-h <height of the image to create>]
                 [-g <double value of gravity factor>]
                 [-l <double value of edge length>]
                 [-nosbml]
                 [-grid]
                 [-magnetism]
                 [-boundary]
                 [-emptySet]
                 [-emptySets]
                 [-r || --removeExisting]
                 [-sourceSink]
                 [--bmp]
                 [--png]
                 [--fullJPG]
                 [--ps]
                 [--svg]
     */




    /**
     * 
     * @param nonLayoutedSBMLDoc
     * @return 
     */
    public SBMLDocument getLayoutedSBML(SBMLDocument nonLayoutedSBMLDoc) {
        /**
         * This class needs to write the nonLayoutedSBMLDoc SBML document to a temporary directory, set up the command 
         * line call for the SBW AutoLayouter, execute the call, and parse the resulting SBML document with layout, 
         * producing an SBMLDocument object that has the layout on it.
         */


        // Test the environment is setup properly:
        if (!testSetup(pathToSaveLayoutEXE)){
            LOGGER.error("System is not properly setup. Either mono is not installed or Layouter EXE path is wrong. See log.");
            return null;
        }

        // Request for a temporary folder
        File tempFolder = Files.createTempDir();

        LOGGER.info("Working on temporary folder: " + tempFolder.getAbsolutePath());
        File tempFileIn  = new File (tempFolder.getAbsolutePath() + "/sbmlIn.xml");

        // Set the output directory to the temporary directory...
        options.setDirectoryToSaveTo(tempFolder.getAbsolutePath());

        SBMLDocument output = null;

        // Save the SBMLDocument (without any layout information) in the filesystem to use it in the command line
        try {

            LOGGER.info("Writing sbml document to " + tempFileIn);
            // Write the Document object to the file system
            SBMLWriter.write(nonLayoutedSBMLDoc,tempFileIn.getAbsolutePath(), this.getClass().getName(),"1.0");

            // Fill the file option with the saved file
            options.setFileToConvert(tempFileIn.getAbsolutePath());

            // Invoke the layouter through the command line
            makeTheCall();

            String generatedFile = tempFileIn.getAbsolutePath() + "_layout.xml";

            LOGGER.info("Reading generated layout sbml from " + generatedFile);

            // Return the new SBMLDocument, this time with layout information.
            output = SBMLReader.read(new File(generatedFile));


        } catch (XMLStreamException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // In case of an error it will return null
        return output;
    }

    /**
     * Makes the command line call
     */
    private void makeTheCall() {

        Runtime rt = Runtime.getRuntime();
        Process proc;
        String commandLine = "mono " + pathToSaveLayoutEXE + options.toString();

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

    /**
     * default constructor
     */
    public SBWAutoLayouterAlgorithm(String pathToExec, SBWAutoLayouterAlgorithmOptions options){

        this.pathToSaveLayoutEXE = pathToExec;
        this.options = options;

    }
    
    public SBWAutoLayouterAlgorithm(String pathToExec) {
        this(pathToExec, new SBWAutoLayouterAlgorithmOptions());
    }

    public static boolean testSetup(String pathToLayouter){

        if (testMono()){
            if (testLayouter(pathToLayouter)){
                return true;
            }
        }

        return false;
    }

    public static boolean testMono(){
        return testcommand("mono" , "--version");
    }

    public static boolean testLayouter(String pathToLayouterEXE){
        return testcommand("mono" , pathToLayouterEXE );
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
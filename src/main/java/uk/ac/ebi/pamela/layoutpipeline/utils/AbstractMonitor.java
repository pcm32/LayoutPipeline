package uk.ac.ebi.pamela.layoutpipeline.utils;

import org.apache.log4j.Logger;
import uk.ac.ebi.pamela.layoutpipeline.Query;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 20/6/13
 * Time: 23:09
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractMonitor<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractMonitor.class);

    boolean active = false;
    BufferedWriter writer;
    String fileName;


    /**
     * Produces the file name (and not the complete path) of the file where the monitor is writing
     * it's output.
     *
     * @return the file name (not the complete path) where output is written
     */
    abstract String getFileName();

    /**
     * Sets the ouput path (directory) where the "depth.txt" file will be created, and where the record for each query
     * will be written (the used depth in the end for each query). Calling this method activates the registration of data.
     *
     * @param path of directory where the depth.txt file will reside
     */
    public synchronized void setOutputPath(String path) {
        try {
            active = true;
            writer = new BufferedWriter(new FileWriter(path+ File.separator+getFileName()));
        } catch (IOException e) {
            LOGGER.error("Problems opening "+getFileName()+" file for writing",e);
            active = false;
            throw new RuntimeException();
        }
    }

    /**
     * If the monitor is active (by calling previously {@link #setOutputPath(String)}, then the given depth is written
     * to file for the given query.
     *
     * @param query
     * @param objectToRegister the object that the monitor will register.
     * @param index optional index for the file where the output is left.
     */
    public abstract void register(Query query, T objectToRegister, int index);

    /**
     * Closes the file writer if it is not null.
     */
    public synchronized void close() {
        try {
            if(writer!=null) {
                writer.close();
            }
        } catch (IOException e) {
            LOGGER.error("Could not close "+getFileName());
        }
    }
}

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
 * Date: 15/6/13
 * Time: 02:06
 *
 * The aim of this class is to register the depth used for each query. This is relevant since for each query, if a certain
 * number of reactions is reached, then the depth could be reduced, to have fewer reactions and improve visualization.
 *
 */
public class ReactionRecursionDepthMonitor {

    private static final Logger LOGGER = Logger.getLogger(ReactionRecursionDepthMonitor.class);

    private static boolean active = false;
    private static BufferedWriter writer;

    /**
     * Sets the ouput path (directory) where the "depth.txt" file will be created, and where the record for each query
     * will be written (the used depth in the end for each query). Calling this method activates the registration of data.
     *
     * @param path of directory where the depth.txt file will reside
     */
    public synchronized static void setOutputPath(String path) {
        try {
            active = true;
            writer = new BufferedWriter(new FileWriter(path+File.separator+"depths.txt"));
        } catch (IOException e) {
            LOGGER.error("Problems opening depth.txt file for writing",e);
            throw new RuntimeException();
        } finally {
            active = false;
        }
    }

    /**
     * If the monitor is active (by calling previously {@link #setOutputPath(String)}, then the given depth is written
     * to file for the given query.
     *
     * @param query
     * @param depth
     */
    public synchronized static void register(Query query, Integer depth) {
        try {
            if(active) {
                writer.write(query.getChemicalIdentifier().toString()+"\t"+query.getOrganismIdentifier().toString()+"\t"+depth+"\n");
            }
        } catch (IOException e) {
            LOGGER.error("Problems writing to depths.txt file",e);
        }
    }

    /**
     * Closes the file writer if it is not null.
     */
    public synchronized static void close() {
        try {
            if(writer!=null) {
                writer.close();
            }
        } catch (IOException e) {
            LOGGER.error("Could not close depth.txt");
        }
    }





}

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
public class ReactionRecursionDepthMonitor extends AbstractMonitor<Integer> {

    private static final Logger LOGGER = Logger.getLogger(ReactionRecursionDepthMonitor.class);

    private static ReactionRecursionDepthMonitor INSTANCE;

    private ReactionRecursionDepthMonitor() {
        super();
        this.fileName = "depth.txt";
    }

    @Override
    String getFileName() {
        return fileName;
    }

    public static synchronized ReactionRecursionDepthMonitor getMonitor() {
        if(INSTANCE==null) {
            INSTANCE = new ReactionRecursionDepthMonitor();
        }
        return INSTANCE;
    }

    @Override
    public synchronized void register(Query query, Integer depth, int index) {
        try {
            if(active) {
                writer.write(query.getChemicalIdentifier().toString()+"\t"
                        +query.getOrganismIdentifier().toString()+"\t"+index+"\t"+depth+"\n");
            }
        } catch (IOException e) {
            LOGGER.error("Problems writing to "+getFileName()+" file",e);
        }
    }


}

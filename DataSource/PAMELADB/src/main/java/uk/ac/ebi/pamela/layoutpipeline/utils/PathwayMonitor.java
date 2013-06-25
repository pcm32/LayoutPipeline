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

import com.sri.biospice.warehouse.schema.DBID;
import com.sri.biospice.warehouse.schema.object.Pathway;
import org.apache.log4j.Logger;
import uk.ac.ebi.pamela.layoutpipeline.Query;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 15/6/13
 * Time: 02:06
 *
 * The aim of this class is to register the pathways used by each query. This is relevant for metadata collection.
 *
 * TODO rename to BWHPathwayMonitor.
 */
public class PathwayMonitor extends AbstractMonitor<Pathway> {

    private static final Logger LOGGER = Logger.getLogger(PathwayMonitor.class);

    private static PathwayMonitor INSTANCE;

    private PathwayMonitor() {
        super();
        this.fileName = "pathway.txt";
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    String getFileName() {
        return fileName;
    }

    /**
     * Retrieves the single instance of the monitor. The only instance needs to be first initialized by executing
     * {@link #setOutputPath(String)}.
     *
     * @return the current instance of the monitor.
     */
    public static synchronized PathwayMonitor getMonitor() {
        if(INSTANCE==null) {
            INSTANCE = new PathwayMonitor();
        }
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void register(Query query, Pathway pathway, int index) {
        try {
            if(active) {
                Iterator<DBID> itXrefs = DBID.loadDBIDsV2(pathway.getWID()).iterator();
                String extIdent = "null";
                if(itXrefs.hasNext()) {
                    DBID dbid = itXrefs.next();
                    extIdent = dbid.getXID();
                }
                writer.write(query.getChemicalIdentifier().toString()+
                        "\t"+query.getOrganismIdentifier().toString()+"\t"+index+"\t"+pathway.getName()+"\t"+extIdent+"\t"+pathway.getWID()+"\n");
            }
        } catch (IOException e) {
            LOGGER.error("Problems writing to "+getFileName()+" file",e);
        }
    }


}

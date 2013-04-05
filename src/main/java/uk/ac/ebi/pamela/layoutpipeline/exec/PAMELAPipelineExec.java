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

package uk.ac.ebi.pamela.layoutpipeline.exec;

import java.io.IOException;
import java.sql.SQLException;
import uk.ac.ebi.metabolomes.biowh.BiowhConnection;
import uk.ac.ebi.metabolomes.biowh.BiowhPooledConnection;
import uk.ac.ebi.metabolomes.biowh.DataSetProvider;
import uk.ac.ebi.pamela.layoutpipeline.PAMELAReactionListRetriever;
import uk.ac.ebi.pamela.layoutpipeline.Query;
import uk.ac.ebi.pamela.layoutpipeline.SBWAutoLayouterAlgorithm;
import uk.ac.ebi.pamela.layoutpipeline.SBWRenderer;
import uk.ac.ebi.pamela.layoutpipeline.SimpleOrgMolQuery;
import uk.ac.ebi.pamela.layoutpipeline.bwh.DataSetSelector;
import uk.ac.ebi.pamela.layoutpipeline.bwh.NewestUnifiedDataSetSelector;

/**
 * @name    PAMELAPipelineExec
 * @date    2013.04.04
 * @version $Rev$ : Last Changed $Date$
 * @author  Pablo Moreno <pablacious at users.sf.net>
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class PAMELAPipelineExec implements Runnable {

    private Query query;
    private PAMELAReactionListRetriever rxnRetriever;
    private final SBWAutoLayouterAlgorithm layouterAlgorithm;
    private final SBWRenderer renderer;
    private final DataSetSelector selector;
    private final Integer depth;
    private final String outPath;
    
    public PAMELAPipelineExec(Integer depth, String outputPath) throws IOException, SQLException {
        BiowhPooledConnection bwhc = new BiowhPooledConnection();
        DataSetProvider.loadPropsForCurrentSchema();
        this.selector = new NewestUnifiedDataSetSelector();
        this.depth = depth;
        this.outPath = outputPath;     
        this.layouterAlgorithm = new SBWAutoLayouterAlgorithm("/Users/pmoreno/Documents/Projects/MTBL/Layout/Test/AutoLayoutWithoutLibSBML/SaveLayout.exe");
        //this.renderer = new SBWRenderer("/Applications/SBW/lib/SBMLLayoutReader.exe");
        this.renderer = new SBWRenderer("/Users/pmoreno/Documents/Projects/MTBL/Layout/Test/AutoLayoutWithoutLibSBML/SBMLLayoutReader.exe");
    }
    
    /**
     * Set the value of query
     *
     * @param query new value of query
     */
    public void setQuery(Query query) {
        this.query = query;
        try {
        this.rxnRetriever = new PAMELAReactionListRetriever(selector, query.getOrganismIdentifier(), depth);
        } catch(Exception e) {
          throw new RuntimeException("Could not start PAMELAReactionListRetriever", e);
        } 
    }

    
    public void run() {
        PipelineExec exec = new PipelineExec(query, rxnRetriever, layouterAlgorithm, renderer, 2, 3, outPath);
        exec.run();
    }
    
    
    public static void main(String[] args) throws IOException, SQLException {
        PAMELAPipelineExec exec = new PAMELAPipelineExec(3, "/tmp/TestPamelaLayout");        
        exec.setQuery(new SimpleOrgMolQuery("CHEBI:17737", "9606"));
        exec.run();
    }


}

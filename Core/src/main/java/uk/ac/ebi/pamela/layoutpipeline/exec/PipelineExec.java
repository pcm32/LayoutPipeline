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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.google.common.io.Files;
import org.apache.log4j.Logger;
import org.sbml.jsbml.SBMLDocument;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.io.xml.sbml.SBMLIOUtil;
import uk.ac.ebi.mdk.io.xml.sbml.SimpleSideCompoundHandler;
import uk.ac.ebi.pamela.layoutpipeline.LayoutAlgorithm;
import uk.ac.ebi.pamela.layoutpipeline.LayoutRenderer;
import uk.ac.ebi.pamela.layoutpipeline.Query;
import uk.ac.ebi.pamela.layoutpipeline.ReactionListRetriever;
import uk.ac.ebi.pamela.layoutpipeline.detection.ReconsMotifCleaner;

/**
 * @name PipelineExec
 * @date 2013.02.25
 * @version $Rev$ : Last Changed $Date$
 * @author pmoreno
 * @author $Author$ (this version)
 * @brief Given an injected query and reaction list retriever, it obtains a list of reactions, write them to SBML and
 * executes a layout algorithm implementation to produce a layouted SBML and resulting image.
 *
 */
public class PipelineExec {

    private static final Logger LOGGER = Logger.getLogger(PipelineExec.class);
    private Query query;
    private ReactionListRetriever retriever;
    private LayoutAlgorithm layoutAlg;
    private LayoutRenderer layoutRend;
    private Integer sbmlLevel;
    private Integer sbmlVersion;
    private String imageOutputPath;
    private List<ReconsMotifCleaner> reconsCleaners;


    /**
     *
     *
     * @param query The query that the ReactionListRetriever will process to obtain a set of metabolic reactions.
     * @param retriever Deals with the query, connecting to a data source, and retrieving the desired reactions given.
     * @param layoutAlg object capable of rendering a
     * @param layoutRend
     * @param sbmlLevel
     * @param sbmlVersion
     * @param imageOutputPath
     */
    public PipelineExec(Query query, ReactionListRetriever retriever, LayoutAlgorithm layoutAlg, LayoutRenderer layoutRend,
            Integer sbmlLevel, Integer sbmlVersion, String imageOutputPath) {
        this.query = query;
        this.retriever = retriever;
        this.layoutAlg = layoutAlg;
        this.layoutRend = layoutRend;
        this.sbmlLevel = sbmlLevel;
        this.sbmlVersion = sbmlVersion;
        this.imageOutputPath = imageOutputPath;
        this.reconsCleaners = new LinkedList<ReconsMotifCleaner>();
        File outputDir = new File(imageOutputPath);
        if(!outputDir.isDirectory())
            outputDir.mkdir();
    }

    public void addReconstructionCleaners(ReconsMotifCleaner cleaner) {
        this.reconsCleaners.add(cleaner);
    }

    public void run() {
        List<Reconstruction> recons = retriever.getReactionsAsReconstructions(query);
        for (int i = 0; i < recons.size(); i++) {
            for (ReconsMotifCleaner cleaner : reconsCleaners) {
                cleaner.cleanRecons(recons.get(i));
            }
            SBMLIOUtil sbmlIO = new SBMLIOUtil(DefaultEntityFactory.getInstance(), sbmlLevel, sbmlVersion, new SimpleSideCompoundHandler());
            SBMLDocument doc = sbmlIO.getDocument(recons.get(i));
            /**
             * Should we produce directly a layout image, or should we keep the intermediate layouted SBML? Probably for
             * the first versions it will be useful as a debugging/check step.
             */
            SBMLDocument docWLayout = layoutAlg.getLayoutedSBML(doc);
            layoutRend.produceRender(docWLayout, createOuputFileNamePrefix(i));
        }
        if(recons.isEmpty()) {
            LOGGER.info("No reactions found for : "+query.getChemicalIdentifier().getAccession()+"\t"+query.getOrganismIdentifier().getAccession());
        }

    }

    private String createOuputFileNamePrefix(int i) {
        return imageOutputPath+query.getChemicalIdentifier().toString().replaceAll(":","_")+"_"+query.getOrganismIdentifier().toString()+"_"+i;
    }
}

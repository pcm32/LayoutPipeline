package uk.ac.ebi.pamela.layoutpipeline.exec;

import com.sri.biospice.warehouse.schema.object.Pathway;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.metabolomes.biowh.BiowhPooledConnection;
import uk.ac.ebi.metabolomes.biowh.DataSetProvider;
import uk.ac.ebi.pamela.layoutpipeline.PAMELAPathwayBasedRxnListRetriever;
import uk.ac.ebi.pamela.layoutpipeline.PathwayListRetriever;
import uk.ac.ebi.pamela.layoutpipeline.SBWAutoLayouterAlgorithm;
import uk.ac.ebi.pamela.layoutpipeline.SBWAutoLayouterAlgorithmOptions;
import uk.ac.ebi.pamela.layoutpipeline.bwh.DataSetSelector;
import uk.ac.ebi.pamela.layoutpipeline.bwh.NewestUnifiedDataSetSelector;
import uk.ac.ebi.pamela.layoutpipeline.detection.autorxn.AutoRxnReconsMotifCleaner;
import uk.ac.ebi.pamela.layoutpipeline.detection.nad.NADRelatedReconsMotifCleaner;
import uk.ac.ebi.pamela.layoutpipeline.pathway.PAMELAPathwayFileNamer;
import uk.ac.ebi.pamela.layoutpipeline.pathway.PathwayFileNamer;
import uk.ac.ebi.pamela.layoutpipeline.renderer.batik.BatikRenderer;
import uk.ac.ebi.pamela.layoutpipeline.utils.PropertiesUtil;
import uk.ac.ebi.pamela.layoutpipeline.utils.SBWAlgorithmPrefsSetter;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 21/6/13
 * Time: 15:49
 * To change this template use File | Settings | File Templates.
 */
public class PAMELADirectMultiPathwayPipelineExec implements Runnable{

    private static final Logger LOGGER = Logger.getLogger(PAMELADirectMultiPathwayPipelineExec.class);

    private PathwayListRetriever<Pathway> rxnRetriever;
    private PathwayFileNamer<Pathway> pathwayFileNamer;
    private final SBWAutoLayouterAlgorithm layouterAlgorithm;
    private final BatikRenderer renderer = new BatikRenderer();
    private final DataSetSelector selector;
    private final String path;

    private final String pathwaysDir = "pathways";

    public PAMELADirectMultiPathwayPipelineExec(String path) throws IOException, SQLException {
        DataSetProvider.loadPropsForCurrentSchema();
        this.selector = new NewestUnifiedDataSetSelector();
        this.path = path;
        SBWAutoLayouterAlgorithmOptions optionLayout = new SBWAutoLayouterAlgorithmOptions();
        optionLayout.setGravityFactor(120);
        optionLayout.setMagnetism(true);
        //optionLayout.setBoundary(true);
        //optionLayout.setGrid(true);
        this.layouterAlgorithm =
                new SBWAutoLayouterAlgorithm(PropertiesUtil.getPreference(
                        SBWAlgorithmPrefsSetter.SBWAlgPrefsField.pathToSaveLayoutEXE, ""),optionLayout);
//        SBWRendererOptions opts = new SBWRendererOptions();
//        opts.setOutputFormat(SBWRendererOptions.Format.png);
//        //this.renderer = new SBWRenderer("/Applications/SBW/lib/SBMLLayoutReader.exe",opts);
//        this.renderer =
//                new SBWRenderer(PropertiesUtil.getPreference(
//                        SBWRendererPrefsSetter.SBWRendererPrefsField.pathToRendererEXE,""),opts);
        this.pathwayFileNamer = new PAMELAPathwayFileNamer(); // the rendered class will add the adequate suffix.
    }

    @Override
    public void run() {
        /**
         * This class should read the the pathway.txt file in the directory and iterate on the pathways, setting
         * the rxnRetriever
         *
         * WID10927853	DWID5	9606
         * WID12203169	DWID2	9606
         *
         * We iterate over this file, and whenever we encounter a different organism, we reset the rxnRetriever,
         * otherwise, we just set it with the new pathway to process.
         */
        List<PathwayContainer> pathways = getPathwaysFromFilePath();
        Taxonomy currentTaxID = null;

        for (PathwayContainer cont : pathways) {
            if(!cont.getTaxid().equals(currentTaxID)) {
                this.rxnRetriever = new PAMELAPathwayBasedRxnListRetriever(this.selector,cont.getTaxid());
                currentTaxID = cont.getTaxid();
            }

            PathwayPipelineExec<Pathway> exec = new PathwayPipelineExec<Pathway>(rxnRetriever, layouterAlgorithm,
                    renderer, 2, 3, path + File.separator + pathwaysDir, pathwayFileNamer);
            exec.setPathway(cont.getPathway());
            //PathwayMonitor.getMonitor().setOutputPath(outPath);
            exec.addReconstructionCleaners(new NADRelatedReconsMotifCleaner());
            exec.addReconstructionCleaners(new AutoRxnReconsMotifCleaner());
            exec.run();
            //PathwayMonitor.getMonitor().close();
        }
    }

    private List<PathwayContainer> getPathwaysFromFilePath() {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path+ File.separator+"queriesPathways.txt"));
            String line = reader.readLine();
            List<PathwayContainer> pathways = new LinkedList<PathwayContainer>();
            while (line!=null) {
                String[] tokens = line.split("\t");
                Long dataSetWID = Long.parseLong(tokens[1].replaceFirst("DWID", ""));
                Long WID = Long.parseLong(tokens[0].replaceFirst("WID", ""));
                String taxID = tokens[2];
                Pathway pathway = new Pathway(dataSetWID,WID);
                try {
                    pathway.load();
                } catch (SQLException e) {
                    LOGGER.error("Could not load pathway WID "+WID,e);
                }
                Taxonomy taxid = new Taxonomy();
                taxid.setAccession(taxID);
                PathwayContainer container = new PathwayContainer(pathway,taxid);
                pathways.add(container);
                line = reader.readLine();
            }
            reader.close();
            return pathways;
        } catch (IOException e) {
            throw new RuntimeException("Could not read or close the queriesPathways.txt file in the location defined : "
                    +path+ File.separator+"queriesPathways.txt",e);
        }
    }

    class PathwayContainer {
        private Pathway pathway;
        private Taxonomy taxid;

        public PathwayContainer(Pathway pathway, Taxonomy taxid) {
            this.pathway = pathway;
            this.taxid = taxid;
        }

        Pathway getPathway() {
            return pathway;
        }

        Taxonomy getTaxid() {
            return taxid;
        }

    }

    public static void main(String[] args) throws SQLException, IOException{
        String path = args[0];
        BiowhPooledConnection con = new BiowhPooledConnection();

        PAMELADirectMultiPathwayPipelineExec exec = new PAMELADirectMultiPathwayPipelineExec(path);
        exec.run();

    }
}

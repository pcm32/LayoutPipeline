package uk.ac.ebi.pamela.layoutpipeline.exec;

import com.sri.biospice.warehouse.schema.DataSet;
import com.sri.biospice.warehouse.schema.object.Pathway;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.type.ChemicalIdentifier;
import uk.ac.ebi.metabolomes.biowh.BiowhPooledConnection;
import uk.ac.ebi.pamela.layoutpipeline.Query;
import uk.ac.ebi.pamela.layoutpipeline.SimpleOrgMolQuery;
import uk.ac.ebi.pamela.layoutpipeline.bwh.DataSetSelector;
import uk.ac.ebi.pamela.layoutpipeline.bwh.NewestUnifiedDataSetSelector;
import uk.ac.ebi.pamela.layoutpipeline.pathway.PAMELAPathwayFileNamer;
import uk.ac.ebi.pamela.layoutpipeline.pathway.PAMELAPreUnificationPathwayGetter;
import uk.ac.ebi.pamela.layoutpipeline.pathway.PathwayFileNamer;
import uk.ac.ebi.pamela.layoutpipeline.pathway.PathwayGetter;
import uk.ac.ebi.pamela.layoutpipeline.utils.LayoutFileNamer;
import uk.ac.ebi.pamela.layoutpipeline.utils.PathwayMonitor;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

/**
 * This class receives a set of chemical identifiers, resolve which pathways from PAMELA need to be drawn, the
 * assignment between chemical identifiers and pathways (through links that will be created later and through the
 * pathway.txt file), and which chemical identifiers need to be run in the depth mode as no pathways are available
 * for it. This should also handle small molecules that extremely highly connected, and for which no pathway should
 * be drawn.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 21/6/13
 * Time: 08:44
 * To change this template use File | Settings | File Templates.
 */
public class CombinedPathwayDepthPreRunner {

    private static final Logger LOGGER = Logger.getLogger(CombinedPathwayDepthPreRunner.class);

    Set<ChemicalIdentifier> identifiers;
    Taxonomy organism;
    private PathwayGetter<Pathway> pathwayGetter;
    private String outputPath;
    private DataSet ds;
    private LinkMaker linkMaker;
    private Integer maxNumberPaths = 60;

    private String pathwaysDir = "pathways";


    public CombinedPathwayDepthPreRunner(DataSet ds, Taxonomy org, PathwayGetter<Pathway> pGetter,
                                         Collection<ChemicalIdentifier> idents, String outpath) {
        this.ds = ds;
        this.organism = org;
        this.pathwayGetter = pGetter;
        this.identifiers = new TreeSet<ChemicalIdentifier>(new ChemIdentifierComparator());
        this.identifiers.addAll(idents);
        this.outputPath = outpath;
        createDirs();
        this.linkMaker = new LinkMaker(outputPath,pathwaysDir,new PAMELAPathwayFileNamer(".svg"), new LayoutFileNamer(".svg"));

    }

    private void createDirs() {
        File outputDir = new File(outputPath);
        if(!outputDir.isDirectory())
            outputDir.mkdir();
        File pathways = new File(outputDir+File.separator+pathwaysDir);
        if(!pathways.isDirectory())
            pathways.mkdir();
    }

    public void run() {
        Set<Pathway> pathways = new HashSet<Pathway>();
        PathwayMonitor.getMonitor().setOutputPath(outputPath);
        Set<ChemicalIdentifier> forDepthDrawing = new TreeSet<ChemicalIdentifier>(new ChemIdentifierComparator());
        for (ChemicalIdentifier identifier : identifiers) {
            Query query = new SimpleOrgMolQuery(identifier.getAccession(),organism.getTaxon()+"");
            Collection<Pathway> paths = pathwayGetter.getPathways(query);
            if(paths.size()>maxNumberPaths) {
                LOGGER.info("Skipping "+query.getChemicalIdentifier().getAccession()+" because it has "+paths.size()+" pathways.");
                continue;
            }
            int pathCounter = 0;
            LOGGER.info(query.getChemicalIdentifier().getAccession() + " pathways:\t" + paths.size());
            for (Pathway pathway : paths) {
                PathwayMonitor.getMonitor().register(query,pathway,pathCounter);
                linkMaker.writeLinkCreationCommand(query,pathCounter,pathway);
                pathCounter++;
            }
            pathways.addAll(paths);
            if(pathCounter==0)
                forDepthDrawing.add(identifier);
        }
        PathwayMonitor.getMonitor().close();
        writeChemicalsForExecution(forDepthDrawing, "queriesForDepth.txt");
        writePathwaysForExecution(pathways, "queriesPathways.txt");

        linkMaker.close();
    }

    private void writeChemicalsForExecution(Set<ChemicalIdentifier> forDepthDrawing, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath+File.separator+fileName));
            for (ChemicalIdentifier ident : forDepthDrawing) {
                writer.write(ident.getAccession()+"\t"+organism.getAccession()+"\n");
            }
            writer.close();
        } catch (IOException e) {

        }
    }

    private void writePathwaysForExecution(Set<Pathway> pathways, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath+File.separator+fileName));
            for (Pathway pathway : pathways) {
                writer.write("WID"+pathway.getWID()+"\tDWID"+pathway.getDataSetWID()+"\t"+organism.getAccession()+"\t"+pathway.getName()+"\n");
            }
            writer.close();
        } catch (IOException e) {

        }
    }

    class LinkMaker {
        private String outpath;
        private BufferedWriter writer;
        private PathwayFileNamer pathwayFileNamer;
        private LayoutFileNamer layoutNamer;
        private String pathwayDir;

        public LinkMaker(String outpath, String pathwayDir, PathwayFileNamer<Pathway> pathwayFileNamer, LayoutFileNamer layoutNamer) {

            try {
                this.outpath = outpath;
                this.writer = new BufferedWriter(new FileWriter(outpath+ File.separator+"pathwayLinks.sh"));
                this.pathwayFileNamer = pathwayFileNamer;
                this.layoutNamer = layoutNamer;
                this.pathwayDir = pathwayDir;
            } catch (IOException e) {
                throw new RuntimeException("Could not open pathwayLinks.sh to write",e);
            }
        }

        public void writeLinkCreationCommand(Query query, int pathCounter, Pathway pathway) {
            try {
                this.writer.write("ln -s "+pathwayDir+File.separator+pathwayFileNamer.getFileName(query, pathway)+" "+layoutNamer.getName(query,pathCounter)+"\n");
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        public void close() {
            try {
                this.writer.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    class ChemIdentifierComparator implements Comparator<ChemicalIdentifier> {
        @Override
        public int compare(ChemicalIdentifier o1, ChemicalIdentifier o2) {
            String acc1 = o1.getAccession();
            String acc2 = o2.getAccession();

            try {
                if(acc1.contains(":") && acc2.contains(":")) {
                    Integer acc1Int = Integer.parseInt(acc1);
                    Integer acc2Int = Integer.parseInt(acc2);

                    return acc1Int.compareTo(acc2Int);
                }
            } catch (NumberFormatException e) {
            }
            return acc1.compareTo(acc2);
        }
    }

    public static void main(String[] args) throws IOException, SQLException{
        BiowhPooledConnection con = new BiowhPooledConnection();

        Taxonomy tax = new Taxonomy();
        tax.setAccession("9606");
        DataSetSelector selector = new NewestUnifiedDataSetSelector();
        DataSet ds=null;
        if(selector.hasDataSetForOrganism(tax))
            ds = selector.getDataSetForOrganism(tax);
        else {
            LOGGER.error("Species not found "+tax.getTaxon());
            System.exit(1);
        }

		String fileWithIds = args[0];
		System.out.println("File with ids is " + fileWithIds);


		String output = args[1];
		System.out.println("Output will go to " + output);

        BufferedReader reader = new BufferedReader(new FileReader(fileWithIds));
        String line = reader.readLine();
        Collection<ChemicalIdentifier> idents = new LinkedList<ChemicalIdentifier>();
        while (line!=null) {
            idents.add(new ChEBIIdentifier(line));
            line = reader.readLine();
        }
        reader.close();
        CombinedPathwayDepthPreRunner runner = new CombinedPathwayDepthPreRunner(ds,tax,new PAMELAPreUnificationPathwayGetter(ds),idents,output);
        runner.run();
    }


}

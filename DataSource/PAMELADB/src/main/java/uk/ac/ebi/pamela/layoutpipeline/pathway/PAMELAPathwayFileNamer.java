package uk.ac.ebi.pamela.layoutpipeline.pathway;

import com.sri.biospice.warehouse.schema.object.Pathway;
import uk.ac.ebi.pamela.layoutpipeline.Query;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 21/6/13
 * Time: 11:40
 *
 * {@inheritDoc}
 *
 * The file name for PAMELA derived pathways makes use of the WID and Data Set WID of the pathway.
 * A suffix can be specified for the file extension.
 *
 */
public class PAMELAPathwayFileNamer implements PathwayFileNamer<Pathway> {

    String suffix;

    public PAMELAPathwayFileNamer(String suffix) {
        this.suffix = suffix;
    }

    public PAMELAPathwayFileNamer() {
        this.suffix = "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileName(Query query, Pathway pathway) {
        return "PAMELA_Pathway_WID"+pathway.getWID()+"DWID"+pathway.getDataSetWID()+suffix;
    }
}

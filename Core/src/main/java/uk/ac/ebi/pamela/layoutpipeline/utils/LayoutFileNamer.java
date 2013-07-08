package uk.ac.ebi.pamela.layoutpipeline.utils;

import uk.ac.ebi.pamela.layoutpipeline.Query;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 21/6/13
 * Time: 10:31
 *
 * Encapsulates the logic of file name generator for a query (small molecule - organism) and iteration of the layout.
 */
public class LayoutFileNamer {

    String suffix;

    public LayoutFileNamer(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Given the suffix, query, and pathCounter, this method produces the output file name.
     *
     * @param query
     * @param pathCounter
     * @return formed name
     */
    public String getName(Query query, int pathCounter) {
        return query.getChemicalIdentifier().getAccession().replaceAll(":","_")
                +"_"+query.getOrganismIdentifier().getTaxon()+"_"+pathCounter+ suffix;
    }
}

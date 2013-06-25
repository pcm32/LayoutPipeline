package uk.ac.ebi.pamela.layoutpipeline.pathway;

import com.sri.biospice.warehouse.schema.TableBase;
import com.sri.biospice.warehouse.schema.object.Pathway;
import uk.ac.ebi.pamela.layoutpipeline.Query;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 21/6/13
 * Time: 09:42
 *
 * Given a query and pathway, implementations produce a file name for the pathway layout (but not the complete path).
 */
public interface PathwayFileNamer<P> {

    /**
     * Produces a file name for the layout of the given pathway within the given organism in the query.
     *
     * @param query
     * @param pathway
     * @return file name (not complete path) for the layout output.
     */
    public String getFileName(Query query, P pathway);
}

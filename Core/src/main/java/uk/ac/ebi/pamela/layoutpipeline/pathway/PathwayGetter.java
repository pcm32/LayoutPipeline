package uk.ac.ebi.pamela.layoutpipeline.pathway;

import uk.ac.ebi.pamela.layoutpipeline.Query;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 20/6/13
 * Time: 16:27
 *
 * Describes functionality for the retrieval of pathways starting from a query (organism-small molecule).
 */
public interface PathwayGetter<P> {

    /**
     * Produces a collection of pathway objects based on the given query.
     *
     * @param query
     * @return collection of pathways (or empty collection) corresponding to the query.
     */
    public Collection<P> getPathways(Query query);
}

package uk.ac.ebi.pamela.layoutpipeline;

import uk.ac.ebi.mdk.domain.entity.Reconstruction;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 21/6/13
 * Time: 16:20
 *
 * Defines the functionality needed for a PathwayListRetriever. The analogous {@link ReactionListRetriever} doesn't
 * use generics, as it uses the {@link Query} object. TODO these two interfaces need to be made equivalent in that respect.
 */
public interface PathwayListRetriever<P> {
    /**
     * Given a pathway, it produces a reconstruction out of it.
     *
     * @param pathway
     * @return reconstruction for the given pathway.
     */
    Reconstruction getPathwayAsReconstructions(P pathway);

    /**
     * Checks whether a pathway is suitable for layout.
     *
     * @param pathway
     * @return true if the pathway is suitable for layouting.
     */
    boolean isPathwaySuitableForLayout(P pathway);
}

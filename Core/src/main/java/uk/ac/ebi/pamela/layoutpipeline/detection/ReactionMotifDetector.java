package uk.ac.ebi.pamela.layoutpipeline.detection;

import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.Set;

/**
 * Describes the functionality to detect certain motifs within a reconstruction. Normally should receive a reconstruction,
 * which contains a set of reactions and small molecules, and produces a list of reactions as output which comply with
 * the motif.
 *
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/5/13
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
public interface ReactionMotifDetector {

    /**
     * When called, it should return the next set of reactions that are compliant (between them) with the motif. In other
     * words, all these reactions form an instance of the motif specified in the implementation.
     *
     * @return a collection of reactions that
     */
    public Set<MetabolicReaction> nextCompliantGroup();
}

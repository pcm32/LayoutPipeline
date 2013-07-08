package uk.ac.ebi.pamela.layoutpipeline.detection;

import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.Set;

/**
 * Given a set of reactions, classes implementing this interface should pick reactions that should be removed from the set.
 * This process is probably done iteratively, comparing pairs of reactions and removing on if applies.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/5/13
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public interface ReactionSetReducer {

    public Set<MetabolicReaction> reduceReactions(Set<MetabolicReaction> rxns);

}

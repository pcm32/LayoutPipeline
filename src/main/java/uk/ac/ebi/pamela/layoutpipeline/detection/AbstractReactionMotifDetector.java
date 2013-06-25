package uk.ac.ebi.pamela.layoutpipeline.detection;

import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 9/5/13
 * Time: 11:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractReactionMotifDetector {

    protected final Reconstruction recons;
    protected Set<MetabolicReaction> currentGroup;
    protected Set<MetabolicReaction> visitedReactions;

    public AbstractReactionMotifDetector(Reconstruction recons) {
        this.recons = recons;
        this.visitedReactions = new HashSet<MetabolicReaction>();
    }

    /**
     * Provides the next instance of the motif that the implementation is looking for.
     *
     * @return a set of reaction that follow the motif required, or null if none is found.
     */
    public Set<MetabolicReaction> nextCompliantGroup() {
        if(currentGroup==null) {
            init();
        }
        if(currentGroup.size()==0)
            return null;
        else {
            Set<MetabolicReaction> toRet = currentGroup;
            currentGroup = findNewMatch();
            return toRet;
        }
    }

    /**
     * Initializes the current group.
     */
    private void init() {
        this.currentGroup = findNewMatch();
    }

    /**
     * Finds subsequent matches for the motif that we are looking for. If no match is found, an empty set should be returned.
     *
     * @return a set containing a set of reactions that match the motif, or an empty set if no match is found.
     */
    protected abstract Set<MetabolicReaction> findNewMatch();
}

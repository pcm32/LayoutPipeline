package uk.ac.ebi.pamela.layoutpipeline.detection.autorxn;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.pamela.layoutpipeline.detection.AbstractReactionMotifDetector;
import uk.ac.ebi.pamela.layoutpipeline.detection.ReactionMotifDetector;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 9/5/13
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public class AutoRxnMotifDetector extends AbstractReactionMotifDetector implements ReactionMotifDetector {

    /**
     * Finds reactions that start and end in the same metabolite. These are errors in the database and should be fixed.
     * One reaction is returned per next call (as each reaction in this case verifies the motif).
     *
     * @param recons where this pattern should be looked for.
     */
    public AutoRxnMotifDetector(Reconstruction recons) {
        super(recons);
    }

    @Override
    Set<MetabolicReaction> findNewMatch() {
        Set<MetabolicReaction> toRem = new HashSet<MetabolicReaction>();
        for(MetabolicReaction rxn : recons.reactome()) {
            if (visitedReactions.contains(rxn))
                continue;
            Set<Metabolite> reactsMet = new HashSet<Metabolite>();
            for (MetabolicParticipant partReact : rxn.getReactants()) {
                reactsMet.add(partReact.getMolecule());
            }
            Set<Metabolite> prodsMet = new HashSet<Metabolite>();
            for (MetabolicParticipant partProd : rxn.getProducts()) {
                prodsMet.add(partProd.getMolecule());
            }

            visitedReactions.add(rxn);
            if(reactsMet.retainAll(prodsMet) && reactsMet.size()>0)
                toRem.add(rxn);

            if (toRem.size()>0)
                return toRem;
        }
        return toRem;
    }
}

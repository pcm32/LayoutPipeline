package uk.ac.ebi.pamela.layoutpipeline.detection.nad;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.pamela.layoutpipeline.detection.MoleculeDetector;
import uk.ac.ebi.pamela.layoutpipeline.detection.ReactionSetReducer;
import uk.ac.ebi.pamela.layoutpipeline.detection.RxnParticipantsSetComparator;
import uk.ac.ebi.pamela.layoutpipeline.detection.nad.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/5/13
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
public class NADPHReactionSReducer implements ReactionSetReducer {

    RxnParticipantSetDetector nadhSetDet = new NADHRxnParticipantSetDetector();
    RxnParticipantSetDetector nadphSetDet = new NADPHRxnParticipantSetDetector();
    RxnParticipantSetDetector nad_p_SetDet = new NADP_P_HRxnParticipantSetDetector();
    RxnParticipantsSetComparator comparator = new RxnParticipantsSetComparator();

    @Override
    public Set<MetabolicReaction> reduceReactions(Set<MetabolicReaction> rxns) {
        Set<MetabolicReaction> toRemove = new HashSet<MetabolicReaction>();
        for (MetabolicReaction rxnA : rxns) {
            if (toRemove.contains(rxnA))
                continue;
            for (MetabolicReaction rxnB : rxns) {
                if(rxnA.equals(rxnB))
                    continue;
                if (toRemove.contains(rxnB))
                    continue;

                toRemove.addAll(pairIsComparable(rxnA,rxnB));
            }
        }
        return toRemove;
    }

    /**
     * If the the two reactions are comparable (ie they contain certain molecules), then the method
     * provides a set with the reaction that should be removed. If they are not comparable, the set returned
     * is empty.
     *
     * @param rxnA
     * @param rxnB
     * @return set containing a reaction to be removed.
     */
    private Set<MetabolicReaction> pairIsComparable(MetabolicReaction rxnA, MetabolicReaction rxnB) {
        Set<Metabolite> onlyInA = comparator.onlyInFirstRXN(rxnA,rxnB);
        Set<Metabolite> onlyInB = comparator.onlyInFirstRXN(rxnB,rxnA);

        Set<MetabolicReaction> toRet = new HashSet<MetabolicReaction>();

        if(isNADHSet(onlyInA) && isNADGeneralSet(onlyInB))
            toRet.add(rxnA);
        else if (isNADPHSet(onlyInA) && isNADGeneralSet(onlyInB))
            toRet.add(rxnA);
        else if (isNADHSet(onlyInB) && isNADGeneralSet(onlyInA))
            toRet.add(rxnB);
        else if (isNADHSet(onlyInB) && isNADGeneralSet(onlyInA))
            toRet.add(rxnB);

        return toRet;
    }

    private boolean isNADGeneralSet(Set<Metabolite> onlyInB) {
        return nad_p_SetDet.isSet(onlyInB);
    }

    private boolean isNADPHSet(Set<Metabolite> onlyInB) {
        return nadphSetDet.isSet(onlyInB);
    }

    private boolean isNADHSet(Set<Metabolite> onlyInA) {
        return nadhSetDet.isSet(onlyInA);
    }


}

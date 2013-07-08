package uk.ac.ebi.pamela.layoutpipeline.detection;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 14/5/13
 * Time: 19:05
 * To change this template use File | Settings | File Templates.
 */
public class RxnParticipantsSetComparator {

    /**
     * Returns a set of molecules that participate in rxn A but not in rxn B. Currently direction is neglected.
     *
     * @param rxnA
     * @param rxnB
     * @return molecules present in A but not in B.
     */
    public Set<Metabolite> onlyInFirstRXN(MetabolicReaction rxnA, MetabolicReaction rxnB) {
        Set<Metabolite> onlyInFirst = new HashSet<Metabolite>();
        for (MetabolicParticipant part : rxnA.getParticipants()) {
            onlyInFirst.add(part.getMolecule());
        }
        for (MetabolicParticipant partB : rxnB.getParticipants()) {
            onlyInFirst.remove(partB.getMolecule());
        }
        return onlyInFirst;
    }

}

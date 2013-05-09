package uk.ac.ebi.pamela.layoutpipeline.detection;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/5/13
 * Time: 16:57
 * To change this template use File | Settings | File Templates.
 */
public class NADPHReactionSReducer implements ReactionSetReducer {

    MoleculeDetector nadhDetector = new NADHDetector();
    MoleculeDetector nadphDetector = new NADPHDetector();
    MoleculeDetector nadDetector = new NADDetector();
    MoleculeDetector nadpDetector = new NADPDetector();
    MoleculeDetector nad_p_Dectector = new NAD_P_Detector();
    MoleculeDetector nad_p_hDetector = new NAD_P_HDetector();

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
        Set<Metabolite> onlyInA = onlyInFirstRXN(rxnA,rxnB);
        Set<Metabolite> onlyInB = onlyInFirstRXN(rxnB,rxnA);

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
        return isSetWithProtons(onlyInB,nad_p_hDetector,nad_p_Dectector);
    }

    private boolean isNADPHSet(Set<Metabolite> onlyInB) {
        return isSetWithProtons(onlyInB,nadphDetector,nadpDetector);
    }

    private boolean isNADHSet(Set<Metabolite> onlyInA) {
        return isSetWithProtons(onlyInA, nadhDetector, nadDetector);
    }

    private boolean isSetWithProtons(Set<Metabolite> mols,
                                     MoleculeDetector detector1, MoleculeDetector detector2) {
        boolean mol1=false;
        boolean mol2=false;
        boolean others=false;

        for (Metabolite met : mols) {
            if (detector1.isMolecule(met))
                mol1 = true;
            else if (detector2.isMolecule(met))
                mol2 = true;
            else if (!met.getName().equalsIgnoreCase("h+"))
                others = true;
        }

        return mol1 && mol2 && !others;

    }

    /**
     * Returns a set of molecules that participate in rxn A but not in rxn B. Currently direction is neglected.
     *
     * @param rxnA
     * @param rxnB
     * @return molecules present in A but not in B.
     */
    private Set<Metabolite> onlyInFirstRXN(MetabolicReaction rxnA, MetabolicReaction rxnB) {
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

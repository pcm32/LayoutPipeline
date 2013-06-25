package uk.ac.ebi.pamela.layoutpipeline.detection.nad;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.pamela.layoutpipeline.detection.RxnParticipantsSetComparator;

import java.util.HashSet;
import java.util.Set;

/**
 * Merges a NADH and a NADPH reaction into a single NAD(P)H if the only difference between the two reactions is
 * the NADH -> NAD (NADPH -> NADP) participants (plus protons). It should be done by replacing NADPH metabolite (
 * and associated) with NAD(P)H, and removing the NADH reaction. Attributes from the removed reaction need to be copied
 * over.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/5/13
 * Time: 12:33
 * To change this template use File | Settings | File Templates.
 */
public class NADReactionMerger {

    Reconstruction recons;
    Metabolite nad_p_h;
    Metabolite nad_p_;

    NADPHDetector nadphDetector;
    NADPDetector nadpDetector;

    NADHRxnParticipantSetDetector nadhRxnParticipantSetDetector;
    NADPHRxnParticipantSetDetector nadphRxnParticipantSetDetector;
    RxnParticipantsSetComparator comparator = new RxnParticipantsSetComparator();


    public NADReactionMerger(Reconstruction recons) {
        this.recons = recons;
        this.nad_p_h = findNAD_P_H();
        this.nad_p_ = findNAD_P();

        this.nadhRxnParticipantSetDetector = new NADHRxnParticipantSetDetector();
        this.nadphRxnParticipantSetDetector = new NADPHRxnParticipantSetDetector();

        this.nadpDetector = new NADPDetector();
        this.nadphDetector = new NADPHDetector();
    }

    /**
     * Given a set of reactions where a pair of NADPH and NADH (otherwise identical) reactions appear, this method
     * merges those two into a single NAD(P)H reaction. This method does not check whether the reaction set also contains
     * a NAD(P)H version of the reactions to be merged, that should be sorted out before getting to call this method.
     *
     * @param rxnSet
     * @return the set of reactions to be removed from the reconstruction.
     */
    public Set<MetabolicReaction> mergeReactions(Set<MetabolicReaction> rxnSet) {
        Set<MetabolicReaction> toRemove = new HashSet<MetabolicReaction>();
        // Find the NADH and NADPH pair of reactions
        MetabolicReaction nadhRxn=null;
        MetabolicReaction nadhpRxn=null;

        firstFor:
        for (MetabolicReaction rxnA : rxnSet) {
            for (MetabolicReaction rxnB : rxnSet) {
                if (rxnA.equals(rxnB))
                    continue;
                Set<Metabolite> onlyInA = comparator.onlyInFirstRXN(rxnA,rxnB);
                Set<Metabolite> onlyInB = comparator.onlyInFirstRXN(rxnB,rxnA);

                if (nadphRxnParticipantSetDetector.isSet(onlyInA)
                        && nadhRxnParticipantSetDetector.isSet(onlyInB)) {
                    nadhRxn = rxnB;
                    nadhpRxn = rxnA;
                    break firstFor;
                } else if (nadhRxnParticipantSetDetector.isSet(onlyInA)
                        && nadphRxnParticipantSetDetector.isSet(onlyInB)) {
                    nadhRxn = rxnA;
                    nadhpRxn = rxnB;
                    break firstFor;
                }
            }
        }

        if (nadhpRxn!=null && nadhRxn!=null) {
            toRemove.add(nadhRxn);
            replaceMols(nadhpRxn);
        }

        return toRemove;
    }

    private void replaceMols(MetabolicReaction nadhpRxn) {
        for (MetabolicParticipant part : nadhpRxn.getParticipants()) {
            Metabolite currentMol = part.getMolecule();
            boolean replaced=false;
            if(nadphDetector.isMolecule(currentMol)) {
                part.setMolecule(this.nad_p_h);
                replaced=true;
                if (!recons.metabolome().contains(this.nad_p_h))
                    recons.metabolome().add(this.nad_p_h);
            } else if (nadpDetector.isMolecule(currentMol)) {
                part.setMolecule(this.nad_p_);
                replaced=true;
                if (!recons.metabolome().contains(this.nad_p_))
                    recons.metabolome().add(this.nad_p_);
            }
            // We remove the mol if it doesn't participate in any other reaction
            // and was eliminated.
            if(replaced && recons.reactome().participatesIn(currentMol).size()==0) {
                recons.metabolome().remove(currentMol);
                recons.deregister(currentMol);
            }
        }
    }

    private Metabolite findNAD_P_H() {
        NAD_P_HDetector nad_p_hDetector = new NAD_P_HDetector();
        for (Metabolite met : this.recons.metabolome()) {
            if (nad_p_hDetector.isMolecule(met))
                return met;
        }
        return nad_p_hDetector.getMetabolite();
    }

    private Metabolite findNAD_P() {
        NAD_P_Detector nad_p_detector = new NAD_P_Detector();
        for (Metabolite met : this.recons.metabolome()) {
            if (nad_p_detector.isMolecule(met))
                return met;
        }
        return nad_p_detector.getMetabolite();
    }
}

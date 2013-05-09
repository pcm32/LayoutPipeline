package uk.ac.ebi.pamela.layoutpipeline.detection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.*;

/**
 * Finds reactions that share both a common main reactant and a common main product.
 *
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/5/13
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */
public class SameMainReactProdMotifDet extends AbstractReactionMotifDetector implements ReactionMotifDetector {

    private Multimap<Metabolite,Metabolite> visitedPairs;

    /**
     * Finds reactions that share both a reactant and a product (which are not the same).
     *
     * @param recons the reconstruction where the motif is looked for.
     */
    public SameMainReactProdMotifDet(Reconstruction recons) {
        super(recons);
        this.visitedPairs = HashMultimap.create();
    }


    @Override
    protected Set<MetabolicReaction> findNewMatch() {
        Set<MetabolicReaction> toRet = new HashSet<MetabolicReaction>();
        for (MetabolicReaction rxn : recons.reactome()) {

            if (visitedReactions.contains(rxn))
                continue;

            List<Metabolite> reactants = new ArrayList<Metabolite>();
            for (MetabolicParticipant reactPart : rxn.getReactants()) {
                if(reactPart.isSideCompound()) {
                    continue;
                }
                reactants.add(reactPart.getMolecule());
            }

            List<Metabolite> products = new ArrayList<Metabolite>();
            for (MetabolicParticipant prodPart : rxn.getProducts()) {
                if(prodPart.isSideCompound())
                    continue;
                else
                    products.add(prodPart.getMolecule());
            }

            for (Metabolite react : reactants) {

                Metabolite pairingMol = null;

                for (MetabolicReaction otherRxn : recons.participatesIn(react)) {
                    if (visitedReactions.contains(otherRxn))
                        continue;
                    if(otherRxn.equals(rxn))
                        continue;

                    for (MetabolicParticipant otherProdPart : otherRxn.getProducts()) {
                        if(visitedPairs.containsEntry(react,otherProdPart.getMolecule()))
                            continue;
                        if(products.contains(otherProdPart.getMolecule()) && pairingMol==null) {
                            toRet.add(otherRxn);
                            pairingMol = otherProdPart.getMolecule();
                            visitedReactions.add(otherRxn);
                        } else if(products.contains(otherProdPart.getMolecule()) && pairingMol.equals(otherProdPart.getMolecule())) {
                            toRet.add(otherRxn);
                            visitedReactions.add(otherRxn);
                        }
                    }
                }

                if(toRet.size()>0) {
                    toRet.add(rxn);
                    visitedPairs.put(react,pairingMol);
                    visitedReactions.add(rxn);
                    return toRet;
                }
            }


        }
        return toRet;
    }
}

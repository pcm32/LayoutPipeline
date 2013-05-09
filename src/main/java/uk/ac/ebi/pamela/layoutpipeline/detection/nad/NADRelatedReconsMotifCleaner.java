package uk.ac.ebi.pamela.layoutpipeline.detection.nad;

import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.pamela.layoutpipeline.detection.ReactionMotifDetector;
import uk.ac.ebi.pamela.layoutpipeline.detection.ReconsMotifCleaner;
import uk.ac.ebi.pamela.layoutpipeline.detection.SameMainReactProdMotifDet;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 9/5/13
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */
public class NADRelatedReconsMotifCleaner implements ReconsMotifCleaner {

    ReactionMotifDetector rxnMotDet;

    /**
     * Cleans the reconstruction from duplicate reactions that use alternative versions of NAD(P)H, giving preference to the
     * most general form.
     */
    public NADRelatedReconsMotifCleaner() {

    }

    @Override
    public void cleanRecons(Reconstruction recons) {
        /**
         * This first part looks for reactions that share reactant and products and that the differences is only
         * explained by NAD(P)(H) species. The less general reactions are removed. The NADPHReactionSReducer() however
         * doesn't handle the case where NADPH and NADH reactions run in parallel and should be replaced by a single
         * NAD(P)H reaction.
         */
        rxnMotDet = new SameMainReactProdMotifDet(recons);
        Set<MetabolicReaction> rxns = rxnMotDet.nextCompliantGroup();
        NADPHReactionSReducer reducer = new NADPHReactionSReducer();
        Set<MetabolicReaction> toRm = new HashSet<MetabolicReaction>();
        while(rxns!=null) {
            Set<MetabolicReaction> auxToRM = reducer.reduceReactions(rxns);
            toRm.addAll(auxToRM);
            if(auxToRM.size()==0) {
                /**
                 * TODO : We can try integrating here the parallel NADPH and NADH reactions into a single NAD(P)H
                 * If reactions for such integration are present, they should be available in the rxns Set.
                 */
            }
            rxns = rxnMotDet.nextCompliantGroup();
        }

        for (MetabolicReaction rxnToRm : toRm) {
            recons.reactome().remove(rxnToRm);
        }

        /**
         * If there are still reactions remaining in
         */


    }

}

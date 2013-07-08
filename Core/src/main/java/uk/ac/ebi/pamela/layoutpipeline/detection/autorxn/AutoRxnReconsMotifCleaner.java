package uk.ac.ebi.pamela.layoutpipeline.detection.autorxn;

import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.pamela.layoutpipeline.detection.ReactionMotifDetector;
import uk.ac.ebi.pamela.layoutpipeline.detection.ReconsMotifCleaner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 9/5/13
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public class AutoRxnReconsMotifCleaner implements ReconsMotifCleaner {

    /**
     * Cleans reconstructions from reactions that start and stop from the same small molecule, which are mostly artifacts.
     * This will also remove polymeric reactions if they use the same compound.
     *
     */
    public AutoRxnReconsMotifCleaner() {
    }

    @Override
    public void cleanRecons(Reconstruction recons) {
        Set<MetabolicReaction> toRem = new HashSet<MetabolicReaction>();
        ReactionMotifDetector detector = new AutoRxnMotifDetector(recons);

        Set<MetabolicReaction> rxnsInMotif = detector.nextCompliantGroup();
        while (rxnsInMotif!=null) {
            toRem.addAll(rxnsInMotif);
            rxnsInMotif = detector.nextCompliantGroup();
        }

        for (MetabolicReaction rxnToRem : toRem) {
            recons.reactome().remove(rxnToRem);
        }
    }
}

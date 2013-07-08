package uk.ac.ebi.pamela.layoutpipeline.detection.nad;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.pamela.layoutpipeline.detection.MoleculeDetector;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/5/13
 * Time: 12:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractNADRxnParticipantSetDetector {

    protected boolean isSetWithProtons(Set<Metabolite> mols,
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
}

package uk.ac.ebi.pamela.layoutpipeline.detection.nad;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.pamela.layoutpipeline.detection.MoleculeDetector;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/5/13
 * Time: 12:12
 * To change this template use File | Settings | File Templates.
 */
public class NADHRxnParticipantSetDetector extends AbstractNADRxnParticipantSetDetector implements RxnParticipantSetDetector {

    MoleculeDetector nadhDetector = new NADHDetector();
    MoleculeDetector nadDectector = new NADDetector();

    @Override
    public boolean isSet(Set<Metabolite> metabs) {
        return isSetWithProtons(metabs, nadhDetector, nadDectector);
    }

}

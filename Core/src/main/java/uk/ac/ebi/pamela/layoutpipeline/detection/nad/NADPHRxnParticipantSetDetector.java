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
public class NADPHRxnParticipantSetDetector extends AbstractNADRxnParticipantSetDetector implements RxnParticipantSetDetector {

    MoleculeDetector nadphDetector = new NADPHDetector();
    MoleculeDetector nadpDectector = new NADPDetector();

    @Override
    public boolean isSet(Set<Metabolite> metabs) {
        return isSetWithProtons(metabs, nadphDetector, nadpDectector);
    }

}

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
public class NADP_P_HRxnParticipantSetDetector extends AbstractNADRxnParticipantSetDetector implements RxnParticipantSetDetector {

    MoleculeDetector nad_p_hDetector = new NAD_P_HDetector();
    MoleculeDetector nad_p_Dectector = new NAD_P_Detector();

    @Override
    public boolean isSet(Set<Metabolite> metabs) {
        return isSetWithProtons(metabs, nad_p_hDetector, nad_p_Dectector);
    }

}

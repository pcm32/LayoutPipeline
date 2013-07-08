package uk.ac.ebi.pamela.layoutpipeline.detection.nad;

import uk.ac.ebi.mdk.domain.entity.Metabolite;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 13/5/13
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public interface RxnParticipantSetDetector {

    /**
     * Returns true if the given set of metabolites matches the set defined in the implementation.
     *
     * @param metabs belonging to a reaction.
     * @return
     */
    public boolean isSet(Set<Metabolite> metabs);

}

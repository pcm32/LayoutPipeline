package uk.ac.ebi.pamela.layoutpipeline.detection;

import uk.ac.ebi.mdk.domain.entity.Metabolite;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/5/13
 * Time: 17:49
 * To change this template use File | Settings | File Templates.
 */
public interface MoleculeDetector {

    public boolean isMolecule(Metabolite met);

}

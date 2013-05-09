package uk.ac.ebi.pamela.layoutpipeline.detection.nad;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.*;
import uk.ac.ebi.pamela.layoutpipeline.detection.AbstractMoleculeDetector;
import uk.ac.ebi.pamela.layoutpipeline.detection.MoleculeDetector;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 9/5/13
 * Time: 08:22
 * To change this template use File | Settings | File Templates.
 */
public class NAD_P_Detector extends AbstractMoleculeDetector implements MoleculeDetector {

    public NAD_P_Detector() {
        super();
        this.chemIdents = new LinkedList<Identifier>();
        this.chemIdents.add(new ChEBIIdentifier("CHEBI:25524"));
        this.chemIdents.add(BioCycChemicalIdentifier.meta("NAD-P-OR-NOP"));
        this.names = new LinkedList<String>();
        this.names.add("nad(p)");
        this.names.add("nad(p)+");
    }

}

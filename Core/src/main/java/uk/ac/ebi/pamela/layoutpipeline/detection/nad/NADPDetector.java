package uk.ac.ebi.pamela.layoutpipeline.detection.nad;

import uk.ac.ebi.mdk.domain.identifier.BioCycChemicalIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.PubChemCompoundIdentifier;
import uk.ac.ebi.pamela.layoutpipeline.detection.AbstractMoleculeDetector;
import uk.ac.ebi.pamela.layoutpipeline.detection.MoleculeDetector;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/5/13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public class NADPDetector extends AbstractMoleculeDetector implements MoleculeDetector {

    public NADPDetector() {
        super();
        chemIdents.add(new ChEBIIdentifier("CHEBI:18009"));
        chemIdents.add(new PubChemCompoundIdentifier("5886"));
        chemIdents.add(new PubChemCompoundIdentifier("15938972"));
        chemIdents.add(new KEGGCompoundIdentifier("C00006"));
        chemIdents.add(BioCycChemicalIdentifier.meta("NADP"));

        names.add("nadp");
        names.add("nadp+");
        names.add("nadp(+)");
    }

}

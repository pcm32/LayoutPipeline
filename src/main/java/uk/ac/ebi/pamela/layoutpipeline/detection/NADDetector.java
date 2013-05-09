package uk.ac.ebi.pamela.layoutpipeline.detection;

import uk.ac.ebi.mdk.domain.identifier.*;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/5/13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public class NADDetector extends AbstractMoleculeDetector implements MoleculeDetector {

    public NADDetector() {
        chemIdents = new LinkedList<Identifier>();
        chemIdents.add(new ChEBIIdentifier("CHEBI:15846"));
        chemIdents.add(new PubChemCompoundIdentifier("5893"));
        chemIdents.add(new KEGGCompoundIdentifier("C00003"));
        chemIdents.add(BioCycChemicalIdentifier.meta("NAD"));

        names = new LinkedList<String>();
        names.add("nad+");
        names.add("nad(+)");
        names.add("nad");
        names.add("Nicotinamide adenine dinucleotide");
    }

}

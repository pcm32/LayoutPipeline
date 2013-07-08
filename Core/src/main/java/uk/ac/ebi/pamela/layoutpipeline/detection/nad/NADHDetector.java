package uk.ac.ebi.pamela.layoutpipeline.detection.nad;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.*;
import uk.ac.ebi.pamela.layoutpipeline.detection.AbstractMoleculeDetector;
import uk.ac.ebi.pamela.layoutpipeline.detection.MoleculeDetector;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/5/13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public class NADHDetector extends AbstractMoleculeDetector implements MoleculeDetector {

    public NADHDetector() {
        chemIdents = new LinkedList<Identifier>();
        chemIdents.add(new ChEBIIdentifier("CHEBI:16908"));
        chemIdents.add(new PubChemCompoundIdentifier("439153"));
        chemIdents.add(new KEGGCompoundIdentifier("C00004"));
        chemIdents.add(BioCycChemicalIdentifier.meta("NADH"));

        names = new LinkedList<String>();
        names.add("nadh");
        names.add("nicotinamide adenine dinucleotide (reduced)");
    }

}

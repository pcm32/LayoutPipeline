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
public class NADPHDetector extends AbstractMoleculeDetector implements MoleculeDetector {

    public NADPHDetector() {
        super();
        chemIdents.add(new ChEBIIdentifier("CHEBI:16474"));
        chemIdents.add(new ChEBIIdentifier("CHEBI:57783"));
        chemIdents.add(new PubChemCompoundIdentifier("22833512"));
        chemIdents.add(new KEGGCompoundIdentifier("C00005"));
        chemIdents.add(BioCycChemicalIdentifier.meta("NADPH"));

        names.add("nadph");
        names.add("TPNH");
    }

}

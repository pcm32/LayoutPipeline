package uk.ac.ebi.pamela.layoutpipeline.detection;

import uk.ac.ebi.mdk.domain.identifier.*;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 9/5/13
 * Time: 08:26
 * To change this template use File | Settings | File Templates.
 */
public class NAD_P_HDetector extends AbstractMoleculeDetector implements MoleculeDetector {

    public NAD_P_HDetector() {
        this.chemIdents = new LinkedList<Identifier>();
        this.chemIdents.add(new ChEBIIdentifier("CHEBI:13392"));
        //this.chemIdents.add(new PubChemCompoundIdentifier("nad(p)h"));
        //this.chemIdents.add(new KEGGCompoundIdentifier("nad(p)h"));
        this.chemIdents.add(BioCycChemicalIdentifier.meta("NADH-P-OR-NOP"));
        this.names = new LinkedList<String>();
        this.names.add("nad(p)h");
    }
}

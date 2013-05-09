package uk.ac.ebi.pamela.layoutpipeline.detection;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.identifier.Identifier;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 8/5/13
 * Time: 18:15
 * To change this template use File | Settings | File Templates.
 */
public class AbstractMoleculeDetector {
    Collection<Identifier> chemIdents;
    Collection<String> names;

    public AbstractMoleculeDetector() {
        chemIdents = new LinkedList<Identifier>();
        names = new LinkedList<String>();
    }

    public boolean isMolecule(Metabolite met) {
        if(chemIdents.contains(met.getIdentifier()))
            return true;
        if(names.contains(met.getName().toLowerCase()))
            return true;
        return false;
    }
}

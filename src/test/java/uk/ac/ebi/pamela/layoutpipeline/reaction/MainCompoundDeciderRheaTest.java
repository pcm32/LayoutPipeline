package uk.ac.ebi.pamela.layoutpipeline.reaction;

import org.junit.Test;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Reaction;

import java.util.Collection;
import static org.junit.Assert.*;

/**
 * User: conesa
 * Date: 28/03/2013
 * Time: 15:25
 */
public class MainCompoundDeciderRheaTest {

    private static final String CHEBIID_TEST = "CHEBI:71045";

    @Test
    public void testGetMainCompounds() throws Exception {


        MainCompoundDeciderRhea mcdRhea = new MainCompoundDeciderRhea();

        RheaRecursiveReactionGetter rrg = new RheaRecursiveReactionGetter(0,null,null);

        Compound comp = rrg.getRheaCompound(CHEBIID_TEST);

        Collection<Reaction> reactions = rrg.getReactionsForChemical(comp);

        Reaction reaction = reactions.iterator().next();

        Collection<Compound> compounds = mcdRhea.getMainCompounds(reaction,comp);

        // This should return all the compounds
        assertEquals("Same number of compounds", reaction.getLeftSide().size()+ reaction.getRightSide().size() , compounds.size());



    }
}

package uk.ac.ebi.pamela.layoutpipeline.reaction;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.rhea.domain.Compound;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * User: conesa
 * Date: 22/03/2013
 * Time: 17:07
 */
public class RheaRecursiveReactionGetterTest {


    private static final String CHEBIID_TEST = "CHEBI:71045";

    @Test
    public void testGetRheaCompound(){
        RheaRecursiveReactionGetter rheaReactionRetriever = new RheaRecursiveReactionGetter(1,null,null);


        // bisdemethoxycurcumin: http://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI%3A71045&conversationContext=b
        //
        Compound compound = rheaReactionRetriever.getRheaCompound(CHEBIID_TEST);

        assertNotNull(compound);

        assertEquals("getRheaCompound: returns correct id", CHEBIID_TEST, compound.getAccession());




    }


    @Test
    public void testGetReactions() throws Exception {


        RheaRecursiveReactionGetter rheaReactionRetriever = new RheaRecursiveReactionGetter(0,null,null);

        Compound compound = rheaReactionRetriever.getRheaCompound(CHEBIID_TEST);

        Collection<MetabolicReaction> reactions = rheaReactionRetriever.getReactions(compound);

        // Reactions must be 2....
        assertEquals("Number of returned reaction test for " + CHEBIID_TEST, 2, reactions.size());



    }


    @Test
    public void testGetReactionsRecursivenes() throws Exception {


        // These chebiids are for: H2O, H, CO2 and  CoA
        CurrencyCompoundDeciderByList ccdl = new CurrencyCompoundDeciderByList(Arrays.asList(new String[]{"CHEBI:15377", "CHEBI:15378", "CHEBI:16526", "CHEBI:57287"}));
        MainCompoundDecider mcdr = new MainCompoundDeciderRhea();

        RheaRecursiveReactionGetter rheaReactionRetriever = new RheaRecursiveReactionGetter(1,ccdl,mcdr);

        Compound compound = rheaReactionRetriever.getRheaCompound(CHEBIID_TEST);

        Collection<MetabolicReaction> reactions = rheaReactionRetriever.getReactions(compound);

        /**
        First iteration query for bisdemethoxycurcumin, we should get 2 reactions:

        (4-coumaroyl)acetyl-CoA + 4-coumaroyl-CoA + H2O <?> bisdemethoxycurcumin + CO2 + 2 CoA
         2 4-coumaroyl-CoA + H2O + H+ + malonyl-CoA <?> bisdemethoxycurcumin + 2 CO2 + 3 CoA

        And 4 compounds should be selected for the next itereation (excluding the already visited one: bisdemethoxycurcumin.

        Second round should be for:
        (4-coumaroyl)acetyl-CoA
        4-coumaroyl-CoA
        2 4-coumaroyl-CoA
        malonyl-CoA




        // Reactions must be ?....
        assertEquals("Number of recursive returned reactions test for " + CHEBIID_TEST, 2, reactions.size());


        **/


    }


}

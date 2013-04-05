package uk.ac.ebi.pamela.layoutpipeline.reaction;

import org.junit.Test;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
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
    private static final String CHEBIID_SPECIES_TEST = "CHEBI:27732";

    @Test
    public void testGetRheaCompound(){
        RheaRecursiveReactionGetter rheaReactionRetriever = new RheaRecursiveReactionGetter(1,null,null,null);


        // bisdemethoxycurcumin: http://www.ebi.ac.uk/chebi/searchId.do?chebiId=CHEBI%3A71045&conversationContext=b
        //
        Compound compound = rheaReactionRetriever.getRheaCompound(CHEBIID_TEST);

        assertNotNull(compound);

        assertEquals("getRheaCompound: returns correct id", CHEBIID_TEST, compound.getAccession());




    }


    @Test
    public void testGetReactions() throws Exception {


        RheaRecursiveReactionGetter rheaReactionRetriever = new RheaRecursiveReactionGetter(0,null,null,null);

        Compound compound = rheaReactionRetriever.getRheaCompound(CHEBIID_TEST);

        Collection<MetabolicReaction> reactions = rheaReactionRetriever.getReactions(compound);

        // Reactions must be 2....
        assertEquals("Number of returned reaction test for " + CHEBIID_TEST, 2, reactions.size());



    }


    @Test
         public void testGetReactionsRecursivenessSimple() throws Exception {


        // These chebiids are for: H2O, H, CO2 and  CoA, plus 4-coumaroyl-CoA and malonyl-CoA (to simplify the output)
        CurrencyCompoundDeciderByList ccdl = new CurrencyCompoundDeciderByList(Arrays.asList(new String[]{"CHEBI:15377", "CHEBI:15378", "CHEBI:16526", "CHEBI:57287", "CHEBI:57355", "CHEBI:57384" }));
        MainCompoundDecider mcdr = new MainCompoundDeciderRhea();

        RheaRecursiveReactionGetter rheaReactionRetriever = new RheaRecursiveReactionGetter(1,ccdl,mcdr,null);

        Compound compound = rheaReactionRetriever.getRheaCompound(CHEBIID_TEST);

        Collection<MetabolicReaction> reactions = rheaReactionRetriever.getReactions(compound);

        /**
         First iteration query for bisdemethoxycurcumin, we should get 2 reactions:

         RHEA:35119 --> (4-coumaroyl)acetyl-CoA + 4-coumaroyl-CoA + H2O <?> bisdemethoxycurcumin + CO2 + 2 CoA   -1050050211
         RHEA:34803 -->  2 4-coumaroyl-CoA + H2O + H+ + malonyl-CoA <?> bisdemethoxycurcumin + 2 CO2 + 3 CoA

         And 1 compounds should be selected for the next iteration (excluding the already visited one: bisdemethoxycurcumin).

         Second round should be for:
         (4-coumaroyl)acetyl-CoA     CHEBI:71211     --> 2 reactions -> 1
         ________________________
         Total              3


         there must be 2 + 1 -> 3

         **/

        // Reactions must be 58....
        assertEquals("Number of recursive returned reactions test for " + CHEBIID_TEST, 3, reactions.size());

    }

    @Test
    public void testGetReactionsRecursivenessComplex() throws Exception {


        // These chebiids are for: H2O, H, CO2 and  CoA
        CurrencyCompoundDeciderByList ccdl = new CurrencyCompoundDeciderByList(Arrays.asList(new String[]{"CHEBI:15377", "CHEBI:15378", "CHEBI:16526", "CHEBI:57287"}));
        MainCompoundDecider mcdr = new MainCompoundDeciderRhea();

        RheaRecursiveReactionGetter rheaReactionRetriever = new RheaRecursiveReactionGetter(1,ccdl,mcdr,null);

        Compound compound = rheaReactionRetriever.getRheaCompound(CHEBIID_TEST);

        Collection<MetabolicReaction> reactions = rheaReactionRetriever.getReactions(compound);

        /**
         First iteration query for bisdemethoxycurcumin (CHEBI:71045) , we should get 2 reactions:

         RHEA:35119 --> (4-coumaroyl)acetyl-CoA + 4-coumaroyl-CoA + H2O <?> bisdemethoxycurcumin + CO2 + 2 CoA   -1050050211
         RHEA:34803 -->  2 4-coumaroyl-CoA + H2O + H+ + malonyl-CoA <?> bisdemethoxycurcumin + 2 CO2 + 3 CoA

         And 3 compounds should be selected for the next iteration (excluding the already visited one: bisdemethoxycurcumin.

         Second round should be for:
         (4-coumaroyl)acetyl-CoA     CHEBI:71211     --> 2 reactions -> 1
         malonyl-CoA                 CHEBI:57384     --> 44 reactions -> 42 (RHEA:34803 from first iteration, and RHEA:35115 from previous line)
         4-coumaroyl-CoA             CHEBI:57355     --> 14 reactions -> 7 (5 shared with CHEBI:57384 and the 2 initial (appears twice))

                                                        ________________________
                                                        Total            50


         there must be 2 + 50 -> 52

         **/

        // Reactions must be 52....
        assertEquals("Number of returned reactions, complex test, for  " + CHEBIID_TEST, 52, reactions.size());

    }

    @Test
    public void testGetReactionsRecursivenessSimpleWithSpecie() throws Exception {


        // These chebiids are for: H2O, H, CO2 and  CoA, plus 4-coumaroyl-CoA and malonyl-CoA (to simplify the output)
        CurrencyCompoundDeciderByList ccdl = new CurrencyCompoundDeciderByList(Arrays.asList(new String[]{"CHEBI:15377", "CHEBI:15378", "CHEBI:16526", "CHEBI:57287", "CHEBI:57355", "CHEBI:57384" }));
        MainCompoundDecider mcdr = new MainCompoundDeciderRhea();

        Taxonomy taxon = new Taxonomy();
        taxon.setAccession("hola");

        RheaRecursiveReactionGetter rheaReactionRetriever = new RheaRecursiveReactionGetter(1,ccdl,mcdr,taxon);

        Compound compound = rheaReactionRetriever.getRheaCompound(CHEBIID_SPECIES_TEST);

        Collection<MetabolicReaction> reactions = rheaReactionRetriever.getReactions(compound);

        /**
         First iteration query for bisdemethoxycurcumin, we should get 2 reactions:

         RHEA:35119 --> (4-coumaroyl)acetyl-CoA + 4-coumaroyl-CoA + H2O <?> bisdemethoxycurcumin + CO2 + 2 CoA   -1050050211
         RHEA:34803 -->  2 4-coumaroyl-CoA + H2O + H+ + malonyl-CoA <?> bisdemethoxycurcumin + 2 CO2 + 3 CoA

         And 1 compounds should be selected for the next iteration (excluding the already visited one: bisdemethoxycurcumin).

         Second round should be for:
         (4-coumaroyl)acetyl-CoA     CHEBI:71211     --> 2 reactions -> 1
         ________________________
         Total              3


         there must be 2 + 1 -> 3

         **/

        // Reactions must be 58....
        assertEquals("Number of recursive returned reactions test for " + CHEBIID_SPECIES_TEST, 3, reactions.size());

    }
}

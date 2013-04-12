/*
 * Copyright (C) 2013 EMBL-EBI
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

        Collection<RheaReactionWrapper> reactions = rrg.getReactionsForChemical(comp);

        RheaReactionWrapper wrappedReaction = reactions.iterator().next();

        Collection<Compound> compounds = mcdRhea.getMainCompounds(wrappedReaction,comp);

        Reaction reaction = wrappedReaction.getRheaReaction();

        // This should return all the compounds
        assertEquals("Same number of compounds", reaction.getLeftSide().size()+ reaction.getRightSide().size() , compounds.size());



    }
}

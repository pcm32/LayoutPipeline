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

import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.ReactionParticipant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * User: conesa
 * Date: 28/03/2013
 * Time: 14:21
 */
public class MainCompoundDeciderRhea implements MainCompoundDecider<Compound, RheaReactionWrapper> {


    public Collection<Compound> getMainCompounds(RheaReactionWrapper rheaWrapper, Compound compound) {

        Reaction rxn = rheaWrapper.getRheaReaction();

        Collection<ReactionParticipant> allParticipants = new ArrayList<ReactionParticipant>(rxn.getLeftSide());


        allParticipants.addAll(rxn.getRightSide());

        Collection<Compound> allCompounds = new HashSet<Compound>();

        for (ReactionParticipant participant: allParticipants){

            allCompounds.add(participant.getCompound());
        }

        return allCompounds;

    }

}

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
public class MainCompoundDeciderRhea implements MainCompoundDecider<Compound, Reaction> {


    public Collection<Compound> getMainCompounds(Reaction rxn, Compound compound) {

        Collection<ReactionParticipant> allParticipants = new ArrayList<ReactionParticipant>(rxn.getLeftSide());

        allParticipants.addAll(rxn.getRightSide());

        Collection<Compound> allCompounds = new HashSet<Compound>();

        for (ReactionParticipant participant: allParticipants){

            allCompounds.add(participant.getCompound());
        }

        return allCompounds;

    }

}

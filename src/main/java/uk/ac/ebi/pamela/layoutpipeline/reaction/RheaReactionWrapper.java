package uk.ac.ebi.pamela.layoutpipeline.reaction;

import uk.ac.ebi.rhea.domain.Reaction;

/**
 *
 * Implement a wrapper since, the hashcode method of Rhea is nor returning the same code for 2 same reactions.
 *
 * User: conesa
 * Date: 02/04/2013
 * Time: 15:16
 */
public class RheaReactionWrapper {
    Reaction rheaReaction;
    public RheaReactionWrapper(Reaction rheaReaction) {

        this.rheaReaction = rheaReaction;

    }

    @Override
    public int hashCode(){

        return rheaReaction.getId().hashCode();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RheaReactionWrapper)) return false;
        final RheaReactionWrapper otherWrapper = (RheaReactionWrapper) o;
        boolean sameId = (this.rheaReaction.getId().equals(otherWrapper.getRheaReaction().getId()));

        return sameId;
    }

    public Reaction getRheaReaction(){
        return this.rheaReaction;
    }

    public String toString(){
        return rheaReaction.getId().toString();
    }
}

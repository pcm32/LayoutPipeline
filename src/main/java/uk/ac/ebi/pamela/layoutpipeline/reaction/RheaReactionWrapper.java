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

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

import java.util.Collection;

/**
 * @name    MainCompoundDecider
 * @date    2013.03.21
 * @version $Rev$ : Last Changed $Date$
 * @author  Pablo Moreno <pablacious at users.sf.net>
 * @author  $Author$ (this version)
 * @brief   Interface to describe the functionality of isolating a main compounds related to a given compound
 *          that participates in a reaction.
 *
 */
public interface MainCompoundDecider<C, R> {


    /**
     * Given a reaction and compound participating in that reaction, this method produces a list of main compounds
     * in the reaction, which are directly related to the compound c specified.
     * 
     * @param rxn where main compounds are to be identified.
     * @param compound to which the main compounds should be related to.
     * @return a collection of main compounds.
     */
    public Collection<C> getMainCompounds(R rxn, C compound);
}

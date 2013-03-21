/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

/**
 * @name AbstractRecursiveReactionGetter
 * @date 2013.03.21
 * @version $Rev$ : Last Changed $Date$
 * @author Pablo Moreno <pablacious at users.sf.net>
 * @author $Author$ (this version)
 * @brief ...class description...
 *
 */
public abstract class AbstractRecursiveReactionGetter<C, R> {

    private Set<C> visitedChemicals;
    private Set<R> visitedReactions;
    private CurrencyCompoundDecider<C, R> currencyDec;
    private MainCompoundDecider<C, R> mainCompDec;
    private Integer depth;

    public AbstractRecursiveReactionGetter(Integer depth, CurrencyCompoundDecider<C, R> currencyDec, MainCompoundDecider<C, R> mainCompDec) {        
        this.currencyDec = currencyDec;
        this.mainCompDec = mainCompDec;
    }

    /**
     * Triggers the recursive retrieval of reactions up to the depth set in the constructor, starting from this
     * metabolite.
     *
     * @param chem
     * @return
     * @throws SQLException
     */
    public Collection<MetabolicReaction> getReactions(C chem) throws SQLException {
        this.visitedChemicals = new HashSet<C>();
        this.visitedReactions = new HashSet<R>();
        return getReactions(chem, depth);
    }

    private Collection<MetabolicReaction> getReactions(C chem, Integer depth) throws SQLException {
        Collection<MetabolicReaction> metabRxns = new ArrayList<MetabolicReaction>();
        Collection<R> rxns = getReactionsForChemical(chem);
        Collection<C> chemsToVisit = new ArrayList<C>();

        visitedChemicals.add(chem);

        for (R rxn : rxns) {
            // We skip reactions already visited.
            if (visitedReactions.contains(rxn)) {
                continue;
            }

            MetabolicReaction r = convertReaction(rxn);
            metabRxns.add(r);

            visitedReactions.add(rxn);


            if (depth > 0) {
                Collection<C> mainComps = mainCompDec.getMainCompounds(rxn,chem);
                Collection<C> currencies = currencyDec.getCurrencyMetabolites(rxn);
                visitedChemicals.addAll(currencies);

                for (C participant : mainComps) {
                    if (visitedChemicals.contains(participant)) {
                        continue;
                    }
                    chemsToVisit.add(participant);
                }
            }
        }
        if (depth > 0) {
            for (C chemical : chemsToVisit) {
                metabRxns.addAll(getReactions(chemical, depth - 1));
            }
        }

        return metabRxns;
    }

    /**
     * This method needs to be implemented so that for a given chemical participant, all reactions in which that
     * participant is present are returned.
     *
     * @param chem
     * @return collection of reactions where chem participates.
     */
    abstract Collection<R> getReactionsForChemical(C chem);

    /**
     * Converts the independent model reaction into a metabolic reaction.
     *
     * @param rxn the rxn in the external domain model to be transformed to the mdk model reaction.
     * @return the converted metabolic reaction.
     */
    abstract MetabolicReaction convertReaction(R rxn);
}

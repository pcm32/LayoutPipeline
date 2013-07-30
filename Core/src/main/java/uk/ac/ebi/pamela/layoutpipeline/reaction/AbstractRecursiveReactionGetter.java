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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.pamela.layoutpipeline.utils.ReactionRecursionDepthMonitor;

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
    protected CurrencyCompoundDecider<C, R> currencyDec;
    private MainCompoundDecider<C, R> mainCompDec;
    private Integer depth;
    private Integer maxReactions;
    private Integer lastDepthUsed;
    private Integer minDepth;

    private static final Logger LOGGER = Logger.getLogger(AbstractRecursiveReactionGetter.class.getName());

    public AbstractRecursiveReactionGetter(Integer depth, CurrencyCompoundDecider<C, R> currencyDec, MainCompoundDecider<C, R> mainCompDec) {
        this.depth = depth;
        this.currencyDec = currencyDec;
        this.mainCompDec = mainCompDec;
    }

    /**
     * Sets the maximum reactions that the recursion should get. It also sets the minimum depth to one unit below the current
     * depth as a default.
     *
     * @param maxReactions
     */
    public void setMaxReactions(Integer maxReactions) {
        this.maxReactions = maxReactions;
        this.minDepth = depth - 1;
    }

    /**
     * Sets a minimum depth for the recursion of reactions.
     *
     * @param minDepth
     */
    public void setMinDepth(Integer minDepth) {
        this.minDepth = minDepth;
    }

    /**
     * Gets the depth used for the last reaction retrieval operation.
     *
     * @return last depth used.
     */
    public Integer getLastDepthUsed() {
        return this.lastDepthUsed;
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
        this.lastDepthUsed = depth;
        Collection<MetabolicReaction> reactions = getReactions(chem, depth);
        if(maxReactions!=null && reactions.size()>maxReactions) {
            int depthUsed = depth;
            while (reactions.size()>maxReactions && depthUsed > minDepth) {
                depthUsed--;
                this.visitedChemicals.clear();
                this.visitedReactions.clear();
                this.lastDepthUsed = depthUsed;
                reactions = getReactions(chem,depthUsed);
            }
        }
        return reactions;
    }

    private Collection<MetabolicReaction> getReactions(C chem, Integer depth) throws SQLException {


        LOGGER.debug ("Getting reactions for chemical " + chem.toString() + ", depth : " + depth );

        Collection<MetabolicReaction> metabRxns = new ArrayList<MetabolicReaction>();
        Collection<R> rxns = getReactionsForChemical(chem);
        Collection<C> chemsToVisit = new HashSet<C>();

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
    
    public CurrencyCompoundDecider<C,R> getCurrencyDecider() {
        return currencyDec;
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
     * Converts the independent model reaction into a metabolic reaction. Given that the uniqueness of participants is
     * evaluated by the {@link uk.ac.ebi.mdk.domain.entity.Reconstruction} object through the UUID of the elements,
     * whenever a new reaction participant is added, it needs to have its UUID generated from a stable identifier IF the
     * same specie could be added again (because of the way in which the traversal of reactions go). This is normally
     * accomplished by using UUID.nameUUIDFromBytes() and giving the stable identifier as argument.
     *
     *
     * @param rxn the rxn in the external domain model to be transformed to the mdk model reaction.
     * @return the converted metabolic reaction.
     */
    abstract MetabolicReaction convertReaction(R rxn);

    boolean isReactionVisited(R reaction){
        return visitedReactions.contains(reaction);
    }
    void addVisitedReaction(R reaction){
        visitedReactions.add(reaction);
    }

}

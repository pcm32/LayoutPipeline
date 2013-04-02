package uk.ac.ebi.pamela.layoutpipeline.reaction;

import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.ReactionParticipant;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User: conesa
 * Date: 25/03/2013
 * Time: 13:26
 */
public class CurrencyCompoundDeciderByList implements CurrencyCompoundDecider<Compound,RheaReactionWrapper> {


    Collection<String> currencylist;

    CurrencyCompoundDeciderByList (Collection<String> currencylist){
        this.currencylist = currencylist;

    }

    /**
     * look inside the reactions compound if there is any currency metabolite (currency metabolites are stored in list)
     * So, no clever currency decider, if it's in the list...it's a currency metabolite.
     * @param rheaWrapper
     * @return
     */
    public Collection<Compound> getCurrencyMetabolites(RheaReactionWrapper rheaWrapper) {


        // If there is no currency list ...there will not be any currency metabolite
        if (currencylist == null || currencylist.size()==0) return null;

        Reaction rxn = rheaWrapper.getRheaReaction();

        // Currencies found array
        ArrayList<Compound> currenciesFound = new ArrayList<Compound>();

        // Join product and reactant sides.
        Collection<ReactionParticipant> participants = new ArrayList (rxn.getLeftSide());
        participants.addAll(rxn.getRightSide());

        // For each participants...
        for (ReactionParticipant compound: participants){

            String accession = compound.getCompound().getAccession();

            // If the accession is in the currency list...
            if (currencylist.contains(accession)){
                currenciesFound.add(compound.getCompound());
            }


        }

        return currenciesFound;

    }



}

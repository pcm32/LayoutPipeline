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
package uk.ac.ebi.pamela.layoutpipeline;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.pamela.layoutpipeline.reaction.*;
import uk.ac.ebi.pamela.layoutpipeline.utils.PropertiesUtil;
import uk.ac.ebi.rhea.domain.Compound;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import java.util.prefs.Preferences;

/**
 * @name RheaReactionListRetriever
 * @date 2013.04.05
 * @author Pablo Conesa
 *
 */
public class RheaReactionListRetriever extends AbstractReactionListRetriever implements ReactionListRetriever {

    private CurrencyCompoundDecider<Compound,RheaReactionWrapper> currencyDecider;
    private MainCompoundDecider<Compound,RheaReactionWrapper> mainCompoundDecider;
    private static List<String> currencies;
    private static final String DEFAULT_CURRENCIES = PropertiesUtil.getProperty("defaultRheaCurrencies");
    private static final Logger LOGGER = Logger.getLogger( RheaReactionListRetriever.class );

    private Integer depth;

    public RheaReactionListRetriever(Integer depth) throws IOException, SQLException {

        this.currencyDecider = new CurrencyCompoundDeciderByList(getCurrencyList());
        this.mainCompoundDecider = new MainCompoundDeciderRhea();
        this.depth = depth;
    }

    private List<String> getCurrencyList(){


        if (currencies == null){

            String currenciesS = PropertiesUtil.getPreference("rheaCurrencyList", DEFAULT_CURRENCIES);

            currencies = Arrays.asList(currenciesS.split("_"));

        }

        return currencies;

    }

    Iterable<MetabolicReaction> getReactions(Query query) {

        Collection<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();

        RheaRecursiveReactionGetter rxnGetter = new RheaRecursiveReactionGetter(depth,currencyDecider,mainCompoundDecider,query.getOrganismIdentifier());

        Compound compound =  rxnGetter.getRheaCompound(query.getChemicalIdentifier().getAccession());

        // If the compound is not in rhea...
        if (compound == null) {
            LOGGER.info("The compound " + query.getChemicalIdentifier() + " is not found in rhea");
            return reactions;
        }

        try {
            reactions = rxnGetter.getReactions(compound);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }

        return reactions;
    }
}

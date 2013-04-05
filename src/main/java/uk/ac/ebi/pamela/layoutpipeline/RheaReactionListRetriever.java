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
    private static final String DEFAULT_CURRENCIES = "CHEBI:15377_CHEBI:15378_CHEBI:15379_CHEBI:15422_CHEBI:18009_CHEBI:16474_CHEBI:16761_CHEBI:26078_CHEBI:15846_CHEBI:16908_CHEBI:17659_CHEBI:18361_CHEBI:15346_CHEBI:16526_CHEBI:16134_CHEBI:16027_CHEBI:16240_CHEBI:57287_CHEBI:15339_CHEBI:15351_CHEBI:15757_CHEBI:17200_CHEBI:17985_CHEBI:17980_CHEBI:15414_CHEBI:16680_CHEBI:30915_CHEBI:17361_CHEBI:16264_CHEBI:13390_CHEBI:16856_CHEBI:13392_CHEBI:17499_CHEBI:17552_CHEBI:32816_CHEBI:29101_CHEBI:17984_CHEBI:15428_CHEBI:15366_CHEBI:15741_CHEBI:18307_CHEBI:16110_CHEBI:16650_CHEBI:15996_CHEBI:16382_CHEBI:16556_CHEBI:16238_CHEBI:26836_CHEBI:30751_CHEBI:15903_CHEBI:17009_CHEBI:35366_CHEBI:16113_CHEBI:28260_CHEBI:15713_CHEBI:17053_CHEBI:17925_CHEBI:18050_CHEBI:17877_CHEBI:17677_CHEBI:15635_CHEBI:16214_CHEBI:15843_CHEBI:16842_CHEBI:17668_CHEBI:16695_CHEBI:16467_CHEBI:4917_CHEBI:17654_CHEBI:15978_CHEBI:4167";
    private static final Logger LOGGER = Logger.getLogger( RheaReactionListRetriever.class );

    public RheaReactionListRetriever(Taxonomy organism) throws IOException, SQLException {

        this.currencyDecider = new CurrencyCompoundDeciderByList(getCurrencyList());
        this.mainCompoundDecider = new MainCompoundDeciderRhea();
    }

    private List<String> getCurrencyList(){


        if (currencies == null){

            String currenciesS = PropertiesUtil.getPreference("rheaCurrencyList", DEFAULT_CURRENCIES);

            currencies = Arrays.asList(currenciesS.split("_"));

        }

        return currencies;

    }

    Iterable<MetabolicReaction> getReactions(Query query) {

        List<MetabolicReaction> reactions = new ArrayList<MetabolicReaction>();

        RheaRecursiveReactionGetter rxnGetter = new RheaRecursiveReactionGetter(2,currencyDecider,mainCompoundDecider,query.getOrganismIdentifier());

        return reactions;
    }
}

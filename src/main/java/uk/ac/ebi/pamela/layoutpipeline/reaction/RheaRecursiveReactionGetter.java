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

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.MetaboliteImpl;
import uk.ac.ebi.mdk.domain.entity.reaction.*;
import uk.ac.ebi.mdk.domain.identifier.CHEMBLIdentifier;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.pamela.layoutpipeline.utils.PropertiesUtil;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.ReactionParticipant;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.SearchOptions;
import uk.ac.ebi.rhea.mapper.SearchSwitch;
import uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader;
import uk.ac.ebi.rhea.mapper.db.RheaDbReader;

import java.io.IOException;
import java.sql.*;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;


/**
 * User: conesa
 * Date: 19/03/2013
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */
public class RheaRecursiveReactionGetter extends AbstractRecursiveReactionGetter<Compound,RheaReactionWrapper> {

private static final Logger LOGGER = Logger.getLogger(RheaRecursiveReactionGetter.class.getName());
private Connection rheaConnection = null;


    public RheaRecursiveReactionGetter(Integer depth, CurrencyCompoundDecider<Compound, RheaReactionWrapper> currencyDecider, MainCompoundDecider<Compound, RheaReactionWrapper> mainCompDecider) {
        super(depth,currencyDecider,mainCompDecider);

        connectToRhea();
    }

    private void connectToRhea(){


        // Get the connection properties
        String rheaUrl = PropertiesUtil.getProperty("rhea.url");
        String rheaUser = PropertiesUtil.getProperty("rhea.username");
        String rheaPassword = PropertiesUtil.getProperty("rhea.password");
        String rheaSchema = PropertiesUtil.getProperty("rhea.schema");

        // We should externalise this.
        try {
            rheaConnection = DriverManager.getConnection(rheaUrl, rheaUser, rheaPassword);

            if (rheaSchema !=null){

                Statement stmt = rheaConnection.createStatement();
                stmt.execute("alter session set current_schema = " + rheaSchema);
                stmt.close();

            }

            LOGGER.info("Connection successful to rhea db.");
        } catch (SQLException e) {
            LOGGER.error("Can't connect to rhea database: " + e.getMessage());
        }

    }
    @Override
    public Collection<RheaReactionWrapper> getReactionsForChemical(Compound chemical) {

        return getReactionsForChemical(chemical.getAccession());

    }
    public Collection<RheaReactionWrapper> getReactionsForChemical(String chemical) {

        try {

            // Fill searchOptions...for simple reactions.
            SearchOptions so = new SearchOptions();
            so.setSimpleSwitch(SearchSwitch.YES);

            EnumSet<uk.ac.ebi.rhea.domain.Direction> rheaDir = EnumSet.of(uk.ac.ebi.rhea.domain.Direction.UN);
            so.setDirection(rheaDir);
            so.setStatus("OK");

            LOGGER.info("Loading reactions for " + chemical);


            // Instantiate the compound db reader...
            RheaCompoundDbReader rheaCompoundDbReader = new RheaCompoundDbReader(rheaConnection);

            // Instantiate the rhea db reader  (for reactions)
            RheaDbReader rheaReader = new RheaDbReader(rheaCompoundDbReader);

            // Get the reactions for the search option
            Set<Reaction> reactionsId = rheaReader.findByCompoundAccession(chemical, so);

            Set<RheaReactionWrapper> reactions = new HashSet<RheaReactionWrapper>();

            for (Reaction reaction:reactionsId){

                Long rheaId = reaction.getId();


                LOGGER.info("Loading reaction " + rheaId);

                Reaction populatedReaction =rheaReader.findByReactionId(rheaId);

                RheaReactionWrapper rheaWrapper = new RheaReactionWrapper(populatedReaction);

                reactions.add(rheaWrapper);

            }

            return reactions;

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MapperException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;

    }

    MetabolicReaction convertReaction(RheaReactionWrapper rheawrapper){

        // Instantiate a metabolic reaction...
        MetabolicReaction mr = new MetabolicReactionImpl();

        Reaction rhea = rheawrapper.getRheaReaction();

        // Populate with Rhea reaction values
        mr.setDirection(Direction.UNKNOWN);

        // Populate MetabolicReaction collections
        for (ReactionParticipant rp :rhea.getLeftSide()){

            MetabolicParticipant met = convertRheaParticipantToMDKMetabolite(rp);
            mr.addReactant(met);
         }

        // Populate MetabolicReaction collections
        for (ReactionParticipant rp :rhea.getRightSide()){

            MetabolicParticipant met = convertRheaParticipantToMDKMetabolite(rp);

            if (met == null){
                LOGGER.info("Can't convert rhea participant into a MDK participant:" + rp.toString());
            } else {
                mr.addProduct(met);
            }

        }

        // Return it
        return mr;

    }

    private MetabolicParticipant convertRheaParticipantToMDKMetabolite(ReactionParticipant rp){

        Compound comp = rp.getCompound();

        if (comp != null){

            // Are all rhea compounds chebiId..?
            Metabolite metabolite = new MetaboliteImpl(new ChEBIIdentifier(comp.getAccession()),"",comp.getName());

            MetabolicParticipant met = new MetabolicParticipantImplementation(metabolite, Double.valueOf(rp.getCoefficient().getValue()));

            return met;
        }

        return null;


    }

    public Compound getRheaCompound(String chebiId){


        try {
            // Instantiate the reader
            RheaCompoundDbReader rheaCompoundReader = new RheaCompoundDbReader(rheaConnection);

            // Request for the compound
            Compound compound = rheaCompoundReader.findByAccession(chebiId);

            return compound;

        } catch (IOException e) {
            LOGGER.error("Can't instantiate RheaCompoundDbReader: " + e.getMessage());
        } catch (MapperException e) {
            LOGGER.error("Can't find compound " + chebiId + ": " + e.getMessage());
        }

        return null;

    }

}
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
import uk.ac.ebi.mdk.domain.entity.reaction.Direction;
import uk.ac.ebi.mdk.domain.identifier.ChEBIIdentifier;
import uk.ac.ebi.mdk.domain.identifier.SwissProtIdentifier;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.UniProtIdentifier;
import uk.ac.ebi.mdk.service.query.orthology.UniProtECNumber2OrganismProteinService;
import uk.ac.ebi.pamela.layoutpipeline.utils.PropertiesUtil;
import uk.ac.ebi.rhea.domain.*;
import uk.ac.ebi.rhea.mapper.MapperException;
import uk.ac.ebi.rhea.mapper.SearchOptions;
import uk.ac.ebi.rhea.mapper.SearchSwitch;
import uk.ac.ebi.rhea.mapper.db.RheaCompoundDbReader;
import uk.ac.ebi.rhea.mapper.db.RheaDbReader;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

import java.io.IOException;
import java.sql.*;
import java.util.*;


/**
 * User: conesa
 * Date: 19/03/2013
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */
public class RheaRecursiveReactionGetter extends AbstractRecursiveReactionGetter<Compound,RheaReactionWrapper> {

private static final Logger LOGGER = Logger.getLogger(RheaRecursiveReactionGetter.class.getName());
private Connection rheaConnection = null;

private Taxonomy specie;
private UniProtECNumber2OrganismProteinService uniProtSpecieServ = new UniProtECNumber2OrganismProteinService();
private SpecialCharacters xchars = SpecialCharacters.getInstance(null);

    public RheaRecursiveReactionGetter(Integer depth, CurrencyCompoundDecider<Compound, RheaReactionWrapper> currencyDecider, MainCompoundDecider<Compound, RheaReactionWrapper> mainCompDecider, Taxonomy specie) {
        super(depth,currencyDecider,mainCompDecider);
        this.specie = specie;
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

            // Get the reactions for the search option...this returns a lightweight reactions...we need to ask rhea to full populate it
            Set<Reaction> reactionsId = rheaReader.findByCompoundAccession(chemical, so);

            Set<RheaReactionWrapper> reactions = new HashSet<RheaReactionWrapper>();

            for (Reaction reaction:reactionsId){

                Long rheaId = reaction.getId();

                // If we already have the reaction in the visited list
                RheaReactionWrapper rheaWrapper = new RheaReactionWrapper(reaction);

                if (isReactionVisited(rheaWrapper)){

                    LOGGER.info("Avoiding load of reaction " + rheaId + ": already visited.");
                    continue;

                } else {

                    LOGGER.info("Loading reaction " + rheaId);

                    Reaction populatedReaction =rheaReader.findByReactionId(rheaId);

                    rheaWrapper = new RheaReactionWrapper(populatedReaction);

                    if (checkSpecieForReaction(populatedReaction)){

                        reactions.add(rheaWrapper);
                    } else {

                        addVisitedReaction(rheaWrapper);
                    }

                }

            }

            return reactions;

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MapperException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;

    }

    protected boolean checkSpecieForReaction(Reaction reaction){

        // If there's no specie information we will return the reaction
        if ((specie == null) || specie.getAccession().equals("")) return true;

        // Get the list of FamilyXRefs...
        Map<uk.ac.ebi.rhea.domain.Direction, Set<XRef>> familyXref = reaction.getFamilyXrefs();

        // If there is no Xref...we can't retrieve species information...return null
        if (familyXref == null) return false;

        // go through the map
        for (Map.Entry<uk.ac.ebi.rhea.domain.Direction, Set<XRef>> entry:familyXref.entrySet()){

            // Ignore undefined direction
            if (entry.getKey().equals(uk.ac.ebi.rhea.domain.Direction.UN)) continue;

            // Go through the set
            for (XRef xRef:entry.getValue()){

                // Pick the uniprot ids...
                if (xRef.getDatabase() == Database.UNIPROT){

                    LOGGER.debug("UniprotId  " + xRef.getName() + " found for RHEA:" + reaction.getId());

                    if (hasUniprotIdTheSpecie(xRef.getAccessionNumber())){
                        return true;
                    }
                }
            }

        }

        return false;
    }

    protected boolean hasUniprotIdTheSpecie (String uniprotId){

        //return true;

        UniProtIdentifier ident = new SwissProtIdentifier(uniprotId);
        Collection<Taxonomy> orgs = uniProtSpecieServ.getOrganismForProteinIdentifier(ident);

        Taxonomy orgIdent=null;

        // I will never be null
        if(!orgs.isEmpty())
            orgIdent = orgs.iterator().next();

        // Until we have the index return true
        return specie.equals(orgIdent);

    }

    MetabolicReaction convertReaction(RheaReactionWrapper rheawrapper){

        // Instantiate a metabolic reaction...
        MetabolicReaction mr = new MetabolicReactionImpl();

        Reaction rhea = rheawrapper.getRheaReaction();

        // Populate with Rhea reaction values
        mr.setDirection(Direction.UNKNOWN);

        Collection<Compound> currencyComps = getCurrencyDecider().getCurrencyMetabolites(rheawrapper);

        // Populate MetabolicReaction collections
        for (ReactionParticipant rp :rhea.getLeftSide()){

            MetabolicParticipant met = convertRheaParticipantToMDKMetabolite(rp);

            // Set currency compound...
            if(currencyComps.contains(rp.getCompound())){
                met.setSideCompound(true);
            }


            if (met == null){
                LOGGER.info("Can't convert rhea participant into a MDK participant:" + rp.toString());
            } else {
                mr.addReactant(met);
            }

         }

        // Populate MetabolicReaction collections
        for (ReactionParticipant rp :rhea.getRightSide()){

            MetabolicParticipant met = convertRheaParticipantToMDKMetabolite(rp);


            // Set currency compound...
            if(currencyComps.contains(rp.getCompound())){
                met.setSideCompound(true);
            }

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
            // Rhea compound name has some xml tags like <strong> <super> ....we need to clean this.
            String compoundName = xchars.xml2Display(comp.getName(), EncodingType.CHEBI_CODE);


            Metabolite metabolite = new MetaboliteImpl(new ChEBIIdentifier(comp.getAccession()),"",compoundName);

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
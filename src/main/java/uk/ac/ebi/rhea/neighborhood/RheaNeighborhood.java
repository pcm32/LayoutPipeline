package uk.ac.ebi.rhea.neighborhood;

import org.apache.log4j.Logger;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 20/03/2013
 * Time: 16:34
 * Provides a mechanism to query rhea for neighborhood reactions from a chebi id.
 * Singleton.
 */
public final class RheaNeighborhood {

    private static final Logger LOGGER = Logger.getLogger(RheaNeighborhood.class.getName());
    private static RheaNeighborhood instance;

    private Connection rheaConnection = null;
    private ECNumberChecker checker;
    private ExclusionMetaboliteChecker exclusionMetaboliteChecker;
    private ArrayList<String> alreadyVisited = new ArrayList<String>();


    // Avoid instantiation
    private RheaNeighborhood(ECNumberChecker ecChecker, ExclusionMetaboliteChecker exclusionMetaboliteChecker){
        connectToRhea();
        this.checker = ecChecker;
        this.exclusionMetaboliteChecker = exclusionMetaboliteChecker;

    }


    public static RheaNeighborhood getInstance(ECNumberChecker ecChecker, ExclusionMetaboliteChecker exclusionMetaboliteChecker){
        if (instance == null)
            instance = new RheaNeighborhood(ecChecker,exclusionMetaboliteChecker);

        return instance;

    }

    private void connectToRhea(){


        // Get the connection properties
        String rheaUrl = PropertiesUtil.getProperty("rhea.url");
        String rheaUser = PropertiesUtil.getProperty("rhea.username");
        String rheaPassword = PropertiesUtil.getProperty("rhea.password");

        // We should externalise this.
        try {
            rheaConnection = DriverManager.getConnection(rheaUrl, rheaUser, rheaPassword);
        } catch (SQLException e) {
            LOGGER.error("Can't connect to rhea database: " + e.getMessage());
        }

    }

    /**
     * Gets the neighborhood reactions from a chebiId, that matches the list of ec numbers.
     * @param chebiId : chebiID to query
     * @param neighborhoodSize : size of the neighbourhood ("radio")
     */
    private List<Reaction> getNeighborhood(String chebiId, int neighborhoodSize){


        // Clean the already visited list
        alreadyVisited.clear();

        try {

            // Fill searchOptions...for simple reactions.
            SearchOptions so = new SearchOptions();
            so.setSimpleSwitch(SearchSwitch.YES);
            so.setStatus("OK");

            // Instantiate the compound db reader...
            RheaCompoundDbReader rheaCompoundDbReader = new RheaCompoundDbReader(rheaConnection);

            // Instantiate the rhea db reader  (for reactions)
            RheaDbReader rheaReader = new RheaDbReader(rheaCompoundDbReader);

            // Get the reactions for the search option
           Set<Reaction> reactions = rheaReader.findByCompoundAccession(chebiId, so);



        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MapperException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    private Set<Reaction> getNeighborhood(Set<Reaction> reactions, int neighborsLeft){

        // Loop through the reactions...
        for (Reaction reaction: reactions){

            Collection<ReactionParticipant> participants = reaction.getReactantsSide();

            Collection<ReactionParticipant> products = reaction.getProductsSide();

            // Add the products to the participants...
            participants.addAll(products);

            // For each participant ...
            for (ReactionParticipant participant: participants){

                // Get the compound...
                Compound compound= participant.getCompound();

                // Check if the compound has been already used in a query

            }

        }

    }

}

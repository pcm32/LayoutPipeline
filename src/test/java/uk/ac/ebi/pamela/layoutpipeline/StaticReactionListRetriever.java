package uk.ac.ebi.pamela.layoutpipeline;

import java.util.*;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.ReconstructionImpl;

/**
 * Created with IntelliJ IDEA.
 * User: conesa
 * Date: 27/02/2013
 * Time: 10:20
 * To change this template use File | Settings | File Templates.
 */
public class StaticReactionListRetriever implements ReactionListRetriever {

    List<Reconstruction> recs = new ArrayList<Reconstruction>();

    public List<Reconstruction> getReactionsAsReconstructions(Query query) {

        if (recs.size() ==0) {

            recs.add(getReconstruction("Rec1"));
            recs.add(getReconstruction("Rec2"));
            recs.add(getReconstruction("Rec4"));
        }

        return recs;

    }

    private Reconstruction getReconstruction(String param1){

        Reconstruction rec = new ReconstructionImpl();

        // Fill the reconstruction


        // Return the reconstruction
        return rec;

    }
}

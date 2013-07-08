package uk.ac.ebi.pamela.layoutpipeline.reaction;

import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 20/6/13
 * Time: 22:12
 * To change this template use File | Settings | File Templates.
 */
public class RedundantMetabProcessor {

    Map<UUID,Metabolite> generatedMetabolites;

    public RedundantMetabProcessor() {
        this.generatedMetabolites = new HashMap<UUID, Metabolite>();
    }

    /**
     * Takes the metabolites participating in a reaction, checks whether they have been already generated for the
     * current reconstruction. If the metabolites (same UUID) have been already generated, then newer one is replaced by
     * the existing one.
     *
     * @param metReaction
     */
    public void processRedundantMetabolites(MetabolicReaction metReaction) {
        for (MetabolicParticipant metabolicParticipant : metReaction.getParticipants()) {
            UUID newMolUuid = metabolicParticipant.getMolecule().uuid();
            if(generatedMetabolites.containsKey(newMolUuid)) {
                metabolicParticipant.setMolecule(generatedMetabolites.get(newMolUuid));
            } else {
                generatedMetabolites.put(newMolUuid, metabolicParticipant.getMolecule());
            }
        }
    }

}

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

package uk.ac.ebi.pamela.layoutpipeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.ReconstructionImpl;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicParticipant;
import uk.ac.ebi.mdk.domain.entity.reaction.MetabolicReaction;

/**
 * @name    AbstractReactionListRetriever
 * @date    2013.03.19
 * @version $Rev$ : Last Changed $Date$
 * @author  Pablo Moreno <pablacious at users.sf.net>
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public abstract class AbstractReactionListRetriever {
    
    Map<UUID,Metabolite> generatedMetabolites;

    abstract Iterable<MetabolicReaction> getReactions(Query query);

    public List<Reconstruction> getReactionsAsReconstructions(Query query) {
        Reconstruction rec = new ReconstructionImpl();
        this.generatedMetabolites = new HashMap<UUID, Metabolite>();
        for (MetabolicReaction metReaction : getReactions(query)) {
            processRedunantMetabolites(metReaction);
            rec.addReaction(metReaction);
        }
        List<Reconstruction> recs = new ArrayList<Reconstruction>();

        if (!rec.reactome().isEmpty())
            recs.add(rec);

        return recs;
    }

    /**
     * Takes the metabolites participating in a reaction, checks whether they have been already generated for the
     * current reconstruction. If the metabolites (same UUID) have been already generated, then newer one is replaced by
     * the existing one.
     * 
     * @param metReaction 
     */
    private void processRedunantMetabolites(MetabolicReaction metReaction) {
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

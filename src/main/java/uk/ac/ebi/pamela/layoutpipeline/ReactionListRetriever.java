/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.pamela.layoutpipeline;

import java.util.List;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

/**
 *
 * @author pmoreno
 */
public interface ReactionListRetriever {
    
    public List<Reconstruction> getReactionsAsReconstructions(Query query);
}

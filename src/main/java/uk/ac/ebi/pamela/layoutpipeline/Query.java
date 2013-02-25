/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.pamela.layoutpipeline;

import uk.ac.ebi.mdk.domain.identifier.Taxonomy;
import uk.ac.ebi.mdk.domain.identifier.type.ChemicalIdentifier;

/**
 * Represents the query object which contains the set of elements that will be used by the pipeline to produce a list
 * of reactions.
 * 
 * @author pmoreno
 */
public interface Query {
    
    public Taxonomy getOrganismIdentifier();
    public ChemicalIdentifier getChemicalIdentifier();
    
}

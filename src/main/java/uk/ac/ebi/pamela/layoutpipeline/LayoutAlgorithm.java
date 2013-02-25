/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.pamela.layoutpipeline;

import org.sbml.jsbml.SBMLDocument;

/**
 *
 * @author pmoreno
 */
public interface LayoutAlgorithm {
    
    public SBMLDocument getLayoutedSBML(SBMLDocument nonLayoutedSBMLDoc);
    
}

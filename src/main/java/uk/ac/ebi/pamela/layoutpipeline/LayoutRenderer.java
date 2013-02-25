/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.pamela.layoutpipeline;

import java.io.OutputStream;
import org.sbml.jsbml.SBMLDocument;

/**
 *
 * @author pmoreno
 */
public interface LayoutRenderer {
    
    public void produceRender(SBMLDocument sbmlWithLayout, String outputFilePrefix);
    
}

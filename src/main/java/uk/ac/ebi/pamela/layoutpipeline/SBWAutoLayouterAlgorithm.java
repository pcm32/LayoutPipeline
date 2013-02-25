/**
 * SBWAutoLayouterAlgorithm.java
 *
 * 2013.02.25
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.ebi.pamela.layoutpipeline;


import org.apache.log4j.Logger;
import org.sbml.jsbml.SBMLDocument;

/**
 * @name    SBWAutoLayouterAlgorithm
 * @date    2013.02.25
 * @version $Rev$ : Last Changed $Date$
 * @author  pmoreno
 * @author  $Author$ (this version)
 * @brief   This implementation uses the SBW mono based executable through a nasty linux exec to process the given 
 *          non layouted SBML doc.
 *
 */
public class SBWAutoLayouterAlgorithm implements LayoutAlgorithm {

    private static final Logger LOGGER = Logger.getLogger( SBWAutoLayouterAlgorithm.class );
    
    
    /**
     * Executed example
     * mono SaveLayout.exe -f /Applications/SBW/SBML\ Models/Jana_WolfGlycolysis.xml -l 20
     * 
     * produced:
     * sbw_auto_layouter_opt20.xml
     * 
     * Set up needed within user account.
     * 
     * SaveLayout.exe is part of AutoLayoutWithoutLibSBML.zip, this needs to be unziped and then copy libsbml* from 
     * the linux SBW/lib installation into the directory (SBW needs to be downloaded of course).
     * 
     * mono framework needs to be installed as well: www.mono-project.com/
     * 
     * This object should be configured with the desired parameters for SaveLayout.exe:
     * 
     * usage: SaveLayout -f <file to convert>
                 [-o <directory to save to>         ]
                 [-a <degree for auto aliasing>     ]
                 [-w <width     of the image to create>]
                 [-h <height of the image to create>]
                 [-g <double value of gravity factor>]
                 [-l <double value of edge length>]
                 [-nosbml]
                 [-grid]
                 [-magnetism]
                 [-boundary]
                 [-emptySet]
                 [-emptySets]
                 [-r || --removeExisting]
                 [-sourceSink]
                 [--bmp]
                 [--png]
                 [--fullJPG]
                 [--ps]
                 [--svg]
     */
    
    
    
    
    /**
     * 
     * @param nonLayoutedSBMLDoc
     * @return 
     */
    public SBMLDocument getLayoutedSBML(SBMLDocument nonLayoutedSBMLDoc) {
        /**
         * This class needs to write the nonLayoutedSBMLDoc SBML document to a temporary directory, set up the command 
         * line call for the SBW AutoLayouter, execute the call, and parse the resulting SBML document with layout, 
         * producing an SBMLDocument object that has the layout on it.
         */
        return null;
    }


}

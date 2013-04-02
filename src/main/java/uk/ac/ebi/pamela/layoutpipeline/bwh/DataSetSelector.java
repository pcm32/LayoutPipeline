/*
 * Copyright (C) 2013 Pablo Moreno <pablacious at users.sf.net>
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

package uk.ac.ebi.pamela.layoutpipeline.bwh;

import com.sri.biospice.warehouse.schema.DataSet;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;

/**
 * @name    DataSetSelector
 * @date    2013.03.21
 * @version $Rev$ : Last Changed $Date$
 * @author  Pablo Moreno <pablacious at users.sf.net>
 * @author  $Author$ (this version)
 * @brief   defines functionality for a data set selector.
 *
 */
public interface DataSetSelector {

    /**
     * Given a Taxonomy identifier, this selector retrieves a DataSet according to implementation criteria. 
     * 
     * 
     * @param organismIdentifier
     * @return a DataSet for the defined organism.
     */
    public DataSet getDataSetForOrganism(Taxonomy organismIdentifier);


}

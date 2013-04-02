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
import java.util.List;
import java.util.Map;
import java.util.Set;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;

/**
 * @name    AbstractDataSetSelector
 * @date    2013.04.02
 * @version $Rev$ : Last Changed $Date$
 * @author  Pablo Moreno <pablacious at users.sf.net>
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public abstract class AbstractDataSetSelector {
    Set<Taxonomy> calledForOrg;
    Map<Taxonomy, DataSet> org2DataSet;

    public DataSet getDataSetForOrganism(Taxonomy organismIdentifier) {
        if (!calledForOrg.contains(organismIdentifier)) {
            throw new RuntimeException("Method hasDataSetForOrganism(Taxonomy identifier) needs to be called first");
        }
        return org2DataSet.get(organismIdentifier);
    }

    public boolean hasDataSetForOrganism(Taxonomy organismIdentifier) {
        recordVisit(organismIdentifier);
        List<DataSet> res = obtainDataSetAsList();
        return recordAnswer(organismIdentifier, res);
    }

    abstract List<DataSet> obtainDataSetAsList();

    boolean recordAnswer(Taxonomy organismIdentifier, List<DataSet> res) {
        if (res.size() > 0) {
            org2DataSet.put(organismIdentifier, res.get(0));
            return true;
        }
        return false;
    }

    void recordVisit(Taxonomy organismIdentifier) {
        calledForOrg.add(organismIdentifier);
    }


}

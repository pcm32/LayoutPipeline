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

package uk.ac.ebi.pamela.layoutpipeline.reaction;

import com.sri.biospice.warehouse.schema.DataSet;
import com.sri.biospice.warehouse.schema.object.Chemical;
import com.sri.biospice.warehouse.schema.object.Reaction;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import uk.ac.ebi.warehouse.connectivity.HighConnectivityRetriever;
import uk.ac.ebi.warehouse.util.ReactionUtil;

/**
 * @name    BWHConnectivityBasedCurrencyDecider
 * @date    2013.03.22
 * @version $Rev$ : Last Changed $Date$
 * @author  Pablo Moreno <pablacious at users.sf.net>
 * @author  $Author$ (this version)
 * @brief   ...class description...
 *
 */
public class BWHConnectivityBasedCurrencyDecider implements CurrencyCompoundDecider<Chemical, Reaction>{
    
    private static final Logger LOGGER = Logger.getLogger(BWHConnectivityBasedCurrencyDecider.class);
    
    private Integer cutOff;
    private HighConnectivityRetriever ret;
    private Set<Chemical> currencyDs;
    
    public BWHConnectivityBasedCurrencyDecider(DataSet dsForCutOff) {
        // use biowh data set to obtain first 5% of connectivities to use as cutoff.
        ret = new HighConnectivityRetriever(dsForCutOff, 20);
    }

    public Collection<Chemical> getCurrencyMetabolites(Reaction rxn) {
        Set<Chemical> currency = new HashSet<Chemical>();
        try {
            currency.addAll(ReactionUtil.getChemicalReactProductCoeffForRxn(rxn.getWID(), rxn.getDataSetWID()).keySet());
        } catch(SQLException e) {
            LOGGER.error("Problems retrieving participants for reaction WID "+rxn.getWID()+" towards retrieving currencies", e);
        }
        currency.retainAll(currencyDs);
        return currency;
    }


}

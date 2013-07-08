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
package uk.ac.ebi.pamela.layoutpipeline.bwh;

import com.sri.biospice.warehouse.database.PooledWarehouse;
import com.sri.biospice.warehouse.database.PooledWarehouseManager;
import com.sri.biospice.warehouse.database.Warehouse;
import com.sri.biospice.warehouse.schema.DataSet;
import com.sri.biospice.warehouse.schema.TableFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.identifier.Taxonomy;

/**
 * @name NewestUnifiedDataSetSelector
 * @date 2013.04.02
 * @version $Rev$ : Last Changed $Date$
 * @author Pablo Moreno <pablacious at users.sf.net>
 * @author $Author$ (this version)
 * @brief ...class description...
 *
 */
public class NewestUnifiedDataSetSelector extends AbstractDataSetSelector implements DataSetSelector {
    
    private static final Logger LOGGER = Logger.getLogger(NewestUnifiedDataSetSelector.class);
    
    List<DataSet> obtainDataSetAsList(Taxonomy taxonomyIdent) {
        String query = "SELECT ds.* FROM DataSet ds \n"
                + "JOIN BioSource bs ON ds.WID = bs.DataSetWID \n"
                + "JOIN Taxon t ON t.WID = bs.TaxonWID \n"
                + "JOIN DBID dbid ON dbid.OtherWID = t.WID\n"
                + "WHERE dbid.XID = ?\n"
                + "AND Application = 'UnificationDataSetBuilder'\n"
                + "GROUP BY ds.WID, dbid.XID ORDER BY Version, WID DESC LIMIT 1;";
        Warehouse bwh = PooledWarehouseManager.getWarehouse();
        List<DataSet> res = new ArrayList<DataSet>();
        try {
            PreparedStatement ps = bwh.createPreparedStatement(query);
            ps.setString(1, taxonomyIdent.getAccession());
            ResultSet rs = ps.executeQuery();
            if(rs.first()) {
                DataSet ds = new DataSet(rs.getLong("WID"));
                res.add(ds);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not load DataSets", e);
        } finally {
            try {
                if(bwh instanceof PooledWarehouse) {
                    bwh.close();
                }
            } catch(SQLException e) {
                throw new RuntimeException("Could not close connection");
            }
        }
        return res;
    }
}

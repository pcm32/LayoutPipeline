package uk.ac.ebi.pamela.layoutpipeline.pathway;

import com.sri.biospice.warehouse.database.PooledWarehouseManager;
import com.sri.biospice.warehouse.database.Warehouse;
import com.sri.biospice.warehouse.schema.DataSet;
import com.sri.biospice.warehouse.schema.object.Pathway;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 21/6/13
 * Time: 14:29
 *
 * {@inheritDoc}
 */
public class PAMELAPathwayNoFlightChecker implements PathwayNoFlightChecker<Pathway> {

    private static final Logger LOGGER = Logger.getLogger(PAMELAPathwayNoFlightChecker.class);

    private Map<Pathway,Boolean> checkedPaths;
    private Integer maxReactions;
    private PreparedStatement ps;

    /**
     * Default constructor which sets the number of maximum reaction in a pathway for it to be layouted
     * to 100.
     */
    public PAMELAPathwayNoFlightChecker() {
        this.checkedPaths = new HashMap<Pathway, Boolean>();
        this.maxReactions = 100;
    }


    /**
     * {@inheritDoc}
     *
     * for this implementation, the size of the pathway is based on the reactions belonging to the pathway
     * within the same data set as the pathway.
     */
    @Override
    public boolean checkNoFlight(Pathway pathway) {
        if(checkedPaths.containsKey(pathway))
            return checkedPaths.get(pathway);

        try {
            Warehouse bwh = PooledWarehouseManager.getWarehouse();
            if(ps==null) {
                ps = bwh.createPreparedStatement("SELECT COUNT(DISTINCT ReactionWID) FROM PathwayReaction WHERE PathwayWID = ?");
            }
            ps.setLong(1,pathway.getWID());
            ResultSet rs = ps.executeQuery();
            boolean res=false;
            if(rs.next()) {
                res = rs.getInt(1)>maxReactions;
            }
            checkedPaths.put(pathway,res);
            bwh.close();
            return checkedPaths.get(pathway);
        } catch (SQLException e) {
            LOGGER.error("Could not retrieve reaction count for pathway.");
        }
        return false;
    }

    /**
     * Exclude:
     *
     * 'Microbial metabolism in diverse environments'
     * 'Biosynthesis of secondary metabolites'
     * 'Metabolic pathways'
     */
}

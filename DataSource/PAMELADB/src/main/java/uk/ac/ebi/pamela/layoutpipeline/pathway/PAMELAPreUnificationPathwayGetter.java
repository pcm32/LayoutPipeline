package uk.ac.ebi.pamela.layoutpipeline.pathway;

import com.sri.biospice.warehouse.database.PooledWarehouseManager;
import com.sri.biospice.warehouse.database.Warehouse;
import com.sri.biospice.warehouse.schema.DataSet;
import com.sri.biospice.warehouse.schema.TableFactory;
import com.sri.biospice.warehouse.schema.object.Pathway;
import org.apache.log4j.Logger;
import uk.ac.ebi.pamela.layoutpipeline.Query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 20/6/13
 * Time: 16:29
 *
 * Obtains Biowarehouse pathways based on a query, based on pathways belonging to a data set which was at some point
 * merged into the current data sets with others.
 *
 */
public class PAMELAPreUnificationPathwayGetter implements PathwayGetter<Pathway> {

    private static final Logger LOGGER = Logger.getLogger(PAMELAPreUnificationPathwayGetter.class);

    private DataSet ds;
    private PAMELAPathwayNoFlightChecker checker;

    /**
     * Initializes the pathway getter with a unified data set.
     *
     * @param ds
     */
    public PAMELAPreUnificationPathwayGetter(DataSet ds) {
        this.ds = ds;
        this.checker = new PAMELAPathwayNoFlightChecker();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Pathway> getPathways(Query query) {

        Collection<Pathway> pathways = new LinkedList<Pathway>();
        String querySetIdent = "SET @identifier = ?";
        String querySetDS = "SET @dwid = ?";

        try {
            Warehouse bwh = PooledWarehouseManager.getWarehouse();

            PreparedStatement psSetIdent = bwh.createPreparedStatement(querySetIdent);
            psSetIdent.setString(1,query.getChemicalIdentifier().getAccession());
            psSetIdent.execute();
            psSetIdent.close();

            PreparedStatement psSetDS = bwh.createPreparedStatement(querySetDS);
            psSetDS.setLong(1,this.ds.getWID());
            psSetDS.execute();
            psSetDS.close();

            String mainQuery = "SELECT DISTINCT po.* " +
                    "FROM Reaction r " +
                    "JOIN CrossReference cr ON r.WID = cr.OtherWID " +
                    "JOIN Reaction ro ON ro.WID = cr.CrossWID " +
                    "JOIN PathwayReaction pro ON pro.ReactionWID = ro.WID " +
                    "JOIN Pathway po ON po.WID = pro.PathwayWID " +
                    "JOIN (" +
                    "(SELECT DISTINCT r.WID FROM Reaction r " +
                    "    JOIN Product p ON p.ReactionWID = r.WID " +
                    "    JOIN Chemical c ON c.WID = p.OtherWID " +
                    "JOIN CrossReference cr ON cr.OtherWID = c.WID " +
                    "WHERE cr.XID LIKE @identifier AND c.DataSetWID = @dwid) " +
                    "    UNION " +
                    "    (SELECT DISTINCT r.WID FROM Reaction r " +
                    "JOIN Reactant re ON re.ReactionWID = r.WID " +
                    "JOIN Chemical c ON c.WID = re.OtherWID " +
                    "    JOIN CrossReference cr ON cr.OtherWID = c.WID " +
                    "    WHERE cr.XID LIKE @identifier AND c.DataSetWID = @dwid) " +
                    ") as Participants ON r.WID = Participants.WID " +
                    "JOIN DBID db ON db.OtherWID = po.WID " +
                    "WHERE r.DataSetWID = @dwid AND db.XID NOT LIKE ?;";

            PreparedStatement psMain = bwh.createPreparedStatement(mainQuery);
            psMain.setString(1,"K%");

            pathways.addAll(TableFactory.loadTables(psMain.executeQuery(), TableFactory.PATHWAY));

            Set<Pathway> toRM = new HashSet<Pathway>();
            for (Pathway p : pathways) {
                if(checker.checkNoFlight(p)) {
                    toRM.add(p);
                    LOGGER.info("Skipping pathway "+p.getName()+" due to no flight check.");
                }
            }
            pathways.removeAll(toRM);

            bwh.close();
        } catch (SQLException e) {
            LOGGER.error("Could not query the PAMELA database for pathways for query "+query);
        }

        return pathways;

    }

}

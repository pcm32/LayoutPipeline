package uk.ac.ebi.pamela.layoutpipeline.rhea;

import org.apache.log4j.Logger;
import uk.ac.ebi.pamela.layoutpipeline.utils.PropertiesUtil;
import uk.ac.ebi.pamela.layoutpipeline.utils.RheaDBConnectionSetter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: pmoreno
 * Date: 26/6/13
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class RheaConnectionManager {

    private static final Logger LOGGER = Logger.getLogger(RheaConnectionManager.class);

    private static Connection rheaConnection;

    public static Connection getConnection() {
        if(rheaConnection==null) {
            // Get the connection properties
            String rheaUrl = PropertiesUtil.getPreference(RheaDBConnectionSetter.RheaDBField.rheUrl);
            String rheaUser = PropertiesUtil.getPreference(RheaDBConnectionSetter.RheaDBField.rheaUsername);
            String rheaPassword = PropertiesUtil.getPreference(RheaDBConnectionSetter.RheaDBField.rheaPassword);
            String rheaSchema = PropertiesUtil.getPreference(RheaDBConnectionSetter.RheaDBField.rheaSchema);

            // We should externalise this.
            try {
                rheaConnection = DriverManager.getConnection(rheaUrl, rheaUser, rheaPassword);

                if (rheaSchema !=null){

                    Statement stmt = rheaConnection.createStatement();
                    stmt.execute("alter session set current_schema = " + rheaSchema);
                    stmt.close();

                }

                LOGGER.info("Connection successful to rhea db.");
            } catch (SQLException e) {
                LOGGER.error("Can't connect to rhea database: " + e.getMessage());
            }
        }
        return rheaConnection;
    }

    public static void closeConnection() {
        if(rheaConnection!=null) {
            try {
                rheaConnection.close();
            } catch (SQLException e) {
                LOGGER.error("Could not close Rhea connection",e);
            }
        }
    }
}

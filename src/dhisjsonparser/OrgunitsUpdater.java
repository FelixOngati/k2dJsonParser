package dhisjsonparser;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import static dhisjsonparser.Moh711DataElementsUpdater.pwd;
import static dhisjsonparser.Moh711DataElementsUpdater.user;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author trusty
 */
public class OrgunitsUpdater {
    static Connection conn;
    static PreparedStatement pstmt;
    static final String url = "jdbc:mysql://localhost/k2ddummy";

    
        //fetches moh711 dataelement IDs and returns a string array
    public static String[] fetchOrgunitIds() {
        String[] array = null;

        //establish connection to DB
        try {
            //register jdbc driver
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url, user, pwd);
            conn.setAutoCommit(false);

            String sql = "SELECT orgunitid FROM orgunits";
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            //determine number of rows in in the resultset
            rs.last(); //move cursor to the last row
            int rowcount = rs.getRow();
            array = new String[rowcount - 1];
            rs.first();//return cursor to the first row
            int count = 0;
            while(rs.next()){
                //retrieve by column name
                String id = rs.getString("orgunitid");
                array[count] = id;
                count++;
            }
            rs.close();
            conn.close();
            pstmt.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Moh711DataElementsUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
       return array; 
    }

}

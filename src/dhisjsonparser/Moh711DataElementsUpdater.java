package dhisjsonparser;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

/**
 *
 * @author trusty
 */
public class Moh711DataElementsUpdater {

    static Connection conn;
    static PreparedStatement pstmt;
    static final String url = "jdbc:mysql://localhost/k2ddummy";
    static final String user = "root";
    static final String pwd = "password";

    //parse the json String passed from DhisJsonParser class && establishes connection to DB
    public static void parser(String curlResults) {
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = (Connection) DriverManager.getConnection(url, user, pwd);
                conn.setAutoCommit(false);
                //truncate the table first before updating
                String sql1 = "TRUNCATE TABLE moh711dataelements";
                PreparedStatement preparedStatement = (PreparedStatement) conn.prepareStatement(sql1);
                preparedStatement.execute();
                conn.commit();
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(Moh711DataElementsUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }

            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(curlResults);
            JSONObject metaData = (JSONObject) jsonObject.get("metaData");
            JSONObject elements = (JSONObject) metaData.get("names");
            //get keys from the Json object
            Iterator iteratorkeys = elements.keySet().iterator();
            int count = 0;
            while (iteratorkeys.hasNext()) {
                String key = (String) iteratorkeys.next();
                //sanitize the data by removing unwanted items
                if (key.length() > 10 && !"HrFWseAGXzN".equals(key)) {
                    System.out.println(key + " " + elements.get(key));
                    updateDB(key, (String) elements.get(key));
                    count++;
                }

            }
            System.out.println(count + "Successfull");
            //establishes connection to DB; for some reason, i had to put it inside this method

        } catch (ParseException | SQLException ex) {
            Logger.getLogger(Moh711DataElementsUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //updates the database table that hold moh711 data elements

    public static void updateDB(String key, String element) throws SQLException {

        String sql2 = "INSERT INTO moh711dataelements(dataelementid, dataelementname) VALUES(?,?)";
        pstmt = (PreparedStatement) conn.prepareStatement(sql2);
        pstmt.setString(1, key);
        pstmt.setString(2, element);
        pstmt.execute();

        conn.commit();
    }

    //fetches moh711 dataelement IDs and returns a string array
    public static String[] fetchDatalementIds() {
        String[] array = null;

        //establish connection to DB
        try {
            //register jdbc driver
            Class.forName("com.mysql.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection(url, user, pwd);
            conn.setAutoCommit(false);

            String sql = "SELECT dataelementid FROM moh711dataelements";
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
                String id = rs.getString("dataelementid");
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

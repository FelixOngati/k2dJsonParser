package dhisjsonparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;


/**
 * @author trusty
 */
public class DhisJsonParser {

    /**
     * @param args the command line arguments
     * @throws java.net.MalformedURLException
     */
    
    //Create a blank workbook
        static XSSFWorkbook workbook = new XSSFWorkbook();
        //Create a blank sheet
        static XSSFSheet sheet = workbook.createSheet("moh711values");
        
    /*For test purpose only. Will later be fetched from the DB*/
    static String[] ouid = OrgunitsUpdater.fetchOrgunitIds();
    
    /*For test purpose only. Will later be fetched from the DB*/
    static String[] moh711dataElements = Moh711DataElementsUpdater.fetchDatalementIds();
    
    public static void main(String[] args) throws MalformedURLException, NoSuchAlgorithmException, KeyManagementException, IOException, ParseException {
        /*To avoid javax.net.ssl.SSLHandshakeException: java.security.cert.
        CertificateException:.., Create a trust manager that does not validate certificate 
        chains*/
        TrustManager[] trustAllCerts = new TrustManager[] {
          new X509TrustManager(){
              public java.security.cert.X509Certificate[] getAcceptedIssuers(){
                  return null;
              }
              public void checkClientTrusted(X509Certificate[] certs, String authType){
                  
              }
              public void checkServerTrusted(X509Certificate[] certs, String authType){
                  
              }
          }
        };
        
        //Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        
        //Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        
        //Install all-trusting host name verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        
        String[] periods = {"201301","201302","201303","201304","201305","201306","201307",
            "201308","201309","201310","201311","201312"};        
        
        
        String urlr = "https://test.hiskenya.org/api/analytics.json?dimension=dx:KzIawX7xMAv;"
                + "YWGxnnD9KXb;jaPrPmor6WV;svJzNw1TdaI;rcfDxB8Hpuu;AY5ZPkANII8&dimension=pe:"
                + "201301&filter=ou:";
        
        System.out.printf("%s\t%s\t%s\t%s\t\n", "OrgUnitId", "DataElementId","Period","Value");
        
//        //iterate over org units fetching data
//        for (int i = 0; i < ouid.length; i++){
//            String url = urlr + ouid[i];
//            //call dataSetsProcessBuilder method to execute curl equivalent
//            String genreJson = dataSetsProcessBuilder(url);
//           // System.out.println(genreJson);
//            //parse json data and display results
//            dataElementsParser(genreJson, ouid[i]);
//        }
        
        /* updates moh711 dataelements*/
        //Moh711DataElementsUpdater.parser(dataSetsProcessBuilder(urlBuilder("201304","HrFWseAGXzN")));
        System.out.println(Arrays.toString(ouid));
        for(String p :periods){
            for(String org:ouid ){
                //dataElementsParser(dataSetsProcessBuilder(urlBuilder(p, org)), org);               
            }
        }
    }
    
    //processes the json string
    public static void dataElementsParser(String genreJson, String ouid) throws ParseException{
            JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(genreJson);
            JSONArray dataElementValues = (JSONArray) genreJsonObject.get("rows");
            Iterator  values = dataElementValues.iterator();
            String[] tempArray = new String[4];
            int row = 0;
            while(values.hasNext()){
                JSONArray array = (JSONArray) values.next();
                String dataElementId = (String) array.get(0);
                String period  = (String) array.get(1);
                String value = (String) array.get(2);
                tempArray[0] = ouid;
                tempArray[1] = dataElementId;
                tempArray[2] = period;
                tempArray[3] = value;
                System.out.printf("%s\t%s\t%s\t%s\t\n", ouid,dataElementId, period, value);
                //writeToExcel(tempArray,row);
                row++;
            }
    }
    
    public static String dataSetsProcessBuilder(String urlr) throws IOException{
        String username = "";
            String pass = "";
            ProcessBuilder p = new ProcessBuilder("curl","--insecure","--proxy", "https://p15%2F1317%2F2011%40students:inx%40uon@proxy.uonbi.ac.ke:80","-X",
            "GET", "-u", username + ":" + pass, urlr);
            final Process shell = p.start();
            InputStream in = shell.getInputStream();
            //System.out.println(IOUtils.toString(in));
            
            return IOUtils.toString(in);
    }
    
    public static String urlBuilder( String period, String orgunit){
        String url = "https://test.hiskenya.org/api/analytics.json?dimension=dx:";
        for (String moh711dataElement : moh711dataElements) {
            url = url + moh711dataElement;
        }
        url = url + "&dimension=pe:" + period + "&filter=ou:" + orgunit;
        
        return url;
    }
    
        private static void writeToExcel(String[] tempArray, int rowx) {        
        Row row = sheet.createRow(rowx);
        int cellnum = 0;
        for(int i = 0; i < tempArray.length; i++){
              Cell cell = row.createCell(cellnum++);
              cell.setCellValue(tempArray[i]);
        }
        
        //write the workbook in a filesystem
        FileOutputStream out;
        try {
            out = new FileOutputStream(new File("/home/trusty/dhis/moh711data.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Successful");
        } catch (FileNotFoundException  ex) {
            Logger.getLogger(DhisJsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DhisJsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }        
        }

}

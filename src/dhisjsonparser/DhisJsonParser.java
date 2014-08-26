package dhisjsonparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.io.IOUtils;
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
            
        String ouid = "mu9d9jNXA6Y";//make dynamic
        String urlr = "https://test.hiskenya.org/api/analytics.json?"
                + "dimension=dx:KzIawX7xMAv;YWGxnnD9KXb;jaPrPmor6WV;svJzNw1TdaI;"
                + "eEpRB9ra4jj;rcfDxB8Hpuu;ypJG5mczJNT;mmqXlRfsXlf;FCmhDa4kaMp;"
                + "TkwjrwghYVF;AY5ZPkANII8;Kx64gGqaFVq;QDD62ZsbVnT;TLiUok6zK9w;"
                + "bhFvc6EeCTC;pSIDJ79UquD;qa1IrxISrSJ;sMqM8DwiAaj;OiuBpHJw4kf;"
                + "UsyFvMBxvn0;IV9vOoO4Mo4;M9wNpvfC8wb;FwsTJpdxT4C;Q5H6IEbD5ak;"
                + "eNcsljfoUKW;EHGBrVPiG3N;N9VDFWZAumw;TJwizRWebWZ&"
                + "dimension=pe:201404;filter=ou:" + ouid;
        try {
            URL obj = new URL(urlr);
            HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
            System.out.println(conn.toString());
            String redirect = conn.getHeaderField("Location");
             
//            while (redirect != null) {
//                URL obj1 = new URL(redirect);
//                HttpURLConnection conn1 = (HttpURLConnection) obj1.openConnection();               
//                System.out.println(conn1.toString());
//                
//                conn1.setRequestMethod("GET");            
//            String userpass = "fegati" + ":" + "Hiskenya7";
//            String basicAuth = "Basic" + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
//            conn1.setRequestProperty("Authorization", basicAuth);
//            redirect = conn1.getHeaderField("Location");
//           }
            
//            conn.setRequestMethod("GET");            
//            String userpass = "fegati" + ":" + "Hiskenya7";
//            String basicAuth = "Basic" + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
//            conn.setRequestProperty("Authorization", basicAuth); 
             
             String username = "fegati";
             String pass = "Hiskenya7";
            ProcessBuilder p = new ProcessBuilder("curl","--insecure","--noproxy", "*","-X",
            "GET", "-u", username + ":" + pass, urlr);
            final Process shell = p.start();
            InputStream in = shell.getInputStream();
            // String text = new Scanner( in ).useDelimiter("\\A").next();
            //System.out.println(text);
            String genreJson = IOUtils.toString(in);
            System.out.println(genreJson);
            
                JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(genreJson);
            JSONArray dataElementValues = (JSONArray) genreJsonObject.get("rows");
            Iterator  values = dataElementValues.iterator();
            System.out.printf("%s\t%s\t%s\t\n", "DataElementId","Period","Value");
            while(values.hasNext()){
                JSONArray array = (JSONArray) values.next();
                String dataElementId = (String) array.get(0);
                String period  = (String) array.get(1);
                String value = (String) array.get(2);
                System.out.printf("%s\t%s\t%s\t\n", dataElementId, period, value);
            }
           // System.out.println(genreJsonObject.get("metaData"));
            //System.out.println(genreJsonObject.get("rows"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

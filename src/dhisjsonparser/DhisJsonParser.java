package dhisjsonparser;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;

/**
 * @author trusty
 */
public class DhisJsonParser {

    /**
     * @param args the command line arguments
     * @throws java.net.MalformedURLException
     */
    public static void main(String[] args) throws MalformedURLException, NoSuchAlgorithmException, KeyManagementException, IOException {
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
            
        
        String urlr = "https://test.hiskenya.org/api/analytics.json?dimension=dx:KzIawX7xMAv;YWGxnnD9KXb;jaPrPmor6WV;svJzNw1TdaI;eEpRB9ra4jj;rcfDxB8Hpuu;ypJG5mczJNT;mmqXlRfsXlf;FCmhDa4kaMp;TkwjrwghYVF;AY5ZPkANII8;Kx64gGqaFVq;QDD62ZsbVnT;TLiUok6zK9w;bhFvc6EeCTC;pSIDJ79UquD;qa1IrxISrSJ;sMqM8DwiAaj;OiuBpHJw4kf;UsyFvMBxvn0;IV9vOoO4Mo4;M9wNpvfC8wb;FwsTJpdxT4C;Q5H6IEbD5ak;eNcsljfoUKW;EHGBrVPiG3N;N9VDFWZAumw;TJwizRWebWZ&dimension=pe:201404;filter=ou:mu9d9jNXA6Y";
        //String urlr = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Nairobi&mode=json&units=metric&cnt=7";
        try {
            URLConnection connection = (new URL(urlr)).openConnection();
            System.out.println(connection.toString());
            String userpass = "fegati" + ":" + "Hiskenya7";
            String basicAuth = "Basic" + new String(new Base64().encode(userpass.getBytes()));
            connection.setRequestProperty("Authorization", basicAuth);            
            
            String redirect = connection.getHeaderField("Location");
            if (redirect != null) {
                connection = new URL(redirect).openConnection();
                connection.setRequestProperty("Authorization", basicAuth); 
                System.out.println(connection.toString());
           }
            InputStream in = connection.getInputStream();
            // String text = new Scanner( in ).useDelimiter("\\A").next();
            //System.out.println(text);
            String genreJson = IOUtils.toString(in);
            System.out.print(genreJson);
            
            //JSONObject genreJsonObject = (JSONObject) JSONValue.parseWithException(genreJson.toString());
            //get the name
            //System.out.println(genreJsonObject.get("headers"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

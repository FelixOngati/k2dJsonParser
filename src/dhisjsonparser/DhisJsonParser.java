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
    static String[] ouid = {"HrFWseAGXzN","d23ZMyDAOEE","zpPVs8bYduv",
        "LkV5kJMdakx","NFVVCDVuAkk","UOKEv02h8eL","TT3kyK0Iam5","GOxptySBE5j","hZqDvGaLiEr",
        "eXQGo7rOHzG","HAXZM3YNUmN","XVRGfr0CuHg","VT72TxMgtu7","fNVYEyxjawL","t75KnRpIucJ",
        "irwLEN2bSMG","sg4BJjECrJ2","LGC7iKM2acq","eCRyPPTMFs9","ZosyfWIzXWl","x9hGRq2zvep","uBfCuhllvg3",
        "Q7WmVW0gNmv","MXJHIvxYgrD","zRRAVOH7s28","XIo0DuIga6G","syMX95GoW0g","ajV4U9XRbBN","bvUg2vPkLWw"};
    
    /*For test purpose only. Will later be fetched from the DB*/
    static String[] moh711dataElements = {"KzIawX7xMAv;","ypJG5mczJNT;","mmqXlRfsXlf;","TkwjrwghYVF;",
            "AY5ZPkANII8;","Kx64gGqaFVq;","QDD62ZsbVnT;","SI8igpKYhVG;","TLiUok6zK9w;","bhFvc6EeCTC;",
            "pSIDJ79UquD;","qa1IrxISrSJ;","sMqM8DwiAaj;","OiuBpHJw4kf;","UsyFvMBxvn0;","IV9vOoO4Mo4;",
            "M9wNpvfC8wb;","FwsTJpdxT4C;","Q5H6IEbD5ak;","eNcsljfoUKW;","EHGBrVPiG3N;","N9VDFWZAumw;",
            "TJwizRWebWZ;","FCmhDa4kaMp;","rcfDxB8Hpuu;","svJzNw1TdaI;","eEpRB9ra4jj;","YWGxnnD9KXb;",
            "jaPrPmor6WV;","SDIS7W1zhPW;","LNOJnResD5m;","f0yEn4dedjn;","xAAEwRrU8EH;","PggNwT09D3U;",
            "j6EvRpVJONr;","vvoE9dLHFgg;","yQFyyQBhXQf;","cV4qoKSYiBs;","R6oylU6CF31;","PLN5zlML149;",
            "qrTk1w6Y5Bk;","rS1DZr9EZGG;","jFpKjQupwHf;","oiZ7R6su8ZX;","ia50XRXor21;","RK2r7Sgr2rX;",
            "PZb5Bk5JKSw;","jI6RYlclUgr;","KGRCdw30ILH;","KuMX8VqCejs;","zVTIzkATPDS;","gAKCshSgSH3;",
            "TxhvipxGJoA;","tF5z3LnQvWu;","cTY2tTKwUqd;","bmFMhQa3G2v;","I6U6idiMIOC;","hxXwYIgT8rI;",
            "BRzqHfgsmn9;","uvNayIO0zrN;","oq4vCrIRVxb;","P0gfmezl6IV;","lNNrYXr0Dnz;","f1yrtCEf1HP;",
            "DuBH6qPPdaO;","k8qXy4LyyJj;","VihTrdMMli8;","uvBuzBj0AbL;","rAZBTMa7Jy3;","GAr6xu6f1n7;",
            "oC215Fl92uW;","NeH2XNZ4B78;","nspwXhqQaAz;","xsh8ECnUJIk;","kMe8cor2UxQ;","syjjPqXbjTm;",
            "Mn3FQ9r5Wtp;","xO4Kh84Bwlk;","M4RzpOew1Im;","uxYaacfgY22;","ND0kGuYLszx;","JglserSm6IQ;",
            "k8Y2rcga2FJ;","CvoE5s0xLLf;","uHM6lzLXDBd;","s0Zi5NpklJN;","YZdSnMLgiAV;","Yq2bq0zZokF;",
            "UqKC1DJnymn;","d4KC4RIwpKH;","zIiDTYhg8K5;","OPII6vOKimx;","h6Bcw3YvuRR;","BQmcVE8fex4;",
            "MKICatwGRPz;","KtZWJoV27fM;","eIvn8VgI5Zx;","eBHhfupqMHz;","hNsd8cQIYun;","JzM8Q9sIc9i;",
            "f9vesk5d4IY;","KmLPBDUi48L;","cKxrOyX8yfM;","dnNfHX89udH;","Hh4daYAcb49;","P15TN95Ie3F;",
            "otgQMOXuyIn;","BgFeMs6gQ7E;","xjlkLTQhA2b;","Fz0LzxMT1vV;","Sb8ICydVNVd;","sdA7bJkZc9E;",
            "Nqt6rz4tqnm;","Gp38RIEAOCb;","GyIfu4qpXJM;","GlGolXdf6gr;","aTBJvDJQAF0;","eM7FcVVzvz1;",
            "j1Vu1ZUZK0s;","Jln7Ggzq0qJ;","T59McmnP4FJ;","YDZlFPpGO2W;","C8ZgpIWef1p;","NQOwqGwcBnN;",
            "GfubvZs2ES6;","Uhldghgqs2D;","rxhsRqFckMf;","jF2Uk71SaYC;","qMybCnYBkNY;","nOC15N8wS01;",
            "Uj7NxkhB71r;","j4bfChlEqWb;","IjrWNuj9rzt;","jFrSw8UkNQ4"};
    
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
        String username = "fegati";
            String pass = "Hiskenya7";
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

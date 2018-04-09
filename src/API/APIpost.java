/**
 * @date 2018-04-09 
 * @author Algorithme Solutions inc. ©
 * @email DonavanMartin@AlgorithmeSolutions.com
 */
package API;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIpost {
    private static final String url = "https://algorithmesolutions.com/routing";
    public Integer[][] result = {};
    public String key = "98C10D89-9FE2-4AE9-A9BE-F149ECE8AD97";
    
    public APIpost(){
    }
    
    public String post(String key, String diconsumeTimeMatrix, String consumeTimeMatrix, String firstInRoute, String truckCapacity, String truckAvailable) throws IOException, JSONException{
        String r = getString(key, diconsumeTimeMatrix, consumeTimeMatrix, firstInRoute, truckCapacity, truckAvailable);
        byte[] data = r.getBytes();
        String request = new String(data, "UTF-8");
        System.out.println("Read test succesful");
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        // Setting basic post request
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type","application/json");
        
        // Send post request
        con.setDoOutput(true);
        try (OutputStream os = con.getOutputStream()) {
            os.write(request.getBytes("UTF-8"));
        }
        
        int responseCode = con.getResponseCode();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String output;
        StringBuffer response = new StringBuffer();
        
        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();
        
        //printing result from response
        System.out.println("Response Code :"+responseCode+ "\t"+ response.toString().replaceAll("\"", ""));
        String sresponse = response.toString().replaceAll("\"", "");
        sresponse = "{ result:"+sresponse+"}";
        JSONObject obj2 = new JSONObject(sresponse);
        JSONArray resultJson = obj2.getJSONArray("result");
        
        result = new Integer[resultJson.length()][];
        for(int i=0; i<resultJson.length(); i++){
            JSONArray path = resultJson.getJSONArray(i);
            result[i] = new Integer[path.length()];
            for(int j=0; j<path.length(); j++){
                result[i][j] = path.getInt(j);
            }
        }
        
        String returnValue = response.toString().replaceAll("\"", "");
        return returnValue;
    }
    
    public static String getString(String key, String diconsumeTimeMatrix, String consumeTimeMatrix, String firstInRoute, String truckCapacity, String truckAvailable) throws IOException{
        String r="";
        r += "{";
        r += "\"key\":";                      r += key;
        r += ",\"distanceMatrix\":";          r += diconsumeTimeMatrix;
        r += ",\"weight\":";       r += consumeTimeMatrix;
        r += ",\"firstToVisit\":";            r += firstInRoute;
        r += ",\"capacity\":";           r += truckCapacity;
        r += ",\"available\":";          r += truckAvailable;
        r += "}";
        
        System.out.print("Request:"+r);
        return r;
    }

}

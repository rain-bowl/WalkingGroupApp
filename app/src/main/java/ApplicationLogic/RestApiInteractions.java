package ApplicationLogic;


import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RestApiInteractions {
    private final  String SERVER_URL = "placeholderURL";
    private HttpsURLConnection webConnection;
    private URL serverURL;


    public String createNewUser(String userName, String password, String emailAddr) throws IOException, JSONException{
        serverURL = new URL("https://cmpt276-1177-bf.cmpt.sfu.ca:8443/users/signup");
        webConnection = (HttpsURLConnection) serverURL.openConnection();
        webConnection.setRequestMethod("POST");
        webConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        webConnection.setRequestProperty("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496");
        JSONObject data = new JSONObject();
        data.put("name", userName);
        data.put("email", emailAddr);
        data.put("password", password);

       webConnection.setDoOutput(true);
       webConnection.setDoInput(true);
       OutputStreamWriter outputWriter = new OutputStreamWriter(webConnection.getOutputStream());
       outputWriter.write(data.toString());
       outputWriter.flush();
       outputWriter.close();


       return Integer.toString(webConnection.getResponseCode());
    }
}

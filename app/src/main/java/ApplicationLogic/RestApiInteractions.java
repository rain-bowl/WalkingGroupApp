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
        webConnection.setRequestProperty("Content-Type", "application/json");
        webConnection.setRequestProperty("Accept", "application/json");
        webConnection.setRequestProperty("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496");
        JSONObject data = new JSONObject();
        data.put("user", userName);
        data.put("email", emailAddr);
        data.put("password", password);
        /*webConnection.setRequestProperty("name", userName);
        webConnection.setRequestProperty("email", emailAddr);
        webConnection.setRequestProperty("password", password);
        webConnection.setDoInput(true);
        webConnection.setDoOutput(true);*/
       /* JSONObject user = new JSONObject();
        JSONObject pass = new JSONObject();
        JSONObject email = new JSONObject();
        email.put("email", emailAddr);
        user.put("user", userName);
        pass.put("password",password);
*/
       webConnection.setDoOutput(true);
       webConnection.setDoInput(true);
        OutputStreamWriter createReqBody = new OutputStreamWriter(webConnection.getOutputStream());
        createReqBody.write(data.toString());
        createReqBody.flush();
        createReqBody.close();

        //Display return of message
        StringBuilder response = new StringBuilder();
       return Integer.toString(webConnection.getResponseCode());
    }
}

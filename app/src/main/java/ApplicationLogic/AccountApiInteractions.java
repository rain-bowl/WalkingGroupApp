package ApplicationLogic;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONArrayRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class AccountApiInteractions {
    private final String baseURL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    private String bearerToken;
    private int userID;

    //Creates a single user using the inputs
public void createNewUser(String userName, String userPassword, String userEmailAddr, Context appContext){
    //Initialize androidNetworking library with current activity context
            AndroidNetworking.initialize(appContext);
            final JSONObject jsonBody = new JSONObject();
            //Create the json body to be attached
        try {
            jsonBody.put("password", userPassword);
            jsonBody.put("email", userEmailAddr);
            jsonBody.put("name",userName);

        }
        catch (Exception e){
            Log.d(TAG, "createNewUser:Unexpected error");
            e.printStackTrace();
        }
//Make POST call to server with attached json body
    AndroidNetworking.post(baseURL + "/users/signup")
            .addHeaders("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496")
            .addJSONObjectBody(jsonBody)
            .build()
            .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                @Override
                public void onResponse(Response okHttpResponse, JSONObject response) {
                    try {
                        userID = response.getInt("id");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onResponse: SUCCESS yAAAYY " + okHttpResponse.code());
                    Log.d(TAG, "onResponse: JsonBody " + response.toString());
                }
                @Override
                public void onError(ANError anError) {
                    Log.d(TAG, "onError:"+ jsonBody.toString());
                    Log.d(TAG, "onError: "+anError.getErrorBody());
                    Log.d(TAG, "onError: "+ anError.getErrorDetail().toString());

                }
            });
    }

    //Handles the login of a user. Sets the bearer token on success.
    public void userLogIn(String email, String password, final Context appContext){

        AndroidNetworking.initialize(appContext);
        final JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        AndroidNetworking.post(baseURL + "/login")
        .addHeaders("apiKey","F369E8E6-244B-4672-B8A8-1E44A32CA496")
        .setContentType("application/json")
        .addJSONObjectBody(jsonBody)
        .build()
        .getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {
               bearerToken = response.header("Authorization");
                Toast.makeText(appContext, bearerToken, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(ANError anError) {
                Log.d(TAG, "onError: Error" + anError.getErrorDetail().toString());
            }
        });
    }
    //Recover bearer token on login
    public String getBearerToken(){
        return bearerToken;
    }
    //Recover user id
    public int getUserID(){
        return userID;
    }


    //old class. Kept here in case shit hits the fan
   /* public String createNewUser(String userName, String password, String emailAddr) throws IOException, JSONException{
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
    }*/
}

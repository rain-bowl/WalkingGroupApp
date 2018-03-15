package ApplicationLogic;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
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

import okhttp3.OkHttpClient;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class AccountApiInteractions {
    private final String baseURL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    private final String apiKey = "F369E8E6-244B-4672-B8A8-1E44A32CA496";
    private  String bearerToken;
    private int userID = -1;

    //Creates a single user using the inputs
public void createNewUser(String userName, String userPassword, String userEmailAddr, Context appContext){
    //Initialize androidNetworking library with current activity context
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
     AndroidNetworking.initialize(appContext);
    ANRequest signupRequest = AndroidNetworking.post(baseURL + "/users/signup")
            .addHeaders("apiKey", apiKey)
            .addJSONObjectBody(jsonBody)
            .build();


    ANResponse<JSONObject> serverResponse = signupRequest.executeForJSONObject();
    if(serverResponse.isSuccess()){
        JSONObject jsonResponseBody = serverResponse.getResult();
        Log.d(TAG, "createNewUser: JSONRESPONSE" + jsonResponseBody);
        try{
        userID = jsonResponseBody.getInt("id");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.d(TAG, "createNewUser: USER ID ON SUCCESS IS" + userID);
    }
    else {
        Log.d(TAG, "createNewUser: ERROR" + serverResponse.getError());
        Log.d(TAG, "createNewUser: ERRODETAIL" + serverResponse.getError().getErrorBody());
    }
    }

    //Handles the login of a user. Sets the bearer token on success.
    public Boolean userLogIn(String email, String password, final Context appContext){
        final JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    AndroidNetworking.initialize(appContext);
    ANRequest request = AndroidNetworking.post(baseURL + "/login")
            .addHeaders("apiKey", apiKey)
            .addJSONObjectBody(jsonBody)
            .build();

    ANResponse<OkHttpResponseListener> responseListenerANResponse = request.executeForOkHttpResponse();
    if(responseListenerANResponse.isSuccess()) {
        if (responseListenerANResponse.getOkHttpResponse().code() == 200){
            Log.d(TAG, "userLogIn: Response Good " + responseListenerANResponse.getOkHttpResponse().code());
        bearerToken = responseListenerANResponse.getOkHttpResponse().header("Authorization");
        return true;
    }
        else{
            return false;
        }
    }
    else {
        Log.d(TAG, "userLogIn: Response Error" + responseListenerANResponse.getError());
        return false;
    }


    }
    //Class which recovers a users ID number from the database. This is needed to implement the user monitoring.
    public int getDatabaseUserID(String email, Context currContext) {
        Log.d(TAG, "getDatabaseUserID: USERID bearer token" + bearerToken);
        AndroidNetworking.initialize(currContext);
        String formattedEmail = email.replace("@", "%40");
        Log.d(TAG, "getDatabaseUserID: Formatted email" + formattedEmail);
        ANRequest getUserIDRequest = AndroidNetworking.get(baseURL + "/users/byEmail?email=" + formattedEmail)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();
        ANResponse<JSONObject> serverResponse = getUserIDRequest.executeForJSONObject();
        if (serverResponse.isSuccess()) {
            JSONObject jsonServerResponse = serverResponse.getResult();
            try {
                userID = jsonServerResponse.getInt("id");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userID;
    }






    //Recover bearer token on login
    public String getBearerToken(){
        return bearerToken;
    }
    //Recover user id
    public int getUserID(){
        return userID;
    }



}

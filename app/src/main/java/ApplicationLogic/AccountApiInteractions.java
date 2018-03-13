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
    private int userID = 0;
    private int groupID = 0;
    private JSONObject groupDetails;

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
                    Log.d(TAG, "onError: "+ anError.getErrorBody());
                    Log.d(TAG, "onError: "+ anError.getErrorDetail());

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
                Log.d(TAG, "onResponse: Success!");
            }

            @Override
            public void onError(ANError anError) {
                Log.d(TAG, "onError: Error" + anError.getErrorDetail());
            }
        });
    }
    //Class which recovers a users ID number from the database. This is needed to implement the user monitoring.
    public int getDatabaseUserID(String email, Context currContext){
        AndroidNetworking.initialize(currContext);
        String formattedEmail = email.replace("@", "%40");
        AndroidNetworking.get(baseURL + "/users/byEmail?email=" + formattedEmail)
        .addHeaders("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496")
        .addHeaders("Authorization", bearerToken)
        .build()
        .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    userID = response.getInt("id");
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {

            }
        });
        if(userID != 0) {
            return userID;
        }
        else {
            return -1;
        }
    }

    //Recover bearer token on login
    public String getBearerToken(){
        return bearerToken;
    }
    //Recover user id
    public int getUserID(){
        return userID;
    }



    /*  classes for group implementation:    */

    // create new group
    public void createNewGroup(String groupDescription, int leaderID, Context appContext){
        //Initialize androidNetworking library with current activity context
        AndroidNetworking.initialize(appContext);
        final JSONObject jsonBody = new JSONObject();
        //Create the json body to be attached
        try {
            jsonBody.put("group description", groupDescription);
            jsonBody.put("routeLatArray", new JSONArray());
            jsonBody.put("routeLngArray", new JSONArray());
            jsonBody.put("leader", leaderID);
            jsonBody.put("memberUsers", new JSONArray());
        }
        catch (Exception e){
            Log.d(TAG, "createNewGroup: Unexpected error");
            e.printStackTrace();
        }

        //Make POST call to server with attached json body
        AndroidNetworking.post(baseURL + "/groups")
                .addHeaders("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496")
                .addHeaders("Authorization", bearerToken)
                .addJSONObjectBody(jsonBody)
                .build()
                .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, JSONObject response) {
                        try {
                            groupID = response.getInt("id");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: Success " + okHttpResponse.code());
                        Log.d(TAG, "onResponse: JsonBody " + response.toString());
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError:"+ jsonBody.toString());
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                        Log.d(TAG, "onError: "+ anError.getErrorDetail());
                    }
                });
    }

    //gets list of all groups
    public void getGroupList(Context currContext){
        AndroidNetworking.initialize(currContext);
        AndroidNetworking.get(baseURL + "/groups")
                .addHeaders("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496")
                .addHeaders("Authorization", bearerToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            groupDetails = response.getJSONObject("id");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: JsonBody " + response.toString());
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                    }
                });
    }

    //gets group's details through groupID
    public void getGroupDetails(int groupID, Context currContext){
        AndroidNetworking.initialize(currContext);
        AndroidNetworking.get(baseURL + "/groups/" + groupID)
                .addHeaders("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496")
                .addHeaders("Authorization", bearerToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            groupDetails = response.getJSONObject("id");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: JsonBody " + response.toString());
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                    }
                });
    }


    // update group
    public void updateGroup(int groupID, final String newDescription, final double latitude, final double longitude, Context appContext){
        //Initialize androidNetworking library with current activity context
        AndroidNetworking.initialize(appContext);
        AndroidNetworking.post(baseURL + "/groups/" + groupID)
                .addHeaders("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496")
                .addHeaders("Authorization", bearerToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response.put("group description", newDescription);
                            response.put("routeLatArray", latitude);
                            response.put("routeLngArray", longitude);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: JsonBody " + response.toString());
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+ anError.getErrorBody());
                        Log.d(TAG, "onError: "+ anError.getErrorDetail());
                    }
                });
    }

    // delete group
    public void deleteGroup(int groupID, Context appContext){
        //Initialize androidNetworking library with current activity context
        AndroidNetworking.initialize(appContext);
        AndroidNetworking.delete(baseURL + "/groups/" + groupID)
                .addHeaders("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496")
                .addHeaders("Authorization", bearerToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: JsonBody " + response.toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Log.d(TAG, "error: JsonBody not deleted" + response.toString());
            }
            @Override
            public void onError(ANError anError) {
                Log.d(TAG, "onError errorCode : " + anError.getErrorCode());
                Log.d(TAG, "onError: " + anError.getErrorBody());
                Log.d(TAG, "onError: " + anError.getErrorDetail());
            }
        });
    }

    // get group members
    public void getGroupMembers(int groupID, Context appContext){
        //Initialize androidNetworking library with current activity context
        AndroidNetworking.initialize(appContext);
        AndroidNetworking.get(baseURL + "/groups/" + groupID + "/memberUsers")
                .addHeaders("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496")
                .addHeaders("Authorization", bearerToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse: JsonBody " + response.toString());
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: JsonBody " + response.toString());
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                    }
                });
    }

    // add group member
    public void addGroupMember(int groupID, final int memberID, Context appContext){
        //Initialize androidNetworking library with current activity context
        AndroidNetworking.initialize(appContext);
        AndroidNetworking.post(baseURL + "/groups/" + groupID + "/memberUsers")
                .addHeaders("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496")
                .addHeaders("Authorization", bearerToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (memberID < userID) {
                                if ((Integer) response.get("id") != memberID) {
                                    response.put("id", memberID);
                                }
                                else {
                                    Log.d(TAG, "fail: user already in group" + response.toString());
                                }
                            }
                            else {
                                Log.d(TAG, "fail: user does not exist" + response.toString());

                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: JsonBody " + response.toString());
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                    }
                });
    }

    // remove group member
    public void removeGroupMember(int groupID, final int memberID, Context appContext){
        //Initialize androidNetworking library with current activity context
        AndroidNetworking.initialize(appContext);
        AndroidNetworking.delete(baseURL + "/groups/" + groupID + "/memberUsers/" + memberID)
                .addHeaders("apiKey", "F369E8E6-244B-4672-B8A8-1E44A32CA496")
                .addHeaders("Authorization", bearerToken)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "onResponse: JsonBody " + response.toString());
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        Log.d(TAG, "error: member not deleted" + response.toString());
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError errorCode : " + anError.getErrorCode());
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                    }
                });
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

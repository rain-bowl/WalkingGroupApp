package ApplicationLogic;


import android.content.Context;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.example.nurdan.lavaproject.R;
import com.example.nurdan.lavaproject.UserMonitorDisplay;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class AccountApiInteractions {
    private final String baseURL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    private String bearerToken;
    private int groupID = 0;
    private JSONObject groupDetails;
    private final String apiKey = "F369E8E6-244B-4672-B8A8-1E44A32CA496";
    private int userID = -1;
    private JSONArray groupList;


        //Creates a single user using the inputs
    public Boolean createNewUser(JSONObject jsonBody, Context appContext){
      Boolean successFlag;
        Log.d(TAG, "createNewUser: Json Body recieved " + jsonBody.toString());
    //Make POST call to server with attached json body
         AndroidNetworking.initialize(appContext);
        ANRequest signupRequest = AndroidNetworking.post(baseURL + "/users/signup")
                .addHeaders("apiKey", apiKey)
                .addJSONObjectBody(jsonBody)
                .build();


        ANResponse<JSONObject> serverResponse = signupRequest.executeForJSONObject();
        if(serverResponse.isSuccess()) {
            if (serverResponse.getOkHttpResponse().code() == 201) {
                JSONObject jsonResponseBody = serverResponse.getResult();
                Log.d(TAG, "createNewUser: JSONRESPONSE" + jsonResponseBody);
                try {
                    userID = jsonResponseBody.getInt("id");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "createNewUser: USER ID ON SUCCESS IS" + userID);
                return true;
            }
            return false;
        }
        else {
            Log.d(TAG, "createNewUser: ERROR" + serverResponse.getError().getErrorBody());
            Log.d(TAG, "createNewUser: ERRODETAIL " + serverResponse.getError().getErrorDetail().toString());
            Log.d(TAG, "createNewUser: More Detail " + serverResponse.getError().getResponse().toString());
        }
        return false;
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
                ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
                bearerToken = responseListenerANResponse.getOkHttpResponse().header("Authorization");
                currInstance.setBearerToken(bearerToken);
                return true;
            }
            else{
                return false;
            }
        }
        else {
            Log.d(TAG, "userLogIn: Response Error" + responseListenerANResponse.getError());
            return null;
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


    /*Sends of the provided JsonObject input to the server to edit the users information. CURRENTLY NOT WORKING PROPERLY!!!*/
    public Boolean editDatabaseUserProfile(JSONObject jsonBody, Context currContext, int userID, String currBearer){
        AndroidNetworking.initialize(currContext);
        ANRequest sendUserInfo = AndroidNetworking.post(baseURL + "/users/" + userID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currBearer)
                .addApplicationJsonBody(jsonBody)
                .build();

        ANResponse<JSONObject> serverResponse = sendUserInfo.executeForJSONObject();
        Log.d(TAG, "editDatabaseUserProfile: JSON BODY INPUT BEFORE SEND "+ jsonBody.toString());
        if(serverResponse.isSuccess()){
            Log.d(TAG, "editDatabaseUserProfile: Success in sneding edited information!!");
            ProgramSingletonController controllerInstance = ProgramSingletonController.getCurrInstance();
            JSONObject responseContent = serverResponse.getResult();
            controllerInstance.setUserInfo(responseContent);
            return true;
        }
        else {
            Log.d(TAG, "editDatabaseUserProfile: Server error when modding info " + serverResponse.getError().getErrorBody());
            Log.d(TAG, "editDatabaseUserProfile: More error info " + serverResponse.getError().getResponse().toString());
        }

        return false;
    }


    //Retreives the user information based on their email.
     public void getDatabaseUserProfile(String email, Context currContext) {
        Log.d(TAG, "getDatabaseUserID: USERID bearer token" + bearerToken);
        AndroidNetworking.initialize(currContext);
        String formattedEmail = email.replace("@", "%40");
        Log.d(TAG, "getDatabaseUserID: Formatted email" + formattedEmail);
        ANRequest getUserIDRequest = AndroidNetworking.get(baseURL + "/users/byEmail?email=" + formattedEmail)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        //Creates an instance of user class, fills its fields and sends it off to the singleton method
         //for access by all other classes

        ANResponse<JSONObject> serverResponse = getUserIDRequest.executeForJSONObject();
        if (serverResponse.isSuccess()) {
            JSONObject jsonServerResponse = serverResponse.getResult();
            ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
            currInstance.setUserInfo(jsonServerResponse);

/*            try {
               currUser.setID(jsonServerResponse.getInt("id"));
               currUser.setName(jsonServerResponse.getString("name"));
               currUser.setEmailAddress(jsonServerResponse.getString("email"));
               currUser.setBirthyear(jsonServerResponse.getInt("birthYear"));
               currUser.setBirthmonth(jsonServerResponse.getInt("birthMonth"));
               currUser.setUserAddress(jsonServerResponse.getString("address"));
               currUser.setCellPhoneNumber(jsonServerResponse.getString("cellPhone"));
               currUser.setHomePhoneNumber(jsonServerResponse.getString("homePhone"));
               currUser.setGrade(jsonServerResponse.getString("grade"));
               currUser.setTeacherName(jsonServerResponse.getString("teacherName"));
               currUser.setEmergencyContactInfoInstruction(jsonServerResponse.getString("emergencyContactInfo"));
               currUser.setMonitoredByUsers(jsonServerResponse.getJSONArray("monitoredByUsers"));
               currUser.setMonitorsOtherUsers(jsonServerResponse.getJSONArray("monitorsUsers"));
               currUser.setMemberOfGroups(jsonServerResponse.getJSONArray("memberOfGroups"));
               currUser.setLeaderOfGroups(jsonServerResponse.getJSONArray("leadsGroups"));
                Log.d(TAG, "getDatabaseUserProfile: USER INFO RECIEVED " + currUser.getBirthyear());
                currUser.setJsonObject(jsonServerResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
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
    public void createNewGroup(final String currToken, String groupDescription, final int leaderID, LatLng start, LatLng dest, Context appContext){
        final JSONObject jsonBody = new JSONObject();
        bearerToken = currToken;

        try {
            JSONArray LatArray = new JSONArray();
            JSONArray LngArray = new JSONArray();
            JSONObject leaderBody = new JSONObject();

            jsonBody.put("groupDescription", groupDescription);

            LatArray.put(start.latitude);
            LatArray.put(dest.latitude);
            jsonBody.put("routeLatArray", LatArray);

            LngArray.put(start.longitude);
            LngArray.put(dest.longitude);
            jsonBody.put("routeLngArray", LngArray);

            leaderBody.put("id", leaderID);
            jsonBody.put("leader", leaderBody);
            jsonBody.put("memberUsers", new JSONArray());
        }
        catch (Exception e){
            Log.d(TAG, "createNewGroup: Unexpected jsonbody error");
            e.printStackTrace();
        }

        //Make POST call to server with attached json body
        AndroidNetworking.initialize(appContext);
        AndroidNetworking.post(baseURL + "/groups")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
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
                        Log.d(TAG, "onError: JSONBODY: " + jsonBody);
                        Log.d(TAG, "onError: bearertoken: " + currToken);
                        Log.d(TAG, "onError: UserID: " + leaderID);
                        Log.d(TAG, "onError: body:"+ jsonBody.toString());
                        Log.d(TAG, "onError: errorbody: "+ anError.getErrorBody());
                        Log.d(TAG, "onError: detail: "+ anError.getErrorDetail());
                    }
                });
    }

    //gets list of all groups
    //todo implement properly :(
    public JSONArray getGroupList(String currToken, Context currContext){
        bearerToken = currToken;
        AndroidNetworking.initialize(currContext);
       // for (int currID = 0; currID < 999; currID++){
            AndroidNetworking.get(baseURL + "/groups")
                    .addHeaders("apiKey", apiKey)
                    .addHeaders("Authorization", bearerToken)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                groupList.put(response);
                                Log.d(TAG, "onResponse: why "+ response.toString());
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            Log.d(TAG, "onResponse: JsonBody " + response.toString());
                        }
                        @Override
                        public void onError(ANError anError) {
                            Log.d(TAG, "onError: errorbody: " + anError.getErrorBody());
                            Log.d(TAG, "onError: detail: " + anError.getErrorDetail());
                        }
                    });
    //    }
        return groupList;
    }


    //gets group's details through groupID
    public void getGroupDetails(String currToken, int groupID, Context currContext){
        AndroidNetworking.initialize(currContext);
        AndroidNetworking.get(baseURL + "/groups/" + groupID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
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
                        Log.d(TAG, "onError body: " + anError.getErrorBody());
                        Log.d(TAG, "onError detail: " + anError.getErrorDetail());
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
                            response.put("groupDescription", newDescription);
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


}

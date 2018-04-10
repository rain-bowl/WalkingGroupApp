package ApplicationLogic;


import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONArrayRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Class contains all methods related to the monitoring of users
 */
public class  UserMonitor {
private JSONArray returnArray;
private boolean successFlag;
private final String baseURL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
private final String apiKey = "F369E8E6-244B-4672-B8A8-1E44A32CA496";

    /**
     * Adds a user to be monitored by the current logged in user
     * @param userID                Id of the user that is logged in(the monitor)
     * @param monitoredUserID       Id of the user that is to be monitored
     * @param bearerKey             Bearer token
     * @param appContext            Context for library
     * @return                      Boolean flag indicating success
     */
    public Boolean addMonitoredUser(int userID, int monitoredUserID, String bearerKey, Context appContext){

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", monitoredUserID);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        AndroidNetworking.initialize(appContext);
        ANRequest addRequest = AndroidNetworking.post(baseURL + "/users/" + userID + "/monitorsUsers")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerKey)
                .addJSONObjectBody(jsonBody)
                .build();
        ANResponse<OkHttpResponseListener> serverResponse = addRequest.executeForOkHttpResponse();
        if (serverResponse.isSuccess()){
            if(serverResponse.getOkHttpResponse().code() == 201){
                Log.d(TAG, "addMonitoredUser: Success in adding user");
                return true;
            }
            else{
                Log.d(TAG, "addMonitoredUser: Could not add user " + serverResponse.getError());
                return false;
            }
        }
        else {
            Log.d(TAG, "addMonitoredUser: Request Error" + serverResponse.getError());
            return false;
        }



    }

    /**
     * Adds a user to monitor the logged in user
     * @param userId           Logged in user id
     * @param monitorID         Id of the user that will monitor the logged in user
     * @param bearer            Bearer token
     * @param appContext        context
     * @return                  Boolean flag indicating status of request
     */
    public Boolean addUsrToMonitorYou(int userId, int monitorID, String bearer, Context appContext){
        Boolean successFlag;
        AndroidNetworking.initialize(appContext);
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("id", monitorID);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        ANRequest addMonitorRequest = AndroidNetworking.post(baseURL + "/users/" + userId + "/monitoredByUsers")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearer)
                .addJSONObjectBody(jsonBody)
                .build();

        ANResponse<OkHttpResponseListener> serverResponse = addMonitorRequest.executeForOkHttpResponse();
        if(serverResponse.isSuccess()){
            if(serverResponse.getOkHttpResponse().code() == 201){
                return true;
            }
            else {
                return false;
            }
        }
        return null;

    }

    /**
     * Method which removes a user from being monitored by the logged in user
     * @param userID            Id of the logged in user
     * @param removedUserId     Id of the user we want to stop monitoring
     * @param bearerKey         Bearer token
     * @param appContext        Context for library
     * @return                  Boolean flag indicating success/failure to complete
     */
    public boolean stopMonitoringUser(int userID, int removedUserId, String bearerKey, Context appContext){
        String accessURL = String.format("/users/%d/monitorsUsers/%d", userID, removedUserId);
        AndroidNetworking.initialize(appContext);
        ANRequest stopMntrRequest = AndroidNetworking.delete(baseURL + accessURL)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerKey)
                .build();

        ANResponse<OkHttpResponseListener> serverResponse = stopMntrRequest.executeForOkHttpResponse();
        if (serverResponse.isSuccess()){
            if(serverResponse.getOkHttpResponse().code() == 204){
                Log.d(TAG, "stopMonitoringUser: Successful removal");
            }
            return true;
        }
        else {
            Log.d(TAG, "stopMonitoringUser: Error with request " + serverResponse.getError());
            return false;
        }
    }


    /**
     * Retreives all users who are monitored
     * @param userID        Id of the logged in user
     * @param bearerToken   Bearer token
     * @param appContext    Context
     * @return              Returns a json array containing all of the users
     * @throws JSONException
     */
    public JSONArray getMonitoredUsers(int userID, String bearerToken, Context appContext) throws JSONException{
        String URLPath = String.format("/users/%d/monitorsUsers", userID);
        Log.d(TAG, "getMonitoredUsers: Formatted URL" + URLPath);
        JSONArray monitorUsersArr = null;

        AndroidNetworking.initialize(appContext);
        Log.d(TAG, "getMonitoredUsers: Bearer token" + bearerToken);
        ANRequest monitoredUsersServerReq = AndroidNetworking.get(baseURL + URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONArray> serverResponse = monitoredUsersServerReq.executeForJSONArray();
        if(serverResponse.isSuccess()){
          monitorUsersArr = serverResponse.getResult();
            Log.d(TAG, "getMonitoredUsers:Success Server response to request" + monitorUsersArr);
        }
        else {
            Log.d(TAG, "getMonitoredUsers: Error when retrieving monitored users " + serverResponse.getError().toString());
        }
        return monitorUsersArr;


    }

    /**
     * Retrieves all users who monitor the logged in user
     * @param userID        Id of the logged in user
     * @param bearerToken   Bearer token
     * @param appContext    Context for library
     * @return              Json array containing all of the users who monitor the logged in user
     */
    public JSONArray getUsersWhoMonitor(int userID, String bearerToken, Context appContext){
        String URLPath = String.format("/users/%d/monitoredByUsers", userID);
        JSONArray users = null;
        AndroidNetworking.initialize(appContext);
        ANRequest usersWhoMonitorRequest = AndroidNetworking.get(baseURL + URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();
        ANResponse<JSONArray> serverResponse = usersWhoMonitorRequest.executeForJSONArray();
        if (serverResponse.isSuccess()){
           users = serverResponse.getResult();
        }
        else {
            Log.d(TAG, "getUsersWhoMonitor: Error from server" + serverResponse.getError());
        }
        return users;
    }


}

package ApplicationLogic;


import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
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

/*Class handles retrieval of information from the server which is directly related
to the monitoring of users or being monitored.
 */
public class UserMonitor {
private JSONArray returnArray;
private boolean successFlag;
private final String baseURL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
private final String apiKey = "F369E8E6-244B-4672-B8A8-1E44A32CA496";
/*This method adds a user to be monitored by another user.
 */
    public void addMonitoredUser(int userID, int monitoredUserID, String bearerKey, Context appContext){
        AndroidNetworking.initialize(appContext);
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", monitoredUserID);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        AndroidNetworking.post(baseURL + "/users/" + userID + "/monitorsUsers")
                .setContentType("application/json")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerKey)
                .addJSONObjectBody(jsonBody)
                .build()
                .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, JSONObject response) {
                        Log.d(TAG, "onResponse: Success");
                        Log.d(TAG, "onResponse: Response" + response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: Error:"+ anError.getResponse().toString());
                    }
                });

    }

    /* Class removes a user which is monitored by another. The inputs are as follows:
    userID corresponds to the user which is monitoring somebody
    removedUserId corresponds to the user being monitored
     */
    public void stopMonitoringUser(int userID, int removedUserId, String bearerKey, Context appContext){
        String accessURL = String.format("/users/%d/monitorsUsers/%d", userID, removedUserId);
        AndroidNetworking.initialize(appContext);
        AndroidNetworking.delete(baseURL + accessURL)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerKey)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        Log.d(TAG, "onResponse: Success!");
                        Log.d(TAG, "onResponse: " + response.code());
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

//Recovers users which are monitored by the current logged in user. Currently needs
//some more work in particular, the case where it is not successful.
    public JSONArray getMonitoredUsers(int userID, String bearerToken, Context appContext) throws JSONException{
        String URLPath = String.format("/users/%d/monitorsUsers", userID);
        AndroidNetworking.initialize(appContext);
        AndroidNetworking.get(baseURL + URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build()
                .getAsOkHttpResponseAndJSONArray(new OkHttpResponseAndJSONArrayRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, JSONArray response) {
                       successFlag = true;
                       returnArray = response;
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
        if (successFlag == true) {
            return returnArray;
        }
        else{
            throw new JSONException("Was not able to parse json responce");
        }
    }

    public JSONArray getUsersWhoMonitor(int userID, String bearerToken, Context appContext){
        String URLPath = String.format("/users/%d/monitoredByUsers", userID);
        AndroidNetworking.initialize(appContext);
        AndroidNetworking.get(baseURL + URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build()
                .getAsOkHttpResponseAndJSONArray(new OkHttpResponseAndJSONArrayRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, JSONArray response) {
                       successFlag = true;
                       returnArray = response;
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: Error with retrieving monitored users" + anError.getErrorBody().toString());

                    }
                });
        return returnArray;
    }


}

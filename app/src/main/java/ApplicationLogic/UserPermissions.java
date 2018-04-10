package ApplicationLogic;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * This class implements all of the methods related to settigns the user permissions
 */
public class UserPermissions {
    private final String apiKey = "F369E8E6-244B-4672-B8A8-1E44A32CA496";
    private final String baseURL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";

    /**
     * Handles the response to a permission
     * @param permisID          Id of the permission
     * @param bearerToken       Bearer token
     * @param status            Boolean value representing whether or not a permission is denied or accepted
     */
    public void respondToRequest(int permisID, String bearerToken, Boolean status){
        //Default value is denied, if status == true then it is set to approved
        String userChoice = "DENIED";
        if(status){
            userChoice = "APPROVED";
        }
        AndroidNetworking.post(baseURL + "/permissions/" + permisID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .addApplicationJsonBody(userChoice)
                .build()
                .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, JSONObject response) {
                        Log.d(TAG, "onResponse: Response on successful send " + okHttpResponse.code());
                        Log.d(TAG, "onResponse: Respnse body on success " + okHttpResponse.body());
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: Error when sending response " + anError.getErrorDetail());
                    }
                });
    }

    /**
     * Synchronous method that retrieves all of the accepted requests for a particular user
     * @param userID            ID of the user
     * @param bearerToken       Bearer token
     * @return                  JSON array containing all permissions
     */
    public JSONArray getAcceptedRequests(int userID, String bearerToken){
        ANRequest permRequest = AndroidNetworking.get(baseURL + "/permissions?userId=" + userID + "&statusForUser=APPROVED")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONArray> permResponse = permRequest.executeForJSONArray();
        if(permResponse.isSuccess()){
            Log.d(TAG, "getAcceptedRequests: Accepted request status " + permResponse.getOkHttpResponse().code());
            Log.d(TAG, "getAcceptedRequests: Accepted requests " + permResponse.getResult().toString());
            return permResponse.getResult();
        }
        else{
            Log.d(TAG, "getAcceptedRequests: Accept request fail " + permResponse.getOkHttpResponse().code());
        }
        return null;
    }

    /**
     * Synchronous method to retrieve all of the permissions which have been denied
     * @param userID        User id representing the user we want to retrieve permissions for
     * @param bearerToken   Bearer token
     * @return              Json array containing all of the denied permissions
     */
    public JSONArray getDeniedRequests(int userID, String bearerToken){
        ANRequest permRequest = AndroidNetworking.get(baseURL + "/permissions?userId=" + userID + "&statusForUser=DENIED")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONArray> permResponse = permRequest.executeForJSONArray();
        if(permResponse.isSuccess()){
            Log.d(TAG, "getDeniedRequests: Success in getting denied requests " + permResponse.getOkHttpResponse().code());
            Log.d(TAG, "getDeniedRequests: Contents of denied requests " + permResponse.getResult().toString());
            return permResponse.getResult();
        }
        else {
            Log.d(TAG, "getDeniedRequests: Error " + permResponse.getError().getErrorDetail());
        }
        return null;
    }

    public JSONArray getPendingRequests(int userID, String bearerToken){
        ANRequest permRequest = AndroidNetworking.get(baseURL + "/permissions?userId=" + userID + "&statusForUser=PENDING")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONArray> permResponse = permRequest.executeForJSONArray();
        if(permResponse.isSuccess()){
            return permResponse.getResult();
        }
        return null;
    }
    /**
     * Synchronous method which retrieves the message which is attached to a particular permission. Used to display
     * this to the user.
     * @param requestID         Id of the permission to be retrieved
     * @param bearerToken       Bearer token
     * @return                  Message for the permission in string format
     */
    public String getRequestMessage (int requestID, String bearerToken){
        ANRequest getRequestMessage = AndroidNetworking.get(baseURL + "/permissions/" + requestID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();


        ANResponse<JSONObject> serverResponse = getRequestMessage.executeForJSONObject();
        if(serverResponse.getOkHttpResponse().code() == 200){
            try {
                return serverResponse.getResult().getString("message");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}




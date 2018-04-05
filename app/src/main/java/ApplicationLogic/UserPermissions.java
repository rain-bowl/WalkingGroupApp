package ApplicationLogic;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/*This class implements all of the code related to working with any kind of user permissions */
public class UserPermissions {
    private final String apiKey = "F369E8E6-244B-4672-B8A8-1E44A32CA496";
    private final String baseURL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";

    //Method to handle the response to a request. Requires the Id of the permission as well as a boolean value to indicate
    //the response from the user.
    public void respondToRequest(int permisID, String bearerToken, Boolean status){
        //Default value is denied, if status == true then it is set to approved
        String userChoice = "\"DENIED\"";
        if(status){
            userChoice = "\"APPROVED\"";
        }
        AndroidNetworking.post(baseURL + "/permissions/" + permisID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .addStringBody(userChoice)
                .build();
    }

    //Synchronous method to retrieve the accepted requests. Returns a JSON array containing all of the requests
    public JSONArray getAcceptedRequests(int userID, String bearerToken){
        ANRequest permRequest = AndroidNetworking.get(baseURL + "permissions?userId" + userID + "&statusForUser=APPROVED")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONArray> permResponse = permRequest.executeForJSONArray();
        if(permResponse.getOkHttpResponse().code() == 200){
            return permResponse.getResult();
        }
        return null;
    }

    //Synchronous method to retrieve the denied requests. Returns a JSON array containing all of the requests
    public JSONArray getDeniedRequests(int userID, String bearerToken){
        ANRequest permRequest = AndroidNetworking.get(baseURL + "permissions?userId" + userID + "&statusForUser=DENIED")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONArray> permResponse = permRequest.executeForJSONArray();
        if(permResponse.getOkHttpResponse().code() == 200){
            return permResponse.getResult();
        }
        return null;
    }

    //Method retrieves the message attached to a particular request. Used to display the request to the user.
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




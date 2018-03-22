package ApplicationLogic;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;

import org.json.JSONObject;

/*This class will contain all network calls as well as data formatting needed to implement the messaging between users.

 */
public class UserMessagingService {
    private final String apiKey = "F369E8E6-244B-4672-B8A8-1E44A32CA496";
    private final String baseURL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";


    /*Sends a message to the specified group. The message is held in as one string.

     */
    public void newMessageToGroup(String message, int groupID, Boolean emergencyStatus, String bearerToken, Context currContext){
        JSONObject requestBody = new JSONObject();
        try{
            requestBody.put("text", message);
            requestBody.put("emergency", emergencyStatus);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        AndroidNetworking.initialize(currContext);
        AndroidNetworking.post(baseURL + "/messages/togroup/" + groupID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .addJSONObjectBody(requestBody)
                .build();
    }

    /*Sends a message to the parents of a user
     */
    public void newMessageToParents(String message, int userID, Boolean emergencyStatus, String bearerToken, Context currContext){
        JSONObject requestBody = new JSONObject();
        try{
            requestBody.put("text", message);
            requestBody.put("emergency", emergencyStatus);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        AndroidNetworking.initialize(currContext);
        AndroidNetworking.post(baseURL + "/messages/toparents/" + userID )
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .addJSONObjectBody(requestBody)
                .build();
    }
    //Retrieves a single message by its message id. If it is sucessful in doing so then it will return the server
    //response as a JSON object. Note, the JSONObject returned is "untouched" so all values will have to be retrieved
    //from it as they are needed which means that it cannot be iterated through.
    public JSONObject retrieveSingleMessage(int messageID, String bearerToken, Context currContext)
    { AndroidNetworking.initialize(currContext);
        ANRequest getMessage = AndroidNetworking.get(baseURL + "/messages/" + messageID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONObject> serverResponse = getMessage.executeForJSONObject();

        if(serverResponse.isSuccess()){
            if(serverResponse.getOkHttpResponse().code() == 200){
                return  serverResponse.getResult();
            }
        }
        return null;
    }

    //Gets all messages for a single user. These can be both emergency and non-emergency
    public JSONObject getMessagesForSingleUser(int userID, String bearerToken, Context currContext){
        AndroidNetworking.initialize(currContext);
        ANRequest retrieveMessagesRequest = AndroidNetworking.get(baseURL + "/messages?foruser=" + userID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONObject> serverResponse = retrieveMessagesRequest.executeForJSONObject();

        if(serverResponse.isSuccess()){
            if(serverResponse.getOkHttpResponse().code() == 200){
                return serverResponse.getResult();
            }
        }
        return null;
    }

     //Gets all read messages for a single user. These can be both emergency and non-emergency
    public JSONObject getReadMessagesForSingleUser(int userID, String bearerToken, Context currContext){
        AndroidNetworking.initialize(currContext);
        ANRequest retrieveMessagesRequest = AndroidNetworking.get(baseURL + "/messages?foruser=" + userID + "&status=unread")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONObject> serverResponse = retrieveMessagesRequest.executeForJSONObject();

        if(serverResponse.isSuccess()){
            if(serverResponse.getOkHttpResponse().code() == 200){
                return serverResponse.getResult();
            }
        }
        return null;
    }

    //Gets all unread messages for a single user. These can be both emergency and non-emergency.
    //Returns a raw json object response which will need to be worked with.
    public JSONObject getUnreadMessagesForSingleUser(int userID, String bearerToken, Context currContext){
        AndroidNetworking.initialize(currContext);
        ANRequest retrieveMessagesRequest = AndroidNetworking.get(baseURL + "/messages?foruser=" + userID + "&status=read")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONObject> serverResponse = retrieveMessagesRequest.executeForJSONObject();

        if(serverResponse.isSuccess()){
            if(serverResponse.getOkHttpResponse().code() == 200){
                return serverResponse.getResult();
            }
        }
        return null;
    }

    //Gets all messages for a certain group with the groupID specified. These are both emergency and non-emergency.
    public JSONObject getMessagesForGroup(int groupID, String bearerToken, Context currContext){
        AndroidNetworking.initialize(currContext);
        ANRequest retrieveMessagesRequest = AndroidNetworking.get(baseURL + "/messages?togroup=" + groupID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONObject> serverResponse = retrieveMessagesRequest.executeForJSONObject();

        if(serverResponse.isSuccess()){
            if(serverResponse.getOkHttpResponse().code() == 200){
                return serverResponse.getResult();
            }
        }
        return null;
    }

    //Gets ONLY the emergency messages for a group specified by the groupID parameter.
     public JSONObject getEmergencyMessagesForGroup(int groupID, String bearerToken, Context currContext){
        AndroidNetworking.initialize(currContext);
        ANRequest retrieveMessagesRequest = AndroidNetworking.get(baseURL + "/messages?togroup=" + groupID + "&is-emergency=true")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONObject> serverResponse = retrieveMessagesRequest.executeForJSONObject();

        if(serverResponse.isSuccess()){
            if(serverResponse.getOkHttpResponse().code() == 200){
                return serverResponse.getResult();
            }
        }
        return null;
    }


}

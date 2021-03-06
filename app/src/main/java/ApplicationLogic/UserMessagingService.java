package ApplicationLogic;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.example.nurdan.lavaproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import java.util.logging.LogRecord;

import okhttp3.Response;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;
import static java.security.AccessController.getContext;

/**
 * Class contains all networking calls needed to facilitate messaging between users.
 */
public class UserMessagingService {
    private final String apiKey = "F369E8E6-244B-4672-B8A8-1E44A32CA496";
    private final String baseURL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    JSONObject currMsg= new JSONObject();
    private ProgramSingletonController currController=ProgramSingletonController.getCurrInstance();


    /**
     * Sends a message to the specified group
     * @param message           Message we want to send
     * @param groupID           ID of the group that we want ot send it to
     * @param emergencyStatus   Boolean representing whether or not this is a panic message
     * @param bearerToken       Bearer for authentication
     * @param currContext       Context for library
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

        Log.d(TAG, "newMessageToGroup: Before sending message to group #" + groupID + ", jsonobject: " + requestBody);


        AndroidNetworking.initialize(currContext);
        AndroidNetworking.post(baseURL + "/messages/togroup/" + groupID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .addJSONObjectBody(requestBody)
                .build()
                .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener(){
            @Override
            public void onResponse(Response httpresp, JSONObject response) {
                if(httpresp.code() == 201){
                    Log.d(TAG, "newMessageToGroup onResponse: Success when sending message to group " + httpresp.code());
                    Log.d(TAG, "newMessageToGroup onResponse: Success when sending message to group, response: " + response);
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.d(TAG, "onError: Error when sending message to groups!" + anError.getErrorDetail());
            }
        });
    }

    /**
     * Sends a message to the parents of the user
     * @param message           Message text
     * @param userID            The user to whose parents we want to send the message to
     * @param emergencyStatus   Boolean indicating if its a panic message or not
     * @param bearerToken       Bearer for authentication
     * @param currContext       Context for library
     */
    public void newMessageToParents(String message, int userID, Boolean emergencyStatus, String bearerToken, final Context currContext){
        final JSONObject requestBody = new JSONObject();
        try{
            requestBody.put("text", message);
            requestBody.put("emergency", emergencyStatus);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        AndroidNetworking.initialize(currContext);
        AndroidNetworking.post(baseURL + "/messages/toparentsof/" + userID )
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .addJSONObjectBody(requestBody)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if(response.code() == 201){
                            Toast.makeText(currContext, R.string.successfulMessageSend, Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse: Success when sending message to parents " + response.code());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(currContext, R.string.errorMessageSend, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onError: Error when sending message to parents!" + anError.getErrorDetail());

                    }
                });
    }

    /**
     * Retreives a single message by its id. If it is sucessful in doing so then it will return the server response
     * as a json object. Note, the JSON object returned is untouched so all values will have to be retrieved from it as they are
     * needed which means that we cannot simply iterate through it to display information
     * @param messageID         ID of the message to be retrieved
     * @param bearerToken       Bearer token
     * @param currContext       context for library
     * @return
     */
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

    /**
     * Retrieves all messages for single user. Both emergency and non-emergency ones
     * @param userID            Id of the user whose messages are to be retrieved
     * @param bearerToken       Bearer token
     * @param currContext       Context for library
     * @return
     */
    public JSONArray getMessagesForSingleUser(int userID, String bearerToken, Context currContext){
        AndroidNetworking.initialize(currContext);
        ANRequest retrieveMessagesRequest = AndroidNetworking.get(baseURL + "/messages?foruser=" + userID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONArray> serverResponse = retrieveMessagesRequest.executeForJSONArray();

        if(serverResponse.isSuccess()){
            if(serverResponse.getOkHttpResponse().code() == 200){
                Log.d(TAG, "getMessagesForSingleUser: Server response " + serverResponse.getResult().toString());
                return serverResponse.getResult();
            }
        } else {
            Log.d(TAG, "getMessagesForSingleUser: error " + serverResponse.getError().toString());
        }
        return null;
    }

    /**
     * Retrive a single message designated for the current user
     * @param messageId         Id of the message
     * @param userId            Id of the user
     * @param bearerToken       Bearer token
     * @param currContext       Context
     * @return                  Returns a json object containing the message details
     */
    public JSONObject getMessageById(int messageId, int userId, String bearerToken, Context currContext) {
        AndroidNetworking.initialize(currContext);
        ANRequest retrieveMessagesRequest = AndroidNetworking.get(baseURL + "/messages/" + messageId)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse serverResponse = retrieveMessagesRequest.executeForJSONObject();

        if(serverResponse.isSuccess()) {
            if(serverResponse.getOkHttpResponse().code() == 200) {
                Log.d(TAG, "getSingleMessage: Server response for message id " + messageId + " " + serverResponse.getResult().toString());
                return (JSONObject) serverResponse.getResult();
            }
        } else {
            Log.d(TAG, "getSingleMessage: message id " + messageId + " error " + serverResponse.getError().getErrorCode());
        }
        return null;
    }


    /**
     * Sets a message as read by the user
     * @param isRead            Boolean flag indicating that it has been read
     * @param messageId         Id of the message
     * @param userId            Id of the user
     * @param bearerToken       Bearer token
     * @param context           Context for networking library
     */
    public void setMessageRead(boolean isRead, int messageId, int userId, String bearerToken, Context context) {
        final Context currContext = context;
        AndroidNetworking.initialize(currContext);
        AndroidNetworking.post(baseURL + "/messages/" + messageId + "/readby/" + userId )
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .addApplicationJsonBody(isRead)
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        if(response.code() == 201){
                            Toast.makeText(currContext, R.string.successfulMessageSend, Toast.LENGTH_LONG).show();
                        }
                        Log.d(TAG, "MessageStatus: " + response.code());
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(currContext, R.string.errorMessageSend, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "MessageStatus: " + anError.getErrorDetail());
                    }
                });
    }

    public JSONObject getMessageByDefault() {

        ANRequest retrieveMessagesRequest = AndroidNetworking.get(baseURL + "&is-emergency=true")
                .addHeaders("apiKey", apiKey)
                .build();

        ANResponse<JSONObject> serverResponse = retrieveMessagesRequest.executeForJSONObject();

        if (serverResponse.isSuccess()) {
            if (serverResponse.getOkHttpResponse().code() == 200) {
                return serverResponse.getResult();
            }
        }
        return null;
    }


    //update the message every 30s to show up,using Handler to  refresh the UI thread

    private class setMessages extends  AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            //anything that i can retrieve the Msg defalut
            getMessageByDefault();


            return null;
        }
    }


    //---------------------------- Deprecated -------------------------------------------//
    private void handlerMsgCall(){
         Handler handler = new Handler();
         Runnable runnableCode=new Runnable() {
             @Override
             public void run() {
                setMessages messagebox=new setMessages();
                messagebox.execute();
             }
         };
        handler.postDelayed(runnableCode,60000);
        handler.post(runnableCode);

    }
     //Gets all read messages for a single user. These can be both emergency and non-emergency
    public JSONArray getReadMessagesForSingleUser(int userID, String bearerToken, Context currContext){
        AndroidNetworking.initialize(currContext);
        ANRequest retrieveMessagesRequest = AndroidNetworking.get(baseURL + "/messages?foruser=" + userID + "&status=unread")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONArray> serverResponse = retrieveMessagesRequest.executeForJSONArray();

        if(serverResponse.isSuccess()){
            if(serverResponse.getOkHttpResponse().code() == 200){
                return serverResponse.getResult();
            }
        }
        return null;
    }

    //Gets all unread messages for a single user. These can be both emergency and non-emergency.
    //Returns a raw json object response which will need to be worked with.
    public JSONArray getUnreadMessagesForSingleUser(int userID, String bearerToken, Context currContext){
        AndroidNetworking.initialize(currContext);
        ANRequest retrieveMessagesRequest = AndroidNetworking.get(baseURL + "/messages?foruser=" + userID + "&status=read")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONArray> serverResponse = retrieveMessagesRequest.executeForJSONArray();

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

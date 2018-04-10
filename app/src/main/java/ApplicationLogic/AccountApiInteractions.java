package ApplicationLogic;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Response;
import static android.content.ContentValues.TAG;

public class AccountApiInteractions {
    private final String baseURL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    private String bearerToken;
    private int groupID = 0;
    private JSONObject groupDetails;
    private final String apiKey = "F369E8E6-244B-4672-B8A8-1E44A32CA496";
    private int userID = -1;
    private JSONArray groupList = new JSONArray();


    //Creates a single user using the inputs provided during registration
    public Boolean createNewUser(JSONObject jsonBody, Context appContext) {
        Boolean successFlag;
        Log.d(TAG, "createNewUser: Json Body recieved " + jsonBody.toString());
        //Make POST call to server with attached json body
        AndroidNetworking.initialize(appContext);
        ANRequest signupRequest = AndroidNetworking.post(baseURL + "/users/signup")
                .addHeaders("apiKey", apiKey)
                .addJSONObjectBody(jsonBody)
                .build();


        ANResponse<JSONObject> serverResponse = signupRequest.executeForJSONObject();
        if (serverResponse.isSuccess()) {
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
        } else {
            Log.d(TAG, "createNewUser: ERROR" + serverResponse.getError().getErrorBody());
            Log.d(TAG, "createNewUser: ERRODETAIL " + serverResponse.getError().getErrorDetail().toString());
            Log.d(TAG, "createNewUser: More Detail " + serverResponse.getError().getResponse().toString());
        }
        return false;
    }

    //Handles the login of a user. Sets the bearer token on success.
    public Boolean userLogIn(String email, String password, final Context appContext) {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AndroidNetworking.initialize(appContext);
        ANRequest request = AndroidNetworking.post(baseURL + "/login")
                .addHeaders("apiKey", apiKey)
                .addJSONObjectBody(jsonBody)
                .build();

        ANResponse<OkHttpResponseListener> responseListenerANResponse = request.executeForOkHttpResponse();
        if (responseListenerANResponse.isSuccess()) {
            if (responseListenerANResponse.getOkHttpResponse().code() == 200) {
                Log.d(TAG, "userLogIn: Response Good " + responseListenerANResponse.getOkHttpResponse().code());
                bearerToken = responseListenerANResponse.getOkHttpResponse().header("Authorization");
                return true;
            } else {
                Log.d(TAG, "userLogIn: Response Not Good " + responseListenerANResponse.getOkHttpResponse().code());
                return false;
            }
        } else {
            Log.d(TAG, "userLogIn: Response Error" + responseListenerANResponse.getError());
            return false;
        }
    }

    public JSONArray getAllUsers (String currToken, Context currContext){
        String URLPath = baseURL + "/users";
        JSONArray list = null;

        AndroidNetworking.initialize(currContext);
        ANRequest usersReq = AndroidNetworking.get(URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
                .build();

        ANResponse<JSONArray> serverResponse = usersReq.executeForJSONArray();
        if (serverResponse.isSuccess()) {
            list = serverResponse.getResult();
        } else {
            Log.d(TAG, "getAllUsers: Error from server" + serverResponse.getError());
        }
        if (list != null) {
            Log.d(TAG, "onResponse: JSONObject b/f return: " + list.toString());
        }
        return list;
    }

    public JSONObject getUserByID (String currToken, int ID, Context currContext){
        String URLPath = baseURL + "/users/" + ID;
        JSONObject list = null;
        AndroidNetworking.initialize(currContext);
        ANRequest groupListReq = AndroidNetworking.get(URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
                .build();
        ANResponse<JSONObject> serverResponse = groupListReq.executeForJSONObject();
        if (serverResponse.isSuccess()) {
            list = serverResponse.getResult();
        } else {
            Log.d(TAG, "getUserByID: Error from server" + serverResponse.getError());
        }
        if (list != null) {
            Log.d(TAG, "onResponse: JSONObject b/f return: " + list.toString());
        }
        return list;
    }

    //Recovers a users ID number from the database. This is needed to implement the user monitoring.
    public int getDatabaseUserID(String email, Context currContext, String bearer) {
        Log.d(TAG, "getDatabaseUserID: USERID bearer token" + bearer);
        AndroidNetworking.initialize(currContext);
        String formattedEmail = email.replace("@", "%40");
        Log.d(TAG, "getDatabaseUserID: Formatted email" + formattedEmail);
        ANRequest getUserIDRequest = AndroidNetworking.get(baseURL + "/users/byEmail?email=" + formattedEmail)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearer)
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


    /*Sends the provided JsonObject input to the server to edit the users information.
      */
    public Boolean editDatabaseUserProfile(JSONObject jsonBody, Context currContext, int userID, String currBearer) {
        AndroidNetworking.initialize(currContext);
        Log.d(TAG, "editDatabaseUserProfile: ID AND BEARER " + userID + "" + currBearer);
        ANRequest sendUserInfo = AndroidNetworking.post(baseURL + "/users/" + userID)
                .setContentType("application/json")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currBearer)
                .addJSONObjectBody(jsonBody)
                .build();

        ANResponse<JSONObject> serverResponse = sendUserInfo.executeForJSONObject();
        Log.d(TAG, "editDatabaseUserProfile: JSON BODY INPUT BEFORE SEND " + jsonBody.toString());
        if (serverResponse.isSuccess()) {
            Log.d(TAG, "editDatabaseUserProfile: Success in sneding edited information!!");
            ProgramSingletonController controllerInstance = ProgramSingletonController.getCurrInstance();
            JSONObject responseContent = serverResponse.getResult();
            controllerInstance.setUserInfo(responseContent);
            return true;
        } else {
            Log.d(TAG, "editDatabaseUserProfile: Server error when modding info " + serverResponse.getError().getErrorBody());
            Log.d(TAG, "editDatabaseUserProfile: More error info " + serverResponse.getError().getResponse().toString());
        }

        return false;
    }


    //Retrieves user information based on the used ID provided
    public JSONObject getUserInfoByID(int id, String bearerKey, Context currContext){
        AndroidNetworking.initialize(currContext);
        ANRequest getUserProfile = AndroidNetworking.get(baseURL + "/users/" + id)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerKey)
                .build();

        ANResponse<JSONObject> serverResponse = getUserProfile.executeForJSONObject();
        if(serverResponse.isSuccess()){
            JSONObject response = serverResponse.getResult();
            Log.d(TAG, "getUserProfileByID: SUCCESS " + response.toString());
            return response;
        }
        else{
            Log.d(TAG, "getUserProfileByID: FAILURE for ID " + id + " " + serverResponse.getError().getErrorBody());
            return null;
        }
    }


    //Retreives the user information based on the email which is provided. Used to retrieve user information during login.
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
            Log.d(TAG, "getDatabaseUserProfile: USER INFORMATION ON LOGIN " + jsonServerResponse.toString());
            ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
            try {
                Log.d(TAG, "getDatabaseUserProfile: Test for permissions: " + jsonServerResponse.getString("pendingPermissionRequests").toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
            currInstance.setUserInfo(jsonServerResponse);
        }
    }
    //Retrieves the name of a single group and returns it. Used to list the different groups to implement the messaging feature.
    public String getGroupName(String bearerToken, Context currContext, int groupID){
        AndroidNetworking.initialize(currContext);

        ANRequest groupNameRequest = AndroidNetworking.get(baseURL + "/groups/" + groupID)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", bearerToken)
                .build();

        ANResponse<JSONObject> serverResponse = groupNameRequest.executeForJSONObject();
        if(serverResponse.getOkHttpResponse().code() == 200){
            String temp;
            try{
                temp = serverResponse.getResult().getString("groupDescription");
                return temp;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }



    //Recover bearer token on login
    public String getBearerToken() {
        return bearerToken;
    }

    //Recover user id after login
    public int getUserID() {
        return userID;
    }


    /*  classes for group implementation:    */

    // create new group and set its corrdinates as well as a leader.
    public void createNewGroup(final String currToken, String groupDescription, final int leaderID, LatLng start, LatLng dest, Context appContext) {
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
        } catch (Exception e) {
            Log.d(TAG, "createNewGroup: Unexpected jsonbody error");
            e.printStackTrace();
        }

        //Make POST call to server with attached json body
        AndroidNetworking.initialize(appContext);
        AndroidNetworking.post(baseURL + "/groups")
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
                .addHeaders("PERMISSIONS-ENABLED", "true")
                .addJSONObjectBody(jsonBody)
                .build()
                .getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, JSONObject response) {
                        try {
                            groupID = response.getInt("id");
                        } catch (Exception e) {
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
                        Log.d(TAG, "onError: body:" + jsonBody.toString());
                        Log.d(TAG, "onError: errorbody: " + anError.getErrorBody());
                        Log.d(TAG, "onError: detail: " + anError.getErrorDetail());
                    }
                });
    }

    //Return a list of all of the groups created. Used for testing.
    public JSONArray getGroupList(String currToken, Context appContext) {
        String URLPath = baseURL + "/groups";
        JSONArray list = null;
        AndroidNetworking.initialize(appContext);
        ANRequest groupListReq = AndroidNetworking.get(URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
                .build();
        ANResponse<JSONArray> serverResponse = groupListReq.executeForJSONArray();
        if (serverResponse.isSuccess()) {
            list = serverResponse.getResult();
        } else {
            Log.d(TAG, "getGroupList: Error from server" + serverResponse.getError());
        }
        if (list != null) {
            Log.d(TAG, "onResponse: JSONArray b/f return: " + list.toString());
        }
        return list;
    }


    //gets group's details through the provided groupID
    public JSONObject getGroupDetails(String currToken, int groupID, Context currContext) {
        String URLPath = baseURL + "/groups/" + groupID;
        JSONObject details = null;
        AndroidNetworking.initialize(currContext);
        ANRequest groupDetailsReq = AndroidNetworking.get(URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
                .build();
        ANResponse<JSONObject> serverResponse = groupDetailsReq.executeForJSONObject();
        if (serverResponse.isSuccess()) {
            details = serverResponse.getResult();
            Log.d(TAG, "getGroupDetails: success! respon: " + details);

        } else {
            Log.d(TAG, "getGroupDetails: Error from server" + serverResponse.getError());
        }
        if (details != null) {
            Log.d(TAG, "onResponse: JSONArray b/f return: " + details.toString());
        }
        return details;
    }

    // Updates a groups location, leader and group name
    public void updateGroup(String currToken, int groupID, int leaderID, String newDescription, JSONArray latitude, JSONArray longitude, Context currContext, Boolean newLeaderFlag) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("groupDescription", newDescription);
            JSONObject lead = new JSONObject();
            lead.put("id", leaderID);
            jsonBody.put("leader", lead);
            jsonBody.put("routeLatArray", latitude);
            jsonBody.put("routeLngArray", longitude);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //If true then that means that we have a new group leader selected. This will set the permissions on.
        if(newLeaderFlag) {
            Log.d(TAG, "updateGroup: NEW LEADER");
            String URLPath = baseURL + "/groups/" + groupID;
            AndroidNetworking.initialize(currContext);
            ANRequest groupEditReq = AndroidNetworking.post(URLPath)
                    .addHeaders("apiKey", apiKey)
                    .addHeaders("Authorization", currToken)
                    .addHeaders("PERMISSIONS-ENABLED", "true")
                    .addJSONObjectBody(jsonBody)
                    .build();
            ANResponse<JSONObject> serverResponse = groupEditReq.executeForJSONObject();
            Log.d(TAG, "updateGroup: JSON BODY INPUT BEFORE SEND " + jsonBody.toString());
            if (serverResponse.isSuccess()) {
                Log.d(TAG, "updateGroup: Success in sending edited information!!");
                Log.d(TAG, "updateGroup: Server response on success edit " + serverResponse.getOkHttpResponse().code());
            } else {
                Log.d(TAG, "updateGroup: Server error when modding info detail: " + serverResponse.getError().getErrorDetail());
                Log.d(TAG, "updateGroup: Server error when modding info " + serverResponse.getError().getErrorBody());
                Log.d(TAG, "updateGroup: More error info " + serverResponse.getError().getResponse());
            }
        }
        //If no new leader is selected then there is no need for permissions so it is excluded.
        else{
             String URLPath = baseURL + "/groups/" + groupID;
            AndroidNetworking.initialize(currContext);
            ANRequest groupEditReq = AndroidNetworking.post(URLPath)
                    .addHeaders("apiKey", apiKey)
                    .addHeaders("Authorization", currToken)
                    .addJSONObjectBody(jsonBody)
                    .build();
            ANResponse<JSONObject> serverResponse = groupEditReq.executeForJSONObject();
            Log.d(TAG, "updateGroup: JSON BODY INPUT BEFORE SEND " + jsonBody.toString());
            if (serverResponse.isSuccess()) {
                Log.d(TAG, "updateGroup: Success in sending edited information!!");
            } else {
                Log.d(TAG, "updateGroup: Server error when modding info detail: " + serverResponse.getError().getErrorDetail());
                Log.d(TAG, "updateGroup: Server error when modding info " + serverResponse.getError().getErrorBody());
                Log.d(TAG, "updateGroup: More error info " + serverResponse.getError().getResponse());
            }
        }
    }

    // will delete later, backup
    /*
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
*/
    // deletes the group corresponding to the provided group ID
    public void deleteGroup(String currToken, int groupID, Context appContext) {
        String URLPath = baseURL + "/groups/" + groupID;
        AndroidNetworking.initialize(appContext);
        ANRequest deleteGroupReq = AndroidNetworking.delete(URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
                .build();

        ANResponse<OkHttpResponseListener> serverResponse = deleteGroupReq.executeForOkHttpResponse();
        if (serverResponse.isSuccess()) {
            if (serverResponse.getOkHttpResponse().code() == 204) {
                Log.d(TAG, "deleteGroup: Successful removal");
            }
        } else {
            Log.d(TAG, "deleteGroup: Error with request " + serverResponse.getError());
        }
    }

    // Returns a JSON array containing all of the members of the group corresponding to the group ID provided
    public JSONArray getGroupMembers(String currToken, int groupID, Context appContext) {
        String URLPath = baseURL + "/groups/" + groupID + "/memberUsers";
        JSONArray list = null;
        AndroidNetworking.initialize(appContext);
        ANRequest groupMemReq = AndroidNetworking.get(URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
                .build();
        ANResponse<JSONArray> serverResponse = groupMemReq.executeForJSONArray();
        if (serverResponse.isSuccess()) {
            list = serverResponse.getResult();
        } else {
            Log.d(TAG, "getGroupMembers: Error from server" + serverResponse.getError());
        }
        if (list != null) {
            Log.d(TAG, "onResponse: JSONArray b/f return: " + list.toString());
        }
        return list;
    }

    // Adds a member to the group indicated by the groupID
    public void addGroupMember(String currToken, int groupID, final int memberID, Context currContext) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", memberID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String URLPath = baseURL + "/groups/" + groupID + "/memberUsers";
        AndroidNetworking.initialize(currContext);
        ANRequest addMemberReq = AndroidNetworking.post(URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
                .addJSONObjectBody(jsonBody)
                .build();
        ANResponse<JSONArray> serverResponse = addMemberReq.executeForJSONArray();
        Log.d(TAG, "addGroupMember: JSON BODY INPUT BEFORE SEND " + jsonBody.toString());
        if (serverResponse.isSuccess()) {
            Log.d(TAG, "addGroupMember: Success?");
        } else {
            Log.d(TAG, "addGroupMember: Server error when adding: " + serverResponse.getError().getErrorBody());
            Log.d(TAG, "addGroupMember: More error info: " + serverResponse.getError().getResponse());
        }
    }

    // Removes a member from the indicated group.
    public void removeGroupMember(String currToken, int groupID, final int memberID, Context appContext) {
        AndroidNetworking.initialize(appContext);
        String URLPath = baseURL + "/groups/" + groupID + "/memberUsers/" + memberID;
        AndroidNetworking.initialize(appContext);
        ANRequest deleteMemberReq = AndroidNetworking.delete(URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
                .build();

        ANResponse<OkHttpResponseListener> serverResponse = deleteMemberReq.executeForOkHttpResponse();
        if (serverResponse.isSuccess()) {
            if (serverResponse.getOkHttpResponse().code() == 204) {
                Log.d(TAG, "removeGroupMember: Successful removal");
            }
        } else {
            Log.d(TAG, "removeGroupMember: Error with request " + serverResponse.getError());
        }
    }

    //Sets the last known location of the current logged in user. Used to update the last know location after a trip has completed
    public void setLastGpsLocation(String currToken, int currUserID, Location lastKnownLocation, Context currContext) {
        JSONObject gpsInfo = new JSONObject();
        try {
            gpsInfo.put("lat", lastKnownLocation.getLatitude());
            Log.d(TAG, "lat: " + lastKnownLocation.getLatitude());
            gpsInfo.put("lng", lastKnownLocation.getLongitude());
            Log.d(TAG, "lng: " + lastKnownLocation.getLongitude());

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CANADA);
            String currentTime = df.format(new Date());
            gpsInfo.put("timestamp", currentTime);
            Log.d(TAG, "timestamp: " + currentTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String URLPath = baseURL + "/users/" + currUserID + "/lastGpsLocation";
        AndroidNetworking.initialize(currContext);
        ANRequest setGPSReq = AndroidNetworking.post(URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
                .addJSONObjectBody(gpsInfo)
                .build();
        ANResponse<JSONObject> serverResponse = setGPSReq.executeForJSONObject();
        Log.d(TAG, "setLastGpsLocation: JSON BODY INPUT BEFORE SEND " + gpsInfo.toString());
        if (serverResponse.isSuccess()) {
            Log.d(TAG, "setLastGpsLocation: Success?");
        } else {
            Log.d(TAG, "setLastGpsLocation: Server error when adding: " + serverResponse.getError().getErrorDetail());
            Log.d(TAG, "setLastGpsLocation: Server error when adding: " + serverResponse.getError().getErrorBody());
            Log.d(TAG, "setLastGpsLocation: Server error code: " + serverResponse.getError().getErrorCode());
            Log.d(TAG, "setLastGpsLocation: More error info: " + serverResponse.getError().getResponse());
        }
    }

    // Retrieves the last known GPS location for the specified user
    public JSONObject getLastGpsLocation(String currToken, int UserID, Context currContext) {
        JSONObject gpsInfo = new JSONObject();

        String URLPath = baseURL + "/users/" + UserID + "/lastGpsLocation";
        AndroidNetworking.initialize(currContext);
        ANRequest getGPSReq = AndroidNetworking.get(URLPath)
                .addHeaders("apiKey", apiKey)
                .addHeaders("Authorization", currToken)
                .build();
        ANResponse<JSONObject> serverResponse = getGPSReq.executeForJSONObject();
        if (serverResponse.isSuccess()) {
            Log.d(TAG, "getLastGpsLocation: Success?");
            gpsInfo = serverResponse.getResult();
            Log.d(TAG, "getLastGpsLocation: response: " + gpsInfo);
        } else {
            Log.d(TAG, "getLastGpsLocation: Server error when getting: detail: " + serverResponse.getError().getErrorDetail());
            Log.d(TAG, "getLastGpsLocation: Server error when getting: " + serverResponse.getError().getErrorBody());
            Log.d(TAG, "getLastGpsLocation: Server error code: " + serverResponse.getError().getErrorCode());
            Log.d(TAG, "getLastGpsLocation: More error info: " + serverResponse.getError().getResponse());
        }
        return gpsInfo;
    }

    public boolean changeUserXP(String item, int xp, int id, String token, Context context) {

        JSONObject userObj = getUserInfoByID(id, token, context);
        int currXP = 0;
        int totalXP = 0;
        try {
            currXP = userObj.getInt("currentPoints");
            totalXP = userObj.getInt("totalPointsEarned");
        } catch (Exception e) {}

        // purchasing if xp is negative otherwise add xp points
        if(xp < 0 && item != null) {
            // check if user does not have enough points to purchase
            if(currXP + xp < 0) return false;
            currXP += xp;

            // add item to their list of possessions
            try {
                userObj.put("customJson", "{\"purchased\": " + item + "}");
            } catch (Exception e) {}
        } else {
            currXP += xp;
            totalXP += xp;
        }

        try {
            userObj.put("currentPoints", currXP);
            userObj.put("totalPointsEarned", totalXP);
        } catch (Exception e) {}

        return editDatabaseUserProfile(userObj, context, id, token);
    }

}

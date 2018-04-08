package ApplicationLogic;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.text.Layout;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.graphics.Point;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.google.android.gms.maps.model.LatLng;

import com.example.nurdan.lavaproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

// This class control the entire program and any interaction between UI and the application logic
// is done through here.
public class ProgramSingletonController {
    private String bearerToken;
    private int userID;
    private int currGroupID;
    private String currGroupName;
    private AccountApiInteractions currInstance;
    Boolean logInStatus = false;
    private static ProgramSingletonController instance;
    private List<LatLng> listOfPoints = new ArrayList<>();
    private User currLoggedInUser;
    private ArrayList<Integer> groupID = new ArrayList<>();
    private ArrayList<String>  groupNames = new ArrayList<>();
    private int currMemberID;


    private  ProgramSingletonController(){
        //Private constructor to prevent anybody instantiating the singleton class without using official method
    }

    //Static method to return the current instance of this singleton class or create one if it does not exist
    public static ProgramSingletonController getCurrInstance(){
        if(instance == null){
            instance = new ProgramSingletonController();
            return instance;
        }
        else{
            return instance;
        }
    }

    public int getCurrMemberID() {
        return currMemberID;
    }

    public void setCurrMemberID(int currMemberID) {
        this.currMemberID = currMemberID;
    }

    public void setUserID (int currUser) {
        this.userID = currUser;
    }

    public void setCurrGroupID (int groupID) {
        this.currGroupID = groupID;
    }

    public int getCurrGroupID () {
        return this.currGroupID;
    }

    public String getCurrGroupName() {
        return this.currGroupName;
    }

    public void setCurrGroupName(String currGroupName) {
        this.currGroupName = currGroupName;
    }

    public void setBearerToken(String token) {
        this.bearerToken = token;
    }

    public int getUserID(){
        return this.userID;
    }

    public String getBearerToken(){
        return this.bearerToken;
    }
    public User getCurrLoggedInUser(){
        return this.currLoggedInUser;
    }

    public User getLoggedInUserProfile(){
        return this.currLoggedInUser;
    }

    //Creates a new user
    public Boolean createNewUser(JSONObject jsonBody, Context appContext){
        currInstance = new AccountApiInteractions();
        return currInstance.createNewUser(jsonBody, appContext);
    }

    //Logs user into their account and creates a new user instance to hold their details.
    public Boolean logIn(String email, String password, Context appContext){
       JSONArray tempArr;
       this.currLoggedInUser = new User();
        currInstance = new AccountApiInteractions();
        logInStatus = currInstance.userLogIn(email, password, appContext);
        this.bearerToken = currInstance.getBearerToken();
        Log.d(TAG, "logIn: Program singleton bearer token " + this.bearerToken);
        currInstance.getDatabaseUserProfile(email, appContext);
        this.userID = currLoggedInUser.getID();
        Log.d(TAG, "logIn: UserIDTEST " + this.userID   );
        Log.d(TAG, "logIn: MONITORED BY TEST " +currLoggedInUser.getMonitorsOtherUsers());
        getGroupNames(appContext);
        //saveEmail(email, this.bearerToken, appContext);

        //save token in shared preferences
        SharedPreferences prefs = appContext.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("bearerToken", bearerToken)
                .putInt("userID", userID)
                .apply();

        return logInStatus;
    }

     //Simple method which discards bearer token to log user out.
    public void userLogout(){
        bearerToken = null;
    }

    //Sets the fields of the user class according to the information provided by the JsonObject. Used on logging in.
    public void setUserInfo(JSONObject newInformation){
        currLoggedInUser.setJsonObject(newInformation);
    }

    //Provides access to the JsonObject which contains all of the data of the current logged in user. Used for displaying it
    //and editing it.
    public JSONObject getUserInfo(){
        return currLoggedInUser.returnJsonUserInfo();
    }

    public JSONObject getUserInfoByID(Integer id, Context appContext) {
        AccountApiInteractions currInstance = new AccountApiInteractions();
        return currInstance.getUserInfoByID(id, this.bearerToken, appContext);
    }

    //Networking method which sends a post request to server to edit user info.
    public Boolean editUserInformation(JSONObject newInformation, Context currContext){
        currInstance = new AccountApiInteractions();
        Log.d(TAG, "editUserInformation: USER INFO JSON " + newInformation.toString());
        return currInstance.editDatabaseUserProfile(newInformation, currContext, userID, bearerToken);
    }

    // Edit monitoring users
    public Boolean editUserInformationById(JSONObject newInfo, Integer userMonitorID, Context currContext) {
        currInstance = new AccountApiInteractions();
        Log.d("EDITMONITOR", "" + userMonitorID + " JSON: " + newInfo.toString());
        return currInstance.editDatabaseUserProfile(newInfo, currContext, userMonitorID, this.bearerToken);
    }

    //Adds new user to be monitored by another
    public Boolean addUsrMonitor(String userEmail,Context appContext){
        Log.d(TAG, "addUsrMonitor: Beaerer Token " + this.bearerToken );
        Boolean successFlag;
        AccountApiInteractions getId = new AccountApiInteractions();
        UserMonitor currInstance = new UserMonitor();
        int tempUsrID = getId.getDatabaseUserID(userEmail, appContext, this.bearerToken);
        successFlag = currInstance.addMonitoredUser(userID, tempUsrID, this.bearerToken, appContext);
        return successFlag;
    }

    public Boolean addUsrMonitorYou(String userEmail, Context appContext){
        Log.d(TAG, "addUsrMonitorYou: Bearer Token " + this.bearerToken);
        Boolean successFlag;
        AccountApiInteractions getMonitorID = new AccountApiInteractions();
        UserMonitor currMonitorInstance = new UserMonitor();
        int tempMonitorID = getMonitorID.getDatabaseUserID(userEmail, appContext, this.bearerToken);
        successFlag = currMonitorInstance.addUsrToMonitorYou(userID, tempMonitorID, this.bearerToken, appContext);
        return successFlag;
    }
    // Delete a user from the list of monitored users
    public boolean deleteMonitoredUsr(int userToDelete, Context appContext){
        UserMonitor currInstance = new UserMonitor();
        return currInstance.stopMonitoringUser(this.userID, userToDelete, this.bearerToken, appContext);
    }

    // Delete a user from the list who are monitoring you
    public boolean deleteMonitoringMeUser(int userToDelete, Context appContext) {
        UserMonitor currInstance = new UserMonitor();
        return currInstance.stopMonitoringUser(userToDelete, this.userID, this.bearerToken, appContext);
    }


    // Method to get users who are monitored by the currently logged in user.
    public ArrayList<String> getUsersMonitored(Context appContext){
        Log.d(TAG, "getUsersMonitored: USERID" + this.userID);
        Log.d(TAG, "getUsersMonitored: TOKENBEARER " + this.bearerToken);
        JSONArray tempArr = null;
        UserMonitor currInstance = new UserMonitor();
        try{
            tempArr = currInstance.getMonitoredUsers(userID, bearerToken, appContext);
            Log.d(TAG, "getUsersMonitored temparr: " + tempArr);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(tempArr != null) {
            return createUserList(tempArr, appContext);
        }
        else return null;
    }


    // Method to get users who are monitored by the currently logged in user.
    public JSONArray getMonitoredUsersJSONArray(Context appContext){
        Log.d(TAG, "getMonitoredUsersJSONArray: USERID" + this.userID);
        Log.d(TAG, "getMonitoredUsersJSONArray: TOKENBEARER " + this.bearerToken);
        JSONArray tempArr = null;
        UserMonitor currInstance = new UserMonitor();
        try{
            tempArr = currInstance.getMonitoredUsers(userID, bearerToken, appContext);
            Log.d(TAG, "getMonitoredUsersJSONArray temparr: " + tempArr);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(tempArr != null) {
            return tempArr;
        }
        else return null;
    }


    public ArrayList<String> getUsersWhoMonitorThis(Context appContext){
        JSONArray tempArr = null;
        UserMonitor currInstance = new UserMonitor();
        try{
            tempArr = currInstance.getUsersWhoMonitor(userID, bearerToken, appContext);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (tempArr != null){
            return createUserList(tempArr, appContext);
        }
        else return null;
    }

    public ArrayList<Integer> getIDsWhoMonitorThis(Context appContext){
        JSONArray tempArr = null;
        UserMonitor currInstance = new UserMonitor();
        try{
            tempArr = currInstance.getUsersWhoMonitor(userID, bearerToken, appContext);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if (tempArr != null){
            return createUserListID(tempArr, appContext);
        }
        else return null;
    }

    // Get ids of monitoring me
    public ArrayList<Integer> getUsersMonitoredIDs(Context appContext){
        JSONArray tempArr = null;
        UserMonitor currInstance = new UserMonitor();
        try{
            tempArr = currInstance.getMonitoredUsers(userID, bearerToken, appContext);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(tempArr != null) {
            return createUserListID(tempArr, appContext);
        }
        else return null;
    }

    private ArrayList<String> createUserList(JSONArray jsonArr, Context appContext){
        JSONObject tempJSONObject;
        ArrayList<String> tempUserStorage = new ArrayList<>();
        ArrayAdapter<String> tempArrAdapter;
       for(int i = 0; i < jsonArr.length(); i++){
           try {
               tempJSONObject = jsonArr.getJSONObject(i);
               tempUserStorage.add(tempJSONObject.getString("name"));
           }
           catch (Exception e){
               e.printStackTrace();
           }
       }
        return tempUserStorage;
    }

    private ArrayList<Integer> createUserListID(JSONArray jsonArr, Context appContext){
        JSONObject tempJSONObject;
        ArrayList<Integer> tempUserStorage = new ArrayList<>();
        ArrayAdapter<Integer> tempArrAdapter;
        for(int i = 0; i < jsonArr.length(); i++){
            try {
                tempJSONObject = jsonArr.getJSONObject(i);
                tempUserStorage.add(tempJSONObject.getInt("id"));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return tempUserStorage;
    }


    public void getGroupNames(Context currContext){
        ArrayList<String> temp = new ArrayList<>();
        JSONArray tempGroupID;
        AccountApiInteractions currInstance = new AccountApiInteractions();
        try{
            JSONObject tempUserProfile = getUserInfo();
            tempGroupID = tempUserProfile.getJSONArray("leadsGroups");
            if(tempGroupID != null && tempGroupID.length() != 0) {
                for (int i = 0; i < tempGroupID.length(); i++) {
                    JSONObject tempJson = tempGroupID.getJSONObject(i);
                    int tempID = tempJson.getInt("id");
                    groupID.add(tempID);
                    groupNames.add(currInstance.getGroupName(this.bearerToken, currContext, tempID));
                }
            }
            else {
                groupNames.add("You are not the leader of any groups!");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList<String> getGroupNamesList (){
        return groupNames;
    }
    public ArrayList<Integer> getGroupIDList(){
        return this.groupID;
    }


    public JSONObject getUserByID(int ID, Context context){
        currInstance = new AccountApiInteractions();
        JSONObject currUserInfo = currInstance.getUserByID(this.bearerToken, ID, context);
        Log.d(TAG, "getUserByID: currUserInfo: " + currUserInfo);
        return currUserInfo;
    }

    /* group functions */
    /* need to implement/test */

    //input latlng
    public void inputLatLng(LatLng point, Context context) {
        if (listOfPoints.size() == 2){
            listOfPoints.clear();
        }
        listOfPoints.add(point);
    }

    //get group starting latlng
    public List<LatLng> returnLatLng(Context context) {
        return listOfPoints;
    }

        //create new group
    public void createNewGroup(String groupDescription, LatLng start, LatLng dest, Context appContext){
        currInstance = new AccountApiInteractions();
        Log.d(TAG, "createNewGroup: USERID: " + this.userID);
        Log.d(TAG, "createNewGroup: TOKENBEARER: " + this.bearerToken);
        String currToken = this.bearerToken;
        int leaderID = this.userID;
        currInstance.createNewGroup(currToken, groupDescription, leaderID, start, dest, appContext);
    }

    //get group info
    public JSONObject getGroupDetails(int groupID, Context appContext){
        String currToken = this.bearerToken;
        currInstance = new AccountApiInteractions();
        return currInstance.getGroupDetails(currToken, groupID, appContext);
    }

    //get list of all groups
    public JSONArray getGroupList(Context appContext){
        currInstance = new AccountApiInteractions();
        String currToken = this.bearerToken;
        JSONArray list = null;
        try{
            list = currInstance.getGroupList(currToken, appContext);
            Log.d(TAG, "getGroupList: " + list);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(list != null){
            return list;
        }
        else {
            return null;
        }
    }

    //update existing group info
    public void updateGroup(int groupID, String newDescription, JSONArray latitude, JSONArray longitude, Context appContext){
        currInstance = new AccountApiInteractions();
        if (newDescription == null) {
            newDescription = "No Name";
        }
        if (latitude.length() == 0) {
            latitude.put(0);
        }
        if (longitude.length() == 0) {
            longitude.put(0);
        }
        currInstance.updateGroup(this.bearerToken, groupID, this.userID, newDescription, latitude, longitude, appContext);
    }

    // delete group
    public void deleteGroup(int groupID, Context appContext) {
        currInstance = new AccountApiInteractions();
        currInstance.deleteGroup(this.bearerToken, groupID, appContext);
    }

    // get group members
    public JSONArray getGroupMembers(int groupID, Context appContext){
        currInstance = new AccountApiInteractions();
        return currInstance.getGroupMembers(this.bearerToken, groupID, appContext);
    }

    // add group member
    public void addGroupMember(int groupID, Context appContext) {
        currInstance = new AccountApiInteractions();
        currInstance.addGroupMember(this.bearerToken, groupID, this.userID, appContext);
    }

    // remove group member
    // todo: implement support for removing monitored users
    public void removeGroupMember(int groupID, Context appContext) {
        currInstance = new AccountApiInteractions();
        currInstance.removeGroupMember(this.bearerToken, groupID, this.userID, appContext);
    }


    /* These methods are related to messaging */
    public JSONArray getMessagesForUser(Context currContext){
        UserMessagingService currInstance = new UserMessagingService();
        Log.d(TAG, "getMessagesForUser: Inputs " + userID + " " + bearerToken);
        return currInstance.getMessagesForSingleUser(userID, bearerToken, currContext);
    }

    public JSONObject getMessageObjById(int msgID, Context currContext) {
        UserMessagingService currInstance = new UserMessagingService();
        return currInstance.getMessageById(msgID, this.userID, this.bearerToken, currContext);
    }


    public void sendMsgToGroup(String message, int groupID, Boolean emergencyStatus, Context currContext){
        UserMessagingService currInstance = new UserMessagingService();
        currInstance.newMessageToGroup(message, groupID, emergencyStatus, bearerToken, currContext);
    }

    public void sendMsgToParents(String message, Boolean emergencyStatus, Context currContext){
        UserMessagingService currInstance = new UserMessagingService();
        currInstance.newMessageToParents(message, userID, emergencyStatus, bearerToken, currContext);
    }
    public void setLastGpsLocation(Location lastKnown, Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.setLastGpsLocation(this.bearerToken, this.userID, lastKnown, appContext);
    }

    public JSONObject getLastGpsLocation(int UserID, Context appContext) {
        currInstance = new AccountApiInteractions();
        return currInstance.getLastGpsLocation(this.bearerToken, UserID, appContext);
    }

     /*------------------Everything related to user permissions ------------------------- */
     //Grab denied requests
    public JSONArray getDeniedRequests(){
        UserPermissions currInstance = new UserPermissions();
        return currInstance.getDeniedRequests(userID, bearerToken);
    }

    //Grab accepted requests
    public JSONArray getAcceptedRequests(){
        UserPermissions currInstance = new UserPermissions();
        return currInstance.getAcceptedRequests(userID, bearerToken);
    }

    //Grab the message associated with a requests(its contents)
    public String getPermMessage(int messageId){
        UserPermissions currInstance = new UserPermissions();
        return currInstance.getRequestMessage(messageId, bearerToken);
    }
    //Respond to a certain request identified by its id.
    public void respondToRequest(int permId, Boolean choice){
        UserPermissions currInstance = new UserPermissions();
        currInstance.respondToRequest(permId, bearerToken, choice);
    }
}

package ApplicationLogic;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.text.Layout;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.graphics.Point;
import com.google.android.gms.maps.model.LatLng;
import com.example.nurdan.lavaproject.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * This is the singleton class which is central to the applictaion. It contains many fields which are used
 * across the app as well as a front for all of the methods implemented in the other feature specific classes.
 */
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

    //Returns the id of the currently logged in user
    public int getUserID(){
        return this.userID;
    }

    /**
     * Sets up the currently logged in user after they close the application and open it again.
     * @param userID        The user ID recovered from shared preferences
     * @param bearerToken   Bearer token recovered from shared preferences
     */
    public void setUpLoggedInUser(int userID, String bearerToken){
        this.userID = userID;
        this.bearerToken = bearerToken;
        //Create a new user instance to store the user profile in
        currLoggedInUser = new User();
    }

    /**
     * Creates a new user based on the provided information
     * @param jsonBody          Contains the infomration in JSON format to be sent to the server
     * @param appContext        Context for networking library
     * @return                  Boolean status indicatin success or failure
     */
    public Boolean createNewUser(JSONObject jsonBody, Context appContext){
        currInstance = new AccountApiInteractions();
        return currInstance.createNewUser(jsonBody, appContext);
    }

    /**
     * Logs a user into their account and saves their settings into shared preferences so the app can be closed
     * and opened again
     * @param email             user email
     * @param password          user password
     * @param appContext        context for library
     * @return                  boolean status to indicate success/failure
     */
    public Boolean logIn(String email, String password, Context appContext){
       JSONArray tempArr;
       this.currLoggedInUser = new User();
        currInstance = new AccountApiInteractions();
        logInStatus = currInstance.userLogIn(email, password, appContext);
        //If the login is successful then the user information is loaded in.
        if (logInStatus) {
            this.bearerToken = currInstance.getBearerToken();
            Log.d(TAG, "logIn: Program singleton bearer token " + this.bearerToken);

            //Retrieve the profile of the user loggin in
            currInstance.getDatabaseUserProfile(email, appContext);
            this.userID = currLoggedInUser.getID();
            Log.d(TAG, "logIn: UserIDTEST " + this.userID);

            // Grab the group names of every group that this person is a part of
            getGroupNames(appContext);

            //save token in shared preferences, used to keep the user logged in.
            SharedPreferences prefs = appContext.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
            prefs.edit()
                    .putString("bearerToken", bearerToken)
                    .putInt("userID", userID)
                    .apply();
        }
        return logInStatus;
    }

    public void savePurchasedItemsToPrefs(Context appContext) {
        String purchasedString = "";
        JSONObject purchasedItems = new JSONObject();
        try {
             purchasedString = this.getUserInfo().getString("customJson");
        } catch (Exception e) {}

        if(purchasedString.toLowerCase().contains(("Dark Blue Theme").toLowerCase())) {
            SharedPreferences prefs = appContext.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
            prefs.edit()
                    .putString("currentTheme", "Dark Blue Theme")
                    .apply();
        }
    }

    //Discard bearer token on user logout.
    public void userLogout(){
        bearerToken = null;
    }

    //Sets the fields of the user class according to the information provided by the JsonObject. Used when logging in.
    public void setUserInfo(JSONObject newInformation){
        currLoggedInUser.setJsonObject(newInformation);
    }

    //Provides access to the JsonObject which contains all of the data of the current logged in user. Used for displaying it
    //and editing it.
    public JSONObject getUserInfo(){
        return currLoggedInUser.returnJsonUserInfo();
    }

    /**
     * Retrieves the user information by their id.
     * @param id                User id
     * @param appContext        Context for library
     * @return                  Json object containing the information of the user
     */
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
    //Adds a user to monitor you
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


    /*Method used to retrieve and format a list of users who the logged in user is monitoring. Uses the logged in user ID to retrieve the list

     */
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
    //Method used to iterate through a Json Array which contains Json Objects as each entry. Retrieves the value associated with the "name" key
    //and returns an array list containing all of the names.
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
    //Method used to iterate through a JSON Array which contains a JSON object as each entry. Retrieves the "id" key from each object and
    //creates then returns an array list containing them. Used when deleting users from are monitoring you/you are monitoring
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

    //Retrieves the group names of all groups for which the logged in user is the leader of.
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
                groupNames.add(R.string.notLeading + "");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    //Retruns the array list containing the group names for which the current user is a leader of.
    public ArrayList<String> getGroupNamesList (){
        return groupNames;
    }
    //Returns the array list containg the ID's of all groups for which the current user is a leader of.
    public ArrayList<Integer> getGroupIDList(){
        return this.groupID;
    }

    //retrieves jsonarray containing all users
    public JSONArray getAllUsers(Context context) {
        currInstance = new AccountApiInteractions();
        JSONArray userList = currInstance.getAllUsers(this.bearerToken, context);
        Log.d(TAG, "getAllUsers: userList: " + userList);
        return userList;
    }

    //Retreives the information for a user by their ID
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

    //Create a new group based on name, starting point and their destination
    public void createNewGroup(String groupDescription, LatLng start, LatLng dest, Context appContext){
        currInstance = new AccountApiInteractions();
        Log.d(TAG, "createNewGroup: USERID: " + this.userID);
        Log.d(TAG, "createNewGroup: TOKENBEARER: " + this.bearerToken);
        String currToken = this.bearerToken;
        int leaderID = this.userID;
        currInstance.createNewGroup(currToken, groupDescription, leaderID, start, dest, appContext);
    }

    //Get the info for the specified group
    public JSONObject getGroupDetails(int groupID, Context appContext){
        String currToken = this.bearerToken;
        currInstance = new AccountApiInteractions();
        return currInstance.getGroupDetails(currToken, groupID, appContext);
    }

    //get list of all groups, used for testing purposes
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
        currInstance.updateGroup(this.bearerToken, groupID, this.userID, newDescription, latitude, longitude, appContext, false);
    }

    //Overriden method which is used to set a new leader for a group and generate a permission for it.
    public void updateGroup(int newLeaderId,int groupID, String newDescription, JSONArray latitude, JSONArray longitude, Context appContext){
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
        currInstance.updateGroup(this.bearerToken, groupID, newLeaderId, newDescription, latitude, longitude, appContext, true);
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


    /* --------------------------- These methods are related to messaging --------------------------------*/
    public JSONArray getMessagesForUser(Context currContext){
        UserMessagingService currInstance = new UserMessagingService();
        Log.d(TAG, "getMessagesForUser: Inputs " + userID + " " + bearerToken);
        return currInstance.getMessagesForSingleUser(userID, bearerToken, currContext);
    }

    public JSONObject getMessageObjById(int msgID, Context currContext) {
        UserMessagingService currInstance = new UserMessagingService();
        return currInstance.getMessageById(msgID, this.userID, this.bearerToken, currContext);
    }

    public void setUserMessageRead(Boolean isRead, int msgId, Context currContext) {
        Log.d(TAG, "MessageChange: setting to " + isRead + " " + msgId);
        UserMessagingService currInstance = new UserMessagingService();
        currInstance.setMessageRead(isRead, msgId, this.userID, this.bearerToken, currContext);
    }

    public boolean rewardXP(Context context) {
        AccountApiInteractions account = new AccountApiInteractions();
        return account.changeUserXP("", 1, this.userID, this.bearerToken, context);
    }

    public boolean purchaseWithXP(String item, int costXP, Context context) {
        AccountApiInteractions account = new AccountApiInteractions();
        return account.changeUserXP(item, (-1)*costXP, this.userID, this.bearerToken, context);
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

    /**
     * Retreive last gps location
     * @param UserID        Logged in user id
     * @param appContext    Context
     * @return              Json object containing the gps location
     */
    public JSONObject getLastGpsLocation(int UserID, Context appContext) {
        currInstance = new AccountApiInteractions();
        return currInstance.getLastGpsLocation(this.bearerToken, UserID, appContext);
    }

     /*------------------Everything related to user permissions ------------------------- */

    /**
     * Grab all denied requests
     * @return  Json array containing all denied requests
     */
    public JSONArray getDeniedRequests(){
        UserPermissions currInstance = new UserPermissions();
        return currInstance.getDeniedRequests(userID, bearerToken);
    }

    /**
     * Retrieve all accepted permissions
     * @return  Json array containing the permissions
     */
    public JSONArray getAcceptedRequests(){
        UserPermissions currInstance = new UserPermissions();
        return currInstance.getAcceptedRequests(userID, bearerToken);
    }

    /**
     * Retrieves all pending requests for the logged in user. This could be done from the user profile retreived
     * from the server during login but if we retrieve them this way, their display is easier to update.
     * @return  Json array containing the pending permissions
     */
    public JSONArray getPendingRequests(){
        UserPermissions currInstance = new UserPermissions();
        return currInstance.getPendingRequests(userID, bearerToken);
    }

    /**
     * Grab the message associated with a request
     * @param messageId         id of request
     * @return                  The message in string format
     */
    public String getPermMessage(int messageId){
        UserPermissions currInstance = new UserPermissions();
        return currInstance.getRequestMessage(messageId, bearerToken);
    }

    /**
     * Respond to a certain permission with the provided status
     * @param permId    Id of permission
     * @param choice    Response status. false for deny, true for accept
     */
    public void respondToRequest(int permId, Boolean choice){
        UserPermissions currInstance = new UserPermissions();
        currInstance.respondToRequest(permId, bearerToken, choice);
    }

}

package ApplicationLogic;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;

import com.example.nurdan.lavaproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

// This class control the entire program and any interaction between UI and the application logic
// is done through here.
public class ProgramSingletonController {
    private String bearerToken;
    private int userID;
    private String userEmail;
    private JSONObject jsonResponse;
    private AccountApiInteractions currInstance;
    Boolean logInStatus = false;
    private static ProgramSingletonController instance;
    private List<LatLng> listOfPoints = new ArrayList<>();
    private User currLoggedInUser;


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

    public int getUserID(){
        currInstance = new AccountApiInteractions();
        return currInstance.getUserID();
    }

    public String getBearerToken(){
        currInstance = new AccountApiInteractions();
        return currInstance.getBearerToken();
    }
    public User getCurrLoggedInUser(){
        return this.currLoggedInUser;
    }

    //used for test, can delete
    /*
    //saves curr email, bearer toekn
    private void saveEmail(String email, String token, Context context){
        userEmail = email;
        bearerToken = token;
    }

    private String getEmail(Context context){
        return userEmail;
    }


    public int getCurrUserID(Context appContext){
        currInstance = new AccountApiInteractions();
        userEmail = getEmail(appContext);
        int tempUserID = currInstance.getDatabaseUserID(userEmail, appContext);
        return tempUserID;
    }

    */

    public User getLoggedInUserProfile(){
        return this.currLoggedInUser;
    }

    //Creates a new user
    public Boolean createNewUser(JSONObject jsonBody, Context appContext){
        currInstance = new AccountApiInteractions();
        return currInstance.createNewUser(jsonBody, appContext);
    }


    public void setUserInfo(JSONObject newInformation){
        currLoggedInUser.setJsonObject(newInformation);
    }

    public JSONObject getUserInfo(){
        return currLoggedInUser.returnJsonUserInfo();
    }

    //Logs user into their account
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
        //saveEmail(email, this.bearerToken, appContext);
        return logInStatus;
    }
    //Adds new user to be monitored by another
    public void addUsrMonitor(int monitorID, String userEmail, String bearerToken, Context appContext){
        AccountApiInteractions getId = new AccountApiInteractions();
        UserMonitor currInstance = new UserMonitor();
        int tempUsrID = getId.getDatabaseUserID(userEmail, appContext);
        currInstance.addMonitoredUser(monitorID, tempUsrID, bearerToken, appContext);
    }
    //Deletes a user from the list of monitored users
    public void deleteMonitoredUsr(int monitorID, int dltdUser, Context appContext){
        UserMonitor currInstance = new UserMonitor();
        currInstance.stopMonitoringUser(monitorID, dltdUser, bearerToken, appContext);
    }


    // Method to get users who are monitored by the currently logged in user.
    public ArrayList<String> getUsersMonitored(Context appContext){
        Log.d(TAG, "getUsersMonitored: USERID" + this.userID);
        Log.d(TAG, "getUsersMonitored: TOKENBEARER " + this.bearerToken);
         JSONArray tempArr = null;
        UserMonitor currInstance = new UserMonitor();
        try{
           tempArr = currInstance.getMonitoredUsers(userID, bearerToken, appContext);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(tempArr != null) {
            return createUserList(tempArr, appContext);
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
    public void createNewGroup(String groupDescription, int leaderID, LatLng start, LatLng dest, Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.createNewGroup(groupDescription, leaderID, start, dest, appContext);
        bearerToken = currInstance.getBearerToken();
    }

    //get group info
    public void getGroupDetails(int groupID, Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.getGroupDetails(groupID, appContext);
        bearerToken = currInstance.getBearerToken();
    }

    //get list of all groups
    public JSONArray getGroupList(Context appContext){
        currInstance = new AccountApiInteractions();
        bearerToken = currInstance.getBearerToken();
        return currInstance.getGroupList(appContext);
    }

    //update existing group info
    public void updateGroup(int groupID, final String newDescription, final double latitude, final double longitude, Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.updateGroup(groupID, newDescription, latitude, longitude, appContext);
        bearerToken = currInstance.getBearerToken();
    }

    // delete group
    public void deleteGroup(int groupID, Context appContext) {
        currInstance = new AccountApiInteractions();
        currInstance.deleteGroup(groupID, appContext);
        bearerToken = currInstance.getBearerToken();
    }

    // get group members
    public void getGroupMembers(int groupID, Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.getGroupMembers(groupID, appContext);
        bearerToken = currInstance.getBearerToken();
    }

    // add group member
    public void addGroupMember(int groupID, final int memberID, Context appContext) {
        currInstance = new AccountApiInteractions();
        currInstance.addGroupMember(groupID, memberID, appContext);
        bearerToken = currInstance.getBearerToken();
    }

    // remove group member
    public void removeGroupMember(int groupID, final int memberID, Context appContext) {
        currInstance = new AccountApiInteractions();
        currInstance.removeGroupMember(groupID, memberID, appContext);
        bearerToken = currInstance.getBearerToken();
    }

     //Simple method which discards bearer token to log user out.
    public void userLogout(){
        bearerToken = null;
    }
}

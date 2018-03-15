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

import static android.content.ContentValues.TAG;

// This class control the entire program and any interaction between UI and the application logic
// is done through here.
public class ProgramSingletonController {
    private String bearerToken;
    private int userID;
    private JSONObject jsonResponse;
    private AccountApiInteractions currInstance;
    Boolean logInStatus = false;
    private static ProgramSingletonController instance;

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


    //Creates a new user
    public void createNewUser(String name, String email, String password, Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.createNewUser(name, password, email, appContext);
    }

    //Logs user into their account
    public Boolean logIn(String email, String password, Context appContext){
       JSONArray tempArr;
        currInstance = new AccountApiInteractions();
        logInStatus = currInstance.userLogIn(email, password, appContext);
        this.bearerToken = currInstance.getBearerToken();
        Log.d(TAG, "logIn: Programsingletonberer " + this.bearerToken);
        this.userID = currInstance.getDatabaseUserID(email, appContext);
        Log.d(TAG, "logIn: UserIDTEST " + this.userID   );
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
    public void deleteMonitoredUsr(int monitorID, int dltdUser, String bearerToken, Context appContext){
        UserMonitor currInstance = new UserMonitor();
        currInstance.stopMonitoringUser(monitorID, dltdUser, bearerToken, appContext);
    }

    public int getUserID(){
        currInstance = new AccountApiInteractions();
        return currInstance.getUserID();
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
        currInstance = new AccountApiInteractions();
        currInstance.inputLatLng(point, context);
    }

    //get group starting latlng
    public LatLng returnLatLng(Context context) {
        currInstance = new AccountApiInteractions();
        return currInstance.returnLatLng(context);
    }

        //create new group
    public void createNewGroup(String groupDescription, int leaderID, LatLng point, Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.createNewGroup(groupDescription, leaderID, point, appContext);
        bearerToken = currInstance.getBearerToken();
    }

    //get group info
    public void getGroupDetails(int groupID, Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.getGroupDetails(groupID, appContext);
        bearerToken = currInstance.getBearerToken();
    }

    //get list of all groups
    public void getGroupList(Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.getGroupList(appContext);
        bearerToken = currInstance.getBearerToken();
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
}

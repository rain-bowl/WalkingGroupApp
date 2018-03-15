package ApplicationLogic;

import android.content.Context;
import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

// This class control the entire program and any interaction between UI and the application logic
// is done through here.
public class ProgramSingletonController {
    private String bearerToken;
    private int userID;
    private JSONObject jsonResponse;
    private AccountApiInteractions currInstance;
    private static ProgramSingletonController instance;

    private  ProgramSingletonController(){
        //Private instance to prevent anybody instantiating the singleton class without using official method
    }

    public static ProgramSingletonController getCurrInstance(){
        if(instance == null){
            return new ProgramSingletonController();
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
    public void logIn(String email, String password, Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.userLogIn(email, password, appContext);
        bearerToken = currInstance.getBearerToken();
        userID = currInstance.getDatabaseUserID(email, appContext);
    }

    public int getUserID(){
        currInstance = new AccountApiInteractions();
        return currInstance.getUserID();
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

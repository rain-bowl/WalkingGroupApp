package ApplicationLogic;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

// This class control the entire program and any interaction between UI and the application logic
// is done through here.
public class ProgramSingletonController {
    private String bearerToken;
    private int userID;
    private JSONObject jsonResponse;
    private AccountApiInteractions currInstance;
    private static ProgramSingletonController instance;

    private  ProgramSingletonController(){
        //Private constructor to prevent anybody instantiating the singleton class without using official method
    }

    //Static method to return the current instance of this singleton class or create one if it does not exist
    public static ProgramSingletonController getCurrInstantce(){
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
        Log.d(TAG, "logIn: Programsingletonberer " + bearerToken);
        userID = currInstance.getDatabaseUserID(email, appContext);
        Log.d(TAG, "logIn: UserIDTEST" + userID);
       // UserMonitor newinstance = new UserMonitor();
        /*try{
            Log.d(TAG, "logIn: Userid test " + userID);
            JSONArray arr = newinstance.getMonitoredUsers(userID, bearerToken, appContext);
            Log.d(TAG, "logIn: Getting users who are being monitored by this user" + arr.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
*/


    }
    //Adds new user to be monitored by another
    public void addUsrMonitor(int monitorID, int usrToBeMonitoredID, String bearerToken, Context appContext){
        UserMonitor currInstance = new UserMonitor();
        currInstance.addMonitoredUser(monitorID, usrToBeMonitoredID, bearerToken, appContext);
    }
    //Deletes a user from the list of monitored users
    public void deleteMonitoredUsr(int monitorID, int dltdUser, String bearerToken, Context appContext){
        UserMonitor currInstance = new UserMonitor();
        currInstance.stopMonitoringUser(monitorID, dltdUser, bearerToken, appContext);
    }

}

package ApplicationLogic;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.widget.ArrayAdapter;

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
    public static ProgramSingletonController getCurrInstantce(){
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
    public void addUsrMonitor(int monitorID, int usrToBeMonitoredID, String bearerToken, Context appContext){
        UserMonitor currInstance = new UserMonitor();
        currInstance.addMonitoredUser(monitorID, usrToBeMonitoredID, bearerToken, appContext);
    }
    //Deletes a user from the list of monitored users
    public void deleteMonitoredUsr(int monitorID, int dltdUser, String bearerToken, Context appContext){
        UserMonitor currInstance = new UserMonitor();
        currInstance.stopMonitoringUser(monitorID, dltdUser, bearerToken, appContext);
    }


    // Method to get users who are monitored by the currently logged in user.
    public ArrayAdapter<String> getUsersMonitored(Context appContext){
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


    public ArrayAdapter<String> getUsersWhoMonitorThis(Context appContext){
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


    private ArrayAdapter<String> createUserList(JSONArray jsonArr, Context appContext){
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
       tempArrAdapter = new ArrayAdapter<String>(appContext, android.R.layout.activity_list_item,tempUserStorage);
       return tempArrAdapter;

    }
}

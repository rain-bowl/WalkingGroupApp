package ApplicationLogic;

import android.content.Context;

import org.json.JSONObject;

// This class control the entire program and any interaction between UI and the application logic
// is done through here.
public class ProgramSingletonController {
    private String bearerToken;
    private String userID;
    private JSONObject jsonResponse;
    private AccountApiInteractions currInstance;
    private static ProgramSingletonController instance;

    private  ProgramSingletonController(){
        //Private instance to prevent anybody instantiating the singleton class without using official method
    }

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
    }


}

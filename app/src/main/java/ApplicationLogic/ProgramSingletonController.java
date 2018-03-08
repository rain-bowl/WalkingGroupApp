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



    public void createNewUser(String name, String email, String password, Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.createNewUser(name, password, email, appContext);
    }
    public void logIn(String email, String password, Context appContext){
        currInstance = new AccountApiInteractions();
        currInstance.userLogIn(email, password, appContext);
        bearerToken = currInstance.getBearerToken();
    }


}

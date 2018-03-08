package ApplicationLogic;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;

import org.json.JSONObject;

//This class deals with all aspects of user monitoring such as adding users as well as
//fetching the lists of users.
public class MonitoringApiInteractions {
    private final String baseURL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";

    public void monitorsUsers(String userID, Context appContext){
        AndroidNetworking.initialize(appContext);
        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("id", userID);
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }
}

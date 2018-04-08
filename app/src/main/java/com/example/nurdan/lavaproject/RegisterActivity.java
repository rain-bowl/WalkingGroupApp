package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import ApplicationLogic.ProgramSingletonController;
import UIFragmentClasses.MandatoryRegisterInformationFragment;
/*This class is responsible for handling the logic when registering a new user

 */
public class RegisterActivity extends AppCompatActivity {
    private ProgramSingletonController localInstance;
    JSONObject serverCallBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        serverCallBody = new JSONObject();
        replaceFragment(new MandatoryRegisterInformationFragment());
    }

    public void replaceFragment(Fragment fragmentClass){
        FragmentTransaction fTInstance = getSupportFragmentManager().beginTransaction();
        fTInstance.replace(R.id.fragmentContainer, fragmentClass);
        fTInstance.commit();
}
//Adds the provided key and provided object(either string or int) to the JsonObject which is used in creating a new user.
    public void addJson(String key, String content){
            try{
                if(content != null){
                    serverCallBody.put(key, content);
                 }
                else {
                    serverCallBody.put(key, "Not provided");
                 }
                Log.d("Tag", "addJson: Show jsonObject contents " + serverCallBody.toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
    }

//Overridden method to handle cases of input being an integer.
    public void addJson(String key, int content){
            try{
                    serverCallBody.put(key, content);
                Log.d("Tag", "addJson: Show jsonObject contents " + serverCallBody.toString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
    }

    public void executeRegisterAction(){
        asyncRunner instance = new asyncRunner();
        instance.execute();
    }


    //Static intent method to access activity
   public static Intent registerActIntent(Context actContext){
       return new Intent(actContext, RegisterActivity.class);
    }

    //Private async class to execute the network call to send the information for the new user.
    private class asyncRunner extends AsyncTask<Void,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean successFlag;
            localInstance = ProgramSingletonController.getCurrInstance();
            successFlag = localInstance.createNewUser(serverCallBody, getApplicationContext());
            if(successFlag){
                Intent successRegistration = LoginActivity.loginActIntent(getApplicationContext());
                startActivity(successRegistration);
            }
            return true;
        }
    }
}

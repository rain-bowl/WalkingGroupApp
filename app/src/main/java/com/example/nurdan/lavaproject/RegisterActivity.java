package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;

import ApplicationLogic.RestApiInteractions;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    }


    //Code needs to run in an async thread. The two cannot be mixed!
    //I'm calling some simple Log commands to debug.
    //Everything is "hardwired" at this point but its just for testing purposes. I will
    //Change it up when i can. If you want to read up on what I have done, try looking up
    //some asyncTask examples/documentation.
    private class runASYNC extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... voids) {
           RestApiInteractions test = new RestApiInteractions();
           try{
               String resp = test.createNewUser("testMan","1234","testManfasf@gmail.com");
                           Log.d("POSTEXEC", "onPostExecuteRET: " + resp);
               return resp;
           }
           catch (Exception e){
                           Log.d("POSTEXEC", "onPostExecute: " + e);
               return e.toString();
           }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("POSTEXEC", "onPostExecute: " + s);
        }
    }

    //Static intent method to access activity
   public static Intent registerActIntent(Context actContext){
       return new Intent(actContext, RegisterActivity.class);

    }


}

package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import ApplicationLogic.RestApiInteractions;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        new runASYNC().execute();

    }
    private class runASYNC extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... voids) {
           RestApiInteractions test = new RestApiInteractions();
           try{
               String resp = test.createNewUser("testMan","1234","testMan@gmail.com");
                           Log.d("POSTEXEC", "onPostExecute: " + resp);
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
   public static Intent registerActIntent(Context actContext){
       return new Intent(actContext, RegisterActivity.class);

    }


}

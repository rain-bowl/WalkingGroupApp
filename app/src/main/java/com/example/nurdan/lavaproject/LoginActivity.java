package com.example.nurdan.lavaproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ApplicationLogic.AccountApiInteractions;
import ApplicationLogic.ProgramSingletonController;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity {
    ProgressBar loginProgress;
    private ProgramSingletonController localInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginProgress = findViewById(R.id.loginProgressBar);
        loginProgress.setVisibility(GONE);

       // checkIfLoggedIn();

        createLogInBtns();
    }

    public void checkIfLoggedIn() {
        localInstance = ProgramSingletonController.getCurrInstance();
        // skip login in case user already signed in
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("bearerToken", "");
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        Integer userID = prefs.getInt("userID", -1);

        Log.d("UserSkipLogin", "id: " + userID + ", token: " + token);
        if(isLoggedIn && token.length() != 0 && userID != -1) {
            localInstance.setBearerToken(token);
            localInstance.setUserID(userID);
            startNextActivity();
        }
    }
    public void startNextActivity() {
        Intent intent = new Intent(LoginActivity.this, MainMenu.class);// New activity
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish(); // Kill login activity once you redirect to another activity
    }

    public void createLogInBtns(){
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.regButton);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgress.setVisibility(View.VISIBLE);
                async test = new async();
                test.execute();


            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = RegisterActivity.registerActIntent(getApplicationContext());
                startActivity(register);
            }
        });
    }
    public static Intent loginActIntent(Context actContext){
        return new Intent(actContext, LoginActivity.class);
    }

    private class async extends AsyncTask<Void,Void,Void>{
        Boolean successFlag;
            @Override
            protected Void doInBackground(Void... voids) {
                final EditText usernameText = findViewById(R.id.usernameText);
                final EditText passText = findViewById(R.id.passText);
                localInstance = ProgramSingletonController.getCurrInstance();
                String user = usernameText.getText().toString();
                String pass = passText.getText().toString();
                successFlag = localInstance.logIn(user,pass, getApplicationContext());
                Log.d("AsyncLogIn", "doInBackground: SuccessFlag " + successFlag);
                pass = "";
                user = "";
                if(successFlag){
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
                    prefs.edit().putBoolean("isLoggedIn", true).apply();
                    String t = prefs.getString("bearerToken", "");
                    Log.d("AsyncLogin", "onPostExecute: " + t);
                }
                else{
                    Log.d("UIERROR", "doInBackground: failed login from ui");
                }
               return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                toggleLoadSpinner();
                if(successFlag)
                    startNextActivity();
            }
    }
    private void toggleLoadSpinner() {
        if(loginProgress.getVisibility() == View.GONE)
            loginProgress.setVisibility(View.VISIBLE);
        else
            loginProgress.setVisibility(View.GONE);
    }
}

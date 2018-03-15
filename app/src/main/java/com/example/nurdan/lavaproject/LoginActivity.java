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

public class LoginActivity extends AppCompatActivity {
    ProgressBar loginProgress;
    private ProgramSingletonController localInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginProgress = findViewById(R.id.loginProgressBar);
        loginProgress.setVisibility(View.GONE);
        createLogInBtns();

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
                Log.d("AsyncLogIn", "doInBackground: SuccessFlag" + successFlag);
                pass = "";
                user = "";
                if(successFlag){
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
                    prefs.edit().putBoolean("isLoggedIn", false).apply();
                    Log.d("AsyncLogin", "onPostExecute: I got here");
                    Intent intent = new Intent(LoginActivity.this, MainMenu.class);// New activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish(); // Call once you redirect to another activity
                }
                else{
                    Toast.makeText(getApplicationContext(), "Could not log in!", Toast.LENGTH_LONG).show();
                    Log.d("UIERROR", "doInBackground: failed login from ui");
                }
               return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
            loginProgress.setVisibility(View.GONE);
            }
    }
}

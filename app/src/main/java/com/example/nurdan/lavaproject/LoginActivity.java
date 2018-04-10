package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ApplicationLogic.ProgramSingletonController;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity {
    ProgressBar loginProgress;
    SharedPreferences prefs;
    private ProgramSingletonController localInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set preferred theme
        MainMenuActivity.setPrefTheme(this);

        setContentView(R.layout.activity_login);
        loginProgress = findViewById(R.id.loginProgressBar);
        loginProgress.setVisibility(GONE);
        prefs = getApplicationContext().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        if(prefs.getBoolean("isLoggedIn", false)){
            startNextActivity();
        }
        //Create the login buttons+listeners
        createLogInBtns();
    }


    //Goes to the next activity, main menu in this case
    public void startNextActivity() {
        Intent intent = MainMenuActivity.mainMenuIntent(getApplicationContext());
        startActivity(intent);
    }

    //Create references to the UI buttons and their listeners
    public void createLogInBtns(){
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.regButton);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgress.setVisibility(View.VISIBLE);
                //Execute async class containing networking calls
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
                //Grab user input
                String user = usernameText.getText().toString();
                String pass = passText.getText().toString();

                successFlag = localInstance.logIn(user,pass, getApplicationContext());
                Log.d("AsyncLogIn", "doInBackground: SuccessFlag " + successFlag);
                //Clear the information so it is not stored inside the app for longer than it has to.
                pass = "";
                user = "";

               return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                toggleLoadSpinner();
                if (successFlag) {
                    prefs.edit()
                            .putBoolean("isLoggedIn", true)
                            .apply();
                    startNextActivity();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Was not able to login. Please try again",Toast.LENGTH_LONG).show();
                }
            }
    }

    //Toggles the loading spinner on or off depending on its state
    private void toggleLoadSpinner() {
        if(loginProgress.getVisibility() == View.GONE)
            loginProgress.setVisibility(View.VISIBLE);
        else
            loginProgress.setVisibility(View.GONE);
    }
}

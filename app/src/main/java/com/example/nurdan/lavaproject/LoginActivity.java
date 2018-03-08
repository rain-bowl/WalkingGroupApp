package com.example.nurdan.lavaproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ApplicationLogic.AccountApiInteractions;

public class LoginActivity extends AppCompatActivity {
    private AccountApiInteractions currInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createLogInBtns();

    }

    public void createLogInBtns(){
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.regButton);

        final EditText usernameText = findViewById(R.id.usernameText);
        final EditText passText = findViewById(R.id.passText);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = usernameText.getText().toString();
                String pass = passText.getText().toString();
                currInstance = new AccountApiInteractions();
                currInstance.userLogIn(user, pass, getApplicationContext());
                pass = "";
                user = "";

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
}

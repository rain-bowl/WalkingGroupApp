package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ApplicationLogic.ProgramSingletonController;

public class LoginActivity extends AppCompatActivity {
    private ProgramSingletonController localInstance;
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
                localInstance = ProgramSingletonController.getCurrInstance();
                String user = usernameText.getText().toString();
                String pass = passText.getText().toString();
                pass = "";
                user = "";
                localInstance.logIn(user,pass, getApplicationContext());
                Intent intent=MapActivity.makeIntent(LoginActivity.this);
                startActivity(intent);

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
    //public static Intent loginActIntent(Context actContext){
        //return new Intent(actContext, MapActivity.class);
    //}
}

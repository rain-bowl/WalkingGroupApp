package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ApplicationLogic.AccountApiInteractions;

public class RegisterActivity extends AppCompatActivity {
    private String username;
    private String password;
    private String email;
    private Boolean passMatchFlag;
    AccountApiInteractions createUser = new AccountApiInteractions();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        gatherUserInput();
        createButtons();

    }


    private void gatherUserInput(){
        final EditText usernameInput = (EditText) findViewById(R.id.usernameInput);
        final EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        final EditText passwordInputConfirm = (EditText) findViewById(R.id.passwordConfirmInput);
        final EditText userEmail = (EditText) findViewById(R.id.userEmail);

        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               username = usernameInput.getText().toString();
            }
        });

        userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                email = userEmail.getText().toString();
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = passwordInput.getText().toString();
            }
        });

        passwordInputConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String secondaryPassword = passwordInputConfirm.getText().toString();
                if(secondaryPassword.compareTo(password) != 0){
                    passMatchFlag = false;

                }
                else {
                    passMatchFlag = true;
                }
            }
        });
    }

    private void createButtons(){
        Button registerBtn = (Button) findViewById(R.id.createNewAcctBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passMatchFlag) {
                    createUser.createNewUser(username, password, email, getApplicationContext());
                    password = "";
                }
                else {
                    Toast.makeText(getApplicationContext(),"Passwords do not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    //Static intent method to access activity
   public static Intent registerActIntent(Context actContext){
       return new Intent(actContext, RegisterActivity.class);

    }

//Code needs to run in an async thread. The two cannot be mixed!
    //I'm calling some simple Log commands to debug.
    //Everything is "hardwired" at this point but its just for testing purposes. I will
    //Change it up when i can. If you want to read up on what I have done, try looking up
    //some asyncTask examples/documentation.
    /*private class runASYNC extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... voids) {
           AccountApiInteractions test = new AccountApiInteractions();
           try{
               String resp = test.createNewUser("testMan12424234","1234","testManf424asf@gmail.com");
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
    }*/

}

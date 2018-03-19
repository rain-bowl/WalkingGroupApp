package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import ApplicationLogic.AccountApiInteractions;
import ApplicationLogic.ProgramSingletonController;

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
/* This method sets listeners for the user inputs in the activity. Additionally, it deals
with the task of comparing both the initial password entered as well as the confirmation password
which insures that the user has typed in the desired password with no mistakes.
 */

/*
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
//Initializes the button. If the initial password and the confirmation passwords do not match, a toast is
//displayed to let the user know.
    private void createButtons(){
        Button registerBtn = (Button) findViewById(R.id.createNewAcctBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asyncRunner currInstance = new asyncRunner();
                currInstance.execute();

            }
        });
    }
*/

    //Static intent method to access activity
   public static Intent registerActIntent(Context actContext){
       return new Intent(actContext, RegisterActivity.class);

    }

/*private class asyncRunner extends AsyncTask<Void,Void,Void>{
    @Override
    protected Void doInBackground(Void... voids) {
        if (passMatchFlag) {
                    localInstance = ProgramSingletonController.getCurrInstance();
                    localInstance.createNewUser(username, email, password, getApplicationContext());
                    password = "";
                }
                else {
                    Toast.makeText(getApplicationContext(),"Passwords do not match", Toast.LENGTH_LONG).show();
                }
        return null;
    }
}*/
}

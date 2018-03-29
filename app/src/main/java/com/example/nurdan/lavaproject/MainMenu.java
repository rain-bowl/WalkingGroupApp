package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ApplicationLogic.ProgramSingletonController;
import UIFragmentClasses.PanicMessageFragment;

// Class simply creates and contains listeners to help user navigate the application.
public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        createBtns();
    }

    private void createBtns(){
        Button mapBtn = findViewById(R.id.mapbtn);
        Button mngGroups = findViewById(R.id.mngGroups);
        Button usrMonitor = findViewById(R.id.usrMonitor);
        Button usrLogout = findViewById(R.id.usrLogout);
        Button usrProfile = findViewById(R.id.profileBtn);
        Button messageInbox = findViewById(R.id.inboxAccessBtn);
        Button panicBtn = findViewById(R.id.panicBtn);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
            }
        });

        mngGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapSecondActivity.class));
            }
        });

        usrMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userMonitor = UserMonitorDisplay.createUserMonitorIntent(getApplicationContext());
                startActivity(userMonitor);

            }
        });
        usrProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userProfile = UserProfile.userProfileIntent(getApplicationContext());
                startActivity(userProfile);
            }
        });

        //Logs out user by discarding currently saved bearer token and returns them to the log in menu
        usrLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgramSingletonController currInstence = ProgramSingletonController.getCurrInstance();
                currInstence.userLogout();
                Intent logoutIntent = LoginActivity.loginActIntent(getApplicationContext());
                startActivity(logoutIntent);

            }
        });

        messageInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inboxIntent = MessageInbox.getInboxIntent(getApplicationContext());
                startActivity(inboxIntent);
            }
        });

        panicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                PanicMessageFragment panicFrag = new PanicMessageFragment();
                panicFrag.show(fm, "Show panic message fragment");
            }
        });
    }


    public static Intent mainMenuIntent(Context currActivityContext){
        return new Intent(currActivityContext, MainMenu.class);
    }
}

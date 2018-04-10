package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ApplicationLogic.ProgramSingletonController;
import UIFragmentClasses.PanicMessageFragment;


// Class simply creates and contains listeners to help user navigate the application.
public class MainMenuActivity extends AppCompatActivity {
    private ProgramSingletonController localInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPrefTheme(this);
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
        Button gotoStore = findViewById(R.id.gotoStore);
        Button leaderboardBtn = findViewById(R.id.leaderboard_btn);

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
                Intent userMonitor = UserMonitorActivity.createUserMonitorIntent(getApplicationContext());
                startActivity(userMonitor);

            }
        });
        usrProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userProfile = UserProfileActivity.userProfileIntent(getApplicationContext());
                startActivity(userProfile);
            }
        });

        //Logs out user by discarding currently saved bearer token and returns them to the log in menu
        usrLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgramSingletonController currInstence = ProgramSingletonController.getCurrInstance();
                currInstence.userLogout();

                // reset login credentials
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
                prefs.edit()
                        .putString("bearerToken", "")
                        .putInt("userID", -1)
                        .apply();

                Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);// New activity
                startActivity(intent);
                finish();
            }
        });

        messageInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inboxIntent = MessageInboxActivity.getInboxIntent(getApplicationContext());
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

        gotoStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent store = new Intent(MainMenuActivity.this, StoreActivity.class);
                startActivity(store);
            }
        });

        leaderboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LeaderboardActivity.class));
            }
        });
    }


    public static Intent mainMenuIntent(Context currActivityContext){
        return new Intent(currActivityContext, MainMenuActivity.class);
    }


    public static void setPrefTheme(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
        String theme = prefs.getString("currentTheme", "");
        if(theme.equals("Dark Blue Theme"))
            context.setTheme(R.style.AppTheme_lvl1_NoActionBar);
    }
}

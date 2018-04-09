package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

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
        setupToolbar();
        createBtns();
    }


    private void createBtns(){
        Button mapBtn = findViewById(R.id.mapbtn);
        Button mngGroups = findViewById(R.id.mngGroups);
        Button usrMonitor = findViewById(R.id.usrMonitor);
        Button panicBtn = findViewById(R.id.panicBtn);
        Button gotoStore = findViewById(R.id.gotoStore);

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

    public void setupToolbar(){
        Toolbar mainMenuToolbar = findViewById(R.id.mainMenuToolbar);
        setSupportActionBar(mainMenuToolbar);
        getSupportActionBar().setTitle(null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainMenuInboxItem:
                Intent inboxIntent = MessageInboxActivity.getInboxIntent(getApplicationContext());
                startActivity(inboxIntent);
                break;
            case R.id.userProfileMenuItem:
                Intent userProfile = UserProfileActivity.userProfileIntent(getApplicationContext());
                startActivity(userProfile);
                break;
            //Logs out user by discarding currently saved bearer token and returns them to the log in menu
            case R.id.mainMenuLogoutItem:
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
                break;
        }
        return true;
    }
}

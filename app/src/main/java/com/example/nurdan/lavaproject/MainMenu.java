package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ApplicationLogic.ProgramSingletonController;

public class MainMenu extends AppCompatActivity {
    private ProgramSingletonController localInstance;
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

        usrLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // reset login credentials
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
                prefs.edit()
                        .putString("bearerToken", "")
                        .putInt("userID", -1)
                        .apply();

                Intent intent = new Intent(MainMenu.this, LoginActivity.class);// New activity
                startActivity(intent);
                finish();
            }
        });
    }
    public static Intent mainMenuIntent(Context currActivityContext){
        return new Intent(currActivityContext, MainMenu.class);
    }
}

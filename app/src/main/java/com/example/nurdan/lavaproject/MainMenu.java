package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    private void createBtns(){
        Button mngGroups = (Button) findViewById(R.id.mngGroups);
        Button usrMonitor = (Button) findViewById(R.id.usrMonitor);
        Button usrLogout = (Button) findViewById(R.id.usrLogout);

        mngGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            }
        });
    }
}

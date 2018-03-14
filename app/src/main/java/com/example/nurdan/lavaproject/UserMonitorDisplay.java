package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserMonitorDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_monitor_display);
    }

    //Static class to return an intent used in navigating application.
    public static Intent createUserMonitorIntent(Context currContext){
        return new Intent(currContext, UserMonitorDisplay.class);
    }
}

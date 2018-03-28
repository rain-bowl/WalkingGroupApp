package com.example.nurdan.lavaproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ApplicationLogic.ProgramSingletonController;

public class GroupMemberInfoActivity extends AppCompatActivity {
    ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
    int currMemberID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_info);
        currMemberID = currInstance.getCurrMemberID();
        Log.d("GroupMemberInfoActivity", "currMemberID: " + currMemberID);

        //TODO: set information display for current member
    }
}

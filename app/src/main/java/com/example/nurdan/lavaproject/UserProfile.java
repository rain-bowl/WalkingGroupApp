package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import UIFragmentClasses.userProfileDisplayFragment;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        loadFragment(new userProfileDisplayFragment());
    }

    public void loadFragment(Fragment fragmentClass){
        FragmentTransaction fTInstance = getSupportFragmentManager().beginTransaction();
        fTInstance.replace(R.id.userProfileFragmentContainer, fragmentClass);
        fTInstance.commit();

    }

    public static Intent userProfileIntent(Context activityContexts){
        Intent userProfileIntent = new Intent(activityContexts, UserProfile.class);
        return userProfileIntent;
    }
}

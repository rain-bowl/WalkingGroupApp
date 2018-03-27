package com.example.nurdan.lavaproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import UIFragmentClasses.userProfileDisplayFragment;

public class display_other_user_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_other_user_info);

        loadFragment(new userProfileDisplayFragment());
    }

    public void loadFragment(Fragment fragmentClass){
        FragmentTransaction fTInstance = getSupportFragmentManager().beginTransaction();
        fTInstance.replace(R.id.otherUserDisplayFrag, fragmentClass);

        Integer otherUserID = getIntent().getIntExtra("otherUserID", -1);

        Bundle bundle = new Bundle();
        bundle.putInt("theUserID", otherUserID);
        fragmentClass.setArguments(bundle);

        fTInstance.commit();
    }

}

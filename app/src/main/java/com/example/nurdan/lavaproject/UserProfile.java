package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
;

import UIFragmentClasses.userProfileEditInfoFragment;
import UIFragmentClasses.userProfileDisplayFragment;

public class UserProfile extends AppCompatActivity {
    MenuItem editItem, backItem, mainMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setUpToolbar();
        loadFragment(new userProfileDisplayFragment());

    }

    public void loadFragment(Fragment fragmentClass){
        FragmentTransaction fTInstance = getSupportFragmentManager().beginTransaction();
        fTInstance.replace(R.id.userProfileFragmentContainer, fragmentClass);
        // don't need to pass Bundle with userID when displaying logged in user's info
        fTInstance.commit();
    }

    private void setUpToolbar(){
        Toolbar profileToolbar = (Toolbar) findViewById(R.id.profileDisplayToolbar);
        setSupportActionBar(profileToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_display_menu, menu);
        editItem = menu.findItem(R.id.editItem);
        backItem = menu.findItem(R.id.backItem);
        mainMenuItem = menu.findItem(R.id.mainMenuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.editItem:
                backItem.setVisible(true);
                editItem.setVisible(false);
                loadFragment(new userProfileEditInfoFragment());
                break;
            case R.id.mainMenuItem:
                Intent mainMenu = MainMenu.mainMenuIntent(getApplicationContext());
                startActivity(mainMenu);
                finish(); // to remove activity from stack
                break;
            case R.id.backItem:
                backItem.setVisible(false);
                editItem.setVisible(true);
                loadFragment(new userProfileDisplayFragment());
        }
        return true;
    }

    public static Intent userProfileIntent(Context activityContexts){
        Intent userProfileIntent = new Intent(activityContexts, UserProfile.class);
        return userProfileIntent;
    }
}

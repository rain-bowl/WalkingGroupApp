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

import UIFragmentClasses.UserProfileEditInfoFragment;
import UIFragmentClasses.UserProfileDisplayFragment;

public class UserProfileActivity extends AppCompatActivity {
    MenuItem editItem, backItem, mainMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainMenuActivity.setPrefTheme(this);
        setContentView(R.layout.activity_user_profile);
        setUpToolbar();
        loadFragment(new UserProfileDisplayFragment());

    }

    //Loads the fragment which is passed as a parameter into the fragment container in the activity layou
    public void loadFragment(Fragment fragmentClass){
        FragmentTransaction fTInstance = getSupportFragmentManager().beginTransaction();
        fTInstance.replace(R.id.userProfileFragmentContainer, fragmentClass);
        fTInstance.commit();
    }

    //Set up our tool bar
    private void setUpToolbar(){
        Toolbar profileToolbar = (Toolbar) findViewById(R.id.profileDisplayToolbar);
        setSupportActionBar(profileToolbar);
    }

    //Set up references to the menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_display_menu, menu);
        editItem = menu.findItem(R.id.editItem);
        backItem = menu.findItem(R.id.backItem);
        mainMenuItem = menu.findItem(R.id.mainMenuItem);
        return true;
    }
    //Listener for the toolbar items.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.editItem:
                backItem.setVisible(true);
                editItem.setVisible(false);
                loadFragment(new UserProfileEditInfoFragment());
                break;
            case R.id.mainMenuItem:
                Intent mainMenu = MainMenuActivity.mainMenuIntent(getApplicationContext());
                startActivity(mainMenu);
                finish(); // to remove activity from stack
                break;
            case R.id.backItem:
                backItem.setVisible(false);
                editItem.setVisible(true);
                loadFragment(new UserProfileDisplayFragment());
        }
        return true;
    }

    //Static intent used to navigate to this activity
    public static Intent userProfileIntent(Context activityContexts){
        Intent userProfileIntent = new Intent(activityContexts, UserProfileActivity.class);
        return userProfileIntent;
    }
}

package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import UIFragmentClasses.UserProfileDisplayFragment;
import UIFragmentClasses.UserProfileEditInfoFragment;
//This class is the hosting activity class for displaying the information of another user who is being monitored by
//the current logged in user.
public class MonitoredUserInformationDisplayActivity extends AppCompatActivity {
    MenuItem editItem;
    Integer otherUserID;
    Boolean isMonitored;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainMenuActivity.setPrefTheme(this);
        setContentView(R.layout.activity_display_other_user_info);

        //Retrieve the information about the user we want to monitor from the intent for this activity
        otherUserID = getIntent().getIntExtra("otherUserID", -1);
        isMonitored = getIntent().getBooleanExtra("isMonitored", true);

        setUpToolbar();
        loadFragment(new UserProfileDisplayFragment());
    }

    //set up toolbar
    private void setUpToolbar(){
        Toolbar profileToolbar = (Toolbar) findViewById(R.id.otherProfileDisplayToolbar);
        setSupportActionBar(profileToolbar);
    }

    //Hide options from the user is the current user is not monitoring them
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_display_menu, menu);
        editItem = menu.findItem(R.id.editItem);
        if(!isMonitored) editItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editItem:
                loadFragment(new UserProfileEditInfoFragment());
                break;
            case R.id.mainMenuItem:
                startActivity(new Intent(MonitoredUserInformationDisplayActivity.this, UserMonitorActivity.class));
                finish();
                break;
        }
        return true;
    }

    //Loads a fragment into the container within the layout of this activity. Used to swap fragments back and forth
    public void loadFragment(Fragment fragmentClass){
        FragmentTransaction fTInstance = getSupportFragmentManager().beginTransaction();
        fTInstance.replace(R.id.otherUserDisplayFrag, fragmentClass);

        Bundle bundle = new Bundle();
        bundle.putInt("theUserID", otherUserID);
        fragmentClass.setArguments(bundle);

        fTInstance.commit();
    }

}

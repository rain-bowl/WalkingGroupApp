package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import UIFragmentClasses.userProfileDisplayFragment;
import UIFragmentClasses.userProfileEditInfoFragment;

public class display_other_user_info extends AppCompatActivity {
    MenuItem editItem;
    Integer otherUserID;
    Boolean isMonitored;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_other_user_info);

        otherUserID = getIntent().getIntExtra("otherUserID", -1);
        isMonitored = getIntent().getBooleanExtra("isMonitored", true);

        setUpToolbar();
        loadFragment(new userProfileDisplayFragment());
    }

    private void setUpToolbar(){
        Toolbar profileToolbar = (Toolbar) findViewById(R.id.otherProfileDisplayToolbar);
        setSupportActionBar(profileToolbar);
    }

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
                loadFragment(new userProfileEditInfoFragment());
                break;
            case R.id.mainMenuItem:
                startActivity(new Intent(display_other_user_info.this, UserMonitorDisplay.class));
                finish();
                break;
        }
        return true;
    }

    public void loadFragment(Fragment fragmentClass){
        FragmentTransaction fTInstance = getSupportFragmentManager().beginTransaction();
        fTInstance.replace(R.id.otherUserDisplayFrag, fragmentClass);

        Bundle bundle = new Bundle();
        bundle.putInt("theUserID", otherUserID);
        fragmentClass.setArguments(bundle);

        fTInstance.commit();
    }

}

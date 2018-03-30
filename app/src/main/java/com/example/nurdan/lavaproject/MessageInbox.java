package com.example.nurdan.lavaproject;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import UIFragmentClasses.UserInboxDisplayFragment;
import UIFragmentClasses.UserInboxNewMessageFragment;

public class MessageInbox extends AppCompatActivity {
    int groupID = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_inbox);
        setUpToolbar();
        setFragment(new UserInboxDisplayFragment());
    }

    private void setUpToolbar(){
        Toolbar inboxToolBar = (Toolbar) findViewById(R.id.userMessageInboxToolbar);
        setSupportActionBar(inboxToolBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_inbox_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.newMessageItem:
                setFragment(new UserInboxNewMessageFragment());
                break;
            case R.id.backInboxItem:
                setFragment(new UserInboxDisplayFragment());
                break;
            case R.id.mainMenuInboxItem:
                Intent mainMenu = MainMenu.mainMenuIntent(getApplicationContext());
                startActivity(mainMenu);
        }
        return true;
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction ftInstance = getSupportFragmentManager().beginTransaction();
        ftInstance.replace(R.id.inboxFragmentContainer, fragment);
        ftInstance.commit();

    }

    public static Intent getInboxIntent(Context activityContext){
        Intent inboxIntent = new Intent(activityContext, MessageInbox.class);
        return inboxIntent;
    }
    public void setGroupID(int id){
        groupID = id;
        Log.d("messageinbox", "setGroupID: id: " + id + "groupid: " + groupID);
    }

    public Integer getGroupID(){
        Log.d("messageinbox", "getGroupID: groupid: " + groupID);
        return this.groupID;
    }
}

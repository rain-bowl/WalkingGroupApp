package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import ApplicationLogic.ProgramSingletonController;
import UIFragmentClasses.UserInboxDisplayFragment;
import UIFragmentClasses.UserInboxNewMessageFragment;

import static java.security.AccessController.getContext;

public class single_message extends AppCompatActivity {
    ProgramSingletonController currSingletonInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_message);

        currSingletonInstance = ProgramSingletonController.getCurrInstance();

        setupToolbar();
        displayAmessage();

    }
    private void setupToolbar(){
        Toolbar inboxToolBar = (Toolbar) findViewById(R.id.singleMessageToolbar);
        setSupportActionBar(inboxToolBar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_message_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.single_msg_back:
                //Intent mainMenu = new Intent(single_message.this, MessageInbox.class);
                // can finish activity since there is no direct way to get to this activity
                finish();
                break;
        }
        return true;
    }

    private void displayAmessage() {
        Intent intent = getIntent();
        int messageId = intent.getIntExtra("messageId", -1);
        if(messageId == -1) {
            Toast.makeText(single_message.this, "Unable to retrieve the message", Toast.LENGTH_SHORT);
            return;
        }

        getOneMessage getMsg = new getOneMessage();
        getMsg.execute(messageId);
    }

    private class getOneMessage extends AsyncTask<Integer,Void,JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... ints) {
            int msgId = ints[0];
            currSingletonInstance = ProgramSingletonController.getCurrInstance();
            return currSingletonInstance.getMessageObjById(msgId, single_message.this);
        }
        @Override
        protected void onPostExecute(JSONObject aMessage) {
            String messageStr = "";
            int msgId = -1;
            String fromUser;
            try {
                messageStr = aMessage.getString("text");
                msgId = aMessage.getInt("id");
            } catch (Exception e) {}

            TextView messageBox = findViewById(R.id.messageBox);
            messageBox.setText(messageStr);

            currSingletonInstance = ProgramSingletonController.getCurrInstance();
            currSingletonInstance.setUserMessageRead(true, msgId, single_message.this);
        }
    }
}

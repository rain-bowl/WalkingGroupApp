package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MessageInbox extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_inbox);
    }

    public static Intent getInboxIntent(Context activityContext){
        Intent inboxIntent = new Intent(activityContext, MessageInbox.class);
        return inboxIntent;
    }
}

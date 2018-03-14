package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ApplicationLogic.AccountApiInteractions;
import ApplicationLogic.ProgramSingletonController;

public class UserMonitorDisplay extends AppCompatActivity {
    ProgramSingletonController currInstanceSingleton = ProgramSingletonController.getCurrInstantce();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_monitor_display);
        setUpListView();
    }

    //Static class to return an intent used in navigating application.
    public static Intent createUserMonitorIntent(Context currContext){
        return new Intent(currContext, UserMonitorDisplay.class);
    }
private void setUpListView(){
    getMntrUsers getUsers = new getMntrUsers();
    getUsers.execute();


}

private class getMntrUsers extends AsyncTask<Void,Void,Void>{
    ArrayAdapter<String> temp;
    ListView displayMntrdUser;
    @Override
    protected Void doInBackground(Void... voids) {
         displayMntrdUser = (ListView) findViewById(R.id.usersMonitoredView);
         temp = currInstanceSingleton.getUsersMonitored(getApplicationContext());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        displayMntrdUser.setAdapter(temp);
    }
}

}

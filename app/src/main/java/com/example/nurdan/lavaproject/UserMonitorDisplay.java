package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

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
    private void setUpToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.monitorToolBar);
        setSupportActionBar(toolbar);
    }

    //Static class to return an intent used in navigating application.
    public static Intent createUserMonitorIntent(Context currContext){
        return new Intent(currContext, UserMonitorDisplay.class);
    }
    private void setUpListView(){
        getMntrUsers getMonitorees = new getMntrUsers();
        getUsrsMntrThis getMonitors = new getUsrsMntrThis();
        getMonitorees.execute();
        getMonitors.execute();


}

private class getMntrUsers extends AsyncTask<Void,Void,Void>{
    ListView displayMntrdUser;
    ArrayList<String> retrievedUsers;
    @Override
    protected Void doInBackground(Void... voids) {
         displayMntrdUser = (ListView) findViewById(R.id.usersMonitoredView);
        retrievedUsers = currInstanceSingleton.getUsersMonitored(getApplicationContext());

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("USERDISPLAY", "onPostExecute:Got here ");
        if(retrievedUsers.isEmpty()){
            retrievedUsers.add("There are no users currently monitored");
             ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_listview_display_layout, retrievedUsers);
        displayMntrdUser.setAdapter(listViewAdapter);
        }
        else{
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_listview_display_layout, retrievedUsers);
        displayMntrdUser.setAdapter(listViewAdapter);
        }

    }
}
private class getUsrsMntrThis extends AsyncTask<Void,Void,Void>{
    ListView displayMonitors;
    ArrayList<String> retrievedUsers;

    @Override
    protected Void doInBackground(Void... voids) {
        displayMonitors = (ListView) findViewById(R.id.usersWhoMonitoreYouView);
        retrievedUsers = currInstanceSingleton.getUsersWhoMonitorThis(getApplicationContext());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ArrayAdapter<String> listViewAdapter;
       if(retrievedUsers.isEmpty()){
           retrievedUsers.add("There are no users monitoring you");
           listViewAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_listview_display_layout, retrievedUsers);
           displayMonitors.setAdapter(listViewAdapter);
       }
       else{
           listViewAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_listview_display_layout, retrievedUsers);
           displayMonitors.setAdapter(listViewAdapter);
       }
    }
}

}

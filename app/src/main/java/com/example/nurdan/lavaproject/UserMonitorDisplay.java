package com.example.nurdan.lavaproject;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ApplicationLogic.AccountApiInteractions;
import ApplicationLogic.ProgramSingletonController;
import ApplicationLogic.UserMonitor;

public class UserMonitorDisplay extends AppCompatActivity {
    ProgressBar displayProgress;
    ProgramSingletonController currInstanceSingleton = ProgramSingletonController.getCurrInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_monitor_display);
        displayProgress = findViewById(R.id.displayProgress);
        displayProgress.setVisibility(View.GONE);
        setUpToolBar();
        setUpListView();
    }
    private void setUpToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.monitorToolBar);
        setSupportActionBar(toolbar);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_monitor_user:
                AppCompatDialogFragment addUserFragment = new AddUserDialog();
                addUserFragment.show(getSupportFragmentManager(), "addUsr");
                break;
            case R.id.delete_monitor_user:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.taskmenu, menu);
        return true;
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
        displayProgress.setVisibility(View.VISIBLE);
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


            displayMntrdUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String username = parent.getItemAtPosition(position).toString();
                        Toast.makeText(UserMonitorDisplay.this, "POS: " + username, Toast.LENGTH_SHORT).show();


                }
            });
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
       displayProgress.setVisibility(View.GONE);
    }
}

}

package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ApplicationLogic.ProgramSingletonController;
import UIFragmentClasses.AddUserDialogFragment;

public class UserMonitorDisplay extends AppCompatActivity {
    ProgressBar displayProgress;
    ProgramSingletonController currInstanceSingleton = ProgramSingletonController.getCurrInstance();

    // global variable in order to access IDs
    ArrayList<Integer> retrievedMonitoringIDs;
    ArrayList<Integer> retrievedMonitoringMeIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_monitor_display);
        displayProgress = findViewById(R.id.displayProgress);
        displayProgress.setVisibility(View.GONE);

        setUpToolBar();
        setUpListView();
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.monitorToolBar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_monitor_user:
                AppCompatDialogFragment addUserFragment = new AddUserDialogFragment();
                addUserFragment.show(getSupportFragmentManager(), "addUsr");
                break;
            case R.id.delete_monitor_user:
                SparseBooleanArray monitoredList = ((ListView) findViewById(R.id.usersMonitoredView)).getCheckedItemPositions();
                SparseBooleanArray monitoringMelist = ((ListView) findViewById(R.id.usersWhoMonitoreYouView)).getCheckedItemPositions();

                if(monitoredList == null && monitoringMelist == null) {
                    Toast.makeText(this, "Select user to delete", Toast.LENGTH_SHORT).show();
                    break;
                }

                if(monitoredList != null) deleteMonitoringUsers(retrievedMonitoringIDs, R.id.usersMonitoredView);
                if(monitoringMelist != null) deleteUsersMonitoringMe(retrievedMonitoringMeIDs, R.id.usersWhoMonitoreYouView);
                break;
            case R.id.info_monitor_user:
                SparseBooleanArray infoList = ((ListView) findViewById(R.id.usersMonitoredView)).getCheckedItemPositions();
                if(infoList == null) {
                    Toast.makeText(this, "Select user", Toast.LENGTH_SHORT).show();
                    break;
                }
                ArrayList<Integer> checkedUsers = new ArrayList<>();
                for (int i = 0, len = infoList.size(); i < len; i++) {
                    if (infoList.valueAt(i)) {
                        int id = retrievedMonitoringIDs.get(i);
                        checkedUsers.add(id);
                    }
                }
                Log.d("USERINFO", "" + checkedUsers.get(0));
                AsyncGetUserInfo getinfo = new AsyncGetUserInfo();
                getinfo.execute(checkedUsers);

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteMonitoringUsers(ArrayList<Integer> userIds, Integer listID) {
        ListView displayList = (ListView) findViewById(listID);
        SparseBooleanArray checked = displayList.getCheckedItemPositions();
        // check if item is checked
        if (checked == null) {
            Toast.makeText(this, "Select user to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<Integer> checkedUsers = new ArrayList<>();
        for (int i = 0, len = checked.size(); i < len; i++) {
            if (checked.valueAt(i)) {
                //String s = ((TextView) displayList.getChildAt(i)).getText().toString();
                int id = userIds.get(i);
                checkedUsers.add(id);
            }
        }
        AsyncDeleteMonitoredUser deleteUsers = new AsyncDeleteMonitoredUser();
        deleteUsers.execute(checkedUsers);

        String lenUsers = String.format(Locale.CANADA, "Stopped monitoring %d users %s", checkedUsers.size(), Arrays.toString(checkedUsers.toArray()));
        Toast.makeText(this, lenUsers, Toast.LENGTH_SHORT).show();
    }

    private void deleteUsersMonitoringMe(ArrayList<Integer> userIds, Integer listID) {
        ListView displayList = (ListView) findViewById(listID);
        SparseBooleanArray checked = displayList.getCheckedItemPositions();
        // check if item is checked
        if (checked == null) {
            Toast.makeText(this, "Select user to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayList<Integer> checkedUsers = new ArrayList<>();
        for (int i = 0, len = checked.size(); i < len; i++) {
            if (checked.valueAt(i)) {
                int id = userIds.get(i);
                checkedUsers.add(id);
            }
        }
        AsyncDeleteMonitoringMeUsers deleteUsers = new AsyncDeleteMonitoringMeUsers();
        deleteUsers.execute(checkedUsers);

        String lenUsers = String.format(Locale.CANADA, "Stopped monitoring %d users %s", checkedUsers.size(), Arrays.toString(checkedUsers.toArray()));
        Toast.makeText(this, lenUsers, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.taskmenu, menu);
        return true;
    }

    //Static class to return an intent used in navigating application.
    public static Intent createUserMonitorIntent(Context currContext) {
        return new Intent(currContext, UserMonitorDisplay.class);
    }

    private void setUpListView() {
        getMntrUsers getMonitorees = new getMntrUsers();
        getUsrsMntrThis getMonitors = new getUsrsMntrThis();
        getMonitorees.execute();
        getMonitors.execute();
    }

    private void updateListView(ArrayList<String> retrievedUsers, ArrayList<Integer> retrievedUserIDs, int resourceID){
        ListView displayMntrdUser = (ListView) findViewById(resourceID);
        if (retrievedUsers.isEmpty()) {
                retrievedUserIDs.clear();
                retrievedUsers.add("There are no users currently monitored");
                ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_listview_display_layout, retrievedUsers);
                displayMntrdUser.setAdapter(listViewAdapter);
            } else {
                ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, retrievedUsers);
                displayMntrdUser.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                displayMntrdUser.setAdapter(listViewAdapter);
            }
    }

    private class getMntrUsers extends AsyncTask<Void, Void, Void> {
        ListView displayMntrdUser;
        ArrayList<String> retrievedUsers;
        @Override
        protected Void doInBackground(Void... voids) {
            displayMntrdUser = (ListView) findViewById(R.id.usersMonitoredView);
            retrievedUsers = currInstanceSingleton.getUsersMonitored(getApplicationContext());
            retrievedMonitoringIDs = currInstanceSingleton.getUsersMonitoredIDs(getApplicationContext());
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("USERDISPLAY", "onPostExecute:Got here ");
            if(retrievedUsers == null) {
                Toast.makeText(UserMonitorDisplay.this, "Could not retrieve monitored users", Toast.LENGTH_SHORT).show();
                return;
            }
            updateListView(retrievedUsers, retrievedMonitoringIDs, R.id.usersMonitoredView);
        }
    }

    private class getUsrsMntrThis extends AsyncTask<Void, Void, Void> {
        ListView displayMonitors;
        ArrayList<String> retrievedUsers;
        @Override
        protected Void doInBackground(Void... voids) {
            displayMonitors = (ListView) findViewById(R.id.usersWhoMonitoreYouView);
            retrievedUsers = currInstanceSingleton.getUsersWhoMonitorThis(getApplicationContext());
            retrievedMonitoringMeIDs = currInstanceSingleton.getIDsWhoMonitorThis(getApplicationContext());
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(retrievedUsers == null) {
                Toast.makeText(UserMonitorDisplay.this, "Could not retrieve users monitoring you", Toast.LENGTH_SHORT).show();
                return;
            }
            updateListView(retrievedUsers, retrievedMonitoringMeIDs, R.id.usersWhoMonitoreYouView);
        }
    }

    private class AsyncDeleteMonitoredUser extends AsyncTask<ArrayList<Integer>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<Integer>... arrayLists) {
            ArrayList<Integer> list = arrayLists[0];
            for (int i = 0, len = list.size(); i < len; i++) {
                int id = list.get(i);
                currInstanceSingleton.deleteMonitoredUsr(id, UserMonitorDisplay.this);
                Log.d("DELETEDUSER", " with ID " + id);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            new getMntrUsers().execute();
        }
    }

    private class AsyncDeleteMonitoringMeUsers extends AsyncTask<ArrayList<Integer>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<Integer>...arrayLists) {
            ArrayList<Integer> list = arrayLists[0];
            for (int i = 0, len = list.size(); i < len; i++) {
                int id = list.get(i);
                currInstanceSingleton.deleteMonitoringMeUser(id, UserMonitorDisplay.this);
                Log.d("DELETEDUSER", " with ID " + id);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            new getUsrsMntrThis().execute();
        }
    }

    private class AsyncGetUserInfo extends  AsyncTask<ArrayList<Integer>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<Integer>...arrayLists) {
            ArrayList<Integer> list = arrayLists[0];
            currInstanceSingleton.getUserInfoByID(list.get(0), UserMonitorDisplay.this);
            return null;
        }
        @Override
        protected  void onPostExecute(Void param) {
        }
    }
}

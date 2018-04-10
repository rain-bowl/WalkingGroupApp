package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ApplicationLogic.ProgramSingletonController;
import UIFragmentClasses.AddUserDialogFragment;

public class UserMonitorActivity extends AppCompatActivity {
    ProgressBar displayProgress;
    ProgramSingletonController currInstanceSingleton = ProgramSingletonController.getCurrInstance();

    // global variable in order to access IDs
    ArrayList<Integer> retrievedMonitoringIDs;
    ArrayList<Integer> retrievedMonitoringMeIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainMenuActivity.setPrefTheme(this);
        setContentView(R.layout.activity_user_monitor_display);
        displayProgress = findViewById(R.id.displayProgress);
        displayProgress.setVisibility(View.GONE);

        setUpToolBar();
        setUpListView();
    }
    //Set up activity toolbar
    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.monitorToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
    }

    //Listener for different buttons in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ListView listMonitorThem = (ListView) findViewById(R.id.usersMonitoredView);
        ListView listMonitorMe = (ListView) findViewById(R.id.usersWhoMonitoreYouView);
        Integer checkedMonitorThem = listMonitorThem.getCheckedItemPosition();
        Integer checkedMonitorMe = listMonitorMe.getCheckedItemPosition();
        switch (item.getItemId()) {
            case R.id.add_monitor_user:
                //load new fragment
                AppCompatDialogFragment addUserFragment = new AddUserDialogFragment();
                addUserFragment.show(getSupportFragmentManager(), "addUsr");
                break;

            case R.id.delete_monitor_user:
                Log.d("CHECKED", "" + checkedMonitorMe);

                if(checkedMonitorThem != -1) deleteMonitorUser(checkedMonitorThem, retrievedMonitoringIDs, R.id.usersMonitoredView);
                else if(checkedMonitorMe != -1) deleteMonitorUser(checkedMonitorMe, retrievedMonitoringMeIDs, R.id.usersWhoMonitoreYouView);
                break;

            case R.id.info_monitor_user:
                //Show the info of the selected user, goes to a new activity
                Intent otherUsers = new Intent(UserMonitorActivity.this, MonitoredUserInformationDisplayActivity.class);
                Integer checked = -1;
                Boolean isMonitored;
                if(checkedMonitorThem != -1) {
                    checked = retrievedMonitoringIDs.get(checkedMonitorThem);
                    isMonitored = true;
                }
                else if(checkedMonitorMe != -1) {
                    checked = retrievedMonitoringMeIDs.get(checkedMonitorMe);
                    isMonitored = false;
                }
                else {
                    Toast.makeText(UserMonitorActivity.this, "Must select user", Toast.LENGTH_SHORT).show();
                    break;
                }

                otherUsers.putExtra("otherUserID", checked);
                otherUsers.putExtra("isMonitored", isMonitored);
                startActivity(otherUsers);
                finish(); // in case information is modified
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteMonitorUser(Integer userID, ArrayList<Integer> listID, Integer listviewID) {
        ListView listMonitorThem = (ListView) findViewById(R.id.usersMonitoredView);
        ListView listMonitorMe = (ListView) findViewById(R.id.usersWhoMonitoreYouView);
        ListView list = findViewById(listviewID);

        Log.d("DELETEUSER", "with ID " + listID.get(userID));

        if(listMonitorMe == list) {
            AsyncDeleteMonitoringMeUsers delUser = new AsyncDeleteMonitoringMeUsers();
            Integer delId = listID.get(userID);
            ArrayList<Integer> d = new ArrayList<>();
            d.add(delId);
            delUser.execute(d);
        } else if (listMonitorThem == list) {
            AsyncDeleteMonitoredUser delUser = new AsyncDeleteMonitoredUser();
            Integer delId = listID.get(userID);
            ArrayList<Integer> d = new ArrayList<>();
            d.add(delId);
            delUser.execute(d);
        }
    }
   //Set up meny layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.taskmenu, menu);
        return true;
    }

    //Static class to return an intent used in navigating application.
    public static Intent createUserMonitorIntent(Context currContext) {
        return new Intent(currContext, UserMonitorActivity.class);
    }
    //Calls the methods to retrieve the information which is shown to the user(Names) as well as ID's
    private void setUpListView() {
        getMntrUsers getMonitorees = new getMntrUsers();
        getUsrsMntrThis getMonitors = new getUsrsMntrThis();
        getMonitorees.execute();
        getMonitors.execute();
    }

    //Loads the information retreived by the setUpListview method into the UI.
    private void updateListView(ArrayList<String> retrievedUsers, ArrayList<Integer> retrievedUserIDs, int resourceID){
        ListView displayMntrdUser = (ListView) findViewById(resourceID);
        //Handle the case of the number of retrieved users being 0
        if (retrievedUsers.isEmpty()) {
                retrievedUserIDs.clear();
                retrievedUsers.add("There are no users currently monitored");
                ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_listview_display_layout, retrievedUsers);
                displayMntrdUser.setAdapter(listViewAdapter);
            } else {
                ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_single_choice, retrievedUsers);
                displayMntrdUser.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
                displayMntrdUser.setAdapter(listViewAdapter);
                displayMntrdUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // Make sure only one item is selected
                        ListView list1 = findViewById(R.id.usersMonitoredView);
                        ListView list2 = findViewById(R.id.usersWhoMonitoreYouView);
                        if(list1 == parent)
                            list2.setItemChecked(-1, true);
                        else
                            list1.setItemChecked(-1, true);
                    }
                });
            }
    }
    //Async classes to retrieve the user information.
    private class getMntrUsers extends AsyncTask<Void, Void, Void> {
        ListView displayMntrdUser;
        ArrayList<String> retrievedUsers;
        @Override
        protected Void doInBackground(Void... voids) {
            displayMntrdUser = (ListView) findViewById(R.id.usersMonitoredView);
            //Simply calling singleton methods to get information
            retrievedUsers = currInstanceSingleton.getUsersMonitored(getApplicationContext());
            retrievedMonitoringIDs = currInstanceSingleton.getUsersMonitoredIDs(getApplicationContext());
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("USERDISPLAY", "onPostExecute:Got here ");
            //Case of possible network error, display a toast
            if(retrievedUsers == null) {
                Toast.makeText(UserMonitorActivity.this, "Could not retrieve monitored users", Toast.LENGTH_SHORT).show();
                return;
            }
            updateListView(retrievedUsers, retrievedMonitoringIDs, R.id.usersMonitoredView);
        }
    }

    //Another async class which is similar to the one above. Retrieves the users who monitor the logged in user.
    //Sets up 2 array lists with one holding the names and the other their id's
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
            //Network error case
            if(retrievedUsers == null) {
                Toast.makeText(UserMonitorActivity.this, "Could not retrieve users monitoring you", Toast.LENGTH_SHORT).show();
                return;
            }
            updateListView(retrievedUsers, retrievedMonitoringMeIDs, R.id.usersWhoMonitoreYouView);
        }
    }

    //Delete a user who is being monitored by the current logged in user
    private class AsyncDeleteMonitoredUser extends AsyncTask<ArrayList<Integer>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<Integer>... arrayLists) {
            ArrayList<Integer> list = arrayLists[0];
            for (int i = 0, len = list.size(); i < len; i++) {
                int id = list.get(i);
                currInstanceSingleton.deleteMonitoredUsr(id, UserMonitorActivity.this);
                Log.d("DELETEDUSER", " with ID " + id);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            new getMntrUsers().execute();
        }
    }

    //Delete a user who is monitoring the current logged in user
    private class AsyncDeleteMonitoringMeUsers extends AsyncTask<ArrayList<Integer>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<Integer>...arrayLists) {
            ArrayList<Integer> list = arrayLists[0];
            Log.d("DELETEDUSER", " with size " + list.size());

            for (int i = 0, len = list.size(); i < len; i++) {
                int id = list.get(i);
                currInstanceSingleton.deleteMonitoringMeUser(id, UserMonitorActivity.this);
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
            currInstanceSingleton.getUserInfoByID(list.get(0), UserMonitorActivity.this);
            return null;
        }
        @Override
        protected  void onPostExecute(Void param) {
        }
    }
}

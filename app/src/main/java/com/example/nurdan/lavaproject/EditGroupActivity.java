package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ApplicationLogic.ProgramSingletonController;
import ApplicationLogic.User;

/**
 * This activity edits the information for a particular group which is selected by the logged in user
 */
public class EditGroupActivity extends AppCompatActivity {
    ListView currMembers;
    ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
    int currGroupId;                                    //Id of the current group
    JSONObject currGroupDetails = new JSONObject();     //Will hold group details
    String currGroupName;           //Group name
    String newGroupName;            //Possibly hold the new group name if one is provided
    JSONArray latArr = new JSONArray();
    JSONArray lngArr = new JSONArray();
    ArrayList<String> membersNameList = new ArrayList<>();
    ArrayList<Integer> membersIDList = new ArrayList<>();
    //Shows state of program if current leaded wants to change leadership of the group
    Boolean newLeader = false;
    //Holds the ID of the new leader chosen
    int newLeaderID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        currMembers = findViewById(R.id.membersListView);
        //Get id of the current selected group from singleton class
        currGroupId = currInstance.getCurrGroupID();
        Log.d("currGroupId: ", "currGroupId = " + currGroupId);

        //Set up all buttons
        setupBackBtn();
        createSubmitBtn();
        setupNewLeaderBtn();

        //Grab information to be displayed to the user
        editGroup test = new editGroup();
        test.execute();

        getMembers test2 = new getMembers();
        test2.execute();

        ListClick(currMembers);
    }

    //Async method to retrieve the group details. Sets the appropriate fields upon completion
    private class editGroup extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void ... voids) {
            currGroupDetails = currInstance.getGroupDetails(currGroupId, getApplicationContext());
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            try {
                //Set fields to edit
                currGroupName = currGroupDetails.getString("groupDescription");
                latArr = currGroupDetails.getJSONArray("routeLatArray");
                lngArr = currGroupDetails.getJSONArray("routeLngArray");
                Log.d("editGroupActivity: ", "currGroupName = " + currGroupName);
                Log.d("editGroupActivity: ", "latArr = " + latArr);
                Log.d("editGroupActivity: ", "lngArr = " + lngArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Set textview to group name
            TextView edName = findViewById(R.id.editName);
            edName.setText(currGroupName);
        }
    }

    //Retrieves the members of a group
    private class getMembers extends AsyncTask<Void,Void,Void> {
        JSONArray original;

        @Override
        protected Void doInBackground(Void... voids) {
            original = currInstance.getGroupMembers(currGroupId, getApplicationContext());
            Log.d("getGroupMembers: ", original.toString());

            for (int i = 0; i < original.length(); i++) {
                JSONObject childJSONObject = null;
                try {
                    childJSONObject = original.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (childJSONObject != null) {
                        membersNameList.add(childJSONObject.getString("name"));
                        membersIDList.add(childJSONObject.getInt("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("onpost: members list", membersNameList.toString());
            Log.d("onpost: membersID list", membersIDList.toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setMemberView(currMembers, membersNameList);
        }
    }

    //Sets up the listview contents
    private void setMemberView(ListView list, ArrayList<String> names) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, names);
        list.setAdapter(adapter);
    }

    //Implements handles for listview. If the newleader flag is selected then it will collect the user Id of the selected group member
    //Otherwise, it displays the information for the selected user
    private void ListClick (final ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                if(!newLeader) {
                    Object member = list.getItemAtPosition(position);
                    Log.d("ListClick", "member: " + member);
                    currInstance.setCurrMemberID(membersIDList.get(position));
                    Log.d("ListClick", "memberID: " + membersIDList.get(position));
                    startActivity(new Intent(getApplicationContext(), GroupMemberInfoActivity.class));
                }
                else{
                    newLeaderID = membersIDList.get(position);
                    Log.d("ListClick", "onItemClick: New Leader ID " + newLeaderID);
                    newLeader = false;
                    Toast.makeText(getApplicationContext(), getText(R.string.submitChanges), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //Submits the edited information. Uses 2 overloaded methods, one which simply edits the name of the group and another which can also
    //edit the new leader.
    private class submitEdit extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void ... voids) {
            if(newLeaderID == -1) {
                currInstance.updateGroup(currGroupId, newGroupName, latArr, lngArr, getApplicationContext());
            }
            else{
                currInstance.updateGroup(newLeaderID, currGroupId, newGroupName, latArr, lngArr, getApplicationContext());
                currInstance.addGroupMember(currGroupId, getApplicationContext());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
        }
    }

    //creates the submit button which will launch the async task to either edit the group name or change the group leader
    public void createSubmitBtn(){
        Button submitBtn = findViewById(R.id.submitBtn);
        final EditText newName = findViewById(R.id.editName);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGroupName = newName.getText().toString();
                // check if name was changed, if not then return to list of groups
                if (newGroupName.equals(currGroupName)) {
                    startActivity(new Intent(getApplicationContext(), MapSecondActivity.class));
                }
                // make editgroup request to server
                submitEdit test3 = new submitEdit();
                test3.execute();
                startActivity(new Intent(getApplicationContext(), MapSecondActivity.class));
                finish();
            }
        });
    }

    //Sets up the new leader button. When clicked, it sets the newLeader boolean flag to true which indicates that the user wants to select a
    //new group leader
    private void setupNewLeaderBtn(){
        Button newLeaderBtn = findViewById(R.id.newLeaderBtn);
        newLeaderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Please select a new leader from the list", Toast.LENGTH_LONG).show();
                newLeader = true;
            }
        });
    }

    //Simply set the back button. Closes current activity and removes it from stack
    private void setupBackBtn() {
        Button btn = findViewById(R.id.editBackBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import ApplicationLogic.ProgramSingletonController;

public class MapSecondActivity extends AppCompatActivity{
    private ListView allGroupsList;
    private ListView leaderOfList;
    private ListView memberOfList;
    private ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
    private ArrayList<String> allGroupNameList = new ArrayList<>();
    private ArrayList<String> leaderOfNameList = new ArrayList<>();
    private ArrayList<String> memberOfNameList = new ArrayList<>();
    private ArrayList<Integer> allGroupIDList = new ArrayList<>();
    private ArrayList<Integer> leaderOfIDList = new ArrayList<>();
    private ArrayList<Integer> memberOfIDList = new ArrayList<>();
    private setupListView test = new setupListView();
    SparseBooleanArray leaderChecked = new SparseBooleanArray();
    SparseBooleanArray memberChecked = new SparseBooleanArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainMenuActivity.setPrefTheme(this);
        setContentView(R.layout.activity_map_second);
        allGroupsList = findViewById(R.id.groupListView);
        leaderOfList = findViewById(R.id.leaderGroups);
        memberOfList = findViewById(R.id.memberGroups);

        setupBackbtn();
        makeJoinBtn();
        makeRemoveBtn ();
        makeEditBtn();
        test.execute();
    }

    //Make the toolbar and let users to edit the groups features
    private void makeEditBtn() {
        Button editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Edit/Details clicked", Toast.LENGTH_SHORT).show();
                Log.d("Edit/Details clicked: ", "Edit/Details clicked ");

                leaderChecked = leaderOfList.getCheckedItemPositions();
                memberChecked = memberOfList.getCheckedItemPositions();

                Log.d("boolarr: ", "leader array: " + leaderChecked);
                Log.d("boolarr: ", "member array: " + memberChecked);

                ArrayList<Integer> checkedLeaderGroups = new ArrayList<>();
                ArrayList<String> checkedLeaderNameGroups = new ArrayList<>();
                for (int i = 0; i < leaderOfIDList.size(); i++) {
                    if (leaderChecked.get(i)) {
                        int id = leaderOfIDList.get(i);
                        Log.d("leaveCheckedGroup:", "curr id: " + id);
                        checkedLeaderGroups.add(id);
                        checkedLeaderNameGroups.add(leaderOfNameList.get(i));
                    }
                }

                ArrayList<Integer> checkedMemberGroups = new ArrayList<>();
                ArrayList<String> checkedMemberNameGroups = new ArrayList<>();
                for (int i = 0; i < memberOfIDList.size(); i++) {
                    if (memberChecked.get(i)) {
                        int id = memberOfIDList.get(i);
                        Log.d("leaveCheckedGroup:", "curr id: " + id);
                        checkedMemberGroups.add(id);
                        checkedMemberNameGroups.add(memberOfNameList.get(i));
                    }
                }

                if (checkedLeaderGroups.size() == 1 && checkedMemberGroups.size() == 0) {
                    int id = checkedLeaderGroups.get(0);
                    currInstance.setCurrGroupID(id);
                    currInstance.setCurrGroupName(checkedLeaderNameGroups.get(0));
                    startActivity(new Intent(getApplicationContext(), EditGroupActivity.class));
                }
                else if (checkedLeaderGroups.size() == 0 && checkedMemberGroups.size() == 1) {
                    int id = checkedMemberGroups.get(0);
                    currInstance.setCurrGroupID(id);
                    currInstance.setCurrGroupName(checkedMemberNameGroups.get(0));
                    startActivity(new Intent(getApplicationContext(), ViewMembersActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please select only one group", Toast.LENGTH_SHORT).show();
                    Log.d("Edit/Details clicked: ", "more than one group selected");
                    leaderChecked.clear();
                    memberChecked.clear();
                    checkedLeaderNameGroups.clear();
                    checkedMemberNameGroups.clear();
                }
            }
        });
    }

    //Make join button and let users join groups
    private void makeJoinBtn () {
        Button removeBtn = findViewById(R.id.SecJoinBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Join clicked", Toast.LENGTH_SHORT).show();
                Log.d("Join clicked: ", "Join clicked ");

                if(allGroupNameList == null){
                    Toast.makeText(getApplicationContext(), "Select group to join", Toast.LENGTH_SHORT).show();
                }

                if(allGroupNameList != null) {
                    joinCheckedGroup(allGroupIDList, R.id.groupListView);
                }
            }
        });
    }

    //Make remove button and let user to remove groups
    private void makeRemoveBtn () {
        Button removeBtn = findViewById(R.id.SecRemoveBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Remove/Leave clicked", Toast.LENGTH_SHORT).show();
                Log.d("clicked Remove/Leave: ", "Remove/Leave clicked");

                if(leaderOfNameList == null && memberOfNameList == null){
                    Toast.makeText(getApplicationContext(), "Select group to Remove/Leave", Toast.LENGTH_SHORT).show();
                }

                if(leaderOfNameList != null) {
                    deleteCheckedGroup(leaderOfIDList, R.id.leaderGroups);
                }

                if(memberOfNameList != null) {
                    leaveCheckedGroup(memberOfIDList, R.id.memberGroups);
                }
            }
        });
    }

    //Make the listview and let user to see the group list, main on child monitor------
    private class setupListView extends AsyncTask<Void,Void,Void>{
        JSONArray original;
        @Override
        protected Void doInBackground(Void... voids) {
            original = currInstance.getGroupList(getApplicationContext());

            if (original != null) {
                Log.d("testing getgrouplist: ", original.toString());

                for (int i = 0; i < original.length(); i++) {
                    JSONObject childJSONObject = null;
                    try {
                        childJSONObject = original.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (childJSONObject != null) {
                            if (!childJSONObject.getString("groupDescription").equals("null")) {
                                allGroupNameList.add(childJSONObject.getString("groupDescription"));
                                allGroupIDList.add(childJSONObject.getInt("id"));

                                if (childJSONObject.getJSONObject("leader").getInt("id") == currInstance.getUserID()) {
                                    leaderOfNameList.add(childJSONObject.getString("groupDescription"));
                                    leaderOfIDList.add(childJSONObject.getInt("id"));
                                }

                                if (!(childJSONObject.get("memberUsers")).equals("null")) {
                                    JSONArray groupMembersList = childJSONObject.getJSONArray("memberUsers");
                                    for (int x = 0; x < groupMembersList.length(); x++) {
                                        JSONObject member = groupMembersList.getJSONObject(x);
                                        if (member.getInt("id") == currInstance.getUserID()) {
                                            memberOfNameList.add(childJSONObject.getString("groupDescription"));
                                            memberOfIDList.add(childJSONObject.getInt("id"));
                                        }
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("onpost: all list: ", allGroupNameList.toString());
                Log.d("onpost: allID list: ", allGroupIDList.toString());
                Log.d("onpost: leader list: ", leaderOfNameList.toString());
                Log.d("onpost: LeaderID list: ", leaderOfIDList.toString());
                Log.d("onpost: member list: ", memberOfNameList.toString());
                Log.d("onpost: memberID list: ", memberOfIDList.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            updateUI();
        }
    }

    //Uadate the UI when has groups
    private void updateUI(){
        setPickGroup(allGroupsList, allGroupNameList);
        setPickGroup(leaderOfList, leaderOfNameList);
        setPickGroup(memberOfList, memberOfNameList);
    }

    //Let users to pick up which groups-----
    private void setPickGroup(ListView list, ArrayList<String> names) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, names);
        list.setAdapter(adapter);
        list.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(adapter);
    }

    //Make join group method in the meantime
    private class joinGroup extends AsyncTask<ArrayList<Integer>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<Integer>... arrayLists) {
            ArrayList<Integer> list = arrayLists[0];
            for (int i = 0; i < list.size(); i++) {
                int id = list.get(i);
                Log.d("try to join group", " with ID: " + id);
                currInstance.addGroupMember(id, getApplicationContext());
                Log.d("joined group", " with ID: " + id);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
        }
    }

    //Make join group when you want to join in that
    private void joinCheckedGroup(ArrayList<Integer> groupIDs, Integer listID) {
        ListView displayList = findViewById(listID);
        SparseBooleanArray checked = displayList.getCheckedItemPositions();
        Log.d("boolarr: ", "array: " + checked);

        if (checked == null) {
            Toast.makeText(this, "Select group to join", Toast.LENGTH_SHORT).show();
        }

        ArrayList<Integer> checkedGroups = new ArrayList<>();
        for (int i = 0; i < groupIDs.size(); i++) {
            if (checked != null && checked.get(i)) {
                int id = groupIDs.get(i);
                Log.d("joinCheckGroup:", "curr id: " + id);
                checkedGroups.add(id);
            }
        }
        joinGroup joinChecked = new joinGroup();
        joinChecked.execute(checkedGroups);

        String joinedGorups = String.format(Locale.CANADA, "Attempted to join %d groups, %s\nPlease return to main menu to update lists", checkedGroups.size(), Arrays.toString(checkedGroups.toArray()));
        Toast.makeText(this, joinedGorups, Toast.LENGTH_SHORT).show();
    }

    //Make delete group method to delete group-----
    private class deleteGroup extends AsyncTask<ArrayList<Integer>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<Integer>... arrayLists) {
            ArrayList<Integer> list = arrayLists[0];
            for (int i = 0; i < list.size(); i++) {
                int id = list.get(i);
                currInstance.deleteGroup(id, getApplicationContext());
                Log.d("Deleted group", " with ID: " + id);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
        }
    }

    //make quit groups method--------
    private class leaveGroup extends AsyncTask<ArrayList<Integer>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<Integer>... arrayLists) {
            ArrayList<Integer> list = arrayLists[0];
            for (int i = 0; i < list.size(); i++) {
                int id = list.get(i);
                currInstance.removeGroupMember(id, getApplicationContext());
                Log.d("Left group", " with ID: " + id);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
        }
    }

    //make quit check method let user quit------
    private void leaveCheckedGroup(ArrayList<Integer> groupIDs, Integer listID) {
        ListView displayList = findViewById(listID);
        SparseBooleanArray checked = displayList.getCheckedItemPositions();
        Log.d("boolarr: ", "array: " + checked);
        if (checked == null) {
            Toast.makeText(this, "Select group to leave", Toast.LENGTH_SHORT).show();
        }
        ArrayList<Integer> checkedGroups = new ArrayList<>();
        for (int i = 0; i < groupIDs.size(); i++) {
            if (checked.get(i)) {
                int id = groupIDs.get(i);
                Log.d("leaveCheckedGroup:", "curr id: " + id);
                checkedGroups.add(id);
            }
        }
        if (!checkedGroups.isEmpty()) {
            leaveGroup leaveChecked = new leaveGroup();
            leaveChecked.execute(checkedGroups);

            String leftGroups = String.format(Locale.CANADA, "Attempted to leave %d groups, %s\nPlease return to main menu to update lists", checkedGroups.size(), Arrays.toString(checkedGroups.toArray()));
            Toast.makeText(this, leftGroups, Toast.LENGTH_SHORT).show();
        }
    }

    //make delete checked group methods---------
    private void deleteCheckedGroup(ArrayList<Integer> groupIDs, Integer listID) {
        ListView displayList = findViewById(listID);
        SparseBooleanArray checked = displayList.getCheckedItemPositions();
        Log.d("boolarr: ", "array: " + checked);
        if (checked == null) {
            Toast.makeText(this, "Select group to delete", Toast.LENGTH_SHORT).show();
        }
        ArrayList<Integer> checkedGroups = new ArrayList<>();
        for (int i = 0; i < groupIDs.size(); i++) {
            if (checked.get(i)) {
                int id = groupIDs.get(i);
                Log.d("deleteCheckGroup:", "curr id: " + id);
                checkedGroups.add(id);
            }
        }
        if (!checkedGroups.isEmpty()){
            deleteGroup deleteChecked = new deleteGroup();
            deleteChecked.execute(checkedGroups);

            String goneGroups = String.format(Locale.CANADA, "Attempted to delete %d groups, %s\n Please refresh page to update lists", checkedGroups.size(), Arrays.toString(checkedGroups.toArray()));
            Toast.makeText(this, goneGroups, Toast.LENGTH_SHORT).show();
        }
    }

    //set back button to back last activity--------------
    private void setupBackbtn() {
        Button btn = findViewById(R.id.MapSecondBackbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaderChecked.clear();
                memberChecked.clear();
                startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
            }
        });
    }
}

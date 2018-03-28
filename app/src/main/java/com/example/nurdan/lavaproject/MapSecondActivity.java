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
    setupListView test = new setupListView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void makeEditBtn() {
        Button editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Edit clicked", Toast.LENGTH_SHORT).show();
                Log.d("Edit clicked: ", "Edit clicked ");

                if(leaderOfNameList == null){
                    Toast.makeText(getApplicationContext(), "Select group to Edit", Toast.LENGTH_SHORT).show();
                }

                if(leaderOfNameList != null) {
                    SparseBooleanArray checked = leaderOfList.getCheckedItemPositions();
                    Log.d("boolarr: ", "array: " + checked);
                    if (checked == null) {
                        Toast.makeText(getApplicationContext(), "Select group to edit", Toast.LENGTH_SHORT).show();
                    }
                    if (checked.size() != 1) {
                        Toast.makeText(getApplicationContext(), "Please select only one group", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        for (int i = 0; i < leaderOfIDList.size(); i++) {
                            if (checked.get(i)) {
                                int id = leaderOfIDList.get(i);
                                Log.d("editing:", "editing curr id: " + id);
                                currInstance.setCurrGroupID(id);
                                startActivity(new Intent(getApplicationContext(), EditGroupActivity.class));
                            }
                        }
                    }
                }
            }
        });
    }

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

    private void makeRemoveBtn () {
        Button removeBtn = findViewById(R.id.SecRemoveBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Remove clicked", Toast.LENGTH_SHORT).show();
                Log.d("clicked remove: ", "remove clicked");

                if(leaderOfNameList == null){
                    Toast.makeText(getApplicationContext(), "Select group to remove", Toast.LENGTH_SHORT).show();
                }

                if(leaderOfNameList != null) {
                    deleteCheckedGroup(leaderOfIDList, R.id.leaderGroups);
                }
            }
        });
    }




    private class setupListView extends AsyncTask<Void,Void,Void>{
        JSONArray original;
        @Override
        protected Void doInBackground(Void... voids) {
            original = currInstance.getGroupList(getApplicationContext());
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
                        allGroupNameList.add(childJSONObject.getString("groupDescription"));
                        allGroupIDList.add(childJSONObject.getInt("id"));

                        if (childJSONObject.getJSONObject("leader").getInt("id") == currInstance.getUserID()){
                            leaderOfNameList.add(childJSONObject.getString("groupDescription"));
                            leaderOfIDList.add(childJSONObject.getInt("id"));
                        }

/*                        if (childJSONObject.getJSONArray("memberUsers").getJSONObject(currInstance.getUserID()).getInt("id") == currInstance.getUserID()){
                            memberOfNameList.add(childJSONObject.getString("groupDescription"));
                            memberOfIDList.add(childJSONObject.getInt("id"));
                        }*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("onpost: all list: ", allGroupNameList.toString());
            Log.d("onpost: allID list: ", allGroupIDList.toString());
            Log.d("onpost: leader list: ", leaderOfNameList.toString());
            Log.d("onpost: LeaderID list: ", leaderOfIDList.toString());
/*            Log.d("onpost: member list: ", memberOfNameList.toString());
            Log.d("onpost: memberID list: ", memberOfIDList.toString());*/
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setPickGroup(allGroupsList, allGroupNameList);
            setPickGroup(leaderOfList, leaderOfNameList);
            setPickGroup(memberOfList, memberOfNameList);
        }
    }

    private void setPickGroup(ListView list, ArrayList<String> names) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, names);
        list.setAdapter(adapter);
        list.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(adapter);
    }

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

    private void joinCheckedGroup(ArrayList<Integer> groupIDs, Integer listID) {
        ListView displayList = findViewById(listID);
        SparseBooleanArray checked = displayList.getCheckedItemPositions();
        Log.d("boolarr: ", "array: " + checked);
        if (checked == null) {
            Toast.makeText(this, "Select group to join", Toast.LENGTH_SHORT).show();
        }
        ArrayList<Integer> checkedGroups = new ArrayList<>();
        for (int i = 0; i < groupIDs.size(); i++) {
            if (checked.get(i)) {
                int id = groupIDs.get(i);
                Log.d("joinCheckGroup:", "curr id: " + id);
                checkedGroups.add(id);
            }
        }
        joinGroup joinChecked = new joinGroup();
        joinChecked.execute(checkedGroups);

        String joinedGorups = String.format(Locale.CANADA, "Joined %d groups, %s", checkedGroups.size(), Arrays.toString(checkedGroups.toArray()));
        Toast.makeText(this, joinedGorups, Toast.LENGTH_SHORT).show();
    }

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
        deleteGroup deleteChecked = new deleteGroup();
        deleteChecked.execute(checkedGroups);

        String goneGroups = String.format(Locale.CANADA, "Deleted %d groups, %s", checkedGroups.size(), Arrays.toString(checkedGroups.toArray()));
        Toast.makeText(this, goneGroups, Toast.LENGTH_SHORT).show();
    }

    private void setupBackbtn() {
        Button btn = findViewById(R.id.MapSecondBackbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

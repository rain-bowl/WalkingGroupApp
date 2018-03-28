package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ApplicationLogic.ProgramSingletonController;
import UIFragmentClasses.AddUserDialogFragment;

public class MapSecondActivity extends AppCompatActivity{
    private ListView groupList;
    private ListView leaderOfList;
    private ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
    private ArrayList<String> nameList = new ArrayList<>();
    private ArrayList<String> leaderList = new ArrayList<>();
    private ArrayList<Integer> groupIDList = new ArrayList<>();
    private ArrayList<Integer> groupLeaderIDList = new ArrayList<>();
    setupListView test = new setupListView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               setContentView(R.layout.activity_map_second);
        groupList = findViewById(R.id.groupListView);
        leaderOfList = findViewById(R.id.leaderGroups);

        setupBackbtn();
        makeJoinBtn();
        makeRemoveBtn ();
        test.execute();
    }

    private void makeRemoveBtn () {
        Button removeBtn = findViewById(R.id.SecRemoveBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Remove clicked", Toast.LENGTH_SHORT).show();
                Log.d("clicked remove: ", "remove clicked");

                if(leaderList == null){
                    Toast.makeText(getApplicationContext(), "Select group to remove", Toast.LENGTH_SHORT).show();
                }

                if(leaderList != null) {
                    deleteCheckedGroup(groupLeaderIDList, R.id.leaderGroups);
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

                if(nameList == null){
                    Toast.makeText(getApplicationContext(), "Select group to join", Toast.LENGTH_SHORT).show();
                }

                if(nameList != null) {
                    joinCheckedGroup(groupIDList, R.id.groupListView);
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
                        nameList.add(childJSONObject.getString("groupDescription"));
                        groupIDList.add(childJSONObject.getInt("id"));
                        if (childJSONObject.getJSONObject("leader").getInt("id") == currInstance.getUserID()){
                            leaderList.add(childJSONObject.getString("groupDescription"));
                            groupLeaderIDList.add(childJSONObject.getInt("id"));
                        }
                        // todo: get memberUsers
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("onpost: all list: ", nameList.toString());
            Log.d("onpost: allID list: ", groupIDList.toString());
            Log.d("onpost: leader list: ", leaderList.toString());
            Log.d("onpost: LeaderID list: ", groupLeaderIDList.toString());
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setPickGroup(groupList, nameList);
            setPickGroup(leaderOfList, leaderList);
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

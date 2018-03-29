package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ApplicationLogic.ProgramSingletonController;

public class ViewMembersActivity extends AppCompatActivity {
    ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
    ArrayList<String> membersNameList = new ArrayList<>();
    ArrayList<Integer> membersIDList = new ArrayList<>();
    ArrayList<String> leaderNameList = new ArrayList<>();
    ArrayList<Integer> leaderIDList = new ArrayList<>();
    ListView currMembers;
    int currGroupId;
    String currGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);
        setupBackBtn();
        currGroupId = currInstance.getCurrGroupID();
        currGroupName = currInstance.getCurrGroupName();

        currMembers = findViewById(R.id.memberList);
        TextView groupName = findViewById(R.id.currGroupName);
        groupName.setText(currGroupName);

        getMembers test2 = new getMembers();
        test2.execute();
    }

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
                    JSONObject leaderField = new JSONObject();

                    if (childJSONObject != null) {
                        membersNameList.add(childJSONObject.getString("name"));
                        membersIDList.add(childJSONObject.getInt("id"));
/*                        leaderField.getJSONObject("leader");
                        leaderIDList.add(leaderField.getInt("id"));*/
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

    private void setMemberView(ListView list, ArrayList<String> names) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, names);
        list.setAdapter(adapter);
    }

    private void setupBackBtn() {
        Button btn = findViewById(R.id.viewBackBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

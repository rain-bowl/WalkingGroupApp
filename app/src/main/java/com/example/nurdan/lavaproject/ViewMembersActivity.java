package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    String leaderName;
    int leaderID;
    ListView currMembers;
    int currGroupId;
    String currGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainMenuActivity.setPrefTheme(this);
        setContentView(R.layout.activity_view_members);
        setupBackBtn();
        currGroupId = currInstance.getCurrGroupID();
        currGroupName = currInstance.getCurrGroupName();

        currMembers = findViewById(R.id.memberList);
        TextView groupName = findViewById(R.id.currGroupName);
        groupName.setText(currGroupName);

        getMembers test2 = new getMembers();
        test2.execute();

        ListClick(currMembers);
    }

    private void ListClick (final ListView list) {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Object member = list.getItemAtPosition(position);
                Log.d("ListClick", "member: " + member);
                currInstance.setCurrMemberID(membersIDList.get(position));
                Log.d("ListClick", "memberID: " + membersIDList.get(position));
                startActivity(new Intent(getApplicationContext(), GroupMemberInfoActivity.class));
            }
        });
    }

    private class getMembers extends AsyncTask<Void,Void,Void> {
        JSONArray original;

        @Override
        protected Void doInBackground(Void... voids) {
            JSONObject currGroupDetails = currInstance.getGroupDetails(currGroupId, getApplicationContext());
            Log.d("getGroupDetails: ", currGroupDetails.toString());

            try {
                JSONObject leaderfield = currGroupDetails.getJSONObject("leader");
                Log.d("getGroupDetails: ", "leaderfield: " + leaderfield);
                leaderID = leaderfield.getInt("id");
                leaderName = (currInstance.getUserByID(leaderID, getApplicationContext())).getString("name");
                membersNameList.add(leaderName + " - Leader");
                membersIDList.add(leaderID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("currGroupDetails", "leaderName: " + leaderName);

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

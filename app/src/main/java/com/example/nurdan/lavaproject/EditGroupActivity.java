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

public class EditGroupActivity extends AppCompatActivity {
    ListView currMembers;
    ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
    int currGroupId;
    JSONObject currGroupDetails = new JSONObject();
    String currGroupName;
    String newGroupName;
    JSONArray latArr = new JSONArray();
    JSONArray lngArr = new JSONArray();
    ArrayList<String> membersNameList = new ArrayList<>();
    ArrayList<Integer> membersIDList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);
        currMembers = findViewById(R.id.membersListView);


        currGroupId = currInstance.getCurrGroupID();
        Log.d("currGroupId: ", "currGroupId = " + currGroupId);

        setupBackBtn();
        createSubmitBtn();

        editGroup test = new editGroup();
        test.execute();

        getMembers test2 = new getMembers();
        test2.execute();

        ListClick(currMembers);

  /*      submitEdit test2 = new submitEdit();
        test2.execute();*/
    }

    private class editGroup extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void ... voids) {
            currGroupDetails = currInstance.getGroupDetails(currGroupId, getApplicationContext());
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            try {
                currGroupName = currGroupDetails.getString("groupDescription");
                latArr = currGroupDetails.getJSONArray("routeLatArray");
                lngArr = currGroupDetails.getJSONArray("routeLngArray");
                Log.d("editGroupActivity: ", "currGroupName = " + currGroupName);
                Log.d("editGroupActivity: ", "latArr = " + latArr);
                Log.d("editGroupActivity: ", "lngArr = " + lngArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TextView edName = findViewById(R.id.editName);
            edName.setText(currGroupName);
        }
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


    //used previously, probably gonna delete
/*    private class submitEdit extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void ... voids) {
            currInstance.updateGroup(currGroupId, currGroupName, latArr, lngArr, getApplicationContext());
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            createSubmitBtn();
        }
    }*/

//todo: fix updategroup, returns error rn
    public void createSubmitBtn(){
        Button submitBtn = findViewById(R.id.submitBtn);
        final EditText newName = findViewById(R.id.editName);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGroupName = newName.getText().toString();
                Toast.makeText(getApplicationContext(), "newGroupName: " + newGroupName, Toast.LENGTH_LONG).show();
                currInstance.updateGroup(currGroupId, newGroupName, latArr, lngArr, getApplicationContext());
                startActivity(new Intent(getApplicationContext(), MapSecondActivity.class));
            }
        });
    }

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

package com.example.nurdan.lavaproject;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ApplicationLogic.ProgramSingletonController;

public class GroupMemberInfoActivity extends AppCompatActivity {
    ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
    int currMemberID;
    JSONObject currUserInfo;
    JSONObject monitorInfo;
    JSONArray monitoredByArray;
    String monitorValueInfo;
    ArrayList<Integer> monitoredByIDList = new ArrayList<>();
    String monitorName;
    String monitorEmail;
    String monitorCell;
    String monitorHome;
    ListView monitorInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_info);

        monitorInfoList = findViewById(R.id.monitorInfoList);
        setupBackBtn();

        currMemberID = currInstance.getCurrMemberID();
        Log.d("GroupMemberInfoActivity", "currMemberID: " + currMemberID);

        getMemberInfo test = new getMemberInfo();
        test.execute();

        //TODO: set information display for current member, who they're monitored by + their info
    }

    private void getContactInfo (int ID) {
        monitorName = getMonitorInfo("name", ID);
        monitorEmail = getMonitorInfo("email", ID);
        monitorCell = getMonitorInfo("cellPhone", ID);
        monitorHome = getMonitorInfo("homePhone", ID);
        Log.d("GroupMemberInfoActivity", "getContactInfo monitorName: " + monitorName);
        Log.d("GroupMemberInfoActivity", "getContactInfo monitorEmail: " + monitorEmail);
        Log.d("GroupMemberInfoActivity", "getContactInfo monitorCell: " + monitorCell);
        Log.d("GroupMemberInfoActivity", "getContactInfo monitorHome: " + monitorHome);
        String output = "name: " + monitorName+" email: "+monitorEmail+" cell: "+monitorCell+ " home: "+monitorHome;
        Log.d("GroupMemberInfoActivity", "getContactInfo output: " + output);
    }

    private String getMonitorInfo (String valueName, int ID) {
        monitorInfo = currInstance.getUserByID(ID, getApplicationContext());
        try {
            monitorValueInfo = monitorInfo.getString(valueName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return monitorValueInfo;
    }

// need to make it work rip, may change to dialog
/*    private void setMonitorView(ListView list, ArrayList<String> names, String detail) {
        ListAdapter adapter = new SimpleAdapter(this, names, android.R.layout.simple_list_item_2, new int[] {android.R.id.text1, android.R.id.text2});

        // Bind to our new adapter.
        setListAdapter(adapter);
    }*/

    private class getMemberInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            currUserInfo = currInstance.getUserByID(currMemberID, getApplicationContext());
            Log.d("GroupMemberInfoActivity", "currUserInfo: " + currUserInfo);
            try {
                monitoredByArray = currUserInfo.getJSONArray("monitoredByUsers");
                for (int i = 0; i < monitoredByArray.length(); i++){
                    int id = monitoredByArray.getJSONObject(i).getInt("id");
                    Log.d("GroupMemberInfoActivity", "monitoredByArray ID: " + id);
                    monitoredByIDList.add(id);
                    getContactInfo(id);
                }
                Log.d("GroupMemberInfoActivity", "monitoredByIDList IDs: " + monitoredByIDList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void setupBackBtn() {
        Button btn = findViewById(R.id.infoBackBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

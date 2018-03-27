package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ApplicationLogic.ProgramSingletonController;

public class MapSecondActivity extends AppCompatActivity {
    private ListView groupList;
    private ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
    private ArrayList<String> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_second);
        groupList = findViewById(R.id.groupListView);

        setupBackbtn();

        setupListView Test = new setupListView();
        Test.execute();
    }


    //may need the data from the server to inside list to view how many groups in there
    //may change to radiogroup for easier access to join / delete
    //todo: implement async properly
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("onpost: test list: ", nameList.toString());
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setPickGroup();
        }
    }

    private void setPickGroup() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, nameList);
        groupList.setAdapter(adapter);
        groupList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        groupList.setAdapter(adapter);
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

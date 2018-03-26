package com.example.nurdan.lavaproject;

import android.content.Context;
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
    private List<String> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_second);
        groupList = findViewById(R.id.groupListView);

        setupBackbtn();

        setupListView Test = new setupListView();
        Test.execute();
        setPickGroup();
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
            return null;
       }
        @Override
        protected void onPostExecute(Void aVoid) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, nameList);
            groupList.setAdapter(adapter);
        }
    }

    private void setPickGroup() {
        groupList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                Toast.makeText(getApplicationContext(), "delete item in position : " + arg2, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
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

    public static Intent makeIntent(Context context) {
        return new Intent(context,MapSecondActivity.class);
    }

}

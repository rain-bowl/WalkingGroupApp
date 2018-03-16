package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
        setupBackbtn();
        setupListView();
    }

    //may need the data from the server to inside list to view how many groups in there
    private void setupListView(){
        groupList = findViewById(R.id.groupListView);

        JSONArray original = currInstance.getGroupList(getApplicationContext());

        if (original == null) {
            original = new JSONArray();
        }

        for (int i = 0; i < original.length(); i++) {
            JSONObject childJSONObject = null;
            try {
                childJSONObject = original.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                nameList.add(childJSONObject.getString("groupDescription"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, nameList);
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

    public static Intent makeIntent(Context context) {
        return new Intent(context,MapSecondActivity.class);
    }

}

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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ApplicationLogic.ProgramSingletonController;

public class MapSecondActivity extends AppCompatActivity {
    private ListView groupList;
    private ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_second);
        setupBackbtn();
//        setupListView();
    }

    //may need the data from the server to inside list to view how many groups in there
    private void setupListView(){
        groupList = findViewById(R.id.groupListview);


        JSONArray jArr = currInstance.getGroupList(this);
       // String output = null;
      /*  try {
            output = jArr.getString("group description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        int length = jArr .length();
        ArrayList<String> groupList = new ArrayList<>();
     //   groupList.add(output);

    //    Toast.makeText(this, output, Toast.LENGTH_LONG).show();
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

package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import ApplicationLogic.ProgramSingletonController;

public class GroupActivity extends AppCompatActivity {
    private ProgramSingletonController localInstance;
    private String groupName;
    private int leaderID;
    private LatLng point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        localInstance = ProgramSingletonController.getCurrInstance();
        leaderID = localInstance.getUserID();
        // todo: fix returnlatlng
        point = localInstance.returnLatLng(getApplicationContext());
        String input = point.toString();
        Toast.makeText(this, input, Toast.LENGTH_LONG).show();

        if (point == null) {
            point = new LatLng(0, 0);
        }
        createBtn();
    }

    public void createBtn(){
        Button createButton = findViewById(R.id.create_btn);

        final EditText group = findViewById(R.id.groupName);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localInstance = ProgramSingletonController.getCurrInstance();
                groupName = group.getText().toString();
                localInstance.createNewGroup(groupName, leaderID, point, getApplicationContext());
                finish();
            }
        });
    }
}

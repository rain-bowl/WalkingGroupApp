package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ApplicationLogic.ProgramSingletonController;

public class GroupActivity extends AppCompatActivity {
    private ProgramSingletonController localInstance;
    private String groupName;
    private int leaderID;
    private List<LatLng> startEnd;
    private LatLng start;
    private LatLng end;
    private String bearerToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        localInstance = ProgramSingletonController.getCurrInstance();
        leaderID = localInstance.getUserID(); //.getCurrUserID(getApplicationContext());
        bearerToken = localInstance.getBearerToken(); //.getBearerToken(getApplicationContext());

        startEnd = localInstance.returnLatLng(getApplicationContext());
        if (startEnd == null) {
            start = new LatLng(0, 0);
            end = new LatLng(0, 0);
        }
        start = startEnd.get(0);
        end = startEnd.get(1);

        String startLatLng = start.toString();
        Toast.makeText(this, "start:" + startLatLng, Toast.LENGTH_LONG).show();

        String endLatLng = end.toString();
        Toast.makeText(this, "end:" + endLatLng, Toast.LENGTH_LONG).show();

        createBtn();
        setupBackbtn();
    }

    public void createBtn(){
        Button createButton = findViewById(R.id.create_btn);
        final EditText group = findViewById(R.id.groupName);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localInstance = ProgramSingletonController.getCurrInstance();
                groupName = group.getText().toString();
                Toast.makeText(getApplicationContext(), groupName + "BearerToken: " + bearerToken, Toast.LENGTH_LONG).show();
                localInstance.createNewGroup(bearerToken, groupName, leaderID, start, end, getApplicationContext());
                startActivity(new Intent(getApplicationContext(), MapSecondActivity.class));
            }
        });
    }

    private void setupBackbtn() {
        Button btn = findViewById(R.id.back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

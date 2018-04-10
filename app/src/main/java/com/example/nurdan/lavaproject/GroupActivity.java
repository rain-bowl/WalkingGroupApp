package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ApplicationLogic.ProgramSingletonController;

public class GroupActivity extends AppCompatActivity {
    private ProgramSingletonController localInstance;
    private String groupName;
    private List<LatLng> startEnd;
    private LatLng start;
    private LatLng end;
    private String startLatLng;
    private String endLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainMenuActivity.setPrefTheme(this);
        setContentView(R.layout.activity_group);

        localInstance = ProgramSingletonController.getCurrInstance();
        startEnd = localInstance.returnLatLng(getApplicationContext());
        if (startEnd.size() != 2) {
            Toast.makeText(this, "Please reselect coordinates.", Toast.LENGTH_LONG).show();
            startEnd.clear();
            finish();
        }
        else{
            start = startEnd.get(0);
            end = startEnd.get(1);
        }

        if (start != null && end != null){
            startLatLng = start.toString();
            endLatLng = end.toString();
        }

        createBtn();
        setupBackbtn();
        setUpCoord();
    }

    private void setUpCoord(){
        final TextView startpt = findViewById(R.id.startCoord);
        final TextView endpt = findViewById(R.id.endCoord);


        startpt.setText(startLatLng);
        endpt.setText(endLatLng);
    }

    public void createBtn(){
        Button createButton = findViewById(R.id.create_btn);
        final EditText group = findViewById(R.id.groupName);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localInstance = ProgramSingletonController.getCurrInstance();
                groupName = group.getText().toString();
                Toast.makeText(getApplicationContext(), groupName, Toast.LENGTH_LONG).show();
                localInstance.createNewGroup(groupName, start, end, getApplicationContext());
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

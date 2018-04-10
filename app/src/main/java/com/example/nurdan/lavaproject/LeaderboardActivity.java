package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;

import java.util.ArrayList;

import ApplicationLogic.ProgramSingletonController;

public class LeaderboardActivity extends AppCompatActivity {
    private ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
    private JSONArray userlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getUsers test = new getUsers();
        test.execute();
        Log.d("LeaderboardActivity", "list of all users: " + userlist);

        makeBackBtn();
    }

    private class getUsers extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            userlist = currInstance.getAllUsers(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

        }
    }

    // set up leaderboard listview
    private void populateLeaderboard(ListView list, ArrayList<String> names){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, names);
        list.setAdapter(adapter);
    }

    private void makeBackBtn(){
        Button back = findViewById(R.id.lb_back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
            }
        });
    }
}

package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ApplicationLogic.LeaderboardMember;
import ApplicationLogic.ProgramSingletonController;

public class LeaderboardActivity extends AppCompatActivity {
    private ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();
    private JSONArray userlist = new JSONArray();
    private int leaderboardSize;
    private ArrayList<LeaderboardMember> userAccount = new ArrayList<>();
    private ArrayList<String> userNames = new ArrayList<>();
    private ListView leaderboardDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        leaderboardDisplay = findViewById(R.id.leaderboard);

        // retrieves list of all users to use
        getUsers getAllUsers = new getUsers();
        getAllUsers.execute();

        makeBackBtn();
    }

    // retrieves list of all users
    private class getUsers extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            userlist = currInstance.getAllUsers(getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (userlist != null) {
                if(userlist.length() < 100){
                    leaderboardSize = userlist.length();
                }
                else{
                    leaderboardSize = 100;
                }
                try{
                    //Iterate through all users to retrieve their information
                    for (int i = 0; i < leaderboardSize; i++){
                        JSONObject memberJSON = userlist.getJSONObject(i);
                        //Create a new member and populate fields
                        LeaderboardMember member = new LeaderboardMember();
                        String[] nameParts = memberJSON.getString("name").split("\\s");
                        member.setFirstName(nameParts[0]);
                        //check if last name exists
                        if (nameParts.length == 2){
                            member.setLastName(nameParts[1]);
                        }
                        member.setUserPoints(memberJSON.getInt("totalPointsEarned"));
                        //Add to the user list
                        userAccount.add(member);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                populateLeaderboard(leaderboardDisplay);
            }
        }
    }

    // set up leaderboard listview
    private void populateLeaderboard(ListView list){
        //Sort the arraylist of all users
        Collections.sort(userAccount, new Comparator<LeaderboardMember>() {
            @Override
            public int compare(LeaderboardMember o1, LeaderboardMember o2) {
                return Integer.valueOf(o1.getUserPoints()).compareTo(Integer.valueOf(o2.getUserPoints()));
            }
        });

        Collections.reverse(userAccount);
        //Add some titles for 1, 2 and 3rd places
        for(int i = 0; i < userAccount.size(); i++){
            // create "listing" variable, a string that contains the entry for listview
            String listing = "#" + (i+1);
            // set titles for top 3 users
            if (i+1 == 1){
                listing += "   -   Generally General";
            }
            if (i+1 == 2){
                listing += "   -   Colonel Kernel";
            }
            if (i+1 == 3){
                listing += "   -   Captain Crunch";
            }
            listing += "\n            " + userAccount.get(i).getFirstName();
            if (userAccount.get(i).getLastName() != null) {
                listing += " " + userAccount.get(i).getLastName().charAt(0);
            }
            listing += "\n            " + userAccount.get(i).getUserPoints() + " points";
            userNames.add(listing);
        }

        // put entries into listview
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, userNames);
        list.setAdapter(adapter);
    }
    //Create back button for navigation
    private void makeBackBtn(){
        Button back = findViewById(R.id.lb_back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // can safely finish activity since there is no direct way to get to it
                finish();
            }
        });
    }
}

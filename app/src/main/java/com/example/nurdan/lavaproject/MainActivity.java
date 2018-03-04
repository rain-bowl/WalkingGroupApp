package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLoggedIn();
    }

    private void checkLoggedIn() {
        SharedPreferences savedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Boolean isLogged = savedPrefs.getBoolean("isLogged", false);

        if(isLogged) {
            Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT);
        } else {
            Intent loginAct = new Intent(this, LoginActivity.class);
            startActivity(loginAct);
        }
    }
}

package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    //    checkLoggedIn();
    }

    private void checkLoggedIn() {
        SharedPreferences savedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Boolean isLogged = savedPrefs.getBoolean("isLogged", false);

        if(isLogged) {
            //this doesn't actually show anything?? maybe fix
            Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
        } else {
            Intent loginAct = new Intent(this, LoginActivity.class);
            startActivity(loginAct);
        }
    }
}

package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ApplicationLogic.ProgramSingletonController;

public class MapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    private static final String TAG = MapActivity.class.getSimpleName();
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private boolean mLocationPermissionGranted = false;
    private static final int DEFAULT_ZOOM = 18;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private final LatLng mDefaultLocation = new LatLng(0, 0);
    private LatLng destinationCoord = new LatLng(0, 0);
    private Location mLastKnownLocation;
    ArrayList<LatLng> MarkerPoints;

    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<LatLng> groupStartPoints = new ArrayList<>();
    ArrayList<LatLng> groupEndPoints = new ArrayList<>();
    ArrayList<Double> latArray = new ArrayList<>();
    ArrayList<Double> lngArray = new ArrayList<>();
    ArrayList<String> monitoredNames = new ArrayList<>();
    ArrayList<LatLng> monitoredLatLng = new ArrayList<>();
    ArrayList<String> monitoredTime = new ArrayList<>();

    private ProgramSingletonController localInstance = ProgramSingletonController.getCurrInstance();
    private boolean arrivalFlag = true;
    JSONObject currGPS = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MarkerPoints = new ArrayList<>();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();
        initializeMapFrag();
        makeListBtn();
        makeCreateBtn();
        setupBackBtn();
        setOnOffBtn();

        getGroupList test = new getGroupList();
        test.execute();

        Toast.makeText(getApplicationContext(), R.string.mapIntroText, Toast.LENGTH_LONG).show();

        if (!arrivalFlag){
            handleGPS();
        }
    }

    private void arrivalCheck() {
        if (monitoredNames.size() != 0 && monitoredLatLng.size() != 0){
            for (int i = 0; i < monitoredNames.size(); i++){
                LatLng currMonitoredCoord = monitoredLatLng.get(i);
                if (currMonitoredCoord.latitude - destinationCoord.latitude <= +- 0.001 && currMonitoredCoord.longitude - destinationCoord.longitude <= +- 0.001){
                    Handler handler = new Handler();
                    Runnable runnableCode = new Runnable() {
                        @Override
                        public void run() {
                            arrivalFlag = true;
                        }
                    };
                    handler.postDelayed(runnableCode, 600000);
                    handler.post(runnableCode);
                }
            }
        }

        // add points to the user
        rewardUserXP addXP = new rewardUserXP();
        addXP.execute(MapActivity.this);
    }

    private void setOnOffBtn(){
        Button list = findViewById(R.id.onOffBtn);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrivalFlag){
                    if (destinationCoord == null || destinationCoord == mDefaultLocation){
                        Toast.makeText(getApplicationContext(), "Please click the marker of the group that you wish to track first.", Toast.LENGTH_LONG).show();
                    }
                    else {
                        arrivalFlag = false;
                        Toast.makeText(getApplicationContext(), "GPS Tracking ON, monitoring enabled", Toast.LENGTH_LONG).show();
                        handleGPS();
                    }
                }
                else {
                    arrivalFlag = true;
                    Toast.makeText(getApplicationContext(), "GPS Tracking OFF, monitoring disabled", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void handleGPS() {
        if (!arrivalFlag){
            Handler handler = new Handler();
            Runnable runnableCode = new Runnable() {
                @Override
                public void run() {
                    setGPS test2 = new setGPS();
                    test2.execute();
                    Log.d("Handlers", "Called on main thread");
                    arrivalCheck();
                    createMonitorMarkers();
                }
            };
            handler.postDelayed(runnableCode, 30000);
            handler.post(runnableCode);
        }
    }

    private void makeListBtn () {
        Button list = findViewById(R.id.listGroupBtn);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapSecondActivity.class));
            }
        });
    }

    private void makeCreateBtn () {
        Button list = findViewById(R.id.createGroupBtn);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MarkerPoints.size() == 2) {
                    startActivity(new Intent(getApplicationContext(), GroupActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please select start and destination", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setupBackBtn() {
        Button btn = findViewById(R.id.mapBackBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
            }
        });
    }

    private void initializeMapFrag() {
        if (mLocationPermissionGranted) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    @Override
    public void onMapClick(LatLng point) {
        if (MarkerPoints.size() > 2) {
            MarkerPoints.clear();
            mMap.clear();
            createGroupMarkers();
        }

        MarkerPoints.add(point);
        MarkerOptions options = new MarkerOptions();
        options.position(point);

        if (MarkerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(options.title("Tap 'Create Group' to create: Blue = Start"));
            LatLng origin = MarkerPoints.get(0);
            localInstance.inputLatLng(origin, this);
        }

        else if (MarkerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(options.title("Tap 'Create Group' to create: Green = End"));
            LatLng dest = MarkerPoints.get(1);
            localInstance.inputLatLng(dest, this);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        LatLng markerTag = (LatLng) marker.getTag();
        if (marker.getTag() != null && markerTag != mDefaultLocation){
            destinationCoord = markerTag;
            Log.d("onMarkerClick", "destinationCoord: " + destinationCoord);
        }
        else if (markerTag == mDefaultLocation){
            Toast.makeText(this, "To refresh date of marker, click the info window.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.getTag() != mDefaultLocation){
            Toast.makeText(this, "To join group, click 'View Groups'.", Toast.LENGTH_LONG).show();
        }
        mMap.clear();
        createGroupMarkers();
        createMonitorMarkers();
    }

    // setgps for curr user, get gps for monitored users
    private class setGPS extends AsyncTask<Void, Void, Void> {
        JSONArray retrievedMonitoredUsers;
        ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();

        @Override
        protected Void doInBackground(Void... voids) {
            retrievedMonitoredUsers = currInstance.getMonitoredUsersJSONArray(getApplicationContext());
            Log.d("getMonitoredUsersJSARR:", retrievedMonitoredUsers.toString());

            for (int i = 0; i < retrievedMonitoredUsers.length(); i++) {
                JSONObject childJSONObject = null;
                try {
                    childJSONObject = retrievedMonitoredUsers.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (childJSONObject != null) {
                        double lat;
                        double lng;
                        monitoredNames.add(childJSONObject.getString("name"));
                        lat = childJSONObject.getJSONObject("lastGpsLocation").getDouble("lat");
                        lng = childJSONObject.getJSONObject("lastGpsLocation").getDouble("lng");
                        monitoredLatLng.add(new LatLng(lat, lng));
                        monitoredTime.add(childJSONObject.getJSONObject("lastGpsLocation").getString("timestamp"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("getMonitoredUsersJSARR", "monitoredNames" + monitoredNames.toString());
            Log.d("getMonitoredUsersJSARR", "monitoredLatLng" + monitoredLatLng.toString());
            Log.d("getMonitoredUsersJSARR", "monitoredTime" + monitoredTime.toString());

            // set/get lastgps

            getDeviceLocation();
            if (mLastKnownLocation != null) {
                Log.d("test setLastGpsLocation", mLastKnownLocation.toString());
                localInstance.setLastGpsLocation(mLastKnownLocation, getApplicationContext());

                currGPS = localInstance.getLastGpsLocation(localInstance.getUserID(), getApplicationContext());
                Log.d("test getLastGpsLocation", "curr gps of " + localInstance.getUserID() + ": " + currGPS.toString());
            }
            if(mLastKnownLocation == null)
            {
                Log.d("setLastGpsLocation", "mLastKnownLocation is null");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mLocationPermissionGranted) {
                createMonitorMarkers();
            }
        }
    }

    private class getGroupList extends AsyncTask<Void,Void,Void> {
        JSONArray original;
        ProgramSingletonController currInstance = ProgramSingletonController.getCurrInstance();

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
                        if (!childJSONObject.getString("groupDescription").equals("null")){
                            nameList.add(childJSONObject.getString("groupDescription"));
                            if (childJSONObject.getJSONArray("routeLatArray").length() != 0){
                                latArray.add(childJSONObject.getJSONArray("routeLatArray").getDouble(0));
                                lngArray.add(childJSONObject.getJSONArray("routeLngArray").getDouble(0));
                                latArray.add(childJSONObject.getJSONArray("routeLatArray").getDouble(1));
                                lngArray.add(childJSONObject.getJSONArray("routeLngArray").getDouble(1));
                            }
                            else {
                                latArray.add(0.0);
                                lngArray.add(0.0);
                                latArray.add(0.0);
                                lngArray.add(0.0);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d("onpost: test name: ", nameList.toString());
            Log.d("onpost: lat points: ", latArray.toString());
            Log.d("onpost: lng points: ", lngArray.toString());

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mLocationPermissionGranted) {
                createGroupMarkers();
            }
        }
    }

    private void createGroupMarkers(){
        BitmapDescriptor colour = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);

        for (int i = 0; i < nameList.size() * 2; i += 2){
            try {
                groupStartPoints.add(new LatLng(latArray.get(i), lngArray.get(i)));
                groupEndPoints.add(new LatLng(latArray.get(i + 1), lngArray.get(i + 1)));

                Log.d(TAG, "createGroupMarkers, groupStartPoints: " + groupStartPoints);
                Log.d(TAG, "createGroupMarkers, groupEndPoints: " + groupEndPoints);

                makeMarker(groupStartPoints.get(i/2), groupEndPoints.get(i/2),"Group: ", nameList.get(i/2), colour);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "createGroupMarkers, done");
    }

    private long convertTimeStamp(String timestamp){
        long millis = 0;
        long currmill = 0;
        long res = 0;
        try {
            millis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CANADA).parse(timestamp).getTime();
            currmill = new Date().getTime();
            res = currmill - millis;
            res = res/1000;
            Log.d(TAG, "time since last monitor update: " + res);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void createMonitorMarkers(){
        if (monitoredNames.size() != 0 && monitoredLatLng.size() != 0 && monitoredTime.size() != 0){
            for (int i = 0; i < monitoredNames.size(); i++){
                BitmapDescriptor colour = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                makeMarker(monitoredLatLng.get(i), mDefaultLocation, "Monitoring user: " + monitoredNames.get(i), ", " + convertTimeStamp(monitoredTime.get(i)) + " seconds ago.", colour);
                Log.d(TAG, "createMonitorMarkers, created for user: " + monitoredNames.get(i) + " at: " + monitoredLatLng.get(i) + " at time: " + convertTimeStamp(monitoredTime.get(i)));
            }
        }
    }

    private void makeMarker(LatLng start, LatLng end, String markerTitle, String markerName, BitmapDescriptor colour){
        MarkerOptions option = new MarkerOptions();
        option.position(start);
        option.title(markerTitle + markerName);
        option.icon(colour);
        option.alpha(0.7f);
        mMap.addMarker(option).setTag(end);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
                initializeMapFrag();
                updateLocationUI();
            }
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            Log.d(TAG, "mLastKnownLocation: " + mLastKnownLocation);
                            if (mLastKnownLocation == null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mDefaultLocation.latitude,
                                                mDefaultLocation.longitude), DEFAULT_ZOOM));
                            }
                            else {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private class rewardUserXP extends AsyncTask<Context,Void,Void> {
        @Override
        protected Void doInBackground(Context... contexts) {
            Context context = contexts[0];
            localInstance = ProgramSingletonController.getCurrInstance();
            localInstance.rewardXP(context);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

        }
    }
}

package com.example.nurdan.lavaproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ApplicationLogic.AccountApiInteractions;
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
    private Location mLastKnownLocation;
    ArrayList<LatLng> MarkerPoints;

    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<LatLng> groupPoints = new ArrayList<>();
    ArrayList<Double> latArray = new ArrayList<>();
    ArrayList<Double> lngArray = new ArrayList<>();
    private ProgramSingletonController localInstance = ProgramSingletonController.getCurrInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MarkerPoints = new ArrayList<>();

        getLocationPermission();
        initializeMapFrag();
        makeListBtn();
        makeCreateBtn();
        getGroupList test = new getGroupList();
        test.execute();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
    public boolean onMarkerClick(final Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "To join, click 'View Groups'.", Toast.LENGTH_LONG).show();
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
                        nameList.add(childJSONObject.getString("groupDescription"));
                        latArray.add(childJSONObject.getJSONArray("routeLatArray").getDouble(0));
                        lngArray.add(childJSONObject.getJSONArray("routeLngArray").getDouble(0));
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
        for (int i = 0; i < nameList.size()-1; i++){
            try {
                groupPoints.add(new LatLng(latArray.get(i), lngArray.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (nameList.get(i) != null && groupPoints.get(i) != null) {
                makeMarker(groupPoints.get(i), nameList.get(i));
            }
        }
    }

    private void makeMarker(LatLng point, String markerName){
        MarkerOptions option = new MarkerOptions();
        option.position(point);
        option.title("Group: " + markerName);
        option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        option.alpha(0.7f);
        mMap.addMarker(option);
    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
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
                            if (mLastKnownLocation == null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mDefaultLocation.latitude,
                                                mDefaultLocation.longitude), DEFAULT_ZOOM));
                            }
                            else {
                                localInstance.setLastGpsLocation(mLastKnownLocation, getApplicationContext());
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
}

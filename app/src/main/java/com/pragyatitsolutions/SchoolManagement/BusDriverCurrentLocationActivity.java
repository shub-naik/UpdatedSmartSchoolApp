package com.pragyatitsolutions.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class BusDriverCurrentLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int LocationPermission = 10;
    private final int GOOGLE_PLAY_SERVICES_ERROR_CODE = 1000;
    private final int GPS_ENABLED_CODE = 2000;
    private FrameLayout googleMapsFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap MGoogleMap;
    private LocationCallback locationCallback;
    private ArrayList<Marker> MarkerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_driver_current_location);
        googleMapsFragment = findViewById(R.id.BusDriverGoogleMapFrameLayout);

        getSupportActionBar().setTitle("Pragyat IT Solutions");

        // For Removing Old Markers
        MarkerList = new ArrayList<>();

        final String DriverPhone = getIntent().getStringExtra("DriverPhoneNumber");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                MarkerOptions markerOptions = new MarkerOptions()
                        .title("Current Position")
                        .position(new LatLng(latitude, longitude));
                Marker driver_marker = MGoogleMap.addMarker(markerOptions);
                MarkerList.add(driver_marker);

                LatLng latLng = new LatLng(latitude, longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                MGoogleMap.moveCamera(cameraUpdate);
                MGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DriversLocationUpdates");

                // Hash Map for Storing Drivers Location Updates.
                HashMap<String, Object> map = new HashMap<>();
                map.put("Driver Phone", DriverPhone);
                map.put("Latitude", latitude);
                map.put("Longitude", longitude);

                ref.child(DriverPhone).setValue(map);
            }
        };

        // For Checking Whether The Play Services Are Available Or Not
        initGoogleMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MGoogleMap = googleMap;
        getLocationUpdates();
    }

    private void initGoogleMap() {
        if (isServiceOk()) {
            if (isGPSEnabled()) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LocationPermission);
                    }
                } else {
                    SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().add(R.id.BusDriverGoogleMapFrameLayout, supportMapFragment).commit();
                    supportMapFragment.getMapAsync(this);
                }
            }

        }
    }

    public boolean isServiceOk() {
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int result = googleApi.isGooglePlayServicesAvailable(this); // returns 0 if success
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApi.isUserResolvableError(result)) {
            Dialog dialog = googleApi.getErrorDialog(this, result, GOOGLE_PLAY_SERVICES_ERROR_CODE, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(BusDriverCurrentLocationActivity.this, "Dialog is Cancelled By User", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        } else {
            Toast.makeText(this, "Play Services Must Be Present On Your System", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (GPSEnabled) {
            return true;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permissions")
                    .setMessage("GPS is Disabled, Please Enable it for getting the Required Service")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_ENABLED_CODE);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LocationPermission && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initGoogleMap();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(2000);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_ENABLED_CODE) {
            if (isGPSEnabled()) {
                initGoogleMap();
            }
        }
    }

}
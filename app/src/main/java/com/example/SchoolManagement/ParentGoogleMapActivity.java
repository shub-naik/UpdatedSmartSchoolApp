package com.example.SchoolManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ParentGoogleMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int GOOGLE_PLAY_SERVICES_ERROR_CODE = 1000;
    private GoogleMap MGoogleMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private ArrayList<Marker> MarkerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_google_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        FrameLayout frameLayout = findViewById(R.id.ParentGoogleMapFrameLayout);
        // For Checking Whether The Play Services Are Available Or Not
        initGoogleMap();

        MarkerList = new ArrayList<>();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DriversLocationUpdates");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (int i = 0; i < MarkerList.size(); i++) {
                            Marker remove_marker = MarkerList.get(i);
                            remove_marker.remove();
                        }

                        for (DataSnapshot d1 : dataSnapshot.getChildren()) {

                            HashMap<String, Object> map = (HashMap<String, Object>) d1.getValue();
                            Double latitude = (Double) map.get("Latitude");
                            Double longitude = (Double) map.get("Longitude");
                            String DriverPhone = (String) map.get("Driver Phone");

                            String address = "";
                            Geocoder geocoder = new Geocoder(ParentGoogleMapActivity.this, Locale.getDefault());

                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses != null) {
                                    Address address1 = addresses.get(0);
                                    StringBuilder stringBuilder = new StringBuilder("");

                                    for (int i = 0; i <= address1.getMaxAddressLineIndex(); i++) {
                                        stringBuilder.append(address1.getAddressLine(i)).append("\n");
                                    }
                                    address = stringBuilder.toString();

                                    MarkerOptions markerOptions = new MarkerOptions()
                                            .title(address)
                                            .position(new LatLng(latitude, longitude));
                                    Marker driver_marker = MGoogleMap.addMarker(markerOptions);
                                    MarkerList.add(driver_marker);

                                    LatLng latLng = new LatLng(latitude, longitude);
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                                    MGoogleMap.moveCamera(cameraUpdate);
                                    MGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                                }
                            } catch (IOException e) {
                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }

        ;
    }

    private void initGoogleMap() {
        if (isServiceOk()) {
            SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.ParentGoogleMapFrameLayout, supportMapFragment).commit();
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MGoogleMap = googleMap;
        getDriversLocationUpdates();
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
                    Toast.makeText(ParentGoogleMapActivity.this, "Dialog is Cancelled By User", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        } else {
            Toast.makeText(this, "Play Services Must Be Present On Your System", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    private void getDriversLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(2000);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
}

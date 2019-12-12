package com.example.firebaseapplication;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    final private int REQUEST_COARSE_ACCESS = 123;
    boolean permissionGranted = false;

    private GoogleMap mMap;
    LocationManager lm;
    LocationListener locationListener;
    private double latitide, longitude;
    private int PROXIMITY_RADIUS = 10000;
    private  final int REQUEST_FINE_LOCATION = 1234;
    private static final int Request_User_Location_Code = 99;
    private View view;
    private long lastUpdateTime;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new  String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);

    }

    public void onClick(View view) {
        Object transferData[] = new Object[2];
        GetNearbyRestaurants getNearbyRestaurants = new GetNearbyRestaurants();


        switch (view.getId()) {
            case R.id.get_nearby_restaurants:
                locationGet();
                break;
        }
    }

    private void locationGet() {

        String restaurant = "restaurant";
        Object transferData[] = new Object[2];
        GetNearbyRestaurants getNearbyRestaurants = new GetNearbyRestaurants();

        mMap.clear();
        String url = getUrl(latitide, longitude, restaurant);
        transferData[0] = mMap;
        transferData[1] = url;

        getNearbyRestaurants.execute(transferData);
        Toast.makeText(this, "Searching for nearby restaurants...", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Restaurants now showing", Toast.LENGTH_SHORT).show();

    }

    private String getUrl(double latitide, double longitude, String nearbyPlace) {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitide + "," + longitude);
        googleURL.append("&radius=" + PROXIMITY_RADIUS);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&keyword=restaurant");
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + "AIzaSyCxVAiUe5rBz8OnMfEVJKbVLVcKZ4EenhI");
        Log.d("GoogleMapsActivity", "url = " + googleURL.toString());
        return googleURL.toString();

    }



    @Override
    public void onPause(){
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            },REQUEST_COARSE_ACCESS);
            return;
        } else {
            permissionGranted = true;
        }
        if (permissionGranted) {
            lm.removeUpdates(locationListener);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Nearby Restaurant.");
                builder.setMessage("Name: " + marker.getTitle());

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LatLng latLng = marker.getPosition();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            },REQUEST_COARSE_ACCESS);
            return;
        } else {
            permissionGranted = true;
        }
        if (permissionGranted) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case REQUEST_COARSE_ACCESS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionGranted = true;
                }else {
                    permissionGranted = false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private class MyLocationListener implements LocationListener{
        public void onLocationChanged(Location location) {
            if (location != null) {
                LatLng p = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(p).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 12.0f));
                latitide = location.getLatitude();
                longitude = location.getLongitude();
            }

        }
        public void onProviderDisabled(String provider){
        }

        public void onProviderEnabled(String provider){
        }

        public void onStatusChanged(String provider, int status, Bundle extras){
        }
    }
}
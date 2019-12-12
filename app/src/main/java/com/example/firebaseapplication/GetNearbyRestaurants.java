package com.example.firebaseapplication;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyRestaurants extends AsyncTask<Object, String, String> {
    private String googlePlaceData, Url;
    private GoogleMap mMap;



    @Override
    protected String doInBackground(Object... object)
    {
        mMap = (GoogleMap) object[0];
        Url = (String) object[1];

        DownloadURL urlDownload = new DownloadURL();
        try {
            googlePlaceData = urlDownload.readUrl(Url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return googlePlaceData;
    }

    @Override
    protected void onPostExecute(String s)
    {
        List<HashMap<String, String>> nearByPlaceList = null;
        DataParser dataParser = new DataParser();
        nearByPlaceList = dataParser.parse(s);

        DisplayNearbyPlaces(nearByPlaceList);
    }

    private void DisplayNearbyPlaces (List<HashMap<String, String>> nearByPlaceList)
    {
        for (int i=0; i<nearByPlaceList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String, String> googleNearbygyms = nearByPlaceList.get(i);
            String nameOfPlace = googleNearbygyms.get("place_name");
            String vicinity = googleNearbygyms.get("vicinity");
            double lat = Double.parseDouble(googleNearbygyms.get("lat"));
            double lng = Double.parseDouble(googleNearbygyms.get("lng"));

            LatLng latLng = new LatLng(lat, lng);

            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


        }
    }
}

package com.example.firebaseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

public class WeatherActivity extends AppCompatActivity
{
    private static final String OPEN_WEATHER_MAP_API_KEY = "18fa3e68a532b2961bd8dd0afe720a9c";
    TextView txtView;

    String city;
    LocationManager locationManager;
    TextView locationText;

    private SensorManager sensorManager;
    private Sensor sensor;
    private static float SHAKE_THRESHOLD_GRAVITY = 2;
    private long lastUpdateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        txtView = (TextView) findViewById(R.id.TextView);
        double lat = 40.712774, lon = -74.006091;
        String main = "";
        String humidity = "humidity";
        String units = "metric";
        String url = String.format(
                "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                lat, lon, units, OPEN_WEATHER_MAP_API_KEY);
        new GetWeatherTask().execute(url);
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            String weather = "UNDEFINED";
            try
            {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null )
                {
                    builder.append(inputString);
                }

                JSONObject topLevel = new JSONObject(builder.toString());
                JSONObject main = topLevel.getJSONObject("main");
                weather = String.valueOf(main.getDouble("temp"));

                urlConnection.disconnect();
            }
            catch (IOException | JSONException e)
            {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(String temp)
        {
            txtView.setText("Current Temperature : " + temp);
        }
    }

}

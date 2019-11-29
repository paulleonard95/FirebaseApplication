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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

public class WeatherActivity extends AppCompatActivity implements SensorEventListener
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
        String temp ="";
        @Override
        protected String doInBackground(String... strings)
        {
            String weather = "UNDEFINED";
            String pressure ="UNDEFINED";
            String humidity = "UNDEFINED";
            String wind_spedd = "UNDEFINED";
            List<String> weather_list = new ArrayList<>();
            String listString="";
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
                weather_list.add(String.valueOf(main.getDouble("temp")));
                weather_list.add(String.valueOf(main.getInt("pressure")));
                weather_list.add(String.valueOf(main.getInt("humidity")));

                JSONObject wind = topLevel.optJSONObject("wind");
                weather_list.add(String.valueOf(wind.getDouble("speed")));
                weather_list.add(String.valueOf(wind.getDouble("deg")));
                //code test
                //weather_list.add(String.valueOf(wind.getDouble("gust")));

                for(String s : weather_list)
                {
                    listString += s + "\t";
                }

                urlConnection.disconnect();
            }
            catch (IOException | JSONException e)
            {
                e.printStackTrace();
            }
            temp = listString;
            return listString;
        }

        @Override
        protected void onPostExecute(String weather_list)
        {
            String[] tempA = temp.split("\t");
            if(tempA.length > 1){
                txtView.setText("Current Temperature : " + tempA[0] + "Current Pressure : " + tempA[1] + "Current Humidity : "
                        + tempA[2]);
                txtView.setText("Current Speed : " + tempA[3] + " Current Deg : " + tempA[4] );
            }

        }
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == TYPE_ACCELEROMETER)
        {
            getAccelerometer(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    protected void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener) this);
    }

    private void getAccelerometer(SensorEvent event)
    {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = x / SensorManager.GRAVITY_EARTH;
        float gZ = x / SensorManager.GRAVITY_EARTH;

        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
        long currentTime = System.currentTimeMillis();
        if (gForce >= SHAKE_THRESHOLD_GRAVITY)
        {
            if (currentTime - lastUpdateTime < 200)
            {
                return;
            }
            lastUpdateTime = currentTime;
            Toast.makeText(this, "Device was shaken", Toast.LENGTH_SHORT).show();
            loadFoodHygiene();
        }
    }

    public void loadFoodHygiene()
    {
        Intent intent = new Intent(WeatherActivity.this, FoodHygieneActivity.class);
        startActivity(intent);
    }

    public void LogOut(View view)
    {
        startActivity(new Intent(WeatherActivity.this, MainActivity.class));
    }

}

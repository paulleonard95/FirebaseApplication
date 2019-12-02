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
    TextView txtView_temp;
    TextView textView_city, txtView_humidity, txtView_pressure, txtView_wind;

    String city;
    LocationManager locationManager;
    TextView locationText;

    private SensorManager sensorManager;
    private Sensor sensor;
    private static float SHAKE_THRESHOLD_GRAVITY = 2;
    private long lastUpdateTime;
    private boolean switching = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        txtView = (TextView) findViewById(R.id.TextView);
  //     txtView_humidity = (TextView) findViewById(R.id.textView_display_humidity);
//        txtView_pressure = (TextView) findViewById(R.id.textView_display_pressure);
//        txtView_wind = (TextView) findViewById(R.id.textView_display_wind);
        double lat = 54.995800, lon = -7.307400;
        String main = "";
        String humidity = "humidity";
        String units = "metric";
        String url = String.format(
                "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                lat, lon, units, OPEN_WEATHER_MAP_API_KEY);
        new GetWeatherTask().execute(url);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdateTime = System.currentTimeMillis();
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
            String wind_speed = "UNDEFINED";
            String city = "UNDEFINED";
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
                //txtView.setText("Current Temperature : " + tempA[0] + "Current Pressure : " + tempA[1] + "Current Humidity : "
                       // + tempA[2]);
                //textView_city.setText("Current City: " + tempA[6]);
                //txtView_temp.setText(tempA[0] + "Celsius");
                txtView.setText("Temperature : " + tempA[0] + " Celsius                                      " + "Pressure : " + tempA[1] + "mb             " + "Humidity : "
                       + tempA[2] + "%            ");
                //txtView_humidity.setText("Test" );

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
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
        long currentTime = System.currentTimeMillis();
        if (gForce >= SHAKE_THRESHOLD_GRAVITY)
        {
            if (currentTime - lastUpdateTime < 200)
            {
                return;
            }
            lastUpdateTime = currentTime;
            Toast.makeText(this, "Device shaken", Toast.LENGTH_SHORT).show();
            loadFoodHygiene();
        }
    }

    public void loadFoodHygiene()
    {
        startActivity(new Intent(WeatherActivity.this, FoodHygieneActivity.class));
    }

    public void LogOut(View view)
    {
        startActivity(new Intent(WeatherActivity.this, MainActivity.class));
    }

}

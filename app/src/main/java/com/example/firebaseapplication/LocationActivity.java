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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

public class LocationActivity extends AppCompatActivity implements SensorEventListener
{
    private static final String OPEN_WEATHER_MAP_API_KEY = "18fa3e68a532b2961bd8dd0afe720a9c";
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
        setContentView(R.layout.activity_location);

        Intent intent = getIntent();
        String loginname = intent.getStringExtra("Name");

        boolean internetenabled = checkInternetConn();
        if(internetenabled != true) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            intent = new Intent(LocationActivity.this, FirstPage.class);
            intent.putExtra("Name", loginname);
            startActivity(intent);
        }

        Log.d("TAG", "ON CREATE");

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdateTime = System.currentTimeMillis();


        try {
            intent = getIntent();

            city = intent.getStringExtra("City");

            Log.d("TAG", city);
            weatherSearch(city);

        } catch (Exception e) {

        }

    }
    public boolean checkInternetConn()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            return true;
        }
        else
            return false;
    }

    public void weatherSearch(String city)
    {
        GetWeather task = new GetWeather();
        task.execute(city);
    }


    public void logOutOK(View view) {
        Intent intent = new Intent(LocationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public static String Conn(String targetURL) {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("content-type", "application/json;  charset=utf-8");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);

            InputStream is;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {


        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return "0";
    }
    private class GetWeather extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String xml = LocationActivity.Conn("http://api.openweathermap.org/data/2.5/weather?q=" + city +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API_KEY);

            Log.d("printXML", xml.toString());
            return xml;
        }

        public void onPostExecute(String response) {

            TextView cityField = (TextView) findViewById(R.id.textView_city);
            TextView tempField = (TextView) findViewById(R.id.textView_temperature);
            TextView descField = (TextView) findViewById(R.id.textView_description);
            TextView humidityField = (TextView) findViewById(R.id.textView_humidity);
            TextView pressureField = (TextView) findViewById(R.id.textView_pressure);
            TextView windField = (TextView) findViewById(R.id.textView_wind);

            try {
                JSONObject json = new JSONObject(response);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    cityField.setText(json.getString("name").toUpperCase(Locale.ENGLISH) + ", " + json.getJSONObject("sys").getString("country"));
                    tempField.setText(String.format("%.0f", main.getDouble("temp")) + "°");
                    humidityField.setText(String.format("%.0f", main.getDouble("humidity")) + "%");
                    pressureField.setText(String.format("%.0f", main.getDouble("pressure")) + "mb");
                    String description = details.getString("description").toUpperCase(Locale.ENGLISH);
                    descField.setText(description);
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Occurred", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();

        sensorManager.registerListener((SensorEventListener) this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener((SensorEventListener) this);
    }

    private void getAccelerometer(SensorEvent event) {
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
        if (gForce >= SHAKE_THRESHOLD_GRAVITY) {
            if (currentTime - lastUpdateTime < 200) {
                return;
            }
            lastUpdateTime = currentTime;
            Toast.makeText(this, "Device was shaken", Toast.LENGTH_SHORT).show();
            loadFoodHygiene();
        }
    }
    public void logOut(View view)
    {
        startActivity(new Intent(LocationActivity.this, MainActivity.class));
    }
    public void loadFoodHygiene()
    {
        startActivity(new Intent(LocationActivity.this, FoodHygieneActivity.class));
    }


}

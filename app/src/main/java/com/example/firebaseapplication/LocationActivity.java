package com.example.firebaseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

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

public class LocationActivity extends AppCompatActivity
{
    //auto-complete lists
    String[] city_names = {"Derry, NI", "Belfast, NI", "Dublin, IE", "London, GB", "Glasgow, GB"};
    private static final String OPEN_WEATHER_MAP_API_KEY = "18fa3e68a532b2961bd8dd0afe720a9c";
    TextView txtView;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        TextView textView = (TextView) findViewById(R.id.TextView_weather_search);
        Intent intent = getIntent();

        txtView = (TextView) findViewById(R.id.TextView);
        double lat = 54.9966, lon = 7.3086;
        String main = "";
        String humidity = "humidity";
        String units = "metric";
        String url = String.format(
                "http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                lat, lon, units, OPEN_WEATHER_MAP_API_KEY);
        new LocationActivity.GetWeatherTask().execute(url);
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

    public void logOut(View view)
    {
        startActivity(new Intent(LocationActivity.this, MainActivity.class));
    }

    public void searchCity(View view)
    {
        try
        {
            String city = ((EditText) findViewById(R.id.SearchView_city_search)).getText().toString();
            Intent intent = new Intent(LocationActivity.this, WeatherActivity.class);
            intent.putExtra("Name", name);
            intent.putExtra("City", city);
            Log.d("Search by City", "Search by City : " + city);
            startActivity(intent);
        }
        catch (Exception e)
        {
            Log.d("Search", "Problem with searching for city");
        }
    }
}

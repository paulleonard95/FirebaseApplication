package com.example.firebaseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class LocationActivity extends AppCompatActivity
{
    //auto-complete lists
    String[] city_names = {"Derry, NI", "Belfast, NI", "Dublin, IE", "London, GB", "Glasgow, GB"};
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        TextView textView = (TextView) findViewById(R.id.TextView_weather_search);
        Intent intent = getIntent();
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

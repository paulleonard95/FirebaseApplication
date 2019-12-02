package com.example.firebaseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class FirstPage extends AppCompatActivity
{

    String[] cityArr = {"Derry, GB" , "London, GB", "Belfast, GB", "Dublin, IE", "Cork, IE", "Glasgow, IE", "Cardiff, GB" };
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        TextView txtname = (TextView) findViewById(R.id.textView_success);

        Intent intent = getIntent();

        String loginName = intent.getStringExtra("Name");
        txtname.setText("Welcome, " + loginName);
        name = loginName;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, cityArr);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewCitySearch);

        textView.setThreshold(3);
        textView.setAdapter(adapter);
    }


    public void searchByCity(View view) {

        //Intent intent = new Intent(LoginSuccessActivity.this, WeatherSearchActivity.class);
        //startActivity(intent);

        Log.d("Search by City", "Search by City");

        try {
            String city = ((EditText) findViewById(R.id.autoCompleteTextViewCitySearch)).getText().toString();

            Intent intent = new Intent(FirstPage.this, LocationActivity.class);
            intent.putExtra("City", city);
            intent.putExtra("Name", name);
            Log.d("Search by City", "Search by City : " + city);
            startActivity(intent);

        } catch (Exception e) {
            Log.d("Search", "Unable to Search by City");

        }

    }

    public void loadMain(View view)
    {
        startActivity(new Intent(FirstPage.this, WeatherActivity.class));
    }
    public void logOut(View view)
    {
        startActivity(new Intent(FirstPage.this, MainActivity.class));
    }
}

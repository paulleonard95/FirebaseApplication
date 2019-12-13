package com.example.firebaseapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class FirstPage extends AppCompatActivity
{

    String[] cityArr = {"Derry, GB" , "Liverpool, GB" ,"Belfast, GB","Dublin, IE", "Galway, IE", "Donegal, IE", "London, GB", "Glasgow, GB"};
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        TextView txtname = (TextView) findViewById(R.id.textView_success);

        Intent intent = getIntent();

//        String loginName = intent.getStringExtra("Name");
//        txtname.setText("Welcome, " + loginName);
//        name = loginName;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, cityArr);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewCitySearch);

        textView.setThreshold(3);
        textView.setAdapter(adapter);

        isNetworkConnectionAvailable();
    }

    public void checkNetworkConnection()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please turn on internet connection");
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public  boolean isNetworkConnectionAvailable()

    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (isConnected)
        {
            Log.d("Network", "Connected");
            return true;
        }
        else
        {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }
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

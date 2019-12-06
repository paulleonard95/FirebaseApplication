package com.example.firebaseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.xml.transform.ErrorListener;

public class FoodHygieneActivity<stringRequest> extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private  TextView textView;
    private RequestQueue requestQueue;


    //final TextView textView = (TextView) findViewById(R.id.TextView_Food_Hygiene);
    final String url = "http://api.ratings.food.gov.uk/Establishments/basic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_hygiene);

        textView = findViewById(R.id.TextView_Food_Hygiene);
        Button buttonParse = findViewById(R.id.button_parse);

        requestQueue = Volley.newRequestQueue(this);
        buttonParse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
    }

    private void jsonParse()
    {
        String url = "http://api.ratings.food.gov.uk/Establishments/basic";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            JSONArray jsonArray = response.getJSONArray("establishments");

                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject establishment = jsonArray.getJSONObject(i);

                                int FHRSID = establishment.getInt("FHRSID");
                                String businessID = establishment.getString("LocalAuthorityBusinessID");
                                String businessName = establishment.getString("BusinessName");
                                String businessValue = establishment.getString("RatingValue");

                                textView.append(String.valueOf(FHRSID) + ", " + businessID + " , " +
                                        businessName + " , " + businessValue + "\n\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }

    public void LogOut(View view) {
        startActivity(new Intent(FoodHygieneActivity.this, MainActivity.class));
    }

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    textView.setText("Response: " + response.toString());
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error

                }
            });


}

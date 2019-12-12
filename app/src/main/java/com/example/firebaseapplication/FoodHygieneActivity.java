package com.example.firebaseapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.ErrorListener;

public class FoodHygieneActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private  TextView textView;
    private RequestQueue requestQueue;


    //final TextView textView = (TextView) findViewById(R.id.TextView_Food_Hygiene);
    //final String url = "http://api.ratings.food.gov.uk/Establishments/basic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_hygiene);

        textView = findViewById(R.id.TextView_Food_Hygiene);
        //Button buttonParse = findViewById(R.id.button_parse);

        requestQueue = Volley.newRequestQueue(this);
//        buttonParse.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                jsonParse();
//           }

        jsonParse();

    }

//    private void jsonParse()
//    {
//        String url = "http://api.ratings.food.gov.uk/Establishments/basic";
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response)
//                    {
//                        Toast.makeText(getBaseContext(), response.toString(), Toast.LENGTH_LONG).show();
//                        try {
//                            JSONArray jsonArray = response.getJSONArray("establishments");
//
//                            for (int i = 0; i < jsonArray.length(); i++)
//                            {
//                                JSONObject establishment = jsonArray.getJSONObject(i);
//
//                                //int FHRSID = establishment.getInt("FHRSID");
//                                //String businessID = establishment.getString("LocalAuthorityBusinessID");
//                                String businessName = establishment.getString("BusinessName");
//                                String businessValue = establishment.getString("RatingValue");
//
//                                textView.append(
//                                        businessName + " , " + businessValue + "\n\n");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error)
//            {
//                error.printStackTrace();
//            }
//        });
//
//        requestQueue.add(jsonObjectRequest);
//    }

    private void jsonParse() {

        final String url = "http://api.ratings.food.gov.uk/Establishments?longitude=-7.323717&latitude=55.00669&distancelimit=1&businesstypeid=1";

        /*

        <input type="hidden" class="ResultsLongitude" value="-7.323717" />
        <input type="hidden" class="ResultsLatitude" value="55.00669" />
        GET Establishments?longitude={-7.323717}&latitude={55.00669}&maxDistanceLimit={}
        Establishments?longitude={-7.323717}&latitude={55.00669}

         */

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(getBaseContext(), response.toString(), Toast.LENGTH_LONG).show();
                // here you can get the restaurants and rating details
                try {
                    JSONArray jsonArray = response.getJSONArray("establishments");
                    System.out.println(response);

                    for (int i =0; i < jsonArray.length(); i++)
                    {
                        JSONObject estab = jsonArray.getJSONObject(i);

                        String businessName = estab.getString("BusinessName");
                        String businessValue = estab.getString("RatingValue");

                        textView.append("Restaurant Name: " + businessName + "\n " + "Food Hygiene Rating: " +businessValue + "\n\n");
                    }
//                                String businessValue = establishment.getString("RatingValue");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("LOG", "An Error occured while making the request");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                //The api requires me to specify the api version
                params.put("x-api-version", "2");
                params.put("accept", "application/json");
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }


    public void LogOut(View view) {
        startActivity(new Intent(FoodHygieneActivity.this, MainActivity.class));
    }




}

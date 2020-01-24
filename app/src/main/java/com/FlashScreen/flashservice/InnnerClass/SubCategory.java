package com.FlashScreen.flashservice.InnnerClass;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.database.ServiceDb;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubCategory extends AppCompatActivity {
    ProgressDialog progressDialog;
    Shaved_shared_preferences sharedPreferences;
    String TAG="TAG",city,subcat;
    ArrayAdapter<String> adapter;
    Spinner city_spinner;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListId;
    TextView submit_cat;
    EditText input_sub,price;
    String PRICE="On call";
    ServiceDb serviceDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        // actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        sharedPreferences = new Shaved_shared_preferences(getApplicationContext());
        progressDialog = new ProgressDialog(SubCategory.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Data...");

        city_spinner = (Spinner)findViewById(R.id.city_spinner);
        submit_cat = (TextView) findViewById(R.id.submit_cat);
        input_sub = (EditText) findViewById(R.id.input_sub);
        price = (EditText) findViewById(R.id.price);

        arrayList = new ArrayList<>();
        arrayListId = new ArrayList<>();
        arrayList.clear();
        arrayListId.clear();

        serviceDb = new ServiceDb(getApplicationContext());

        arrayList.add("Select Category");
        arrayListId.add("0");


        if(AppController.getInstance().isOnline())
        {
            FetchType();
        }
        else {

            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        submit_cat.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                subcat = input_sub.getText().toString();
                PRICE = price.getText().toString();


                if(city.equalsIgnoreCase("0"))
                {
                    Toast.makeText(getApplicationContext(),"SELECT CATEGORY",Toast.LENGTH_LONG).show();

                }
                else {
                    if(subcat.length()>0)
                    {

                            SENDDATA();


                    }
                    else{
                        input_sub.setError("ENTER SUBCATEGORY");
                    }
                }
            }
        });

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = String.valueOf(parent.getItemAtPosition(position));

                city = arrayListId.get(position);
               Log.e("dataaSENDING ", "--" + city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void SENDDATA() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.Add_Service,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if(text.equals("1"))
                            {

                                Intent intent = new Intent(getApplicationContext(), NavigationAdmin.class);
                                startActivity(intent);
                                finish();

                                serviceDb.delall();

                                Toast.makeText(getApplicationContext(),"SUBCATGORY ADDED SUCESSFULLY",Toast.LENGTH_LONG).show();
                                progressDialog.hide();

                            }
                            else {
                                String errMessage = ob.getString("type");
                                Toast.makeText(getApplicationContext(), "" + errMessage, Toast.LENGTH_LONG).show();
     progressDialog.hide();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SubCategory.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Categoryid", "" + city);
                params.put("Service", "" + subcat);
                params.put("Price", "" + PRICE);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void FetchType()
    {
        progressDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_CAT, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String city_Id = jsonObject.getString("category_Id");
                                String city = jsonObject.getString("category_name");


                                arrayList.add(city);
                                arrayListId.add(city_Id);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                        // adp =  new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList);
                        // select_category.setAdapter(adp);
                        adapter = new ArrayAdapter<String>(SubCategory.this, R.layout.textview, arrayList) {

                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View v = null;
                                v = super.getDropDownView(position, null, parent);
                                // If this is the selected item position

                                // for other views
                                TextView text = (TextView) v.findViewById(R.id.textvw);
                                text.setTextColor(Color.parseColor("#3d406b"));
                                Drawable backgroundColor = getResources().getDrawable(R.drawable.underline);
                                v.setBackground(backgroundColor);


                                return v;
                            }
                        };


                        // Drop down layout style - list group_layouts with radio button
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        adapter.setDropDownViewResource(R.layout.textview);
                        city_spinner.setAdapter(adapter);



                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "tag_json_obj");

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent i = new Intent(getApplicationContext(),NavigationAdmin.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplicationContext(). getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),NavigationAdmin.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();

    }
}

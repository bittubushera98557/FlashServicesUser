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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.database.CityDb;
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

public class AssignScreen extends AppCompatActivity {
    Shaved_shared_preferences shaved_shared_preferences;
    ProgressDialog progressDialog;
    String Assign;
    String Order_Id;
    String Service_Id;
    String Category_Id;
    String customer_id;
    String Category;
    String ProId="0";
    String Service;
    String Date;
    String Time;
    String State;
    String City;
    String Area;
    String Assignid="0";
    Spinner selection;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;
    ArrayList<Integer> arrayList_pos;
    CityDb cityDb;
    ArrayList<HashMap<String,String>> HASH;
    TextView assigned_name,assigned_phone,worker,assigned_city;
    TextView submit_assign,cancel_assign;
    TextView area_,assign_name,assign_phone,assign_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_screen);

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

        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());

        progressDialog = new ProgressDialog(AssignScreen.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait......");

         selection=(Spinner) findViewById(R.id.selection);

         submit_assign=(TextView) findViewById(R.id.submit_assign);
         cancel_assign=(TextView) findViewById(R.id.cancel_assign);

        TextView cat_name=(TextView) findViewById(R.id.v_order);
        TextView v_date=(TextView) findViewById(R.id.v_date);
        TextView v_time=(TextView) findViewById(R.id.v_time);
        TextView v_work=(TextView)findViewById(R.id.v_work);
        TextView v_workk=(TextView) findViewById(R.id.v_workk);
         area_=(TextView) findViewById(R.id.area);

         assign_name=(TextView) findViewById(R.id.assign_name);
         assigned_name=(TextView) findViewById(R.id.assigned_name);
         assign_phone=(TextView) findViewById(R.id.assign_phone);
        assign_email=(TextView) findViewById(R.id.assign_email);
         assigned_phone=(TextView) findViewById(R.id.assigned_phone);
        assigned_city=(TextView) findViewById(R.id.assigned_city);
        worker=(TextView) findViewById(R.id.worker);

        LinearLayout linear_assign=(LinearLayout) findViewById(R.id.linear_assign);

        HASH =new ArrayList<>();
        arrayList =new ArrayList<>();
        arrayList_pos =new ArrayList<>();
        HASH.clear();
        arrayList.clear();
        arrayList_pos.clear();

        cityDb = new CityDb(getApplicationContext());

        Intent i = getIntent();
        Assign = i.getStringExtra("Assign");
        Service_Id = i.getStringExtra("Service_Id");
        Assignid = i.getStringExtra("Assignid");
        Order_Id = i.getStringExtra("Order_Id");
        customer_id = i.getStringExtra("customer_id");
        Category = i.getStringExtra("Category");
        Category_Id = i.getStringExtra("Category_Id");
        Service = i.getStringExtra("Service");
        Date = i.getStringExtra("date");
        Time = i.getStringExtra("Time");
        ProId = i.getStringExtra("ProId");
        State = i.getStringExtra("State");
        City = i.getStringExtra("City");
        Area = i.getStringExtra("Area");

        worker.setText("Available "+Category);

        area_.setText("Area: "+Area);

        Log.e("q1orderId", "" + Order_Id);
        Log.e("q1userId", "" + customer_id);
        Log.e("q1providerId", "" + ProId);
        Log.e("q1assignId", "" + Assignid);

        if (isOnline())
        {
            FETCH_CITY();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        if(Assign.equalsIgnoreCase("0"))
        {
            linear_assign.setVisibility(View.GONE);

        }
        else {
            linear_assign.setVisibility(View.VISIBLE);

            if (isOnline())
            {
                Fetch_Providers();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }


        cat_name.setText("Order Id: " +Order_Id);
        v_work.setText(Category);
        v_workk.setText(Service);
        v_date.setText("Date: " +Date);
        v_time.setText("Time: " +Time);




        submit_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Assign.equalsIgnoreCase("0")) {
                    if (isOnline()) {
                        SEND_ASSIGN();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (isOnline()) {
                        CHANGE_ASSIGN();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        selection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String NAME = parent.getItemAtPosition(position).toString();

                for (int j = 0; j < HASH.size(); j++) {
                    if (NAME.equalsIgnoreCase(HASH.get(j).get("name"))) {
                        String Id = HASH.get(j).get("Id");
                        String n = HASH.get(j).get("name");
                        String nm = HASH.get(j).get("number");
                        String em = HASH.get(j).get("email");
                        String city = HASH.get(j).get("city");

                        assign_name.setText(n);
                        assign_phone.setText(nm);
                        assign_email.setText(em);
                        assigned_city.setText(city);

                        ProId = Id;
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancel_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCancel();
            }
        });

    }
    private void sendCancel()
    {
        Log.e("q11orderId", "" + Order_Id);
        Log.e("q11userId", "" + customer_id);
        Log.e("q11providerId", "" + ProId);
        Log.e("q11assignId", "" + Assignid);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_CANCEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if(text.equals("1"))
                            {
                                Toast.makeText(getApplicationContext(), "Service Cancelled Successfully", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(getApplicationContext(), AllServices.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);

                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        if(ProId.equalsIgnoreCase("0"))
                        {

                        }
                        else {
                            sendCancelS();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderId", "" + Order_Id);
                params.put("userId", "" + customer_id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void sendCancelS()
    {
        Log.e("q11orderId", "" + Order_Id);
        Log.e("q11userId", "" + customer_id);
        Log.e("q11providerId", "" + ProId);
        Log.e("q11assignId", "" + Assignid);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_CANCEL_VENDOR_NOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGGQ", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if(text.equals("1"))
                            {
                                Toast.makeText(getApplicationContext(), "Notify to Vendor Successfully", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(getApplicationContext(), AllServices.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);

                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Server Error",Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderId", "" + Order_Id);
                params.put("providerId", ""+ProId);
                params.put("assignId", ""+Assignid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void FETCH_CITY()
    {
        cityDb.delall();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_CITY, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String state_Id = jsonObject.getString("state_Id");

                                String cityId = jsonObject.getString("cityId");
                                String city = jsonObject.getString("city");

                                cityDb.insertRecord(cityId, city);

                                if(City.equalsIgnoreCase(cityId))
                                {
                                    area_.setText("Area: " +Area+", "+city);
                                }


                            }



                            // Drop down layout style - list view with radio button

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Fetch_Provider();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());

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

    private void Fetch_Providers() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_FETCH_PROVIDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String type = ob.getString("type");

                            JSONArray jsonArray = ob.getJSONArray("data");


                            for (int j = 0; j < jsonArray.length(); j++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String Id = jsonObject.getString("Id");
                                String category_Id = jsonObject.getString("category_Id");
                                String city_Id = jsonObject.getString("city_Id");
                                String area_Id = jsonObject.getString("area_Id");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String number = jsonObject.getString("number");

                                Log.e("dataaa","--"+ProId+"--"+Id);

                                String city = cityDb.getName(city_Id);

                                Log.e("dataaacity_Id","--"+city+"--"+city_Id);

                                if(ProId.equalsIgnoreCase(Id))
                                {
                                    assigned_name.setText(name);
                                    assigned_phone.setText(number);
                                    assigned_city.setText(city);
                                }


                            }
                            progressDialog.hide();

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        progressDialog.hide();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }
    private void Fetch_Provider() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_FETCH_PROVIDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String type = ob.getString("type");

                            JSONArray jsonArray = ob.getJSONArray("data");

                            int v = 0;

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String Id = jsonObject.getString("Id");
                                String category_Id = jsonObject.getString("category_Id");
                                String city_Id = jsonObject.getString("city_Id");
                                String area_Id = jsonObject.getString("area_Id");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String number = jsonObject.getString("number");


                                String city = cityDb.getName(city_Id);

                                Log.e("dataaa", "--" + ProId + "--" + Id);

                                if (ProId.equalsIgnoreCase(Id)) {

                                } else {

                                    Log.e("dataaacity_Id", "--" + city + "--" + city_Id);

                                    if (category_Id.equalsIgnoreCase(Category_Id)) {
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("Id", "" + Id);
                                        hashMap.put("category_Id", "" + category_Id);
                                        hashMap.put("city_Id", "" + city_Id);
                                        hashMap.put("area_Id", "" + area_Id);
                                        hashMap.put("name", "" + name);
                                        hashMap.put("email", "" + email);
                                        hashMap.put("number", "" + number);
                                        hashMap.put("city", "" + city);

                                        HASH.add(hashMap);

                                        Log.e("vvvvvvvv ", "--------" + area_Id);
                                    //    Log.e("vvvvvvvvbbb ","--------"+city_Id);
                                        Log.e("vvvvvvvvbbb ","--------"+City);
                                        Log.e("vvvvvvvvbbb ","--------"+name);

                                        if(area_Id.contains(City) ||area_Id.equalsIgnoreCase(City))
                                        {
                                            arrayList_pos.add(v);
                                            Log.e("vvvvvvvvbbbhh ", "--------" + j);
                                        }

                                        v=v+1;

                                        arrayList.add(name);
                                    }
                                }
                            }
                                adapter = new ArrayAdapter<String>(AssignScreen.this, R.layout.textview, arrayList) {

                                    @TargetApi(Build.VERSION_CODES.M)
                                    @Override
                                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                        View v = null;
                                        v = super.getDropDownView(position, null, parent);
                                        // If this is the selected item position

                                        TextView text = (TextView) v.findViewById(R.id.textvw);
                                        text.setTextColor(Color.parseColor("#3d406b"));

                                        if(arrayList_pos.contains(position))
                                        {
                                            Drawable backgroundColor = getResources().getDrawable(R.drawable.boxx);
                                            v.setBackground(backgroundColor);
                                        }
                                        else {

                                            Drawable backgroundColor = getResources().getDrawable(R.drawable.box);
                                            v.setBackground(backgroundColor);
                                        }


                                        return v;
                                    }
                                };


                                // Drop down layout style - list view with radio button
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                adapter.setDropDownViewResource(R.layout.textview);
                                selection.setAdapter(adapter);
                                progressDialog.hide();

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        progressDialog.hide();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void SEND_ASSIGN()
    {

        Log.e("TAGGGGGG","--"+Order_Id);
        Log.e("TAGGGGGG","--"+ProId);

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_SEND_ASSIGN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if(text.equals("1"))
                            {
                                String type = ob.getString("type");

                                 Toast.makeText(getApplicationContext(),"SUCESSFULLY ASSIGNED",Toast.LENGTH_LONG).show();
                                progressDialog.hide();

                                Intent i = new Intent(getApplicationContext(), AllServices.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                                finish();


                            }
                            else {

                                Toast.makeText(getApplicationContext(),"SERVER ERROR",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(AssignScreen.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("serviceid", "" + Order_Id);
                params.put("providerid", "" + ProId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void CHANGE_ASSIGN()
    {
        Log.e("TAGGGGGG","--"+Order_Id);
        Log.e("TAGGGGGG","--"+ProId);
        Log.e("TAGGGGGG","--"+Assignid);

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_CHANGE_ASSIGN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("CHG-TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if(text.equals("1"))
                            {
                                String type = ob.getString("type");

                                Toast.makeText(getApplicationContext(),"SUCCESSFULLY ASSIGNED UPDATED",Toast.LENGTH_LONG).show();
                                progressDialog.hide();

                                Log.e("CHGG - TAGGGGGG", response.toString());

                                Intent i = new Intent(getApplicationContext(), AllServices.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                                finish();


                            }
                            else {

                               Toast.makeText(getApplicationContext(),"SERVER ERROR",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(AssignScreen.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("serviceid", "" + Order_Id);
                params.put("providerid", "" + ProId);
                params.put("assignid", "" + Assignid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
                Intent intent = new Intent(AssignScreen.this,AllServices.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
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
    public void onBackPressed()
    {
        // super.onBackPressed();
        Intent intent = new Intent(AssignScreen.this,AllServices.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);

    }
}

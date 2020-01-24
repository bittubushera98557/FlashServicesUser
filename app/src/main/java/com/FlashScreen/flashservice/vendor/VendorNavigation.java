package com.FlashScreen.flashservice.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.All;
import com.FlashScreen.flashservice.adapter.AllAdapter;
import com.FlashScreen.flashservice.adapter.Pack;
import com.FlashScreen.flashservice.database.CityDb;
import com.FlashScreen.flashservice.database.ServiceDb;
import com.FlashScreen.flashservice.database.StateDb;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class VendorNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Shaved_shared_preferences sharedPreferences;
    ProgressDialog progressDialog;
    ServiceDb serviceDb;
    StateDb stateDb;
    CityDb cityDb;
    String TAG="TAG",ProId = "";
    ArrayList<HashMap<String,String>> hash_arrayList;
    ArrayList<String> arrayList;
    ListView list_vendor;
    ArrayList<Vendor> arrayListt;
    VendorAdapter vendorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = new Shaved_shared_preferences(getApplicationContext());

        ProId  = sharedPreferences.get_userid();


        serviceDb = new ServiceDb(getApplicationContext());
        stateDb = new StateDb(getApplicationContext());
        cityDb = new CityDb(getApplicationContext());

        list_vendor = (ListView)findViewById(R.id.list_vendor);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        progressDialog = new ProgressDialog(VendorNavigation.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Data...");

        hash_arrayList = new ArrayList<>();
        arrayListt = new ArrayList<>();
        arrayList = new ArrayList<>();
        hash_arrayList.clear();
        arrayListt.clear();
        arrayList.clear();

        if(AppController.getInstance().isOnline())
        {

            if(serviceDb.CATID().size()>0)
            {

            }
            else {
                Fetch_Category();
            }
            if(stateDb.ID().size()>0)
            {

            }
            else {
                FETCH_STATE();
            }
            if(stateDb.ID().size()>0 && serviceDb.CATID().size()>0) {
                Fetch_Assign(ProId);
            }

        }
        else {

            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void Fetch_Category()
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

                            for (int j = 0; j < jsonArray.length(); j++)
                            {

                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String category_Id = jsonObject.getString("category_Id");
                                String category_name = jsonObject.getString("category_name");
                                String image_upload1 = jsonObject.getString("image_upload1");
                                String date = jsonObject.getString("date");


                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("category_Id",category_Id);
                                hashMap.put("category_name", category_name);

                                hash_arrayList.add(hashMap);


                            }


                            progressDialog.hide();
                            FetchPACKAGE();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();
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


    private void FetchPACKAGE()
    {
        serviceDb.delall();

        final JsonArrayRequest req = new JsonArrayRequest(Const.URL_FETCH_CAT_DETAILS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            if(response.length()>0) {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject jsonObject = response.getJSONObject(i);

                                    Log.e("jsonObject ", "" + jsonObject);

                                    String category_Idd = jsonObject.getString("category_Id");

                                    Log.e("messagewwjsonObject ", "" + category_Idd);

                                    JSONArray jsonArrays = jsonObject.getJSONArray("categorieslevelone");

                                    Log.e("messagewwjsonObject..2 ", "" + jsonArrays);

                                    for (int j = 0; j < jsonArrays.length(); j++) {
                                        Pack pack = new Pack();

                                        JSONObject jsonObject22 = jsonArrays.getJSONObject(j);

                                        String service_Id = jsonObject22.getString("service_Id");
                                        String category_Id = jsonObject22.getString("category_Id");
                                        String service1 = jsonObject22.getString("service1");
                                        String price1 = jsonObject22.getString("price1");

                                        String category_name = "";

                                        for(int ii = 0;ii<hash_arrayList.size();ii++)
                                        {
                                            String catid = hash_arrayList.get(ii).get("category_Id");

                                            if(category_Id.equalsIgnoreCase(catid))
                                            {
                                                category_name = hash_arrayList.get(ii).get("category_name");
                                            }

                                        }

                                        serviceDb.insertRecord(category_Id, category_name, service_Id, service1, price1);


                                    }
                                }
                            }

                        }
                        catch (Exception e)
                        {
                            e.getMessage();
                        }
                        Fetch_Assign(ProId);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                "tag_json_arry");

    }

    private void FETCH_STATE()
    {
        stateDb.delall();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_STATE, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String state_Id = jsonObject.getString("state_Id");
                                String state_name = jsonObject.getString("state_name");

                                stateDb.insertRecord(state_Id,state_name);
                            }
                            FETCH_CITY();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
    private void FETCH_CITY()
    {
        cityDb.delall();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_CITY, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String state_Id = jsonObject.getString("state_Id");

                                String cityId = jsonObject.getString("cityId");
                                String city = jsonObject.getString("city");

                                cityDb.insertRecord(cityId, city);

                            }



                            // Drop down layout style - list view with radio button

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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


    private void Fetch_Assign(final String PId) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_FETCH_ASSIGN,
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
                                String provider_Id = jsonObject.getString("provider_Id");
                                String service_Id = jsonObject.getString("service_Id");

                                Log.e("zzzzzzzpp ","--------"+provider_Id+"---"+PId);


                             if(PId.equalsIgnoreCase(provider_Id))
                             {
                                   arrayList.add(service_Id);
                                 Log.e("zzzzzzzppuu ", "--------" + provider_Id + "---" + PId);
                             }
                            }
                            progressDialog.hide();

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                        if(arrayList.size()>0)
                        {
                            Log.e("zzzzzzzppuu ", "--------" + arrayList.size());
                            Fetch();

                        }
                        else {
                            new SweetAlertDialog(VendorNavigation.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Sorry ")
                                    .setContentText("No Service Assigned!")
                                    .show();
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
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void Fetch() {

        for(int i=0;i<arrayList.size();i++)
        {
            Log.e("zzzzzzzppuuzz","--"+arrayList.get(i));
        }



        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.Fetch_all,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String type = ob.getString("type");

                            JSONArray jsonArray = ob.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                Vendor vendor = new Vendor();

                                String Id = jsonObject.getString("Id");
                                String category_Id = jsonObject.getString("category_Id");
                                String customer_Id = jsonObject.getString("customer_Id");
                                String service_Id = jsonObject.getString("service_Id");
                                String service_price = jsonObject.getString("service_price");
                                String area_name = jsonObject.getString("area_name");
                                String state_Id = jsonObject.getString("state_Id");
                                String city_Id = jsonObject.getString("city_Id");
                                String date = jsonObject.getString("date");
                                String time = jsonObject.getString("time");

                                String category = serviceDb.getCat_Name(category_Id);
                                String service = serviceDb.getService_Name(service_Id);

                                String STATE = "";//stateDb.getName(state_Id);
                                String CITY = "";//cityDb.getName(city_Id);


                                vendor.setId(Id);
                                vendor.setCategory(category);
                                vendor.setCategory_Id(category_Id);
                                vendor.setCustomer_Id(customer_Id);
                                vendor.setService_Id(service_Id);
                                vendor.setService(service);
                                vendor.setService_price(service_price);
                                vendor.setState_Id(state_Id);
                                vendor.setState(STATE);
                                vendor.setCity_Id(city_Id);
                                vendor.setCity(CITY);
                                vendor.setDate(date);
                                vendor.setTime(time);
                                vendor.setArea_name(area_name);

                                if(arrayList.contains(Id))
                                {
                                    arrayListt.add(vendor);
                                }

                            }
                            progressDialog.hide();
                            vendorAdapter = new VendorAdapter(getApplicationContext(),arrayListt);
                        list_vendor.setAdapter(vendorAdapter);
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



    @Override
    public void onBackPressed() {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("You Want to Exit Application !")
                    .setConfirmText("Yes!")
                    .setCancelText("No")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            sharedPreferences.set_user_login(0);

                            sDialog.dismissWithAnimation();
                            finish();


                        }
                    })
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();



    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(getApplicationContext(), VendorNavigation.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Are you sure?")
                    .setContentText("You Want to Exit Application !")
                    .setConfirmText("Yes!")
                    .setCancelText("No")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sharedPreferences.set_user_login(0);
                            sDialog.dismissWithAnimation();
                            finish();

                        }
                    })
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    })
                    .show();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

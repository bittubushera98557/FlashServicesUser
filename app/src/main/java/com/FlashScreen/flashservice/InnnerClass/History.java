package com.FlashScreen.flashservice.InnnerClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.His;
import com.FlashScreen.flashservice.adapter.HistoryAdapter;
import com.FlashScreen.flashservice.database.CityDb;
import com.FlashScreen.flashservice.database.ServiceDb;
import com.FlashScreen.flashservice.database.StateDb;
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

public class History extends AppCompatActivity {
    ProgressDialog progressDialog;
    ArrayList<His> arrayList;
    String userId, TAG = "History ";
    ListView listview_history;
    HistoryAdapter historyAdapter;
    ServiceDb serviceDb;
    StateDb stateDb;
    CityDb cityDb;
    Shaved_shared_preferences shaved_shared_preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

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

        progressDialog = new ProgressDialog(History.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait......");

        listview_history = (ListView) findViewById(R.id.listview_history);

        arrayList = new ArrayList<>();
        arrayList.clear();

        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());

        serviceDb = new ServiceDb(getApplicationContext());
        stateDb = new StateDb(getApplicationContext());
        cityDb = new CityDb(getApplicationContext());

        userId = shaved_shared_preferences.get_userid();

        if (isOnline()) {
            FETCH_STATE();
            Log.e("userId", "----" + userId);

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        }


    }

    private void FETCH_STATE() {

        progressDialog.show();
        Log.e(TAG,"FETCH_STATE response ="+  Const.URL_FETCH_STATE);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_STATE, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "FETCH_STATE =  "+response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String state_Id = jsonObject.getString("state_Id");
                                String state_name = jsonObject.getString("state_name");

                                stateDb.insertRecord(state_Id, state_name);

                            }

                            progressDialog.hide();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        FETCH_CITY();
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

    private void FETCH_CITY() {
        progressDialog.show();
        Log.e(TAG, "FETCH_CITY = "+   Const.URL_FETCH_CITY);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_CITY, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG,"FETCH_CITY response ="+ response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String state_Id = jsonObject.getString("state_Id");


                                String cityId = jsonObject.getString("cityId");
                                String city = jsonObject.getString("city");

                                cityDb.insertRecord(cityId, city);

                            }
                            progressDialog.hide();

                        } catch (Exception e) {
                            Log.e(TAG + "  FETCH_CITY exception ",e.toString());
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                        callHistoryApi();
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

    private void callHistoryApi() {
        progressDialog.show();
        Log.e(TAG + "callHistoryApi URL_ORDER", Const.URL_ORDER);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                         JSONObject mainObj=new JSONObject(response);
                            JSONArray jsonArray = mainObj.getJSONArray("data");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                String Id = jsonObject.getString("Id");
                                String category_Id = jsonObject.getString("category_Id");
                                String customer_Id = jsonObject.getString("customer_Id");
                                String service_Id = jsonObject.getString("service_Id");
                                String service_price = jsonObject.getString("service_price");
                                String state_Id = jsonObject.getString("state_Id");
                                String city_Id = jsonObject.getString("city_Id");
                                String area_name = jsonObject.getString("area_name");
                                String date = jsonObject.getString("date");
                                String time = jsonObject.getString("time");
                                String task = jsonObject.getString("task");
                                String isActive = jsonObject.getString("isActive");
                                String category = "", service = "";

                                try {
                                    if (userId.equalsIgnoreCase(customer_Id)) {
                                        Log.e(TAG + " UserOrder="+userId , ""+jsonObject);
                                        His his = new His();
                                        category = serviceDb.getCat_Name(category_Id);
                                        service = serviceDb.getService_Name(service_Id);
                                        his.setId(Id);
                                        his.setCategory_Id(category);
                                        his.setCustomer_Id(customer_Id);
                                        his.setService_Id(service);
                                        his.setService_price(service_price);
                                        his.setState_Id(state_Id);
                                        his.setCity_Id(city_Id);
                                        his.setArea_name(area_name);
                                        his.setDate(date);
                                        his.setTime(time);
                                        his.setIsActive(isActive);
                                        his.setTask(task);
                                         arrayList.add(his);
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG + "callHistoryApi Except", "serviceDb  " + e.toString());
                                    Log.e(TAG + "callHistoryApi Except", "Id=" + Id + "  --category_Id=" + category_Id + "  --userId=" + userId);
                                }
                            }
                            historyAdapter = new HistoryAdapter(History.this, arrayList);
                            listview_history.setAdapter(historyAdapter);
                            progressDialog.hide();
                        } catch (Exception e) {
                            Log.e(TAG + "Except callHistoryApi",   e.toString());
                            e.printStackTrace();
                        }
                        progressDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(History.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_type", "user" );

/*                params.put("number", "" + mobile);
                params.put("password", "" + password);*/
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(History.this, NavigationScreen.class);
                startActivity(intent);
                finish();
                //   overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(History.this, NavigationScreen.class);
        startActivity(intent);
        finish();
        //overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);

    }
}

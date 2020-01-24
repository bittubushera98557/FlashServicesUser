package com.FlashScreen.flashservice.InnnerClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
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

public class NavigationAdmin extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView issue_view,member_view,issue_view_date,call_all;
    Shaved_shared_preferences shaved_shared_preferences;
    ProgressDialog progressDialog;
    String userId,TokenId,Token;
    String TAG="TAGG";

    ServiceDb serviceDb;
    StateDb stateDb;
    CityDb cityDb;
    ArrayList<HashMap<String,String>> hash_arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());

        progressDialog = new ProgressDialog(NavigationAdmin.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Data...");

        userId = shaved_shared_preferences.get_userid();
        TokenId = shaved_shared_preferences.get_TokenId();
        Token = shaved_shared_preferences.get_Token();


        serviceDb = new ServiceDb(getApplicationContext());
        stateDb = new StateDb(getApplicationContext());
        cityDb = new CityDb(getApplicationContext());

        issue_view = (TextView) findViewById(R.id.issue_view);
        member_view = (TextView) findViewById(R.id.member_view);

        hash_arrayList =new ArrayList<>();
        hash_arrayList.clear();


        issue_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), AllServices.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                finish();
            }
        });

        member_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MemberActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                finish();
            }
        });



        if(AppController.getInstance().isOnline())
        {

            Fetch_Category();
        }
        else {

            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            logout_app();
            // super.onBackPressed();
        }
    }

    private void logout_app() {
        Log.e("logout_123", "--" + shaved_shared_preferences.get_user_login());

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You Want to Exit Application !")
                .setConfirmText("Yes!")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        finish();

                        serviceDb.delall();
                        stateDb.delall();
                        cityDb.delall();


                        Log.e("logout_1234 admin", "--" + shaved_shared_preferences.get_user_login());

                    }
                })
                .showCancelButton(true)
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

        Log.e("zzzzzzz","--"+item.getItemId());

        if (id == R.id.nav_home) {
            Intent i = new Intent(getApplicationContext(), NavigationAdmin.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        } else if (id == R.id.nav_sub) {
            Intent i = new Intent(getApplicationContext(), SubCategory.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        } else if (id == R.id.nav_service) {
            Intent i = new Intent(getApplicationContext(), AllServices.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        }else if (id == R.id.nav_viewfeed) {
            Intent i = new Intent(getApplicationContext(), ViewFeedback.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        }
        else if (id == R.id.nav_viewvendorfeed) {
            Intent i = new Intent(getApplicationContext(), Vendor_FeedbackView.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        }
        else if (id == R.id.nav_member) {
            Intent i = new Intent(getApplicationContext(), MemberActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        }
        else if (id == R.id.send_noti) {
            Intent i = new Intent(getApplicationContext(), SendNotification.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        }
        else if (id == R.id.view_noti) {
            Intent i = new Intent(getApplicationContext(), ViewNotification.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        }
        else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout()
    {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Want to Logout !")
                .setConfirmText("Yes!")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        if (AppController.getInstance().isOnline()) {
                            String MobToken    = "";

                            MobToken = shaved_shared_preferences.get_MobToken();


                            shaved_shared_preferences.set_user_login(0);

                            serviceDb.delall();
                            stateDb.delall();
                            cityDb.delall();

                            if(MobToken.equalsIgnoreCase("") ||MobToken==null)
                            {
                                shaved_shared_preferences.set_user_login(0);
                                SENDDATA();
                                finish();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    finishAffinity();
                                }
                            }
                            else {
                                shaved_shared_preferences.set_user_login(0);
                                SENDToken(MobToken);

                                SENDDATA();
                                finish();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    finishAffinity();
                                }
                            }
                        } else {
                            finish();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                finishAffinity();
                            }
                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                        }




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

    private void SENDToken(final String mobToken)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.Logout_noty,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG+"  SENDToken ", Const.Logout_noty+"\n"+ response.toString());


                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if (text.equals("1")) {


                            //    Toast.makeText(getApplicationContext(), "NOTIFICATION SERVICE LOGOUT", Toast.LENGTH_LONG).show();
                                progressDialog.hide();

                            }
                            else {

                             //   Toast.makeText(getApplicationContext(), "NOTIFICATION SERVICE ERROR", Toast.LENGTH_LONG).show();
                                progressDialog.hide();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                        }

                        progressDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NavigationAdmin.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Token", "" + mobToken);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SENDDATA() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.Logout_Admin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if (text.equals("1")) {

                                shaved_shared_preferences.set_user_login(0);
                                shaved_shared_preferences.set_Token("");
                                shaved_shared_preferences.set_TokenId("");
                                shaved_shared_preferences.set_userid("");

                                Toast.makeText(getApplicationContext(), "LOGOUT SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                progressDialog.hide();

                                finish();

                            }
                            else {

                                Toast.makeText(getApplicationContext(), "LOGOUT ERROR", Toast.LENGTH_LONG).show();
                                progressDialog.hide();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                        }

                        progressDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NavigationAdmin.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Id", "" + userId);
                params.put("Tokenid", "" + TokenId);
                params.put("Token", "" + Token);
                params.put("Sessionvalue", "0");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

                                     //   FETCH_STATE();

                                    }
                                }
                            }

                        }
                        catch (Exception e)
                        {
                            e.getMessage();
                        }

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

                                cityDb.insertRecord(cityId,city);

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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}

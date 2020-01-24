package com.FlashScreen.flashservice.InnnerClass;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.BuildConfig;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.MyAdapter;
import com.FlashScreen.flashservice.adapter.Pack;
import com.FlashScreen.flashservice.adapter.Player;
import com.FlashScreen.flashservice.database.CityDb;
import com.FlashScreen.flashservice.database.ServiceDb;
import com.FlashScreen.flashservice.database.StateDb;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.judemanutd.autostarter.AutoStartPermissionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bolts.Bolts;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class NavigationScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener {
    Shaved_shared_preferences shaved_shared_preferences;
    GridView gridview;
    //   String version="6";
    String TAG = "TAG_NavigationScreen";
    ArrayList<Player> arrayList;
    ArrayList<HashMap<String, String>> hash_arrayList;
    MyAdapter myAdapter;
    ServiceDb serviceDb;
    StateDb stateDb;
    CityDb cityDb;
    ProgressDialog progressDialog;
    private SliderLayout mDemoSlider;
    FloatingActionButton call_now;
    TextView call,tv_ver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDemoSlider = (SliderLayout) findViewById(R.id.sliders);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        gridview = (GridView) findViewById(R.id.gridview);
        call_now = (FloatingActionButton) findViewById(R.id.call_now);
        call = (TextView) findViewById(R.id.call);
        View header = navigationView.getHeaderView(0);
        tv_ver= header.findViewById(R.id.tv_ver);
        tv_ver.setText("Version :  " + BuildConfig.VERSION_NAME);

        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());
        progressDialog = new ProgressDialog(NavigationScreen.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait......");

        arrayList = new ArrayList<>();
        arrayList.clear();

        hash_arrayList = new ArrayList<>();
        hash_arrayList.clear();

        shaved_shared_preferences.set_packIdT("");
        shaved_shared_preferences.set_packNameT("");
        shaved_shared_preferences.set_price("");

        serviceDb = new ServiceDb(getApplicationContext());
        stateDb = new StateDb(getApplicationContext());
        cityDb = new CityDb(getApplicationContext());

        stateDb.delall();
        cityDb.delall();
        shaved_shared_preferences.set_user_add("0");


        callFetchVersionApi();
        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.call_now);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + Const.adminPhone_First));
                startActivity(intent);
         }
        });


        if (AppController.getInstance().isOnline()) {
            // init();
            Fetch_Banner();
            Fetch_Category();
        } else {

            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        setFacaebookCode();
        FETCH_STATE();
        AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(getApplicationContext());
 String string="autostart background= "+AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(getApplicationContext());

        Log.e(TAG, "autostart background= "+string);

     /*   if( (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(getApplicationContext()))==true)
        {
            Toast.makeText(getApplicationContext(), "auto start permission allowed", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "auto start permission not allowed", Toast.LENGTH_LONG).show();


        }*/


/*
else        {
            String manufacturer = android.os.Build.MANUFACTURER;
            try {
                Intent intent = new Intent();
                if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
                } else if ("Honor".equalsIgnoreCase(manufacturer)) {
                    intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                }

                List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.size() > 0) {
                    startActivity(intent);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
*/

    }

    private void FETCH_STATE() {
        progressDialog.setMessage("Please Wait ");
        if (!Const.catId.equalsIgnoreCase("")) {
            Log.e(TAG, "callBookService");

            progressDialog.setMessage("Please Wait , Pending order going to submit");
        }
        progressDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_STATE, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "FETCH_STATE= "+ response.toString());
                        progressDialog.hide();

                        try {
                            stateDb = new StateDb(getApplicationContext());

                            stateDb.delall();


                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                String state_Id = jsonObject.getString("state_Id");
                                String state_name = jsonObject.getString("state_name");
                                stateDb.insertRecord(state_Id, state_name);
                            }
                        } catch (JSONException e) {
                            progressDialog.hide();
                            e.printStackTrace();
                        }
                        FETCH_CITY();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
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
        if (!Const.catId.equalsIgnoreCase("")) {
            Log.e(TAG, "callBookService");

            progressDialog.setMessage("Please Wait , Pending order going to submit");
        }    progressDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_CITY, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "FETCH_CITY= "+ response.toString());
                        progressDialog.hide();

                        try {
                            cityDb = new CityDb(getApplicationContext());
                            cityDb.delall();

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String state_Id = jsonObject.getString("state_Id");


                                String cityId = jsonObject.getString("cityId");
                                String city = jsonObject.getString("city");

                                cityDb.insertRecord(cityId, city);

                            }

                            if (!Const.catId.equalsIgnoreCase("")) {

                                callSubmitOrder();
                            }
                        } catch (Exception e) {
                            progressDialog.hide();
                            Log.e(TAG + "  FETCH_CITY exception ", e.toString());
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
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

    private void callSubmitOrder() {
        if (!Const.catId.equalsIgnoreCase("")) {
            Log.e(TAG, "callBookService");

            progressDialog.setMessage("Please Wait , Pending order going to submit");
        }
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_SEND_SERVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG + "callBookService  ", "--" + response.toString());
                        progressDialog.hide();

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");
                            String Locationid = ob.getString("Locationid");

                            if (text.equals("1")) {
                                Toast.makeText(getApplicationContext(), "SERVICE ADDED SUCESSFULLY", Toast.LENGTH_LONG).show();
                                Const.catId = "";


                                Intent intent = new Intent(getApplicationContext(), ViewOrderDetail.class);
                                intent.putExtra("OrderId", "" + Locationid);
                                startActivity(intent);
                                finish();


                            } else {

                                Toast.makeText(getApplicationContext(), "SERVICE ERROR", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NavigationScreen.this, error.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.hide();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("categoryid", "" + Const.catId);
                params.put("customerid", "" + shaved_shared_preferences.get_userid());
                params.put("serviceid", "" + Const.serviceId);
                params.put("serviceprice", "" + Const.price);
                params.put("stateid", "" + Const.stateid);
                params.put("cityid", "" + Const.cityid);
                params.put("areaname", "" + Const.areaname);
                params.put("date", "" + Const.date);
                params.put("time", "" + Const.time);
                params.put("lat", "" + Const.lattitude);
                params.put("lng", "" + Const.longitude);
                params.put("orderFrom", "androidApp" );

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void setFacaebookCode() {

        String userID = "indiaWeb@" + shaved_shared_preferences.get_userid();
        AppEventsLogger.setUserID(userID);
//   AppEventsLogger.setUserID("123456");
// Set the User Properties
        Bundle user_props = new Bundle();
        user_props.putString("$state", "California");
        user_props.putString("$city", "Mohali");
        user_props.putString("$install_source", "staticSetTest");
        user_props.putString("token", "" + shaved_shared_preferences.get_MobToken());
        user_props.putString("shoeWidth", "D");
        user_props.putString("subscription", "Premium");

        AppEventsLogger.updateUserProperties(user_props, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    JSONObject graphResponse = response.getJSONObject();
                    Log.e(TAG + "Facebook_data", "try " + graphResponse.toString());
                } catch (Exception exp) {
//                    Toast.makeText(UserNavigation.this, "Facebook Exception=" + exp.toString(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG + "Facebook_data", "catch " + exp.toString());
                }
            }
        });


        //   AppEventsLogger.updateUserProperties(Bundle parameters, GraphRequest.Callback callback);
       /* fbq('init', '9998887776665554', {uid: '123456'});
        // Set the User Properties
        fbq('setUserProperties', '9998887776665554',
                {$state: 'California',
                $city: 'Menlo Park',
                shoeSize: '11',
                shoeWidth: 'D',
                subscription: 'premium'
        }
      )*/
    }

    private void callFetchVersionApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_VERSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            String text = object.getString("text");


                            if (text.equals("1")) {
                            } else {

                                new SweetAlertDialog(NavigationScreen.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Upgrade new Version?")
                                        .setContentText("Do you want to update app!")
                                        .setConfirmText("Yes!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));

                                            }
                                        })
                                        .setCancelText("No")
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                                sweetAlertDialog.cancel();
                                            }
                                        })
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NavigationScreen.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("version", "" + BuildConfig.VERSION_CODE);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
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
        shaved_shared_preferences.set_user_login(0);
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You Want to Exit Application !")
                .setConfirmText("Yes!")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        serviceDb.delall();
                        stateDb.delall();
                        cityDb.delall();

                        finish();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
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

    private void Fetch_Banner() {
        Log.d(TAG, Const.URL_FETCH_BANNER);
        progressDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_BANNER, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        HashMap<String, String> url_maps = new HashMap<String, String>();
                        try {

                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                String Id = jsonObject.getString("Id");
                                String name = jsonObject.getString("name");
                                String image_upload1 = jsonObject.getString("image_upload1");
                                String date = jsonObject.getString("date");
                                String Image = "http://iwwphp.info/flashservices/uploads/banner/" + image_upload1;

                                url_maps.put(name, Image);

                            }
                            for (String name : url_maps.keySet()) {
                                TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .image(url_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit);
                                // .setOnSliderClickListener(NavigationScreen.this);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", name);

                                mDemoSlider.addSlider(textSliderView);
                            }
                            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                            mDemoSlider.setDuration(4000);


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


    private void logout() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Want to Logout !")
                .setConfirmText("Yes!")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        shaved_shared_preferences.set_user_login(0);
                        shaved_shared_preferences.set_userid(null);

                        serviceDb.delall();
                        stateDb.delall();
                        cityDb.delall();


                        if (AppController.getInstance().isOnline()) {
                            String MobToken = "";

                            MobToken = shaved_shared_preferences.get_MobToken();


                            shaved_shared_preferences.set_user_login(0);

                            logoutTokenApi(MobToken);

                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                finishAffinity();
                            }
                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                        Intent intent = new Intent(NavigationScreen.this, NavigationUser.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
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

    private void logoutTokenApi(final String mobToken) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.Logout_noty_user,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG+"  logoutTokenApi ", Const.Logout_noty_user+"\n"+ response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if (text.equals("a")) {


                                //  Toast.makeText(getApplicationContext(), "NOTIFICATION SERVICE LOGOUT", Toast.LENGTH_LONG).show();
                                progressDialog.hide();

                            } else {

                                //  Toast.makeText(getApplicationContext(), "NOTIFICATION SERVICE ERROR", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(NavigationScreen.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void Fetch_Category() {
        progressDialog.show();
        Log.d(TAG, Const.URL_FETCH_CAT);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_CAT, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                Player player = new Player();

                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String category_Id = jsonObject.getString("category_Id");
                                String category_name = jsonObject.getString("category_name");
                                String image_upload1 = jsonObject.getString("image_upload1");
                                String date = jsonObject.getString("date");

                                player.setCategory_Id(category_Id);
                                player.setCategory_name(category_name);
                                player.setImage_upload(image_upload1);
                                player.setDate(date);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("category_Id", category_Id);
                                hashMap.put("category_name", category_name);
                                hash_arrayList.add(hashMap);
                                arrayList.add(player);
                            }

                            myAdapter = new MyAdapter(getApplicationContext(), arrayList);
                            gridview.setAdapter(myAdapter);
                            progressDialog.hide();

                            if (serviceDb.CATID().size() > 0) {

                            } else {
                                FetchPACKAGE();
                            }

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

    private void FetchPACKAGE() {
        progressDialog.show();
        serviceDb.delall();

        final JsonArrayRequest req = new JsonArrayRequest(Const.URL_FETCH_CAT_DETAILS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject jsonObject = response.getJSONObject(i);

                                    Log.e("jsonObject ", "" + jsonObject);

                                    String category_Idd = jsonObject.getString("category_Id");

                                    Log.e("messagewwjsonObject ", "" + category_Idd);

                                    JSONArray jsonArrays = jsonObject.getJSONArray("categorieslevelone");

                                    Log.e("messagewwjsonObject..b ", "" + jsonArrays);

                                    for (int j = 0; j < jsonArrays.length(); j++) {
                                        Pack pack = new Pack();

                                        JSONObject jsonObject22 = jsonArrays.getJSONObject(j);

                                        String service_Id = jsonObject22.getString("service_Id");
                                        String category_Id = jsonObject22.getString("category_Id");
                                        String service1 = jsonObject22.getString("service1");
                                        String price1 = jsonObject22.getString("price1");

                                        String category_name = "";

                                        for (int ii = 0; ii < hash_arrayList.size(); ii++) {
                                            String catid = hash_arrayList.get(ii).get("category_Id");

                                            if (category_Id.equalsIgnoreCase(catid)) {
                                                category_name = hash_arrayList.get(ii).get("category_name");
                                            }

                                        }

                                        serviceDb.insertRecord(category_Id, category_name, service_Id, service1, price1);


                                    }
                                }
                            }
                            progressDialog.hide();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        progressDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                "tag_json_arry");

    }

  /*  private void FETCH_STATE() {
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

                                stateDb.insertRecord(state_Id, state_name);
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

            *//**
     * Passing some request headers
     * *//*
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

            */

    /**
     * Passing some request headers
     *//*
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
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.user_nav_Home) {
            Intent i = new Intent(getApplicationContext(), NavigationScreen.class);
            startActivity(i);
            //  overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        } else if (id == R.id.user_nav_Profile) {
            Intent i = new Intent(getApplicationContext(), ProfilePage.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        } else if (id == R.id.user_nav_history) {
            Intent i = new Intent(getApplicationContext(), History.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        } else if (id == R.id.user_view_notification) {
            Intent i = new Intent(getApplicationContext(), Notification_View_User.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        } else if (id == R.id.user_nav_feedback) {
            Intent i = new Intent(getApplicationContext(), Feedback.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        } else if (id == R.id.user_Vendor_SignUp) {
            Intent i = new Intent(getApplicationContext(), VendorSignup.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();

        } else if (id == R.id.user_follow_on_social) {
            Intent i = new Intent(getApplicationContext(), FollowUs_Sec.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        } else if (id == R.id.user_social_share) {
            socialShare();
        } else if (id == R.id.user_contactUs) {
            Intent i = new Intent(getApplicationContext(), ContactCustomerSupport.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

        }
      /*  else if (id == R.id.user_UpdatePsd) {
            Intent i = new Intent(getApplicationContext(), UpdatePassword.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        }*/
        else if (id == R.id.user_nav_logout) {
            logout();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void socialShare() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Flash Services");
        share.putExtra(Intent.EXTRA_TEXT,
                "Flash Services \n" +
                        "Download the App and Enjoy the Services ,\n http://bit.ly/flashservices ");

        startActivity(Intent.createChooser(share, "Share to Friends !"));

        //Flash Services 30 min free service on first order, download the app now http://bit.ly/flashservices
        //Now


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

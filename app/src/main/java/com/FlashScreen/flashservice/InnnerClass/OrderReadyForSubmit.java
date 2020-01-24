package com.FlashScreen.flashservice.InnnerClass;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.GPSTracker;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.GoogleClasses.GooglePlacesAutocompleteAdapter;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.database.CityDb;
import com.FlashScreen.flashservice.database.StateDb;
import com.FlashScreen.flashservice.startscreen.RegisterAccount;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderReadyForSubmit extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Context context;
    String TAG = "OrderReadyForSubmit ", catId, getimage, getname, getprice, getserviceId, dummyArea = "";
    StateDb stateDb;
    CityDb cityDb;
    ProgressDialog progressDialog;
    TextView btn_gotoMap, btn_submitOrder, title_name, date_set, price_estimate, tv_back, tv_fetchedAddress;
    String packId;
    String DATE = "0", TIME = "Choose Time", str_FinalTIME = "";
    int mHour, mMinute, mSecond;
    Shaved_shared_preferences shaved_shared_preferences;
    ArrayList<String> arrayList_State, arrayList_StateId, arrayList_City, arrayList_CityId, arrayListTime, arryLstSingleTm;
    ImageView imageView, iv_closeSearchLoc, iv_clearTxt;
    Spinner spinner_state, spinner_time;
    SearchableSpinner spinner_city;
    String userId, name, mobile, email, password, member, packages, Seller, Packageid;
    EditText area_set;
    ArrayAdapter<String> stateadp, cityadp, timeAdap;

    String STATE_ID = "", STATE = "", CITY_ID = "", CITY = "", AREA = "", STATE_IDD = "", CITY_IDD = "";
    Calendar calendar;
    int Year, Month, Day;
    Date dd;
    Date CNT_date;
    String current_date;
    LinearLayout linear;
    Boolean bol = false;
    LinearLayout ll_submitOrder, ll_mapAddress, ll_googlePlaces;
    SupportMapFragment mapFragment;
    private GoogleMap googleMap_address;
    private double lattitude = 0.0, longitude = 0.0;
    FrameLayout fl_map;
    GPSTracker gpsTracker;
    private int num = 0;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199, autoPlaceIntentCode = 1234;
    TextView tv_editLoc;
    AutoCompleteTextView autoTv_place;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ready_for_submit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        // actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        stateDb = new StateDb(getApplicationContext());
        cityDb = new CityDb(getApplicationContext());

        imageView = (ImageView) findViewById(R.id.imageView);
        title_name = (TextView) findViewById(R.id.title_name);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        date_set = (TextView) findViewById(R.id.date_set);
        price_estimate = (TextView) findViewById(R.id.price_estimate);
        tv_fetchedAddress = (TextView) findViewById(R.id.tv_fetchedAddress);
        linear = (LinearLayout) findViewById(R.id.linear);
        ll_submitOrder = (LinearLayout) findViewById(R.id.ll_submitOrder);
        ll_mapAddress = (LinearLayout) findViewById(R.id.ll_mapAddress);
        ll_mapAddress.setOnClickListener(this);
        area_set = (EditText) findViewById(R.id.area_set);

        spinner_state = (Spinner) findViewById(R.id.spinner_state);
        spinner_city = (SearchableSpinner) findViewById(R.id.spinner_city);
        spinner_time = (Spinner) findViewById(R.id.spinner_time);
        tv_editLoc = (TextView) findViewById(R.id.tv_editLoc);
        ll_googlePlaces = (LinearLayout) findViewById(R.id.ll_googlePlaces);
        ll_googlePlaces.setOnClickListener(this);
        iv_closeSearchLoc = (ImageView) findViewById(R.id.iv_closeSearchLoc);
        iv_closeSearchLoc.setOnClickListener(this);
        iv_clearTxt = (ImageView) findViewById(R.id.iv_clearTxt);
        iv_clearTxt.setOnClickListener(this);
        autoTv_place = (AutoCompleteTextView) findViewById(R.id.autoTv_place);
        arrayListTime = new ArrayList<>();
        arrayListTime.add("Choose Time");
        arryLstSingleTm = new ArrayList<>();
        arryLstSingleTm.add("Choose Time");
        timeAdap = new ArrayAdapter<String>(OrderReadyForSubmit.this, R.layout.textview, arrayListTime) {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = null;
                v = super.getDropDownView(position, null, parent);
                // If this is the selected item position
                // for other views
                TextView text = (TextView) v.findViewById(R.id.textvw);
                text.setTextColor(Color.parseColor("#3d406b"));
                Drawable backgroundColor = getResources().getDrawable(R.drawable.box);
                v.setBackground(backgroundColor);
                return v;
            }
        };
        timeAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeAdap.setDropDownViewResource(R.layout.textview);
        spinner_time.setAdapter(timeAdap);
        progressDialog = new ProgressDialog(OrderReadyForSubmit.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait Fetching Areas...");
        progressDialog.setCancelable(true);

        arrayList_StateId = new ArrayList<>();
        arrayList_State = new ArrayList<>();
        arrayList_StateId.clear();
        arrayList_State.clear();

        arrayList_CityId = new ArrayList<>();
        arrayList_City = new ArrayList<>();
        arrayList_CityId.clear();
        arrayList_City.clear();
        gpsTracker = new GPSTracker(OrderReadyForSubmit.this);
        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());

        userId = shaved_shared_preferences.get_userid();
        mobile = shaved_shared_preferences.get_phone();
        email = shaved_shared_preferences.get_user_email();

        Log.e(TAG + " ", "userId =" + userId + "   mobile=" + mobile + "   email=" + email);

        shaved_shared_preferences.set_packIdT("1");
        shaved_shared_preferences.set_packNameT("Free");

        packId = shaved_shared_preferences.get_packId();


        arrayList_State.clear();
        arrayList_StateId.clear();

        arrayList_State.add("Select City");
        arrayList_StateId.add("0");

        btn_gotoMap = (TextView) findViewById(R.id.btn_gotoMap);
        btn_submitOrder = (TextView) findViewById(R.id.btn_submitOrder);

        Intent i = getIntent();
        catId = i.getStringExtra("catId");
        getname = i.getStringExtra("name");
        getimage = i.getStringExtra("image");
        getprice = i.getStringExtra("price");
        getserviceId = i.getStringExtra("serviceId");

        STATE_IDD = i.getStringExtra("stateid");
        CITY_IDD = i.getStringExtra("cityid");
        AREA = i.getStringExtra("areaname");
        DATE = i.getStringExtra("date");
        TIME = i.getStringExtra("time");

        try {
            lattitude = Double.parseDouble(0 + i.getStringExtra("lattitude"));
            longitude = Double.parseDouble(0 + i.getStringExtra("longitude"));
        } catch (Exception e) {

        }
        if (i.getStringExtra("lattitude") == null || i.getStringExtra("lattitude").equals(""))
            lattitude = 0;
        else
            lattitude = Double.parseDouble(i.getStringExtra("lattitude"));

        if (i.getStringExtra("longitude") == null || i.getStringExtra("longitude").equals(""))
            longitude = 0;
        else
            longitude = Double.parseDouble(i.getStringExtra("longitude"));

        Log.e(TAG + "DATAAA_v1 ", "catId-- " + catId);
        Log.e(TAG + "DATAAA_v1 ", "getname-- " + getname);
        Log.e(TAG + "DATAAA_v1 ", "getimage-- " + getimage);
        Log.e(TAG + "DATAAA_v1 ", "getprice-- " + getprice);
        Log.e(TAG + "DATAAA_v1 ", "getserviceId-- " + getserviceId);

        Log.e(TAG + "DATAAA_v1 ", "STATE_IDD-- " + STATE_IDD);
        Log.e(TAG + "DATAAA_v1 ", "CITY_IDD-- " + CITY_IDD);
        Log.e(TAG + "DATAAA_v1 ", "AREA-- " + AREA);
        Log.e(TAG + "DATAAA_v1 ", "DATE-- " + DATE);
        Log.e(TAG + "DATAAA_v1 ", "TIME-- " + TIME);
        Log.e(TAG + "DATAAA_v1 ", "lattitude-- " + lattitude + "   longitude=" + longitude);

        if (STATE_IDD.equalsIgnoreCase("0") || CITY_IDD.equalsIgnoreCase("0")) {
            STATE_IDD = "0";
            CITY_IDD = "0";
        }

        title_name.setText(getname);
        price_estimate.setText("Estimated Cost Rs." + getprice);

        area_set.setText(AREA);

        if (DATE.equalsIgnoreCase("0")) {
            date_set.setText(" Select Date");
        } else {
            date_set.setText(DATE);
        }


        Log.e("imagee ", "---" + getimage);

        //   Picasso.with(getApplicationContext()).load(getimage).into(imageView);


        linear.setVisibility(View.GONE);


        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH) + 1;
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        String ss = "";
        if (Month < 10) {
            ss = "0" + Month;
        } else {
            ss = "" + Month;
        }
        current_date = Year + "-" + ss + "-" + Day;

        Log.e("vvvvvvvuu ", "==" + current_date);
        // DATE =  parseDateToddMMyyyy(DATE);

        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mSecond = calendar.get(Calendar.SECOND);

        //  TIME = mHour + ":" + mMinute+":"+mSecond;

        if ((lattitude == 0.0 && longitude == 0.0)) {
            gpsTracker.getLocation();
            lattitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            if (lattitude == 0.0 || longitude == 0.0) {
                TurnOnGps();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Toast.makeText(OrderReadyForSubmit.this, "Please GoTo Setting and Allow the Location Permissions for Registeration", Toast.LENGTH_LONG).show();
            }
            //askForGPS();
        }
        Log.e("askForGPS", "" + lattitude + "   lng=" + longitude);
        initMap();

        if (isOnline()) {

            FETCH_STATE();
            FETCH_TIME();
//call_MapStatusForShow_api();
           /* if(dummyArea.equalsIgnoreCase(""))
                dummyArea="ludhiana punjab";

            String urlLocAdd = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + dummyArea + "&inputtype=textquery&fields=formatted_address,name,geometry&key=AIzaSyC8jFU-yrO7rzRh4gWaMlB3L82U1gDc_3A";
            callGoogleLocationLatLng(urlLocAdd);*/
            ll_submitOrder.setVisibility(View.VISIBLE);
            btn_gotoMap.setVisibility(View.GONE);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        }


        date_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dpd = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int s = monthOfYear + 1;
                        int D = dayOfMonth;
                        String ss = "";

                        if (s < 10) {
                            ss = "0" + s;
                        } else {
                            ss = "" + s;
                        }

                        String DD = "";

                        if (D < 10) {
                            DD = "0" + D;
                        } else {
                            DD = "" + D;
                        }

                        String a = year + "-" + ss + "-" + DD;
                        DATE = a;
                        current_date = a;

                        //  DATE =  parseDateToddMMyyyy(DATE);

                        Log.e("search_datee ", "--" + DATE);

                        date_set.setText(" " + parseDateToddMMyyyy(DATE));

                        Log.e("Selected_Date : ", "to_calender--" + DATE);
                    }
                };
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    dd = sdf.parse("2017-01-01");
                    CNT_date = sdf.parse(current_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                DatePickerDialog d = new DatePickerDialog(OrderReadyForSubmit.this, dpd, Year, Month - 1, Day);
                //  d.getDatePicker().setMaxDate(System.currentTimeMillis());
                d.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                //  d.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                // d.getDatePicker().setMinDate(dd.getTime());
                d.show();


            }

        });


        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isOnline()) {

                    STATE_ID = arrayList_StateId.get(position);
                    STATE = arrayList_State.get(position);

                    Log.e("STATE_ID..", "-----" + STATE_ID);
                    Log.e("STATE..", "-----" + STATE);

                    if (STATE_ID.equalsIgnoreCase("0")) {
                        bol = false;
                        linear.setVisibility(View.GONE);
                        arrayList_City.clear();
                        arrayList_City.clear();


                        spinner_city.setSelection(position);

                        for (int k = 0; k < arrayList_StateId.size(); k++) {
                            String state = arrayList_StateId.get(k);
                            if (state.equalsIgnoreCase(STATE_IDD)) {
                                spinner_state.setSelection(k);
                            }
                        }

                    } else {
                        bol = false;
                        linear.setVisibility(View.VISIBLE);
                        FETCH_CITY(STATE_ID);

                    }

                } else {

                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /////////////////////////////
        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TIME = arrayListTime.get(position);
                Log.e("time..", "-----" + TIME);
                if (TIME.equalsIgnoreCase("Choose Time")) {
                    bol = false;
                    //   Toast.makeText(getApplicationContext(),"Please Select Area",Toast.LENGTH_LONG).show();
                } else {
                    bol = true;
                    TIME = arrayListTime.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                CITY_ID = arrayList_CityId.get(position);
                CITY = arrayList_City.get(position);

                Log.e("city123..", "-----" + CITY_ID);
                Log.e("city123..", "-----" + CITY);

                if (CITY_ID.equalsIgnoreCase("0")) {
                    bol = false;

                    for (int k = 0; k < arrayList_CityId.size(); k++) {
                        String city = arrayList_CityId.get(k);
                        if (city.equalsIgnoreCase(CITY_IDD)) {
                            spinner_city.setSelection(k);
                        }
                    }


                } else {
                    bol = true;
                    CITY_ID = arrayList_CityId.get(position);
                    CITY = arrayList_City.get(position);


                    try {
                        dummyArea = CITY + " ludhiana punjab";
                        String urlLocAdd = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + dummyArea + "&inputtype=textquery&fields=formatted_address,name,geometry&key=AIzaSyC8jFU-yrO7rzRh4gWaMlB3L82U1gDc_3A";
                        callGoogleLocationLatLng(urlLocAdd);
                        num=0;
                    } catch (Exception exp) {
                        Log.e(TAG + " selected city to map", "exp=" + exp.toString());
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_gotoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AREA = area_set.getText().toString();

                Log.e(TAG + ".put arryLstSingleTm", "" + arryLstSingleTm.get(spinner_time.getSelectedItemPosition()));

                Log.e(TAG + ".put customerid", "" + userId);
                Log.e(TAG + ".put Price", "" + getprice);
                Log.e(TAG + ".put Catid", "" + catId);
                Log.e(TAG + ".put SERVICE_ID", "" + getserviceId);
                Log.e(TAG + ".put DATE", "" + DATE);
                Log.e(TAG + ".put TIME", "" + arryLstSingleTm.get(spinner_time.getSelectedItemPosition()));
                Log.e(TAG + ".put STATE", "" + STATE);
                Log.e(TAG + ".put STATE_ID", "" + STATE_ID);
                Log.e(TAG + ".put CITY", "" + CITY);
                Log.e(TAG + ".put CITY_ID", "" + CITY_ID);
                Log.e(TAG + ".put AREA", "" + AREA);
                Log.e(TAG + ".put lattitude  =", "" + lattitude + "    longitude=" + longitude);

                if ((!CITY_ID.isEmpty() && CITY_ID != "" && CITY_ID != "0") && (STATE_ID != "" && STATE_ID != "0")) {
                    if (AREA.length() > 0) {
                        if (DATE.equalsIgnoreCase("0")) {
                            Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_LONG).show();
                        } else {
                            if (TIME.equalsIgnoreCase("Choose Time")) {
                                Toast.makeText(getApplicationContext(), "Please Select Time", Toast.LENGTH_LONG).show();

                            } else {

                                Log.e("user_not", "--" + userId);
                                if (userId == null || userId.equalsIgnoreCase("")) {

                                    Intent i = new Intent(getApplicationContext(), RegisterAccount.class);
                                    i.putExtra("catId", "" + catId);
                                    i.putExtra("name", "" + getname);
                                    i.putExtra("image", "" + getimage);
                                    i.putExtra("price", "" + getprice);
                                    i.putExtra("serviceId", "" + getserviceId);
                                    i.putExtra("cityName", CITY);
                                    i.putExtra("stateName", STATE);
                                    i.putExtra("stateid", STATE_ID);
                                    i.putExtra("cityid", CITY_ID);
                                    i.putExtra("areaname", AREA);
                                    i.putExtra("date", DATE);
                                    i.putExtra("time", arryLstSingleTm.get(spinner_time.getSelectedItemPosition()));
                                    i.putExtra("lattitude", "" + lattitude);
                                    i.putExtra("longitude", "" + longitude);
                                    startActivity(i);
                                    overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                                    finish();


                                } else {
                                    //      SENDDATA();

                                    Intent i = new Intent(getApplicationContext(), ChooseMapLocation.class);
                                    i.putExtra("catId", "" + catId);
                                    i.putExtra("name", "" + getname);
                                    i.putExtra("image", "" + getimage);
                                    i.putExtra("price", "" + getprice);
                                    i.putExtra("serviceId", "" + getserviceId);
                                    i.putExtra("cityName", CITY);
                                    i.putExtra("stateName", STATE);
                                    i.putExtra("stateid", STATE_ID);
                                    i.putExtra("cityid", CITY_ID);
                                    i.putExtra("areaname", AREA);
                                    i.putExtra("date", DATE);
                                    i.putExtra("timeSingle", arrayListTime.get(spinner_time.getSelectedItemPosition()));
                                    i.putExtra("time", arryLstSingleTm.get(spinner_time.getSelectedItemPosition()));
                                    i.putExtra("lattitude", lattitude);
                                    i.putExtra("longitude", longitude);

                                    startActivity(i);
                                    overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                                    finish();
                                }
                            }
                        }

                    } else {
                        area_set.setError("Enter Address");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select City and Area", Toast.LENGTH_LONG).show();

                }
            }
        });

        btn_submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AREA = area_set.getText().toString();

                Log.e(TAG + ".put arryLstSingleTm", "" + arryLstSingleTm.get(spinner_time.getSelectedItemPosition()));

                Log.e(TAG + ".put customerid", "" + userId);
                Log.e(TAG + ".put Price", "" + getprice);
                Log.e(TAG + ".put Catid", "" + catId);
                Log.e(TAG + ".put SERVICE_ID", "" + getserviceId);
                Log.e(TAG + ".put DATE", "" + DATE);
                Log.e(TAG + ".put TIME", "" + arryLstSingleTm.get(spinner_time.getSelectedItemPosition()));
                Log.e(TAG + ".put STATE", "" + STATE);
                Log.e(TAG + ".put STATE_ID", "" + STATE_ID);
                Log.e(TAG + ".put CITY", "" + CITY);
                Log.e(TAG + ".put CITY_ID", "" + CITY_ID);
                Log.e(TAG + ".put AREA", "" + AREA);
                Log.e(TAG + ".put lattitude  =", "" + lattitude + "    longitude=" + longitude);

                if ((!CITY_ID.isEmpty() && CITY_ID != "" && CITY_ID != "0") && (STATE_ID != "" && STATE_ID != "0")) {
                    if (AREA.length() > 0) {
                        if (DATE.equalsIgnoreCase("0")) {
                            Toast.makeText(getApplicationContext(), "Please Select Date", Toast.LENGTH_LONG).show();
                        } else {
                            if (TIME.equalsIgnoreCase("Choose Time")) {
                                Toast.makeText(getApplicationContext(), "Please Select Time", Toast.LENGTH_LONG).show();

                            } else {


                                float[] results = new float[1];
                                Location.distanceBetween(30.907495, 75.855675, lattitude, longitude, results);
                                float distanceInMeters = results[0];
                                boolean isWithinRange = distanceInMeters < 18000;
                                Log.e("isWithinRange ", "" + isWithinRange);
                                if (isWithinRange == true) {

                                    Log.e(TAG + "callBookService  ", "--" + Const.URL_SEND_SERVICE + "?categoryid=" + catId + "&customerid=" + userId +
                                            "&serviceid=" + getserviceId + "&serviceprice=" + getprice + "&stateid=" + STATE_ID + "&cityid=" + CITY_ID +
                                            "&areaname=" + AREA + "&date=" + DATE + "&time=" + spinner_time.getSelectedItem() + "&lat=" + lattitude + "&lng=" + longitude);

                                    Log.e(TAG + "user_Logged_or_not ", "id =" + userId);
                                    if (userId == null || userId.equalsIgnoreCase("")) {
                                        addGlobalValuesForOrder();

                                        Intent i = new Intent(getApplicationContext(), RegisterAccount.class);
                                        i.putExtra("catId", "" + catId);
                                        i.putExtra("name", "" + getname);
                                        i.putExtra("image", "" + getimage);
                                        i.putExtra("price", "" + getprice);
                                        i.putExtra("serviceId", "" + getserviceId);
                                        i.putExtra("cityName", CITY);
                                        i.putExtra("stateName", STATE);
                                        i.putExtra("stateid", STATE_ID);
                                        i.putExtra("cityid", CITY_ID);
                                        i.putExtra("areaname", AREA);
                                        i.putExtra("date", DATE);
                                        i.putExtra("time", "" + spinner_time.getSelectedItem());
                                        i.putExtra("lattitude", lattitude);
                                        i.putExtra("longitude", longitude);

                                        startActivity(i);
                                        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                                        finish();


                                    } else {
                                        progressDialog.show();

                                        str_FinalTIME = arrayListTime.get(spinner_time.getSelectedItemPosition());
                                        try {
                                            if (str_FinalTIME.contains("am") || str_FinalTIME.contains("AM")) {
                                                str_FinalTIME = str_FinalTIME.replaceAll("am", "").trim();
                                                str_FinalTIME = str_FinalTIME.replaceAll("AM", "").trim();
                                                int numberTIME = Integer.parseInt(str_FinalTIME);
                                                str_FinalTIME = numberTIME + ":00:00";
                                            } else if (str_FinalTIME.contains("pm") || str_FinalTIME.contains("PM"))
                                                str_FinalTIME = str_FinalTIME.replaceAll("pm", "").trim();
                                            str_FinalTIME = str_FinalTIME.replaceAll("PM", "").trim();
                                            int numberTIME = Integer.parseInt(str_FinalTIME) + 12;
                                            str_FinalTIME = numberTIME + ":00:00";
                                        } catch (Exception e) {

                                        }
                                        callSubmitOrder();
                                    }
                                } else {
                                    double distance = distanceInMeters / 1000;
                                    String number = new DecimalFormat("##.##").format(distance);
                                    new SweetAlertDialog(OrderReadyForSubmit.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Sorry !")
                                            .setContentText("Services are only available in Ludhiana city , Set your location as Ludhiana")
                                            .setCancelText("No")
                                            .setConfirmText("Yes")
                                            .showCancelButton(true)
                                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.cancel();
                                                }
                                            })
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    dummyArea = "ludhiana punjab";
                                                    String urlLocAdd = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + dummyArea + "&inputtype=textquery&fields=formatted_address,name,geometry&key=AIzaSyC8jFU-yrO7rzRh4gWaMlB3L82U1gDc_3A";
                                                    callGoogleLocationLatLng(urlLocAdd);
                                                    num=0;
                                                }
                                            })
                                            .show();
                                }
                            }
                        }
                    } else {
                        area_set.setError("Enter Address");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select City and Area", Toast.LENGTH_LONG).show();

                }
            }
        });
        tv_editLoc.setOnClickListener(this);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        autoTv_place.setAdapter( new GooglePlacesAutocompleteAdapter(OrderReadyForSubmit.this, R.layout.list_item));


        autoTv_place.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoTv_place.getWindowToken(), 0);

                long id_item = parent.getSelectedItemId();
                String Str_id = String.valueOf(id_item);
                String selectedChoosedVal = autoTv_place.getText().toString();
                ll_googlePlaces.setVisibility(View.GONE);
                dummyArea = selectedChoosedVal;
                String urlLocAdd = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + dummyArea + "&inputtype=textquery&fields=formatted_address,name,geometry&key=" + getApplicationContext().getString(R.string.mapApi_key);   //AIzaSyC7DPo6b80-3LtRUAoHHuoNm6alYZfVuNE
                callGoogleLocationLatLng(urlLocAdd);

            }
        });
    }

    private void addGlobalValuesForOrder() {


        Const.catId = catId;
        Const.serviceId = getserviceId;
        Const.price = getprice;
        Const.stateid = STATE_ID;
        Const.cityid = CITY_ID;
        Const.areaname = AREA;

        Const.date = DATE;
        Const.time = "" + spinner_time.getSelectedItem();
        Const.lattitude = "" + lattitude;
        Const.longitude = "" + longitude;
    }

    private void TurnOnGps() {
        Log.e("permissionx ", "------");
        gpsTracker = new GPSTracker(OrderReadyForSubmit.this);

        if (gpsTracker.getIsGPSTrackingEnabled()) {

            Location loc = gpsTracker.getLocation();

            if (loc != null) {

                lattitude = loc.getLatitude();
                longitude = loc.getLongitude();

                String stringLatitude = Double.toString(loc.getLatitude());

                String stringLongitude = Double.toString(loc.getLongitude());

                Log.e("current_locationQQ", "--\n LAT " + stringLatitude + "\nLONG " + stringLongitude);

                if (stringLatitude.equalsIgnoreCase("0.0") || stringLongitude.equalsIgnoreCase("0.0")) {

                    if (checkLocationPermission()) {

                        Toast.makeText(OrderReadyForSubmit.this, "Turn ON GPS", Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(OrderReadyForSubmit.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                    }

                } else {

                    lattitude = Double.parseDouble(stringLatitude);
                    longitude = Double.parseDouble(stringLongitude);

                    Geocoder geocoder = new Geocoder(
                            OrderReadyForSubmit.this, Locale
                            .getDefault());
                    List<Address> addresses;

                }
            } else {


            }
        } else {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();

        }
        //  Toast.makeText(NavigationScreen.this,"Turn ON GPS", Toast.LENGTH_LONG).show();

    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void initMap() {
        fl_map = (FrameLayout) findViewById(R.id.fl_map);
        //  iv_mapRefresh = (LinearLayout) findViewById(R.id.iv_mapRefresh );
        gpsTracker = new GPSTracker(OrderReadyForSubmit.this);
        lattitude = gpsTracker.getLocation().getLatitude();
        longitude = gpsTracker.getLocation().getLongitude();

        if (mapFragment == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.frag_readySubmit);

            mapFragment.getMapAsync(this);
            // Check if we were successful in obtaining the map.
            if (mapFragment != null) {


            }
        }
    }

    private void callGoogleLocationLatLng(String urlLocAddress) {
        progressDialog.show();
        num++;
        urlLocAddress = urlLocAddress.replaceAll(",,", ",");
        urlLocAddress = urlLocAddress.replaceAll("#", "");
        urlLocAddress = urlLocAddress.replaceAll("  ", " ");
        urlLocAddress = urlLocAddress.replaceAll(" ", "%20");

        Log.e("<>API urlLocAddress", " " + urlLocAddress);
        //   int ciPo  = sp_cityName.getSelectedItemPosition();
        //  final String city = arrayListCity.get(ciPo).toString();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLocAddress,
                new Response.Listener<String>() {
                    //response handling
                    @Override
                    public void onResponse(String response) {
                        progressDialog.cancel();
                        Log.e(TAG + " callGoogleLocLatLng", " " + response);
                        String Status = null;

                        try {
                            JSONObject objMain = new JSONObject(response);
                            Status = objMain.getString("status");
                            if (Status.equalsIgnoreCase("OK")) {
                                JSONArray resultsArray = objMain.getJSONArray("candidates");
                                JSONObject arrObj = resultsArray.getJSONObject(0);
                                final String formatted_address = arrObj.getString("formatted_address");
                                JSONObject geometryObj = arrObj.getJSONObject("geometry");

                                JSONObject locationObj = geometryObj.getJSONObject("location");
                                lattitude = Double.parseDouble(locationObj.getString("lat"));
                                longitude = Double.parseDouble(locationObj.getString("lng"));

                                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                        new LatLng(lattitude, longitude)).zoom(14.0F)
                                        .bearing(300F) // orientation
                                        .tilt(30F) // viewing angle
                                        .build();

                                googleMap_address.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e(TAG, "choosed Location from map= " + formatted_address);
                                        //                                       tv_fetchedAddress.setText(formatted_address);
                                    }
                                }, 2000);
                            } else {
                                if (num < 3) {
//                             String       urlLocAdd = "https://maps.google.com/maps/api/geocode/json?address=" + city + "&sensor=false";
                                    if (dummyArea.equalsIgnoreCase(""))
                                        dummyArea = "ludhiana punjab";
                                    String urlLocAdd = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + dummyArea + "&inputtype=textquery&fields=formatted_address,name,geometry&key=AIzaSyC8jFU-yrO7rzRh4gWaMlB3L82U1gDc_3A";

                                    callGoogleLocationLatLng(urlLocAdd);
                                }
                            }
                            Log.e(TAG + "<>api status", "" + Status);


                        } catch (Exception e) {
                            //         alertMessage.cancelDialog();
                            Log.e(TAG + "excep callCategoryApi", "" + e.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.VolleyError error) {
                        progressDialog.cancel();
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "No internet Access, Check your internet connection.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {

            //send parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    private void call_MapStatusForShow_api() {
        //       http://iwwphp.info/flashservices/json/fetch_map_status.php
        Log.e(TAG + "  call_MapStatusForShow_api url ", Const.URL_GET_MAP_STATUS);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_GET_MAP_STATUS, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, response.toString());
                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                String show_map = jsonObject.getString("show_map");
                                if (show_map.equalsIgnoreCase("1")) {
                                    btn_gotoMap.setVisibility(View.VISIBLE);
                                    ll_submitOrder.setVisibility(View.GONE);
                                }
                            }

                        } catch (Exception e) {
                            Log.e(TAG + "  call_MapStatusForShow_api exception ", e.toString());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "call_MapStatusForShow_api exp: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             */
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
    }

    private void callSubmitOrder() {


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

                                Intent intent = new Intent(getApplicationContext(), ViewOrderDetail.class);
                                intent.putExtra("OrderId", "" + Locationid);
                                startActivity(intent);
                                finish();


                                Toast.makeText(getApplicationContext(), "SERVICE ADDED SUCESSFULLY", Toast.LENGTH_LONG).show();


                            } else {

                                Toast.makeText(getApplicationContext(), "SERVICE ERROR", Toast.LENGTH_LONG).show();

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
                        Toast.makeText(OrderReadyForSubmit.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("categoryid", "" + catId);
                params.put("customerid", "" + userId);
                params.put("serviceid", "" + getserviceId);
                params.put("serviceprice", "" + getprice);
                params.put("stateid", "" + STATE_ID);
                params.put("cityid", "" + CITY_ID);
                params.put("areaname", "" + AREA);
                params.put("date", "" + DATE);
                params.put("time", "" + str_FinalTIME);
                params.put("lat", "" + lattitude);
                params.put("lng", "" + longitude);
                params.put("orderFrom", "androidApp");

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


    private void FETCH_TIME() {
        arrayListTime = new ArrayList<>();
        arrayListTime.add("Choose Time");
        arryLstSingleTm = new ArrayList<>();
        arryLstSingleTm.add("Choose Time");
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_TIME, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                String timeSlot = jsonObject.getString("show_time");
                                arrayListTime.add(timeSlot);
                                String timeSingle = jsonObject.getString("add_time");
                                arryLstSingleTm.add(timeSingle);
                                Log.e("time", "timeSingle " + timeSingle + "    timeSlot=" + timeSlot);
                                if (TIME.equalsIgnoreCase("" + timeSingle)) {
                                    spinner_time.setSelection(j + 1);
                                }
                            }
                            timeAdap = new ArrayAdapter<String>(OrderReadyForSubmit.this, R.layout.textview, arrayListTime) {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // for other views
                                    TextView text = (TextView) v.findViewById(R.id.textvw);
                                    text.setTextColor(Color.parseColor("#3d406b"));

                                    Drawable backgroundColor = getResources().getDrawable(R.drawable.box);
                                    v.setBackground(backgroundColor);
                                    return v;
                                }
                            };

                            Log.e("arrayListTime", "" + arrayListTime.size());
                            timeAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            timeAdap.setDropDownViewResource(R.layout.textview);

                            progressDialog.hide();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        spinner_time.setAdapter(timeAdap);

                        if (TIME.equalsIgnoreCase("Choose Time")) {

                        } else {
                            for (int h = 0; h < arryLstSingleTm.size(); h++) {
                                if (arryLstSingleTm.get(h).toLowerCase().trim().contains(TIME)) {
                                    spinner_time.setSelection(h);
                                }
                            }
                        }
                        progressDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
                spinner_time.setAdapter(timeAdap);

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

    /* private void SENDDATA() {
         progressDialog.show();
         Log.e("TAGGGGG111 ", "--" + CITY_ID);
         StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_SEND_SERVICE,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {
                         Log.e("TAGGGGGG", response.toString());

                         try {
                             JSONObject ob = new JSONObject(response);
                             String text = ob.getString("text");
                             String Locationid = ob.getString("Locationid");

                             if (text.equals("1")) {
                                 Log.e("ooooooooooww 123", "-1-");

                                 Intent intent = new Intent(getApplicationContext(), ViewOrderDetail.class);
                                 intent.putExtra("OrderId", "" + Locationid);
                                 intent.putExtra("Active", "1");
                                 startActivity(intent);
                                 finish();

                                 Toast.makeText(getApplicationContext(), "SERVICE ADDED SUCESSFULLY", Toast.LENGTH_LONG).show();
                                 progressDialog.hide();

                             } else {

                                 Toast.makeText(getApplicationContext(), "SERVICE ERROR", Toast.LENGTH_LONG).show();
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
                         Toast.makeText(OrderReadyForSubmit.this, error.toString(), Toast.LENGTH_LONG).show();
                     }
                 }) {
             @Override
             protected Map<String, String> getParams() {
                 Map<String, String> params = new HashMap<String, String>();

                 params.put("categoryid", "" + catId);
                 params.put("customerid", "" + userId);
                 params.put("serviceid", "" + getserviceId);
                 params.put("serviceprice", "" + getprice);

                 params.put("stateid", "" + STATE_ID);
                 params.put("cityid", "" + CITY_ID);
                 params.put("areaname", "fff" + AREA);
                 params.put("date", "" + DATE);
                 params.put("time", "" + TIME);
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

 */
    private void FETCH_STATE() {

        progressDialog.show();
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

                                arrayList_StateId.add(state_Id);
                                arrayList_State.add(state_name);

                                stateDb.insertRecord(state_Id, state_name);

                            }

                            stateadp = new ArrayAdapter<String>(OrderReadyForSubmit.this, R.layout.textview, arrayList_State) {

                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position

                                    // for other views
                                    TextView text = (TextView) v.findViewById(R.id.textvw);
                                    text.setTextColor(Color.parseColor("#3d406b"));
                                    Drawable backgroundColor = getResources().getDrawable(R.drawable.box);
                                    v.setBackground(backgroundColor);


                                    return v;
                                }
                            };


                            // Drop down layout style - list view with radio button
                            stateadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            stateadp.setDropDownViewResource(R.layout.textview);
                            spinner_state.setAdapter(stateadp);

                            progressDialog.hide();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (STATE_IDD.equalsIgnoreCase("0")) {

                        } else {
                            for (int h = 0; h < arrayList_StateId.size(); h++) {
                                if (STATE_IDD.equalsIgnoreCase(arrayList_StateId.get(h))) {
                                    spinner_state.setSelection(h);
                                }
                            }
                        }
                        if (CITY_IDD.equalsIgnoreCase("0")) {

                        } else {
                            bol = false;
                            linear.setVisibility(View.VISIBLE);
                            FETCH_CITY(STATE_IDD);
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

    private void FETCH_CITY(final String mystate) {
        progressDialog.show();

        arrayList_City.clear();
        arrayList_CityId.clear();

        Log.e("state..", "-----" + mystate);

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

                                if (mystate.equalsIgnoreCase(state_Id)) {
                                    String cityId = jsonObject.getString("cityId");
                                    String city = jsonObject.getString("city");

                                    arrayList_CityId.add(cityId);
                                    arrayList_City.add(city);

                                    cityDb.insertRecord(cityId, city);
                                }
                            }
                            cityadp = new ArrayAdapter<String>(OrderReadyForSubmit.this, R.layout.textview, arrayList_City) {

                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position

                                    // for other views
                                    TextView text = (TextView) v.findViewById(R.id.textvw);
                                    text.setTextColor(Color.parseColor("#3d406b"));
                                    Drawable backgroundColor = getResources().getDrawable(R.drawable.box);
                                    v.setBackground(backgroundColor);


                                    return v;
                                }
                            };


                            // Drop down layout style - list view with radio button
                            cityadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cityadp.setDropDownViewResource(R.layout.textview);
                            spinner_city.setAdapter(cityadp);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.hide();

                        if (CITY_IDD.equalsIgnoreCase("0")) {

                        } else {
                            for (int h = 0; h < arrayList_CityId.size(); h++) {
                                if (CITY_IDD.equalsIgnoreCase(arrayList_CityId.get(h))) {
                                    spinner_city.setSelection(h);
                                }
                            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                progressDialog.hide();
                Intent intent = new Intent(OrderReadyForSubmit.this, MyServices.class);
                intent.putExtra("catId", "" + catId);
                intent.putExtra("name", "" + getname);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String parse(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "yy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public void onBackPressed() {
        progressDialog.hide();
        Intent intent = new Intent(OrderReadyForSubmit.this, MyServices.class);
        intent.putExtra("catId", "" + catId);
        intent.putExtra("name", "" + getname);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
        finish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                onBackPressed();
                break;

            case R.id.tv_editLoc:
                ll_googlePlaces.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_closeSearchLoc:
                ll_googlePlaces.setVisibility(View.GONE);
                break;
            case R.id.iv_clearTxt:
                autoTv_place.setText("");
                break;
            case R.id.ll_googlePlaces:
                break;

        }
    }

 /*   private void showAutoPlacesIntent() {
        AutocompleteFilter.Builder filterBuilder = new AutocompleteFilter.Builder();
        filterBuilder.setCountry("IN");

        filterBuilder.setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS);

        LatLngBounds latLngBounds = new LatLngBounds(
                new LatLng(30.903976, 75.840461),
                new LatLng(30.931614, 75.871338));


        try {
           Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                  //          .zzeZ("Enter Area Address")
                            .zzlc(65)
                            .setBoundsBias(latLngBounds)
                            .setFilter(filterBuilder.build())
                            .build(this);
            startActivityForResult(intent, autoPlaceIntentCode);

        } catch (Exception e) {
            Log.e(TAG, e.getStackTrace().toString());
        }



    }*/

/*
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
            }
        }, 1500);*/
/*
        if (requestCode == autoPlaceIntentCode) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 1500);
            Log.e(TAG+"  onActivityResult ", "  Address=" + resultCode+"  "+data.getDataString());

            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e(TAG, " place.. " + place.getLatLng() + "     Address=" + place.getAddress() );
                lattitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;

                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(lattitude, longitude)).zoom(14.0F)
                        .bearing(300F) // orientation
                        .tilt(30F) // viewing angle
                        .build();

                googleMap_address.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  }
            else{
                Toast.makeText(this,"Please choose another ",Toast.LENGTH_LONG).show();
            }
              }
*//*


    }
*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap_address = googleMap;
        googleMap_address.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                lattitude = cameraPosition.target.latitude;
                longitude = cameraPosition.target.longitude;
                Geocoder geocoder = new Geocoder(
                        OrderReadyForSubmit.this, Locale
                        .getDefault());
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocation(lattitude, longitude, 1);
                    //         Log.e(TAG+" onMapReady", "changedMap_location " + addresses);
                    if (addresses.size() != 0) {
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        Log.e(TAG + "changedMap_location ", address);
                        tv_fetchedAddress.setText("" + address);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.e("onCameraChange", "lat=  " + lattitude + "   lng=  " + longitude);

            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap_address.setMyLocationEnabled(true);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(lattitude, longitude)).zoom(14.0F)
                .bearing(300F) // orientation
                .tilt(30F) // viewing angle
                .build();

        googleMap_address.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        Log.e("cameraposition", "lat=  " + lattitude + "   lng=  " + longitude);

        // check if map is created successfully or not
        if (googleMap_address == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e("aaaaaaaaaa ", "----DONE----");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        Log.e("aaaaaaaaaa ", "----PENDING----");
                        try {
                            status.startResolutionForResult(
                                    OrderReadyForSubmit.this,
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("aaaaaaaaaa ", "----CHANGE----");
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}



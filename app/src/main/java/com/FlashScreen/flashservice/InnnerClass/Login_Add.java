package com.FlashScreen.flashservice.InnnerClass;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.startscreen.LoginActivity;
import com.FlashScreen.flashservice.startscreen.RegisterAccount;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by indiaweb on 6/27/2017.
 */
public class Login_Add extends AppCompatActivity {
    Context context;
    private static final String TAG = "Login_Add";
    private static final int REQUEST_SIGNUP = 0;
    String mobile, password;
    String catId, getname, getimage, getprice, getserviceId, Packageid;
    Shaved_shared_preferences sharedPreferences;
    @Bind(R.id.input_mobile)
    EditText _mobileText;
    @Bind(R.id.input_password)
    EditText mPasswordView;
    @Bind(R.id.customer_SignIn)
    TextView signIn_CUSTOMER;
    @Bind(R.id.customer_SignUp)
    TextView signUp_CUSTOMER;
    @Bind(R.id.forget)
    TextView FORGET;
    ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String STATE_ID, CITY_ID, AREA, DATE, TIME, REFER_CODE = "XXXX", CITY, STATE, lattitude = "", longitude = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__add);
        sharedPreferences = new Shaved_shared_preferences(getApplicationContext());
        progressDialog = new ProgressDialog(Login_Add.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating Account...");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        // actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        Intent i = getIntent();
        catId = i.getStringExtra("catId");
        getname = i.getStringExtra("name");
        getimage = i.getStringExtra("image");
        getprice = i.getStringExtra("price");
        getserviceId = i.getStringExtra("serviceId");

        STATE_ID = i.getStringExtra("stateid");
        CITY_ID = i.getStringExtra("cityid");
        AREA = i.getStringExtra("areaname");
        DATE = i.getStringExtra("date");
        TIME = i.getStringExtra("time");
        CITY = i.getStringExtra("cityName");
        STATE = i.getStringExtra("stateName");
        lattitude = i.getStringExtra("lattitude");
        longitude = i.getStringExtra("longitude");

        Log.e(TAG+" hold catId", "-- " + catId);
        Log.e(TAG+" hold getname ", "-- " + getname);
        Log.e(TAG+" hold getimage", "-- " + getimage);
        Log.e(TAG+" hold  getprice", "-- " + getprice);
        Log.e(TAG+" hold getserviceId", "-- " + getserviceId);

        Log.e(TAG+" hold STATE_ID", "-- " + STATE_ID);
        Log.e(TAG+" hold CITY_ID ", "-- " + CITY_ID);
        Log.e(TAG+" hold AREA ", "-- " + AREA);
        Log.e(TAG+" hold  DATE", "-- " + DATE);
        Log.e(TAG+" hold  TIME", "-- " + TIME);

        ButterKnife.bind(this);

        mPasswordView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                signIn_CUSTOMER.callOnClick();
                return false;
            }

        });

        signIn_CUSTOMER.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (AppController.getInstance().isOnline()) {


                    login();
                } else {

                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        signUp_CUSTOMER.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent i = new Intent(getApplicationContext(), RegisterAccount.class);
                i.putExtra("catId", "" + catId);
                i.putExtra("name", "" + getname);
                i.putExtra("image", "" + getimage);
                i.putExtra("price", "" + getprice);
                i.putExtra("serviceId", "" + getserviceId);
                i.putExtra("cityName", "" + CITY);
                i.putExtra("stateName", "" + STATE);
                i.putExtra("stateid", STATE_ID);
                i.putExtra("cityid", CITY_ID);
                i.putExtra("areaname", AREA);
                i.putExtra("date", DATE);
                i.putExtra("time", TIME);

                startActivity(i);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                finish();

            }
        });


        FORGET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
// custom dialog
                final Dialog dialog = new Dialog(Login_Add.this);
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("ENTER EMAIL");

                // set the custom dialog components - text, image and button
                final EditText text = (EditText) dialog.findViewById(R.id.input_email);


                TextView dialogButton = (TextView) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String TEXT = text.getText().toString();

                        if (TEXT.length() > 0) {
                            callForgetPsdApi(TEXT);
                        } else {
                            text.setError("ENTER EMAIL");
                        }
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
    }

    public void login() {

        mobile = _mobileText.getText().toString();
        password = mPasswordView.getText().toString();

        // TODO: Implement your own authentication logic here.

        boolean cancel = false;
        View focusView = null;

        // Check for a valid mobile, if the user entered one.
        if (TextUtils.isEmpty(mobile)) {
            _mobileText.setError(getString(R.string.error_field_required));
            focusView = _mobileText;
            cancel = true;
        } else if (!isMobile(mobile)) {
            _mobileText.setError(getString(R.string.error_invalid_number));
            focusView = _mobileText;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        Log.e(TAG+" boolean ", "..." + cancel);

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            progressDialog.show();
            loginApi();

        }
    }

    private boolean isMobile(String address) {
        //TODO: Replace this with your own logic
        if (address.length() >= 10) {
            return true;
        }
        return false;
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        if (password.length() > 0) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }


    private void callForgetPsdApi(final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.Add_forget,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG+" callForgetPsdApi", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if (text.equals("1")) {


                                Toast.makeText(getApplicationContext(), "Password send To Mail", Toast.LENGTH_LONG).show();


                            } else {


                                Toast.makeText(getApplicationContext(), "Server Error Or Invalid Email", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login_Add.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", "" + email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loginApi() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
progressDialog.cancel();
                        Log.e(TAG+"TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");
                            String type = ob.getString("type");

                            if (text.equals("1")) {
                                String CustomerId = ob.getString("customerId");
                                String Name = ob.getString("Name");
                                String Email = ob.getString("Email");
                                String Number = ob.getString("Number");
                                String ReferCode = ob.getString("ReferCode");
                                String ReferBy = ob.getString("ReferBy");

                                sharedPreferences.set_user_login(1);
                                sharedPreferences.set_userid(CustomerId);

                                sharedPreferences.set_name(Name);
                                sharedPreferences.set_phone(Number);
                                sharedPreferences.set_user_email(Email);
                                sharedPreferences.set_pass(password);
                                sharedPreferences.set_RefBy(ReferBy);
                                sharedPreferences.set_RefCode(ReferCode);


                                Intent i = new Intent(getApplicationContext(), NavigationScreen.class);
                            /*
                            Intent i = new Intent(getApplicationContext(), ServiceDetails.class);
                                i.putExtra("catId",""+catId);
                                i.putExtra("name",""+getname);
                                i.putExtra("image",""+getimage);
                                i.putExtra("price", "" + getprice);
                                i.putExtra("serviceId", "" + getserviceId);
                                i.putExtra("stateid",STATE_ID);
                                i.putExtra("cityid",CITY_ID);
                                i.putExtra("cityName", "" + CITY);
                                i.putExtra("stateName", "" + STATE);
                                i.putExtra("areaname",AREA);
                                i.putExtra("date",DATE);
                                i.putExtra("time",TIME);
                                i.putExtra("lattitude",lattitude);
                                i.putExtra("longitude",longitude);*/

                                startActivity(i);
                                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                                finish();


                                Toast.makeText(getApplicationContext(), "LOGIN SUCESSFULLY", Toast.LENGTH_LONG).show();
                                progressDialog.hide();

                                String token = sharedPreferences.get_MobToken();
                                if (token != null && token.length() > 0) {
                                    SEND_TOKEN(CustomerId, token);
                                }

                            } else if (text.equalsIgnoreCase("2")) {
                                new SweetAlertDialog(Login_Add.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Sorry !")
                                        .setContentText("" + type + "\n Contact with Admin :\n" + Const.adminPhone_First + "," + Const.adminPhone_Second/*+"\n"+Const.adminEmail*/)
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
                            } else {
                                new SweetAlertDialog(Login_Add.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Sorry !")
                                        .setContentText("" + type)
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
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
                        progressDialog.cancel();
                        Toast.makeText(Login_Add.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("number", "" + mobile);
                params.put("password", "" + password);
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
                Intent i = new Intent(getApplicationContext(), RegisterAccount.class);
                i.putExtra("catId", "" + catId);
                i.putExtra("name", "" + getname);
                i.putExtra("image", "" + getimage);
                i.putExtra("price", "" + getprice);
                i.putExtra("serviceId", "" + getserviceId);
                i.putExtra("stateid", STATE_ID);
                i.putExtra("cityName", "" + CITY);
                i.putExtra("stateName", "" + STATE);
                i.putExtra("cityid", CITY_ID);
                i.putExtra("areaname", AREA);
                i.putExtra("date", DATE);
                i.putExtra("time", TIME);
                i.putExtra("lattitude", "" + lattitude);
                i.putExtra("longitude", "" + longitude);
                startActivity(i);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
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

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), RegisterAccount.class);
        i.putExtra("catId", "" + catId);
        i.putExtra("name", "" + getname);
        i.putExtra("image", "" + getimage);
        i.putExtra("price", "" + getprice);
        i.putExtra("serviceId", "" + getserviceId);
        i.putExtra("cityName", "" + CITY);
        i.putExtra("stateName", "" + STATE);
        i.putExtra("stateid", STATE_ID);
        i.putExtra("cityid", CITY_ID);
        i.putExtra("areaname", AREA);
        i.putExtra("date", DATE);
        i.putExtra("time", TIME);
        i.putExtra("lattitude", "" + lattitude);
        i.putExtra("longitude", "" + longitude);

        startActivity(i);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        finish();
    }

    private void SEND_TOKEN(final String userid, final String mob_token) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.Save_user_Mob_token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG+"  SEND_TOKEN ", Const.Save_user_Mob_token+"\n"+ response.toString());


                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if (text.equals("1")) {
                                String type = ob.getString("type");

                                //  Toast.makeText(getApplicationContext(),"NOTIFICATION SERVICE STARTED SUCESSFULLY",Toast.LENGTH_LONG).show();
                                progressDialog.hide();


                            } else {

                                //  Toast.makeText(getApplicationContext(),"SERVER ERROR",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Login_Add.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", "" + userid);
                params.put("token", "" + mob_token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
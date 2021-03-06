package com.FlashScreen.flashservice.startscreen;

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
import com.FlashScreen.flashservice.InnnerClass.NavigationScreen;
import com.FlashScreen.flashservice.InnnerClass.NavigationUser;
import com.FlashScreen.flashservice.R;
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
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by indiaweb on 6/27/2017.
 */
public class LoginActivity extends AppCompatActivity {
    Context context;
    private static final String TAG = "TAG_LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    String mobile, password;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = new Shaved_shared_preferences(getApplicationContext());
        progressDialog = new ProgressDialog(LoginActivity.this,
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
                Intent intent = new Intent(getApplicationContext(), RegisterScreen.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });


        FORGET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
// custom dialog
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Enter Email/Mobile");

                // set the custom dialog components - text, image and button
                final EditText text = (EditText) dialog.findViewById(R.id.input_email);


                TextView dialogButton = (TextView) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String strEmailOrMob = text.getText().toString();
                        boolean atleastOneAlpha = strEmailOrMob.matches(".*[a-zA-Z]+.*");
                        if (atleastOneAlpha == true) {

                            if (!validateEmailId(strEmailOrMob))
                                text.setError("Enter a valid Email or Mobile ");
                            else {
                                dialog.dismiss();
                                sendEmailOrMsg(strEmailOrMob);
                            }
                        } else {
                            if (strEmailOrMob.length() != 10)
                                text.setError("Enter a valid Email or Mobile ");

                            else {
                                dialog.dismiss();
                                sendEmailOrMsg(strEmailOrMob);
                            }
                        }


                    }
                });

                dialog.show();

            }
        });
    }

    private boolean validateEmailId(String email) {
// TODO Auto-generated method stub
        final Pattern EMAIL_ADDRESS_PATTERN = Pattern
                .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                        + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "("
                        + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"
                        + ")+");

        boolean validEmail = EMAIL_ADDRESS_PATTERN.matcher(email)
                .matches();
        if (!validEmail) {
            return false;
        }
        return true;
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
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        Log.e("data123", "..." + cancel);

        if (cancel) {

            focusView.requestFocus();
        } else {

            progressDialog.show();
            SENDDATA();

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

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), NavigationUser.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }

    private void sendEmailOrMsg(final String emailOrMob) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.Add_forget,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG + " ", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");
                            String errMessage = ob.getString("type");

                            Toast.makeText(getApplicationContext(), "" + errMessage, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", "" + emailOrMob);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SENDDATA() {

        Log.e("LoginUrl", Const.URL_LOGIN + "?number=" + mobile + "&password=" + password);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());
                        progressDialog.hide();

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");
                            String type = ob.getString("type");

                            if (text.equalsIgnoreCase("1")) {
                                String CustomerId = ob.getString("customerId");
                                String Name = ob.getString("Name");
                                String Email = ob.getString("Email");
                                String Number = ob.getString("Number");
                                String ReferCode = ob.getString("ReferCode");
                                String ReferBy = ob.getString("ReferBy");

                                Log.e("REFFFF ", "--" + ReferCode);

                                sharedPreferences.set_user_login(1);
                                sharedPreferences.set_userid(CustomerId);
                                sharedPreferences.set_name(Name);
                                sharedPreferences.set_phone(Number);
                                sharedPreferences.set_user_email(Email);
                                sharedPreferences.set_pass(password);
                                sharedPreferences.set_RefBy(ReferBy);
                                sharedPreferences.set_RefCode(ReferCode);
                                Intent intent = new Intent(getApplicationContext(), NavigationScreen.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(getApplicationContext(), "LOGIN SUCESSFULLY", Toast.LENGTH_LONG).show();


                                String token = sharedPreferences.get_MobToken();
                                if (token != null && token.length() > 0) {
                                    SEND_TOKEN(CustomerId, token);
                                }


                            } else if (text.equalsIgnoreCase("2")) {
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                        } catch (Exception e) {
                            Log.e(TAG + "excep ", "--" + e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    private void SEND_TOKEN(final String userid, final String mob_token) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.Save_user_Mob_token,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG+"  SEND_TOKEN ", Const.Save_user_Mob_token+"\n response=     "+ response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");
                            progressDialog.hide();
                            if (text.equals("1")) {
                                String type = ob.getString("type");
                                //    Toast.makeText(getApplicationContext(),"NOTIFICATION SERVICE STARTED SUCESSFULLY",Toast.LENGTH_LONG).show();
                            } else {
                                //  Toast.makeText(getApplicationContext(),"SERVER ERROR",Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG + " SEND_TOKEN", "exception " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent i = new Intent(getApplicationContext(), NavigationUser.class);
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
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}
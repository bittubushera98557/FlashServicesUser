package com.FlashScreen.flashservice.startscreen;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.InnnerClass.NavigationUser;
import com.FlashScreen.flashservice.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {
    private String strTermsCond = "";
    private String mobileNum = "";

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    private static final String TAG = "RegisterScreen";
    private static final int REQUEST_SIGNUP = 0;
    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.refcode)
    TextView Refcode;
    //  @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_mobile)
    EditText _mobileText;
    @Bind(R.id.input_passwords)
    EditText _passwordText;
    @Bind(R.id.tv_termCond)
    TextView tv_termCond;

    // @Bind(R.id.input_reEnterPasswords) EditText _reEnterPasswordText;
    Shaved_shared_preferences sharedPreferences;
    @Bind(R.id.signUp_Customer)
    TextView signUp_CUSTOMER;
    @Bind(R.id.signIn_Customer)
    TextView signIn_CUSTOMER;
    @Bind(R.id.tv_loginHere)
    TextView tv_loginHere ;
    @Bind(R.id.chkBox_termCond)
    CheckBox chkBox_termCond;
    @Bind(R.id.scrVw_registerFields)
    ScrollView scrVw_registerFields;

    @Bind(R.id.fl_otpView)
    FrameLayout fl_otpView;

    @Bind(R.id.tv_resendOTP)
    TextView tv_resendOTP;
    @Bind(R.id.tv_submitOtp)
    TextView tv_submitOtp;
    @Bind(R.id.tv_getOTP)
    TextView tv_getOTP;

    @Bind(R.id.et_otp)
    EditText et_otp;
    @Bind(R.id.ll_getOTP)
    LinearLayout ll_getOTP;
    @Bind(R.id.ll_submitOTP)
    LinearLayout ll_submitOTP;
    @Bind(R.id.et_mobile)
    EditText et_mobile;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", strApiOTP = "";
    String name, REFER_CODE, email, mobile, password, reEnterPassword;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(RegisterScreen.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");

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

        sharedPreferences = new Shaved_shared_preferences(getApplicationContext());

        Refcode.setImeOptions(EditorInfo.IME_ACTION_DONE);

        Refcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                signUp_CUSTOMER.callOnClick();
                return false;
            }

        });


        signUp_CUSTOMER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.getInstance().isOnline()) {
                    signup();
                } else {

                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });

        tv_loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn_CUSTOMER.callOnClick();
            }
        });
        signIn_CUSTOMER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        tv_termCond.setText(Html.fromHtml("<B><U><font color=#F86F5C size=6>" + "Terms & Conditions" + "</font></U></B>"));


        tv_termCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTermsPopUp();
            }
        });

        if (AppController.getInstance().isOnline()) {
            callTermCondApi();
        } else {

            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
        fl_otpView.setVisibility(View.VISIBLE);
        scrVw_registerFields.setVisibility(View.GONE);
        ll_submitOTP.setVisibility(View.GONE);
        ll_getOTP.setVisibility(View.VISIBLE);
        tv_getOTP.setOnClickListener(this);
        tv_resendOTP.setOnClickListener(this);
        tv_submitOtp.setOnClickListener(this);

    }

    private void showTermsPopUp() {
        final Dialog mDialog;
        mDialog = new Dialog(RegisterScreen.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.popup_term_condition);
        TextView tv_i_agree, tv_text_terms;
        ImageView iv_closePopUp;
        tv_i_agree = (TextView) mDialog.findViewById(R.id.tv_i_agree);
        tv_text_terms = (TextView) mDialog.findViewById(R.id.tv_text_terms);

        iv_closePopUp = (ImageView) mDialog.findViewById(R.id.iv_closePopUp);
        tv_text_terms.setText("" + strTermsCond);

        iv_closePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        tv_i_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkBox_termCond.setChecked(true);
                mDialog.cancel();
            }
        });
        mDialog.show();
    }

    private void callTermCondApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_userTermCondition,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG + "", response.toString());
                        String CustomerId = "";
                        progressDialog.hide();
                        try {
                            JSONObject ob = new JSONObject(response);
                            //   String text = ob.getString("text");
                            String type = ob.getString("type");

                            JSONArray dataArr = ob.getJSONArray("data");
                            if (dataArr != null && dataArr.length() > 0) {
                                JSONObject jsonObject = dataArr.getJSONObject(0);
                                strTermsCond = jsonObject.getString("terms");

                            }


                        } catch (Exception e) {
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Toast.makeText(VendorNavigation.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void signup() {
        Log.d(TAG, "Signupmmmmmmmmmmmmm");

        boolean cancel = false;
        View focusView = null;
        REFER_CODE = Refcode.getText().toString();
        name = _nameText.getText().toString();
        // address = _addressText.getText().toString();
        email = _emailText.getText().toString();
        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();
        // reEnterPassword = _reEnterPasswordText.getText().toString();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            _passwordText.setError(getString(R.string.error_field_required));
            focusView = _passwordText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            _passwordText.setError(getString(R.string.error_invalid_password));
            focusView = _passwordText;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        // Check for a valid password, if the user entered one.
     /*   if (TextUtils.isEmpty(reEnterPassword)) {
            _reEnterPasswordText.setError(getString(R.string.error_field_required));
            focusView = _reEnterPasswordText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            _reEnterPasswordText.setError(getString(R.string.error_invalid_password));
            focusView = _reEnterPasswordText;
            cancel = true;
        }
*/
        // Check for a valid name
        else if (TextUtils.isEmpty(name)) {
            _nameText.setError(getString(R.string.error_field_required));
            focusView = _nameText;
            cancel = true;
        } else if (!isName(name)) {
            _nameText.setError(getString(R.string.error_invalid_name));
            focusView = _nameText;
            cancel = true;
        }

        // Check for a valid address
       /* if (TextUtils.isEmpty(address)) {
            _addressText.setError(getString(R.string.error_field_required));
            focusView = _addressText;
            cancel = true;
        } else if (!isAddress(address)) {
            _addressText.setError(getString(R.string.error_invalid_address));
            focusView = _addressText;
            cancel = true;
        }*/
        // Check for a valid email address.
        else if (TextUtils.isEmpty(email)) {
            _emailText.setError(getString(R.string.error_field_required));
            focusView = _emailText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            _emailText.setError(getString(R.string.error_invalid_email));
            focusView = _emailText;
            cancel = true;
        }

        // Check for a valid mobile, if the user entered one.
        else if (TextUtils.isEmpty(mobile)) {
            _mobileText.setError(getString(R.string.error_field_required));
            focusView = _mobileText;
            cancel = true;
        } else if (!isMobile(mobile)) {
            _mobileText.setError(getString(R.string.error_invalid_number));
            focusView = _mobileText;
            cancel = true;
        } else if (chkBox_termCond.isChecked() == false) {
            Toast.makeText(getApplicationContext(), "Please agree with Terms & Condition", Toast.LENGTH_LONG).show();
            focusView = chkBox_termCond;
            cancel = true;
        }


        //  Log.e("data123", "..." + cancel);

        else if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            progressDialog.show();
            SENDDATA();
            Log.e("Name", "" + name);
            Log.e("Phone", "" + mobile);
            Log.e("Email", "" + email);
            Log.e("Password", "" + password);
            Log.e("Referby", "" + REFER_CODE);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if (email.matches(emailPattern)) {
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


    private boolean isAddress(String address) {
        //TODO: Replace this with your own logic
        if (address.length() > 0) {
            return true;
        }
        return false;
    }

    private boolean isName(String name) {
        //TODO: Replace this with your own logic
        if (name.length() > 0) {
            return true;
        }
        return false;
    }


    private boolean isMobile(String address) {
        //TODO: Replace this with your own logic
        if (address.length() >= 10) {
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

    private void SENDDATA() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_REG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.cancel();
                        Log.e("TAGGGGGG", response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            String text = object.getString("text");
    if (text.equals("1")) {
                                // String type = object.getString("type");
                                //  String CustomerIdd = object.getString("CustomerId");

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivityForResult(intent, REQUEST_SIGNUP);
                                finish();
                                // sharedPreferences.set_userid(CustomerIdd);
                                Toast.makeText(getApplicationContext(), "REGISTER SUCESSFULLY", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    String Warning = object.getString("Warning");
                                    Toast.makeText(getApplicationContext(), "Sorry ! " + Warning, Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "REGISTER ERROR", Toast.LENGTH_LONG).show();

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterScreen.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Name", "" + name);
                params.put("Phone", "" + mobile);
                params.put("Email", "" + email);
                params.put("Password", "" + password);
                params.put("Referby", "" + REFER_CODE);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_getOTP:
                String strMobNum = et_mobile.getText().toString();
                if (strMobNum.equalsIgnoreCase("") && strMobNum.length() != 10) {

                }
                if (TextUtils.isEmpty(strMobNum)) {
                    et_mobile.setError(getString(R.string.error_field_required));


                } else if (!isMobile(strMobNum)) {
                    et_mobile.setError(getString(R.string.error_invalid_number));

                } else {
                    if (AppController.getInstance().isOnline()) {
  getOtpAPI("" + strMobNum);
                    } else {
        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }

                break;

            case R.id.tv_resendOTP:
                if (AppController.getInstance().isOnline()) {
    getOtpAPI("" + et_mobile.getText().toString());
                } else {
        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.tv_submitOtp:
                if (strApiOTP.equalsIgnoreCase("" + et_otp.getText().toString())) {
                  mobileNum = et_mobile.getText().toString();
                    fl_otpView.setVisibility(View.GONE);
                    scrVw_registerFields.setVisibility(View.VISIBLE);
                    _mobileText.setText("" + mobileNum);
                    _mobileText.setClickable(false);
                    _mobileText.setFocusable(false);
                    _mobileText.setFocusableInTouchMode(false);
                } else {
                    Toast.makeText(getApplicationContext(), "OTP not matched. Please try again", Toast.LENGTH_LONG).show();
                }
                break;

          /*  case R.id.tv_goto_SignIn:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
                break;*/

        }
    }

    private void getOtpAPI(final String strMobNum) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.OTP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG + "", response.toString());
                        String CustomerId = "";
                        progressDialog.hide();
                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");
                            String type = ob.getString("type");


                            if (text.equals("1")) {
                                String strOtp = ob.getString("otp");
                                strApiOTP = "" + strOtp;
                                ll_getOTP.setVisibility(View.GONE);
                                ll_submitOTP.setVisibility(View.VISIBLE);
                            } else {
                                new SweetAlertDialog(RegisterScreen.this, SweetAlertDialog.WARNING_TYPE)
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
                            e.printStackTrace();
                            progressDialog.hide();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterScreen.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("number", "" + strMobNum);
                params.put("user_type", "user" );
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}

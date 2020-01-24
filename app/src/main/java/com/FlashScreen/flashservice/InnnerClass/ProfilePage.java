package com.FlashScreen.flashservice.InnnerClass;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.startscreen.LoginActivity;
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

public class ProfilePage extends AppCompatActivity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    private static final String TAG = "ProfilePage";
    private static final int REQUEST_SIGNUP = 0;
    @Bind(R.id.input_name)
    EditText _nameText;
    //  @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.recode) TextView refercode;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_passwords) EditText _passwordText;
    // @Bind(R.id.input_reEnterPasswords) EditText _reEnterPasswordText;
    Shaved_shared_preferences sharedPreferences;
    @Bind(R.id.update)
    TextView signUp_CUSTOMER;
    @Bind(R.id.tv_updatePsd)
    TextView  tv_updatePsd;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String name ,text="" ,email,mobile ;
    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(ProfilePage.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating Account...");

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


        String htmlString="<u>Update Password</u>";
        tv_updatePsd.setText(Html.fromHtml(htmlString));

        sharedPreferences = new Shaved_shared_preferences(getApplicationContext());
         _nameText.setText(sharedPreferences.get_name());
        _mobileText.setText(sharedPreferences.get_phone());
        _emailText.setText(sharedPreferences.get_user_email());


        Log.e("REFFFF ", "==" + sharedPreferences.get_RefCode());


        refercode.setText("Ref code: " + sharedPreferences.get_RefCode());

        text = sharedPreferences.get_RefCode();

    /*    sharedPreferences.get_pass(password);*/

                _passwordText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        _passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                signUp_CUSTOMER.callOnClick();
                return false;
            }

        });

        tv_updatePsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this,UpdatePassword.class);
                startActivity(intent);
                finish();
            }
        });
        signUp_CUSTOMER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppController.getInstance().isOnline()) {


                    getEditTextValues();
                } else {

                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });


        refercode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {



                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Text Label", text);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(),"REFER CODE COPIED!",Toast.LENGTH_LONG).show();
            }
        });


    }

    public void getEditTextValues() {
        Log.d(TAG, "updateProfileApi");

        boolean cancel = false;
        View focusView = null;

        name = _nameText.getText().toString().trim();
        // address = _addressText.getText().toString();
        email = _emailText.getText().toString().trim();
        mobile = _mobileText.getText().toString().trim();

        // Check for a valid password, if the user entered one.
  /*      if (TextUtils.isEmpty(password)) {
            _passwordText.setError(getString(R.string.error_field_required));
            focusView = _passwordText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            _passwordText.setError(getString(R.string.error_invalid_password));
            focusView = _passwordText;
            cancel = true;
        }*/

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
        if (TextUtils.isEmpty(name)) {
            _nameText.setError(getString(R.string.error_field_required));
            focusView = _nameText;
            cancel = true;
        } else if (!isName(name)) {
            _nameText.setError(getString(R.string.error_invalid_name));
            focusView = _nameText;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            _emailText.setError(getString(R.string.error_field_required));
            focusView = _emailText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            _emailText.setError(getString(R.string.error_invalid_email));
            focusView = _emailText;
            cancel = true;
        }

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
/*
        if(password.equals(reEnterPassword))
        {
            _reEnterPasswordText.setError(getString(R.string.match));
            focusView = _reEnterPasswordText;
        }
        else {
            cancel = true;
        }*/

        Log.e("data123", "..." + cancel);

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            name = _nameText.getText().toString();
             email = _emailText.getText().toString();
            mobile = _mobileText.getText().toString();
            callUpdateProfileApi();

        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if(email.matches(emailPattern))
        {
            return  true;
        }
        return  false;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        if( password.length() > 0)
        {
            return  true;
        }
        return  false;
    }


    private boolean isAddress(String address) {
        //TODO: Replace this with your own logic
        if( address.length() > 0)
        {
            return  true;
        }
        return  false;
    }

    private boolean isName(String name) {
        //TODO: Replace this with your own logic
        if( name.length() > 0)
        {
            return  true;
        }
        return  false;
    }


    private boolean isMobile(String address) {
        //TODO: Replace this with your own logic
        if( address.length() >=10 )
        {
            return  true;
        }
        return  false;
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
        Intent intent = new Intent(ProfilePage.this,NavigationScreen.class);
        startActivity(intent);
        finish();
      ///  overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);

    }

    private void callUpdateProfileApi() {
        progressDialog.show();

        Log.e(TAG+" callUpdateProfileApi resp", Const.URL_User_UpdateProfile.toString()+"?name="+ name+"&number="+mobile+"&email="+email+"&customerid="+sharedPreferences.get_userid());

   StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_User_UpdateProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG+" callUpdateProfileApi resp", response.toString());
                        try
                        {
                            JSONObject object = new JSONObject(response);
                            String text = object.getString("text");
                            String type= object.getString("type");

                            if(text.equals("1"))
                            {
                                sharedPreferences.set_name(""+name);
                                sharedPreferences.set_phone(""+mobile);
                                sharedPreferences.set_user_email(""+email);
                                Toast.makeText(getApplicationContext(),"Profile Updated Successfully",Toast.LENGTH_LONG).show();
                                       }
                            else {

                                Toast.makeText(getApplicationContext(),""+type,Toast.LENGTH_LONG).show();
                            }

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
                        Toast.makeText(ProfilePage.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "" + name);
                params.put("number", "" + mobile);
                params.put("email", "" + email);
                params.put("customerid", "" + sharedPreferences.get_userid());
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



    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplicationContext(). getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(ProfilePage.this,NavigationScreen.class);
                startActivity(intent);
                finish();
             //   overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.FlashScreen.flashservice.InnnerClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class UpdatePassword extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog progressDialog;
    private static final String TAG = "UpdatePassword ";
    String oldPsd,newPsd;
    Shaved_shared_preferences sharedPreferences;
    @Bind(R.id.et_oldPsd)
    EditText et_oldPsd;
    @Bind(R.id.et_newPsd1) EditText et_newPsd1;
    @Bind(R.id.et_newPsdConfirm) EditText et_newPsdConfirm;
    @Bind(R.id.tv_updatePsd)
    TextView tv_updatePsd;
    View focusView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);

        sharedPreferences = new Shaved_shared_preferences(getApplicationContext());
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

        progressDialog = new ProgressDialog(UpdatePassword.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait......");
        tv_updatePsd.setOnClickListener(this);

        et_newPsdConfirm.setImeOptions(EditorInfo.IME_ACTION_DONE);

        et_newPsdConfirm.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                tv_updatePsd.callOnClick();
                return false;
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_updatePsd:
               String str_old=  et_oldPsd.getText().toString().trim();
                String str_newPsd1=  et_newPsd1.getText().toString().trim();
                String str_confirmPsd=  et_newPsdConfirm.getText().toString().trim();

                if(str_old.equalsIgnoreCase(""))
                {
                    et_oldPsd.setError(getString(R.string.error_field_required));
                    focusView = et_oldPsd;
                    focusView.requestFocus();
                }
                else                  if(str_newPsd1.equalsIgnoreCase("") || str_newPsd1.length()<6)
                {
                    et_newPsd1.setError("Password should be at least 6 digits");
                    focusView = et_newPsd1;
                    focusView.requestFocus();
                }
                else                 if(!str_confirmPsd.equalsIgnoreCase(str_newPsd1))
                {
                    et_newPsdConfirm.setError("Confirm password is not matched");
                    focusView = et_newPsdConfirm;
                    focusView.requestFocus();
                }
                else {
                    if (isOnline()) {
                        callUpdatePassword();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    private void callUpdatePassword() {
        progressDialog.show();
        /* params.put("Id", "" +sharedPreferences.get_userid());
                params.put("password", "" + oldPsd);
                params.put("new_password", "" + newPsd );*/
oldPsd=  et_oldPsd.getText().toString().trim();
        newPsd =  et_newPsd1.getText().toString().trim();
        Log.e(TAG+"updatePsd url", ""+Const.UPDATE_NEW_PASSWORD +"?Id="+sharedPreferences.get_userid()+"&password="+ oldPsd+"&new_password="+ newPsd );

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.UPDATE_NEW_PASSWORD ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG+"updatePsd url", response.toString());
                        progressDialog.hide();

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if(text.equals("1"))
                            {
      Toast.makeText(getApplicationContext(),"Password Updated Successfully",Toast.LENGTH_LONG).show();
  }
                            else {
   Toast.makeText(getApplicationContext(),"Password is not matched",Toast.LENGTH_LONG).show();
                             }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdatePassword.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Id", "" +sharedPreferences.get_userid());
                params.put("password", "" + oldPsd);
                params.put("new_password", "" + newPsd );
                      return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(UpdatePassword.this, ProfilePage.class);
                startActivity(intent);
                finish();
                 return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdatePassword.this, ProfilePage.class);
        startActivity(intent);
        finish();

    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }
}

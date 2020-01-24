package com.FlashScreen.flashservice.InnnerClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Feedback extends AppCompatActivity {
    Button send_notification;
    EditText send_message, send_title;
    ProgressDialog progressDialog;
    String img1 = "", TITLE = "", MESSAGE = "";
    private static final int STORAGE_PERMISSION_CODE = 123;
String TAG="Feedback ";
Shaved_shared_preferences shaved_shared_preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

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
        progressDialog = new ProgressDialog(Feedback.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait......");
shaved_shared_preferences=new Shaved_shared_preferences(Feedback.this);
        send_notification = (Button) findViewById(R.id.send_notification);

        send_message = (EditText) findViewById(R.id.send_message);
        send_title = (EditText) findViewById(R.id.send_title);

        send_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = send_title.getText().toString();
                String message = send_message.getText().toString();

                if (TITLE(title))
                    if (MESSAGE(message)) {
                        if (isOnline()) {
                            TITLE = title;
                            MESSAGE = message;

                            callAddFeedBackApi();


                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }

            }
        });

    }

    private void callAddFeedBackApi() {
        progressDialog.show();

        String strApiUrl=Const.URL_SEND_FEEDBACK;
        Log.e(TAG+"callAddFeedBackApi  url", strApiUrl +"  paramsList ="+"     Title="+ TITLE+"userid="+ shaved_shared_preferences.get_userid() + "&Message=" + MESSAGE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_SEND_FEEDBACK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        Log.e(TAG+"callAddFeedBackApi", response.toString());
                        try
                        {
                          send_title.setText("");
                            send_message.setText("");
                            JSONObject object = new JSONObject(response);
                            String text = object.getString("text");
                            if(text.equals("1"))
                            {
                                SweetAlertDialog sweetAlertDialog=                                new SweetAlertDialog(Feedback.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Feedback Send Successfully")
                                        .setConfirmText("Ok!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        onBackPressed();
                                                    }
                                                },500);
                                            }
                                        });
                                        sweetAlertDialog.show();
     }
                            else {
                                Toast.makeText(getApplicationContext(),"callAddFeedBackApi  ERROR",Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            Log.e(TAG+"callAddFeedBackApi excep", e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(Feedback.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Title", "" + TITLE);
                params.put("Message", "" + MESSAGE);
                params.put("userid", "" + shaved_shared_preferences.get_userid());
                params.put("type", "user"  );

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    private boolean TITLE(String n) {
        if (n.length() > 0) {
            return true;
        } else {
            send_title.setError("EMPTY TITLE");

        }
        return false;
    }

    private boolean MESSAGE(String n) {
        if (n.length() > 0) {
            return true;
        } else {
            send_message.setError("EMPTY MESSAGE");

        }
        return false;
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

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
                Intent i = new Intent(Feedback.this, NavigationScreen.class);
                startActivity(i);
                //  overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        //    finish();
        Intent i = new Intent(Feedback.this, NavigationScreen.class);
        startActivity(i);
          overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        finish();
    }


}

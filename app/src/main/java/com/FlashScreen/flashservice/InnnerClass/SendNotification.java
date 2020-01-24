package com.FlashScreen.flashservice.InnnerClass;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SendNotification extends AppCompatActivity {
    Button send_notification;
    EditText send_message,send_title;

    String img1="",TITLE="",MESSAGE="";
    private static final int STORAGE_PERMISSION_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

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


        send_notification = (Button) findViewById(R.id.send_notification);

        send_message = (EditText) findViewById(R.id.send_message);
        send_title = (EditText) findViewById(R.id.send_title);

        send_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title =  send_title.getText().toString();
                String message =  send_message.getText().toString();

                if(TITLE(title))
                    if(MESSAGE(message))
                    {
                        if(isOnline())
                        {
                            TITLE = title;
                            MESSAGE=message;

                            SEND_SERVER();


                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }

            }
        });

}
    private void SEND_SERVER() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_SEND_NOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if (text.equals("1")) {

                                send_message.setText("");
                                send_title.setText("");

                                    new SweetAlertDialog(SendNotification.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Notification Send Successfully")
                                            .setConfirmText("Ok!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();

                                                }
                                            })
                                            .show();

                                Toast.makeText(getApplicationContext(), "NOTIFICATION SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "SERVER ERROR", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SendNotification.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Title", "" + TITLE);
                params.put("Message", "" + MESSAGE);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private boolean TITLE(String n)
    {
        if(n.length()>0)
        {
            return true;
        }
        else {
            send_title.setError("EMPTY TITLE");

        }
        return false;
    }
    private boolean MESSAGE(String n)
    {
        if(n.length()>0)
        {
            return true;
        }
        else {
            send_message.setError("EMPTY MESSAGE");

        }
        return false;
    }
    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplicationContext(). getSystemService(Context.CONNECTIVITY_SERVICE);
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
                Intent i = new Intent(SendNotification.this, NavigationAdmin.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
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
        Intent i = new Intent(SendNotification.this, NavigationAdmin.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        finish();
    }



}

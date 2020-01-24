package com.FlashScreen.flashservice.InnnerClass;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.startscreen.SplashScreen;

public class NotificationActivity extends AppCompatActivity {
    private Context context;
    Shaved_shared_preferences shaved_shared_preferences;
    ProgressDialog progressDialog;
    String OrderId;
    String tickerText;
    int login=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);


        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());

        Intent i = getIntent();
        OrderId = i.getStringExtra("OrderId");
        tickerText = i.getStringExtra("tickerText");

        login = shaved_shared_preferences.get_user_login();

        Log.e("logout_123_login_noty", "--" + login+"--"+OrderId);

        if(login==2 || tickerText.equalsIgnoreCase("2"))
        {

            Intent j = new Intent(getApplication(), SubScreen.class);
            j.putExtra("OrderId", ""+OrderId);
            startActivity(j);
            finish();
        }
        else if(login==2 || tickerText.equalsIgnoreCase("3"))
        {
            Intent j = new Intent(getApplication(), ViewFeedback.class);
            startActivity(j);
            finish();
        }
        else if(login==1 || tickerText.equalsIgnoreCase("1"))
        {
            Intent j = new Intent(getApplication(), Notification_View_User.class);
            startActivity(j);
            finish();
        }
        else
        {

            Intent k = new Intent(getApplicationContext(),SplashScreen.class);
            startActivity(k);
            finish();
        }

    }

}


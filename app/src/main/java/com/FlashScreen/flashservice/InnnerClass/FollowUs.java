package com.FlashScreen.flashservice.InnnerClass;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.R;

public class FollowUs extends AppCompatActivity {
    ImageView  iv_send_mail,follow_onFB,follow_onTwitter,follow_onYoutube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_page);
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

        follow_onYoutube= (ImageView) findViewById(R.id.youtube_link);
        follow_onFB= (ImageView) findViewById(R.id.facebook_link);
        follow_onTwitter= (ImageView) findViewById(R.id.twitter_link);
        iv_send_mail= (ImageView) findViewById(R.id.iv_send_mail);

        iv_send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();

            }
        });

        follow_onFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Toast.makeText(getApplicationContext(), "COMING SOON!!", Toast.LENGTH_LONG).show();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/FlashServices786/"));
                startActivity(browserIntent);
                //  Toast.makeText(getApplicationContext(), "FACEBOOK PAGE COMING SOON!!", Toast.LENGTH_LONG).show();
            }
        });

        follow_onYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   //Toast.makeText(getApplicationContext(), "COMING SOON!!", Toast.LENGTH_LONG).show();

               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/Ge7uIk0zyYk"));
                startActivity(browserIntent);
                //  Toast.makeText(getApplicationContext(),"INSTAGRAM PAGE COMING SOON!!",Toast.LENGTH_LONG).show();
            }
        });

        follow_onTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/flashservices2"));
                startActivity(browserIntent);
            }
        });

    }

    protected void sendEmail() {
     /*   Log.i("Send email", "");
        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"+Const.adminEmail));
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        email.putExtra(Intent.EXTRA_SUBJECT, "Query or Feedback");
        email.putExtra(Intent.EXTRA_TEXT, "");
        email.setType("message/rfc822");

        try {
            startActivity(Intent.createChooser(email, "Send mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FollowUs.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }*/

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"+Const.adminEmail));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Query or Feedback");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FollowUs.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               onBackPressed();
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
        Intent i = new Intent(getApplicationContext(), NavigationUser.class);
        startActivity(i);
        //   overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();

    }
}

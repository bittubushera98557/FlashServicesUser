package com.FlashScreen.flashservice.InnnerClass;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;

import java.net.URLEncoder;

public class ContactCustomerSupport extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ll_email,ll_call,ll_whatsapp;
Shaved_shared_preferences shaved_shared_preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_customer_support);
       // ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 123);
        initView();

        ll_email.setOnClickListener(this);
        ll_call.setOnClickListener(this);
        ll_whatsapp.setOnClickListener(this);
 shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());

    }

    private void initView() {
        ll_call=(LinearLayout)findViewById(R.id.ll_call);
        ll_email=(LinearLayout)findViewById(R.id.ll_email);
        ll_whatsapp      =(LinearLayout)findViewById(R.id.ll_whatsapp);
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
    }
 // @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
        switch (v.getId())

        {

            case R.id.ll_email:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"+Const.adminEmail));
                //i.setType("message/rfc822");
                //i.putExtra(Intent.EXTRA_EMAIL  , new String[]{strEmail});
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactCustomerSupport.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_call:
                Log.e("calling","call intent ");

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Const.adminPhone_First));
                startActivity(intent);

                 break;
            case R.id.ll_whatsapp:
                openWhatsApp() ;
                Log.e("whatsapp","message");
                break;

        }
    }
public void openWhatsApp() {
    String smsNumber = "+91"+Const.adminPhone_First;
/*
       // boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
    Log.e("whatsapp","message"+smsNumber);
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");
    String text = "" + "xyz text message";
    Log.e("sharedata", "" + text);
    // change with required  application package

    intent.setPackage("com.whatsapp");
    intent.putExtra(Intent.EXTRA_TEXT, text);//
    startActivity(Intent.createChooser(intent, text));
*/

    PackageManager packageManager = getPackageManager();
    Intent i = new Intent(Intent.ACTION_VIEW);

    try {
        String url = "https://api.whatsapp.com/send?phone=+91"+ Const.adminPhone_First +"&text=" + URLEncoder.encode("Hi", "UTF-8");
        i.setPackage("com.whatsapp");
        i.setData(Uri.parse(url));
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i);
        }
    } catch (Exception e){
        e.printStackTrace();
    }

        /*  if (isWhatsappInstalled) {

            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
            startActivity(sendIntent);

        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(this, "WhatsApp not Installed",
                    Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }*/
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

  /*  @Override
    public void onBackPressed() {
        Intent intent;
        if(shaved_shared_preferences.get_user_login()==2)
            intent = new Intent(ContactCustomerSupport.this, NavigationUser.class);
       else
           intent = new Intent(ContactCustomerSupport.this, NavigationScreen.class);

        startActivity(intent);
        finish();
      overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);

    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


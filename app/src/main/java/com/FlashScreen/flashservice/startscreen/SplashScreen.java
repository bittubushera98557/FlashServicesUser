package com.FlashScreen.flashservice.startscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.InnnerClass.NavigationAdmin;
import com.FlashScreen.flashservice.InnnerClass.NavigationScreen;
import com.FlashScreen.flashservice.InnnerClass.NavigationUser;
import com.FlashScreen.flashservice.Push_Notification.GCMRegistrationIntentService;
import com.FlashScreen.flashservice.R;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    String TAG = "SplashScreen ";
    private static int SPLASH_TIME_OUT = 3000;
    int timer = 1000;
    ImageView imageView;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Animation zoomin, zoomout;
    Shaved_shared_preferences shaved_shared_preferences;
    final Handler handler = new Handler();
    private static final boolean AUTO_HIDE = true;
    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;

    int login = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());
    //      generateKayHash();
        imageView = (ImageView) findViewById(R.id.yourImageViewId);

        zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        zoomout = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        imageView.setAnimation(zoomout);

        login = shaved_shared_preferences.get_user_login();
        Log.e("splash_123_login", "--" + login);
        try {
            callFacebookCode();
            setAdvertiserIDCollectionEnabled(true);
        } catch (Exception e) {
            Log.e(TAG + " Excep callFacebookCode", e.toString());

        }
        FacebookSdk.publishInstallAsync(getApplicationContext(), getApplicationContext().getString(R.string.facebook_app_id));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                imageView.setAnimation(zoomin);
                if (login == 1) {
                    Intent i = new Intent(getApplicationContext(), NavigationScreen.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                    finish();
                } else if (login == 2) {
                    Intent i = new Intent(getApplicationContext(), NavigationAdmin.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                    finish();
                } else {
                    Intent i = new Intent(SplashScreen.this, NavigationUser.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);




    /*  if(login==1)
        {
            Intent i = new Intent(getApplicationContext(),VendorNavigation.class);
            startActivity(i);
            overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        }
        else {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    handler.postDelayed(this, 1500);
                }
            }, timer);

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    imageView.setAnimation(zoomin);


                    Intent i = new Intent(SplashScreen.this, Vendor_Register.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                    finish();

                }
            }, SPLASH_TIME_OUT);
        }*/
        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");

                    Log.e("TOKEN_IDDW ", "===" + token);

                    shaved_shared_preferences.set_MobToken(token);
                    //Displaying the token as toast
                    //  Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }

    }
    private void callFacebookCode() {

        AppEventsLogger.setUserID("loggedUser=" + login + "&ID=" + shaved_shared_preferences.get_userid());
// Set the User Properties
        Bundle user_props = new Bundle();
        user_props.putString("state", "DummyState"  );
        user_props.putString("city", "DummyCity"  );
        user_props.putInt("userType", login);
        user_props.putString("loggedName", "" + shaved_shared_preferences.get_name());
        user_props.putString("mobile", "" + shaved_shared_preferences.get_phone());
        AppEventsLogger.updateUserProperties(user_props, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    String key = "";
//                    key = response.getJSONObject().getString("city");
                    //     key =key+ "   "+response.getJSONObject().getString("gender");
                    //    key =key+ "   "+response.getJSONObject().getString("email");
                    Log.e(TAG + " try City Key value=", response.toString());
                } catch (Exception exp) {
                    //    Toast.makeText(SplashScreen.this, "Facebook Exception=" + exp.toString(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG + " Excep callFacebookCode", exp.toString());

                }


            }
        });
        AppEventsLogger.clearUserID();
    }


    private void generateKayHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "" + getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {
        }
    }


    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

}
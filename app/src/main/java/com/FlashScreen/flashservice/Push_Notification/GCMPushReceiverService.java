package com.FlashScreen.flashservice.Push_Notification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.InnnerClass.History;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.startscreen.SplashScreen;
import com.google.android.gms.gcm.GcmListenerService;


/**
 * Created by Belal on 4/15/2016.
 */

//Class is extending GcmListenerService
public class GCMPushReceiverService extends GcmListenerService {
    Context context = GCMPushReceiverService.this;
    Shaved_shared_preferences shaved_shared_preferences;
    String  TAG="GCMPushReceiverService vendor ";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle

        Log.e("TOKEN_IDD_MESSAGE ", "--" + data);
        Log.e("TOKEN_IDD_MESSAGE ","--"+data);
        String contentTitle = "" +data.getString("title");
        String message =""+ data.getString("subtitle");
        String tickerText = ""+data.getString("tickerText");
        String vibrate = ""+data.getString("vibrate");
        //Displaying a notiffication with the message

        Log.e("TOKEN_IDD_contentTitle ","--"+contentTitle);
        Log.e("TOKEN_IDD_message ", "--" + message);
        Log.e("TOKEN_IDD_tickerText ", "--" + tickerText);
        Log.e("TOKEN_IDD_vibrate ", "--" + vibrate);
        Log.e("TOKEN_IDD_message ", "--" + message);
  shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());
        // login = shaved_shared_preferences.get_user_login();
        sendNotification(contentTitle,tickerText,message,vibrate);
    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(String contentTitle,String tickerText,String message,String vibrate) {


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.bell);
        Ringtone ring = RingtoneManager.getRingtone(getApplicationContext(), uri);
        ring.play();
        Log.e(TAG + "sendNotification ", "vibrate="+vibrate+"    tickerText="+tickerText+"   message="+message);



        Intent intent = null;

        Log.e(TAG + "notification ", "get_user_login() = "+shaved_shared_preferences.get_user_login());
        if (shaved_shared_preferences.get_user_login() == 1 && !shaved_shared_preferences.get_userid().equalsIgnoreCase("")) {
            intent= new Intent(this, History.class);

        } else {
            intent = new Intent(GCMPushReceiverService.this, SplashScreen.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        sendBroadcast(intent);
        int requestCode = 0;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder noBuilder = new android.support.v4.app.NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(contentTitle)
                .setSubText(contentTitle)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        noBuilder.setSound(sound);
        notificationManager.notify(1, noBuilder.build()); //0 = ID of notification


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.support.v4.app.NotificationCompat.Builder builder;
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (mChannel == null) {
                mChannel = new NotificationChannel
                        ("0", tickerText, importance);
                mChannel.setDescription(message);
                mChannel.enableVibration(true);
                notificationManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, "0");

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 1251, intent, PendingIntent.FLAG_ONE_SHOT);
            builder.setContentTitle(message)
                    .setSmallIcon(R.drawable.logo) // required
                    .setContentText(contentTitle)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource
                            (getResources(), R.drawable.logo))
                    .setBadgeIconType(R.drawable.logo)
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri
                            (RingtoneManager.TYPE_NOTIFICATION));
            Notification notification = builder.build();
            notificationManager.notify(0, notification);


        }


    }
}

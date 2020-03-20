package com.example.lock_screen.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.lock_screen.R;

import static android.telephony.AvailableNetworkInfo.PRIORITY_LOW;

public class LockScreenService extends Service {
    private BroadcastReceiver mReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // Register for Lockscreen event intents
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("lock", "on Start command called");
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new LockScreenIntentReceiver();
        registerReceiver(mReceiver, filter);
        startForeground();
        return START_STICKY;
    }

    // Run service in foreground so it is less likely to be killed by system
    private void startForeground() {
        Log.i("lock", "on foreground");
//        Notification notification = new NotificationCompat.Builder(this)
//                .setContentTitle(getResources().getString(R.string.app_name))
//                .setTicker(getResources().getString(R.string.app_name))
//                .setContentText("Running")
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentIntent(null)
//                .setOngoing(true)
//                .build();
        startForeground(9999, getNotification());
    }

    public Notification getNotification() {
        String channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel();
        else {
            channel = "";
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel).setSmallIcon(android.R.drawable.ic_menu_mylocation).setContentTitle("snap map fake location");
        Notification notification = mBuilder
                .setPriority(PRIORITY_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();


        return notification;
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "snap map fake location ";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel("snap map channel", name, importance);

        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "snap map channel";
    }
    // Unregister receiver
    @Override
    public void onDestroy() {
        Log.i("lock", "on destroy called");
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}

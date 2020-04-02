package com.gnusl.actine.service;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra("type");
        switch (type) {
            case "cancel": {
                if (intent.getExtras() != null) {

//                    Intent serviceIntent = new Intent(context, DownloadService.class);
//                    serviceIntent.putExtra("status", "stop");
//                    ContextCompat.startForegroundService(context, serviceIntent);

                    int shoeId = intent.getExtras().getInt("id");
                    AndroidNetworking.cancelAll();
                    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.cancel(shoeId);



                }
                break;
            }
        }
    }

}

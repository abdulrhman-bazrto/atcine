package com.gnusl.actine.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.DownloadDelegate;
import com.gnusl.actine.ui.activity.MainActivity;

import java.text.DecimalFormat;


public class DownloadService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private long mFutureCallTime;

    public static DownloadDelegate downloadDelegate;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String fileName = intent.getStringExtra("fileName");
            String url = intent.getStringExtra("url");
            String fileDir = intent.getStringExtra("fileDir");

            showProgressNotification(fileName, 0, true);


            AndroidNetworking.download(url, fileDir, fileName)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .setDownloadProgressListener(new DownloadProgressListener() {
                        @Override
                        public void onProgress(long bytesDownloaded, long totalBytes) {
                            if (downloadDelegate != null) {
                                float p = ((float) bytesDownloaded / totalBytes);
                                int progress = (int) (p * 100);
                                downloadDelegate.onDownloadProgress(fileDir, fileName, progress);
                                float x = (p * 100);
                                DecimalFormat df = new DecimalFormat("##.##");
                                showProgressNotification(fileName, Float.parseFloat(df.format(x)), true);
                            }
                        }
                    })
                    .startDownload(new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            if (downloadDelegate != null)
                                downloadDelegate.onDownloadSuccess(fileDir, fileName);
                            downloadDelegate = null;
                            showProgressNotification(fileName, 100, true);
                        }

                        @Override
                        public void onError(ANError error) {
                            if (downloadDelegate != null)
                                downloadDelegate.onDownloadError(error);
                            downloadDelegate = null;
                            showProgressNotification(fileName, -1, true);
                        }
                    });


        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "download channel",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    protected void showProgressNotification(final String fileName, float progress, boolean isDownloading) {
        String message = null;
        int smallIcon = 0;
        int flags = 0;
        flags |= Notification.FLAG_ONGOING_EVENT;

        createNotificationChannel();


        if (progress == 100) {
            smallIcon = R.drawable.icon_download_white;

            if (isDownloading) {
                message = "Download completed.";
            } else {
                message = "Upload completed.";
            }
        } else if (progress >= 0) {
            if (isDownloading) {
                smallIcon = R.drawable.icon_download_gray;
                message = "Downloading: " + progress + "%.";
            }
//            builder.setProgress(100, progress, false);
        } else {
            smallIcon = R.drawable.icon_cancel_red;
            if (isDownloading)
                message = "failed download.";
            else
                message = "Cancelled upload.";
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(fileName)
                .setContentText(message)
                .setProgress(100, (int) progress, false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(pendingIntent)
                .build();

        notification.flags = flags;
        notification.defaults = Notification.DEFAULT_LIGHTS;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        long notificationDelay = 100;
        long now = System.currentTimeMillis();
        if (mFutureCallTime == 0 || now > mFutureCallTime || progress == -1 || progress == 100) {
            startForeground(1, notification);
            //mNotificationManager.notify(item.GetPath().hashCode(), notification);
        }

        mFutureCallTime = now + notificationDelay;
    }
}
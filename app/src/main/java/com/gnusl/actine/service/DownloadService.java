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
import com.gnusl.actine.ui.Mobile.activity.MainActivity;

import java.text.DecimalFormat;

import static android.content.Intent.FLAG_INCLUDE_STOPPED_PACKAGES;


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
            int showId = intent.getIntExtra("showId", 5);
            String fileName = intent.getStringExtra("fileName");
            String url = intent.getStringExtra("url");
            String fileDir = intent.getStringExtra("fileDir");

            showProgressNotification(showId, fileName, 0, true, true);


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
                                String formated = df.format(x).replaceAll("١", "1").replaceAll("٢", "2").replaceAll("٣", "3").replaceAll("٤", "4")
                                        .replaceAll("٥", "5").replaceAll("٦", "6").replaceAll("٧", "7").replaceAll("٨", "8")
                                        .replaceAll("٩", "9").replaceAll("٠", "0").replaceAll("٫",".");

                                showProgressNotification(showId, fileName, Float.parseFloat(formated), true, false);
                            }
                        }
                    })
                    .startDownload(new DownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            if (downloadDelegate != null)
                                downloadDelegate.onDownloadSuccess(fileDir, fileName);
                            downloadDelegate = null;
                            showProgressNotification(showId, fileName, 100, true, false);
                        }

                        @Override
                        public void onError(ANError error) {
                            if (downloadDelegate != null)
                                downloadDelegate.onDownloadError(error);
                            downloadDelegate = null;
                            showProgressNotification(showId, fileName, -1, true, false);
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


    protected void showProgressNotification(final int showId, final String fileName, float progress, boolean isDownloading, boolean isFirstTime) {
        String message = null;
        int smallIcon = 0;
        int flags = 0;
        flags |= Notification.FLAG_ONGOING_EVENT;

        createNotificationChannel();


        if (progress == 100) {
            smallIcon = R.drawable.icon_download_white;

            if (isDownloading) {
                message = getApplicationContext().getString(R.string.download_completed);
            } else {
                message = getApplicationContext().getString(R.string.upload_completed);
            }
        } else if (progress >= 0) {
            if (isDownloading) {
                smallIcon = R.drawable.icon_download_gray;
                message = getApplicationContext().getString(R.string.downloading) + progress + "%.";
            }
//            builder.setProgress(100, progress, false);
        } else {
            smallIcon = R.drawable.icon_cancel_red;
            if (isDownloading)
                message = getApplicationContext().getString(R.string.failed_download);
            else
                message = getApplicationContext().getString(R.string.cancelled_upload);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.putExtra("type", "stop");
        intent.putExtra("id", showId);
        PendingIntent pendingIntentCancel = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(fileName)
                .setContentText(message)
                .setProgress(100, (int) progress, false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(pendingIntent)
//                .addAction(new NotificationCompat.Action(R.drawable.icon_cancel_red, "cancel", pendingIntentCancel))
                .build();

        notification.flags = flags;
        notification.defaults = Notification.DEFAULT_LIGHTS;

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        long notificationDelay = 100;
        long now = System.currentTimeMillis();
        if (progress == 0) {
            startForeground(showId, notification);
        }
        if (isFirstTime) {
            if (mFutureCallTime == 0 || now > mFutureCallTime || progress == -1 || progress == 100) {
                startForeground(showId, notification);
                //mNotificationManager.notify(item.GetPath().hashCode(), notification);
            }
        } else {
            mNotificationManager.notify(showId, notification);
        }

        mFutureCallTime = now + notificationDelay;
    }
}
package com.gnusl.actine.util;

import android.content.Context;

import com.gnusl.actine.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeAgo {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time, Context mContext) {
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return mContext.getString(R.string.just_now);
        } else if (diff < 2 * MINUTE_MILLIS) {
            return mContext.getString(R.string.minute_ago);
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + mContext.getString(R.string.minutes_ago);
        } else if (diff < 90 * MINUTE_MILLIS) {
            return mContext.getString(R.string.hour_ago);
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + mContext.getString(R.string.hours_ago);
        } else if (diff < 48 * HOUR_MILLIS) {
            return mContext.getString(R.string.yesterday);
        } else if (diff < 96 * HOUR_MILLIS) {
            return diff / DAY_MILLIS + mContext.getString(R.string.days_ago);
        } else {
            Date date = new Date(time);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return simpleDateFormat.format(date);
        }
    }
}
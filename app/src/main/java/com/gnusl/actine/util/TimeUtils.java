package com.gnusl.actine.util;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static String formatMillis(long millis) {
        String time = "";

        time = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis) ),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );

        return time;
    }
}

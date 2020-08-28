package com.gnusl.actine.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.ui.Mobile.activity.AuthActivity;


public class DialogUtils {

    public static void showSeriesComingSoonDialog(Context context) {

        final Dialog logoutDialog = new Dialog(context);
        logoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (logoutDialog.getWindow() != null)
            logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutDialog.setContentView(R.layout.dialog_coming_soon);
        logoutDialog.setCancelable(true);

        logoutDialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.dismiss();
            }
        });

        logoutDialog.show();
    }

    public static void showLocationDialog(Activity context, String text) {

        final Dialog logoutDialog = new Dialog(context);
        logoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (logoutDialog.getWindow() != null)
            logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutDialog.setContentView(R.layout.dialog_coming_soon);
        logoutDialog.setCancelable(true);

        TextView tvText = logoutDialog.findViewById(R.id.tv_text);

        tvText.setText(text);

        logoutDialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.clear();
                context.startActivity(new Intent(context, AuthActivity.class));
                context.finish();
                logoutDialog.dismiss();
            }
        });

        logoutDialog.show();
    }
}

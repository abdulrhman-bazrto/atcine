package com.gnusl.actine.ui.Mobile.custom;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.gnusl.actine.R;

public class LoaderPopUp extends DialogFragment {

    private static LoaderPopUp loaderPopUp;
    private static FragmentManager supportFragmentManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View view = getActivity().getLayoutInflater().inflate(R.layout.loader_popup_layout, null);
        GifImageView gifImageView = view.findViewById(R.id.gif1);
        gifImageView.setGifImageResource(R.drawable.loader);
        dialog.setContentView(view);
        setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static void show(FragmentActivity fragmentActivity) {
        if (true)
            return;
        try {
            if (loaderPopUp != null && loaderPopUp.isAdded())
                return;

            if (loaderPopUp == null)
                loaderPopUp = new LoaderPopUp();
            if (fragmentActivity == null)
                return;

            if (loaderPopUp.isAdded())
                return;
            supportFragmentManager = fragmentActivity.getSupportFragmentManager();
            loaderPopUp.show(supportFragmentManager, "tag");
        } catch (Throwable t) {

        }

    }
    public static void show1(FragmentActivity fragmentActivity) {
        try {
            if (loaderPopUp != null && loaderPopUp.isAdded())
                return;

            if (loaderPopUp == null)
                loaderPopUp = new LoaderPopUp();
            if (fragmentActivity == null)
                return;

            if (loaderPopUp.isAdded())
                return;
            supportFragmentManager = fragmentActivity.getSupportFragmentManager();
            loaderPopUp.show(supportFragmentManager, "tag");
        } catch (Throwable t) {

        }

    }

    public static void dismissLoader() {

        try {

            if (loaderPopUp == null)
                return;

            if (supportFragmentManager != null) {
                Fragment oldFragment = supportFragmentManager.findFragmentByTag("tag");
                if (oldFragment != null) {
                    supportFragmentManager.beginTransaction().remove(oldFragment).commit();
                }
            }

            loaderPopUp.dismiss();
        } catch (Exception e) {

        }
    }
}


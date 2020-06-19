package com.gnusl.actine.ui.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.ui.fragment.GuestFragment;
import com.gnusl.actine.ui.fragment.HelpFragment;
import com.gnusl.actine.ui.fragment.LoginFragment;
import com.gnusl.actine.ui.fragment.PaymentLessFragment;
import com.gnusl.actine.ui.fragment.RegisterFragment;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AuthActivity extends AppCompatActivity {

    private Fragment mCurrentFragment;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_auth_activiy);

        replaceFragment(FragmentTags.LoginFragment);

    }

    public void replaceFragment(FragmentTags fragmentTags) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        switch (fragmentTags) {

            case GuestFragment:

                mCurrentFragment = GuestFragment.newInstance();
                transaction.replace(R.id.frame_container_auth, mCurrentFragment);
                transaction.commit();

                break;

            case LoginFragment:

                mCurrentFragment = LoginFragment.newInstance();
                transaction.replace(R.id.frame_container_auth, mCurrentFragment);
                transaction.commit();

                break;

            case RegisterFragment:

                mCurrentFragment = RegisterFragment.newInstance();
                transaction.replace(R.id.frame_container_auth, mCurrentFragment).addToBackStack(null);
                transaction.commit();

                break;

            case HelpFragment:

                mCurrentFragment = HelpFragment.newInstance();
                transaction.replace(R.id.frame_container_auth, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case PaymentLessFragment:

                mCurrentFragment = PaymentLessFragment.newInstance();
                transaction.replace(R.id.frame_container_auth, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();

        } else if (fm.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
    }
}

package com.gnusl.actine.ui.TV.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;

import com.gnusl.actine.R;
import com.gnusl.actine.application.Atcine;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.ui.TV.fragment.TVHelpFragment;
import com.gnusl.actine.ui.TV.fragment.TVLoginFragment;
import com.gnusl.actine.ui.TV.fragment.TVMainAuthFragment;
import com.gnusl.actine.ui.TV.fragment.TVPaymentLessFragment;
import com.gnusl.actine.ui.TV.fragment.TVRegisterFragment;
import com.gnusl.actine.util.SharedPreferencesUtils;

import java.util.Locale;

public class TVAuthActivity extends AppCompatActivity {
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        String languageToLoad = SharedPreferencesUtils.getLanguage(Atcine.getApplicationInstance());
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_tvauth);

        replaceFragment(FragmentTags.MainAuthFragment);

    }

    public void replaceFragment(FragmentTags fragmentTags) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        switch (fragmentTags) {

//            case GuestFragment:
//
//                mCurrentFragment = TVGuestFragment.newInstance();
//                transaction.replace(R.id.frame_container_auth, mCurrentFragment);
//                transaction.commit();
//
//                break;
            case MainAuthFragment:

                mCurrentFragment = TVMainAuthFragment.newInstance();
                transaction.replace(R.id.frame_container_tv_auth, mCurrentFragment);
                transaction.commit();

                break;

            case LoginFragment:

                mCurrentFragment = TVLoginFragment.newInstance();
                transaction.replace(R.id.frame_container_auth, mCurrentFragment);
                transaction.commit();

                break;

            case RegisterFragment:

                mCurrentFragment = TVRegisterFragment.newInstance();
                transaction.replace(R.id.frame_container_auth, mCurrentFragment).addToBackStack(null);
                transaction.commit();

                break;

            case HelpFragment:

                mCurrentFragment = TVHelpFragment.newInstance();
                transaction.replace(R.id.frame_container_auth, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case PaymentLessFragment:

                mCurrentFragment = TVPaymentLessFragment.newInstance();
                transaction.replace(R.id.frame_container_auth, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;
//            case ToWatchFragment:
//
//                mCurrentFragment = TVToWatchFragment.newInstance();
//                transaction.replace(R.id.frame_container_auth, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
//                transaction.commit();
//
//                break;
        }
    }
}

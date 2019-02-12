package com.gnusl.actine.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gnusl.actine.R;
import com.gnusl.actine.ui.enums.FragmentTags;
import com.gnusl.actine.ui.fragment.LoginFragment;

public class AuthActivity extends AppCompatActivity {

    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        replaceFragment(FragmentTags.LoginFragment);

        setContentView(R.layout.activity_auth_activiy);
    }

    public void replaceFragment(FragmentTags fragmentTags) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        switch (fragmentTags) {


            case LoginFragment:

                mCurrentFragment = LoginFragment.newInstance();
                transaction.replace(R.id.frame_container_auth, mCurrentFragment);
                transaction.commit();

                break;

            case RegisterFragment:

//                mCurrentFragment = RegisterFragment.newInstance();
                transaction.replace(R.id.frame_container_auth, mCurrentFragment).addToBackStack(null);
                transaction.commit();

                break;
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();

        }
    }
}

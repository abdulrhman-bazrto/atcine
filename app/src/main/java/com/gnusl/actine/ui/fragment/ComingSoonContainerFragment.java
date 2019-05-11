package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;


public class ComingSoonContainerFragment extends Fragment {

    View inflatedView;
    private Fragment mCurrentFragment;


    public ComingSoonContainerFragment() {
    }

    public static ComingSoonContainerFragment newInstance() {
        ComingSoonContainerFragment fragment = new ComingSoonContainerFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_coming_soon_container, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {
        replaceFragment(FragmentTags.ComingSoonFragment);
    }

    public void replaceFragment(FragmentTags fragmentTags) {

        // init manager
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        switch (fragmentTags) {

            case ComingSoonFragment:

                mCurrentFragment = ComingSoonFragment.newInstance();
                transaction.replace(R.id.frame_container_coming_soon, mCurrentFragment);// newInstance() is a static factory method.
                transaction.commit();

                break;
        }
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }


}

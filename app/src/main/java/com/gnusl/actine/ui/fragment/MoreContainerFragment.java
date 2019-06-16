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


public class MoreContainerFragment extends Fragment {

    View inflatedView;
    private Fragment mCurrentFragment;


    public MoreContainerFragment() {
    }

    public static MoreContainerFragment newInstance() {
        MoreContainerFragment fragment = new MoreContainerFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_more_container, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {
        replaceFragment(FragmentTags.MoreFragment,null);
    }

    public void replaceFragment(FragmentTags fragmentTags, Bundle bundle) {

        // init manager
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        switch (fragmentTags) {

            case MoreFragment:

                mCurrentFragment = MoreFragment.newInstance();
                transaction.replace(R.id.frame_container_more, mCurrentFragment);// newInstance() is a static factory method.
                transaction.commit();

                break;
            case EditNewProfileFragment:

                mCurrentFragment = EditNewProfileFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_more, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;
            case ManageProfileFragment:

                mCurrentFragment = ManageProfileFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_more, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;
            case MyListFragment:

                mCurrentFragment = MyListFragment.newInstance();
                transaction.replace(R.id.frame_container_more, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;
            case ShowDetailsFragment:

                mCurrentFragment = ShowDetailsFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_more, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;
            case HelpFragment:

                mCurrentFragment = HelpFragment.newInstance();
                transaction.replace(R.id.frame_container_more, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;
            case AppSettingsFragment:

                mCurrentFragment = AppSettingFragment.newInstance();
                transaction.replace(R.id.frame_container_more, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;
        }
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

}

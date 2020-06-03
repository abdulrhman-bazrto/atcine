package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        replaceFragment(FragmentTags.MoreFragment, null, null);
    }

    public void replaceFragment(FragmentTags fragmentTags, Bundle bundle, ImageView ivProfile) {

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
                transaction.addSharedElement(ivProfile, ViewCompat.getTransitionName(ivProfile)).replace(R.id.frame_container_more, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
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
                transaction.addSharedElement(ivProfile, ViewCompat.getTransitionName(ivProfile)).replace(R.id.frame_container_more, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case ShowSeasonsFragment:

                mCurrentFragment = SeriesSeasonsFragment.newInstance(bundle);
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

            case HelpDetailsFragment:

                mCurrentFragment = HelpDetailsFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_more, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;


        }
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

}

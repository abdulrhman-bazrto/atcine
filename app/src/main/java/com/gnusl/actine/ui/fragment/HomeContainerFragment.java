package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;

import java.util.Stack;


public class HomeContainerFragment extends Fragment {

    View inflatedView;
    private Fragment mCurrentFragment;
    private Stack<Fragment> fragmentStack = new Stack<>();

    public HomeContainerFragment() {
    }

    public static HomeContainerFragment newInstance() {
        HomeContainerFragment fragment = new HomeContainerFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_home_container, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {
        replaceFragment(FragmentTags.HomeFragment, null);
    }

    public void replaceFragment(FragmentTags fragmentTags, Bundle bundle) {

        // init manager
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        switch (fragmentTags) {

            case HomeFragment:

                mCurrentFragment = HomeFragment.newInstance();
                transaction.replace(R.id.frame_container_home, mCurrentFragment);// newInstance() is a static factory method.
                transaction.commit();

                break;
            case ShowDetailsFragment:

                mCurrentFragment = ShowDetailsFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_home, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case ShowSeasonsFragment:

                mCurrentFragment = SeriesSeasonsFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_home, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case SearchResultFragment:

                mCurrentFragment = SearchResultFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_home, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;
        }

        fragmentStack.push(mCurrentFragment);
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public Stack<Fragment> getFragmentStack() {
        return fragmentStack;
    }
}

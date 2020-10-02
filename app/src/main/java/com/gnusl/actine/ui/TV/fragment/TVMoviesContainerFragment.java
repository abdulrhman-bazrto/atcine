package com.gnusl.actine.ui.TV.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.ui.Mobile.fragment.CategoriesFragment;
import com.gnusl.actine.ui.Mobile.fragment.HomeFragment;
import com.gnusl.actine.ui.Mobile.fragment.SearchResultFragment;
import com.gnusl.actine.ui.Mobile.fragment.SeriesSeasonsFragment;
import com.gnusl.actine.ui.Mobile.fragment.ShowDetailsFragment;

import java.util.Stack;


public class TVMoviesContainerFragment extends Fragment {

    View inflatedView;
    private Fragment mCurrentFragment;
    private Stack<Fragment> fragmentStack = new Stack<>();

    public TVMoviesContainerFragment() {
    }

    public static TVMoviesContainerFragment newInstance() {
        TVMoviesContainerFragment fragment = new TVMoviesContainerFragment();
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
        replaceFragment(FragmentTags.TVMoviesFragment, null );
    }

    public void replaceFragment(FragmentTags fragmentTags, Bundle bundle) {

        // init manager
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        switch (fragmentTags) {

            case TVMoviesFragment:

                mCurrentFragment = TVMoviesFragment.newInstance();
                transaction.replace(R.id.frame_container_home, mCurrentFragment);
                transaction.commit();

                break;

            case SearchResultFragment:

                mCurrentFragment = TVSearchResultFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_home, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case ShowDetailsFragment:

                mCurrentFragment = TVShowDetailsFragment.newInstance(bundle);
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mCurrentFragment != null)
            mCurrentFragment.setUserVisibleHint(isVisibleToUser);
    }
}

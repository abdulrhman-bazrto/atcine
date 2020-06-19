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


public class SearchContainerFragment extends Fragment {

    View inflatedView;
    private Fragment mCurrentFragment;


    public SearchContainerFragment() {
    }

    public static SearchContainerFragment newInstance() {
        SearchContainerFragment fragment = new SearchContainerFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_search_container, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {
        replaceFragment(FragmentTags.SearchFragment, null, null);
    }

    public void replaceFragment(FragmentTags fragmentTags, Bundle bundle, ImageView ivThumbnail) {

        // init manager
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        switch (fragmentTags) {

            case SearchFragment:

                mCurrentFragment = SearchFragment.newInstance();
                transaction.replace(R.id.frame_container_search, mCurrentFragment);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case SearchResultFragment:

                mCurrentFragment = SearchResultFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_search, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case ShowDetailsFragment:

                mCurrentFragment = ShowDetailsFragment.newInstance(bundle);
                transaction.addSharedElement(ivThumbnail, ViewCompat.getTransitionName(ivThumbnail)).replace(R.id.frame_container_search, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;

            case ShowSeasonsFragment:

                mCurrentFragment = SeriesSeasonsFragment.newInstance(bundle);
                transaction.replace(R.id.frame_container_search, mCurrentFragment).addToBackStack(null);// newInstance() is a static factory method.
                transaction.commit();

                break;

        }
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if(mCurrentFragment != null)
//            mCurrentFragment.setUserVisibleHint(isVisibleToUser);
//    }
}

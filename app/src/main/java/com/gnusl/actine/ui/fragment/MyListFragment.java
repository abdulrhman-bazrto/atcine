package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.MyListAdapter;
import com.gnusl.actine.ui.custom.CustomAppBarWithBack;


public class MyListFragment extends Fragment implements View.OnClickListener, HomeMovieClick {

    View inflatedView;

    private CustomAppBarWithBack cubMyListWithBack;
    private RecyclerView rvMyList;
    private MyListAdapter myListAdapter;

    public MyListFragment() {
    }

    public static MyListFragment newInstance() {
        MyListFragment fragment = new MyListFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_my_list, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        cubMyListWithBack = inflatedView.findViewById(R.id.cub_my_list_with_back);
        rvMyList = inflatedView.findViewById(R.id.rv_my_list);

        cubMyListWithBack.getTvTitle().setText("My List");

        cubMyListWithBack.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        myListAdapter = new MyListAdapter(getActivity(), this);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);

        rvMyList.setLayoutManager(layoutManager);

        rvMyList.setAdapter(myListAdapter);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    @Override
    public void onClickMovie() {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof MoreContainerFragment) {
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment);
            }
        }
    }
}

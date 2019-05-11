package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.actine.R;
import com.gnusl.actine.ui.adapter.ComingSoonListAdapter;


public class ComingSoonFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    private RecyclerView rvComingSoon;
    private ComingSoonListAdapter comingSoonListAdapter;


    public ComingSoonFragment() {
    }

    public static ComingSoonFragment newInstance() {
        ComingSoonFragment fragment = new ComingSoonFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_coming_soon, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {
        findViews();

        comingSoonListAdapter = new ComingSoonListAdapter(getActivity());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rvComingSoon.setLayoutManager(layoutManager);

        rvComingSoon.setAdapter(comingSoonListAdapter);

    }

    private void findViews() {
        rvComingSoon = inflatedView.findViewById(R.id.rv_coming_soon);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

}

package com.gnusl.actine.ui.Mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.gnusl.actine.R;
import com.gnusl.actine.ui.Mobile.activity.MainActivity;
import com.gnusl.actine.util.SharedPreferencesUtils;


public class ToWatchFragment extends Fragment implements View.OnClickListener {

    View inflatedView;

    private TextView tvSeries, tvMovies;
    private ImageView ivSeries, ivMovies;


    public ToWatchFragment() {
    }

    public static ToWatchFragment newInstance() {
        ToWatchFragment fragment = new ToWatchFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_to_watch, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {
        findViews();
        tvMovies.setOnClickListener(this);
        tvSeries.setOnClickListener(this);
        ivMovies.setOnClickListener(this);
        ivSeries.setOnClickListener(this);
    }

    private void findViews() {
        tvMovies = inflatedView.findViewById(R.id.tv_movies);
        tvSeries = inflatedView.findViewById(R.id.tv_series);
        ivMovies = inflatedView.findViewById(R.id.iv_movies);
        ivSeries = inflatedView.findViewById(R.id.iv_series);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_movies:
            case R.id.iv_movies: {
                SharedPreferencesUtils.saveCategory("movies");
                break;
            }
            case R.id.tv_series:
            case R.id.iv_series: {
                SharedPreferencesUtils.saveCategory("tvShows");
                break;
            }
        }
        if (getActivity() != null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }



}

package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.AppCategories;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.Movie;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.ui.adapter.ComingSoonListAdapter;
import com.gnusl.actine.ui.custom.CustomAppBarWithSelect;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.List;

import static com.gnusl.actine.network.Urls.MoviesSoon;


public class ComingSoonFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    View inflatedView;

    private RecyclerView rvComingSoon;
    private ComingSoonListAdapter comingSoonListAdapter;
    private CustomAppBarWithSelect cubSoon;
    private KProgressHUD progressHUD;

    private AppCategories currentCategory = AppCategories.Movies;

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

        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setMaxProgress(100)
                .show();

        DataLoader.getRequest(MoviesSoon.getLink(), this);

        cubSoon.getSpCategory().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        if (currentCategory == AppCategories.Movies)
                            return;
                        currentCategory = AppCategories.Movies;
                        init();
                        break;
                    }
                    case 1: {
                        if (currentCategory == AppCategories.TvShows)
                            return;
                        currentCategory = AppCategories.TvShows;
                        init();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentCategory = AppCategories.Movies;
            }
        });


    }

    private void findViews() {
        cubSoon = inflatedView.findViewById(R.id.cub_soon);
        rvComingSoon = inflatedView.findViewById(R.id.rv_coming_soon);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        if (progressHUD != null)
            progressHUD.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        if (progressHUD != null)
            progressHUD.dismiss();
        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (progressHUD != null)
            progressHUD.dismiss();

        switch (currentCategory) {
            case Movies: {
                List<Movie> movies = Movie.newList(jsonObject.optJSONArray("movies"));
                comingSoonListAdapter.setList(movies);
                break;
            }
            case TvShows: {
                break;
            }
        }
    }
}

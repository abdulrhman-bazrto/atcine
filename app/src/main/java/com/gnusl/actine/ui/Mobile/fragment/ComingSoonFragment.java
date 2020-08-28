package com.gnusl.actine.ui.Mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.AppCategories;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.ui.Mobile.adapter.ComingSoonListAdapter;
import com.gnusl.actine.ui.Mobile.custom.CustomAppBarWithSelect;
import com.gnusl.actine.ui.Mobile.custom.LoaderPopUp;

import org.json.JSONObject;

import java.util.List;

import static com.gnusl.actine.network.Urls.MoviesSoon;


public class ComingSoonFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    View inflatedView;

    private RecyclerView rvComingSoon;
    private ComingSoonListAdapter comingSoonListAdapter;
    private CustomAppBarWithSelect cubSoon;

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

        LoaderPopUp.show(getActivity());

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
        LoaderPopUp.dismissLoader();
        if (getActivity() != null)
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        LoaderPopUp.dismissLoader();

//        if (getActivity() != null)
            // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        LoaderPopUp.dismissLoader();

        switch (currentCategory) {
            case Movies: {
                List<Show> movies = Show.newList(jsonObject.optJSONArray("movies"), true, false, false);
                comingSoonListAdapter.setList(movies);
                if (movies.isEmpty()) {
                    inflatedView.findViewById(R.id.hint).setVisibility(View.VISIBLE);
                } else {
                    inflatedView.findViewById(R.id.hint).setVisibility(View.GONE);
                }
                break;
            }
            case TvShows: {
                break;
            }
        }
    }
}

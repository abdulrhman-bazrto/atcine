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
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.model.Category;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.HomeAdapter;
import com.gnusl.actine.ui.custom.CustomAppBar;
import com.gnusl.actine.ui.custom.SelectGenresView;
import com.gnusl.actine.util.Constants;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment implements HomeMovieClick, GenresClickEvents, ConnectionDelegate {

    View inflatedView;

    private RecyclerView rvHome;
    private HomeAdapter homeAdapter;
    private CustomAppBar cubHome;
    private SelectGenresView sgvHome;
    private KProgressHUD progressHUD;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_home, container, false);
            init();
        }

        return inflatedView;
    }

    private void init() {

        findViews();

        switch (Objects.requireNonNull(SharedPreferencesUtils.getCategory())) {
            case Movies:
                DataLoader.getRequest(Urls.MoviesGroups.getLink(), this);
                break;

            case TvShows:
                DataLoader.getRequest(Urls.SeriesGroups.getLink(), this);
                break;
        }

        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setMaxProgress(100)
                .show();

        sgvHome.setClickListener(this);

        DataLoader.getRequest(Urls.Categories.getLink(), this);

        cubHome.getSpGenres().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sgvHome.setVisibility(View.VISIBLE);
            }
        });


        rvHome.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(3);

        rvHome.setLayoutManager(layoutManager);

        homeAdapter = new HomeAdapter(getActivity(), this);

        rvHome.setAdapter(homeAdapter);

        cubHome.getSpCategory().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        if (SharedPreferencesUtils.getCategory() == AppCategories.Movies)
                            return;
                        SharedPreferencesUtils.saveCategory("movies");
                        init();
                        break;
                    }
                    case 1: {
                        if (SharedPreferencesUtils.getCategory() == AppCategories.TvShows)
                            return;
                        SharedPreferencesUtils.saveCategory("tvShows");
                        init();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SharedPreferencesUtils.saveCategory("movies");
            }
        });

    }

    private void findViews() {
        rvHome = inflatedView.findViewById(R.id.rv_home);
        cubHome = inflatedView.findViewById(R.id.cub_home);
        sgvHome = inflatedView.findViewById(R.id.sgv_home);

    }

    @Override
    public void onClickMovie(Show movie) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), movie);
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
            }
        }
    }

    @Override
    public void onClickSeries(Show series) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                bundle.putString("type", "season");
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle);
            }
        }
    }

    @Override
    public void onSelectGenres(Category genres) {
        cubHome.getSpGenres().setText(genres.getTitle());
        sgvHome.setVisibility(View.GONE);
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                switch (Objects.requireNonNull(SharedPreferencesUtils.getCategory())){
                    case TvShows:{
                        bundle.putString("searchFor", "series");
                        break;
                    }
                    case Movies:{
                        bundle.putString("searchFor", "movies");
                        break;
                    }
                }
                bundle.putString("searchType", "category");
                bundle.putString("key", String.valueOf(genres.getId()));
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.SearchResultFragment, bundle);
            }
        }
    }

    @Override
    public void onCloseGenres() {
        sgvHome.setVisibility(View.GONE);
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

        if (jsonObject.has("trend")) {
            switch (Objects.requireNonNull(SharedPreferencesUtils.getCategory())) {
                case TvShows: {
                    Show trendSerie = Show.newInstance(jsonObject.optJSONObject("trend"), false, false, false);
                    HashMap<String, List<Show>> seriesByCategories = new HashMap<>();
                    List<String> categoriesNames = new ArrayList<>();

                    JSONArray otherCategories = jsonObject.optJSONArray("categories");

                    for (int i = 0; i < otherCategories.length(); i++) {
                        JSONObject category = otherCategories.optJSONObject(i);
                        categoriesNames.add(category.optString("title"));
                        seriesByCategories.put(category.optString("title"), Show.newList(category.optJSONArray("items"), false, false, false));
                    }

                    homeAdapter.setData(trendSerie, categoriesNames, seriesByCategories);
                    break;
                }
                case Movies: {
                    Show trendMovie = Show.newInstance(jsonObject.optJSONObject("trend"), true, false, false);
                    HashMap<String, List<Show>> moviesByCategories = new HashMap<>();
                    List<String> categoriesNames = new ArrayList<>();

                    JSONArray otherCategories = jsonObject.optJSONArray("categories");

                    for (int i = 0; i < otherCategories.length(); i++) {
                        JSONObject category = otherCategories.optJSONObject(i);
                        categoriesNames.add(category.optString("title"));
                        moviesByCategories.put(category.optString("title"), Show.newList(category.optJSONArray("items"), true, false, false));
                    }

                    homeAdapter.setData(trendMovie, categoriesNames, moviesByCategories);
                    break;
                }
            }
        }

        if (jsonObject.has("categories")) {
            sgvHome.setList(Category.newList(jsonObject.optJSONArray("categories")));
        }

    }
}

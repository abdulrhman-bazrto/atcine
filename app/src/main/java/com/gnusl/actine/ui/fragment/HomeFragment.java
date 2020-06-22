package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.AppCategories;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.interfaces.LoadMoreCategoriesDelegate;
import com.gnusl.actine.model.Category;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.GenresAdapter;
import com.gnusl.actine.ui.adapter.HomeAdapter;
import com.gnusl.actine.ui.custom.CustomAppBar;
import com.gnusl.actine.ui.custom.LoaderPopUp;
import com.gnusl.actine.util.Constants;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment implements HomeMovieClick, GenresClickEvents, ConnectionDelegate, LoadMoreCategoriesDelegate {

    View inflatedView;

    private RecyclerView rvHome, rvGenres;
    private HomeAdapter homeAdapter;
    private CustomAppBar cubHome;

    private TextView tvMovies, tvSeries, tvSeeAll;
    private GenresAdapter genresAdapter;
    private List<Category> categories;

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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("", "");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_home, container, false);
            init(true);
        }

        return inflatedView;
    }

    private void init(boolean isFirstInit) {

        findViews();
        switch (Objects.requireNonNull(SharedPreferencesUtils.getCategory())) {
            case TvShows: {
                DataLoader.getRequest(Urls.SeriesGroups.getLink(), this);
                tvMovies.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_categories_unselected));
                tvSeries.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_categories_selected));
                break;
            }
            case Movies: {
                tvMovies.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_categories_selected));
                tvSeries.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_categories_unselected));
                DataLoader.getRequest(Urls.MoviesGroups.getLink(), this);
                break;
            }
        }

        LoaderPopUp.show1(getActivity());

        if (isFirstInit) {
            genresAdapter = new GenresAdapter(getActivity(), new ArrayList<>(), HomeFragment.this);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            rvGenres.setLayoutManager(gridLayoutManager);

            rvGenres.setAdapter(genresAdapter);
        }

        rvHome.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(5);

        rvHome.setLayoutManager(layoutManager);

        homeAdapter = new HomeAdapter(getActivity(), rvHome, this, this, this);

        rvHome.setAdapter(homeAdapter);


        tvMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesUtils.getCategory() == AppCategories.Movies)
                    return;
                tvMovies.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_categories_selected));
                tvSeries.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_categories_unselected));
                SharedPreferencesUtils.saveCategory("movies");
                init(false);
            }
        });

        tvSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesUtils.getCategory() == AppCategories.TvShows)
                    return;
                SharedPreferencesUtils.saveCategory("tvShows");
                tvSeries.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_categories_selected));
                tvMovies.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_categories_unselected));
                init(false);
            }
        });

        tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                    if (fragment instanceof HomeContainerFragment) {
                        Bundle bundle = new Bundle();
                        Gson gson = new Gson();
                        bundle.putString("categories", gson.toJson(categories));
                        ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.CategoriesFragment, bundle, null);
                    }
                }
            }
        });

    }

    private void findViews() {
        rvHome = inflatedView.findViewById(R.id.rv_home);
        cubHome = inflatedView.findViewById(R.id.cub_home);

        tvMovies = inflatedView.findViewById(R.id.tv_movies);
        tvSeries = inflatedView.findViewById(R.id.tv_series);
        rvGenres = inflatedView.findViewById(R.id.rv_genres);
        tvSeeAll = inflatedView.findViewById(R.id.tv_see_all);

    }

    @Override
    public void onClickMovie(Show movie, ImageView ivThumbnail) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), movie);
                bundle.putString("transition", ViewCompat.getTransitionName(ivThumbnail));
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail);
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
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
            }
        }
    }

    @Override
    public void onSelectGenres(Category genres) {
//        cubHome.getSpGenres().setText(genres.getTitle());
//        sgvHome.setVisibility(View.GONE);
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof HomeContainerFragment) {
                Bundle bundle = new Bundle();
                switch (Objects.requireNonNull(SharedPreferencesUtils.getCategory())) {
                    case TvShows: {
                        bundle.putString("searchFor", "series");
                        break;
                    }
                    case Movies: {
                        bundle.putString("searchFor", "movies");
                        break;
                    }
                }
                bundle.putString("searchType", "category");
                bundle.putString("key", String.valueOf(genres.getId()));
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.SearchResultFragment, bundle, null);
            }
        }
    }

    @Override
    public void onCloseGenres() {

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
        if (getActivity() != null)
            Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (jsonObject.has("trend")) {
            switch (Objects.requireNonNull(SharedPreferencesUtils.getCategory())) {
                case TvShows: {
                    if (genresAdapter != null) {
                        categories = Category.newList(jsonObject.optJSONArray("series_categories"));
                        List<Category> categoriesTemp = new ArrayList<>();
                        for (int i = 0; i < categories.size(); i++) {
                            if (i < 4)
                                categoriesTemp.add(categories.get(i));
                            else
                                break;
                        }
                        genresAdapter.setList(categoriesTemp);
                        rvGenres.scheduleLayoutAnimation();

                    }
                    Show trendSerie = Show.newInstance(jsonObject.optJSONObject("trend"), false, false, false);
                    List<Category> categories = Category.newList(jsonObject.optJSONArray("categories"), false);
                    homeAdapter.setData(trendSerie, categories);
                    break;
                }
                case Movies: {
                    if (genresAdapter != null) {
                        categories = Category.newList(jsonObject.optJSONArray("movies_categories"));
                        List<Category> categoriesTemp = new ArrayList<>();
                        for (int i = 0; i < categories.size(); i++) {
                            if (i < 4)
                                categoriesTemp.add(categories.get(i));
                            else
                                break;
                        }
                        genresAdapter.setList(categoriesTemp);
                        rvGenres.scheduleLayoutAnimation();

                    }
                    Show trendMovie = Show.newInstance(jsonObject.optJSONObject("trend"), true, false, false);

                    List<Category> categories = Category.newList(jsonObject.optJSONArray("categories"), true);
                    homeAdapter.setData(trendMovie, categories);
                    break;
                }
            }
            LoaderPopUp.dismissLoader();
        }

    }

    @Override
    public void loadMoreCategories(int skip) {

    }

    public void refreshTrendShow() {
        Show show = homeAdapter.getTrendShow();

        if (show == null)
            return;

        String url = "";
        if (show.getIsMovie()) {
            url = Urls.Movie.getLink();
        } else if (show.getIsEpisode()) {
            url = Urls.Episode.getLink();
        }
        if (url.isEmpty())
            return;
        DataLoader.getRequest(url + show.getId(), new ConnectionDelegate() {
            @Override
            public void onConnectionError(int code, String message) {

            }

            @Override
            public void onConnectionError(ANError anError) {
                Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                if (jsonObject.has("is_favourite")) {
                    show.setIsFavourite(jsonObject.optBoolean("is_favourite"));
                    homeAdapter.setTrendShow(show);
                }
            }
        });
    }

}

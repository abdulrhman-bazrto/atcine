package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.MyListAdapter;
import com.gnusl.actine.ui.custom.CustomAppBarWithSelectAndBack;
import com.gnusl.actine.util.Constants;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.List;


public class MyListFragment extends Fragment implements View.OnClickListener, HomeMovieClick, ConnectionDelegate {

    View inflatedView;

    private CustomAppBarWithSelectAndBack cubMyListWithBack;
    private RecyclerView rvMyList;
    private MyListAdapter myListAdapter;
    private AppCategories currentCategory = AppCategories.Movies;
    private KProgressHUD progressHUD;

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

        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setMaxProgress(100)
                .show();

        switch (currentCategory) {
            case Movies:
                DataLoader.getRequest(Urls.MoviesMyList.getLink(), this);
                break;

            case TvShows:
                DataLoader.getRequest(Urls.SeriesMyList.getLink(), this);
                break;
        }

        cubMyListWithBack.getSpCategory().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    @Override
    public void onClickMovie(Show show) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof MoreContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), show);
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
            }
        }
    }

    @Override
    public void onClickSeries(Show series) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof MoreContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                bundle.putString("type", "season");
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle);
            }
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
            case TvShows:
                List<Show> series = Show.newList(jsonObject.optJSONArray("series"), false, false, false);
                myListAdapter.setList(series);
                break;

            case Movies:
                List<Show> movies = Show.newList(jsonObject.optJSONArray("movies"), true, false, false);
                myListAdapter.setList(movies);
                break;
        }
    }
}

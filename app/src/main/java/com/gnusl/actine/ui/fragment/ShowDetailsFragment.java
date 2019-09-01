package com.gnusl.actine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.DownloadDelegate;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.activity.WatchActivity;
import com.gnusl.actine.ui.adapter.CommentsAdapter;
import com.gnusl.actine.ui.adapter.MovieMoreLikeAdapter;
import com.gnusl.actine.ui.custom.CustomAppBarWithBack;
import com.gnusl.actine.util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ShowDetailsFragment extends Fragment implements HomeMovieClick, View.OnClickListener, ConnectionDelegate, DownloadDelegate {

    View inflatedView;

    private RecyclerView rvSuggest;
    private CustomAppBarWithBack cubHomeWithBack;
    private Button btnReactions, btnDownload;
    private View clMoreLikeThis, clReactions;
    private RecyclerView rvComments;
    private TextView tvWatchTime, tvYear, tvShowTitle, tvShowCaption;
    private ImageView ivShowCover, ivPlayShow;
    private Button btnAddToMyList;

    private CommentsAdapter commentsAdapter;
    private MovieMoreLikeAdapter movieMoreLikeAdapter;
    private Show show;

    public ShowDetailsFragment() {
    }

    public static ShowDetailsFragment newInstance(Bundle bundle) {
        ShowDetailsFragment fragment = new ShowDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            show = (Show) getArguments().getSerializable(Constants.HomeDetailsExtra.getConst());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_show_details, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        findViews();

        tvShowTitle.setText(show.getTitle());
        tvYear.setText(String.valueOf(show.getYear()));
        tvWatchTime.setText(show.getWatchTime());
        tvShowCaption.setText(show.getDescription());
        Picasso.with(getActivity()).load(show.getCoverImageUrl()).into(ivShowCover);
        if (show.getIsFavourite()) {
            btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_check_white), null, null, null);
        }

        btnAddToMyList.setOnClickListener(this);

        getRelatedShows();

        btnReactions.setOnClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        rvSuggest.setLayoutManager(gridLayoutManager);

        movieMoreLikeAdapter = new MovieMoreLikeAdapter(getActivity(), this);

        rvSuggest.setAdapter(movieMoreLikeAdapter);


        commentsAdapter = new CommentsAdapter(getActivity(), new ArrayList<String>());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rvComments.setLayoutManager(layoutManager);

        rvComments.setAdapter(commentsAdapter);

        if (show.getIsDownloaded()){
            btnDownload.setText("Downloaded");
        }

    }

    private void getRelatedShows() {

        if (show.getIsMovie())
            DataLoader.getRequest(Urls.MovieSuggest.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
        else
            DataLoader.getRequest(Urls.SerieSuggest.getLink().replaceAll("%id%", String.valueOf(show.getSeriesId())), this);
    }

    private void findViews() {
        rvSuggest = inflatedView.findViewById(R.id.rv_suggest);
        cubHomeWithBack = inflatedView.findViewById(R.id.cub_home_with_back);
        btnReactions = inflatedView.findViewById(R.id.btn_reactions);
        clMoreLikeThis = inflatedView.findViewById(R.id.cl_more_like_this);
        clReactions = inflatedView.findViewById(R.id.cl_reactions);
        rvComments = inflatedView.findViewById(R.id.rv_comments);

        tvShowTitle = inflatedView.findViewById(R.id.tv_show_title);
        tvShowCaption = inflatedView.findViewById(R.id.tv_show_caption);
        tvYear = inflatedView.findViewById(R.id.tv_year);
        tvWatchTime = inflatedView.findViewById(R.id.tv_watch_time);
        ivShowCover = inflatedView.findViewById(R.id.iv_movie_image);
        ivPlayShow = inflatedView.findViewById(R.id.iv_play_show);
        btnAddToMyList = inflatedView.findViewById(R.id.btn_add_to_my_list);
        btnDownload = inflatedView.findViewById(R.id.btn_download);

        ivPlayShow.setOnClickListener(this);
        btnDownload.setOnClickListener(this);

        cubHomeWithBack.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    @Override
    public void onClickMovie(Show movie) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), movie);
            if (fragment instanceof HomeContainerFragment) {
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
            } else if (fragment instanceof MoreContainerFragment) {
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
            } else if (fragment instanceof SearchContainerFragment) {
                ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
            }
        }
    }

    @Override
    public void onClickSeries(Show series) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof HomeContainerFragment) {
                if (series.getIsEpisode()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
                } else if (series.getIsSeason()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "episode");
                    bundle.putString("seasonId", String.valueOf(series.getId()));
                    ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle);
                } else { // click on series
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "season");
                    ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle);
                }
            } else if (fragment instanceof MoreContainerFragment) {
                if (series.getIsEpisode()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
                } else if (series.getIsSeason()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "episode");
                    bundle.putString("seasonId", String.valueOf(series.getId()));
                    ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle);
                } else { // click on series
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "season");
                    ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle);
                }
            } else if (fragment instanceof SearchContainerFragment) {
                if (series.getIsEpisode()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
                } else if (series.getIsSeason()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "episode");
                    bundle.putString("seasonId", String.valueOf(series.getId()));
                    ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle);
                } else { // click on series
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "season");
                    ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reactions: {
                if (clMoreLikeThis.getVisibility() == View.VISIBLE) {
                    clMoreLikeThis.setVisibility(View.GONE);
                    clReactions.setVisibility(View.VISIBLE);
                    sendDetailsRequest();
                } else {
                    clMoreLikeThis.setVisibility(View.VISIBLE);
                    clReactions.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.btn_add_to_my_list: {
                sendFavoriteRequest();
                break;
            }
            case R.id.iv_play_show: {
                Intent intent = new Intent(getActivity(), WatchActivity.class);
                intent.putExtra("show", show);
                startActivity(intent);
                break;
            }
            case R.id.btn_download: {
                if (!show.getIsDownloaded()) {
                    File internalStorage = getActivity().getFilesDir();
                    String url = show.getVideoUrl();
                    if (url.contains("_.m3u8")) {
                        url = url.replaceAll("_.m3u8", ".mp4");
                    } else {
                        url = url.replaceAll(".m3u8", ".mp4");
                    }
                    Toast.makeText(getActivity(),"Downloading",Toast.LENGTH_SHORT).show();
                    DataLoader.downloadRequest(url, internalStorage.getAbsolutePath(), show.getTitle() + ".mp4", this);
                }
                break;
            }
        }
    }

    private void sendDetailsRequest() {


    }

    private void sendFavoriteRequest() {

        String url = "";
        if (show.getIsMovie())
            url = Urls.MovieFavorite.getLink().replaceAll("%id%", String.valueOf(show.getId()));
        else
            url = Urls.SerieFavorite.getLink().replaceAll("%id%", String.valueOf(show.getSeriesId()));

        DataLoader.postRequest(url, new ConnectionDelegate() {
            @Override
            public void onConnectionError(int code, String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionError(ANError anError) {
                Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                if (jsonObject.optString("status").equalsIgnoreCase("added")) {
                    show.setIsFavourite(true);
                    btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_check_white), null, null, null);
                } else {
                    show.setIsFavourite(false);
                    btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_mylist), null, null, null);
                }
            }
        });
    }

    @Override
    public void onConnectionError(int code, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        Toast.makeText(getActivity(), anError.getErrorBody(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (jsonObject.has("movie")) {
            List<Show> movies = Show.newList(jsonObject.optJSONArray("movies"), true, false, false);
            movieMoreLikeAdapter.setList(movies);
        } else if (jsonObject.has("series")) {
            List<Show> series = Show.newList(jsonObject.optJSONArray("series"), false, false, false);
            movieMoreLikeAdapter.setList(series);
        }
    }

    @Override
    public void onDownloadProgress(String fileDir, String fileName, int progress) {

    }

    @Override
    public void onDownloadError(ANError anError) {

    }

    @Override
    public void onDownloadSuccess(String fileDir, String fileName) {
        if (fileName.equalsIgnoreCase(show.getTitle()+".mp4")){
            DataLoader.postRequest(Urls.MovieDownload.getLink().replaceAll("%id%", String.valueOf(show.getId())),this);
        }
    }
}

package com.gnusl.actine.ui.TV.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.MediaRouteButton;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.CommentLongClickEvent;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.DownloadDelegate;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.model.Cast;
import com.gnusl.actine.model.Comment;
import com.gnusl.actine.model.DBShow;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.Mobile.activity.MainActivity;
import com.gnusl.actine.ui.Mobile.activity.WatchActivity;
import com.gnusl.actine.ui.Mobile.activity.WatchActivity2;
import com.gnusl.actine.ui.Mobile.adapter.CastAdapter;
import com.gnusl.actine.ui.Mobile.adapter.CommentsAdapter;
import com.gnusl.actine.ui.Mobile.adapter.HomeMovieListAdapter;
import com.gnusl.actine.ui.Mobile.adapter.ViewPagerAdapter;
import com.gnusl.actine.ui.Mobile.custom.CustomAppBarWithBack;
import com.gnusl.actine.ui.Mobile.custom.LoaderPopUp;
import com.gnusl.actine.ui.Mobile.custom.MarginItemDecoration;
import com.gnusl.actine.ui.Mobile.custom.NonScrollHomeViewPager1;
import com.gnusl.actine.ui.Mobile.fragment.EpisodesFragment;
import com.gnusl.actine.ui.Mobile.fragment.HomeContainerFragment;
import com.gnusl.actine.ui.Mobile.fragment.MoreContainerFragment;
import com.gnusl.actine.ui.Mobile.fragment.OverviewFragment;
import com.gnusl.actine.ui.Mobile.fragment.ReviewsFragment;
import com.gnusl.actine.ui.Mobile.fragment.SearchContainerFragment;
import com.gnusl.actine.ui.Mobile.fragment.TrailerFragment;
import com.gnusl.actine.ui.TV.activity.TVMainActivity;
import com.gnusl.actine.util.Constants;
import com.gnusl.actine.util.ObjectBox;
import com.gnusl.actine.util.Utils;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.objectbox.Box;


public class TVShowDetailsFragment extends Fragment implements HomeMovieClick, View.OnClickListener, ConnectionDelegate, DownloadDelegate, CommentLongClickEvent {

    View inflatedView;

    private RecyclerView rvSuggest;
    private Button btnPlayShow, btnShare, btnAddToMyList;
    private RecyclerView rvCast;
    private TextView tvCategory, tvWatchTime, tvShowTitle, tvShowCaption, tvIMDBRate, tvTomatoRate, tvPlayTrailer;
    private ImageView ivShowCover, ivPlayTrailer, iv_tomato,iv_imdb, ivClock, ivStar;
    CastAdapter castAdapter;
    private HomeMovieListAdapter homeMovieListAdapter;
    private Show show;

    ArrayList<Cast> cast;

    private CommentsAdapter commentsAdapter;
    private RecyclerView rvComments;
    private TextView tvCommentsCount, tvLikesCount, tvViewsCount, tvAddComment;
    private EditText etCommentText;
    private View clRelated, clCast;

    private ViewPagerAdapter adapter;
    private TabLayout tlMainTabLayout;
    private NonScrollHomeViewPager1 vpMainContainer;
    View mIndicator;
    private int indicatorWidth;
    private EpisodesFragment episodesFragment;
    private ReviewsFragment reviewsFragment;
    private OverviewFragment overviewFragment;
    private View clSeries, clReviews;

    public TVShowDetailsFragment() {
    }

    public static TVShowDetailsFragment newInstance(Bundle bundle) {
        TVShowDetailsFragment fragment = new TVShowDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            show = (Show) getArguments().getSerializable(Constants.HomeDetailsExtra.getConst());
        }
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_tv_show_details, container, false);
            init();
        }
        return inflatedView;
    }


    private void init() {

        findViews();
        if (show.getIsMovie()) {
            clSeries.setVisibility(View.GONE);
        } else {
            test();
            clCast.setVisibility(View.GONE);
            clReviews.setVisibility(View.GONE);
            ivPlayTrailer.setVisibility(View.GONE);
            btnPlayShow.setVisibility(View.GONE);
            tvPlayTrailer.setVisibility(View.GONE);
            tvWatchTime.setVisibility(View.GONE);
            ivClock.setVisibility(View.GONE);
        }
        tvShowTitle.setText(show.getTitle());
        tvWatchTime.setText(show.getWatchTime());
        tvShowCaption.setText(show.getDescription());
        tvIMDBRate.setText(show.getImdbRate());

        if (!show.getImdbRate().isEmpty()) {
            tvIMDBRate.setText(show.getImdbRate());
        }else {
            tvIMDBRate.setVisibility(View.GONE);
            iv_imdb.setVisibility(View.GONE);
        }

        if (!show.getRottenTomatoes().isEmpty()) {
            tvTomatoRate.setText(show.getRottenTomatoes());
        } else {
            tvTomatoRate.setVisibility(View.GONE);
            iv_tomato.setVisibility(View.GONE);
            ivStar.setVisibility(View.GONE);
        }
        tvCategory.setText(show.getCategory());
        Picasso.with(getActivity()).load(show.getCoverImageUrl()).into(ivShowCover);


        if (show.getIsFavourite()) {
            btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_filled_heart), null, null, null);
        }

        castAdapter = new CastAdapter(getActivity(), new ArrayList<Cast>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvCast.setLayoutManager(layoutManager);
        rvCast.setAdapter(castAdapter);

        homeMovieListAdapter = new HomeMovieListAdapter(getActivity(), new ArrayList<>(), this, "TV");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rvSuggest.setLayoutManager(linearLayoutManager);
        rvSuggest.setAdapter(homeMovieListAdapter);

        commentsAdapter = new CommentsAdapter(getActivity(), new ArrayList<Comment>(), this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rvComments.setLayoutManager(layoutManager1);
        int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                getActivity().getResources().getDisplayMetrics());
        rvComments.addItemDecoration(new MarginItemDecoration(20 * dp1, getActivity(), 0));
        rvComments.setAdapter(commentsAdapter);


        String url = "";
        if (show.getIsMovie()) {
            url = Urls.Movie.getLink();
        } else {
            url = Urls.Series.getLink();
        }
        sendGetCommentsRequest();
        getRelatedShows();
        DataLoader.getRequest(url + show.getId(), this);


//        Utils.setOnFocusScale(btnAddToMyList);
//        Utils.setOnFocusScale(btnDownload);
//        Utils.setOnFocusScale(btnReactions);
//        Utils.setOnFocusScale(ivAddComment);
//        Utils.setOnFocusScale(ivSendComment);
//        Utils.setOnFocusScale(btnPlayShow);
//        Utils.setOnFocusScale(btnShare);
//        Utils.setOnFocusScale(tvAddComment);
//        btnPlayShow.requestFocus();

    }

    private void getRelatedShows() {

        if (show.getIsMovie())
            DataLoader.getRequest(Urls.MovieSuggest.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
        else
            DataLoader.getRequest(Urls.SerieSuggest.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
    }

    private void findViews() {
        tvShowTitle = inflatedView.findViewById(R.id.tv_show_title);
        tvPlayTrailer = inflatedView.findViewById(R.id.tv_play_trailer);
        tvShowCaption = inflatedView.findViewById(R.id.tv_overview);
        tvWatchTime = inflatedView.findViewById(R.id.tv_watch_time);
        tvCategory = inflatedView.findViewById(R.id.tv_category);
        tvIMDBRate = inflatedView.findViewById(R.id.tv_imdb_rate);
        tvTomatoRate = inflatedView.findViewById(R.id.tv_tomato_rate);
        btnAddToMyList = inflatedView.findViewById(R.id.btn_favorite);
        btnShare = inflatedView.findViewById(R.id.btn_share);
        btnPlayShow = inflatedView.findViewById(R.id.iv_play_show);
        ivPlayTrailer = inflatedView.findViewById(R.id.iv_play_trailer);
        ivShowCover = inflatedView.findViewById(R.id.iv_movie_cover);
        rvCast = inflatedView.findViewById(R.id.rv_cast);
        rvSuggest = inflatedView.findViewById(R.id.rv_suggest);
        iv_tomato = inflatedView.findViewById(R.id.iv_tomato);
        iv_imdb = inflatedView.findViewById(R.id.iv_imdb);
        ivClock = inflatedView.findViewById(R.id.iv_clock);
        ivStar = inflatedView.findViewById(R.id.iv_star2);

        rvComments = inflatedView.findViewById(R.id.rv_comments);
        tvCommentsCount = inflatedView.findViewById(R.id.tv_comments_count);
        tvLikesCount = inflatedView.findViewById(R.id.tv_likes_count);
        tvViewsCount = inflatedView.findViewById(R.id.tv_views_count);
        etCommentText = inflatedView.findViewById(R.id.et_comment_text);
        tvAddComment = inflatedView.findViewById(R.id.tv_add_comment);

        tlMainTabLayout = inflatedView.findViewById(R.id.tl_main_tab_layout);
        vpMainContainer = inflatedView.findViewById(R.id.vp_main_container);
        mIndicator = inflatedView.findViewById(R.id.indicator);

        clCast = inflatedView.findViewById(R.id.cl_cast);
        clRelated = inflatedView.findViewById(R.id.cl_more_like_this);
        clReviews = inflatedView.findViewById(R.id.cl_reviews);
        clSeries = inflatedView.findViewById(R.id.cl_series);
        btnPlayShow.setOnClickListener(this);
        ivPlayTrailer.setOnClickListener(this);
        btnAddToMyList.setOnClickListener(this);

        tvLikesCount.setOnClickListener(this);
        tvAddComment.setOnClickListener(this);

        Utils.setOnFocusScale(ivPlayTrailer);
        Utils.setOnFocusScale(btnShare);
        Utils.setOnFocusScale(btnAddToMyList);
        Utils.setOnFocusScale(btnPlayShow);
        Utils.setOnFocusScale(tvAddComment);


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sharedText = "watch " + show.getTitle() + " on the following link: \n";
                sharedText += "https://atcine.com/movie/" + show.getId() + "/watch";

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, sharedText);
                startActivity(Intent.createChooser(shareIntent, "choose.."));
            }
        });
    }

    @Override
    public void onClickMovie(Show movie, ImageView ivThumbnail) {
        if (getActivity() != null) {
            Fragment fragment = ((TVMainActivity) getActivity()).getmCurrentFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), movie);
            if (fragment instanceof TVMoviesContainerFragment) {
                ((TVMoviesContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
            } else if (fragment instanceof TVSeriesContainerFragment) {
                ((TVSeriesContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle);
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
                    ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, null, null, null);
                } else if (series.getIsSeason()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "episode");
                    bundle.putString("seasonId", String.valueOf(series.getId()));
                    ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null, null, null);
                } else { // click on series
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "season");
                    ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null, null, null);
                }
            } else if (fragment instanceof MoreContainerFragment) {
                if (series.getIsEpisode()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, null);
                } else if (series.getIsSeason()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "episode");
                    bundle.putString("seasonId", String.valueOf(series.getId()));
                    ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
                } else { // click on series
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "season");
                    ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
                }
            } else if (fragment instanceof SearchContainerFragment) {
                if (series.getIsEpisode()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, null);
                } else if (series.getIsSeason()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "episode");
                    bundle.putString("seasonId", String.valueOf(series.getId()));
                    ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
                } else { // click on series
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), series);
                    bundle.putString("type", "season");
                    ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowSeasonsFragment, bundle, null);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_favorite: {
                sendFavoriteRequest();
                break;
            }
            case R.id.iv_play_show: {
                Intent intent = new Intent(getActivity(), WatchActivity.class);
                intent.putExtra("show", show);
                startActivity(intent);
                break;
            }
            case R.id.iv_play_trailer: {

                break;
            }
            case R.id.tv_likes_count: {
                String url = "";
                if (show.getIsMovie()) {
                    url = Urls.MovieLike.getLink();
                } else {
                    url = Urls.SeriesLike.getLink();
                }
                DataLoader.postRequest(url.replaceAll("%id%", String.valueOf(show.getId())), new ConnectionDelegate() {
                    @Override
                    public void onConnectionError(int code, String message) {

                    }

                    @Override
                    public void onConnectionError(ANError anError) {

                    }

                    @Override
                    public void onConnectionSuccess(JSONObject jsonObject) {
                        if (jsonObject.has("status")) {
                            if (jsonObject.optString("status").equalsIgnoreCase("added")) {
                                if (getActivity() != null) {
                                    tvLikesCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_liked), null, null, null);
                                    tvLikesCount.setText(String.valueOf(Integer.parseInt(tvLikesCount.getText().toString()) + 1));
                                }
                            } else {
                                if (getActivity() != null) {
                                    tvLikesCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_rate), null, null, null);
                                    tvLikesCount.setText(String.valueOf(Integer.parseInt(tvLikesCount.getText().toString()) - 1));
                                }
                            }
                        }
                    }
                });
                break;
            }
            case R.id.tv_add_comment: {
                showAddCommentDialog();
                break;
            }
            case R.id.iv_send_comment: {
                if (etCommentText.getText().toString().isEmpty())
                    return;
                HashMap<String, String> body = new HashMap<>();
                body.put("comment", etCommentText.getText().toString());

                String url = "";
                if (show.getIsMovie()) {
                    url = Urls.MovieComments.getLink();
                } else {
                    url = Urls.SeriesComments.getLink();
                }
                DataLoader.postRequest(url.replaceAll("%id%", String.valueOf(show.getId())), body, new ConnectionDelegate() {
                    @Override
                    public void onConnectionError(int code, String message) {

                    }

                    @Override
                    public void onConnectionError(ANError anError) {
                        // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectionSuccess(JSONObject jsonObject) {
                        if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("success")) {
                            etCommentText.setText("");
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            //Find the currently focused view, so we can grab the correct window token from it.
                            imm.hideSoftInputFromWindow(etCommentText.getWindowToken(), 0);
                            String url = "";
                            if (show.getIsMovie()) {
                                url = Urls.MovieComments.getLink();
                            } else {
                                url = Urls.SeriesComments.getLink();
                            }
                            DataLoader.getRequest(url.replaceAll("%id%", String.valueOf(show.getId())), TVShowDetailsFragment.this);
                        }
                    }
                });
                break;
            }

        }
    }

    private void showAddCommentDialog() {
        final Dialog addCommentDialog = new Dialog(getActivity(), R.style.CustomDialogTheme);
//        addCommentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (addCommentDialog.getWindow() != null) {
            WindowManager.LayoutParams lp = addCommentDialog.getWindow().getAttributes();
            lp.dimAmount = 1.0f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
            addCommentDialog.getWindow().setAttributes(lp);
            addCommentDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation; //style id

        }
//            addCommentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addCommentDialog.setContentView(R.layout.dialog_add_comment);
        addCommentDialog.setCancelable(true);

        ImageView ivClose;
        EditText etComment;
        Button btnConfirm;
        ivClose = addCommentDialog.findViewById(R.id.iv_close);
        btnConfirm = addCommentDialog.findViewById(R.id.btn_confirm);
        etComment = addCommentDialog.findViewById(R.id.et_comment);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentDialog.dismiss();
            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etComment.getText().toString().trim().isEmpty()) {
                    etComment.setError(getActivity().getString(R.string.cant_be_empty));
                    return;
                }

                LoaderPopUp.show(getActivity());

                HashMap<String, String> body = new HashMap<>();
                body.put("comment", etComment.getText().toString());

                String url = "";
                if (show.getIsMovie()) {
                    url = Urls.MovieComments.getLink();
                } else {
                    url = Urls.SeriesComments.getLink();
                }
                DataLoader.postRequest(url.replaceAll("%id%", String.valueOf(show.getId())), body, new ConnectionDelegate() {
                    @Override
                    public void onConnectionError(int code, String message) {

                    }

                    @Override
                    public void onConnectionError(ANError anError) {
                        LoaderPopUp.dismissLoader();
                        // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectionSuccess(JSONObject jsonObject) {
                        LoaderPopUp.dismissLoader();
                        if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("success")) {
                            etComment.setText("");
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            //Find the currently focused view, so we can grab the correct window token from it.
                            imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
//                            clInputLayout.setVisibility(View.GONE);
                            String url = "";
                            if (show.getIsMovie()) {
                                url = Urls.MovieComments.getLink();
                            } else {
                                url = Urls.SeriesComments.getLink();
                            }
                            DataLoader.getRequest(url.replaceAll("%id%", String.valueOf(show.getId())), TVShowDetailsFragment.this);
                            addCommentDialog.dismiss();
                        } else {
                            // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        addCommentDialog.show();
    }

    private void sendGetCommentsRequest() {
        if (show.getIsMovie()) {
            DataLoader.getRequest(Urls.MovieComments.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
        } else {
            DataLoader.getRequest(Urls.SeriesComments.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
        }
    }

    private void sendFavoriteRequest() {

        String url = "";
        if (show.getIsMovie())
            url = Urls.MovieFavorite.getLink().replaceAll("%id%", String.valueOf(show.getId()));
        else
            url = Urls.SerieFavorite.getLink().replaceAll("%id%", String.valueOf(show.getId()));

        DataLoader.postRequest(url, new ConnectionDelegate() {
            @Override
            public void onConnectionError(int code, String message) {
                if (getActivity() != null)
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionError(ANError anError) {
//                Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
//                if (getActivity() != null)
                // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                if (jsonObject.optString("status").equalsIgnoreCase("added")) {
                    show.setIsFavourite(true);
                    if (getActivity() != null)
                        btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_filled_heart), null, null, null);
                } else {
                    show.setIsFavourite(false);
                    if (getActivity() != null)
                        btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_empty_heart), null, null, null);
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
//        Toast.makeText(getActivity(), anError.getErrorBody(), Toast.LENGTH_SHORT).show();
        // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (jsonObject.has("movies")) {
            List<Show> movies = Show.newList(jsonObject.optJSONArray("movies"), true, false, false);
            homeMovieListAdapter.setList(movies);
            if (movies.size() > 0) {
                clRelated.setVisibility(View.VISIBLE);
            } else {
                clRelated.setVisibility(View.GONE);
            }
        } else if (jsonObject.has("series")) {
            if (jsonObject.optJSONArray("series") != null) {
                List<Show> series = Show.newList(jsonObject.optJSONArray("series"), false, false, false);
                homeMovieListAdapter.setList(series);
                if (series.size() > 0) {
                    clRelated.setVisibility(View.VISIBLE);
                } else {
                    clRelated.setVisibility(View.GONE);
                }
            } else if (jsonObject.optJSONObject("series") != null && jsonObject.optJSONObject("series").has("seasons")) {
                show = Show.newInstance(jsonObject.optJSONObject("series"), show.getIsMovie(), show.getIsSeason(), show.getIsEpisode());
                if (jsonObject.optJSONObject("series").has("crew")) {
                    cast = (ArrayList<Cast>) Cast.newArray(jsonObject.optJSONObject("series").optJSONArray("crew"));
                    if (show.getIsMovie())
                        castAdapter.setList(cast);
                    else
                        overviewFragment.setCastList(cast);
                }
//                show.setSeasons(jsonObject.optJSONObject("series").optJSONArray("seasons"));
                if (!show.getIsMovie())
                    episodesFragment.setShow(show);
            }
        }

        if (jsonObject.has("comments")) {
            List<Comment> comments = Comment.newArray(jsonObject.optJSONArray("comments"));
            commentsAdapter.setList(comments);
        }

        if (jsonObject.has("like_count") && jsonObject.has("comment_count") && jsonObject.has("visited")) {
            tvLikesCount.setText(String.valueOf(jsonObject.optInt("like_count")));
            tvViewsCount.setText(String.format(Locale.getDefault(), "%d", jsonObject.optInt("visited")));
            tvCommentsCount.setText(String.valueOf(jsonObject.optInt("comment_count")));

            show.setIsDownloaded(jsonObject.optBoolean("is_downloaded"));
            show.setIsLike(jsonObject.optBoolean("is_like"));

            if (jsonObject.optBoolean("is_like")) {
                if (getActivity() != null)
                    tvLikesCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_liked), null, null, null);
            } else {
                if (getActivity() != null)
                    tvLikesCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_rate), null, null, null);
            }

        }


        if (jsonObject.has("crew")) {
            cast = (ArrayList<Cast>) Cast.newArray(jsonObject.optJSONArray("crew"));
            if (show.getIsMovie())
                castAdapter.setList(cast);
            else
                overviewFragment.setCastList(cast);

            if (cast.size() > 0) {
                clCast.setVisibility(View.VISIBLE);
            } else {
                clCast.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDownloadProgress(String fileDir, String fileName, int progress) {
        if (getActivity() != null) {
//            if (downloadingToast == null) {
////                downloadingToast = Toast.makeText(getActivity(), "Downloading (" + progress + "%)", Toast.LENGTH_SHORT);
//            }
//            downloadingToast.setText("Downloading (" + progress + "%)");
//            downloadingToast.show();
        }
    }

    @Override
    public void onDownloadError(ANError anError) {

    }

    @Override
    public void onDownloadSuccess(String fileDir, String fileName) {
        if (fileName.equalsIgnoreCase(show.getTitle() + ".mp4")) {
            DataLoader.postRequest(Urls.MovieDownload.getLink().replaceAll("%id%", String.valueOf(show.getId())), new ConnectionDelegate() {
                @Override
                public void onConnectionError(int code, String message) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConnectionError(ANError anError) {
//                    Toast.makeText(getActivity(), anError.getErrorBody(), Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConnectionSuccess(JSONObject jsonObject) {
//                    btnDownload.setText(getActivity().getString(R.string.downloaded));
                    show.setIsDownloaded(true);

                    Box<DBShow> dbShowBox = ObjectBox.get().boxFor(DBShow.class);
                    DBShow dbShow = show.getDBShowObject();
                    dbShowBox.put(dbShow);
                }
            });
        }
    }

    @Override
    public void onLongClickComment(Comment comment) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(getActivity().getString(R.string.delete_this_comment));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getActivity().getString(R.string.ok),
                (dialog, which) -> {
                    String url = "";
                    if (show.getIsMovie()) {
                        url = Urls.MovieComment.getLink();
                    } else {
                        url = Urls.SeriesCommentDelete.getLink();
                    }

                    DataLoader.postRequest(url.replaceAll("%id%", String.valueOf(comment.getId())), new ConnectionDelegate() {
                        @Override
                        public void onConnectionError(int code, String message) {

                        }

                        @Override
                        public void onConnectionError(ANError anError) {
                            // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onConnectionSuccess(JSONObject jsonObject) {
                            String url = "";
                            if (show.getIsMovie()) {
                                url = Urls.MovieComments.getLink();
                            } else {
                                url = Urls.SeriesComments.getLink();
                            }
                            DataLoader.getRequest(url.replaceAll("%id%", String.valueOf(show.getId())), TVShowDetailsFragment.this);
                        }
                    });
                    dialog.dismiss();
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getActivity().getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

        alertDialog.show();
    }

    private void test() {
        setupViewPager(vpMainContainer);
        vpMainContainer.setOffscreenPageLimit(3);

        tlMainTabLayout.setupWithViewPager(vpMainContainer);

        //Determine indicator width at runtime
        tlMainTabLayout.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = tlMainTabLayout.getWidth() / tlMainTabLayout.getTabCount();

                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                mIndicator.setLayoutParams(indicatorParams);
            }
        });

        tlMainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
//                changeTabTitle(position);
                vpMainContainer.setCurrentItem(position);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpMainContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
//                if (position == 0)
//                    myMoviesFragment.startanimation();
//                if (position == 1)
//                    mySeriesFragment.startanimation();
                vpMainContainer.reMeasureCurrentPage(vpMainContainer.getCurrentItem());

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), show);
        bundle.putString("Device", "TV");
        if (overviewFragment == null)
            overviewFragment = OverviewFragment.newInstance(bundle);
        if (episodesFragment == null)
            episodesFragment = EpisodesFragment.newInstance(bundle);
        if (reviewsFragment == null)
            reviewsFragment = ReviewsFragment.newInstance(bundle);

        adapter.addFragment(overviewFragment, getActivity().getString(R.string.cast));
        adapter.addFragment(episodesFragment, getActivity().getString(R.string.episodes));
        adapter.addFragment(reviewsFragment, getActivity().getString(R.string.reviews));
        viewPager.setAdapter(adapter);

    }
}

package com.gnusl.actine.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.activity.WatchActivity;
import com.gnusl.actine.ui.adapter.CommentsAdapter;
import com.gnusl.actine.ui.adapter.HomeMovieListAdapter;
import com.gnusl.actine.ui.adapter.ViewPagerAdapter;
import com.gnusl.actine.ui.custom.CustomAppBarWithBack;
import com.gnusl.actine.ui.custom.NonScrollHomeViewPager1;
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


public class ShowDetailsFragment extends Fragment implements HomeMovieClick, View.OnClickListener, ConnectionDelegate, DownloadDelegate, CommentLongClickEvent {

    View inflatedView;

    private RecyclerView rvSuggest;
    private CustomAppBarWithBack cubHomeWithBack;
    private Button btnReactions, btnDownload, btnShare, btnAddToMyList;
    private View clMoreLikeThis, clReactions, clInputLayout;
    private RecyclerView rvComments;
    private TextView tvCategory, tvWatchTime, tvYear, tvShowTitle, tvShowCaption, tvCommentsCount, tvLikesCount, tvViewsCount, tvIMDBRate, tvTomatoRate;
    private ImageView ivShowImage, ivShowCover, ivPlayShow, ivSendComment, ivAddComment, ivBack, ivClock;
    private EditText etCommentText;

    private CommentsAdapter commentsAdapter;
    private HomeMovieListAdapter homeMovieListAdapter;
    private Show show;
    private Toast downloadingToast;

    private TabLayout tlMainTabLayout;
    private NonScrollHomeViewPager1 vpMainContainer;
    private ViewPagerAdapter adapter;
    private TrailerFragment trailerFragment;
    private OverviewFragment overviewFragment;
    private EpisodesFragment episodesFragment;
    String imageTransitionName;
    ArrayList<Cast> cast;
    View mIndicator;
    private int indicatorWidth;

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
            this.imageTransitionName = getArguments().getString("transition");

        }
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));

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
        if (show.getIsMovie()) {
            setupViewPagerMovies(vpMainContainer);

        } else {
            setupViewPagerSeries(vpMainContainer);
            ivPlayShow.setVisibility(View.GONE);
            tvWatchTime.setVisibility(View.GONE);
            ivClock.setVisibility(View.GONE);
            btnDownload.setVisibility(View.GONE);
        }

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
                vpMainContainer.reMeasureCurrentPage(vpMainContainer.getCurrentItem());
                Fragment fragment = (Fragment) adapter.instantiateItem(vpMainContainer, position);
                if (fragment instanceof TrailerFragment)
                    ((TrailerFragment) fragment).startAnimation();
                else if (fragment instanceof OverviewFragment)
                    ((OverviewFragment) fragment).startAnimation();
                else if (fragment instanceof ReviewsFragment)
                    ((ReviewsFragment) fragment).startAnimation();
                else if (fragment instanceof EpisodesFragment)
                    ((EpisodesFragment) fragment).startAnimation();

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tvShowTitle.setText(show.getTitle());
        tvYear.setText(String.valueOf(show.getYear()));
        tvWatchTime.setText(show.getWatchTime());
        tvShowCaption.setText(show.getDescription());
        tvIMDBRate.setText(show.getImdbRate());
        tvTomatoRate.setText(show.getRottenTomatoes());
        tvCategory.setText(show.getCategory());
        Picasso.with(getActivity()).load(show.getCoverImageUrl()).into(ivShowCover);
        Picasso.with(getActivity()).load(show.getThumbnailImageUrl()).into(ivShowImage);
        if (show.getIsFavourite()) {
            btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_filled_heart), null, null, null);
        }

        btnAddToMyList.setOnClickListener(this);

        getRelatedShows();

        btnReactions.setOnClickListener(this);

        homeMovieListAdapter = new HomeMovieListAdapter(getActivity(), new ArrayList<>(), this);

//        GridLayoutManager gridLayoutManager;
//        if ((getResources().getConfiguration().screenLayout &
//                Configuration.SCREENLAYOUT_SIZE_MASK) ==
//                Configuration.SCREENLAYOUT_SIZE_LARGE) {
//            // on a large screen device ...
//            gridLayoutManager = new GridLayoutManager(getActivity(), 4);
//        } else if ((getResources().getConfiguration().screenLayout &
//                Configuration.SCREENLAYOUT_SIZE_MASK) ==
//                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
//            // on a large screen device ...
//            gridLayoutManager = new GridLayoutManager(getActivity(), 4);
//        } else {
//            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
//        }
//        rvSuggest.setLayoutManager(gridLayoutManager);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rvSuggest.setLayoutManager(linearLayoutManager);
        rvSuggest.setAdapter(homeMovieListAdapter);


        commentsAdapter = new CommentsAdapter(getActivity(), new ArrayList<Comment>(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rvComments.setLayoutManager(layoutManager);

        rvComments.setAdapter(commentsAdapter);


        File internalStorage = getActivity().getFilesDir();
        File file = new File(internalStorage, show.getTitle() + ".mp4");
        if (file.exists()) {
            show.setInStorage(true);
            show.setIsDownloaded(true);
        } else {
            show.setInStorage(false);
            show.setIsDownloaded(false);
        }

        if (show.getIsDownloaded()) {
            btnDownload.setText(getActivity().getString(R.string.downloaded));
        }

        if (show.getIsMovie())
            btnDownload.setEnabled(true);
        else
            btnDownload.setEnabled(false);

        String url = "";
        if (show.getIsMovie()) {
            url = Urls.Movie.getLink();
        } else {
            url = Urls.Series.getLink();
        }
        DataLoader.getRequest(url + show.getId(), this);

        Utils.setOnFocusScale(btnAddToMyList);
        Utils.setOnFocusScale(btnDownload);
        Utils.setOnFocusScale(btnReactions);
        Utils.setOnFocusScale(ivAddComment);
        Utils.setOnFocusScale(ivSendComment);
        Utils.setOnFocusScale(ivPlayShow);
        Utils.setOnFocusScale(btnShare);
        ivPlayShow.requestFocus();

    }

    private void getRelatedShows() {

        if (show.getIsMovie())
            DataLoader.getRequest(Urls.MovieSuggest.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
        else
            DataLoader.getRequest(Urls.SerieSuggest.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
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
        ivShowCover = inflatedView.findViewById(R.id.iv_movie_cover);
        ivShowImage = inflatedView.findViewById(R.id.iv_movie_image);
        ivPlayShow = inflatedView.findViewById(R.id.iv_play_show);
        ivClock = inflatedView.findViewById(R.id.iv_clock);
        btnAddToMyList = inflatedView.findViewById(R.id.btn_add_to_my_list);
        btnDownload = inflatedView.findViewById(R.id.btn_download);
        btnShare = inflatedView.findViewById(R.id.btn_share);
        tvCategory = inflatedView.findViewById(R.id.tv_category);
        tvCommentsCount = inflatedView.findViewById(R.id.tv_comments_count);
        tvLikesCount = inflatedView.findViewById(R.id.tv_likes_count);
        tvViewsCount = inflatedView.findViewById(R.id.tv_views_count);
        tvIMDBRate = inflatedView.findViewById(R.id.tv_imdb_rate);
        tvTomatoRate = inflatedView.findViewById(R.id.tv_tomato_rate);
        ivShowImage.setTransitionName(imageTransitionName);
        mIndicator = inflatedView.findViewById(R.id.indicator);

//        tvRate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity()
//                        , R.style.CustomPopupTheme);
//                PopupMenu menu = new PopupMenu(ctw, v);
//                MenuItem sub1 = menu.getMenu().add("IMDB: " + show.getImdbRate());
//                MenuItem sub = menu.getMenu().add("Rotten Tomatoes :" + show.getRottenTomatoes());
//                menu.show();
//            }
//        });

        clInputLayout = inflatedView.findViewById(R.id.cl_input_layout);
        ivSendComment = inflatedView.findViewById(R.id.iv_send_comment);
        ivAddComment = inflatedView.findViewById(R.id.iv_add_comment);
        etCommentText = inflatedView.findViewById(R.id.et_comment_text);
        ivBack = inflatedView.findViewById(R.id.iv_back1);

        tlMainTabLayout = inflatedView.findViewById(R.id.tl_main_tab_layout);
        vpMainContainer = inflatedView.findViewById(R.id.vp_main_container);

        ivPlayShow.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
        tvLikesCount.setOnClickListener(this);
        ivSendComment.setOnClickListener(this);
        ivAddComment.setOnClickListener(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


    }

    @Override
    public void onClickMovie(Show movie, ImageView ivThumbnail) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), movie);
            bundle.putString("transition", ViewCompat.getTransitionName(ivThumbnail));
            if (fragment instanceof HomeContainerFragment) {
                ((HomeContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail, null, null);
            } else if (fragment instanceof MoreContainerFragment) {
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail);
            } else if (fragment instanceof SearchContainerFragment) {
                ((SearchContainerFragment) fragment).replaceFragment(FragmentTags.ShowDetailsFragment, bundle, ivThumbnail);
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
            case R.id.btn_reactions: {
                if (clMoreLikeThis.getVisibility() == View.VISIBLE) {
                    clMoreLikeThis.setVisibility(View.GONE);
                    clReactions.setVisibility(View.VISIBLE);
                    sendGetCommentsRequest();
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
            case R.id.tv_likes_count: {
                String url = "";
                if (show.getIsMovie()) {
                    url = Urls.MovieLike.getLink();
                } else if (show.getIsEpisode()) {
                    url = Urls.EpisodeLike.getLink();
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
            case R.id.btn_download: {
                if (!show.getIsDownloaded()) {
                    File internalStorage = getActivity().getFilesDir();
                    String url = show.getDownloadVideoUrl();
//                    if (url.contains("_.m3u8")) {
//                        url = url.replaceAll("_.m3u8", ".mp4");
//                    } else {
//                        url = url.replaceAll(".m3u8", ".mp4");
//                    }
                    Toast.makeText(getActivity(), R.string.downloading1, Toast.LENGTH_SHORT).show();
                    DataLoader.downloadRequest(getActivity(), show.getId(), url, internalStorage.getAbsolutePath(), show.getTitle() + ".mp4", this);
                } else {
                    DataLoader.postRequest(Urls.MovieDownload.getLink().replaceAll("%id%", String.valueOf(show.getId())), new ConnectionDelegate() {
                        @Override
                        public void onConnectionError(int code, String message) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onConnectionError(ANError anError) {
//                            Toast.makeText(getActivity(), anError.getErrorBody(), Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onConnectionSuccess(JSONObject jsonObject) {
                            btnDownload.setText(getActivity().getString(R.string.download));
                            show.setIsDownloaded(false);

                            Box<DBShow> dbShowBox = ObjectBox.get().boxFor(DBShow.class);
                            DBShow dbShowInBox = dbShowBox.get(show.getId());
                            if (dbShowInBox != null) {
                                dbShowBox.remove(show.getId());
                            }
                        }
                    });
                }
                break;
            }
            case R.id.iv_add_comment: {
                if (clInputLayout.getVisibility() == View.VISIBLE)
                    clInputLayout.setVisibility(View.GONE);
                else
                    clInputLayout.setVisibility(View.VISIBLE);
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
                } else if (show.getIsEpisode()) {
                    url = Urls.EpisodeComments.getLink();
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
                            clInputLayout.setVisibility(View.GONE);
                            String url = "";
                            if (show.getIsMovie()) {
                                url = Urls.MovieComments.getLink();
                            } else if (show.getIsEpisode()) {
                                url = Urls.EpisodeComments.getLink();
                            }
                            DataLoader.getRequest(url.replaceAll("%id%", String.valueOf(show.getId())), ShowDetailsFragment.this);
                        }
                    }
                });
                break;
            }
        }
    }

    private void sendGetCommentsRequest() {

        if (show.getIsMovie()) {
            DataLoader.getRequest(Urls.MovieComments.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
        } else if (show.getIsEpisode()) {
            DataLoader.getRequest(Urls.EpisodeComments.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
        }
    }

    private void sendDetailsRequest() {


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
        } else if (jsonObject.has("series")) {
            if (jsonObject.optJSONArray("series") != null) {
                List<Show> series = Show.newList(jsonObject.optJSONArray("series"), false, false, false);
                homeMovieListAdapter.setList(series);
            } else if (jsonObject.optJSONObject("series") != null && jsonObject.optJSONObject("series").has("seasons")) {
                show = Show.newInstance(jsonObject.optJSONObject("series"), show.getIsMovie(), show.getIsSeason(), show.getIsEpisode());
                if (jsonObject.optJSONObject("series").has("crew")) {
                    cast = (ArrayList<Cast>) Cast.newArray(jsonObject.optJSONObject("series").optJSONArray("crew"));
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
            tvViewsCount.setText(String.format(Locale.getDefault(), "%d views", jsonObject.optInt("visited")));
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

            show.setIsFavourite(jsonObject.optBoolean("is_favourite"));
            if (show.getIsFavourite()) {
                if (getActivity() != null)
                    btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_filled_heart), null, null, null);
            } else {
                if (getActivity() != null)
                    btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_empty_heart), null, null, null);
            }
        }

        if (jsonObject.has("crew")) {
            cast = (ArrayList<Cast>) Cast.newArray(jsonObject.optJSONArray("crew"));
            overviewFragment.setCastList(cast);
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
                    btnDownload.setText(getActivity().getString(R.string.downloaded));
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
                    } else if (show.getIsEpisode()) {
                        url = Urls.EpisodeCommentDelete.getLink();
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
                            } else if (show.getIsEpisode()) {
                                url = Urls.EpisodeComments.getLink();
                            }
                            DataLoader.getRequest(url.replaceAll("%id%", String.valueOf(show.getId())), ShowDetailsFragment.this);
                        }
                    });
                    dialog.dismiss();
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getActivity().getString(R.string.cancel), (dialog, which) -> dialog.dismiss());

        alertDialog.show();
    }

    private void setupViewPagerMovies(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), show);
//        bundle.putParcelableArrayList("crew", cast);

        if (trailerFragment == null) {
            trailerFragment = TrailerFragment.newInstance(bundle);
        }
        if (overviewFragment == null) {
            overviewFragment = OverviewFragment.newInstance(bundle);
        }
        adapter.addFragment(trailerFragment, getActivity().getString(R.string.trailer));
        adapter.addFragment(overviewFragment, getActivity().getString(R.string.overview));
        adapter.addFragment(ReviewsFragment.newInstance(bundle), getActivity().getString(R.string.reviews));

        viewPager.setAdapter(adapter);

    }

    private void setupViewPagerSeries(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.HomeDetailsExtra.getConst(), show);
//        bundle.putParcelableArrayList("crew", cast);

        if (episodesFragment == null) {
            episodesFragment = EpisodesFragment.newInstance(bundle);
        }
        if (overviewFragment == null) {
            overviewFragment = OverviewFragment.newInstance(bundle);
        }

        adapter.addFragment(overviewFragment, getActivity().getString(R.string.overview));
        adapter.addFragment(episodesFragment, getActivity().getString(R.string.episodes));
        adapter.addFragment(ReviewsFragment.newInstance(bundle), getActivity().getString(R.string.reviews));

        viewPager.setAdapter(adapter);

    }
}

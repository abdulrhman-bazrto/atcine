package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.model.Category;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.WatchActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final RecyclerView.RecycledViewPool recycledViewPool;
    private final HomeMovieClick homeMovieClick;
    private final GenresClickEvents genresClickEvents;
    private Context mContext;
    private Show trendShow;
    private List<String> categoriesName = new ArrayList<>();
    private List<Integer> categoriesIds = new ArrayList<>();
    private HashMap<String, List<Show>> showsByCategories = new HashMap<>();

    private static int HOLDER_MOVIE = 0;
    private static int HOLDER_MOVIE_LIST = 1;


    public HomeAdapter(Context context, HomeMovieClick homeMovieClick, GenresClickEvents genresClickEvents) {
        this.mContext = context;
        this.homeMovieClick = homeMovieClick;
        this.genresClickEvents = genresClickEvents;
        this.recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == HOLDER_MOVIE) {
            view = inflater.inflate(R.layout.item_home_movie, parent, false);
            return new HomeAdapter.MovieViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_home_movie_list, parent, false);
            return new HomeAdapter.MovieListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MovieViewHolder)
            ((MovieViewHolder) holder).bind();
        else if (holder instanceof MovieListViewHolder) {
            String name = categoriesName.get(holder.getAdapterPosition() - 1);
            int id = categoriesIds.get(holder.getAdapterPosition() - 1);
            ((MovieListViewHolder) holder).bind(id,name, showsByCategories.get(name));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HOLDER_MOVIE;
        } else {
            return HOLDER_MOVIE_LIST;
        }
    }

    @Override
    public int getItemCount() {
        if (showsByCategories.size() == 0)
            return 0;
        return showsByCategories.size() + 1;
    }

    public void setData(Show trendMovie, List<String> categoriesName,List<Integer> categoriesIds, HashMap<String, List<Show>> moviesByCategories) {
        this.trendShow = trendMovie;
        this.categoriesName = categoriesName;
        this.categoriesIds = categoriesIds;
        this.showsByCategories = moviesByCategories;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMovieImage;
        Button btnInfo, btnAddToMyList, btnPlay;
        TextView tvTrendName;

        MovieViewHolder(View itemView) {
            super(itemView);
            ivMovieImage = itemView.findViewById(R.id.iv_movie_image);
            btnPlay = itemView.findViewById(R.id.btn_play);
            btnAddToMyList = itemView.findViewById(R.id.btn_add_to_my_list);
            btnInfo = itemView.findViewById(R.id.btn_info);
            tvTrendName = itemView.findViewById(R.id.tv_trend_name);
        }

        public void bind() {

            Picasso.with(mContext).load(trendShow.getCoverImageUrl()).into(ivMovieImage);

            if (!(trendShow.getIsMovie() || trendShow.getIsEpisode())) {
                btnPlay.setVisibility(View.GONE);
                btnAddToMyList.setVisibility(View.GONE);
            } else {
                btnPlay.setVisibility(View.VISIBLE);
                btnAddToMyList.setVisibility(View.VISIBLE);
            }

            tvTrendName.setText(trendShow.getTitle());

            btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WatchActivity.class);
                    intent.putExtra("show", trendShow);
                    mContext.startActivity(intent);
                }
            });

            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeMovieClick != null) {
                        if (trendShow.getIsMovie())
                            homeMovieClick.onClickMovie(trendShow);
                        else
                            homeMovieClick.onClickSeries(trendShow);
                    }

                }
            });

            if (trendShow.getIsFavourite()) {
                btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_check_white), null, null, null);
            }

            btnAddToMyList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataLoader.postRequest(Urls.MovieFavorite.getLink().replaceAll("%id%", String.valueOf(trendShow.getId())), new ConnectionDelegate() {
                        @Override
                        public void onConnectionError(int code, String message) {

                        }

                        @Override
                        public void onConnectionError(ANError anError) {

                        }

                        @Override
                        public void onConnectionSuccess(JSONObject jsonObject) {
                            if (jsonObject.optString("status").equalsIgnoreCase("added")) {
                                trendShow.setIsFavourite(true);
                                btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_check_white), null, null, null);
                            } else {
                                trendShow.setIsFavourite(false);
                                btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_mylist), null, null, null);
                            }
                        }
                    });
                }
            });

        }
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        TextView tvListTitle,tvMore;
        RecyclerView rvMovieList;

        MovieListViewHolder(View itemView) {
            super(itemView);

            tvListTitle = itemView.findViewById(R.id.tv_list_title);
            tvMore = itemView.findViewById(R.id.tv_more);
            rvMovieList = itemView.findViewById(R.id.rv_movie_list);

        }

        public void bind(int id,String name, List<Show> movies) {

            tvListTitle.setText(name);
            if (name.equalsIgnoreCase("random") || name.equalsIgnoreCase("favourite")  || name.equalsIgnoreCase("not completed") ){
                tvMore.setVisibility(View.GONE);
                tvMore.setOnClickListener(null);
            }else {
                tvMore.setVisibility(View.VISIBLE);
                tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if (genresClickEvents != null) {
                           Category category = new Category();
                           category.setId(id);
                           genresClickEvents.onSelectGenres(category);
                       }
                    }
                });
            }

            rvMovieList.setRecycledViewPool(recycledViewPool);

            HomeMovieListAdapter homeMovieListAdapter = new HomeMovieListAdapter(mContext, movies, homeMovieClick);

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

            rvMovieList.setLayoutManager(layoutManager);

            rvMovieList.swapAdapter(homeMovieListAdapter, false);

        }
    }

    public Show getTrendShow() {
        return trendShow;
    }

    public void setTrendShow(Show trendShow) {
        this.trendShow = trendShow;
        notifyItemChanged(0);
    }
}

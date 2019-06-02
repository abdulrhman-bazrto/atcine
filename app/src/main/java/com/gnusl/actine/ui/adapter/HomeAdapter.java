package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.model.Movie;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final RecyclerView.RecycledViewPool recycledViewPool;
    private final HomeMovieClick homeMovieClick;
    private Context mContext;
    private Movie trendMovie;
    private List<String> categoriesName = new ArrayList<>();
    private HashMap<String, List<Movie>> moviesByCategories = new HashMap<>();

    private static int HOLDER_MOVIE = 0;
    private static int HOLDER_MOVIE_LIST = 1;


    public HomeAdapter(Context context, HomeMovieClick homeMovieClick) {
        this.mContext = context;
        this.homeMovieClick = homeMovieClick;
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
            ((MovieListViewHolder) holder).bind(name, moviesByCategories.get(name));
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
        if (moviesByCategories.size() == 0)
            return 0;
        return moviesByCategories.size() + 1;
    }

    public void setData(Movie trendMovie, List<String> categoriesName, HashMap<String, List<Movie>> moviesByCategories) {
        this.trendMovie = trendMovie;
        this.categoriesName = categoriesName;
        this.moviesByCategories = moviesByCategories;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMovieImage;
        Button btnInfo, btnAddToMyList, btnPlay;

        MovieViewHolder(View itemView) {
            super(itemView);
            ivMovieImage = itemView.findViewById(R.id.iv_movie_image);
            btnPlay = itemView.findViewById(R.id.btn_play);
            btnAddToMyList = itemView.findViewById(R.id.btn_add_to_my_list);
            btnInfo = itemView.findViewById(R.id.btn_info);
        }

        public void bind() {

            Picasso.with(mContext).load(trendMovie.getCoverImageUrl()).into(ivMovieImage);

            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeMovieClick != null)
                        homeMovieClick.onClickMovie(trendMovie);
                }
            });

            if (trendMovie.getIsFavourite()) {
                btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_check_white), null, null, null);
            }

            btnAddToMyList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataLoader.postRequest(Urls.MovieFavorite.getLink().replaceAll("%id%", String.valueOf(trendMovie.getId())), new ConnectionDelegate() {
                        @Override
                        public void onConnectionError(int code, String message) {

                        }

                        @Override
                        public void onConnectionError(ANError anError) {

                        }

                        @Override
                        public void onConnectionSuccess(JSONObject jsonObject) {
                            if (jsonObject.optString("status").equalsIgnoreCase("added")) {
                                trendMovie.setIsFavourite(true);
                                btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_check_white), null, null, null);
                            } else {
                                trendMovie.setIsFavourite(false);
                                btnAddToMyList.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_mylist), null, null, null);
                            }
                        }
                    });
                }
            });

        }
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        TextView tvListTitle;
        RecyclerView rvMovieList;

        MovieListViewHolder(View itemView) {
            super(itemView);

            tvListTitle = itemView.findViewById(R.id.tv_list_title);
            rvMovieList = itemView.findViewById(R.id.rv_movie_list);

        }

        public void bind(String name, List<Movie> movies) {

            tvListTitle.setText(name);

            rvMovieList.setRecycledViewPool(recycledViewPool);

            HomeMovieListAdapter homeMovieListAdapter = new HomeMovieListAdapter(mContext, movies, homeMovieClick);

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

            rvMovieList.setLayoutManager(layoutManager);

            rvMovieList.swapAdapter(homeMovieListAdapter, false);

        }
    }
}

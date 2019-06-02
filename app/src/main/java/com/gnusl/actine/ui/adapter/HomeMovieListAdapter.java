package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeMovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final HomeMovieClick homeMovieClick;
    private Context mContext;
    private List<Movie> movies = new ArrayList<>();


    public HomeMovieListAdapter(Context context, List<Movie> movies, HomeMovieClick homeMovieClick) {
        this.mContext = context;
        this.movies = movies;
        this.homeMovieClick = homeMovieClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_home_list_movie, parent, false);
        return new MovieListViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        ((MovieListViewHolder) holder).bind();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;

        MovieListViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
        }

        public void bind() {

            Picasso.with(mContext).load(movies.get(getAdapterPosition()).getThumbnailImageUrl()).into(ivThumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeMovieClick != null)
                        homeMovieClick.onClickMovie(movies.get(getAdapterPosition()));
                }
            });
        }
    }
}

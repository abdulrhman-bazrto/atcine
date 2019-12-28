package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.interfaces.LoadMoreDelegate;
import com.gnusl.actine.model.Show;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieMoreLikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final HomeMovieClick homeMovieClick;
    private final LoadMoreDelegate loadMoreDelegate;
    private Context mContext;
    private List<Show> movies = new ArrayList<>();


    public MovieMoreLikeAdapter(Context context, HomeMovieClick homeMovieClick, LoadMoreDelegate loadMoreDelegate) {
        this.mContext = context;
        this.homeMovieClick = homeMovieClick;
        this.loadMoreDelegate = loadMoreDelegate;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_more_list_movie, parent, false);
        return new MovieListViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        ((MovieListViewHolder) holder).bind();

        if (position == getItemCount() - 3)
            if (loadMoreDelegate != null)
                loadMoreDelegate.loadMore(getItemCount());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setList(List<Show> movies) {
        if (movies.size() == 0)
            return;
        if (getItemCount() == 0) {
            this.movies = movies;
            notifyDataSetChanged();
        } else {
//            int pos = this.movies.size() - 1;
            this.movies.addAll(movies);
//            notifyItemInserted(pos);
            notifyDataSetChanged();
        }
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        ProgressBar pbLoading;

        MovieListViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            pbLoading = itemView.findViewById(R.id.pb_loading);
        }

        public void bind() {

            Picasso.with(mContext).load(movies.get(getAdapterPosition()).getThumbnailImageUrl()).into(ivThumbnail, new Callback() {
                @Override
                public void onSuccess() {
                    pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    pbLoading.setVisibility(View.GONE);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Show show = movies.get(getAdapterPosition());
                    if (homeMovieClick != null)
                        if (show.getIsMovie()) {
                            homeMovieClick.onClickMovie(show);
                        } else if (!show.getIsMovie()) {
                            if (show.getIsEpisode()) {
                                homeMovieClick.onClickMovie(show);
                            } else {
                                homeMovieClick.onClickSeries(show);
                            }
                        }
                }
            });
        }
    }
}

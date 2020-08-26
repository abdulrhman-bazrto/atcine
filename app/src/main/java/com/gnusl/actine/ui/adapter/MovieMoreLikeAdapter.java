package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.interfaces.LoadMoreDelegate;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.ui.custom.GifImageView;
import com.gnusl.actine.util.Utils;
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

    public void clearList() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        GifImageView pbLoading;
        TextView tvTitle, tvImdb, tvTomato;
        private View iv_tomato;

        MovieListViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            pbLoading = itemView.findViewById(R.id.pb_loading);
            tvTitle = itemView.findViewById(R.id.tv_show_name);
            tvImdb = itemView.findViewById(R.id.tv_imdb_rate);
            tvTomato = itemView.findViewById(R.id.tv_tomato_rate);
            iv_tomato = itemView.findViewById(R.id.iv_tomato);
            pbLoading.setGifImageResource(R.drawable.loader);
            Utils.setOnFocusScale(itemView);
        }

        public void bind() {

            final Show show = movies.get(getAdapterPosition());
            Picasso.with(mContext).load(show.getThumbnailImageUrl()).into(ivThumbnail, new Callback() {
                @Override
                public void onSuccess() {
                    pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    pbLoading.setVisibility(View.GONE);
                }
            });
            tvTitle.setText(show.getTitle());
            tvImdb.setText(show.getImdbRate().toString());
            if (!show.getRottenTomatoes().isEmpty()) {
                tvTomato.setText(show.getRottenTomatoes());
            }else {
                tvTomato.setVisibility(View.GONE);
                iv_tomato.setVisibility(View.GONE);
            }
            ViewCompat.setTransitionName(ivThumbnail, "transition" + show.getId());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeMovieClick != null)
//                        if (show.getIsMovie()) {
                        homeMovieClick.onClickMovie(show, ivThumbnail);
//                        } else if (!show.getIsMovie()) {
//                            if (show.getIsEpisode()) {
//                                homeMovieClick.onClickMovie(show, null);
//                            } else {
//                                homeMovieClick.onClickSeries(show);
//                            }
//                        }
                }
            });
        }
    }
}

package com.gnusl.actine.ui.Mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.interfaces.LoadMoreDelegate;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.ui.Mobile.custom.GifImageView;
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
    private String type = "Mobile";


    public MovieMoreLikeAdapter(Context context, HomeMovieClick homeMovieClick, LoadMoreDelegate loadMoreDelegate, String type) {
        this.mContext = context;
        this.homeMovieClick = homeMovieClick;
        this.loadMoreDelegate = loadMoreDelegate;
        this.type = type;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (type.equalsIgnoreCase("TV"))
            view = inflater.inflate(R.layout.item_tv_more_list_movie, parent, false);
        else
            view = inflater.inflate(R.layout.item_more_list_movie, parent, false);
        return new MovieListViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        ((MovieListViewHolder) holder).bind(position);

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
        private View iv_tomato,iv_imdb;

        MovieListViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            pbLoading = itemView.findViewById(R.id.pb_loading);
            tvTitle = itemView.findViewById(R.id.tv_show_name);
            tvImdb = itemView.findViewById(R.id.tv_imdb_rate);
            tvTomato = itemView.findViewById(R.id.tv_tomato_rate);
            iv_tomato = itemView.findViewById(R.id.iv_tomato);
            iv_imdb = itemView.findViewById(R.id.iv_imdb);
            pbLoading.setGifImageResource(R.drawable.loader);
            Utils.setOnFocusScale(itemView);
        }

        public void bind(int position) {

            final Show show = movies.get(position);
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
            if (!show.getImdbRate().isEmpty()) {
                tvImdb.setText(show.getImdbRate());
            }else {
                tvImdb.setVisibility(View.GONE);
                iv_imdb.setVisibility(View.GONE);
            }
            if (!show.getRottenTomatoes().isEmpty()) {
                tvTomato.setText(show.getRottenTomatoes());
            } else {
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

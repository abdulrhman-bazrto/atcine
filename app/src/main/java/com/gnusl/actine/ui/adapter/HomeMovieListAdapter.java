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
import com.gnusl.actine.model.Show;
import com.gnusl.actine.ui.custom.GifImageView;
import com.gnusl.actine.util.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeMovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final HomeMovieClick homeMovieClick;
    private Context mContext;
    private List<Show> movies = new ArrayList<>();


    public HomeMovieListAdapter(Context context, List<Show> movies, HomeMovieClick homeMovieClick) {
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
//        if (position > movies.size())
//            return;
//        else {
        ((MovieListViewHolder) holder).bind(movies.get(position));
//        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setList(List<Show> movies) {
        this.movies = movies;
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
            pbLoading.setGifImageResource(R.drawable.loader);
            tvTitle = itemView.findViewById(R.id.tv_show_name);
            tvImdb = itemView.findViewById(R.id.tv_imdb_rate);
            tvTomato = itemView.findViewById(R.id.tv_tomato_rate);
            iv_tomato = itemView.findViewById(R.id.iv_tomato);
            Utils.setOnFocusScale(itemView);
        }

        public void bind(Show movie) {

            Picasso.with(mContext).load(movie.getThumbnailImageUrl()).into(ivThumbnail, new Callback() {
                @Override
                public void onSuccess() {
                    pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    pbLoading.setVisibility(View.GONE);
                }
            });
            tvImdb.setText(movie.getImdbRate().toString());
            if (!movie.getRottenTomatoes().isEmpty()) {
                tvTomato.setText(movie.getRottenTomatoes());
            }else {
                tvTomato.setVisibility(View.GONE);
                iv_tomato.setVisibility(View.GONE);
            }
            ViewCompat.setTransitionName(ivThumbnail,"transition" + movie.getId());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeMovieClick != null) {
//                        if (movie.getIsMovie())
                            homeMovieClick.onClickMovie(movie,ivThumbnail);
//                        else
//                            homeMovieClick.onClickSeries(movie);
                    }

                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return movies.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}

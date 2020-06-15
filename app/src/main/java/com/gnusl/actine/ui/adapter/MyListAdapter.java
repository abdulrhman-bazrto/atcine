package com.gnusl.actine.ui.adapter;

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
import com.gnusl.actine.model.Show;
import com.gnusl.actine.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final HomeMovieClick homeMovieClick;
    private Context mContext;
    private List<Show> movies = new ArrayList<>();


    public MyListAdapter(Context context, HomeMovieClick homeMovieClick) {
        this.mContext = context;
        this.homeMovieClick = homeMovieClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_series, parent, false);
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

    public void setList(List<Show> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        TextView tvTitle, tvImdb, tvTomato;

        MovieListViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvImdb = itemView.findViewById(R.id.tv_imdb_rate);
            tvTomato = itemView.findViewById(R.id.tv_tomato_rate);
            Utils.setOnFocusScale(itemView);
        }

        public void bind() {

            final Show show = movies.get(getAdapterPosition());
            Picasso.with(mContext).load(show.getThumbnailImageUrl()).into(ivThumbnail);
            ViewCompat.setTransitionName(ivThumbnail,"transition" + show.getId());

            tvTitle.setText(show.getTitle());
            tvImdb.setText(show.getImdbRate().toString());
            tvTomato.setText(show.getRottenTomatoes());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeMovieClick != null)
//                        if (show.getIsMovie()) {
                            homeMovieClick.onClickMovie(show, ivThumbnail);
//                        } else if (!show.getIsMovie()) {
//                            if (show.getIsEpisode()) {
//                                homeMovieClick.onClickMovie(show, ivThumbnail);
//                            } else {
//                                homeMovieClick.onClickSeries(show);
//                            }
//                        }
                }
            });
        }
    }
}

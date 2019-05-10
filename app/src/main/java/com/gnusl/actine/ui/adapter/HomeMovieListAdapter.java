package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.HomeMovieClick;

import java.util.ArrayList;
import java.util.List;

public class HomeMovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final HomeMovieClick homeMovieClick;
    private Context mContext;
    private List<String> moviesList = new ArrayList<>();


    public HomeMovieListAdapter(Context context, HomeMovieClick homeMovieClick) {
        this.mContext = context;
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
        return 6;
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        MovieListViewHolder(View itemView) {
            super(itemView);
        }

        public void bind() {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeMovieClick != null)
                        homeMovieClick.onClickMovie();
                }
            });
        }
    }
}

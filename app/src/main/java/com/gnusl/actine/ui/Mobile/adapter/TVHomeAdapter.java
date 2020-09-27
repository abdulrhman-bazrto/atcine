package com.gnusl.actine.ui.Mobile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.interfaces.HomeMovieClick;
import com.gnusl.actine.interfaces.LoadMoreCategoriesDelegate;
import com.gnusl.actine.model.Category;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class TVHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final RecyclerView.RecycledViewPool recycledViewPool;
    private final RecyclerView homeRecycler;

    private final HomeMovieClick homeMovieClick;
    private final GenresClickEvents genresClickEvents;
    private final LoadMoreCategoriesDelegate loadMoreCategoriesDelegate;

    private Context mContext;
    private Show trendShow;
    private List<Category> categories = new ArrayList<>();

    private static int HOLDER_MOVIE = 0;
    private static int HOLDER_MOVIE_LIST = 1;


    public TVHomeAdapter(Context context, RecyclerView homeRecycler, HomeMovieClick homeMovieClick, GenresClickEvents genresClickEvents, LoadMoreCategoriesDelegate loadMoreCategoriesDelegate) {
        this.mContext = context;
        this.homeMovieClick = homeMovieClick;
        this.homeRecycler = homeRecycler;
        this.genresClickEvents = genresClickEvents;
        this.loadMoreCategoriesDelegate = loadMoreCategoriesDelegate;
        this.recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_home_movie_list, parent, false);
        return new TVHomeAdapter.MovieListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieListViewHolder) {
            Category category = categories.get(position);
            String name = category.getTitle();
            int id = category.getId();
            ((MovieListViewHolder) holder).bind(id, name, category.getShows());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return HOLDER_MOVIE_LIST;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setData(Show trendMovie, List<Category> categories) {
        this.trendShow = trendMovie;
        this.categories = categories;
        notifyDataSetChanged();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        TextView tvListTitle, tvMore;
        RecyclerView rvMovieList;

        MovieListViewHolder(View itemView) {
            super(itemView);

            tvListTitle = itemView.findViewById(R.id.tv_list_title);
            tvMore = itemView.findViewById(R.id.tv_more);
            rvMovieList = itemView.findViewById(R.id.rv_movie_list);

            Utils.setOnFocusScale(tvMore);
        }

        public void bind(int id, String name, List<Show> movies) {

            tvListTitle.setText(name);
            tvListTitle.setTransitionName("transition" + id + name);
            rvMovieList.setTransitionName("transition_rv" + id + name);
            Log.d("CATEGORY_id", String.valueOf(id));
            Log.d("CATEGORY_NAME", name);
            if (name.equalsIgnoreCase("random") || name.equalsIgnoreCase("favourite") || name.equalsIgnoreCase("not completed")) {
                tvMore.setVisibility(View.GONE);
            } else {
                tvMore.setVisibility(View.VISIBLE);
                tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (genresClickEvents != null) {
                            Category category = new Category();
                            category.setId(id);
                            category.setTitle(name);
                            genresClickEvents.onSelectGenres(category, tvListTitle, rvMovieList);
                        }
                    }
                });
            }

            rvMovieList.setRecycledViewPool(recycledViewPool);

            TVHomeMovieListAdapter homeMovieListAdapter = new TVHomeMovieListAdapter(mContext, movies, homeMovieClick);

            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

            rvMovieList.setLayoutManager(layoutManager);

            rvMovieList.swapAdapter(homeMovieListAdapter, false);
            rvMovieList.scheduleLayoutAnimation();

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

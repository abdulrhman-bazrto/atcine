package com.gnusl.actine.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.model.Category;

import java.util.ArrayList;
import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenresViewHolder> {

    private GenresClickEvents genresClickEvents;
    private Context mContext;
    private List<Category> genres = new ArrayList<>();


    public GenresAdapter(Context context, List<Category> genres, GenresClickEvents genresClickEvents) {
        this.mContext = context;
        this.genres = genres;
        this.genresClickEvents = genresClickEvents;
    }

    @NonNull
    @Override
    public GenresAdapter.GenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_genres, parent, false);
        return new GenresViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final GenresAdapter.GenresViewHolder holder, int position) {
        holder.tvGenresName.setText(genres.get(holder.getAdapterPosition()).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (genresClickEvents != null)
                    genresClickEvents.onSelectGenres(genres.get(holder.getAdapterPosition()));
            }
        });
    }

    public void setClickListener(GenresClickEvents genresClickEvents) {
        this.genresClickEvents = genresClickEvents;
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public void setList(List<Category> categories) {
        this.genres = categories;
        notifyDataSetChanged();
    }

    class GenresViewHolder extends RecyclerView.ViewHolder {

        TextView tvGenresName;

        GenresViewHolder(View itemView) {
            super(itemView);
            tvGenresName = itemView.findViewById(R.id.tv_genres_name);
        }
    }
}

package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.GenresClickEvents;
import com.gnusl.actine.model.Category;
import com.gnusl.actine.util.Utils;
import com.squareup.picasso.Picasso;

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
        holder.tvGenresName.setTransitionName("transition" +genres.get(holder.getAdapterPosition()).getId());
        holder.tvCount.setText(genres.get(holder.getAdapterPosition()).getCount());
        holder.tvType.setText(genres.get(holder.getAdapterPosition()).getType());
        Picasso.with(mContext).load(genres.get(holder.getAdapterPosition()).getImageURL()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (genresClickEvents != null)
                    genresClickEvents.onSelectGenres(genres.get(holder.getAdapterPosition()), holder.tvGenresName, null);
            }
        });
        switch (position) {
            case 0:
            case 9:
            case 18:
            case 27:
                holder.ivCategoryOverlay.setBackground(mContext.getResources().getDrawable(R.drawable.bg_overlay));
                break;

            case 1:
            case 10:
            case 19:
            case 28:
                holder.ivCategoryOverlay.setBackground(mContext.getResources().getDrawable(R.drawable.bg_overlay1));
                break;

            case 2:
            case 11:
            case 20:
            case 29:
                holder.ivCategoryOverlay.setBackground(mContext.getResources().getDrawable(R.drawable.bg_overlay2));
                break;

            case 3:
            case 12:
            case 21:
            case 30:
                holder.ivCategoryOverlay.setBackground(mContext.getResources().getDrawable(R.drawable.bg_overlay3));
                break;

            case 4:
            case 13:
            case 22:
            case 31:
                holder.ivCategoryOverlay.setBackground(mContext.getResources().getDrawable(R.drawable.bg_overlay4));
                break;

            case 5:
            case 14:
            case 23:
            case 32:
                holder.ivCategoryOverlay.setBackground(mContext.getResources().getDrawable(R.drawable.bg_overlay5));
                break;

            case 6:
            case 15:
            case 24:
            case 33:
                holder.ivCategoryOverlay.setBackground(mContext.getResources().getDrawable(R.drawable.bg_overlay6));
                break;

            case 7:
            case 16:
            case 25:
            case 34:
                holder.ivCategoryOverlay.setBackground(mContext.getResources().getDrawable(R.drawable.bg_overlay7));
                break;

            case 8:
            case 17:
            case 26:
            case 35:
                holder.ivCategoryOverlay.setBackground(mContext.getResources().getDrawable(R.drawable.bg_overlay8));
                break;


        }
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

        TextView tvGenresName,tvCount,tvType;
        View ivCategoryOverlay;
        ImageView imageView;

        GenresViewHolder(View itemView) {
            super(itemView);
            tvGenresName = itemView.findViewById(R.id.tv_genres_name);
            tvCount = itemView.findViewById(R.id.tv_genres_type_count);
            tvType = itemView.findViewById(R.id.tv_genres_type);
            ivCategoryOverlay = itemView.findViewById(R.id.iv_category_overlay);
            imageView = itemView.findViewById(R.id.iv_category_image);

            Utils.setOnFocusScale(itemView);
        }
    }
}

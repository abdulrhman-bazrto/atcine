package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.model.Cast;
import com.gnusl.actine.ui.custom.GifImageView;
import com.gnusl.actine.util.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Cast> cast = new ArrayList<>();


    public CastAdapter(Context context, List<Cast> cast) {
        this.mContext = context;
        this.cast = cast;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_cast, parent, false);
        return new MovieListViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
//        if (position > cast.size())
//            return;
//        else {
        ((MovieListViewHolder) holder).bind(cast.get(position));
//        }
    }

    @Override
    public int getItemCount() {
        return cast.size();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        ImageView ivThumbnail;
        GifImageView pbLoading;
        TextView tvType,tvRealName;
        MovieListViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvRealName = itemView.findViewById(R.id.tv_real_name);
            tvType = itemView.findViewById(R.id.tv_type);
            pbLoading = itemView.findViewById(R.id.pb_loading);
            pbLoading.setGifImageResource(R.drawable.loader);
            Utils.setOnFocusScale(itemView);
        }

        public void bind(Cast cast) {

            tvType.setText(cast.getType());
            tvRealName.setText(cast.getName());
            Picasso.with(mContext).load(cast.getImageURL()).into(ivThumbnail, new Callback() {
                @Override
                public void onSuccess() {
                    pbLoading.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    pbLoading.setVisibility(View.GONE);
                }
            });


        }
    }

//    @Override
//    public long getItemId(int position) {
//        return cast.get(position).getId();
//    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setList(List<Cast> cast) {
        this.cast = cast;
        notifyDataSetChanged();
    }
}

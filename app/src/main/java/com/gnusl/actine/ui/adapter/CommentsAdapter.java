package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.GenresClickEvents;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private Context mContext;
    private List<String> comments = new ArrayList<>();


    public CommentsAdapter(Context context, List<String> comments) {
        this.mContext = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_comment, parent, false);
        return new CommentsViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final CommentsViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {

        TextView tvGenresName;

        CommentsViewHolder(View itemView) {
            super(itemView);
            tvGenresName = itemView.findViewById(R.id.tv_genres_name);
        }
    }
}

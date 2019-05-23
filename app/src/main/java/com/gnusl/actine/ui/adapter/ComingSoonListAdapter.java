package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gnusl.actine.R;

import java.util.ArrayList;
import java.util.List;

public class ComingSoonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> moviesList = new ArrayList<>();


    public ComingSoonListAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_coming_soon, parent, false);
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

        Button btnRemindMe;

        MovieListViewHolder(View itemView) {
            super(itemView);
            btnRemindMe = itemView.findViewById(R.id.btn_remind_me);
        }

        public void bind() {

            btnRemindMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnRemindMe.setBackgroundResource(R.drawable.bg_btn_remind_me);
                    btnRemindMe.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_check_white), null, null, null);
                }
            });
        }
    }
}

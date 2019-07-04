package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.HelpItemClickEvents;
import com.gnusl.actine.model.Help;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.CommentsViewHolder> {

    private final HelpItemClickEvents helpItemClickEvents;
    private Context mContext;
    private List<Help> helpList = new ArrayList<>();


    public HelpAdapter(Context context, List<Help> helpList, HelpItemClickEvents helpItemClickEvents) {
        this.mContext = context;
        this.helpList = helpList;
        this.helpItemClickEvents = helpItemClickEvents;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_help, parent, false);
        return new CommentsViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final CommentsViewHolder holder, int position) {

        final Help help = helpList.get(position);

        holder.tvTitle.setText(help.getTitle());

        Picasso.with(mContext).load(help.getIconUrl()).into(holder.ivIcon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helpItemClickEvents != null)
                    helpItemClickEvents.onHelpClicked(help);
            }
        });

    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }

    public void setList(List<Help> helpList) {
        this.helpList = helpList;
        notifyDataSetChanged();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView ivIcon;

        CommentsViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivIcon = itemView.findViewById(R.id.iv_icon);
        }
    }
}

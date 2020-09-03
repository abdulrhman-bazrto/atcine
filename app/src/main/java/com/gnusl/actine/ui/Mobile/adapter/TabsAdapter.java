package com.gnusl.actine.ui.Mobile.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ProfileClick;
import com.gnusl.actine.interfaces.TVTabClick;
import com.gnusl.actine.model.Cast;
import com.gnusl.actine.model.TabObject;
import com.gnusl.actine.ui.Mobile.custom.GifImageView;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.gnusl.actine.util.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TabsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TabObject> tabs = new ArrayList<>();
    private TVTabClick tvTabClick;


    public TabsAdapter(Context context, List<TabObject> tabs,TVTabClick tvTabClick) {
        this.mContext = context;
        this.tabs = tabs;
        this.tvTabClick = tvTabClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_tv_custom_tab_view, parent, false);
        return new TabViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
//        if (position > cast.size())
//            return;
//        else {
        ((TabViewHolder) holder).bind(tabs.get(position));
//        }
    }

    @Override
    public int getItemCount() {
        return tabs.size();
    }

    class TabViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageView;
        TextView tvTitle;

        TabViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);

            Utils.setOnFocusScale(itemView);
        }

        public void bind(TabObject tab) {
            try {
                tvTitle.setText(tab.getText());
                imageView.setImageDrawable(mContext.getResources().getDrawable(tab.getIconRes()));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(tab.isSelected())
                            return;
                        for (int i = 0; i < tabs.size(); i++) {
                            tabs.get(i).setSelected(false);
                        }
                        tab.setSelected(true);
                        tvTabClick.onClick(getAdapterPosition());
                        notifyDataSetChanged();

                    }
                });
                if (tab.isSelected()) {
                    itemView.setBackgroundColor(mContext.getResources().getColor( R.color.tv_main_red_color));
                } else {
                    itemView.setBackgroundColor(mContext.getResources().getColor( R.color.transparent));
                }
            } catch (Exception e) {
                Log.e(e.getMessage(), e.getMessage());
            }

//            Picasso.with(mContext).load(tab.getIconRes()).into(imageView, new Callback() {
//                @Override
//                public void onSuccess() {
//                }
//
//                @Override
//                public void onError() {
//
//                }
//            });


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

    public void setList(List<TabObject> tabs) {
        this.tabs = tabs;
        notifyDataSetChanged();
    }
}

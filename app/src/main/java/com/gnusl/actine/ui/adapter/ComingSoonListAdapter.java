package com.gnusl.actine.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComingSoonListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Show> movies = new ArrayList<>();


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
        return movies.size();
    }

    public void setList(List<Show> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    class MovieListViewHolder extends RecyclerView.ViewHolder {

        Button btnRemindMe;
        TextView tvShowCaption, tvShowDate, tvShowTitle;
        ImageView ivShowImage;

        MovieListViewHolder(View itemView) {
            super(itemView);
            btnRemindMe = itemView.findViewById(R.id.btn_remind_me);
            tvShowCaption = itemView.findViewById(R.id.tv_show_caption);
            tvShowDate = itemView.findViewById(R.id.tv_show_date);
            tvShowTitle = itemView.findViewById(R.id.tv_show_title);
            ivShowImage = itemView.findViewById(R.id.iv_movie_image);
        }

        public void bind() {

            final Show movie = movies.get(getAdapterPosition());

            tvShowTitle.setText(movie.getTitle());
            tvShowCaption.setText(movie.getDescription());
            tvShowDate.setText(movie.getShowTime());

            Picasso.with(mContext).load(movie.getCoverImageUrl()).into(ivShowImage);

            if (movie.getIsReminded()) {
                btnRemindMe.setBackgroundResource(R.drawable.bg_btn_remind_me);
                btnRemindMe.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_check_white), null, null, null);
            } else {
                btnRemindMe.setBackgroundResource(R.drawable.bg_btn_home);
                btnRemindMe.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_notifications_white), null, null, null);
            }

            btnRemindMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataLoader.postRequest(Urls.MovieRemind.getLink().replaceAll("%id%", String.valueOf(movie.getId())), new ConnectionDelegate() {
                        @Override
                        public void onConnectionError(int code, String message) {

                        }

                        @Override
                        public void onConnectionError(ANError anError) {

                        }

                        @Override
                        public void onConnectionSuccess(JSONObject jsonObject) {
                            if (jsonObject.optString("status").equalsIgnoreCase("deleted")) {
                                btnRemindMe.setBackgroundResource(R.drawable.bg_btn_home);
                                btnRemindMe.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_notifications_white), null, null, null);
                            } else if (jsonObject.optString("status").equalsIgnoreCase("added")){
                                btnRemindMe.setBackgroundResource(R.drawable.bg_btn_remind_me);
                                btnRemindMe.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.icon_check_white), null, null, null);
                            }
                        }
                    });
                }
            });
        }
    }
}

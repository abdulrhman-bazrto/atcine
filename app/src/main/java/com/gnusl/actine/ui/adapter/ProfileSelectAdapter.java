package com.gnusl.actine.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.model.Profile;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.gnusl.actine.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProfileSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Profile> profiles = new ArrayList<>();
    private Context mContext;
    private Dialog dialog;

    public ProfileSelectAdapter(Context context, Dialog dialog) {
        this.mContext = context;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.item_profile, parent, false);
        return new ProfileSelectAdapter.ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ProfileViewHolder)
            ((ProfileViewHolder) holder).bind(position);

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void setList(List<Profile> profiles) {
        this.profiles = profiles;
        notifyDataSetChanged();
    }

    public List<Profile> getList() {
        return profiles;
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {

        TextView tvProfileName;
        ImageView ivProfile;

        ProfileViewHolder(View itemView) {
            super(itemView);
            tvProfileName = itemView.findViewById(R.id.tv_profile_name);
            ivProfile = itemView.findViewById(R.id.iv_profile);
            Utils.setOnFocusScale(itemView);

        }

        public void bind(int pos) {

            if (pos == 0) {
                itemView.requestFocus();
            }

            final Profile profile = profiles.get(pos);

            tvProfileName.setText(profile.getName());

            Picasso.with(mContext).load(profile.getImageUrl()).into(ivProfile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < profiles.size(); i++) {
                        profiles.get(i).setCurrentProfile(false);
                    }
                    profile.setCurrentProfile(true);
                    SharedPreferencesUtils.saveCurrentProfile(profile.getId());
                    notifyDataSetChanged();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null)
                                dialog.dismiss();
                        }
                    }, 1000);

                }
            });

            if (profile.isCurrentProfile()) {
                ivProfile.setBackgroundResource(R.drawable.bg_circle_red);
            } else {
                ivProfile.setBackgroundResource(R.color.transparent);
            }

        }
    }


}
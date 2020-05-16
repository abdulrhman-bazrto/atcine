package com.gnusl.actine.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ProfileClick;
import com.gnusl.actine.model.Profile;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ManageProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Profile> profiles;
    private Context mContext;
    private ProfileClick profileClick;


    public ManageProfileAdapter(Context context, List<Profile> profiles, ProfileClick profileClick) {
        this.mContext = context;
        this.profiles = profiles;
        this.profileClick = profileClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        view = inflater.inflate(R.layout.item_profile, parent, false);
        return new ManageProfileAdapter.ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ProfileViewHolder)
            ((ProfileViewHolder) holder).bind();


    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void setList(List<Profile> profiles) {
        this.profiles = profiles;
        notifyDataSetChanged();
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {

        TextView tvProfileName;
        ImageView ivProfile;

        ProfileViewHolder(View itemView) {
            super(itemView);
            tvProfileName = itemView.findViewById(R.id.tv_profile_name);
            ivProfile = itemView.findViewById(R.id.iv_profile);

        }

        public void bind() {

            final Profile profile = profiles.get(getAdapterPosition());

            tvProfileName.setText(profile.getName());

            Picasso.with(mContext).load(profile.getImageUrl()).into(ivProfile);
            ViewCompat.setTransitionName(ivProfile,"test" + getAdapterPosition());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profileClick.onClickProfile(profiles.get(getAdapterPosition()),ivProfile);
                }
            });

        }
    }


}
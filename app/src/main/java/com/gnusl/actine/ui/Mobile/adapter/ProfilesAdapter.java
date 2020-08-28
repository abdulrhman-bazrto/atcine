package com.gnusl.actine.ui.Mobile.adapter;

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
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProfilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Profile> profiles = new ArrayList<>();
    private Context mContext;
    private ProfileClick profileClick;

    private static final int HOLDER_ADD_PROFILE = 0;
    private static final int HOLDER_PROFILE = 1;


    public ProfilesAdapter(Context context, ProfileClick profileClick) {
        this.mContext = context;
        this.profileClick = profileClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == HOLDER_PROFILE) {
            view = inflater.inflate(R.layout.item_profile, parent, false);
            return new ProfilesAdapter.ProfileViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_add_profile, parent, false);
            return new ProfilesAdapter.AddProfileViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ProfileViewHolder)
            ((ProfileViewHolder) holder).bind();
        else if (holder instanceof AddProfileViewHolder)
            ((AddProfileViewHolder) holder).bind();


    }

    @Override
    public int getItemCount() {
        if (profiles.size() == 0)
            return 1;
        else
            return profiles.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == profiles.size())
            return HOLDER_ADD_PROFILE;
        else
            return HOLDER_PROFILE;
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

        }

        public void bind() {

            final Profile profile = profiles.get(getAdapterPosition());

            tvProfileName.setText(profile.getName());

            Picasso.with(mContext).load(profile.getImageUrl()).into(ivProfile);
            ViewCompat.setTransitionName(ivProfile,"transition" + getAdapterPosition());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(profile.isCurrentProfile())
                        return;
                    for (int i = 0; i < profiles.size(); i++) {
                        profiles.get(i).setCurrentProfile(false);
                    }
                    profile.setCurrentProfile(true);
                    SharedPreferencesUtils.saveCurrentProfile(profile.getId());
                    profileClick.onClickProfile(profile,ivProfile,false);
                    notifyDataSetChanged();

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    profileClick.onClickProfile(profile, ivProfile,true);
                    return true;
                }
            });

            if (profile.isCurrentProfile()) {
                ivProfile.setBackgroundResource(R.drawable.bg_circle_red);
            } else {
                ivProfile.setBackgroundResource(R.color.transparent);
            }

        }

//        public void bind() {
//
//            final Profile profile = profiles.get(getAdapterPosition());
//
//            tvProfileName.setText(profile.getName());
//
//            Picasso.with(mContext).load(profile.getImageUrl()).into(ivProfile);
//            ViewCompat.setTransitionName(ivProfile, "test" + getAdapterPosition());
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    profileClick.onClickProfile(profiles.get(getAdapterPosition()), ivProfile);
//                }
//            });
//
//            ivProfile.setBackgroundResource(R.drawable.bg_circle_red);
//
//        }
    }


    class AddProfileViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;

        AddProfileViewHolder(View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_add_profile);

        }

        public void bind() {
            ViewCompat.setTransitionName(ivProfile, "AddNewProfile");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profileClick.onClickProfile(null, ivProfile,true);
                }
            });
        }
    }


}
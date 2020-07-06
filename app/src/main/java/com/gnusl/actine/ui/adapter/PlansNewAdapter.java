package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.model.PlanDetails;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.gnusl.actine.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class PlansNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final RecyclerView rv;
    private Context mContext;
    private List<PlanDetails> planDetails = new ArrayList<>();

    public PlansNewAdapter(Context context, List<PlanDetails> planDetails, RecyclerView rv) {
        this.mContext = context;
        this.rv = rv;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {


        ((ViewHolder) holder).bind();
    }

    @Override
    public int getItemCount() {
        return planDetails.size() == 0 ? 0 : 3;
    }

    public void setList(List<PlanDetails> planDetails) {
        this.planDetails = planDetails;
        notifyDataSetChanged();
    }

    public List<PlanDetails> getList() {
        return planDetails;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvPrice;
        LinearLayout llTexts;
        View background;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            llTexts = itemView.findViewById(R.id.ll_texts);
            background = itemView.findViewById(R.id.background);

        }

        public void bind() {

//            Utils.setOnFocusScale(itemView);
//            Utils.setOnFocusScale1(itemView, rv, getAdapterPosition());

            itemView.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    // run scale animation and make it bigger
                    Animation anim = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_in1);
                    rv.smoothScrollToPosition(getAdapterPosition());
                    itemView.startAnimation(anim);
                    anim.setFillAfter(true);

                    if (getAdapterPosition() == 0) {
                        SharedPreferencesUtils.saveCurrentSelectedPlan("basic");
                    } else if (getAdapterPosition() == 1) {
                        SharedPreferencesUtils.saveCurrentSelectedPlan("standard");
                    } else if (getAdapterPosition() == 2) {
                        SharedPreferencesUtils.saveCurrentSelectedPlan("premium");
                    }
                } else {
                    // run scale animation and make it smaller
                    Animation anim = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_out1);
                    itemView.startAnimation(anim);
                    anim.setFillAfter(true);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.requestFocus();
                    if (getAdapterPosition() == 0) {
                        SharedPreferencesUtils.saveCurrentSelectedPlan("basic");
                    } else if (getAdapterPosition() == 1) {
                        SharedPreferencesUtils.saveCurrentSelectedPlan("standard");
                    } else if (getAdapterPosition() == 2) {
                        SharedPreferencesUtils.saveCurrentSelectedPlan("premium");
                    }
                }
            });

            if (getAdapterPosition() == 0) {
                tvTitle.setText("Basic");
                for (int i = 0; i < planDetails.size(); i++) {
                    if (planDetails.get(i).getTitle().equalsIgnoreCase("price")) {
                        tvPrice.setText(planDetails.get(i).getBasic() + "$");
                    } else {
                        if (planDetails.get(i).getType().equalsIgnoreCase("symbol")) {
                            if (planDetails.get(i).getBasic().equalsIgnoreCase("true")) {
                                TextView tt = new TextView(mContext);
                                tt.setGravity(Gravity.CENTER);
                                tt.setTextColor(mContext.getResources().getColor(R.color.white));
                                tt.setText(planDetails.get(i).getTitle());
                                llTexts.addView(tt);
                            }
                        } else if (planDetails.get(i).getType().equalsIgnoreCase("number")) {
                            TextView tt = new TextView(mContext);
                            tt.setGravity(Gravity.CENTER);
                            tt.setTextColor(mContext.getResources().getColor(R.color.white));
                            tt.setText(planDetails.get(i).getBasic() + " " + planDetails.get(i).getTitle());
                            llTexts.addView(tt);
                        }
                    }
                }
            }
            if (getAdapterPosition() == 1) {
                tvTitle.setText("Standard");
                for (int i = 0; i < planDetails.size(); i++) {
                    if (planDetails.get(i).getTitle().equalsIgnoreCase("price")) {
                        tvPrice.setText(planDetails.get(i).getStandard() + "$");
                    } else {
                        if (planDetails.get(i).getType().equalsIgnoreCase("symbol")) {
                            if (planDetails.get(i).getStandard().equalsIgnoreCase("true")) {
                                TextView tt = new TextView(mContext);
                                tt.setGravity(Gravity.CENTER);
                                tt.setTextColor(mContext.getResources().getColor(R.color.white));
                                tt.setText(planDetails.get(i).getTitle());
                                llTexts.addView(tt);
                            }
                        } else if (planDetails.get(i).getType().equalsIgnoreCase("number")) {
                            TextView tt = new TextView(mContext);
                            tt.setGravity(Gravity.CENTER);
                            tt.setTextColor(mContext.getResources().getColor(R.color.white));
                            tt.setText(planDetails.get(i).getStandard() + " " + planDetails.get(i).getTitle());
                            llTexts.addView(tt);
                        }
                    }
                }
            }
            if (getAdapterPosition() == 2) {
                tvTitle.setText("Premium");
                for (int i = 0; i < planDetails.size(); i++) {
                    if (planDetails.get(i).getTitle().equalsIgnoreCase("price")) {
                        tvPrice.setText(planDetails.get(i).getPremium() + "$");
                    } else {
                        if (planDetails.get(i).getType().equalsIgnoreCase("symbol")) {
                            if (planDetails.get(i).getPremium().equalsIgnoreCase("true")) {
                                TextView tt = new TextView(mContext);
                                tt.setGravity(Gravity.CENTER);
                                tt.setTextColor(mContext.getResources().getColor(R.color.white));
                                tt.setText(planDetails.get(i).getTitle());
                                llTexts.addView(tt);
                            }
                        } else if (planDetails.get(i).getType().equalsIgnoreCase("number")) {
                            TextView tt = new TextView(mContext);
                            tt.setGravity(Gravity.CENTER);
                            tt.setTextColor(mContext.getResources().getColor(R.color.white));
                            tt.setText(planDetails.get(i).getPremium() + " " + planDetails.get(i).getTitle());
                            llTexts.addView(tt);
                        }
                    }
                }
            }
        }
    }


}
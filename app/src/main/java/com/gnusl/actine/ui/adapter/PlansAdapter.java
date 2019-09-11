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
import com.gnusl.actine.model.PlanDetails;

import java.util.ArrayList;
import java.util.List;


public class PlansAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<PlanDetails> planDetails = new ArrayList<>();


    private static final int HOLDER_SYMBOL_ROW = 0;
    private static final int HOLDER_NUMBER_ROW = 1;


    public PlansAdapter(Context context, List<PlanDetails> planDetails) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == HOLDER_NUMBER_ROW) {
            view = inflater.inflate(R.layout.layout_number_row, parent, false);
            return new NumberViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.layout_symbol_row, parent, false);
            return new SymbolViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof SymbolViewHolder)
            ((SymbolViewHolder) holder).bind();
        else if (holder instanceof NumberViewHolder)
            ((NumberViewHolder) holder).bind();


    }

    @Override
    public int getItemCount() {
        return planDetails.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (planDetails.get(position).getType().equalsIgnoreCase("number")) {
            return HOLDER_NUMBER_ROW;
        } else if (planDetails.get(position).getType().equalsIgnoreCase("symbol")) {
            return HOLDER_SYMBOL_ROW;
        }
        return HOLDER_NUMBER_ROW;
    }

    public void setList(List<PlanDetails> profiles) {
        this.planDetails = profiles;
        notifyDataSetChanged();
    }

    public List<PlanDetails> getList() {
        return planDetails;
    }

    public void setSelected(int planNumber) {
        for (PlanDetails planDetail : planDetails) {
            switch (planNumber){
                case 1:{
                    planDetail.setBasicSelected(true);
                    planDetail.setPremiumSelected(false);
                    planDetail.setStandardSelected(false);
                    break;
                }
                case 2:{
                    planDetail.setBasicSelected(false);
                    planDetail.setPremiumSelected(false);
                    planDetail.setStandardSelected(true);
                    break;
                }
                case 3:{
                    planDetail.setBasicSelected(false);
                    planDetail.setPremiumSelected(true);
                    planDetail.setStandardSelected(false);
                    break;
                }
            }
        }
        notifyDataSetChanged();

    }

    class SymbolViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView ivInStandard, ivInBasic, ivInPremium;


        SymbolViewHolder(View itemView) {
            super(itemView);
            ivInPremium = itemView.findViewById(R.id.iv_in_premium);
            ivInStandard = itemView.findViewById(R.id.iv_in_standard);
            ivInBasic = itemView.findViewById(R.id.iv_in_basic);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }

        public void bind() {

            final PlanDetails planDetails = PlansAdapter.this.planDetails.get(getAdapterPosition());
            tvTitle.setText(planDetails.getTitle());

            if (!planDetails.isBasicSelected()) {
                if (planDetails.getBasic().equalsIgnoreCase("false")) {
                    ivInBasic.setImageResource(R.drawable.icon_cancel_grey);
                } else {
                    ivInBasic.setImageResource(R.drawable.icon_check);
                }
            }else {
                if (planDetails.getBasic().equalsIgnoreCase("false")) {
                    ivInBasic.setImageResource(R.drawable.icon_cancel_red);
                } else {
                    ivInBasic.setImageResource(R.drawable.icon_check_red);
                }
            }

            if (!planDetails.isStandardSelected()) {
                if (planDetails.getStandard().equalsIgnoreCase("false")) {
                    ivInStandard.setImageResource(R.drawable.icon_cancel_grey);
                } else {
                    ivInStandard.setImageResource(R.drawable.icon_check);
                }
            }else {
                if (planDetails.getStandard().equalsIgnoreCase("false")) {
                    ivInStandard.setImageResource(R.drawable.icon_cancel_red);
                } else {
                    ivInStandard.setImageResource(R.drawable.icon_check_red);
                }
            }

            if (!planDetails.isPremiumSelected()) {
                if (planDetails.getPremium().equalsIgnoreCase("false")) {
                    ivInPremium.setImageResource(R.drawable.icon_cancel_grey);
                } else {
                    ivInPremium.setImageResource(R.drawable.icon_check);
                }
            }else {
                if (planDetails.getPremium().equalsIgnoreCase("false")) {
                    ivInPremium.setImageResource(R.drawable.icon_cancel_red);
                } else {
                    ivInPremium.setImageResource(R.drawable.icon_check_red);
                }
            }

        }
    }


    class NumberViewHolder extends RecyclerView.ViewHolder {

        TextView tvInPremium, tvInStandard, tvInBasic, tvTitle;

        NumberViewHolder(View itemView) {
            super(itemView);
            tvInPremium = itemView.findViewById(R.id.tv_in_premium);
            tvInStandard = itemView.findViewById(R.id.tv_in_standard);
            tvInBasic = itemView.findViewById(R.id.tv_in_basic);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }

        public void bind() {
            final PlanDetails planDetails = PlansAdapter.this.planDetails.get(getAdapterPosition());
            tvTitle.setText(planDetails.getTitle());
            tvInBasic.setText(planDetails.getBasic());
            tvInPremium.setText(planDetails.getPremium());
            tvInStandard.setText(planDetails.getStandard());

            if (planDetails.isBasicSelected()) {
                tvInBasic.setTextColor(mContext.getResources().getColor(R.color.main_red_color));

            }else {
                tvInBasic.setTextColor(mContext.getResources().getColor(R.color.gray1));
            }

            if (planDetails.isStandardSelected()) {

                tvInStandard.setTextColor(mContext.getResources().getColor(R.color.main_red_color));
            }else {
                tvInStandard.setTextColor(mContext.getResources().getColor(R.color.gray1));
            }

            if (planDetails.isPremiumSelected()) {
                tvInPremium.setTextColor(mContext.getResources().getColor(R.color.main_red_color));
            }else {
                tvInPremium.setTextColor(mContext.getResources().getColor(R.color.gray1));
            }
        }
    }


}
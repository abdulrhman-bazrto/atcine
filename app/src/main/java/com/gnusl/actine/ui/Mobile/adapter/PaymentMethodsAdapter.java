package com.gnusl.actine.ui.Mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.PaymentMethodItemClickEvents;
import com.gnusl.actine.model.PaymentMethods;
import com.gnusl.actine.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodsAdapter extends RecyclerView.Adapter<PaymentMethodsAdapter.CommentsViewHolder> {

    private Context mContext;
    private List<PaymentMethods> paymentMethods = new ArrayList<>();
    private final PaymentMethodItemClickEvents paymentMethodItemClickEvents;


    public PaymentMethodsAdapter(Context context, List<PaymentMethods> paymentMethods, PaymentMethodItemClickEvents paymentMethodItemClickEvents) {
        this.mContext = context;
        this.paymentMethods = paymentMethods;
        this.paymentMethodItemClickEvents = paymentMethodItemClickEvents;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_payment_method, parent, false);
        return new CommentsViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final CommentsViewHolder holder, int position) {

        PaymentMethods paymentMethod = this.paymentMethods.get(position);

        holder.tvTitle.setText(paymentMethod.getPaymentMethodName());

        switch (paymentMethod.getPaymentMethodName()) {
            case "AMEX": {
                holder.ivMethodLogo.setImageResource(R.drawable.icon_payment_amex);
                break;
            }
            case "Sadad": {
                holder.ivMethodLogo.setImageResource(R.drawable.icon_payment_sadad);
                break;
            }
            case "Benefit": {
                holder.ivMethodLogo.setImageResource(R.drawable.icon_payment_benefit);
                break;
            }
            case "VISA/MASTER": {
                holder.ivMethodLogo.setImageResource(R.drawable.icon_payment_visa_master);
                break;
            }
            case "MADA": {
                holder.ivMethodLogo.setImageResource(R.drawable.icon_payment_mada);
                break;
            }
            case "KNET": {
                holder.ivMethodLogo.setImageResource(R.drawable.icon_payment_knet);
                break;
            }
            case "Apple Pay": {
                holder.ivMethodLogo.setImageResource(R.drawable.icon_payment_apple_pay);
                break;
            }
            case "Qatar Debit Card": {
                holder.ivMethodLogo.setImageResource(R.drawable.icon_payment_qatar_cards);
                break;
            }
            case "Debit Cards UAE": {
                holder.ivMethodLogo.setImageResource(R.drawable.icon_payment_uae_cards);
                break;
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentMethodItemClickEvents != null)
                    paymentMethodItemClickEvents.onPaymentMethodClicked(paymentMethod);
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentMethods.size();
    }

    public void setList(List<PaymentMethods> helpList) {
        this.paymentMethods = helpList;
        notifyDataSetChanged();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ImageView ivMethodLogo;

        CommentsViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_method_name);
            ivMethodLogo = itemView.findViewById(R.id.iv_method_logo);

            Utils.setOnFocusScale(itemView);

        }
    }
}

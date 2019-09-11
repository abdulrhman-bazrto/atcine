package com.gnusl.actine.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.HelpItemClickEvents;
import com.gnusl.actine.interfaces.PaymentMethodItemClickEvents;
import com.gnusl.actine.model.PaymentMethods;

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

        CommentsViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_method_name);
        }
    }
}

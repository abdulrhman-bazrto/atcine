package com.gnusl.actine.ui.custom;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.PlanDetails;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.adapter.PlansAdapter;
import com.gnusl.actine.util.SharedPreferencesUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChoosePlanView extends ConstraintLayout implements View.OnClickListener, ConnectionDelegate {


    Context context;
    TextView tvBasic, tvStandard, tvPremium;
    RecyclerView rvPlansDetails;
    PlansAdapter plansAdapter;

    public ChoosePlanView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public ChoosePlanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public ChoosePlanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }


    private void init(AttributeSet attrs) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_choose_plan, ChoosePlanView.this);

        tvBasic = view.findViewById(R.id.tv_basic);
        tvStandard = view.findViewById(R.id.tv_standard);
        tvPremium = view.findViewById(R.id.tv_premium);

        tvBasic.setOnClickListener(this);
        tvStandard.setOnClickListener(this);
        tvPremium.setOnClickListener(this);

        rvPlansDetails = view.findViewById(R.id.rv_plans_details);

        plansAdapter = new PlansAdapter(context,new ArrayList<>());

        LinearLayoutManager layoutManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);

        rvPlansDetails.setLayoutManager(layoutManager);

        rvPlansDetails.setAdapter(plansAdapter);

        DataLoader.getRequest(Urls.UserType.getLink(), this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_basic: {
                selectPlan(1);
                break;
            }
            case R.id.tv_standard: {
                selectPlan(2);
                break;
            }
            case R.id.tv_premium: {
                selectPlan(3);
                break;
            }
        }
    }

    private void selectPlan(int planNumber) {
        switch (planNumber) {
            case 1: {
                SharedPreferencesUtils.saveCurrentSelectedPlan("basic");
                tvBasic.setBackgroundResource(R.drawable.bg_plan_selected);

                tvPremium.setBackgroundResource(R.drawable.bg_plan_unselected);

                tvStandard.setBackgroundResource(R.drawable.bg_plan_unselected);

                plansAdapter.setSelected(1);

                break;
            }
            case 2: {
                SharedPreferencesUtils.saveCurrentSelectedPlan("standard");
                tvStandard.setBackgroundResource(R.drawable.bg_plan_selected);

                tvPremium.setBackgroundResource(R.drawable.bg_plan_unselected);

                tvBasic.setBackgroundResource(R.drawable.bg_plan_unselected);

                plansAdapter.setSelected(2);

                break;
            }
            case 3: {
                SharedPreferencesUtils.saveCurrentSelectedPlan("premium");
                tvPremium.setBackgroundResource(R.drawable.bg_plan_selected);

                tvStandard.setBackgroundResource(R.drawable.bg_plan_unselected);

                tvBasic.setBackgroundResource(R.drawable.bg_plan_unselected);

                plansAdapter.setSelected(3);

                break;
            }
        }
    }

    @Override
    public void onConnectionError(int code, String message) {

    }

    @Override
    public void onConnectionError(ANError anError) {

    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (jsonObject.has("types")){
            List<PlanDetails> types = PlanDetails.newList(jsonObject.optJSONArray("types"));
            plansAdapter.setList(types);
            selectPlan(1);
        }
    }
}

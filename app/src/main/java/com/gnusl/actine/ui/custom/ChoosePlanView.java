package com.gnusl.actine.ui.custom;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gnusl.actine.R;


public class ChoosePlanView extends ConstraintLayout implements View.OnClickListener {

    TextView tvBasic, tvStandard, tvPremium;
    TextView tvPremiumPrice, tvStandardPrice, tvBasicPrice;
    FeaturesPriceRow fprHdAvailable, fprUltraHdAvailable, fprScreenCount, fprDevicesAvailable, fprUnlimited, fprCancelAnyTime, fprFirstMonthFree;

    public ChoosePlanView(Context context) {
        super(context);
        init(null);
    }

    public ChoosePlanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ChoosePlanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        tvPremiumPrice = view.findViewById(R.id.tv_premium_price);
        tvStandardPrice = view.findViewById(R.id.tv_standard_price);
        tvBasicPrice = view.findViewById(R.id.tv_basic_price);


        fprHdAvailable = findViewById(R.id.fpr_hd_available);
        fprUltraHdAvailable = findViewById(R.id.fpr_ultra_hd_available);
        fprScreenCount = findViewById(R.id.fpr_screen_count);
        fprDevicesAvailable = findViewById(R.id.fpr_devices_available);
        fprUnlimited = findViewById(R.id.fpr_unlimited);
        fprCancelAnyTime = findViewById(R.id.fpr_cancel_any_time);
        fprFirstMonthFree = findViewById(R.id.fpr_first_month_free);

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
                tvBasic.setBackgroundResource(R.drawable.bg_plan_selected);
                tvBasicPrice.setTextColor(getResources().getColor(R.color.main_red_color));
                fprHdAvailable.getIvInBasic().setImageResource(R.drawable.icon_check_red);
                fprUltraHdAvailable.getIvInBasic().setImageResource(R.drawable.icon_check_red);
                fprScreenCount.getIvInBasic().setImageResource(R.drawable.icon_check_red);
                fprDevicesAvailable.getIvInBasic().setImageResource(R.drawable.icon_check_red);
                fprUnlimited.getIvInBasic().setImageResource(R.drawable.icon_check_red);
                fprCancelAnyTime.getIvInBasic().setImageResource(R.drawable.icon_check_red);
                fprFirstMonthFree.getIvInBasic().setImageResource(R.drawable.icon_check_red);

                tvPremium.setBackgroundResource(R.drawable.bg_plan_unselected);
                tvPremiumPrice.setTextColor(getResources().getColor(R.color.white));
                fprHdAvailable.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprUltraHdAvailable.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprScreenCount.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprDevicesAvailable.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprUnlimited.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprCancelAnyTime.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprFirstMonthFree.getIvInPremium().setImageResource(R.drawable.icon_check);

                tvStandard.setBackgroundResource(R.drawable.bg_plan_unselected);
                tvStandardPrice.setTextColor(getResources().getColor(R.color.white));
                fprHdAvailable.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprUltraHdAvailable.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprScreenCount.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprDevicesAvailable.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprUnlimited.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprCancelAnyTime.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprFirstMonthFree.getIvInStandard().setImageResource(R.drawable.icon_check);

                break;
            }
            case 2: {
                tvStandard.setBackgroundResource(R.drawable.bg_plan_selected);
                tvStandardPrice.setTextColor(getResources().getColor(R.color.main_red_color));
                fprHdAvailable.getIvInStandard().setImageResource(R.drawable.icon_check_red);
                fprUltraHdAvailable.getIvInStandard().setImageResource(R.drawable.icon_check_red);
                fprScreenCount.getIvInStandard().setImageResource(R.drawable.icon_check_red);
                fprDevicesAvailable.getIvInStandard().setImageResource(R.drawable.icon_check_red);
                fprUnlimited.getIvInStandard().setImageResource(R.drawable.icon_check_red);
                fprCancelAnyTime.getIvInStandard().setImageResource(R.drawable.icon_check_red);
                fprFirstMonthFree.getIvInStandard().setImageResource(R.drawable.icon_check_red);

                tvPremium.setBackgroundResource(R.drawable.bg_plan_unselected);
                tvPremiumPrice.setTextColor(getResources().getColor(R.color.white));
                fprHdAvailable.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprUltraHdAvailable.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprScreenCount.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprDevicesAvailable.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprUnlimited.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprCancelAnyTime.getIvInPremium().setImageResource(R.drawable.icon_check);
                fprFirstMonthFree.getIvInPremium().setImageResource(R.drawable.icon_check);

                tvBasic.setBackgroundResource(R.drawable.bg_plan_unselected);
                tvBasicPrice.setTextColor(getResources().getColor(R.color.white));
                fprHdAvailable.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprUltraHdAvailable.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprScreenCount.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprDevicesAvailable.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprUnlimited.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprCancelAnyTime.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprFirstMonthFree.getIvInBasic().setImageResource(R.drawable.icon_check);

                break;
            }
            case 3: {
                tvPremium.setBackgroundResource(R.drawable.bg_plan_selected);
                tvPremiumPrice.setTextColor(getResources().getColor(R.color.main_red_color));
                fprHdAvailable.getIvInPremium().setImageResource(R.drawable.icon_check_red);
                fprUltraHdAvailable.getIvInPremium().setImageResource(R.drawable.icon_check_red);
                fprScreenCount.getIvInPremium().setImageResource(R.drawable.icon_check_red);
                fprDevicesAvailable.getIvInPremium().setImageResource(R.drawable.icon_check_red);
                fprUnlimited.getIvInPremium().setImageResource(R.drawable.icon_check_red);
                fprCancelAnyTime.getIvInPremium().setImageResource(R.drawable.icon_check_red);
                fprFirstMonthFree.getIvInPremium().setImageResource(R.drawable.icon_check_red);

                tvStandard.setBackgroundResource(R.drawable.bg_plan_unselected);
                tvStandardPrice.setTextColor(getResources().getColor(R.color.white));
                fprHdAvailable.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprUltraHdAvailable.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprScreenCount.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprDevicesAvailable.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprUnlimited.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprCancelAnyTime.getIvInStandard().setImageResource(R.drawable.icon_check);
                fprFirstMonthFree.getIvInStandard().setImageResource(R.drawable.icon_check);

                tvBasic.setBackgroundResource(R.drawable.bg_plan_unselected);
                tvBasicPrice.setTextColor(getResources().getColor(R.color.white));
                fprHdAvailable.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprUltraHdAvailable.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprScreenCount.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprDevicesAvailable.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprUnlimited.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprCancelAnyTime.getIvInBasic().setImageResource(R.drawable.icon_check);
                fprFirstMonthFree.getIvInBasic().setImageResource(R.drawable.icon_check);

                break;
            }
        }
    }
}

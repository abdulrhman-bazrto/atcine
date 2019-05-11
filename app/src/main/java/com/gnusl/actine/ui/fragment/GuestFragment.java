package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.custom.FeaturesPriceRow;


public class GuestFragment extends Fragment implements View.OnClickListener {

    private View inflatedView;

    private TextView tvPrice, tvDevices, tvCancel, tvBtnDesc;
    private ImageView tvPriceArrow, tvDevicesArrow, tvCancelArrow;
    private View la_cancel, la_devices, la_price;
    private Button btnJoinFree;

    boolean isPriceSelected = false, isDevicesSelected = false, isCancelSelected = true;

    public GuestFragment() {
    }

    public static GuestFragment newInstance() {
        GuestFragment fragment = new GuestFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_guest, container, false);
            isDevicesSelected = true;
            init();
        }
        return inflatedView;
    }

    private void init() {

        btnJoinFree = inflatedView.findViewById(R.id.btn_join_free);

        tvCancel = inflatedView.findViewById(R.id.tv_cancel);
        tvDevices = inflatedView.findViewById(R.id.tv_devices);
        tvPrice = inflatedView.findViewById(R.id.tv_price);
        tvBtnDesc = inflatedView.findViewById(R.id.tv_btn_desc);

        tvCancelArrow = inflatedView.findViewById(R.id.iv_cancel_arrow);
        tvPriceArrow = inflatedView.findViewById(R.id.iv_price_arrow);
        tvDevicesArrow = inflatedView.findViewById(R.id.iv_devices_arrow);

        la_cancel = inflatedView.findViewById(R.id.layout_cancel);
        la_devices = inflatedView.findViewById(R.id.layout_devices);
        la_price = inflatedView.findViewById(R.id.layout_price);

        lightSelected();

        tvCancel.setOnClickListener(this);
        tvDevices.setOnClickListener(this);
        tvPrice.setOnClickListener(this);

        btnJoinFree.setOnClickListener(this);
    }

    private void lightSelected() {
        if (isCancelSelected) {
            tvCancel.setTextColor(getResources().getColor(R.color.white));
            tvCancel.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_cancel_white), null, null);
            tvCancel.setBackgroundColor(getResources().getColor(R.color.main_red_color));

            tvBtnDesc.setText("If you decide ATCINE isn't for you - no problem. \n No commitment. Cancel online at any time.");

            tvDevices.setTextColor(getResources().getColor(R.color.dark_light_gray));
            tvDevices.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_devices_grey), null, null);
            tvDevices.setBackgroundColor(getResources().getColor(R.color.dark_gray));

            tvPrice.setTextColor(getResources().getColor(R.color.dark_light_gray));
            tvPrice.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_price_grey), null, null);
            tvPrice.setBackgroundColor(getResources().getColor(R.color.dark_gray));

            tvCancelArrow.setVisibility(View.VISIBLE);
            tvDevicesArrow.setVisibility(View.GONE);
            tvPriceArrow.setVisibility(View.GONE);

            la_cancel.setVisibility(View.VISIBLE);
            la_devices.setVisibility(View.GONE);
            la_price.setVisibility(View.GONE);


        } else if (isDevicesSelected) {
            tvDevices.setTextColor(getResources().getColor(R.color.white));
            tvDevices.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_devices_white), null, null);
            tvDevices.setBackgroundColor(getResources().getColor(R.color.main_red_color));

            tvBtnDesc.setText("Watch TV shows and movies anytime, \n anywhere --- Personalized for you.");

            tvCancel.setTextColor(getResources().getColor(R.color.dark_light_gray));
            tvCancel.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_cancel_grey), null, null);
            tvCancel.setBackgroundColor(getResources().getColor(R.color.dark_gray));

            tvPrice.setTextColor(getResources().getColor(R.color.dark_light_gray));
            tvPrice.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_price_grey), null, null);
            tvPrice.setBackgroundColor(getResources().getColor(R.color.dark_gray));

            tvCancelArrow.setVisibility(View.GONE);
            tvDevicesArrow.setVisibility(View.VISIBLE);
            tvPriceArrow.setVisibility(View.GONE);

            la_cancel.setVisibility(View.GONE);
            la_devices.setVisibility(View.VISIBLE);
            la_price.setVisibility(View.GONE);

        } else if (isPriceSelected) {
            tvPrice.setTextColor(getResources().getColor(R.color.white));
            tvPrice.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_price_white), null, null);
            tvPrice.setBackgroundColor(getResources().getColor(R.color.main_red_color));

            tvBtnDesc.setText("Choose one plan and watch everything on ATCINE.");

            tvCancel.setTextColor(getResources().getColor(R.color.dark_light_gray));
            tvCancel.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_cancel_grey), null, null);
            tvCancel.setBackgroundColor(getResources().getColor(R.color.dark_gray));

            tvDevices.setTextColor(getResources().getColor(R.color.dark_light_gray));
            tvDevices.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.icon_devices_grey), null, null);
            tvDevices.setBackgroundColor(getResources().getColor(R.color.dark_gray));

            tvCancelArrow.setVisibility(View.GONE);
            tvDevicesArrow.setVisibility(View.GONE);
            tvPriceArrow.setVisibility(View.VISIBLE);

            la_cancel.setVisibility(View.GONE);
            la_devices.setVisibility(View.GONE);
            la_price.setVisibility(View.VISIBLE);

            initilizePriceLayout();
        }
    }

    private void initilizePriceLayout() {
        FeaturesPriceRow hdAvailable, ultraHdAvailable, screenCount, devicesAvailable, unlimited, cancel, free;
        hdAvailable = inflatedView.findViewById(R.id.fpr_hd_available);
        hdAvailable.setData("HD available", false, true, true);
        ultraHdAvailable = inflatedView.findViewById(R.id.fpr_ultra_hd_available);
        ultraHdAvailable.setData("Ultra HD available", false, false, true);
        screenCount = inflatedView.findViewById(R.id.fpr_screen_count);
        screenCount.setData("Screens you can watch on at the same time", true, true, true);
        devicesAvailable = inflatedView.findViewById(R.id.fpr_devices_available);
        devicesAvailable.setData("Watch on your laptop, TV, phone and tablet", true, true, true);
        unlimited = inflatedView.findViewById(R.id.fpr_unlimited);
        unlimited.setData("Unlimited movies and TV shows", true, true, true);
        cancel = inflatedView.findViewById(R.id.fpr_cancel_any_time);
        cancel.setData("Cancel any time", true, true, true);
        free = inflatedView.findViewById(R.id.fpr_first_month_free);
        free.setData("First month free", true, true, true);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_join_free: {
                if (getActivity() != null) {
                    ((AuthActivity) getActivity()).replaceFragment(FragmentTags.RegisterFragment);
                }
                break;
            }
            case R.id.tv_price: {
                isPriceSelected = true;
                isCancelSelected = false;
                isDevicesSelected = false;
                lightSelected();
                break;
            }
            case R.id.tv_devices: {
                isPriceSelected = false;
                isCancelSelected = false;
                isDevicesSelected = true;
                lightSelected();
                break;
            }
            case R.id.tv_cancel: {
                isPriceSelected = false;
                isCancelSelected = true;
                isDevicesSelected = false;
                lightSelected();
                break;
            }
        }
    }

}

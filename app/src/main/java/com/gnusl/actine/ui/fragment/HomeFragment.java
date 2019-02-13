package com.gnusl.actine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.ui.activity.MainActivity;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private View inflatedView;

    private TextView tvPrice, tvDevices, tvCancel, tvBtnDesc;
    private ImageView tvPriceArrow, tvDevicesArrow, tvCancelArrow;

    boolean isPriceSelected = false, isDevicesSelected = false, isCancelSelected = true;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_home, container, false);
            isDevicesSelected = true;
            init();
        }
        return inflatedView;
    }

    private void init() {
        tvCancel = inflatedView.findViewById(R.id.tv_cancel);
        tvDevices = inflatedView.findViewById(R.id.tv_devices);
        tvPrice = inflatedView.findViewById(R.id.tv_price);
        tvBtnDesc = inflatedView.findViewById(R.id.tv_btn_desc);

        tvCancelArrow = inflatedView.findViewById(R.id.iv_cancel_arrow);
        tvPriceArrow = inflatedView.findViewById(R.id.iv_price_arrow);
        tvDevicesArrow = inflatedView.findViewById(R.id.iv_devices_arrow);

        lightSelected();

        tvCancel.setOnClickListener(this);
        tvDevices.setOnClickListener(this);
        tvPrice.setOnClickListener(this);
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
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
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

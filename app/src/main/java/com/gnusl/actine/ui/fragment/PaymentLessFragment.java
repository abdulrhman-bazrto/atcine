package com.gnusl.actine.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.PaymentMethodItemClickEvents;
import com.gnusl.actine.model.PaymentMethods;
import com.gnusl.actine.model.User;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.activity.PaymentActivity;
import com.gnusl.actine.ui.adapter.PaymentMethodsAdapter;
import com.gnusl.actine.ui.custom.CustomAppBarRegister;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class PaymentLessFragment extends Fragment implements View.OnClickListener, ConnectionDelegate, PaymentMethodItemClickEvents {

    View inflatedView;

    private Button btnGotoStep1, btnGotoStep2, btnPayment;
    private View viewStep1, viewStep3, viewStep0, viewStepPayPal, viewSetPaymentGateway, tvChangePlan;

    private TextView tvPayPal, tvCreditOrDebit;
    private CustomAppBarRegister cubRegister;
    private RecyclerView rvPaymentGateways;
    private KProgressHUD progressHUD;

    public PaymentLessFragment() {
    }

    public static PaymentLessFragment newInstance() {
        PaymentLessFragment fragment = new PaymentLessFragment();
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
            inflatedView = inflater.inflate(R.layout.fragment_payment_less, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        findViews();


        btnGotoStep1.setOnClickListener(this);
        btnGotoStep2.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
        cubRegister.getBtnLogin().setOnClickListener(this);
        tvPayPal.setOnClickListener(this);
        tvCreditOrDebit.setOnClickListener(this);
        tvChangePlan.setOnClickListener(this);

        viewSetPaymentGateway.setOnClickListener(this);

        cubRegister.getBtnHelp().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    ((AuthActivity) getActivity()).replaceFragment(FragmentTags.HelpFragment);
            }
        });

    }

    private void findViews() {
        viewStep1 = inflatedView.findViewById(R.id.sv_step1);
        viewStep3 = inflatedView.findViewById(R.id.cl_step3);
        viewStep0 = inflatedView.findViewById(R.id.cl_step_0);

        viewStepPayPal = inflatedView.findViewById(R.id.cl_step_pay_pal);
        tvChangePlan = inflatedView.findViewById(R.id.tv_change_plan);
        viewSetPaymentGateway = inflatedView.findViewById(R.id.cl_step_payment_gateways);
        rvPaymentGateways = inflatedView.findViewById(R.id.rv_payment_gateways);

        btnGotoStep1 = inflatedView.findViewById(R.id.btn_goto_step1);
        btnGotoStep2 = inflatedView.findViewById(R.id.btn_goto_step2);

        btnPayment = inflatedView.findViewById(R.id.btn_start_membership);

        tvCreditOrDebit = inflatedView.findViewById(R.id.tv_credit_or_debit);
        tvPayPal = inflatedView.findViewById(R.id.tv_paypal);

        cubRegister = inflatedView.findViewById(R.id.cub_register);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goto_step1: {
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.VISIBLE);
                viewStep3.setVisibility(View.GONE);
                viewStepPayPal.setVisibility(View.GONE);
                viewSetPaymentGateway.setVisibility(View.GONE);
                break;
            }
            case R.id.btn_goto_step2: {
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.GONE);
                viewStep3.setVisibility(View.VISIBLE);
                viewStepPayPal.setVisibility(View.GONE);
                viewSetPaymentGateway.setVisibility(View.GONE);
                break;
            }
            case R.id.tv_credit_or_debit: {
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.GONE);
                viewStep3.setVisibility(View.GONE);
                viewSetPaymentGateway.setVisibility(View.VISIBLE);
                getPaymentsGateway();
                break;
            }
            case R.id.tv_paypal: {
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.GONE);
                viewStep3.setVisibility(View.GONE);
                viewStepPayPal.setVisibility(View.VISIBLE);
                viewSetPaymentGateway.setVisibility(View.GONE);
                break;
            }
            case R.id.tv_change_plan: {
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.VISIBLE);
                viewStep3.setVisibility(View.GONE);
                viewStepPayPal.setVisibility(View.GONE);
                viewSetPaymentGateway.setVisibility(View.GONE);
                break;
            }
            case R.id.btn_start_membership: {
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
                break;
            }
            case R.id.btn_login: {
                if (getActivity() != null) {
                    ((AuthActivity) getActivity()).replaceFragment(FragmentTags.LoginFragment);
                }
                break;
            }
        }
    }

    private void getPaymentsGateway() {

        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setMaxProgress(100)
                .show();

        HashMap<String, String> body = new HashMap<>();
        body.put("email", SharedPreferencesUtils.getUser().getEmail());
        body.put("user_type", SharedPreferencesUtils.getCurrentSelectedPlan());

        DataLoader.postRequest(Urls.Pay.getLink(), body, this);
    }

    @Override
    public void onConnectionError(int code, String message) {
        if (progressHUD != null)
            progressHUD.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        if (progressHUD != null)
            progressHUD.dismiss();
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (progressHUD != null)
            progressHUD.dismiss();

        if (jsonObject.has("payment_methods")) {
            List<PaymentMethods> paymentMethods = PaymentMethods.newList(jsonObject.optJSONArray("payment_methods"));
            PaymentMethodsAdapter paymentMethodsAdapter = new PaymentMethodsAdapter(getActivity(), paymentMethods, PaymentLessFragment.this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            rvPaymentGateways.setLayoutManager(layoutManager);
            rvPaymentGateways.setAdapter(paymentMethodsAdapter);

        }
    }

    @Override
    public void onPaymentMethodClicked(PaymentMethods paymentMethods) {
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra("url", paymentMethods.getPaymentMethodUrl());
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Toast.makeText(getActivity(), "done", Toast.LENGTH_LONG).show();

            HashMap<String, String> body = new HashMap<>();
            body.put("payment_token", data.getStringExtra("paymentId"));
            body.put("status", "valid");
            body.put("user_type", SharedPreferencesUtils.getCurrentSelectedPlan());

            progressHUD = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel(getString(R.string.please_wait))
                    .setMaxProgress(100)
                    .show();

            DataLoader.postRequest(Urls.AccountUpdate.getLink(), body, new ConnectionDelegate() {
                @Override
                public void onConnectionError(int code, String message) {
                    if (progressHUD != null)
                        progressHUD.dismiss();
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConnectionError(ANError anError) {
                    if (progressHUD != null)
                        progressHUD.dismiss();
//                    Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConnectionSuccess(JSONObject jsonObject) {
                    if (progressHUD != null)
                        progressHUD.dismiss();

                    User user = SharedPreferencesUtils.getUser();
                    if (user != null) {
                        user.setStatus("valid");
                        SharedPreferencesUtils.saveUser(user);
                    }
                    if (getActivity() != null) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                }
            });

        } else if (requestCode == 100 && resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
        }
    }
}

package com.gnusl.actine.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.transition.TransitionInflater;
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
import com.gnusl.actine.model.PlanDetails;
import com.gnusl.actine.model.User;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.activity.PaymentActivity;
import com.gnusl.actine.ui.adapter.PaymentMethodsAdapter;
import com.gnusl.actine.ui.adapter.PlansNewAdapter;
import com.gnusl.actine.ui.custom.CenterLayoutManager;
import com.gnusl.actine.ui.custom.CustomAppBarRegister;
import com.gnusl.actine.ui.custom.LoaderPopUp;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class RegisterFragment extends Fragment implements View.OnClickListener, ConnectionDelegate, PaymentMethodItemClickEvents {

    View inflatedView;

    private Button btnGotoStep1, btnGotoStep2, btnGotoStep3, btnGotoStep2half, btnPayment, btnLogin_;
    private View viewStep1, viewStep2, viewStep3, viewStep0, viewStep1half5, viewStepPayPal, viewSetPaymentGateway, tvChangePlan, tvChangePlan1;

    private TextView tvAlreadyUser, tvPayPal, tvCreditOrDebit, tvYourPlan;
    private CustomAppBarRegister cubRegister;
    private RecyclerView rvPaymentGateways;
    private EditText etPasswordConfirm, etPassword, etUsername, etName;
    private RecyclerView rvPlans;

    private PlansNewAdapter plansAdapter;

    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_register, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        findViews();

        if (btnLogin_ != null) {
            btnLogin_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null) {
                        ((AuthActivity) getActivity()).replaceFragment(FragmentTags.LoginFragment);
                    }
                }
            });
        }

        btnGotoStep1.setOnClickListener(this);
        btnGotoStep2.setOnClickListener(this);
        btnGotoStep3.setOnClickListener(this);
        btnGotoStep2half.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
        cubRegister.getBtnLogin().setOnClickListener(this);

        tvPayPal.setOnClickListener(this);
        tvCreditOrDebit.setOnClickListener(this);
        tvChangePlan.setOnClickListener(this);
        tvChangePlan1.setOnClickListener(this);

        viewSetPaymentGateway.setOnClickListener(this);

        SpannableString ss = new SpannableString(tvAlreadyUser.getText().toString());
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (getActivity() != null) {
                    ((AuthActivity) getActivity()).replaceFragment(FragmentTags.LoginFragment);
                }
            }
        };
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        ss.setSpan(bss, tvAlreadyUser.getText().toString().lastIndexOf("?") + 2, tvAlreadyUser.getText().toString().length(), 0);
        ss.setSpan(span1, tvAlreadyUser.getText().toString().lastIndexOf("?") + 2, tvAlreadyUser.getText().toString().length(), 0);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), tvAlreadyUser.getText().toString().lastIndexOf("?") + 2, tvAlreadyUser.getText().toString().length(), 0);

        cubRegister.getBtnHelp().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    ((AuthActivity) getActivity()).replaceFragment(FragmentTags.HelpFragment);
            }
        });

        tvAlreadyUser.setMovementMethod(LinkMovementMethod.getInstance());
        tvAlreadyUser.setText(ss);


        plansAdapter = new PlansNewAdapter(getActivity(), new ArrayList<>(),rvPlans);

        CenterLayoutManager layoutManager = new CenterLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);

        rvPlans.setLayoutManager(layoutManager);

        rvPlans.setAdapter(plansAdapter);

        DataLoader.getRequest(Urls.UserType.getLink(), new ConnectionDelegate() {
            @Override
            public void onConnectionError(int code, String message) {
                Toast.makeText(getActivity(), "error happened " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionError(ANError anError) {
                Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnectionSuccess(JSONObject jsonObject) {
                if (jsonObject.has("types")) {
                    List<PlanDetails> types = PlanDetails.newList(jsonObject.optJSONArray("types"));
                    plansAdapter.setList(types);
                }
            }
        });

    }

    private void findViews() {
        viewStep1 = inflatedView.findViewById(R.id.sv_step1);
        viewStep2 = inflatedView.findViewById(R.id.ll_step2);
        viewStep3 = inflatedView.findViewById(R.id.cl_step3);
        viewStep0 = inflatedView.findViewById(R.id.cl_step_0);
        viewStep1half5 = inflatedView.findViewById(R.id.cl_step_1half5);
        viewStepPayPal = inflatedView.findViewById(R.id.cl_step_pay_pal);
        tvChangePlan = inflatedView.findViewById(R.id.tv_change_plan);
        tvChangePlan1 = inflatedView.findViewById(R.id.tv_change_plan1);
        viewSetPaymentGateway = inflatedView.findViewById(R.id.cl_step_payment_gateways);
        rvPaymentGateways = inflatedView.findViewById(R.id.rv_payment_gateways);

        rvPlans = inflatedView.findViewById(R.id.rv_plans);

        btnGotoStep1 = inflatedView.findViewById(R.id.btn_goto_step1);
        btnGotoStep2 = inflatedView.findViewById(R.id.btn_goto_step2);
        btnGotoStep3 = inflatedView.findViewById(R.id.btn_register);
        btnGotoStep2half = inflatedView.findViewById(R.id.btn_goto_step2half);
        btnPayment = inflatedView.findViewById(R.id.btn_start_membership);
        btnLogin_ = inflatedView.findViewById(R.id.btn_login_);

        etPassword = inflatedView.findViewById(R.id.et_password);
        etPasswordConfirm = inflatedView.findViewById(R.id.et_password_confirm);
        etUsername = inflatedView.findViewById(R.id.et_username);
        etName = inflatedView.findViewById(R.id.et_name);

        tvAlreadyUser = inflatedView.findViewById(R.id.tv_already_user);
        tvCreditOrDebit = inflatedView.findViewById(R.id.tv_credit_or_debit);
        tvPayPal = inflatedView.findViewById(R.id.tv_paypal);
        tvYourPlan = inflatedView.findViewById(R.id.tv_your_plan1);

        cubRegister = inflatedView.findViewById(R.id.cub_register);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goto_step1: {
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.VISIBLE);
                viewStep2.setVisibility(View.GONE);
                viewStep3.setVisibility(View.GONE);
                viewStep1half5.setVisibility(View.GONE);
                viewStepPayPal.setVisibility(View.GONE);
                viewSetPaymentGateway.setVisibility(View.GONE);
                rvPlans.requestFocus();
                break;
            }
            case R.id.btn_goto_step2: {
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.GONE);
                viewStep1half5.setVisibility(View.VISIBLE);
                viewStep2.setVisibility(View.GONE);
                viewStep3.setVisibility(View.GONE);
                viewStepPayPal.setVisibility(View.GONE);
                viewSetPaymentGateway.setVisibility(View.GONE);
                break;
            }
            case R.id.btn_goto_step2half: {
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.GONE);
                viewStep1half5.setVisibility(View.GONE);
                viewStep2.setVisibility(View.VISIBLE);
                viewStep3.setVisibility(View.GONE);
                viewStepPayPal.setVisibility(View.GONE);
                viewSetPaymentGateway.setVisibility(View.GONE);
                break;
            }
            case R.id.btn_register: {
                if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("can't be empty");
                    return;
                }
                Pattern p = Patterns.EMAIL_ADDRESS;
                Matcher m = p.matcher(etUsername.getText().toString());
                if (!m.matches()) {
                    etUsername.setError("invalid email");
                    return;
                }
                if (etPassword.getText().toString().isEmpty()) {
                    etUsername.setError("can't be empty");
                    return;
                }
                if (etPasswordConfirm.getText().toString().isEmpty()) {
                    etUsername.setError("can't be empty");
                    return;
                }
                if (!etPasswordConfirm.getText().toString().equalsIgnoreCase(etPassword.getText().toString())) {
                    etPasswordConfirm.setError("mismatch");
                    return;
                }
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.GONE);
                viewStep2.setVisibility(View.GONE);
                viewStep3.setVisibility(View.VISIBLE);
                viewStep1half5.setVisibility(View.GONE);
                viewStepPayPal.setVisibility(View.GONE);
                viewSetPaymentGateway.setVisibility(View.GONE);
                break;
            }
            case R.id.tv_credit_or_debit: {
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.GONE);
                viewStep2.setVisibility(View.GONE);
                viewStep3.setVisibility(View.GONE);
                viewSetPaymentGateway.setVisibility(View.VISIBLE);
                tvYourPlan.setText(
                        tvYourPlan.getText().toString()
                                .replace("11.99$", SharedPreferencesUtils.getCurrentSelectedPlanPrice()));
                getPaymentsGateway();
                break;
            }
            case R.id.tv_paypal: {
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.GONE);
                viewStep2.setVisibility(View.GONE);
                viewStep3.setVisibility(View.GONE);
                viewStep1half5.setVisibility(View.GONE);
                viewStepPayPal.setVisibility(View.VISIBLE);
                viewSetPaymentGateway.setVisibility(View.GONE);
                break;
            }
            case R.id.tv_change_plan:
            case R.id.tv_change_plan1: {
                viewStep0.setVisibility(View.GONE);
                viewStep1.setVisibility(View.VISIBLE);
                viewStep2.setVisibility(View.GONE);
                viewStep3.setVisibility(View.GONE);
                viewStep1half5.setVisibility(View.GONE);
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

        LoaderPopUp.show(getActivity());

        HashMap<String, String> body = new HashMap<>();
        body.put("email", etUsername.getText().toString());
        body.put("user_type", SharedPreferencesUtils.getCurrentSelectedPlan());

        DataLoader.postRequest(Urls.Pay.getLink(), body, this);
    }

    @Override
    public void onConnectionError(int code, String message) {
        LoaderPopUp.dismissLoader();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        LoaderPopUp.dismissLoader();
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        LoaderPopUp.dismissLoader();

        if (jsonObject.has("payment_methods")) {
            List<PaymentMethods> paymentMethods = PaymentMethods.newList(jsonObject.optJSONArray("payment_methods"));
            PaymentMethodsAdapter paymentMethodsAdapter = new PaymentMethodsAdapter(getActivity(), paymentMethods, RegisterFragment.this);
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
            body.put("name", etName.getText().toString());
            body.put("password", etPassword.getText().toString());
            if (etUsername.getText().toString().contains("@"))
                body.put("email", etUsername.getText().toString());
            else
                body.put("mobile", etUsername.getText().toString());
            body.put("user_type", SharedPreferencesUtils.getCurrentSelectedPlan());

            LoaderPopUp.show(getActivity());

            DataLoader.postRequest(Urls.Register.getLink(), body, new ConnectionDelegate() {
                @Override
                public void onConnectionError(int code, String message) {
                    LoaderPopUp.dismissLoader();
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConnectionError(ANError anError) {
                    LoaderPopUp.dismissLoader();
//                    Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onConnectionSuccess(JSONObject jsonObject) {
                    LoaderPopUp.dismissLoader();
                    if (jsonObject.has("user")) {
                        SharedPreferencesUtils.saveUser(User.parse(jsonObject.optJSONObject("user")));
                    }
                    if (jsonObject.has("token")) {
                        SharedPreferencesUtils.saveToken(jsonObject.optString("token"));
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

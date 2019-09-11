package com.gnusl.actine.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethods implements Serializable {


    private String paymentMethodName;
    private String paymentMethodUrl;
    private String paymentMethodCode;

    public static PaymentMethods newInstance(JSONObject jsonObject) {
        PaymentMethods paymentMethods = new PaymentMethods();
        paymentMethods.setPaymentMethodCode(jsonObject.optString("PaymentMethodCode"));
        paymentMethods.setPaymentMethodName(jsonObject.optString("PaymentMethodName"));
        paymentMethods.setPaymentMethodUrl(jsonObject.optString("PaymentMethodUrl"));

        return paymentMethods;
    }

    public static List<PaymentMethods> newList(JSONArray jsonArray) {
        List<PaymentMethods> profiles = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
            profiles.add(newInstance(jsonArray.optJSONObject(i)));

        return profiles;
    }

    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public String getPaymentMethodUrl() {
        return paymentMethodUrl;
    }

    public void setPaymentMethodUrl(String paymentMethodUrl) {
        this.paymentMethodUrl = paymentMethodUrl;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }
}

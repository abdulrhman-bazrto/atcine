package com.gnusl.actine.interfaces;

import com.androidnetworking.error.ANError;

import org.json.JSONObject;

public interface ConnectionDelegate {

    void onConnectionError(int code, String message);

    void onConnectionError(ANError anError);

    void onConnectionSuccess(JSONObject jsonObject);

}

package com.gnusl.actine.network;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.gnusl.actine.application.Atcine;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.util.SharedPreferencesUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class DataLoader {

    public static void postRequest(Urls url, HashMap<String, String> body, final ConnectionDelegate connectionDelegate) {

        AndroidNetworking.post(url.getLink())
                .addHeaders("Authorization", SharedPreferencesUtils.getToken())
                .addHeaders("language", SharedPreferencesUtils.getLanguage(Atcine.getAppContext()))
                .addBodyParameter(body)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (connectionDelegate != null) {
                            if (response.optInt("status_code") == 200) {
                                connectionDelegate.onConnectionSuccess(response.optJSONObject("data"));
                            } else
                                connectionDelegate.onConnectionError(response.optInt("status_code"), response.optString("message"));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }

    public static void postRequest(String url, final ConnectionDelegate connectionDelegate) {

        AndroidNetworking.post(url)
                .addHeaders("Authorization", SharedPreferencesUtils.getToken())
                .addHeaders("language", SharedPreferencesUtils.getLanguage(Atcine.getAppContext()))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (connectionDelegate != null) {
                            if (response.optInt("status_code") == 200) {
                                connectionDelegate.onConnectionSuccess(response.optJSONObject("data"));
                            } else
                                connectionDelegate.onConnectionError(response.optInt("status_code"), response.optString("message"));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }

    public static void getRequest(Urls url, HashMap<String, String> pathParameters, final ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(url.getLink())
                .addHeaders("Authorization", SharedPreferencesUtils.getToken())
                .addHeaders("language", SharedPreferencesUtils.getLanguage(Atcine.getAppContext()))
                .addPathParameter(pathParameters)
                .addQueryParameter(pathParameters)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (connectionDelegate != null) {
                            if (response.optInt("status_code") == 200) {
                                connectionDelegate.onConnectionSuccess(response.optJSONObject("data"));
                            } else
                                connectionDelegate.onConnectionError(response.optInt("status_code"), response.optString("message"));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }

    public static void getRequest(String url, final ConnectionDelegate connectionDelegate) {

        AndroidNetworking.get(url)
                .addHeaders("Authorization", SharedPreferencesUtils.getToken())
                .addHeaders("language", SharedPreferencesUtils.getLanguage(Atcine.getAppContext()))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (connectionDelegate != null) {
                            if (response.optInt("status_code") == 200) {
                                connectionDelegate.onConnectionSuccess(response.optJSONObject("data"));
                            } else
                                connectionDelegate.onConnectionError(response.optInt("status_code"), response.optString("message"));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }

    public static void uploadRequest(String url, File file, HashMap<String, String> body, final ConnectionDelegate connectionDelegate) {

        AndroidNetworking.upload(url)
                .addHeaders("Authorization", SharedPreferencesUtils.getToken())
                .addHeaders("language", SharedPreferencesUtils.getLanguage(Atcine.getAppContext()))
                .addMultipartFile("file", file)
                .addMultipartParameter(body)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        long x = totalBytes-bytesUploaded;
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (connectionDelegate != null) {
                            if (response.optInt("status_code") == 200) {
                                connectionDelegate.onConnectionSuccess(response.optJSONObject("data"));
                            } else
                                connectionDelegate.onConnectionError(response.optInt("status_code"), response.optString("message"));
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (connectionDelegate != null)
                            connectionDelegate.onConnectionError(anError);
                    }
                });
    }

}

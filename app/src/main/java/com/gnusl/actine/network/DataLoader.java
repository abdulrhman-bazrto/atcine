package com.gnusl.actine.network;

import android.content.Intent;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.gnusl.actine.application.Atcine;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.DownloadDelegate;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.util.Connectivity;
import com.gnusl.actine.util.SharedPreferencesUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DataLoader {

    private static Map<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", SharedPreferencesUtils.getToken());
        headers.put("language", SharedPreferencesUtils.getLanguage(Atcine.getAppContext()));
        headers.put("Accept", "application/json");
        headers.put("profile_id", String.valueOf(SharedPreferencesUtils.getCurrentProfile()));

        return headers;
    }

    public static void postRequest(String url, HashMap<String, String> body, final ConnectionDelegate connectionDelegate) {

        if (!Connectivity.isConnected(Atcine.getApplicationInstance())) {
            if (connectionDelegate != null)
                connectionDelegate.onConnectionError(-5000, "No Internet Connection");
            return;
        }

        AndroidNetworking.post(url)
                .addHeaders(getHeaders())
                .addBodyParameter(body)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (connectionDelegate != null) {
                            if (response.optInt("status_code") == 200) {
                                connectionDelegate.onConnectionSuccess(response.optJSONObject("data"));
                            } else {

                                if (response.optInt("status_code") == 401) {
                                    if (SharedPreferencesUtils.getUser() != null) {
                                        SharedPreferencesUtils.clear();
                                        Intent dialogIntent = new Intent(Atcine.getApplicationInstance(), MainActivity.class);
                                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Atcine.getApplicationInstance().startActivity(dialogIntent);
                                    }
                                }

                                connectionDelegate.onConnectionError(response.optInt("status_code"), response.optString("message"));
                            }
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

        if (!Connectivity.isConnected(Atcine.getApplicationInstance())) {
            if (connectionDelegate != null)
                connectionDelegate.onConnectionError(-5000, "No Internet Connection");
            return;
        }

        AndroidNetworking.post(url)
                .addHeaders(getHeaders())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (connectionDelegate != null) {
                            if (response.optInt("status_code") == 200) {
                                connectionDelegate.onConnectionSuccess(response.optJSONObject("data"));
                            } else {
                                if (response.optInt("status_code") == 401) {
                                    if (SharedPreferencesUtils.getUser() != null) {
                                        SharedPreferencesUtils.clear();
                                        Intent dialogIntent = new Intent(Atcine.getApplicationInstance(), MainActivity.class);
                                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Atcine.getApplicationInstance().startActivity(dialogIntent);
                                    }
                                }
                                connectionDelegate.onConnectionError(response.optInt("status_code"), response.optString("message"));
                            }
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

        if (!Connectivity.isConnected(Atcine.getApplicationInstance())) {
            if (connectionDelegate != null)
                connectionDelegate.onConnectionError(-5000, "No Internet Connection");
            return;
        }

        AndroidNetworking.get(url.getLink())
                .addHeaders(getHeaders())
                .addPathParameter(pathParameters)
                .addQueryParameter(pathParameters)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (connectionDelegate != null) {
                            if (response.optInt("status_code") == 200) {
                                connectionDelegate.onConnectionSuccess(response.optJSONObject("data"));
                            } else {
                                if (response.optInt("status_code") == 401) {
                                    if (SharedPreferencesUtils.getUser() != null) {
                                        SharedPreferencesUtils.clear();
                                        Intent dialogIntent = new Intent(Atcine.getApplicationInstance(), MainActivity.class);
                                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Atcine.getApplicationInstance().startActivity(dialogIntent);
                                    }
                                }
                                connectionDelegate.onConnectionError(response.optInt("status_code"), response.optString("message"));
                            }
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

        if (!Connectivity.isConnected(Atcine.getApplicationInstance())) {
            if (connectionDelegate != null)
                connectionDelegate.onConnectionError(-5000, "No Internet Connection");
            return;
        }

        AndroidNetworking.get(url)
                .addHeaders(getHeaders())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (connectionDelegate != null) {
                            if (response.optInt("status_code") == 200) {
                                connectionDelegate.onConnectionSuccess(response.optJSONObject("data"));
                            } else {
                                if (response.optInt("status_code") == 401) {
                                    if (SharedPreferencesUtils.getUser() != null) {
                                        SharedPreferencesUtils.clear();
                                        Intent dialogIntent = new Intent(Atcine.getApplicationInstance(), MainActivity.class);
                                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Atcine.getApplicationInstance().startActivity(dialogIntent);
                                    }
                                }

                                connectionDelegate.onConnectionError(response.optInt("status_code"), response.optString("message"));
                            }
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

        if (!Connectivity.isConnected(Atcine.getApplicationInstance())) {
            if (connectionDelegate != null)
                connectionDelegate.onConnectionError(-5000, "No Internet Connection");
            return;
        }

        AndroidNetworking.upload(url)
                .addHeaders(getHeaders())
                .addMultipartFile("file", file)
                .addMultipartParameter(body)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        long x = totalBytes - bytesUploaded;
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

    public static void downloadRequest(String url, final String fileDir, final String fileName, final DownloadDelegate downloadDelegate) {


        AndroidNetworking.download(url, fileDir, fileName)
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        if (downloadDelegate != null) {
                            float p = ((float) bytesDownloaded / totalBytes);
                            int progress = (int) (p * 100);
                            downloadDelegate.onDownloadProgress(fileDir, fileName, progress);
                        }
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        if (downloadDelegate != null)
                            downloadDelegate.onDownloadSuccess(fileDir, fileName);
                    }

                    @Override
                    public void onError(ANError error) {
                        if (downloadDelegate != null)
                            downloadDelegate.onDownloadError(error);
                    }
                });
    }
}

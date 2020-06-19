package com.gnusl.actine.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.WebViewOnFinish;
import com.gnusl.actine.network.MyWebViewClient;
import com.gnusl.actine.ui.custom.LoaderPopUp;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class PaymentActivity extends AppCompatActivity implements WebViewOnFinish, ConnectionDelegate {

    private WebView webView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        webView = findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();

        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webSettings.setSafeBrowsingEnabled(false);
        }


        LoaderPopUp.show(this);

        webView.setWebViewClient(new MyWebViewClient(this));
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    public void onFinish(String url) {

        LoaderPopUp.dismissLoader();

        if (url.contains("/success")) {
            webView.evaluateJavascript("(function(){return JSON.parse(window.document.body.innerText)})();",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String html) {
                            try {
                                JSONObject jsonObject = new JSONObject(html);
                                if (jsonObject.optInt("status_code") == 200) {
                                    onConnectionSuccess(jsonObject);
                                } else {
                                    onConnectionError(jsonObject.optInt("status_code"), jsonObject.optString("message"));
                                }
                            } catch (JSONException e) {
                                onConnectionError(null);
                            }
                        }
                    });
        }
        if (url.contains("/error")) {
            webView.evaluateJavascript("(function(){return JSON.parse(window.document.body.innerText)})();",
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String html) {
                            try {
                                JSONObject jsonObject = new JSONObject(html);
                                if (jsonObject.optInt("status_code") == 200) {
                                    onConnectionSuccess(jsonObject);
                                } else {
                                    onConnectionError(jsonObject.optInt("status_code"), jsonObject.optString("message"));
                                }
                            } catch (JSONException e) {
                                onConnectionError(null);
                            }
                        }
                    });
        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onConnectionError(ANError anError) {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        Intent intent = new Intent();
        intent.putExtra("paymentId",jsonObject.optJSONObject("data").optString("payment_id"));
        setResult(RESULT_OK, intent);
        finish();
    }
}

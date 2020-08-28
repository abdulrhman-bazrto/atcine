package com.gnusl.actine.ui.Mobile.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.gnusl.actine.R;
import com.gnusl.actine.application.Atcine;
import com.gnusl.actine.interfaces.WebViewOnFinish;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.MyWebViewClient;
import com.gnusl.actine.ui.Mobile.custom.LoaderPopUp;
import com.gnusl.actine.util.SharedPreferencesUtils;

import java.util.Locale;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AccountActivity extends AppCompatActivity implements WebViewOnFinish {

    private WebView webView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String languageToLoad = SharedPreferencesUtils.getLanguage(Atcine.getApplicationInstance());
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

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
        webView.loadUrl(getIntent().getStringExtra("url"), DataLoader.getHeaders());
    }

    @Override
    public void onFinish(String url) {
        LoaderPopUp.dismissLoader();
    }

//    @Override
//    public void onConnectionError(int code, String message) {
//        Intent intent = new Intent();
//        setResult(RESULT_CANCELED, intent);
//        finish();
//    }
//
//    @Override
//    public void onConnectionError(ANError anError) {
//        Intent intent = new Intent();
//        setResult(RESULT_CANCELED, intent);
//        finish();
//    }
//
//    @Override
//    public void onConnectionSuccess(JSONObject jsonObject) {
//        Intent intent = new Intent();
//        intent.putExtra("paymentId",jsonObject.optJSONObject("data").optString("payment_id"));
//        setResult(RESULT_OK, intent);
//        finish();
//    }
}

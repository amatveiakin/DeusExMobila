package ru.rpg.deusexmobila;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class WebAppInterface {
    WebAppInterface(Activity activity, WebView webView) {
        mActivity = activity;
        mWebView = webView;
    }

    // For debug only! Don't use this to notify players.
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mActivity, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void showNotification(String message) {
        // TODO
    }

    @JavascriptInterface
    public void executeDelayed(final String what, long delayMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl("javascript: " + what);
                    }
                });
            }
        }, delayMillis);
    }

    @JavascriptInterface
    public void storeData(int value) {
        SharedPreferences preferences = mActivity.getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("value", value);
        editor.apply();
    }

    @JavascriptInterface
    public int loadData() {
        SharedPreferences preferences = mActivity.getSharedPreferences("data", MODE_PRIVATE);
        return preferences.getInt("value", 0);
    }

    Activity mActivity;
    WebView mWebView;
}

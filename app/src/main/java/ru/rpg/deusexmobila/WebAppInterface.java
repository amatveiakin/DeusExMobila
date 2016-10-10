package ru.rpg.deusexmobila;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;
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
    public void showNotification(int id, String title, String text, boolean canRemove) {
        Notification notification = new NotificationCompat.Builder(mActivity)
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setContentTitle(title)
                .setContentText(text)
                .build();
        if (!canRemove)
            notification.flags |= Notification.FLAG_NO_CLEAR;
        NotificationManager notificationManager =
                (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    @JavascriptInterface
    public void cancelNotification(int id) {
        NotificationManager notificationManager =
                (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
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

package com.p5m.me.utils;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.p5m.me.R;


/**
 * Created by MyU10 on 1/18/2017.
 */

public class ToastUtils {
    public static boolean showServerMessage = true;

    public static void showFailureResponse(Context context, int messageResource) {
        showFailureResponse(context, context.getString(messageResource));
    }

    public static void showFailureResponse(Context context, String message) {
        if (message.toLowerCase().equals("internet_error")) {
            message = context.getString(R.string.no_internet);
        }

        if (showServerMessage && !message.isEmpty()) {
            showToast(context, message, Toast.LENGTH_SHORT);
        } else {
//            showToast(context, context.getResources().getString(R.string.please_try_later), Toast.LENGTH_SHORT);
        }
    }

    public static void show(Context context, String message, int duration) {
        showToast(context, message, duration);
    }

    public static void show(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    public static void show(Context context, int messageResId) {
        showToast(context, context.getString(messageResId), Toast.LENGTH_SHORT);
    }

    public static void showInternetFailure(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String message, int duration) {
        if (message.isEmpty()) {
            return;
        }
        Toast toast = Toast.makeText(context, message, duration);

        TextView textView = new TextView(context);
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        textView.setPadding(dp * 16, dp * 12, dp * 16, dp * 12);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        textView.setText(message);
        textView.setBackgroundResource(R.drawable.toast_rect);

        toast.setView(textView);
//        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public static void showSnackbar(View view, String msg ){
        Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}


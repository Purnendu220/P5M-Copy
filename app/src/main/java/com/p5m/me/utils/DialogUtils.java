package com.p5m.me.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.view.activity.Main.SettingActivity;

import java.util.List;


public class DialogUtils {

    public static void showBasic(Context context,
                                 String message,
                                 String positiveButtonText,
                                 MaterialDialog.SingleButtonCallback singleButtonCallbackForPositive) {
        showBasic(context, message, positiveButtonText, context.getString(R.string.cancel), singleButtonCallbackForPositive);
    }

    public static void showBasic(Context context,
                                 String message,
                                 String positiveButtonText,
                                 String negativeButtonText,
                                 MaterialDialog.SingleButtonCallback singleButtonCallbackForPositive) {

        TextView textView = new TextView(context);
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        textView.setPadding(dp * 20, dp * 20, dp * 16, dp * 8);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_dark_text));
        textView.setText(message);
        textView.setBackgroundColor(Color.WHITE);

        new MaterialDialog.Builder(context)
                .cancelable(false)
                .customView(textView, false)
                .positiveText(positiveButtonText)
                .onPositive(singleButtonCallbackForPositive)
                .negativeText(negativeButtonText)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public static void showBasicMessage(Context context,
                                        String title,
                                        String message) {
        showBasicMessage(context, title, null, message);
    }

    public static void showBasicMessage(Context context,
                                        String title,
                                        String message,
                                        String buttonText1,
                                        MaterialDialog.SingleButtonCallback singleButtonCallback1,
                                        String buttonText2,
                                        MaterialDialog.SingleButtonCallback singleButtonCallback2) {

        TextView textView = new TextView(context);
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        textView.setPadding(dp * 20, dp * 20, dp * 16, dp * 8);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_dark_text));
        textView.setText(message);
        textView.setBackgroundColor(Color.WHITE);

        new MaterialDialog.Builder(context)
                .cancelable(false)
                .customView(textView, false)
                .positiveText(buttonText1)
                .onPositive(singleButtonCallback1)
                .negativeText(buttonText2)
                .onNegative(singleButtonCallback2)
                .show();
    }

    public static void showBasicMessage(Context context,
                                        String title,
                                        int text) {
        showBasicMessage(context, title, null, context.getString(text));
    }

    public static void showBasicMessage(Context context,
                                        String title,
                                        String buttonText,
                                        int text) {
        showBasicMessage(context, title, buttonText, context.getString(text));
    }

    public static void showBasicMessage(Context context,
                                        String title,
                                        String buttonText,
                                        String message) {
        if (buttonText == null) {
            buttonText = context.getString(R.string.ok);
        }

        TextView textView = new TextView(context);
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        textView.setPadding(dp * 20, dp * 20, dp * 16, dp * 8);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_dark_text));
        textView.setText(message);
        textView.setBackgroundColor(Color.WHITE);

        new MaterialDialog.Builder(context)
                .title(title)
                .customView(textView, false)
                .positiveText(buttonText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();

                    }
                })
                .show();
    }

    public static void showBasicMessage(Context context,
                                        String message,
                                        String buttonText,
                                        MaterialDialog.SingleButtonCallback singleButtonCallback) {

        TextView textView = new TextView(context);
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        textView.setPadding(dp * 20, dp * 20, dp * 16, dp * 8);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_dark_text));
        textView.setText(message);
        textView.setBackgroundColor(Color.WHITE);

        new MaterialDialog.Builder(context)
                .customView(textView, false)
                .cancelable(true)
                .positiveText(buttonText)
                .onPositive(singleButtonCallback)
                .show();
    }
    public static void showBasicMessage(Context context,
                                        String message,
                                        String buttonText,
                                        MaterialDialog.SingleButtonCallback singleButtonCallback,boolean isCancelable) {

        TextView textView = new TextView(context);
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        textView.setPadding(dp * 20, dp * 20, dp * 16, dp * 8);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_dark_text));
        textView.setText(message);
        textView.setBackgroundColor(Color.WHITE);

        new MaterialDialog.Builder(context)
                .customView(textView, false)
                .cancelable(isCancelable)
                .positiveText(buttonText)
                .onPositive(singleButtonCallback)
                .show();
    }

    public static void showBasicMessage(Context context,
                                        String title,
                                        String message,
                                        String buttonText,
                                        MaterialDialog.SingleButtonCallback singleButtonCallback) {

        TextView textView = new TextView(context);
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        textView.setPadding(dp * 20, dp * 20, dp * 16, dp * 8);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_dark_text));
        textView.setText(message);
        textView.setBackgroundColor(Color.WHITE);

        new MaterialDialog.Builder(context)
                .cancelable(false)
                .title(title)
                .customView(textView, false)
                .positiveText(buttonText)
                .onPositive(singleButtonCallback)
                .show();
    }
    public static void showBasicMessageCancelableFalse(Context context,
                                                       String message,
                                                       String buttonText,
                                                       MaterialDialog.SingleButtonCallback singleButtonCallback) {

        TextView textView = new TextView(context);
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        textView.setPadding(dp * 20, dp * 20, dp * 16, dp * 8);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_dark_text));
        textView.setText(message);
        textView.setBackgroundColor(Color.WHITE);

        new MaterialDialog.Builder(context)
                .customView(textView, false)
                .cancelable(false)
                .positiveText(buttonText)
                .onPositive(singleButtonCallback)
                .show();
    }

    public static void showBasicList(Context context,
                                     String title,
                                     List<String> items,
                                     MaterialDialog.ListCallback listCallback) {

        new MaterialDialog.Builder(context)
                .title(title)
                .items(items)
                .itemsCallback(listCallback)
                .negativeText(context.getString(R.string.cancel))
                .dividerColorRes(R.color.separator)
                .itemsGravity(GravityEnum.CENTER)
                .titleGravity(GravityEnum.CENTER)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void showPermissionMessage(Context context,
                                      String title,
                                      String msg,
                                      final DialogUtils.OnClickListener onClickListener) {
        new MaterialDialog.Builder(context)
                .title(title)

                .negativeText(context.getString(R.string.cancel))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        onClickListener.onNegativeClick();
                    }
                }) .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        onClickListener.onPositiveClick();
                    }
                })
                .show();
    }

    public static void showPermissionImportantAlert(final Activity context, String message){
        DialogUtils.showBasicMessage(context,context.getResources().getString(R.string.permission_alert), message,
                context.getResources().getString(R.string.go_to_settings), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + context.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        context.startActivity(i);
                        dialog.dismiss();
                    }
                },context.getResources().getString(R.string.cancel), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
    }

    interface OnClickListener {
        void onPositiveClick();
        void onNegativeClick();
    }


}

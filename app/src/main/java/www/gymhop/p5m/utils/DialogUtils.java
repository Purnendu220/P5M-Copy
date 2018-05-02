package www.gymhop.p5m.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import www.gymhop.p5m.R;

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
        new MaterialDialog.Builder(context)
                .content(message)
                .cancelable(false)
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
            buttonText = context.getString(R.string.cancel);
        }

        new MaterialDialog.Builder(context)
                .title(title)
                .content(message)
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

        new MaterialDialog.Builder(context)
                .content(message)
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
}

package www.gymhop.p5m.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class DialogUtils {

    public static void showBasic(Context context,
                                 String message,
                                 String positiveButtonText,
                                 MaterialDialog.SingleButtonCallback singleButtonCallbackForPositive) {
        new MaterialDialog.Builder(context)
                .content(message)
                .cancelable(false)
                .positiveText(positiveButtonText)
                .onPositive(singleButtonCallbackForPositive)
                .negativeText("Cancel")
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
            buttonText = "Cancel";
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
}

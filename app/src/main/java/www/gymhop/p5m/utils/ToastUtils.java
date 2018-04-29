package www.gymhop.p5m.utils;

import android.content.Context;
import android.widget.Toast;

import www.gymhop.p5m.R;


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
            showToast(context, context.getResources().getString(R.string.please_try_later), Toast.LENGTH_SHORT);
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
        Toast.makeText(context, message, duration).show();
    }
}


package www.gymhop.p5m.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.DialogUtils;
import www.gymhop.p5m.view.activity.Main.ClassProfileActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;

/**
 * Created by Varun John on 4/19/2018.
 */

public class ClassListListenerHelper implements AdapterCallbacks, NetworkCommunicator.RequestListener {

    public Context context;
    public Activity activity;
    private final AdapterCallbacks adapterCallbacks;
    private int shownIn;

    public ClassListListenerHelper(Context context, Activity activity, int shownIn, AdapterCallbacks adapterCallbacks) {
        this.context = context;
        this.activity = activity;
        this.shownIn = shownIn;
        this.adapterCallbacks = adapterCallbacks;
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.imageViewOptions1:
            case R.id.imageViewOptions2:
                if (model instanceof ClassModel) {
                    ClassModel classModel = (ClassModel) model;
                    if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
                        dialogOptionsRemove(context, ((BaseActivity) activity).networkCommunicator, view, classModel);
                    } else {
                        dialogOptionsAdd(context, ((BaseActivity) activity).networkCommunicator, view, classModel);
                    }
                }
                break;
            case R.id.buttonJoin:
            default:
                if (model instanceof ClassModel) {
                    ClassModel classModel = (ClassModel) model;
                    ClassProfileActivity.open(context, classModel);
                }
                break;
        }
    }

    public static void dialogOptionsAdd(Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model) {
        final View viewRoot = LayoutInflater.from(context).inflate(R.layout.popup_options, null);
        TextView textView = (TextView) viewRoot.findViewById(R.id.textViewOption1);
        textView.setText(context.getString(R.string.add_to_WishList));

        final PopupWindow popupWindow = new PopupWindow(viewRoot, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                networkCommunicator.addToWishList(model, model.getClassSessionId());
            }
        });

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void dialogOptionsRemove(final Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model) {
        final View viewRoot = LayoutInflater.from(context).inflate(R.layout.popup_options, null);
        TextView textView = (TextView) viewRoot.findViewById(R.id.textViewOption1);
        textView.setText(context.getString(R.string.remove_WishList));

        final PopupWindow popupWindow = new PopupWindow(viewRoot, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                dialogConfirmDelete(context, networkCommunicator, model);
            }
        });

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private static void dialogConfirmDelete(Context context, final NetworkCommunicator networkCommunicator, final ClassModel model) {

        DialogUtils.showBasic(context, "Are you sure want to remove \"" + model.getTitle() + "\" from your WishList", "Delete", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                networkCommunicator.removeFromWishList(model);
            }
        });
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {
        adapterCallbacks.onShowLastItem();
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
//        switch (requestCode) {
//            case NetworkCommunicator.RequestCode.ADD_TO_WISH_LIST:
//                ToastUtils.showLong(context, "Added to your Wish list");
//                EventBroadcastHelper.sendWishAdded();
//                break;
//        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
//        switch (requestCode) {
//            case NetworkCommunicator.RequestCode.ADD_TO_WISH_LIST:
//                ToastUtils.showLong(context, errorMessage);
//                break;
//        }
    }
}

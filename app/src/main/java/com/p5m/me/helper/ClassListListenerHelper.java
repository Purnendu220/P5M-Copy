package com.p5m.me.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.DefaultSettingServer;
import com.p5m.me.data.main.User;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.ClassProfileActivity;
import com.p5m.me.view.activity.base.BaseActivity;

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
                        popupOptionsRemove(context, ((BaseActivity) activity).networkCommunicator, view, classModel);
                    } else if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
                        popupOptionsCancelClass(context, ((BaseActivity) activity).networkCommunicator, view, classModel);
                    } else {
                        popupOptionsAdd(context, ((BaseActivity) activity).networkCommunicator, view, classModel);
                    }
                }
                break;
            case R.id.buttonJoin:
            default:
                if (shownIn != AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FINISHED) {
                    if (model instanceof ClassModel) {
                        ClassModel classModel = (ClassModel) model;
                        ClassProfileActivity.open(context, classModel);
                    }
                }
                break;
        }
    }

    public static void popupOptionsCancelClass(final Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model) {
        final View viewRoot = LayoutInflater.from(context).inflate(R.layout.popup_options, null);
        TextView textView = (TextView) viewRoot.findViewById(R.id.textViewOption1);
        textView.setText(context.getString(R.string.cancel_class));

        final PopupWindow popupWindow = new PopupWindow(viewRoot, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                dialogConfirmUnJoin(context, networkCommunicator, model);
            }
        });

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void popupOptionsAdd(Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model) {
        if (model.isUserJoinStatus()) {
            return;
        }

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
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void popupOptionsRemove(final Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model) {
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
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private static void dialogConfirmUnJoin(final Context context, final NetworkCommunicator networkCommunicator, final ClassModel model) {

        String message = "Are you sure want to unjoin ?";

        String serverMessageNormalClass = message;
        String serverMessageSpecialClass = message;
        float cancelTime = 2;

        DefaultSettingServer defaultSettingServer = MyPreferences.getInstance().getDefaultSettingServer();
        if (defaultSettingServer != null) {
            cancelTime = defaultSettingServer.getRefundAllowedbefore();
            serverMessageNormalClass = defaultSettingServer.getCancellationPolicy();
            serverMessageSpecialClass = defaultSettingServer.getSpecialClassCancellationPolicy();

        }

        if (Helper.isSpecialClass(model) && !Helper.isFreeClass(model)) {
            message = serverMessageSpecialClass;

        } else if (Helper.isSpecialClass(model) && Helper.isFreeClass(model)) {
            message = "Are you sure want to unjoin ?";

        } else if (DateUtils.hoursLeft(model.getClassDate() + " " + model.getFromTime()) <= cancelTime) {
            message = serverMessageNormalClass;
        }

        final MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                .cancelable(false)
                .customView(R.layout.dialog_unjoin_class, false)
                .build();
        materialDialog.show();

        final TextView textViewMessage = (TextView) materialDialog.findViewById(R.id.textViewMessage);
        final TextView textViewUnJoin = (TextView) materialDialog.findViewById(R.id.textViewOk);
        final TextView textViewClose = (TextView) materialDialog.findViewById(R.id.textViewCancel);

        textViewMessage.setText(message);

        textViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        });

        textViewUnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textViewUnJoin.setVisibility(View.GONE);
                networkCommunicator.unJoinClass(model, model.getJoinClassId(), new NetworkCommunicator.RequestListener() {
                    @Override
                    public void onApiSuccess(Object response, int requestCode) {

                        try {
                            model.setUserJoinStatus(false);
                            EventBroadcastHelper.sendClassJoin(context, model);
                            EventBroadcastHelper.sendUserUpdate(context, ((ResponseModel<User>) response).data);
                            materialDialog.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.exception(e);
                        }

                        textViewUnJoin.setVisibility(View.VISIBLE);
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onApiFailure(String errorMessage, int requestCode) {
                        textViewUnJoin.setVisibility(View.VISIBLE);

                        ToastUtils.showLong(context, errorMessage);
                        materialDialog.dismiss();
                    }
                });
            }
        });
    }

    private static void dialogConfirmDelete(Context context, final NetworkCommunicator networkCommunicator, final ClassModel model) {
        networkCommunicator.removeFromWishList(model);

//        DialogUtils.showBasic(context, "Are you sure you want to remove " + model.getTitle() + " from your WishList?", "Delete", new MaterialDialog.SingleButtonCallback() {
//            @Override
//            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//            }
//        });
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

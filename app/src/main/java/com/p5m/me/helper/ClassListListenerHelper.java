package com.p5m.me.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
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
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.DefaultSettingServer;
import com.p5m.me.data.main.User;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.CalendarHelper;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.ClassProfileActivity;
import com.p5m.me.view.activity.Main.ContactActivity;
import com.p5m.me.view.activity.Main.FullRatingActivity;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.CustomRateAlertDialog;

import java.util.Hashtable;

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
                        popupOptionsRemove(context, ((BaseActivity) activity).networkCommunicator, view, classModel, shownIn);
                    } else if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
                        if (classModel.getRefBookingId() != null && classModel.getRefBookingId() > 0) {
                            popupOptionsCancelClassBookedWithFriend(context, ((BaseActivity) activity).networkCommunicator, view, classModel);

                        } else {
                            popupOptionsCancelClass(context, ((BaseActivity) activity).networkCommunicator, view, classModel);

                        }
                    } else {
                        popupOptionsAdd(context, ((BaseActivity) activity).networkCommunicator, view, classModel, shownIn);
                    }
                }
                break;
            case R.id.buttonEditRating:
                ClassModel classData = (ClassModel) model;
                FullRatingActivity.open(context, shownIn, classData, 0);
                break;
            case R.id.buttonJoin:
                if (model instanceof ClassModel) {
                    ClassModel classModel = (ClassModel) model;
                    ClassProfileActivity.open(context, classModel, shownIn);
                }


                break;
            case R.id.buttonClassRating:
                if (shownIn == AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FINISHED && model instanceof ClassModel) {
                    openRateAlertDialog((ClassModel) model);
                }
                break;
            case R.id.imageViewChat:
                if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
                    if (model instanceof ClassModel) {
                        ClassModel classModel = (ClassModel) model;
                        ContactActivity.openActivity(context, classModel);
                    }
                }
                break;

            default:
                if (shownIn != AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FINISHED) {
                    if (model instanceof ClassModel) {
                        ClassModel classModel = (ClassModel) model;
                        ClassProfileActivity.open(context, classModel, shownIn);
                    }
                }
                break;
        }
    }

    public static void popupOptionsCancelClass(final Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model) {
        final View viewRoot = LayoutInflater.from(context).inflate(R.layout.popup_options, null);
        TextView textView = viewRoot.findViewById(R.id.textViewOption1);
        textView.setText(context.getString(R.string.cancel_class));

        final PopupWindow popupWindow = new PopupWindow(viewRoot, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                dialogConfirmUnJoin(context, networkCommunicator, model, model.getJoinClassId());
            }
        });

        TextView textViewOption2 = viewRoot.findViewById(R.id.textViewOption2);
//        textViewOption2.setText(context.getString(R.string.share));
        textViewOption2.setText(RemoteConfigConst.INVITE_FRIENDS_VALUE);

        textViewOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Helper.shareClass(context, model.getClassSessionId(), model.getTitle());
            }
        });

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void popupOptionsCancelClassBookedWithFriend(final Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model) {
        final View viewRoot = LayoutInflater.from(context).inflate(R.layout.popup_options, null);
        CardView cardView1 = viewRoot.findViewById(R.id.card_view);
        CardView cardViewBWF = viewRoot.findViewById(R.id.card_view_booked_with_friend);
        cardView1.setVisibility(View.GONE);
        cardViewBWF.setVisibility(View.VISIBLE);

        TextView textViewSelfUnjoin = viewRoot.findViewById(R.id.textViewSelfUnjoin);
        TextView textViewFriendUnjoin = viewRoot.findViewById(R.id.textViewFriendUnjoin);
        TextView textViewBothUnjoin = viewRoot.findViewById(R.id.textViewBothUnjoin);
        TextView textViewInviteFriend = viewRoot.findViewById(R.id.textViewInviteFriend);

        textViewSelfUnjoin.setText(context.getString(R.string.cancel_class_self));
        textViewFriendUnjoin.setText(context.getString(R.string.cancel_class_friend));
        textViewBothUnjoin.setText(context.getString(R.string.cancel_class_both));


        final PopupWindow popupWindow = new PopupWindow(viewRoot, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        textViewSelfUnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                dialogConfirmUnJoinBookWithFriend(context, networkCommunicator, model, model.getJoinClassId(), AppConstants.Values.UNJOIN_BOTH_CLASS);
            }
        });

        textViewFriendUnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                dialogConfirmUnJoinBookWithFriend(context, networkCommunicator, model, model.getRefBookingId(), AppConstants.Values.UNJOIN_FRIEND_CLASS);
            }
        });
        textViewBothUnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                dialogConfirmUnJoinBookWithFriend(context, networkCommunicator, model, model.getJoinClassId(), AppConstants.Values.UNJOIN_BOTH_CLASS);
            }
        });

//        textViewInviteFriend.setText(context.getString(R.string.share));
        textViewInviteFriend.setText(RemoteConfigConst.INVITE_FRIENDS_VALUE);

        textViewInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Helper.shareClass(context, model.getClassSessionId(), model.getTitle());
            }
        });

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void popupOptionsAdd(final Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model, final int shownIn) {

        final View viewRoot = LayoutInflater.from(context).inflate(R.layout.popup_options, null);
        TextView textView = viewRoot.findViewById(R.id.textViewOption1);
//        textView.setText(context.getString(R.string.add_to_WishList));
        textView.setText(RemoteConfigConst.ADD_TO_WISHLIST_VALUE);

        final PopupWindow popupWindow = new PopupWindow(viewRoot, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                networkCommunicator.addToWishList(model, model.getClassSessionId());

                MixPanel.trackAddWishList(shownIn, model);
            }
        });

        if (model != null && model.isUserJoinStatus()) {
            textView.setVisibility(View.GONE);
        }

        TextView textViewOption2 = viewRoot.findViewById(R.id.textViewOption2);
//        textViewOption2.setText(context.getString(R.string.invite_friends));
        textViewOption2.setText(RemoteConfigConst.INVITE_FRIENDS_VALUE);

        textViewOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Helper.shareClass(context, model.getClassSessionId(), model.getTitle());
            }
        });

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void popupOptionsRemove(final Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model, final int shownIn) {
        final View viewRoot = LayoutInflater.from(context).inflate(R.layout.popup_options, null);
        TextView textView = viewRoot.findViewById(R.id.textViewOption1);
        textView.setText(context.getString(R.string.remove_WishList));

        final PopupWindow popupWindow = new PopupWindow(viewRoot, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                dialogConfirmDelete(context, networkCommunicator, model, shownIn);
            }
        });

        TextView textViewOption2 = viewRoot.findViewById(R.id.textViewOption2);
//        textViewOption2.setText(context.getString(R.string.share));
        textViewOption2.setText(RemoteConfigConst.INVITE_FRIENDS_VALUE);

        textViewOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Helper.shareClass(context, model.getClassSessionId(), model.getTitle());
            }
        });

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private static void dialogConfirmUnJoin(final Context context, final NetworkCommunicator networkCommunicator, final ClassModel model, final int unJoinClassId) {

        String message = context.getString(R.string.sure_unjoin);

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
            message = context.getString(R.string.sure_unjoin);

        } else if (DateUtils.hoursLeft(
                model.getClassDate() + " " + model.getFromTime()) <= cancelTime) {
            message = serverMessageNormalClass;
        }

        final MaterialDialog materialDialog = new MaterialDialog.Builder(context )
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
                networkCommunicator.unJoinClass(model, unJoinClassId, new NetworkCommunicator.RequestListener() {
                    @Override
                    public void onApiSuccess(Object response, int requestCode) {

                        try {
                            model.setUserJoinStatus(false);
                            EventBroadcastHelper.sendClassJoin(context, model);
                            EventBroadcastHelper.sendUserUpdate(context, ((ResponseModel<User>) response).data);
                            MixPanel.trackUnJoinClass(AppConstants.Tracker.UP_COMING, model);
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





    private static void dialogConfirmUnJoinBookWithFriend(final Context context, final NetworkCommunicator networkCommunicator, final ClassModel model, final int unJoinClassId, final int unJoinType) {

        String message = context.getString(R.string.sure_unjoin);

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
            message = context.getString(R.string.sure_unjoin);

        } else if (DateUtils.hoursLeft(model.getClassDate() + " " + model.getFromTime()) <= cancelTime) {
            message = serverMessageNormalClass;
        } else if (unJoinType == AppConstants.Values.UNJOIN_BOTH_CLASS) {
            message = context.getString(R.string.sure_unjoin_both);

        } else if (unJoinType == AppConstants.Values.UNJOIN_FRIEND_CLASS) {
            message = context.getString(R.string.sure_unjoin_friend);

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
                networkCommunicator.unJoinClass(model, unJoinClassId, new NetworkCommunicator.RequestListener() {
                    @Override
                    public void onApiSuccess(Object response, int requestCode) {

                        try {
                            model.setUserJoinStatus(false);
                            EventBroadcastHelper.sendClassJoin(context, model);
                            EventBroadcastHelper.sendUserUpdate(context, ((ResponseModel<User>) response).data);
                            MixPanel.trackUnJoinClass(AppConstants.Tracker.UP_COMING, model);
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

    private static void dialogConfirmDelete(Context context, final NetworkCommunicator networkCommunicator, final ClassModel model, int shownIn) {
        networkCommunicator.removeFromWishList(model);

        if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
            MixPanel.trackRemoveWishList(AppConstants.Tracker.WISH_LIST, model);
        }

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

    private void openRateAlertDialog(ClassModel model) {
        if (model != null) {
            CustomRateAlertDialog mCustomMatchDialog = new CustomRateAlertDialog(context, model, AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FINISHED);
            try {
                mCustomMatchDialog.show();
                RefrenceWrapper.getRefrenceWrapper(context).setCustomRateAlertDialog(mCustomMatchDialog);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }


    }
}

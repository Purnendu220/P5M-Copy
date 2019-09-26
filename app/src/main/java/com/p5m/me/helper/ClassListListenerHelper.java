package com.p5m.me.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.Join5MinModel;
import com.p5m.me.data.WishListResponse;
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
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.ClassProfileActivity;
import com.p5m.me.view.activity.Main.ContactActivity;
import com.p5m.me.view.activity.Main.FullRatingActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.CustomAlertDialog;
import com.p5m.me.view.custom.CustomRateAlertDialog;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Varun John on 4/19/2018.
 */

public class ClassListListenerHelper implements AdapterCallbacks, NetworkCommunicator.RequestListener, CustomAlertDialog.OnAlertButtonAction {

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
//                    } else if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
                    } else if (((ClassModel) model).isUserJoinStatus()) {
                        if (classModel.getRefBookingId() != null && classModel.getRefBookingId() > 0) {
                            popupOptionsCancelClassBookedWithFriend(context, ((BaseActivity) activity).networkCommunicator, view, classModel);

                        } else {
                            popupOptionsCancelClass(context, ((BaseActivity) activity).networkCommunicator, view, classModel, true);

                        }
                    } else {
                        popupOptionsAdd(context, ((BaseActivity) activity).networkCommunicator, view, classModel, shownIn, viewHolder);
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
                    if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {

                        if (classModel.getRefBookingId() != null && classModel.getRefBookingId() > 0) {
//                            popupOptionsCancelClassBookedWithFriend(context, ((BaseActivity) activity).networkCommunicator, view, classModel);
                            CancelBookingBottomDialogFragment cancelBookingBottomDialogFragment =
                                    CancelBookingBottomDialogFragment.newInstance(classModel, true, this);
                            cancelBookingBottomDialogFragment.show(((HomeActivity) context).getSupportFragmentManager(),
                                    "cancel_friend_booking");

                        } else {
                            dialogConfirmUnJoin(context, ((BaseActivity) activity).networkCommunicator, classModel, classModel.getJoinClassId());

                        }
                    } else {
                        if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)
                                && !Helper.isMalesAllowed(classModel)) {
                            ToastUtils.show(context, context.getString(R.string.gender_females_only_error));
                            return;
                        } else if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)
                                && !Helper.isFemalesAllowed(classModel)) {
                            ToastUtils.show(context, context.getString(R.string.gender_males_only_error));
                            return;
                        }
                        if (classModel.getAvailableSeat() == 0 && classModel.isUserJoinStatus() == false) {
                            NetworkCommunicator.getInstance(context).addToWishList(classModel, classModel.getClassSessionId(), new NetworkCommunicator.RequestListener() {
                                @Override
                                public void onApiSuccess(Object response, int requestCode) {
                                    try {
                                        if (classModel.getAvailableSeat() == 0 && classModel.isUserJoinStatus() == false) {
                                            String message = context.getString(R.string.added_to_waitlist);
                                            classModel.setWishType(AppConstants.ApiParamKey.WAITLIST);
                                            classModel.setWishListId(((ResponseModel<WishListResponse>) response).data.getId());
                                            EventBroadcastHelper.waitlistClassJoin(context, classModel);
                                            EventBroadcastHelper.sendWishAdded(classModel);

                                            DialogUtils.showBasicMessage(context, message, context.getString(R.string.view_wishlist), new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    Intent navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_SCHEDULE, AppConstants.Tab.TAB_MY_SCHEDULE_WISH_LIST);
                                                    context.startActivity(navigationIntent);

                                                }
                                            });
                                        } else {
                                            String message = String.format(context.getString(R.string.added_to_wishlist), classModel.getTitle());
                                            ToastUtils.show(context, message);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        LogUtils.exception(e);
                                    }


                                }

                                @Override
                                public void onApiFailure(String errorMessage, int requestCode) {
                                    ToastUtils.show(context, errorMessage);
//
                                }
                            });
                        } else {
                            ClassProfileActivity.open(context, classModel, shownIn);
                        }
                    }
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
            case R.id.imageViewInviteFriend:
                if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
                    if (model instanceof ClassModel) {
                        ClassModel classModel = (ClassModel) model;

                        Helper.shareClass(context, classModel.getClassSessionId(), classModel.getTitle());

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

    public void popupOptionsCancelClass(final Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model, boolean isShowInviteFriends) {
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
        if (isShowInviteFriends)
            textViewOption2.setVisibility(View.VISIBLE);
        else
            textViewOption2.setVisibility(View.GONE);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.transparent));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void popupOptionsCancelClassBookedWithFriend(final Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model) {
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

    public static void popupOptionsAdd(final Context context, final NetworkCommunicator networkCommunicator, View view, final ClassModel model, final int shownIn, RecyclerView.ViewHolder viewHolder) {

        final View viewRoot = LayoutInflater.from(context).inflate(R.layout.popup_options, null);
        TextView textView = viewRoot.findViewById(R.id.textViewOption1);
//        textView.setText(context.getString(R.string.add_to_WishList));
        if (model.getAvailableSeat() == 0) {
            textView.setText(RemoteConfigConst.JOIN_WAITLIST_VALUE);
        } else
            textView.setText(RemoteConfigConst.ADD_TO_WISHLIST_VALUE);


        final PopupWindow popupWindow = new PopupWindow(viewRoot, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                // Check if class is allowed for the gender..
                if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)
                        && !Helper.isMalesAllowed(model)) {
                    ToastUtils.show(context, context.getString(R.string.gender_females_only_error));
                    return;
                } else if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)
                        && !Helper.isFemalesAllowed(model)) {
                    ToastUtils.show(context, context.getString(R.string.gender_males_only_error));
                    return;
                }
                networkCommunicator.addToWishList(model, model.getClassSessionId(), new NetworkCommunicator.RequestListener() {
                    @Override
                    public void onApiSuccess(Object response, int requestCode) {

                        try {
                            if (model.getAvailableSeat() == 0 && model.isUserJoinStatus() == false) {
                                String message = String.format(context.getString(R.string.added_to_waitlist));
//                                ToastUtils.show(context, message);
                                model.setWishType(AppConstants.ApiParamKey.WAITLIST);

                                model.setWishListId(((ResponseModel<WishListResponse>) response).data.getId());
                                EventBroadcastHelper.sendWishAdded(model);
                                EventBroadcastHelper.waitlistClassJoin(context, model);


                                DialogUtils.showBasicMessage(context, message, context.getString(R.string.view_wishlist), new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_SCHEDULE, AppConstants.Tab.TAB_MY_SCHEDULE_WISH_LIST);
                                        context.startActivity(navigationIntent);
//                                        Helper.setJoinButton(context, buttonJoin, model);
                                    }
                                });

                            } else {
                                String message = String.format(context.getString(R.string.added_to_wishlist), model.getTitle());
                                EventBroadcastHelper.sendWishAdded(model);
                                ToastUtils.show(context, message);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.exception(e);
                        }
                    }

                    @Override
                    public void onApiFailure(String errorMessage, int requestCode) {

                        ToastUtils.showLong(context, errorMessage);
                    }
                });

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

    public void dialogConfirmUnJoin(final Context context, final NetworkCommunicator networkCommunicator, final ClassModel model, final int unJoinClassId) {

        String message = context.getString(R.string.sure_unjoin);

        String serverMessageNormalClass = message;
        String serverMessageSpecialClass = message;
        String serverMessage5MinPolicy = context.getString(R.string.sure_unjoin);

        float cancelTime = 2;


        DefaultSettingServer defaultSettingServer = MyPreferences.getInstance().getDefaultSettingServer();
        if (defaultSettingServer != null) {
            if (Helper.isSpecialClass(model) && !Helper.isFreeClass(model)) {
                cancelTime = defaultSettingServer.getRefundAllowedbeforeForSpecial();


            } else {
                cancelTime = defaultSettingServer.getRefundAllowedbefore();

            }
            serverMessageNormalClass = defaultSettingServer.getCancellationPolicy();
            serverMessageSpecialClass = defaultSettingServer.getSpecialClassCancellationPolicy();

        }


        if (Helper.isSpecialClass(model) && !Helper.isFreeClass(model)) {
            if (DateUtils.hoursLeft(model.getClassDate() + " " + model.getFromTime()) <= cancelTime) {
                message = serverMessageSpecialClass;
                if (checkFor5MinDifference(model)) {
                    message = serverMessage5MinPolicy;
                }
            } else {
                message = context.getString(R.string.sure_unjoin);

            }

        } else if (Helper.isSpecialClass(model) && Helper.isFreeClass(model)) {
            message = context.getString(R.string.sure_unjoin);

        } else if (DateUtils.hoursLeft(
                model.getClassDate() + " " + model.getFromTime()) <= cancelTime) {
            message = serverMessageNormalClass;
            if (checkFor5MinDifference(model)) {
                message = serverMessage5MinPolicy;
            }
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
                            TempStorage.setUser(context, ((ResponseModel<User>) response).data);
                            EventBroadcastHelper.sendUserUpdate(context, ((ResponseModel<User>) response).data);
                            EventBroadcastHelper.sendClassJoin(context, model, AppConstants.Values.CHANGE_AVAILABLE_SEATS_FOR_MY_CLASS);
                            MixPanel.trackUnJoinClass(AppConstants.Tracker.UP_COMING, model);
                            FirebaseAnalysic.trackUnJoinClass(AppConstants.Tracker.UP_COMING, model);
                            IntercomEvents.trackUnJoinClass(model);
//
                            materialDialog.dismiss();
                            openAlertForRefund(model);
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

    private boolean checkFor5MinDifference(ClassModel model) {

        double cancel5min = 0.0833333;
        Boolean isClassBookedIn5Min = false;
        List<Join5MinModel> bookedList = MyPreferences.getInstance().getBookingTime();
        if(bookedList !=null){
        for (Join5MinModel bookedClass : bookedList) {
            if (bookedClass.getGetClassSessionId() == model.getClassSessionId()) {
                if ((DateUtils.find5MinDifference(bookedClass.getJoiningTime(), Calendar.getInstance().getTime())) <= cancel5min) {
                    isClassBookedIn5Min = true;
                }

            }
        }}
        return isClassBookedIn5Min;

    }


    public void dialogConfirmUnJoinBookWithFriend(final Context context, final NetworkCommunicator networkCommunicator, final ClassModel model, final int unJoinClassId, final int unJoinType) {

        String message = context.getString(R.string.sure_unjoin);

        String serverMessageNormalClass = message;
        String serverMessageSpecialClass = message;
        String serverMessage5MinPolicy = context.getString(R.string.sure_unjoin);
        float cancelTime = 2;

        DefaultSettingServer defaultSettingServer = MyPreferences.getInstance().getDefaultSettingServer();
        if (defaultSettingServer != null) {
            if (Helper.isSpecialClass(model) && !Helper.isFreeClass(model)) {
                cancelTime = defaultSettingServer.getRefundAllowedbeforeForSpecial();


            } else {
                cancelTime = defaultSettingServer.getRefundAllowedbefore();

            }

            serverMessageNormalClass = defaultSettingServer.getCancellationPolicy();
            serverMessageSpecialClass = defaultSettingServer.getSpecialClassCancellationPolicy();

        }


        if (Helper.isSpecialClass(model) && !Helper.isFreeClass(model)) {
            if (DateUtils.hoursLeft(model.getClassDate() + " " + model.getFromTime()) <= cancelTime) {
                message = serverMessageSpecialClass;
                if (checkFor5MinDifference(model)) {
                    message = serverMessage5MinPolicy;
                }

            } else {
                message = context.getString(R.string.sure_unjoin);

            }

        } else if (Helper.isSpecialClass(model) && Helper.isFreeClass(model)) {
            message = context.getString(R.string.sure_unjoin);

        } else if (DateUtils.hoursLeft(model.getClassDate() + " " + model.getFromTime()) <= cancelTime) {
            message = serverMessageNormalClass;
            if (checkFor5MinDifference(model)) {
                message = serverMessage5MinPolicy;
            }
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

                            if (unJoinType == AppConstants.Values.UNJOIN_BOTH_CLASS)
                                model.setUserJoinStatus(false);
                            else if (unJoinType == AppConstants.Values.UNJOIN_FRIEND_CLASS) {
                                model.setUserJoinStatus(true);
                                model.setRefBookingId(null);
                            }

                            if (unJoinType == AppConstants.Values.UNJOIN_BOTH_CLASS)
                                EventBroadcastHelper.sendClassJoin(context, model, AppConstants.Values.UNJOIN_BOTH_CLASS);
                            else if (unJoinType == AppConstants.Values.UNJOIN_FRIEND_CLASS)
                                EventBroadcastHelper.sendClassJoin(context, model, AppConstants.Values.UNJOIN_FRIEND_CLASS);
                            else
                                EventBroadcastHelper.sendClassJoin(context, model, AppConstants.Values.CHANGE_AVAILABLE_SEATS_FOR_MY_CLASS);
                            EventBroadcastHelper.sendUserUpdate(context, ((ResponseModel<User>) response).data);

                            MixPanel.trackUnJoinClass(AppConstants.Tracker.UP_COMING, model);
                            FirebaseAnalysic.trackUnJoinClass(AppConstants.Tracker.UP_COMING, model);
                            IntercomEvents.trackUnJoinClass(model);

                            materialDialog.dismiss();
                            openAlertForRefund(model);

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
            EventBroadcastHelper.waitlistClassRemove(context, model);

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

    private void openAlertForRefund(ClassModel model) {
        DefaultSettingServer defaultSettingServer = MyPreferences.getInstance().getDefaultSettingServer();
        float cancelTime = 2;
        if (defaultSettingServer != null) {
            cancelTime = defaultSettingServer.getRefundAllowedbeforeForSpecial();

        }
        if (Helper.isSpecialClass(model) && !Helper.isFreeClass(model) && DateUtils.hoursLeft(model.getClassDate() + " " + model.getFromTime()) > cancelTime) {
            CustomAlertDialog mCustomAlertDialog = new CustomAlertDialog(context, "", context.getString(R.string.successfull_refund_message), 1, context.getString(R.string.not_now), context.getString(R.string.yes), CustomAlertDialog.AlertRequestCodes.ALERT_REQUEST_SUCCESSFULL_UNJOIN, null, false, ClassListListenerHelper.this);
            try {
                mCustomAlertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onOkClick(int requestCode, Object data) {
        switch (requestCode) {
            case CustomAlertDialog.AlertRequestCodes.ALERT_REQUEST_SUCCESSFULL_UNJOIN:
                HomeActivity.show(context, AppConstants.Tab.TAB_MY_PROFILE);

                break;

        }
    }

    @Override
    public void onCancelClick(int requestCode, Object data) {
        switch (requestCode) {
            case CustomAlertDialog.AlertRequestCodes.ALERT_REQUEST_SUCCESSFULL_UNJOIN:
                break;

        }

    }


}

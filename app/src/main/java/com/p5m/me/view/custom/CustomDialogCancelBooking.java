package com.p5m.me.view.custom;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.p5m.me.R;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.Join5MinModel;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.DefaultSettingServer;
import com.p5m.me.data.main.User;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.ClassListListenerHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.HomeActivity;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CustomDialogCancelBooking extends Dialog implements OnClickListener, NetworkCommunicator.RequestListener, CustomAlertDialog.OnAlertButtonAction {


    private final int navigatinFrom;
    private final int unJoinClassId;
    private final int unJoinType;
    private final String message;
    private final NetworkCommunicator networkCommunicator;
    private ClassModel classModel;
    private Context mContext;
    @BindView(R.id.textViewCancelBookingMessage)
    TextView textViewCancelBookingMessage;

    @BindView(R.id.textViewNoRefund)
    TextView textViewNoRefund;

    @BindView(R.id.textViewNoThanks)
    TextView textViewNoThanks;

    @BindView(R.id.textViewCancelBooking)
    TextView textViewCancelBooking;

    public CustomDialogCancelBooking(@NonNull Context context, String message, ClassModel classModel, int unJoinClassId, int unJoinType, NetworkCommunicator networkCommunicator, int navigatinFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.navigatinFrom = navigatinFrom;
        this.classModel = classModel;
        this.unJoinClassId = unJoinClassId;
        this.unJoinType = unJoinType;
        this.message = message;
        this.networkCommunicator = networkCommunicator;
        init(context);
    }

    private void init(Context context) {
        setContentView(R.layout.view_cancel_booking_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setListeners();
        textViewCancelBookingMessage.setText(message);

    }

    private void setListeners() {
        textViewCancelBooking.setOnClickListener(this);
        textViewNoThanks.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewNoThanks:
                dismiss();
                break;

            case R.id.textViewCancelBooking:
                if (unJoinType == -1)
                    networkCommunicator.unJoinClass(classModel, unJoinClassId, this);
                else
                    networkCommunicator.unJoinClass(classModel, unJoinClassId, this);
                dismiss();
                break;
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UNJOIN_CLASS:
                handleUnjoinClass(response);
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UNJOIN_CLASS:
                ToastUtils.showLong(mContext, errorMessage);

                break;
        }
    }

    private void handleUnjoinClass(Object response) {

       if (unJoinType == -1) {
           onUnjoinSelfBooking(response);
       } else {
           onUnjoinFriendBooking(response);
       }
   }

   private void onUnjoinFriendBooking(Object response) {
       try {
           if (unJoinType == AppConstants.Values.UNJOIN_BOTH_CLASS)
               classModel.setUserJoinStatus(false);
           else if (unJoinType == AppConstants.Values.UNJOIN_FRIEND_CLASS) {
               classModel.setUserJoinStatus(true);
               classModel.setRefBookingId(null);
           }

           if (unJoinType == AppConstants.Values.UNJOIN_BOTH_CLASS)
               EventBroadcastHelper.sendClassJoin(mContext, classModel, AppConstants.Values.UNJOIN_BOTH_CLASS);
           else if (unJoinType == AppConstants.Values.UNJOIN_FRIEND_CLASS)
               EventBroadcastHelper.sendClassJoin(mContext, classModel, AppConstants.Values.UNJOIN_FRIEND_CLASS);
           else
               EventBroadcastHelper.sendClassJoin(mContext, classModel, AppConstants.Values.CHANGE_AVAILABLE_SEATS_FOR_MY_CLASS);
           EventBroadcastHelper.sendUserUpdate(mContext, ((ResponseModel<User>) response).data);
           MixPanel.trackUnJoinClass(AppConstants.Tracker.UP_COMING, classModel);
           FirebaseAnalysic.trackUnJoinClass(AppConstants.Tracker.UP_COMING, classModel);
           IntercomEvents.trackUnJoinClass(classModel);
           openAlertForRefund(classModel);

       } catch (Exception e) {
           e.printStackTrace();
           LogUtils.exception(e);
       }
   }

   private void onUnjoinSelfBooking(Object response) {
       try {
           classModel.setUserJoinStatus(false);
           TempStorage.setUser(mContext, ((ResponseModel<User>) response).data);
           EventBroadcastHelper.sendUserUpdate(mContext, ((ResponseModel<User>) response).data);
           EventBroadcastHelper.sendClassJoin(mContext, classModel, AppConstants.Values.CHANGE_AVAILABLE_SEATS_FOR_MY_CLASS);
           MixPanel.trackUnJoinClass(AppConstants.Tracker.UP_COMING, classModel);
           FirebaseAnalysic.trackUnJoinClass(AppConstants.Tracker.UP_COMING, classModel);
           IntercomEvents.trackUnJoinClass(classModel);

           openAlertForRefund(classModel);
       } catch (Exception e) {
           e.printStackTrace();
           LogUtils.exception(e);
       }
   }
    private void openAlertForRefund(ClassModel model) {
        DefaultSettingServer defaultSettingServer = MyPreferences.getInstance().getDefaultSettingServer();
        float cancelTime = 2;
        if (defaultSettingServer != null) {
            cancelTime = defaultSettingServer.getRefundAllowedbeforeForSpecial();

        }
        if (Helper.isSpecialClass(model) && !Helper.isFreeClass(model) && (DateUtils.hoursLeft(model.getClassDate() + " " + model.getFromTime()) > cancelTime ||
                checkFor5MinDifference(model))) {
            CustomAlertDialog mCustomAlertDialog = new CustomAlertDialog(mContext, "", mContext.getString(R.string.successfull_refund_message), 1, mContext.getString(R.string.not_now), mContext.getString(R.string.yes), CustomAlertDialog.AlertRequestCodes.ALERT_REQUEST_SUCCESSFULL_UNJOIN, null, false, CustomDialogCancelBooking.this);
            try {
                mCustomAlertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkFor5MinDifference(ClassModel model) {

        double cancel5min = 5;
        Boolean isClassBookedIn5Min = false;
        List<Join5MinModel> bookedList = MyPreferences.getInstance().getBookingTime();
        if (bookedList != null) {
            for (Join5MinModel bookedClass : bookedList) {
                if (bookedClass.getGetClassSessionId() == model.getClassSessionId()) {
                    if ((DateUtils.find5MinDifference(bookedClass.getJoiningTime(), Calendar.getInstance().getTime())) <= cancel5min) {
                        isClassBookedIn5Min = true;
                    }

                }
            }
        }
        return isClassBookedIn5Min;

    }

    @Override
    public void onOkClick(int requestCode, Object data) {
        switch (requestCode) {
            case CustomAlertDialog.AlertRequestCodes.ALERT_REQUEST_SUCCESSFULL_UNJOIN:
                HomeActivity.show(mContext, AppConstants.Tab.TAB_MY_PROFILE);
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
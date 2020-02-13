package com.p5m.me.view.custom;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.Join5MinModel;
import com.p5m.me.data.main.BookingCancellationResponse;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.DefaultSettingServer;
import com.p5m.me.data.main.StoreApiModel;
import com.p5m.me.data.main.User;
import com.p5m.me.eventbus.EventBroadcastHelper;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CustomFeedbackFormDialog extends Dialog implements OnClickListener, NetworkCommunicator.RequestListener, AdapterView.OnItemSelectedListener, CustomAlertDialog.OnAlertButtonAction {


    private final int navigatinFrom;
    private final NetworkCommunicator networkCommunicator;
    private final int unJoinClassId;
    private final ClassModel classModel;
    private final int unJoinType;
    private Context mContext;

    @BindView(R.id.textViewNoThanks)
    TextView textViewNoThanks;

    @BindView(R.id.textViewCancelBooking)
    TextView textViewCancelBooking;

    @BindView(R.id.spinner)
    public Spinner spinner;
    private ArrayList<String> cancelBookingReasons;
    private int position;
    private List<BookingCancellationResponse> feedbackModel;


    public CustomFeedbackFormDialog(@NonNull Context context, ClassModel model, int unJoinClassId,int unJoinType, NetworkCommunicator networkCommunicator, int navigatinFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.navigatinFrom = navigatinFrom;
        this.classModel = model;
        this.unJoinClassId = unJoinClassId;
        this.unJoinType = unJoinType;
        cancelBookingReasons = new ArrayList<String>();
        this.networkCommunicator = networkCommunicator;
        init();
    }

    private void init() {
        setContentView(R.layout.view_feedback_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setListeners();
        callApi();
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
                    networkCommunicator.unJoinClass(classModel, unJoinClassId,0,"", this);
                else
                    networkCommunicator.unJoinClass(classModel, unJoinClassId, 0,"",this);
                dismiss();
                break;
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {

            case NetworkCommunicator.RequestCode.GET_CANCELLATION_REASON:
                feedbackModel = ((ResponseModel<List<BookingCancellationResponse>>) response).data;
                setSpinnerView();
                break;

            case NetworkCommunicator.RequestCode.UNJOIN_CLASS:
                handleUnjoinClass(response);

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GET_CANCELLATION_REASON:
            case NetworkCommunicator.RequestCode.UNJOIN_CLASS:
                ToastUtils.showLong(mContext, errorMessage);

                break;
        }
    }

    private void callApi() {
        networkCommunicator.getCancellationReason(this, false);
    }

    private void setSpinnerView() {
        cancelBookingReasons.clear();
        cancelBookingReasons.add(mContext.getString(R.string.select_reason));
        for (BookingCancellationResponse data : feedbackModel) {
            cancelBookingReasons.add(data.getCancellationReason());
        }
        spinner.setOnItemSelectedListener(this);
//        cancelBookingReasons.add(mContext.getString(R.string.other_country));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                mContext, R.layout.view_spinner_item, cancelBookingReasons) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(0);

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if (position > 0) {
            this.position = position - 1;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            CustomAlertDialog mCustomAlertDialog = new CustomAlertDialog(mContext, "", mContext.getString(R.string.successfull_refund_message), 1, mContext.getString(R.string.not_now), mContext.getString(R.string.yes), CustomAlertDialog.AlertRequestCodes.ALERT_REQUEST_SUCCESSFULL_UNJOIN, null, false, CustomFeedbackFormDialog.this);
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
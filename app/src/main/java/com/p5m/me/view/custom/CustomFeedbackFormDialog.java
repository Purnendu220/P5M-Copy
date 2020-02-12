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


public class CustomFeedbackFormDialog extends Dialog implements OnClickListener, NetworkCommunicator.RequestListener, AdapterView.OnItemSelectedListener {


    private final int navigatinFrom;
    private final NetworkCommunicator networkCommunicator;
    private Context mContext;

    @BindView(R.id.textViewHeader)
    TextView textViewHeader;

    @BindView(R.id.textViewNoThanks)
    TextView textViewNoThanks;

    @BindView(R.id.textViewCancelBooking)
    TextView textViewCancelBooking;

    @BindView(R.id.textInputLayout)
    TextInputLayout textInputLayout;

    @BindView(R.id.spinner)
    public Spinner spinner;
    private ArrayList<String> cancelBookingReasons;
    private int position;
    private List<StoreApiModel> feedbackModel;


    public CustomFeedbackFormDialog(@NonNull Context context, NetworkCommunicator networkCommunicator, int navigatinFrom) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext = context;
        this.navigatinFrom = navigatinFrom;
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
                /*if (unJoinType == -1)
                    networkCommunicator.unJoinClass(classModel, unJoinClassId, this);
                else
                    networkCommunicator.unJoinClass(classModel, unJoinClassId, this);
                dismiss();*/
                break;
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {

            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                feedbackModel = ((ResponseModel<List<StoreApiModel>>) response).data;
                setSpinnerView();
                break;
        }
    }


    private void callApi() {
        networkCommunicator.getStoreData(this, false);
    }

    private void setSpinnerView() {
        cancelBookingReasons.clear();
        cancelBookingReasons.add(mContext.getString(R.string.select_city));
        for (StoreApiModel data : feedbackModel) {
            cancelBookingReasons.add(data.getName());
        }
        spinner.setOnItemSelectedListener(this);
        cancelBookingReasons.add(mContext.getString(R.string.other_country));

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
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UNJOIN_CLASS:
                ToastUtils.showLong(mContext, errorMessage);

                break;
        }
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

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }
}
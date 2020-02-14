package com.p5m.me.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.BookingCancellationResponse;
import com.p5m.me.data.main.StoreApiModel;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.LocationSelectionActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContinueUser extends BaseActivity implements View.OnClickListener, NetworkCommunicator.RequestListener {

    public static void open(Context context) {
        Intent intent = new Intent(context, ContinueUser.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @BindView(R.id.buttonContinue)
    public Button buttonContinue;
    @BindView(R.id.buttonLogin)
    public Button buttonLogin;
    @BindView(R.id.buttonRegister)
    public Button buttonRegister;
    @BindView(R.id.textViewSwitch)
    public TextView textViewSwitch;
    @BindView(R.id.imageView)
    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_user);

        ButterKnife.bind(activity);

        buttonContinue.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        textViewSwitch.setOnClickListener(this);
        if (TempStorage.getCountries() == null)
            networkCommunicator.getStoreData(this, false);

        try {
            ImageUtils.setImage(context, TempStorage.getUser().getProfileImage(), R.drawable.profile_holder, imageView);
            buttonContinue.setText(getString(R.string.continue_as) + " " + TempStorage.getUser().getFirstName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonContinue:

                MixPanel.trackPastLogin(AppConstants.Tracker.CONTINUE_AS);
                LoginActivity.open(context, AppConstants.AppNavigation.NAVIGATION_FROM_CONTINUE_USER);

                break;
            case R.id.buttonLogin:

                MixPanel.trackPastLogin(AppConstants.Tracker.LOGIN_BUTTON);
                LoginActivity.open(context);

                break;
            case R.id.buttonRegister:

                MixPanel.trackPastLogin(AppConstants.Tracker.REGISTER);
                LocationSelectionActivity.open(context);

                break;
            case R.id.textViewSwitch:

                MixPanel.trackPastLogin(AppConstants.Tracker.SWITCH_ACCOUNT_ACTION);
                LoginActivity.open(context);

                break;
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                List<StoreApiModel> model = ((ResponseModel<List<StoreApiModel>>) response).data;
                TempStorage.setCountries(model);
                break;

        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                ToastUtils.show(context, errorMessage);
                break;
        }
    }
}

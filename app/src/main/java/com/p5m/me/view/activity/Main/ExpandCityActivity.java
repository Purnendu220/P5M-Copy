package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.InterestedCityModel;
import com.p5m.me.data.main.InterestedCityRequestModel;
import com.p5m.me.data.main.StoreApiModel;
import com.p5m.me.data.main.User;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.LoginRegister.InfoScreen;
import com.p5m.me.view.activity.LoginRegister.LoginActivity;
import com.p5m.me.view.activity.LoginRegister.SignUpOptions;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.CustomDialogThankYou;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandCityActivity extends BaseActivity implements View.OnClickListener, NetworkCommunicator.RequestListener {


    public static void open(Context context, String countryName, int id, InterestedCityRequestModel interestedCityRequestModel) {
        Intent intent = new Intent(context, ExpandCityActivity.class);
        ExpandCityActivity.countryName = countryName;
        ExpandCityActivity.id = id;
        ExpandCityActivity.interestedCityRequestModel = interestedCityRequestModel;

        context.startActivity(intent);
    }

    @BindView(R.id.buttonSubmit)
    public Button buttonSubmit;
    @BindView(R.id.textViewLogin)
    public TextView textViewLogin;
    @BindView(R.id.textViewHeader)
    public TextView textViewHeader;
    @BindView(R.id.editTextEmail)
    public EditText editTextEmail;
    @BindView(R.id.imageViewBack)
    public ImageView imageViewBack;
    @BindView(R.id.textViewSkip)
    public TextView textViewSkip;
    @BindView(R.id.textInputLayoutEmail)
    public TextInputLayout textInputLayoutEmail;
    public static String countryName;
    private static int id;
    private CustomDialogThankYou mCustomThankyouDialog;
    private static InterestedCityRequestModel interestedCityRequestModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_city);
        ButterKnife.bind(activity);
        buttonSubmit.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
        textViewSkip.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);
        Helper.setupErrorWatcher(editTextEmail, textInputLayoutEmail);
        textViewHeader.setText(Html.fromHtml(String.format(mContext.getString(R.string.want_us_to_expand_in_your_city), countryName)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewLogin:
                LoginActivity.open(context);
                break;
            case R.id.buttonSubmit:
                if (!isError()) {
                    callInterestedCityApi();
                }
                break;
            case R.id.imageViewBack:
                onBackPressed();
                break;
            case R.id.textViewSkip:
                InfoScreen.open(context);
                finish();
                break;
        }
    }


    private boolean isError() {
        String email = editTextEmail.getText().toString().trim();
        if (email.isEmpty()) {
            textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required));
            return true;
        }

        if (!Helper.validateEmail(email)) {
            textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required_validate));
            return true;
        }
        interestedCityRequestModel.setUserEmail(email);
        return false;
    }

    private void showThankYou() {

        mCustomThankyouDialog = new CustomDialogThankYou(context, false, AppConstants.AppNavigation.NAVIGATION_FROM_OTHER_USER);
        try {
            mCustomThankyouDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void callInterestedCityApi() {
        networkCommunicator.uploadInsterestedCity(id ,interestedCityRequestModel,this,false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (mCustomThankyouDialog != null)
            mCustomThankyouDialog.dismiss();
        super.onPause();

    }
    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.INERESTED_CITY:
                InterestedCityModel interestedCityModel = ((ResponseModel<InterestedCityModel>)response).data;
                showThankYou();
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.INERESTED_CITY:
                ToastUtils.show(context, errorMessage);
                break;

        }
    }
}

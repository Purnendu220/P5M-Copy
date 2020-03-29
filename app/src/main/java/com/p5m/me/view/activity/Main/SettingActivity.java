package com.p5m.me.view.activity.Main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.FAQAdapter;
import com.p5m.me.R;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.data.main.StoreApiModel;
import com.p5m.me.data.main.StoreModel;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.LogoutRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.firebase_dynamic_link.FirebaseDynamicLinnk;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.CustomFeedbackFormDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intercom.android.sdk.Intercom;

public class SettingActivity extends BaseActivity implements View.OnClickListener, NetworkCommunicator.RequestListener, AdapterView.OnItemSelectedListener {

    private int pageToOpen;
    private List<StoreApiModel> countryModel;
    private ArrayList<String> categories;
    private Spinner spinnerCity;
    private String item;
    private int position;
    private boolean isSelectCountry = false;
    private int userCountryIdPosition;


    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    public static Intent createIntent(Context context, int pageToOpen) {
        return new Intent(context, SettingActivity.class)
                .putExtra(AppConstants.DataKey.PAGES_TO_OPEN, pageToOpen);
    }


    @BindView(R.id.layoutNotification)
    public View layoutNotification;
    @BindView(R.id.layoutMembership)
    public View layoutMembership;
    @BindView(R.id.layoutTransHistory)
    public View layoutTransHistory;

    @BindView(R.id.layoutContactUs)
    public View layoutContactUs;
    @BindView(R.id.layoutChangeCountry)
    public View layoutChangeCountry;
    @BindView(R.id.layoutPrivacyPolicy)
    public View layoutPrivacyPolicy;
    @BindView(R.id.layoutTermsCondition)
    public View layoutTermsCondition;
    @BindView(R.id.layoutAboutUs)
    public View layoutAboutUs;
    @BindView(R.id.layoutLogout)
    public View layoutLogout;

    @BindView(R.id.imageViewLogout)
    public View imageViewLogout;
    @BindView(R.id.progressBarLogout)
    public View progressBarLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(activity);
        pageToOpen = getIntent().getIntExtra(AppConstants.DataKey.PAGES_TO_OPEN, -1);
        FirebaseDynamicLinnk.getDynamicLink(this, getIntent());
        layoutNotification.setOnClickListener(this);
        layoutMembership.setOnClickListener(this);
        layoutTransHistory.setOnClickListener(this);
        layoutContactUs.setOnClickListener(this);
        layoutChangeCountry.setOnClickListener(this);
        layoutPrivacyPolicy.setOnClickListener(this);
        layoutTermsCondition.setOnClickListener(this);
        layoutAboutUs.setOnClickListener(this);
        layoutLogout.setOnClickListener(this);
        if (pageToOpen == AppConstants.Tab.OPEN_ABOUT_US) {
            Helper.openWebPage(context, AppConstants.Url.WEBSITE + "aboutus");

        }
        if (pageToOpen == AppConstants.Tab.OPEN_PRIVACY) {
            Helper.openWebPage(context, AppConstants.Url.WEBSITE + "privacy");

        }
        if (pageToOpen == AppConstants.Tab.OPEN_TERMS) {
            Helper.openWebPage(context, AppConstants.Url.WEBSITE + "terms");

        }
        categories = new ArrayList<String>();
        if (countryModel == null)
            callApi();

    }

    private void callApi() {
        networkCommunicator.getStoreData(this, false);
    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutNotification:
                SettingNotification.openActivity(context);
                break;
            case R.id.layoutMembership:
                HomeActivity.show(context, AppConstants.Tab.TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN);
                break;
            case R.id.layoutTransHistory:
                TransactionHistoryActivity.openActivity(context);
                break;
            case R.id.layoutContactUs:
                dialogContactUs();
                break;
            case R.id.layoutPrivacyPolicy:
                Helper.openWebPage(context, AppConstants.Url.WEBSITE + "privacy");
                break;
            case R.id.layoutTermsCondition:
                Helper.openWebPage(context, AppConstants.Url.WEBSITE + "terms");
                break;
            case R.id.layoutAboutUs:
                Helper.openWebPage(context, AppConstants.Url.WEBSITE + "aboutus");
                break;
            case R.id.layoutLogout:
                imageViewLogout.setVisibility(View.GONE);
                progressBarLogout.setVisibility(View.VISIBLE);
                networkCommunicator.logout(new LogoutRequest(TempStorage.getUser().getId()), this, false);
                break;
            case R.id.layoutChangeCountry:
                if (categories != null && countryModel != null)
                    openCountryChangeDialog();
                break;
        }
    }

    private void openCountryChangeDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_transparent)));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.view_spinner_dialog);
        spinnerCity = (Spinner) dialog.findViewById(R.id.spinnerCity);
        setSpinnerView();
        dialog.show();
        (dialog.findViewById(R.id.buttonSubmit)).setOnClickListener(v -> {
            networkCommunicator.updateStoreId(countryModel.get(position).getId(), this, false);
            dialog.dismiss();
        });

    }

    private void setSpinnerView() {
        categories.clear();
        for (StoreApiModel data : countryModel) {
            categories.add(data.getName());
        }
        spinnerCity.setOnItemSelectedListener(this);
        int userCountryId = TempStorage.getUser().getStoreId();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.view_spinner_item, categories);
        if (countryModel != null) {
            for (int i = 0; i < countryModel.size(); i++) {
                if (userCountryId == countryModel.get(i).getId())
                    userCountryIdPosition = i;

            }
            dataAdapter.setDropDownViewResource(R.layout.view_spinner_item);
            spinnerCity.setAdapter(dataAdapter);
            spinnerCity.setSelection(userCountryIdPosition);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        this.position = position;
        isSelectCountry = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        isSelectCountry = false;
    }

    private void dialogContactUs() {
        final List<String> items = new ArrayList<>();
        items.add(getString(R.string.mail_us));
        items.add(getString(R.string.message_us));

        DialogUtils.showBasicList(
                context,
                getString(R.string.contact_us),
                items,
                new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        dialog.dismiss();
                        if (position == 0) {
                            mailUs();
                        } else if (position == 1) {
                            /*Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:0096555028111"));
                            startActivity(intent);*/
                            Intercom.client().displayMessenger();
                        }
                    }
                });
    }

    private void mailUs() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "info@p5m.me", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please identify the details of your issue below. A member of our staff will respond shortly.");
        startActivity(Intent.createChooser(emailIntent, "Send Email"));

    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.LOGOUT:
                imageViewLogout.setVisibility(View.VISIBLE);
                progressBarLogout.setVisibility(View.GONE);
                EventBroadcastHelper.logout(context);
                break;
            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                countryModel = ((ResponseModel<List<StoreApiModel>>) response).data;

                break;
            case NetworkCommunicator.RequestCode.UPDATE_STORE_ID:
                StoreModel model = ((ResponseModel<StoreModel>) response).data;
                NetworkCommunicator.getInstance(context).getDefault();
                User user = TempStorage.getUser();
                user.setStoreId(model.getId());
                user.setCurrencyCode(model.getCurrencyCode());
                user.setStoreName(model.getName());
                IntercomEvents.updateStoreId();
                EventBroadcastHelper.sendUserUpdate(context, user);
                EventBroadcastHelper.changeCountry();
                HomeActivity.open(context);
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.LOGOUT:
                imageViewLogout.setVisibility(View.VISIBLE);
                progressBarLogout.setVisibility(View.GONE);
                ToastUtils.showLong(context, errorMessage);
                break;
            case NetworkCommunicator.RequestCode.UPDATE_STORE_ID:
//                ToastUtils.show(context, errorMessage);
                break;
            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                ToastUtils.show(context, errorMessage);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

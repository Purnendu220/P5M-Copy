package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.data.main.Country;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.UserInfoUpdate;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.LoginRegister.RegistrationActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindCountryActivity extends BaseActivity implements NetworkCommunicator.RequestListener, View.OnClickListener, AdapterView.OnItemSelectedListener {


    private List<Country> countryList;
    private int countryId = -1;


    public static void openActivity(Context context, int navigationFrom) {
        context.startActivity(new Intent(context, FindCountryActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom));
    }


    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.spinnerCountry)
    public Spinner spinnerCountry;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.buttonOk)
    public Button buttonOk;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    private UserInfoUpdate userInfoUpdate;
    private int navigatedFrom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_country);
        ButterKnife.bind(activity);
        navigatedFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);

        userInfoUpdate = new UserInfoUpdate(TempStorage.getUser().getId());
        setToolBar();
        callApiCountry();
        spinnerCountry.setOnItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setSpinner() {
        List<String> country = new ArrayList<String>();
        int selectedCountryIndex = 0;
        country.add(0, getString(R.string.select_country));
        if (TempStorage.getUser().getCountryId() != null && navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SETTING) {
            countryId = TempStorage.getUser().getCountryId();

        }
        for (int i = 0; i < countryList.size(); i++) {
            country.add(countryList.get(i).getName());
            if (countryId == countryList.get(i).getId()) {
                selectedCountryIndex = i + 1;
            }
        }

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this, R.layout.row_country, country);

        countryAdapter.setDropDownViewResource(R.layout.row_country);
        buttonOk.setOnClickListener(this);
        spinnerCountry.setAdapter(countryAdapter);
        if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SETTING) {
            if (countryId != 0)
                spinnerCountry.setSelection(selectedCountryIndex);
            else
                spinnerCountry.setSelection(R.string.select_country);

        }

    }

    private void setToolBar() {

        BaseActivity activity = (BaseActivity) this.activity;
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_normal, null);

        v.findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        ((TextView) v.findViewById(R.id.textViewTitle)).setText("");

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    private void callApiCountry() {
        progressBar.setVisibility(View.VISIBLE);
        networkCommunicator.getCountry("", this, false);
    }


    @Override
    public void onApiSuccess(Object response, int requestCode) {
        progressBar.setVisibility(View.GONE);
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.COUNTRY:
                countryList = ((ResponseModel<List<Country>>) response).data;
                setSpinner();
                break;
            case NetworkCommunicator.RequestCode.UPDATE_USER:
                User user = ((ResponseModel<User>) response).data;
                EventBroadcastHelper.sendUserUpdate(context, user);
                TempStorage.setFilterList(null);
                NetworkCommunicator.getInstance(context).getDefault();
                networkCommunicator.getMyUser(this, false);
                MyPreferences.getInstance().setCountryId(true);
                MyPreferences.getInstance().setUserGivebItsCountry(true);
                HomeActivity.open(context);
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        progressBar.setVisibility(View.GONE);
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.COUNTRY:
                ToastUtils.showLong(context, errorMessage);
                break;
            case NetworkCommunicator.RequestCode.UPDATE_USER:
                ToastUtils.showLong(context, errorMessage);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonOk:
             /*   Country selectedCountry = countryList.get(spinnerCountry.getSelectedItemPosition());
                int countryId = selectedCountry.getId();*/
             if(MyPreferences.getInstance().isLogin()){
                 userInfoUpdate.setCountryId(countryId);
                 networkCommunicator.userInfoUpdate(TempStorage.getUser().getId(), userInfoUpdate, this, false);
             }
             else {
                 if (countryId > -1) {
                     if (countryId > 0) {
                         MyPreferences.getInstance().saveCountryId(countryId);
                         RegistrationActivity.open(this);
                     }

                 } else {
                     ToastUtils.show(this, R.string.please_select_country);

                 }
             }

                break;


        }
    }


   /* private void showPermissionImportantAlert(String message) {
        DialogUtils.showBasicMessage(context, context.getResources().getString(R.string.permission_alert), message,
                context.getResources().getString(R.string.go_to_settings), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + context.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        context.startActivity(i);
                        dialog.dismiss();

                    }
                }, context.getResources().getString(R.string.cancel), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            countryId = countryList.get(i - 1).getId();

        } else {
            countryId = -1;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        countryId = -1;
    }



}

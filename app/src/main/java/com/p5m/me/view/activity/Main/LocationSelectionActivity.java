package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.data.main.StoreApiModel;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.RegistrationRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.LoginRegister.LoginActivity;
import com.p5m.me.view.activity.LoginRegister.RegistrationActivity;
import com.p5m.me.view.activity.LoginRegister.SignUpOptions;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationSelectionActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, NetworkCommunicator.RequestListener {


    private int position = -1;
    private ArrayList<String> categories;
    private List<StoreApiModel> model;
    private boolean other = false;
    private int countryId = 0;

    public static void open(Context context) {
        context.startActivity(new Intent(context, LocationSelectionActivity.class));
    }

    public static void open(Context context, int navigationFromFacebookSignup) {
        LocationSelectionActivity.navigateFrom = navigationFromFacebookSignup;
        context.startActivity(new Intent(context, LocationSelectionActivity.class));
    }

    public static void open(Context context, RegistrationRequest registrationRequest, int navigationFromFacebookLogin) {
        LocationSelectionActivity.navigateFrom = navigationFromFacebookLogin;
        LocationSelectionActivity.registrationRequest = registrationRequest;
        context.startActivity(new Intent(context, LocationSelectionActivity.class));
    }

    @BindView(R.id.spinnerCity)
    public Spinner spinnerCity;

    @BindView(R.id.buttonNext)
    public Button buttonNext;

    @BindView(R.id.textViewCountryName)
    public EditText textViewCountryName;

    @BindView(R.id.textInputLayoutCity)
    public TextInputLayout textInputLayoutCity;

    @BindView(R.id.textViewLogin)
    public TextView textViewLogin;
    Boolean isSelectCountry = false;
    private String item;
    public static RegistrationRequest registrationRequest;
    public static int navigateFrom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);
        ButterKnife.bind(activity);
        categories = new ArrayList<String>();
        buttonNext.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
        textInputLayoutCity.setVisibility(View.GONE);
        Helper.setupErrorWatcher(textViewCountryName, textInputLayoutCity);
        if (TempStorage.getCountries() == null)
            callApi();
        else {
            model = TempStorage.getCountries();
            setSpinnerView();
        }

    }

    private void callApi() {
        networkCommunicator.getStoreData(this, false);
    }

    private void setSpinnerView() {
        categories.clear();
        categories.add(getString(R.string.select_city));
        for (StoreApiModel data : model) {
            categories.add(data.getName());
        }
        spinnerCity.setOnItemSelectedListener(this);
        categories.add(getString(R.string.other_country));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.view_spinner_item, categories) {
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
        spinnerCity.setAdapter(spinnerArrayAdapter);
        spinnerCity.setSelection(0);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
        if (position > 0) {
            this.position = position - 1;
            isSelectCountry = true;
            textInputLayoutCity.setVisibility(View.GONE);
            other = false;
            textInputLayoutCity.setVisibility(View.GONE);
            if (position > model.size()) {
                other = true;
                textInputLayoutCity.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        textInputLayoutCity.setVisibility(View.GONE);
        textInputLayoutCity.setVisibility(View.GONE);
        isSelectCountry = false;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                if (position < 0) {
                    ToastUtils.show(context, getString(R.string.please_select_city));
                } else if (navigateFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FACEBOOK_LOGIN
                        && registrationRequest != null && !other) {
                    registrationRequest.setStoreId(model.get(position).getId());
                    networkCommunicator.register(registrationRequest, this, false);
                } else if (isSelectCountry && model != null) {
                    if (!other) {
                        textInputLayoutCity.setVisibility(View.GONE);
                        countryId = model.get(position).getId();
                        if (navigateFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FACEBOOK_SIGNUP) {
                            GetStartedActivity.open(context);
                            finish();
                        } else {
                            TempStorage.setCountryId(countryId);
                            SignUpOptions.open(context, countryId);
                        }
                    } else {
                        textInputLayoutCity.setVisibility(View.VISIBLE);
                        if (!isError()) {
                            ExpandCityActivity.open(context, textViewCountryName.getText().toString());
                        }
                    }

                }

                break;
            case R.id.textViewLogin:
                LoginActivity.open(context);
                finish();
                break;
        }
    }

    private boolean isError() {
        String country = textViewCountryName.getText().toString().trim();
        if (country.isEmpty()) {
            textInputLayoutCity.setError(context.getResources().getString(R.string.country_required));
            return true;
        }
        return false;
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                model = ((ResponseModel<List<StoreApiModel>>) response).data;
                setSpinnerView();
                break;
            case NetworkCommunicator.RequestCode.REGISTER:
                if (response != null) {
                    User user = ((ResponseModel<User>) response).data;
                    TempStorage.setCountryId(user.getId());
                    EventBroadcastHelper.sendLogin(context, user);
                    MixPanel.trackRegister(AppConstants.Tracker.EMAIL, TempStorage.getUser());
                    FirebaseAnalysic.trackRegister(AppConstants.Tracker.EMAIL, TempStorage.getUser());
                    IntercomEvents.successfulLoginIntercom(user.getFirstName() + " " + user.getLastName(), user.getEmail());
                    GetStartedActivity.open(context);
                }
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                ToastUtils.show(context, errorMessage);
                break;
            case NetworkCommunicator.RequestCode.REGISTER:
//                SignUpOptions.open(context);
                SignUpOptions.open(context, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_FB_LOGIN);
                break;
        }
    }


}
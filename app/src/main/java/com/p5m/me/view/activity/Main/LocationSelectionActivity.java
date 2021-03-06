package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.InterestedCityModel;
import com.p5m.me.data.main.InterestedCityRequestModel;
import com.p5m.me.data.main.StoreApiModel;
import com.p5m.me.data.main.StoreModel;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.LoginRequest;
import com.p5m.me.data.request.RegistrationRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.LoginRegister.LoginActivity;
import com.p5m.me.view.activity.LoginRegister.SignUpOptions;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.analytics.IntercomEvents.successfulLoginIntercom;

public class
LocationSelectionActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, NetworkCommunicator.RequestListener {


    private int position = -1;
    private ArrayList<String> categories;
    private List<StoreApiModel> model;
    private boolean other = false;
    private int countryId = 0;
    private InterestedCityRequestModel interestedCityRequestModel;
    private long loginTime;
    private String gender;
    private boolean isRegisterClick = false;

    public static void open(Context context) {
        LocationSelectionActivity.navigateFrom = 0;
        context.startActivity(new Intent(context, LocationSelectionActivity.class));
    }

    public static void open(Context context, int navigationFromFacebookSignup) {
        LocationSelectionActivity.navigateFrom = navigationFromFacebookSignup;
        context.startActivity(new Intent(context, LocationSelectionActivity.class));
    }

    public static void open(Context context, RegistrationRequest registrationRequest, int navigationFrom) {
        LocationSelectionActivity.navigateFrom = navigationFrom;
        LocationSelectionActivity.registrationRequest = registrationRequest;
        context.startActivity(new Intent(context, LocationSelectionActivity.class));
    }


    @BindView(R.id.spinnerCity)
    public Spinner spinnerCity;

    @BindView(R.id.buttonMale)
    public Button buttonMale;
    @BindView(R.id.buttonFemale)
    public Button buttonFemale;


    @BindView(R.id.buttonNext)
    public Button buttonNext;

    @BindView(R.id.textViewCountryName)
    public EditText textViewCountryName;

    @BindView(R.id.textInputLayoutCity)
    public TextInputLayout textInputLayoutCity;

    @BindView(R.id.textViewLogin)
    public TextView textViewLogin;

    @BindView(R.id.textViewHeader)
    public TextView textViewHeader;
    @BindView(R.id.layoutGender)
    public LinearLayout layoutGender;
    @BindView(R.id.view)
    public View view;

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
        buttonFemale.setOnClickListener(this);
        buttonMale.setOnClickListener(this);
        if (TempStorage.getCountryId() != 0)
            handleGenderScreen();
        else
            handleLocationView();
        Helper.setupErrorWatcher(textViewCountryName, textInputLayoutCity);
        if (TempStorage.getCountries() == null)
            callApi();
        else {
            model = TempStorage.getCountries();
            setSpinnerView();
        }

    }

    private void handleGenderScreen() {

        textViewHeader.setText(getString(R.string.gender_string));
        buttonNext.setText(getString(R.string.register));
        layoutGender.setVisibility(View.VISIBLE);
        textInputLayoutCity.setVisibility(View.GONE);
        spinnerCity.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        isRegisterClick = true;

    }

    private void handleLocationView() {
        textInputLayoutCity.setVisibility(View.GONE);
        buttonNext.setEnabled(true);
        buttonNext.setText(context.getResources().getString(R.string.next));
        view.setVisibility(View.VISIBLE);
        textViewHeader.setText(getString(R.string.location_search_for));
        buttonNext.setText(getString(R.string.next));
        layoutGender.setVisibility(View.GONE);
        spinnerCity.setVisibility(View.VISIBLE);
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
                if (position >= 0 && position < model.size()) {
                    TempStorage.setCountryId(model.get(position).getCountryId());
//                    ToastUtils.show(context, getString(R.string.select_city));
                } else if (other) {
                    TempStorage.setCountryId(-1);
                    textInputLayoutCity.setVisibility(View.VISIBLE);
                    if (!isError()) {
                        callInterestedCityApi();
                        buttonNext.setEnabled(false);
                        buttonNext.setText(context.getResources().getString(R.string.please_wait));
                    }
                }
                if (TempStorage.getCountryId() == 0) {
                    ToastUtils.show(context, getString(R.string.select_city));
                } else if (isRegisterClick) {
                    if (TextUtils.isEmpty(gender)) {
                        ToastUtils.show(context, getString(R.string.gender_required));
                    } else if (navigateFrom == AppConstants.AppNavigation.NAVIGATION_FROM_GOOGLE_LOGIN
                            && registrationRequest != null && !other) {
                        loginTime = System.currentTimeMillis() - 5 * 1000;
                        registrationRequest.setGender(gender);
                        networkCommunicator.loginFb(new LoginRequest(registrationRequest.getGoogleId(), registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getEmail(), gender, AppConstants.ApiParamValue.LOGINWITHGOOGLE), LocationSelectionActivity.this, false);
                    }
                } else if (navigateFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FACEBOOK_LOGIN
                        && registrationRequest != null && !other) {
                    loginTime = System.currentTimeMillis() - 5 * 1000;

                    networkCommunicator.loginFb(new LoginRequest(registrationRequest.getFacebookId(), registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getEmail(), registrationRequest.getGender(), AppConstants.ApiParamValue.LOGINWITHFACEBOOK), LocationSelectionActivity.this, false);

                } else if (navigateFrom == AppConstants.AppNavigation.NAVIGATION_FROM_GOOGLE_LOGIN
                        && registrationRequest != null && !other) {
                    handleGenderScreen();
//                    SignUpOptions.open(this, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_GOOGLE_LOGIN);

                } else if (model != null) {
                    if (!other) {
                        textInputLayoutCity.setVisibility(View.GONE);
                        countryId = model.get(position).getId();
                        if (navigateFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FACEBOOK_SIGNUP) {
                            GetStartedActivity.open(context);
                            finish();
                        } else {
                            TempStorage.setCountryId(countryId);
                            TempStorage.setCountryName(model.get(position).getName());
                            SignUpOptions.open(context, countryId);
                        }
                    }

                }

                break;
            case R.id.textViewLogin:
                TempStorage.setCountryId(0);
                LoginActivity.open(context);
                finish();
                break;
            case R.id.buttonFemale:
                selectFemale();
                break;
            case R.id.buttonMale:
                selectMale();
                break;
        }
    }

    private void callInterestedCityApi() {
        networkCommunicator.uploadInsterestedCity(interestedCityRequestModel, this, false);
    }

    private boolean isError() {
        String country = textViewCountryName.getText().toString().trim();
        if (country.isEmpty()) {
            textInputLayoutCity.setError(context.getResources().getString(R.string.country_required));
            return true;
        }
        interestedCityRequestModel = new InterestedCityRequestModel();
        interestedCityRequestModel.setCityName(country);
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
                    TempStorage.setCountryName(user.getStoreName());
                    EventBroadcastHelper.sendLogin(context, user);
                    MixPanel.trackRegister(AppConstants.Tracker.EMAIL, TempStorage.getUser());
                    FirebaseAnalysic.trackRegister(AppConstants.Tracker.EMAIL, TempStorage.getUser());
                    IntercomEvents.successfulLoginIntercom(user.getFirstName() + " " + user.getLastName(), user.getEmail());
                    GetStartedActivity.open(context);
                    finish();
                }
                break;
            case NetworkCommunicator.RequestCode.LOGIN_FB:

                if (response != null) {
                    User user = ((ResponseModel<User>) response).data;
                    successfulLoginIntercom();
                    EventBroadcastHelper.sendLogin(context, user);

                    if (user.getDateOfJoining() >= loginTime) {
                        MixPanel.trackRegister(AppConstants.Tracker.FB, TempStorage.getUser());
                        FirebaseAnalysic.trackRegister(AppConstants.Tracker.FB, TempStorage.getUser());
                    } else
                        MixPanel.trackLogin(AppConstants.Tracker.FB, TempStorage.getUser());
                    if (countryId != 0)
                        networkCommunicator.updateStoreId(countryId, this, false);
                    else
                        GetStartedActivity.open(context);

                    finish();
                }


                break;
            case NetworkCommunicator.RequestCode.UPDATE_STORE_ID:
                StoreModel model = ((ResponseModel<StoreModel>) response).data;
                User user = TempStorage.getUser();
                user.setStoreId(model.getId());
                user.setCurrencyCode(model.getCurrencyCode());
                user.setStoreName(model.getName());
                EventBroadcastHelper.sendUserUpdate(context, user);
                EventBroadcastHelper.changeCountry();
                HomeActivity.open(context);
                break;

            case NetworkCommunicator.RequestCode.INERESTED_CITY:
                buttonNext.setEnabled(true);
                buttonNext.setText(context.getResources().getString(R.string.next));

                InterestedCityModel interestedCityModel = ((ResponseModel<InterestedCityModel>) response).data;
                ExpandCityActivity.open(context, textViewCountryName.getText().toString(), interestedCityModel.getId(), interestedCityRequestModel);
                break;


        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.INERESTED_CITY:
                ToastUtils.show(context, errorMessage);

                buttonNext.setEnabled(true);
                buttonNext.setText(context.getResources().getString(R.string.next));
                break;
            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                ToastUtils.show(context, errorMessage);
                break;

            case NetworkCommunicator.RequestCode.LOGIN_FB:
                if (navigateFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FACEBOOK_LOGIN)
                    SignUpOptions.open(context, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_FB_LOGIN);
                else
                    SignUpOptions.open(context, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_GOOGLE_LOGIN);

                break;
            case NetworkCommunicator.RequestCode.REGISTER:
                ToastUtils.show(context, errorMessage);
                break;
        }
    }

    private void selectFemale() {
        try {
            buttonMale.setBackground(getResources().getDrawable(R.drawable.button_white));

            buttonMale.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
            buttonFemale.setBackground(getResources().getDrawable(R.drawable.button_blue));
            buttonFemale.setTextColor(ContextCompat.getColor(context, R.color.white));
            gender = AppConstants.ApiParamValue.GENDER_FEMALE;
            registrationRequest.setGender(gender);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void selectMale() {
        try {
            buttonMale.setBackgroundResource(R.drawable.button_blue);
            buttonMale.setTextColor(ContextCompat.getColor(context, R.color.white));
            buttonFemale.setBackgroundResource(R.drawable.button_white);
            buttonFemale.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
            gender = AppConstants.ApiParamValue.GENDER_MALE;
            registrationRequest.setGender(gender);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        TempStorage.setCountryId(0);
        super.onBackPressed();
    }
}

package com.p5m.me.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.User;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.KeyboardUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.LoginRegister.LoginActivity;
import com.p5m.me.view.activity.LoginRegister.RegistrationActivity;
import com.p5m.me.view.activity.LoginRegister.RegistrationDoneActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegistrationSteps extends BaseFragment implements View.OnClickListener, NetworkCommunicator.RequestListener {

    public static Fragment createFragment(int position) {
        Fragment tabFragment = new RegistrationSteps();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    @BindView(R.id.layoutName)
    public View layoutName;
    @BindView(R.id.layoutEmail)
    public View layoutEmail;
    @BindView(R.id.layoutPassword)
    public View layoutPassword;
    @BindView(R.id.layoutGender)
    public View layoutGender;

    @BindView(R.id.textViewLogin)
    public TextView textViewLogin;
    @BindView(R.id.textViewGenderError)
    public TextView textViewGenderError;

    @BindView(R.id.textInputLayoutName)
    public TextInputLayout textInputLayoutName;
    @BindView(R.id.textInputLayoutLastName)
    public TextInputLayout textInputLayoutLastName;
    @BindView(R.id.textInputLayoutEmail)
    public TextInputLayout textInputLayoutEmail;
    @BindView(R.id.textInputLayoutPass)
    public TextInputLayout textInputLayoutPass;
    @BindView(R.id.textInputLayoutConfirmPass)
    public TextInputLayout textInputLayoutConfirmPass;

    @BindView(R.id.editTextConfirmPass)
    public EditText editTextConfirmPass;
    @BindView(R.id.editTextPass)
    public EditText editTextPass;
    @BindView(R.id.editTextEmail)
    public EditText editTextEmail;
    @BindView(R.id.editTextName)
    public EditText editTextName;

    @BindView(R.id.editTextLastName)
    public EditText editTextLastName;

    @BindView(R.id.buttonMale)
    public Button buttonMale;
    @BindView(R.id.buttonFemale)
    public Button buttonFemale;
    @BindView(R.id.buttonNext)
    public Button buttonNext;

    @BindView(R.id.layoutContainer)
    public View layoutContainer;

    private int stepPosition;
    private RegistrationActivity registrationActivity;

    private String email;
    private String gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_steps, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        registrationActivity = (RegistrationActivity) getActivity();

        stepPosition = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);

        setupViews();

        checkSteps();

        layoutContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                KeyboardUtils.close(layoutContainer, context);
                return false;
            }
        });
    }

    @OnClick(R.id.textViewLogin)
    public void textViewLogin(View view) {
        LoginActivity.open(context);
    }

    private void setupViews() {
        buttonMale.setOnClickListener(this);
        buttonFemale.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

        Helper.setupErrorWatcher(editTextName, textInputLayoutName);
        Helper.setupErrorWatcher(editTextLastName, textInputLayoutLastName);
        Helper.setupErrorWatcher(editTextEmail, textInputLayoutEmail);
        Helper.setupErrorWatcher(editTextPass, textInputLayoutPass);
        Helper.setupErrorWatcher(editTextConfirmPass, textInputLayoutConfirmPass);

//        Helper.setupEditTextFocusHideKeyboard(editTextName);
//        Helper.setupEditTextFocusHideKeyboard(editTextEmail);
//        Helper.setupEditTextFocusHideKeyboard(editTextPass);
//        Helper.setupEditTextFocusHideKeyboard(editTextConfirmPass);

        if (registrationActivity.navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FB_LOGIN) {
            if (!registrationActivity.getRegistrationRequest().getFirstName().isEmpty()) {
                editTextName.setText(registrationActivity.getRegistrationRequest().getFirstName());
            }
            if (!registrationActivity.getRegistrationRequest().getLastName().isEmpty()) {
                editTextLastName.setText(registrationActivity.getRegistrationRequest().getLastName());
            }
            if (!registrationActivity.getRegistrationRequest().getEmail().isEmpty()) {
                editTextEmail.setText(registrationActivity.getRegistrationRequest().getEmail());
            }
            if (!registrationActivity.getRegistrationRequest().getGender().isEmpty()) {
                if (registrationActivity.getRegistrationRequest().getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)) {
                    buttonFemale.performClick();
                } else if (registrationActivity.getRegistrationRequest().getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)) {
                    buttonMale.performClick();
                }
            }
        }

        editTextLastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onClick(buttonNext);
                }
                return false;
            }
        });

        editTextEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onClick(buttonNext);
                }
                return false;
            }
        });

        editTextConfirmPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    onClick(buttonNext);
                }
                return false;
            }
        });
    }

    private void checkSteps() {
        switch (stepPosition) {
            case AppConstants.Tab.REGISTRATION_STEP_NAME:
                viewStep(layoutName);
                break;
            case AppConstants.Tab.REGISTRATION_STEP_EMAIL:
                viewStep(layoutEmail);
                break;
            case AppConstants.Tab.REGISTRATION_STEP_PASSWORD:
                viewStep(layoutPassword);
                break;
            case AppConstants.Tab.REGISTRATION_STEP_GENDER:
                viewStep(layoutGender);
                break;
        }
    }

    private void viewStep(View view) {
        layoutName.setVisibility(View.GONE);
        layoutEmail.setVisibility(View.GONE);
        layoutGender.setVisibility(View.GONE);
        layoutPassword.setVisibility(View.GONE);

        view.setVisibility(View.VISIBLE);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonMale:

                textViewGenderError.setVisibility(View.INVISIBLE);

                buttonMale.setBackgroundResource(R.drawable.join_rect);
                buttonFemale.setBackgroundResource(R.drawable.button_white);
                buttonMale.setTextColor(ContextCompat.getColor(context, R.color.white));
                buttonFemale.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));

                gender = AppConstants.ApiParamValue.GENDER_MALE;
                break;

            case R.id.buttonFemale:

                textViewGenderError.setVisibility(View.INVISIBLE);

                buttonMale.setBackgroundResource(R.drawable.button_white);
                buttonFemale.setBackgroundResource(R.drawable.join_rect);
                buttonMale.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
                buttonFemale.setTextColor(ContextCompat.getColor(context, R.color.white));

                gender = AppConstants.ApiParamValue.GENDER_FEMALE;
                break;

            case R.id.buttonNext:

                handleSteps();
                break;
        }
    }

    public void handleSteps() {

        switch (stepPosition) {
            case AppConstants.Tab.REGISTRATION_STEP_NAME:
                String name = editTextName.getText().toString().trim();
                String lastname = editTextLastName.getText().toString().trim();

                if (name.isEmpty()) {
                    textInputLayoutName.setError(context.getResources().getString(R.string.name_required_error));
                    return;
                }
                if (lastname.isEmpty()) {
                    textInputLayoutLastName.setError(context.getResources().getString(R.string.last_name_required_error));
                    return;
                }
                registrationActivity.setName(name);
                registrationActivity.setLastName(lastname);
                registrationActivity.next();
                break;

            case AppConstants.Tab.REGISTRATION_STEP_EMAIL:
                email = editTextEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required_error));
                    return;
                }

                if (!Helper.validateEmail(email)) {
                    textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required_validate));
                    return;
                }

                buttonNext.setVisibility(View.INVISIBLE);
                networkCommunicator.validateEmail(email, this, false);
                break;

            case AppConstants.Tab.REGISTRATION_STEP_PASSWORD:
                String pass = editTextPass.getText().toString();
                String confirmPass = editTextConfirmPass.getText().toString();

                if (pass.isEmpty()) {
                    textInputLayoutPass.setError(context.getResources().getString(R.string.password_required));
                    return;
                }

                if (!Helper.validatePass(pass)) {
                    textInputLayoutPass.setError(context.getResources().getString(R.string.password_weak));
                    return;
                }

                if (confirmPass.isEmpty()) {
                    textInputLayoutConfirmPass.setError(context.getResources().getString(R.string.password_required));
                    return;
                }

                if (!pass.equals(confirmPass)) {
                    textInputLayoutConfirmPass.setError(context.getResources().getString(R.string.confirm_password_not_matched));
                    return;
                }

                registrationActivity.setPass(pass);
                registrationActivity.next();
                break;

            case AppConstants.Tab.REGISTRATION_STEP_GENDER:

                if (gender == null) {
                    textViewGenderError.setVisibility(View.VISIBLE);
                    textViewGenderError.setText(context.getResources().getText(R.string.gender_required));
                    return;
                }

                registrationActivity.setGender(gender);
                registrationActivity.stepsDone();

                buttonNext.setVisibility(View.GONE);
                networkCommunicator.register(registrationActivity.getRegistrationRequest(), this, false);
                break;
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.VALIDATE_EMAIL:

                buttonNext.setVisibility(View.VISIBLE);
                registrationActivity.setEmail(email);
                registrationActivity.next();

                break;

            case NetworkCommunicator.RequestCode.REGISTER:
                buttonNext.setVisibility(View.VISIBLE);

                if (response != null) {

                    User user = ((ResponseModel<User>) response).data;

                    EventBroadcastHelper.sendLogin(context, user);

                    MixPanel.trackRegister(AppConstants.Tracker.EMAIL, TempStorage.getUser());

                    RegistrationDoneActivity.open(context);
                }

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.VALIDATE_EMAIL:
                buttonNext.setVisibility(View.VISIBLE);
                textInputLayoutEmail.setError(errorMessage);
                break;
            case NetworkCommunicator.RequestCode.REGISTER:
                ToastUtils.showFailureResponse(context, errorMessage);
                buttonNext.setVisibility(View.VISIBLE);
                break;
        }
    }
}

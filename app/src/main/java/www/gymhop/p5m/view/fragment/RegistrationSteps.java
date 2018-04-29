package www.gymhop.p5m.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.User;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.storage.preferences.MyPreferences;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.ToastUtils;
import www.gymhop.p5m.view.activity.LoginRegister.RegistrationActivity;
import www.gymhop.p5m.view.activity.LoginRegister.RegistrationDoneActivity;


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

    @BindView(R.id.buttonMale)
    public Button buttonMale;
    @BindView(R.id.buttonFemale)
    public Button buttonFemale;
    @BindView(R.id.buttonNext)
    public Button buttonNext;

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
    }

    private void setupViews() {
        buttonMale.setOnClickListener(this);
        buttonFemale.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

        Helper.setupErrorWatcher(editTextName, textInputLayoutName);
        Helper.setupErrorWatcher(editTextEmail, textInputLayoutEmail);
        Helper.setupErrorWatcher(editTextPass, textInputLayoutPass);
        Helper.setupErrorWatcher(editTextConfirmPass, textInputLayoutConfirmPass);

        editTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
            case AppConstants.FragmentPosition.REGISTRATION_STEP_NAME:
                viewStep(layoutName);
                break;
            case AppConstants.FragmentPosition.REGISTRATION_STEP_EMAIL:
                viewStep(layoutEmail);
                break;
            case AppConstants.FragmentPosition.REGISTRATION_STEP_PASSWORD:
                viewStep(layoutPassword);
                break;
            case AppConstants.FragmentPosition.REGISTRATION_STEP_GENDER:
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
            case AppConstants.FragmentPosition.REGISTRATION_STEP_NAME:
                String name = editTextName.getText().toString();
                if (name.isEmpty()) {
                    textInputLayoutName.setError(context.getResources().getString(R.string.name_required_error));
                    return;
                }

                registrationActivity.setName(name);
                registrationActivity.next();
                break;

            case AppConstants.FragmentPosition.REGISTRATION_STEP_EMAIL:
                email = editTextEmail.getText().toString();
                if (email.isEmpty()) {
                    textInputLayoutEmail.setError(context.getResources().getString(R.string.name_required_error));
                    return;
                }

                if (!Helper.validateEmail(email)) {
                    textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required_validate));
                    return;
                }

                buttonNext.setVisibility(View.INVISIBLE);
                networkCommunicator.validateEmail(email, this, false);
                break;

            case AppConstants.FragmentPosition.REGISTRATION_STEP_PASSWORD:
                String pass = editTextPass.getText().toString();
                String confirmPass = editTextConfirmPass.getText().toString();

                if (pass.isEmpty()) {
                    textInputLayoutPass.setError(context.getResources().getString(R.string.password_required));
                    return;
                }

                if (Helper.validatePass(pass)) {
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

            case AppConstants.FragmentPosition.REGISTRATION_STEP_GENDER:

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
                TempStorage.setUser(context, ((ResponseModel<User>) response).data);
                MyPreferences.getInstance().setLogin(true);
                RegistrationDoneActivity.open(context);
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

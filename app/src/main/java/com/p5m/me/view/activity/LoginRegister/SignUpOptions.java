package com.p5m.me.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.FaceBookUser;
import com.p5m.me.data.main.StoreModel;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.LoginRequest;
import com.p5m.me.data.request.RegistrationRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.helper.MyClickSpan;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.GetStartedActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class SignUpOptions extends BaseActivity implements NetworkCommunicator.RequestListener, View.OnClickListener {


    public static void open(Context context) {
        context.startActivity(new Intent(context, SignUpOptions.class));
    }

    @BindView(R.id.textViewBottom)
    TextView textViewBottom;
    @BindView(R.id.buttonLoginFacebook)
    Button buttonLoginFacebook;
    @BindView(R.id.layoutProgress)
    View layoutProgress;

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

    private String email;
    private String gender;


    private CallbackManager callbackManager;
    private FaceBookUser faceBookUser;
    private long loginTime;
    private RegistrationRequest registrationRequest = new RegistrationRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_options);

        ButterKnife.bind(activity);

        String spanText = activity.getString(R.string.terms_of_service);
        String terms = activity.getString(R.string.terms_service);
        String policy = activity.getString(R.string.privacy_policy);

        Spannable span = Spannable.Factory.getInstance().newSpannable(spanText);
        buttonNext.setOnClickListener(this);
        span.setSpan(new MyClickSpan(context) {
            @Override
            public void onSpanClicked() {
                Helper.openWebPage(context, AppConstants.Url.WEBSITE + "terms");
            }
        }, spanText.indexOf(terms), spanText.indexOf(terms) + terms.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        span.setSpan(new MyClickSpan(context) {
            @Override
            public void onSpanClicked() {
                Helper.openWebPage(context, AppConstants.Url.WEBSITE + "privacy");
            }
        }, spanText.indexOf(policy), spanText.indexOf(policy) + policy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewBottom.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
        textViewBottom.setMovementMethod(LinkMovementMethod.getInstance());
        textViewBottom.setText(span);

        SetupFBLogin();
        setupViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void SetupFBLogin() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {

                        LogUtils.debug("Facebook Token : " + loginResult.getAccessToken().toString());

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

                                        LogUtils.debug("Facebook newMeRequest : " + object.toString());

                                        String first_name = "";
                                        String last_name = "";
                                        String gender = "";
                                        String email = "";
                                        String id = "";

                                        try {
                                            id = object.getString("id");
                                            first_name = object.getString("first_name");
                                            last_name = object.getString("last_name");
                                            gender = object.getString("gender").toUpperCase();
                                            email = object.getString("email");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            LogUtils.exception(e);
                                        }

                                        faceBookUser = new FaceBookUser(id, first_name, last_name, gender, email);

                                        networkCommunicator.loginFb(new LoginRequest(id, first_name, last_name, email, gender), SignUpOptions.this, false);
                                      }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,first_name,last_name,birthday,gender,email,location");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        LogUtils.debug("Facebook onCancel");
                        layoutProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        LogUtils.debug("Facebook onError: " + exception.getMessage());
                        layoutProgress.setVisibility(View.GONE);
                    }
                });

        buttonLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginTime = System.currentTimeMillis() - 5 * 1000;
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email", "user_gender"));
                layoutProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.buttonLogin)
    public void buttonLogin(View view) {
        open(context);
        view.setClickable(false);

        try {
            Handler handler;
            Runnable nextScreenRunnable;
            handler = new Handler();
            nextScreenRunnable = new Runnable() {
                @Override
                public void run() {
                    view.setClickable(true);
                }
            };
            handler.postDelayed(nextScreenRunnable, 500);
        } catch (Exception e) {
            e.printStackTrace();
            view.setClickable(true);

        }


    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {

            case NetworkCommunicator.RequestCode.LOGIN_FB:

                if (response != null) {
                    User user = ((ResponseModel<User>) response).data;
                    successfulLoginIntercom(user.getFirstName() + " " + user.getLastName());
                    EventBroadcastHelper.sendLogin(context, user);

                    networkCommunicator.updateStoreId(TempStorage.getCountryId(), SignUpOptions.this, false);
                    if (user.getDateOfJoining() >= loginTime) {
                        MixPanel.trackRegister(AppConstants.Tracker.FB, TempStorage.getUser());
                        FirebaseAnalysic.trackRegister(AppConstants.Tracker.FB, TempStorage.getUser());
                    } else
                        MixPanel.trackLogin(AppConstants.Tracker.FB, TempStorage.getUser());

                    finish();
                }
                layoutProgress.setVisibility(View.GONE);

                break;
            case NetworkCommunicator.RequestCode.VALIDATE_EMAIL:
                setEmail(email);
                networkCommunicator.register(registrationRequest, SignUpOptions.this, false);
                break;
            case NetworkCommunicator.RequestCode.REGISTER:
                if (response != null) {
                    User user = ((ResponseModel<User>) response).data;
                    EventBroadcastHelper.sendLogin(context, user);
                    MixPanel.trackRegister(AppConstants.Tracker.EMAIL, TempStorage.getUser());
                    FirebaseAnalysic.trackRegister(AppConstants.Tracker.EMAIL, TempStorage.getUser());
//                    successfulLoginIntercom(user.getFirstName() + " " + user.getLastName(), user.getEmail());
                    GetStartedActivity.open(context);
                }
                break;

            case NetworkCommunicator.RequestCode.UPDATE_STORE_ID:
                List<StoreModel> model = ((ResponseModel<List<StoreModel>>) response).data;
                TempStorage.setCountryId(model.get(0).getId());
                TempStorage.setCurrency(model.get(0).getCurrencyCode());

        }

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        switch (requestCode) {

            case NetworkCommunicator.RequestCode.LOGIN_FB:
                facebookValidation();
                break;
            case NetworkCommunicator.RequestCode.VALIDATE_EMAIL:
                textInputLayoutEmail.setError(errorMessage);
                break;
            case NetworkCommunicator.RequestCode.UPDATE_STORE_ID:
            case NetworkCommunicator.RequestCode.REGISTER:
                ToastUtils.showFailureResponse(context, errorMessage);
                break;

        }
        layoutProgress.setVisibility(View.GONE);
    }

    private void successfulLoginIntercom(String name) {
        Registration registration = Registration.create().withUserId(name);
        Intercom.client().registerIdentifiedUser(registration);
        LogUtils.debug("Intercom Working");
    }

    public void setName(String name) {
        registrationRequest.setFirstName(name);
    }

    public void setLastName(String lastname) {
        registrationRequest.setLastName(lastname);
    }

    public void setEmail(String email) {
        registrationRequest.setEmail(email);
    }

    public void setPass(String pass) {
        registrationRequest.setPassword(pass);
    }

    public void setGender(String gender) {
        registrationRequest.setGender(gender);
        registrationRequest.setStoreId(TempStorage.getCountryId());
    }

    public RegistrationRequest getRegistrationRequest() {
        return registrationRequest;
    }

    private void validateAndGenerateRegistrationRequest() {
        String name = editTextName.getText().toString().trim();
        String lastname = editTextLastName.getText().toString().trim();

        if (name.isEmpty()) {
            textInputLayoutName.setError(context.getResources().getString(R.string.name_required_error));
            return;
        }
        if (name.length()>40) {
            textInputLayoutName.setError(context.getResources().getString(R.string.name_length_error));
            return;
        }
        if (lastname.isEmpty()) {
            textInputLayoutLastName.setError(context.getResources().getString(R.string.last_name_required_error));
            return;
        }
        if (lastname.length()>40) {
            textInputLayoutName.setError(context.getResources().getString(R.string.name_length_error));
            return;
        }
        setName(name);
        setLastName(lastname);
//                next();
//                break;

//            case AppConstants.Tab.REGISTRATION_STEP_EMAIL:
        email = editTextEmail.getText().toString().trim();
        if (email.isEmpty()) {
            textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required_error));
            return;
        }

        if (!Helper.validateEmail(email)) {
            textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required_validate));
            return;
        }
        String pass = editTextPass.getText().toString();

        if (pass.isEmpty()) {
            textInputLayoutPass.setError(context.getResources().getString(R.string.password_required));
            return;
        }

        if (!Helper.validatePass(pass)) {
            textInputLayoutPass.setError(context.getResources().getString(R.string.password_weak));
            return;
        }
        String confirmPass = editTextConfirmPass.getText().toString();
        if (confirmPass.isEmpty()) {
            textInputLayoutConfirmPass.setError(context.getResources().getString(R.string.password_required));
            return;
        }

        if (!pass.equals(confirmPass)) {
            textInputLayoutConfirmPass.setError(context.getResources().getString(R.string.confirm_password_not_matched));
            return;
        }

        setPass(pass);

        if (gender == null) {
            textViewGenderError.setVisibility(View.VISIBLE);
            textViewGenderError.setText(context.getResources().getText(R.string.gender_required));
            return;
        }

        setGender(gender);


        networkCommunicator.validateEmail(email, this, false);
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
                validateAndGenerateRegistrationRequest();
                break;

        }
    }

    @OnClick(R.id.textViewLogin)
    public void textViewLogin(View view) {
        LoginActivity.open(context);
        finish();
    }

    private void setupViews() {
        buttonMale.setOnClickListener(this);
        buttonFemale.setOnClickListener(this);

        Helper.setupErrorWatcher(editTextName, textInputLayoutName);
        Helper.setupErrorWatcher(editTextLastName, textInputLayoutLastName);
        Helper.setupErrorWatcher(editTextEmail, textInputLayoutEmail);
        Helper.setupErrorWatcher(editTextPass, textInputLayoutPass);
        Helper.setupErrorWatcher(editTextConfirmPass, textInputLayoutConfirmPass);

//        Helper.setupEditTextFocusHideKeyboard(editTextName);
//        Helper.setupEditTextFocusHideKeyboard(editTextEmail);
//        Helper.setupEditTextFocusHideKeyboard(editTextPass);
//        Helper.setupEditTextFocusHideKeyboard(editTextConfirmPass);

        if (!getRegistrationRequest().getFirstName().isEmpty()) {
            editTextName.setText(getRegistrationRequest().getFirstName());
        }
        if (!getRegistrationRequest().getLastName().isEmpty()) {
            editTextLastName.setText(getRegistrationRequest().getLastName());
        }
        if (!getRegistrationRequest().getEmail().isEmpty()) {
            editTextEmail.setText(getRegistrationRequest().getEmail());
        }
        if (!getRegistrationRequest().getGender().isEmpty()) {
            if (getRegistrationRequest().getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)) {
                buttonFemale.performClick();
            } else if (getRegistrationRequest().getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)) {
                buttonMale.performClick();
            }
        }

        /*editTextLastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
        });*/
    }

    private void facebookValidation() {
        String message = "";
        registrationRequest = new RegistrationRequest(faceBookUser.getId(), faceBookUser.getName(), faceBookUser.getLastName(),TempStorage.getCountryId() );

        if (faceBookUser.getEmail().isEmpty()) {

            if (faceBookUser.getGender().isEmpty()) {
                message = getString(R.string.needs_your_email_and_gender);
            } else {
                message = getString(R.string.needs_your_email);
                registrationRequest.setGender(faceBookUser.getGender());
            }

        } else {
            if (faceBookUser.getGender().isEmpty()) {
                message = getString(R.string.needs_your_gender);
            } else {
                registrationRequest.setGender(faceBookUser.getGender());
            }

            registrationRequest.setEmail(faceBookUser.getEmail());
        }

        if (!message.isEmpty()) {
            DialogUtils.showBasicMessage(context, context.getString(R.string.app_name), getString(R.string.ok), message);
        }
    }

    private void successfulLoginIntercom(String name, String email) {

        Registration registration = Registration.create().withUserId(name).withEmail(email);
        Intercom.client().registerIdentifiedUser(registration);
    }


}

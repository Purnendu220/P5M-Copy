package com.p5m.me.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.p5m.me.R;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.FaceBookUser;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.LoginRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.target_user_notification.UserPropertyConst;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.KeyboardUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.UserAttributes;
import io.intercom.android.sdk.identity.Registration;

import static com.p5m.me.utils.AppConstants.Pref.MEMBERSHIP_INFO_STATE_HAVE_PACKAGE;
import static com.p5m.me.utils.AppConstants.Pref.MEMBERSHIP_INFO_STATE_NO_PACKAGE;

public class LoginActivity extends BaseActivity implements NetworkCommunicator.RequestListener {


    public static void open(Context context, int navigationFrom) {
        context.startActivity(new Intent(context, LoginActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom));
    }

    public static void open(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @BindView(R.id.textViewForgetPassword)
    public TextView textViewForgetPassword;
    @BindView(R.id.textViewSignUp)
    public TextView textViewSignUp;

    @BindView(R.id.textInputLayoutEmail)
    public TextInputLayout textInputLayoutEmail;
    @BindView(R.id.textInputLayoutPassword)
    public TextInputLayout textInputLayoutPassword;

    @BindView(R.id.editTextEmail)
    public EditText editTextEmail;
    @BindView(R.id.editTextPassword)
    public EditText editTextPassword;

    @BindView(R.id.buttonLoginFacebook)
    public Button buttonLoginFacebook;
    @BindView(R.id.buttonLogin)
    public Button buttonLogin;

    @BindView(R.id.layoutProgressRoot)
    View layoutProgressRoot;

    @BindView(R.id.layoutContainer)
    public View layoutContainer;

    private int navigatedFrom;
    private CallbackManager callbackManager;
    private FaceBookUser faceBookUser;
    private long loginTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(activity);

        navigatedFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);

        if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_CONTINUE_USER) {
            editTextEmail.setText(TempStorage.getUser().getEmail());
        }

        setEditWatcher();

        layoutContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                KeyboardUtils.close(layoutContainer, context);
                return false;
            }
        });

        SetupFBLogin();

    }

    private void setUserProperty() {
        FirebaseAnalytics.getInstance(context).setUserProperty(UserPropertyConst.GENDER, TempStorage.getUser().getGender());
    }

    private void setEditWatcher() {

        Helper.setupErrorWatcher(editTextPassword, textInputLayoutPassword);
        Helper.setupErrorWatcher(editTextEmail, textInputLayoutEmail);

//        Helper.setupEditTextFocusHideKeyboard(editTextEmail);
//        Helper.setupEditTextFocusHideKeyboard(editTextPassword);

        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    buttonLogin(buttonLogin);
                }
                return false;
            }
        });
    }

    @OnClick(R.id.textViewSignUp)
    public void textViewSignUp(View view) {
        SignUpOptions.open(context);
        finish();
    }

    @OnClick(R.id.textViewForgetPassword)
    public void textViewForgetPassword(View view) {
        ForgotPasswordActivity.open(context);
    }

    @OnClick(R.id.buttonLogin)
    public void buttonLogin(View view) {
        String user = editTextEmail.getText().toString().trim();
        String pass = editTextPassword.getText().toString();

        if (user.isEmpty()) {
            textInputLayoutEmail.setError(getString(R.string.please_enter_your_email));
            return;
        }

        if (pass.isEmpty()) {
            textInputLayoutPassword.setError(getString(R.string.password_required_error));
            return;
        }

        buttonLogin.setVisibility(View.GONE);
        networkCommunicator.login(new LoginRequest(user, pass), this, false);
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

                                        networkCommunicator.loginFb(new LoginRequest(id, first_name, last_name, email, gender), LoginActivity.this, false);
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
                        layoutProgressRoot.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        LogUtils.debug("Facebook onError: " + exception.getMessage());
                        layoutProgressRoot.setVisibility(View.GONE);
                    }
                });

        buttonLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginTime = System.currentTimeMillis() - 5 * 1000;
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email", "user_gender"));
                layoutProgressRoot.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {

            case NetworkCommunicator.RequestCode.LOGIN: {

                User user = ((ResponseModel<User>) response).data;

                if (user != null) {
                    successfulLoginIntercom();
                    EventBroadcastHelper.sendLogin(context, user);
                    HomeActivity.open(context);
                    setUserProperty();

                    MixPanel.trackLogin(AppConstants.Tracker.EMAIL, TempStorage.getUser());

                    finish();
                } else {
                    textInputLayoutPassword.setError(getString(R.string.please_try_again));
                }

                buttonLogin.setVisibility(View.VISIBLE);
            }
            break;

            case NetworkCommunicator.RequestCode.LOGIN_FB:

                if (response != null) {
                    User user = ((ResponseModel<User>) response).data;
                    successfulLoginIntercom();
                    EventBroadcastHelper.sendLogin(context, user);
                    HomeActivity.open(context);

                    if (user.getDateOfJoining() >= loginTime) {
                        MixPanel.trackRegister(AppConstants.Tracker.FB, TempStorage.getUser());
                        FirebaseAnalysic.trackRegister(AppConstants.Tracker.FB, TempStorage.getUser());
                    } else
                        MixPanel.trackLogin(AppConstants.Tracker.FB, TempStorage.getUser());

                    finish();
                }

                layoutProgressRoot.setVisibility(View.GONE);

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        switch (requestCode) {

            case NetworkCommunicator.RequestCode.LOGIN:

                buttonLogin.setVisibility(View.VISIBLE);
                textInputLayoutPassword.setError(errorMessage);

                break;

            case NetworkCommunicator.RequestCode.LOGIN_FB:

                RegistrationActivity.open(context, faceBookUser);
                layoutProgressRoot.setVisibility(View.GONE);

                break;
        }
    }

    private void successfulLoginIntercom() {
        if (TempStorage.getUser() != null) {
            User user = TempStorage.getUser();
            Registration registration = Registration.create().withUserId(String.valueOf(user.getId()));
            Intercom.client().registerIdentifiedUser(registration);
            LogUtils.debug("Intercom Working");
        }
    }


}

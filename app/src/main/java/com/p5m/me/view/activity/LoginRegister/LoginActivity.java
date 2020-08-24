package com.p5m.me.view.activity.LoginRegister;

import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.p5m.me.R;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.FaceBookUser;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.LoginRequest;
import com.p5m.me.data.request.RegistrationRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.target_user_notification.UserPropertyConst;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.KeyboardUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.Main.LocationActivity;
import com.p5m.me.view.activity.Main.LocationSelectionActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.UserAttributes;
import io.intercom.android.sdk.identity.Registration;

import static com.p5m.me.analytics.IntercomEvents.successfulLoginIntercom;
import static com.p5m.me.utils.AppConstants.Pref.MEMBERSHIP_INFO_STATE_HAVE_PACKAGE;
import static com.p5m.me.utils.AppConstants.Pref.MEMBERSHIP_INFO_STATE_NO_PACKAGE;

public class LoginActivity extends BaseActivity implements NetworkCommunicator.RequestListener {


    private String email;
    private RegistrationRequest registrationRequest;
    private GoogleSignInAccount mGoogleSignInAccount;

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
    @BindView(R.id.sign_in_button)
    public SignInButton signInButton;
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

    private GoogleSignInClient mSignInClient;
    TextView textView;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(activity);

        customizeGoogleButton();
        navigatedFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);

        if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_CONTINUE_USER) {
            if (!TextUtils.isEmpty(TempStorage.getUser().getEmail()))
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
        setUpGoogleSignIn();

    }

    private void customizeGoogleButton() {

            // Find the TextView that is inside of the SignInButton and set its text
            for (int i = 0; i < signInButton.getChildCount(); i++) {
                View v = signInButton.getChildAt(i);

                if (v instanceof TextView) {
                    TextView tv = (TextView) v;
                    tv.setText(context.getString(R.string.enter_with_google));
                    tv.setAllCaps(true);
                    tv.setGravity(Gravity.CENTER);
                    tv.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                    return;
                }
            }

    }

    private void setUpGoogleSignIn() {
        GoogleSignInOptions options =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestScopes(Drive.SCOPE_FILE)
                        .requestEmail()
                        .requestProfile()
                        .build();

        mSignInClient = GoogleSignIn.getClient(this, options);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launches the sign in flow, the result is returned in onActivityResult
                Intent intent = mSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);

            }
        });
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
        LocationSelectionActivity.open(context);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            if (task.isSuccessful()) {
                // Sign in succeeded, proceed with account
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                handleGoogleSignIn(task, result);

            } else {
                // Sign in failed, handle failure and update UI
                // ...
                Toast.makeText(getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show();

            }

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void handleGoogleSignIn(Task<GoogleSignInAccount> completedTask, GoogleSignInResult result) {
        try {
            mGoogleSignInAccount = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateProfileByGoogleSignIn(mGoogleSignInAccount);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            LogUtils.debug("signInResult:failed code=" + e.getStatusCode());
            updateProfileByGoogleSignIn(null);
        }

    }

    private void updateProfileByGoogleSignIn(GoogleSignInAccount account) {
        if (account != null) {
            String first_name = "";
            String last_name = "";
            String gender = "";
            email = "";
            String id = "";

            try {
                id = account.getId();
                first_name = account.getGivenName();
                last_name = account.getFamilyName();
                email = account.getEmail();
                registrationRequest = new RegistrationRequest(id, first_name, last_name, -1, AppConstants.ApiParamValue.LOGINWITHGOOGLE);
                if (id != null && !TextUtils.isEmpty(id))
                    networkCommunicator.validateEmail(id, LoginActivity.this, false);
                else
                    LocationSelectionActivity.open(context, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_GOOGLE_LOGIN);


            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }


        }
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
                                        email = "";
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
                                        registrationRequest = new RegistrationRequest(faceBookUser.getId(), faceBookUser.getName(), faceBookUser.getLastName(), -1, AppConstants.ApiParamValue.LOGINWITHFACEBOOK);
                                        registrationRequest.setGender(gender);
                                        registrationRequest.setFacebookId(faceBookUser.getId());
                                        if (email != null && !TextUtils.isEmpty(email))
                                            networkCommunicator.validateEmail(id, LoginActivity.this, false);
                                        else
                                            LocationSelectionActivity.open(context, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_FACEBOOK_LOGIN);

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
            case NetworkCommunicator.RequestCode.VALIDATE_EMAIL:
               /* registrationRequest.setEmail(email);
                layoutProgressRoot.setVisibility(View.GONE);
                if (faceBookUser != null) {
                    registrationRequest.setFacebookId(faceBookUser.getId());
                    LocationSelectionActivity.open(context, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_FACEBOOK_LOGIN);
                } else {
                    registrationRequest.setGoogleId(mGoogleSignInAccount.getId());
                    LocationSelectionActivity.open(context, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_GOOGLE_LOGIN);
                }*/
//            case NetworkCommunicator.RequestCode.VALIDATE_EMAIL:
                if (response != null) {
                    List<String> email = ((ResponseModel<List<String>>) response).data;
                    if (email != null) {
                        TempStorage.setDefaultPage(AppConstants.Tab.TAB_FIND_CLASS);
                        if (faceBookUser != null) {
                            networkCommunicator.loginFb(new LoginRequest(faceBookUser.getId(), faceBookUser.getName(), faceBookUser.getLastName(), faceBookUser.getEmail(), faceBookUser.getGender(), AppConstants.ApiParamValue.LOGINWITHFACEBOOK), LoginActivity.this, false);
                        }
                        if (mGoogleSignInAccount != null) {
                            networkCommunicator.loginFb(new LoginRequest(mGoogleSignInAccount.getId(), mGoogleSignInAccount.getGivenName(), mGoogleSignInAccount.getFamilyName(), mGoogleSignInAccount.getEmail(), AppConstants.ApiParamValue.LOGINWITHGOOGLE), LoginActivity.this, false);
                        }
                    } else {
                        TempStorage.setDefaultPage(AppConstants.Tab.TAB_EXPLORE_PAGE);
                        if (faceBookUser != null) {
                            registrationRequest.setFacebookId(faceBookUser.getId());
                            registrationRequest.setEmail(faceBookUser.getEmail());
                            LocationSelectionActivity.open(context, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_FACEBOOK_LOGIN);
                        } else if (mGoogleSignInAccount != null) {
                            registrationRequest.setGoogleId(mGoogleSignInAccount.getId());
                            registrationRequest.setEmail(mGoogleSignInAccount.getEmail());
                            registrationRequest.setStoreId(TempStorage.getCountryId());

                            LocationSelectionActivity.open(context, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_GOOGLE_LOGIN);
                        }
                    }

                }
                break;
//                break;
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

                SignUpOptions.open(context, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_FB_LOGIN);
                layoutProgressRoot.setVisibility(View.GONE);

                break;
            case NetworkCommunicator.RequestCode.VALIDATE_EMAIL:
                ToastUtils.show(context,errorMessage);
             /*if (faceBookUser != null) {
                    networkCommunicator.loginFb(new LoginRequest(faceBookUser.getId(), faceBookUser.getName(), faceBookUser.getLastName(), email, faceBookUser.getGender(), AppConstants.ApiParamValue.LOGINWITHFACEBOOK), LoginActivity.this, false);
                } else if (mGoogleSignInAccount != null) {
                    registrationRequest.setGoogleId(mGoogleSignInAccount.getId());
                    networkCommunicator.loginFb(new LoginRequest(mGoogleSignInAccount.getId(), mGoogleSignInAccount.getGivenName(), mGoogleSignInAccount.getFamilyName(), email,  AppConstants.ApiParamValue.LOGINWITHGOOGLE), LoginActivity.this, false);
                }*/
                break;

        }
    }

}

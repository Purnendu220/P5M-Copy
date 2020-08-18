package com.p5m.me.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
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
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.GetStartedActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.Main.LocationSelectionActivity;
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


    private GoogleSignInClient mSignInClient;
    private GoogleSignInAccount mGoogleSignInAccount;

    public static void open(Context context) {
        context.startActivity(new Intent(context, SignUpOptions.class));
    }

    public static void open(Context context, int countryId) {
        SignUpOptions.countryId = countryId;
        context.startActivity(new Intent(context, SignUpOptions.class));
    }

    public static void open(Context context, RegistrationRequest registrationRequest, int navigationFromFbLogin) {
        SignUpOptions.registrationRequest = registrationRequest;
        SignUpOptions.navigationFrom = navigationFromFbLogin;
        context.startActivity(new Intent(context, SignUpOptions.class));

    }

    @BindView(R.id.textViewBottom)
    TextView textViewBottom;
    @BindView(R.id.buttonLoginFacebook)
    Button buttonLoginFacebook;
    @BindView(R.id.signInButton)
    SignInButton signInButton;
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

    @BindView(R.id.layoutSignUpOption)
    public LinearLayout layoutSignUpOption;


    private String email;
    private String gender;


    private CallbackManager callbackManager;
    private FaceBookUser faceBookUser;
    private long loginTime;
    private static RegistrationRequest registrationRequest;
    private static int countryId;
    private static int navigationFrom;
    private static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_options);

        ButterKnife.bind(activity);
        customizeGoogleButton();
        String spanText = activity.getString(R.string.terms_of_service);
        String terms = activity.getString(R.string.terms_service);
        String policy = activity.getString(R.string.privacy_policy);
        registrationRequest = new RegistrationRequest();
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
        setUpGoogleSignIn();
        setupViews();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                // Sign in succeeded, proceed with account
                handleGoogleSignIn(task);

            } else {
                // Sign in failed, handle failure and update UI
                // ...
                Toast.makeText(getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show();

            }

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void handleGoogleSignIn(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateProfileByGoogleSignIn(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            LogUtils.debug("signInResult:failed code=" + e.getStatusCode());
            updateProfileByGoogleSignIn(null);
        }

    }

    private void updateProfileByGoogleSignIn(GoogleSignInAccount account) {
        if (account != null) {
            mGoogleSignInAccount = account;
            String first_name = "";
            String last_name = "";
            String gender = "";
            String gemail = "";
            String id = "";

            try {
                id = account.getId();
                first_name = account.getGivenName();
                last_name = account.getFamilyName();
                gemail = account.getEmail();

                registrationRequest = new RegistrationRequest(id, first_name, last_name, TempStorage.getCountryId(), AppConstants.ApiParamValue.LOGINWITHGOOGLE);
                registrationRequest.setEmail(gemail);
                if (id != null && !TextUtils.isEmpty(id))
                    networkCommunicator.validateEmail(id, SignUpOptions.this, false);
                else {
                    LocationSelectionActivity.open(context, registrationRequest, AppConstants.AppNavigation.NAVIGATION_FROM_GOOGLE_LOGIN);
                }
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

                                        networkCommunicator.loginFb(new LoginRequest(id, first_name, last_name, email, gender, AppConstants.ApiParamValue.LOGINWITHFACEBOOK), SignUpOptions.this, false);
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
                    IntercomEvents.successfulLoginIntercom(user.getFirstName() + " " + user.getLastName());
                    EventBroadcastHelper.sendLogin(context, user);
                    if (countryId != 0)
                        networkCommunicator.updateStoreId(countryId, SignUpOptions.this, false);
                    if (user.getDateOfJoining() >= loginTime) {
                        MixPanel.trackRegister(AppConstants.Tracker.FB, TempStorage.getUser());
                        FirebaseAnalysic.trackRegister(AppConstants.Tracker.FB, TempStorage.getUser());
                    } else
                        MixPanel.trackLogin(AppConstants.Tracker.FB, TempStorage.getUser());

                    HomeActivity.open(context);
                }
                layoutProgress.setVisibility(View.GONE);

                break;
            case NetworkCommunicator.RequestCode.VALIDATE_EMAIL:
                if (response != null) {
                    List<String> email = ((ResponseModel<List<String>>) response).data;
                    if (email != null) {
                        if (faceBookUser != null) {
                            TempStorage.setDefaultPage(AppConstants.Tab.TAB_FIND_CLASS);
                            networkCommunicator.loginFb(new LoginRequest(faceBookUser.getId(), faceBookUser.getName(), faceBookUser.getLastName(), faceBookUser.getEmail(), faceBookUser.getGender(), AppConstants.ApiParamValue.LOGINWITHFACEBOOK), SignUpOptions.this, false);
                        } else if (mGoogleSignInAccount != null) {
                            TempStorage.setDefaultPage(AppConstants.Tab.TAB_FIND_CLASS);
                            networkCommunicator.loginFb(new LoginRequest(mGoogleSignInAccount.getId(), mGoogleSignInAccount.getGivenName(), mGoogleSignInAccount.getFamilyName(), mGoogleSignInAccount.getEmail(), AppConstants.ApiParamValue.LOGINWITHGOOGLE), SignUpOptions.this, false);
                        } else
                            ToastUtils.show(context, getString(R.string.already_account_error));
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


//                    else
//                        textInputLayoutEmail.setError(errorMessage);

                }
                break;
            case NetworkCommunicator.RequestCode.SEARCH_EMAIL:
//                 else {
                registrationRequest.setEmail(this.email);
                networkCommunicator.register(registrationRequest, SignUpOptions.this, false);

//            }
                break;
            case NetworkCommunicator.RequestCode.REGISTER:
                if (response != null) {
                    User user = ((ResponseModel<User>) response).data;
                    registrationRequest = new RegistrationRequest();
                    TempStorage.setCountryId(user.getId());
                    TempStorage.setCountryName(user.getStoreName());
                    EventBroadcastHelper.sendLogin(context, user);
                    MixPanel.trackRegister(AppConstants.Tracker.EMAIL, TempStorage.getUser());
                    FirebaseAnalysic.trackRegister(AppConstants.Tracker.EMAIL, TempStorage.getUser());
                    IntercomEvents.successfulLoginIntercom(user.getFirstName() + " " + user.getLastName(), user.getEmail());
                    MyPreferences.getInstance().saveMembershipIcon(true);
                    GetStartedActivity.open(context);
                    TempStorage.setDefaultPage(AppConstants.Tab.TAB_EXPLORE_PAGE);
                    finish();
                }
                break;

            case NetworkCommunicator.RequestCode.UPDATE_STORE_ID:
                StoreModel model = ((ResponseModel<StoreModel>) response).data;
                TempStorage.setCountryId(model.getId());
                TempStorage.setCountryName(model.getName());
        }

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        switch (requestCode) {

            case NetworkCommunicator.RequestCode.LOGIN_FB:
                if (faceBookUser != null)
                    facebookValidation();
                break;
            case NetworkCommunicator.RequestCode.VALIDATE_EMAIL:

               /* if (faceBookUser != null) {
                    networkCommunicator.loginFb(new LoginRequest(faceBookUser.getId(), faceBookUser.getName(), faceBookUser.getLastName(), email, faceBookUser.getGender(), AppConstants.ApiParamValue.LOGINWITHFACEBOOK), SignUpOptions.this, false);
                } else if (mGoogleSignInAccount != null) {
                    registrationRequest.setGoogleId(mGoogleSignInAccount.getId());
                    networkCommunicator.loginFb(new LoginRequest(mGoogleSignInAccount.getId(), mGoogleSignInAccount.getGivenName(), mGoogleSignInAccount.getFamilyName(), email, AppConstants.ApiParamValue.LOGINWITHGOOGLE), SignUpOptions.this, false);
                }
             *//*   if (faceBookUser != null) {
                    networkCommunicator.loginFb(new LoginRequest(faceBookUser.getId(), faceBookUser.getName(), faceBookUser.getLastName(), email, faceBookUser.getGender(), AppConstants.ApiParamValue.LOGINWITHFACEBOOK), SignUpOptions.this, false);
                } else if (mGoogleSignInAccount != null) {
                    networkCommunicator.loginFb(new LoginRequest(mGoogleSignInAccount.getId(), mGoogleSignInAccount.getDisplayName(), "", email, "MALE", AppConstants.ApiParamValue.LOGINWITHGOOGLE), SignUpOptions.this, false);

                }*//*
                else
                    textInputLayoutEmail.setError(errorMessage);*/
                ToastUtils.show(context, errorMessage);
                break;
            case NetworkCommunicator.RequestCode.SEARCH_EMAIL:
                ToastUtils.show(context, errorMessage);
                break;

            case NetworkCommunicator.RequestCode.REGISTER:
                ToastUtils.showFailureResponse(context, errorMessage);
                break;

        }
        layoutProgress.setVisibility(View.GONE);
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
        if (name.length() > 40) {
            textInputLayoutName.setError(context.getResources().getString(R.string.name_length_error));
            return;
        }
        if (lastname.isEmpty()) {
            textInputLayoutLastName.setError(context.getResources().getString(R.string.last_name_required_error));
            return;
        }
        if (lastname.length() > 40) {
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
        networkCommunicator.searchEmail(this.email, this, false);
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
            case R.id.buttonRegister:
                onClickRegister();
                break;

        }
    }

    @OnClick(R.id.textViewLogin)
    public void textViewLogin(View view) {
        TempStorage.setCountryId(0);
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

        Helper.setupEditTextFocusHideKeyboard(editTextName);
        Helper.setupEditTextFocusHideKeyboard(editTextEmail);
        Helper.setupEditTextFocusHideKeyboard(editTextPass);
        Helper.setupEditTextFocusHideKeyboard(editTextConfirmPass);

      /*  if (!getRegistrationRequest().getFirstName().isEmpty()) {
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
        });*/
    }

    private void facebookValidation() {
        String message = "";
        registrationRequest = new RegistrationRequest(faceBookUser.getId(), faceBookUser.getName(), faceBookUser.getLastName(), TempStorage.getCountryId(), AppConstants.ApiParamValue.LOGINWITHFACEBOOK);

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


    public void onClickRegister() {
        if (gender.isEmpty()) {
            ToastUtils.show(this, getString(R.string.gender_required));
        } else
            networkCommunicator.register(registrationRequest, SignUpOptions.this, false);

    }

    private void customizeGoogleButton() {

        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Enter with google");
                tv.setAllCaps(true);
                tv.setCompoundDrawablePadding(10);
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                return;
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

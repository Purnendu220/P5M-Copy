package com.p5m.me.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.p5m.me.R;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.FaceBookUser;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.LoginRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.helper.MyClickSpan;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpOptions extends BaseActivity implements NetworkCommunicator.RequestListener {


    public static void open(Context context) {
        context.startActivity(new Intent(context, SignUpOptions.class));
    }

    @BindView(R.id.textViewBottom)
    TextView textViewBottom;
    @BindView(R.id.buttonLoginFacebook)
    Button buttonLoginFacebook;
    @BindView(R.id.layoutProgress)
    View layoutProgress;

    private CallbackManager callbackManager;
    private FaceBookUser faceBookUser;
    private long loginTime;

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

                                        faceBookUser = new FaceBookUser(id, first_name,last_name, gender, email);

                                        networkCommunicator.loginFb(new LoginRequest(id,first_name,last_name, email, gender), SignUpOptions.this, false);
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
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email","user_gender"));
                layoutProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.buttonLogin)
    public void buttonLogin(View view) {
        RegistrationActivity.open(context);
    }

    @OnClick(R.id.textViewLogin)
    public void textViewLogin(View view) {
        LoginActivity.open(context);
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {

            case NetworkCommunicator.RequestCode.LOGIN_FB:

                if (response != null) {
                    User user = ((ResponseModel<User>) response).data;

                    EventBroadcastHelper.sendLogin(context, user);

                    if (user.getDateOfJoining() >= loginTime) {
                        MixPanel.trackRegister(AppConstants.Tracker.FB, TempStorage.getUser());
                        FirebaseAnalysic.trackRegister(AppConstants.Tracker.FB, TempStorage.getUser());
                    } else
                        MixPanel.trackLogin(AppConstants.Tracker.FB, TempStorage.getUser());

                    HomeActivity.open(context);
                    finish();
                }

                layoutProgress.setVisibility(View.GONE);

                break;
        }

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        switch (requestCode) {

            case NetworkCommunicator.RequestCode.LOGIN_FB:

                RegistrationActivity.open(context, faceBookUser);

                break;
        }
        layoutProgress.setVisibility(View.GONE);
    }
}

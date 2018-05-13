package www.gymhop.p5m.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.FaceBookUser;
import www.gymhop.p5m.data.main.User;
import www.gymhop.p5m.data.request.LoginRequest;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.KeyboardUtils;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.utils.ToastUtils;
import www.gymhop.p5m.view.activity.Main.HomeActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;

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

    @OnClick(R.id.buttonLogin)
    public void buttonLogin(View view) {
        String user = editTextEmail.getText().toString().trim();
        String pass = editTextPassword.getText().toString();

        if (user.isEmpty()) {
            textInputLayoutEmail.setError("Please input your email");
            return;
        }

        if (pass.isEmpty()) {
            textInputLayoutPassword.setError("Please input your password");
            return;
        }

        buttonLogin.setVisibility(View.GONE);
        networkCommunicator.login(new LoginRequest(user, pass), this, false);
    }

    private void SetupFBLogin() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
//                        ToastUtils.showLong(context, loginResult.getAccessToken().getToken());
                        LogUtils.debug("Facebook Token : " + loginResult.getAccessToken().toString());

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {

//                                        ToastUtils.showLong(context, object.toString());
                                        LogUtils.debug("Facebook newMeRequest : " + object.toString());

                                        String name = "";
                                        String gender = "";
                                        String email = "";
                                        String id = "";

                                        try {
                                            id = object.getString("id");
                                            name = object.getString("name");
                                            gender = object.getString("gender").toUpperCase();
                                            email = object.getString("email");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            LogUtils.exception(e);
                                        }

                                        faceBookUser = new FaceBookUser(id, name, gender, email);

                                        networkCommunicator.loginFb(new LoginRequest(id, name, email, gender), LoginActivity.this, false);
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,birthday,gender,email,location");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showLong(context, "Cancelled");
                        layoutProgressRoot.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        ToastUtils.showLong(context, "Exception: " + exception.getMessage());
                        layoutProgressRoot.setVisibility(View.GONE);
                    }
                });

        buttonLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
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
                    EventBroadcastHelper.sendLogin(context, user);
                    HomeActivity.open(context);
                    finish();
                } else {
                    textInputLayoutPassword.setError("Please try again");
                }

                buttonLogin.setVisibility(View.VISIBLE);
            }
            break;

            case NetworkCommunicator.RequestCode.LOGIN_FB:

                if (response != null) {
                    User user = ((ResponseModel<User>) response).data;

                    EventBroadcastHelper.sendLogin(context, user);
                    HomeActivity.open(context);
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
}

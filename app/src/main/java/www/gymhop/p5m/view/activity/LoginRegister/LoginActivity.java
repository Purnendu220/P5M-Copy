package www.gymhop.p5m.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.main.User;
import www.gymhop.p5m.data.request.LoginRequest;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.storage.preferences.MyPreferences;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.KeyboardUtils;
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

    @BindView(R.id.layoutContainer)
    public View layoutContainer;

    private int navigatedFrom;

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

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {

            case NetworkCommunicator.RequestCode.LOGIN:

                buttonLogin.setVisibility(View.VISIBLE);
                User user = ((ResponseModel<User>) response).data;

                if (user != null) {
                    TempStorage.setUser(context, user);
                    MyPreferences.getInstance().setLogin(true);
                    HomeActivity.open(context);
                    finish();
                } else {
                    textInputLayoutPassword.setError("Please try again");
                }
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
        }
    }
}

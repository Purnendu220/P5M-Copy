package www.gymhop.p5m.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.User;
import www.gymhop.p5m.data.request.LoginRequest;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.storage.preferences.MyPreferences;
import www.gymhop.p5m.view.activity.Main.HomeActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class LoginActivity extends BaseActivity implements NetworkCommunicator.RequestListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(activity);

        setEditWatcher();
    }

    private void setEditWatcher() {

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textInputLayoutPassword.setError("");
            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textInputLayoutEmail.setError("");
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

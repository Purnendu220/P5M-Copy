package com.p5m.me.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener, NetworkCommunicator.RequestListener {

    public static void open(Context context) {
        context.startActivity(new Intent(context, ForgotPasswordActivity.class));
    }

    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;
    @BindView(R.id.textInputLayoutEmail)
    TextInputLayout textInputLayoutEmail;

    @BindView(R.id.buttonReset)
    Button buttonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ButterKnife.bind(activity);

        buttonReset.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);

        Helper.setupEditTextFocusHideKeyboard(editTextEmail);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                finish();
                break;
            case R.id.buttonReset:
                sendRequest(editTextEmail.getText().toString());
                break;
        }
    }

    private void sendRequest(String email) {

        if (email.isEmpty()) {
            textInputLayoutEmail.setError(getString(R.string.please_enter_your_email));
            return;
        }

        if (!Helper.validateEmail(email)) {
            textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required_validate));
            return;
        }

        buttonReset.setVisibility(View.GONE);

        networkCommunicator.forgotPassword(email, this, false);
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.FORGOT_PASSWORD:
                String message = ((ResponseModel<String>) response).data;
                buttonReset.setVisibility(View.VISIBLE);

                DialogUtils.showBasicMessage(context, message, "Ok", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        finish();
                    }
                });

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.FORGOT_PASSWORD:
                buttonReset.setVisibility(View.VISIBLE);

                DialogUtils.showBasicMessage(context, errorMessage, "Ok", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

                break;
        }
    }
}

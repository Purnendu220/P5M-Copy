package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.activity.LoginRegister.LoginActivity;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.CustomDialogThankYou;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandCityActivity extends BaseActivity implements View.OnClickListener {

    public static void open(Context context) {
        Intent intent = new Intent(context, ExpandCityActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @BindView(R.id.buttonSubmit)
    public Button buttonSubmit;
    @BindView(R.id.textViewLogin)
    public TextView textViewLogin;
    @BindView(R.id.editTextEmail)
    public EditText editTextEmail;
    @BindView(R.id.textInputLayoutEmail)
    public TextInputLayout textInputLayoutEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_city);
        ButterKnife.bind(activity);
        buttonSubmit.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
        Helper.setupErrorWatcher(editTextEmail, textInputLayoutEmail);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewLogin:
                LoginActivity.open(context);
                break;
            case R.id.buttonSubmit:
                if (!isError())
                    showThankYou();
                break;
        }
    }

    private boolean isError() {
        String email = editTextEmail.getText().toString().trim();
        if (email.isEmpty()) {
            textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required_error));
            return true;
        }

        if (!Helper.validateEmail(email)) {
            textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required_validate));
            return true;
        }

        return false;
    }

    private void showThankYou() {

        CustomDialogThankYou mCustomThankyouDialog = new CustomDialogThankYou(context, false, AppConstants.AppNavigation.NAVIGATION_FROM_OTHER_USER);
        try {
            mCustomThankyouDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

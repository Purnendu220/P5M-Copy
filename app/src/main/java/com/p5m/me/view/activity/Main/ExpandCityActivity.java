package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.p5m.me.R;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.activity.LoginRegister.InfoScreen;
import com.p5m.me.view.activity.LoginRegister.LoginActivity;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.CustomDialogThankYou;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandCityActivity extends BaseActivity implements View.OnClickListener {

    private CustomDialogThankYou mCustomThankyouDialog;

    public static void open(Context context, String countryName) {
        Intent intent = new Intent(context, ExpandCityActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ExpandCityActivity.countryName = countryName;
        context.startActivity(intent);
    }

    @BindView(R.id.buttonSubmit)
    public Button buttonSubmit;
    @BindView(R.id.textViewLogin)
    public TextView textViewLogin;
    @BindView(R.id.textViewHeader)
    public TextView textViewHeader;
    @BindView(R.id.editTextEmail)
    public EditText editTextEmail;
    @BindView(R.id.imageViewBack)
    public ImageView imageViewBack;
    @BindView(R.id.textViewSkip)
    public TextView textViewSkip;
    @BindView(R.id.textInputLayoutEmail)
    public TextInputLayout textInputLayoutEmail;
    public static String countryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_city);
        ButterKnife.bind(activity);
        buttonSubmit.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
        textViewSkip.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);
        Helper.setupErrorWatcher(editTextEmail, textInputLayoutEmail);
        textViewHeader.setText(Html.fromHtml(String.format(mContext.getString(R.string.want_us_to_expand_in_your_city), countryName)));
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
            case R.id.imageViewBack:
                onBackPressed();
                break;
            case R.id.textViewSkip:
                InfoScreen.open(context);
                finish();
                break;
        }
    }

    private boolean isError() {
        String email = editTextEmail.getText().toString().trim();
        if (email.isEmpty()) {
            textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required));
            return true;
        }

        if (!Helper.validateEmail(email)) {
            textInputLayoutEmail.setError(context.getResources().getString(R.string.email_required_validate));
            return true;
        }

        return false;
    }

    private void showThankYou() {

        mCustomThankyouDialog = new CustomDialogThankYou(context, false, AppConstants.AppNavigation.NAVIGATION_FROM_OTHER_USER);
        try {
            mCustomThankyouDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        if (mCustomThankyouDialog != null)
            mCustomThankyouDialog.dismiss();
        super.onPause();

    }

}

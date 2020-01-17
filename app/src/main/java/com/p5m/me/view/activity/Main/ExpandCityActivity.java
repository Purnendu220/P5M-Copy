package com.p5m.me.view.activity.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.view.activity.LoginRegister.LoginActivity;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.CustomDialogThankYou;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandCityActivity extends BaseActivity implements View.OnClickListener {

    public static void open(Context context) {
        context.startActivity(new Intent(context, ExpandCityActivity.class));
    }

    @BindView(R.id.buttonSubmit)
    public Button buttonSubmit;
    @BindView(R.id.textViewLogin)
    public TextView textViewLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_city);
        ButterKnife.bind(activity);
        buttonSubmit.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.textViewLogin:
                LoginActivity.open(context);
                break;
            case R.id.buttonSubmit:
                showThankYou();
                break;
        }
    }

    private void showThankYou() {

        CustomDialogThankYou mCustomThankyouDialog = new CustomDialogThankYou(context, false, AppConstants.AppNavigation.NAVIGATION_FROM_OTHER_USER);
        try {
            mCustomThankyouDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }
}

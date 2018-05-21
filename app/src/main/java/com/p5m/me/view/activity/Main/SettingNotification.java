package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.p5m.me.R;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.UserUpdateRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingNotification extends BaseActivity implements NetworkCommunicator.RequestListener {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, SettingNotification.class));
    }

    @BindView(R.id.switchCompatNotifyFavClass)
    public SwitchCompat switchCompatNotifyFavClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_notification);

        ButterKnife.bind(activity);

        switchCompatNotifyFavClass.setChecked(TempStorage.getUser().getFavTrainerNotification());

        switchCompatNotifyFavClass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                networkCommunicator.updateUser(TempStorage.getUser().getId(), new UserUpdateRequest(status), SettingNotification.this, false);
            }
        });
    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        finish();
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UPDATE_USER:

                User user = ((ResponseModel<User>) response).data;
                EventBroadcastHelper.sendUserUpdate(context, user);
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UPDATE_USER:
                break;
        }
    }
}

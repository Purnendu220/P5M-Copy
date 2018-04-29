package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.User;
import www.gymhop.p5m.data.request.UserUpdateRequest;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.view.activity.base.BaseActivity;

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

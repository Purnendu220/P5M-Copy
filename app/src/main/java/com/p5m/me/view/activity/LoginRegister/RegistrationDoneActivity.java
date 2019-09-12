package com.p5m.me.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.p5m.me.R;
import com.p5m.me.data.main.User;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.p5m.me.utils.AppConstants.Pref.MEMBERSHIP_INFO_STATE_HAVE_PACKAGE;
import static com.p5m.me.utils.AppConstants.Pref.MEMBERSHIP_INFO_STATE_NO_PACKAGE;

public class RegistrationDoneActivity extends BaseActivity {

    public static void open(Context context) {
        Intent intent = new Intent(context, RegistrationDoneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @BindView(R.id.buttonDone)
    public Button buttonDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_done);

        ButterKnife.bind(activity);
        handleMembershipInfoState(TempStorage.getUser());
    }

    @OnClick(R.id.buttonDone)
    public void buttonDone(View view) {
        HomeActivity.open(context);
        finish();
    }
    private void handleMembershipInfoState(User user){
        if (user.isBuyMembership()) {
            TempStorage.setOpenMembershipInfo(MEMBERSHIP_INFO_STATE_NO_PACKAGE);

        }else{
            TempStorage.setOpenMembershipInfo(MEMBERSHIP_INFO_STATE_HAVE_PACKAGE);

        }
    }
}

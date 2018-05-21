package www.gymhop.p5m.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.view.activity.Main.HomeActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;

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
    }

    @OnClick(R.id.buttonDone)
    public void buttonDone(View view) {
        HomeActivity.open(context);
        finish();
    }
}

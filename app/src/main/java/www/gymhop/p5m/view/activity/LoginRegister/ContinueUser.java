package www.gymhop.p5m.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.ImageUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class ContinueUser extends BaseActivity implements View.OnClickListener {

    public static void open(Context context) {
        Intent intent = new Intent(context, ContinueUser.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @BindView(R.id.buttonContinue)
    public Button buttonContinue;
    @BindView(R.id.buttonLogin)
    public Button buttonLogin;
    @BindView(R.id.buttonRegister)
    public Button buttonRegister;
    @BindView(R.id.textViewSwitch)
    public TextView textViewSwitch;
    @BindView(R.id.imageView)
    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_user);

        ButterKnife.bind(activity);

        buttonContinue.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        textViewSwitch.setOnClickListener(this);

        try {
            ImageUtils.setImage(context, TempStorage.getUser().getProfileImage(), R.drawable.profile_holder, imageView);
            buttonContinue.setText("Continue as "+TempStorage.getUser().getFirstName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonContinue:
                LoginActivity.open(context, AppConstants.AppNavigation.NAVIGATION_FROM_CONTINUE_USER);
                break;
            case R.id.buttonLogin:
                LoginActivity.open(context);
                break;
            case R.id.buttonRegister:
                SignUpOptions.open(context);
                break;
            case R.id.textViewSwitch:
                LoginActivity.open(context);
                break;
        }
    }
}

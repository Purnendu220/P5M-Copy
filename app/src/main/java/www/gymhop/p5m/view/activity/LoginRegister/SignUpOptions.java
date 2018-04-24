package www.gymhop.p5m.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class SignUpOptions extends BaseActivity {

    public static void open(Context context) {
        context.startActivity(new Intent(context, SignUpOptions.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_options);

        ButterKnife.bind(activity);
    }

    @OnClick(R.id.buttonLogin)
    public void buttonLogin(View view) {

        RegistrationActivity.open(context);
    }

    @OnClick(R.id.textViewLogin)
    public void textViewLogin(View view) {
        LoginActivity.open(context);
    }
}

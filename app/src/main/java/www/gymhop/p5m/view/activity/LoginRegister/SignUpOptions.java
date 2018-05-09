package www.gymhop.p5m.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.helper.MyClickSpan;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class SignUpOptions extends BaseActivity {

    public static void open(Context context) {
        context.startActivity(new Intent(context, SignUpOptions.class));
    }

    @BindView(R.id.textViewBottom)
    TextView textViewBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_options);

        ButterKnife.bind(activity);

        String spanText = "By login you are agree to our terms of service and privacy policy";
        String terms = "terms of service";
        String policy = "privacy policy";

        Spannable span = Spannable.Factory.getInstance().newSpannable(spanText);

        span.setSpan(new MyClickSpan(context) {
            @Override
            public void onSpanClicked() {
                Helper.openWebPage(context, AppConstants.Url.WEBSITE + "terms");
            }
        }, spanText.indexOf(terms), spanText.indexOf(terms) + terms.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        span.setSpan(new MyClickSpan(context) {
            @Override
            public void onSpanClicked() {
                Helper.openWebPage(context, AppConstants.Url.WEBSITE + "privacy");
            }
        }, spanText.indexOf(policy), spanText.indexOf(policy) + policy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewBottom.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
        textViewBottom.setMovementMethod(LinkMovementMethod.getInstance());
        textViewBottom.setText(span);
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

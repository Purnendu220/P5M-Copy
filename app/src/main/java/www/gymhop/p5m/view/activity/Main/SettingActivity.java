package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.layoutNotification)
    public View layoutNotification;
    @BindView(R.id.layoutMembership)
    public View layoutMembership;
    @BindView(R.id.layoutTransHistory)
    public View layoutTransHistory;

    @BindView(R.id.layoutContactUs)
    public View layoutContactUs;
    @BindView(R.id.layoutPrivacyPolicy)
    public View layoutPrivacyPolicy;
    @BindView(R.id.layoutTermsCondition)
    public View layoutTermsCondition;
    @BindView(R.id.layoutAboutUs)
    public View layoutAboutUs;

    @BindView(R.id.layoutLogout)
    public View layoutLogout;

    @BindView(R.id.imageViewLogout)
    public View imageViewLogout;
    @BindView(R.id.progressBarLogout)
    public View progressBarLogout;

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(activity);

        layoutNotification.setOnClickListener(this);
        layoutMembership.setOnClickListener(this);
        layoutTransHistory.setOnClickListener(this);
        layoutContactUs.setOnClickListener(this);
        layoutPrivacyPolicy.setOnClickListener(this);
        layoutTermsCondition.setOnClickListener(this);
        layoutAboutUs.setOnClickListener(this);
        layoutLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutNotification:
                break;
            case R.id.layoutMembership:
                break;
            case R.id.layoutTransHistory:
                break;
            case R.id.layoutContactUs:
                break;
            case R.id.layoutPrivacyPolicy:
                break;
            case R.id.layoutTermsCondition:
                break;
            case R.id.layoutAboutUs:
                break;
            case R.id.layoutLogout:
                break;
        }
    }
}

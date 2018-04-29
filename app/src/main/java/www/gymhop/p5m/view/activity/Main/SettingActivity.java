package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.request.LogoutRequest;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.DialogUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class SettingActivity extends BaseActivity implements View.OnClickListener, NetworkCommunicator.RequestListener {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

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

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layoutNotification:
                SettingNotification.openActivity(context);
                break;
            case R.id.layoutMembership:
                MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_SETTING);
                break;
            case R.id.layoutTransHistory:
                TransactionHistoryActivity.openActivity(context);
                break;
            case R.id.layoutContactUs:
                dialogContactUs();
                break;
            case R.id.layoutPrivacyPolicy:
                Helper.openWebPage(context, AppConstants.Url.WEBSITE + "privacy");
                break;
            case R.id.layoutTermsCondition:
                Helper.openWebPage(context, AppConstants.Url.WEBSITE + "terms");
                break;
            case R.id.layoutAboutUs:
                Helper.openWebPage(context, AppConstants.Url.WEBSITE + "aboutus");
                break;
            case R.id.layoutLogout:
                imageViewLogout.setVisibility(View.GONE);
                progressBarLogout.setVisibility(View.VISIBLE);
                networkCommunicator.logout(new LogoutRequest(TempStorage.getUser().getId()), this, false);
                break;
        }
    }

    private void dialogContactUs() {
        final List<String> items = new ArrayList<>();
        items.add("Mail Us");
        items.add("Make a Call");

        DialogUtils.showBasicList(
                context,
                "Contact Us",
                items,
                new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        dialog.dismiss();
                        if (position == 0) {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", "info@p5m.me", null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Please identify the details of your issue below. A member of our staff will respond shortly.");
                            startActivity(Intent.createChooser(emailIntent, "Send Email"));

                        } else if (position == 1) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:0096555028111"));
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.LOGOUT:
                imageViewLogout.setVisibility(View.VISIBLE);
                progressBarLogout.setVisibility(View.GONE);

                EventBroadcastHelper.logout(context);
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.LOGOUT:
                imageViewLogout.setVisibility(View.VISIBLE);
                progressBarLogout.setVisibility(View.GONE);
                break;
        }
    }
}

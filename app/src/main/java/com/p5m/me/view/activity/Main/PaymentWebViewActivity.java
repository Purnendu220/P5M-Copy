package com.p5m.me.view.activity.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.PaymentUrl;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.AppConstants.DataKey.REFERENCE_ID;

public class PaymentWebViewActivity extends BaseActivity implements NetworkCommunicator.RequestListener {

    public static void open(Activity activity, String couponCode, String packageName, ClassModel classModel, PaymentUrl paymentUrl) {
        PaymentWebViewActivity.couponCode = couponCode;
        PaymentWebViewActivity.packageName = packageName;
        PaymentWebViewActivity.classModel = classModel;

        activity.startActivityForResult(new Intent(activity, PaymentWebViewActivity.class)
                .putExtra(AppConstants.DataKey.PAYMENT_URL_OBJECT, paymentUrl), AppConstants.ResultCode.PAYMENT_SUCCESS);
    }

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.layoutProgress)
    View layoutProgress;
    @BindView(R.id.layoutHide)
    View layoutHide;

    private static String couponCode;
    private static String packageName;
    private static ClassModel classModel;

    private PaymentUrl paymentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);

        ButterKnife.bind(activity);

        openPage(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        openPage(intent);
    }

    private void openPage(Intent intent) {

        paymentUrl = (PaymentUrl) getIntent().getSerializableExtra(AppConstants.DataKey.PAYMENT_URL_OBJECT);

        if (paymentUrl == null) {
            finish();
            return;
        }

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.getSettings().setLoadWithOverviewMode(true);

        layoutProgress.setVisibility(View.VISIBLE);
        webView.loadUrl(paymentUrl.getPaymentURL());
        webView.setWebViewClient(new MyWebViewClient());
    }

    private void paymentSuccessful(String url) {
//        networkCommunicator.getMyUser(this, false);
        String refId=getRefferenceId (url);
//        Toast.makeText(this,refId,Toast.LENGTH_LONG).show();
        MixPanel.trackMembershipPurchase(couponCode, packageName);

        if (classModel != null) {
            MixPanel.trackJoinClass(AppConstants.Tracker.PURCHASE_PLAN, classModel);
        }

        overridePendingTransition(0, 0);

        Intent returnIntent = getIntent();
        returnIntent.putExtra(REFERENCE_ID,refId);
        setResult(RESULT_OK,returnIntent);
        finish();
        overridePendingTransition(0, 0);

//        EventBroadcastHelper.sendPackagePurchased();
//
//        webView.setVisibility(View.INVISIBLE);
//        layoutProgress.setVisibility(View.VISIBLE);
//        setResult(RESULT_OK);
//        finish();
    }

    private String getRefferenceId (String url) {
        if(url.indexOf("refId=") == -1)
            return "";
        String id  = url.split("refId=")[1];
        return id;
    }


    @Override
    public void onApiSuccess(Object response, int requestCode) {
        EventBroadcastHelper.sendPackagePurchased();

        webView.setVisibility(View.INVISIBLE);
        layoutProgress.setVisibility(View.VISIBLE);
        setResult(RESULT_OK);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        EventBroadcastHelper.sendPackagePurchased();

        webView.setVisibility(View.INVISIBLE);
        layoutProgress.setVisibility(View.VISIBLE);
        setResult(RESULT_OK);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);

    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            LogUtils.debug("Payment shouldOverrideUrlLoading " + url);

            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            }

            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            LogUtils.debug("Payment shouldOverrideUrlLoading " + request.toString());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (request.getUrl().toString().startsWith("http:") || request.getUrl().toString().startsWith("https:")) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            layoutProgress.setVisibility(View.GONE);
            LogUtils.debug("Payment onPageFinished " + url);

            if (url.toString().contains("profive-midl/api/v1/paymentsuccesspage")) {
                webView.setVisibility(View.INVISIBLE);
                layoutProgress.setVisibility(View.VISIBLE);
            } else {
                webView.setVisibility(View.VISIBLE);
                layoutProgress.setVisibility(View.GONE);
            }

            if (url.contains("intent://com.profive.android.view.activity.SplashScreenActivity")) {

                paymentSuccessful(url);
                return;
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            LogUtils.debug("Payment onLoadResource" + url);
//            layoutProgress.setVisibility(View.VISIBLE);


//            if (url.toString().contains("profive-midl/api/v1/paymentsuccesspage")) {
//                webView.setVisibility(View.INVISIBLE);
//                layoutProgress.setVisibility(View.VISIBLE);
//            } else {
//                webView.setVisibility(View.VISIBLE);
//                layoutProgress.setVisibility(View.GONE);
//            }

//            if (url.equalsIgnoreCase("intent://com.profive.android.view.activity.SplashScreenActivity")) {
//                paymentSuccessful();
//                return;
//            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
//            layoutProgress.setVisibility(View.GONE);

            LogUtils.debug("Payment onReceivedHttpError");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            webView.setVisibility(View.INVISIBLE);
            LogUtils.debug("Payment onReceivedError");
//            layoutProgress.setVisibility(View.VISIBLE);
//            layoutHide.setVisibility(View.VISIBLE);

            super.onReceivedError(view, request, error);
        }
    }

    @Override
    public void onBackPressed() {
        DialogUtils.showBasicMessage(context, getString(R.string.are_you_sure), getString(R.string.to_complete_the_payment_you_should_stay_on_this_page),
                getString(R.string.stay_on_this_page), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }, getString(R.string.leave_this_page), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        overridePendingTransition(0, 0);
                        MixPanel.trackSequentialUpdate(AppConstants.Tracker.PURCHASE_CANCEL);
                        PaymentWebViewActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        networkCommunicator.getMyUser(this, false);
        super.onDestroy();
    }
}

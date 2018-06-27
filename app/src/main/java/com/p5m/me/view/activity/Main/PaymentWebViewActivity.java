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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.PaymentUrl;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentWebViewActivity extends BaseActivity implements NetworkCommunicator.RequestListener {

    public static void open(Activity activity, String couponCode, String packageName, PaymentUrl paymentUrl) {
        PaymentWebViewActivity.couponCode = couponCode;
        PaymentWebViewActivity.packageName = packageName;
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

    private void paymentSuccessful() {
//        networkCommunicator.getMyUser(this, false);
        setResult(RESULT_OK);

        MixPanel.trackMembershipPurchase(couponCode, packageName);

        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);

//        EventBroadcastHelper.sendPackagePurchased();
//
//        webView.setVisibility(View.INVISIBLE);
//        layoutProgress.setVisibility(View.VISIBLE);
//        setResult(RESULT_OK);
//        finish();
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

            if (url.equalsIgnoreCase("intent://com.profive.android.view.activity.SplashScreenActivity")) {
                paymentSuccessful();
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
        DialogUtils.showBasicMessage(context, "Are you sure?", "To complete the payment you should stay on this page",
                "Stay on this page", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }, "Leave this page", new MaterialDialog.SingleButtonCallback() {
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

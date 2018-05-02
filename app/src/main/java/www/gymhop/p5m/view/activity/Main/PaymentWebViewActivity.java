package www.gymhop.p5m.view.activity.Main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.main.PaymentUrl;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.DialogUtils;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class PaymentWebViewActivity extends BaseActivity implements NetworkCommunicator.RequestListener {

    public static void open(Activity activity, PaymentUrl paymentUrl) {
        activity.startActivityForResult(new Intent(activity, PaymentWebViewActivity.class)
                .putExtra(AppConstants.DataKey.PAYMENT_URL_OBJECT, paymentUrl), AppConstants.ResultCode.PAYMENT_SUCCESS);
    }

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private PaymentUrl paymentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web_view);

        ButterKnife.bind(activity);

        webView.getSettings().setJavaScriptEnabled(true);

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

        progressBar.setVisibility(View.VISIBLE);
        webView.loadUrl(paymentUrl.getPaymentURL());
        webView.setWebViewClient(new MyWebViewClient());
    }

    private void paymentSuccessful() {
        networkCommunicator.getMyUser(this, false);

//        EventBroadcastHelper.sendPackagePurchased();
//
//        webView.setVisibility(View.INVISIBLE);
//        progressBar.setVisibility(View.VISIBLE);
//        setResult(RESULT_OK);
//        finish();
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        EventBroadcastHelper.sendPackagePurchased();

        webView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        EventBroadcastHelper.sendPackagePurchased();

        webView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        setResult(RESULT_OK);
        finish();
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            LogUtils.debug("Payment onPageFinished " + url);

            if (url.equalsIgnoreCase("intent://com.profive.android.view.activity.SplashScreenActivity")) {
                paymentSuccessful();
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Uri uri = Uri.parse(url);
            LogUtils.debug("Payment onLoadResource" + url);

            if (url.equalsIgnoreCase("intent://com.profive.android.view.activity.SplashScreenActivity")) {
                paymentSuccessful();
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }
    }

    @Override
    public void onBackPressed() {
        DialogUtils.showBasic(context, "Are you sure want to exit?" +
                "\nyour transaction will be lost", "Exit", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        networkCommunicator.getMyUser(this, false);
        super.onDestroy();
    }
}

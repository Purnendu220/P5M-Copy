package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.PaymentUrl;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class PaymentWebViewActivity extends BaseActivity {

    public static void open(Context context, PaymentUrl paymentUrl) {
        context.startActivity(new Intent(context, PaymentWebViewActivity.class)
                .putExtra(AppConstants.DataKey.PAYMENT_URL_OBJECT, paymentUrl));
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

        paymentUrl = (PaymentUrl) getIntent().getSerializableExtra(AppConstants.DataKey.PAYMENT_URL_OBJECT);

        if (paymentUrl == null) {
            finish();
            return;
        }

        webView = (WebView) findViewById(R.id.webView);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF0000FF, android.graphics.PorterDuff.Mode.MULTIPLY);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(paymentUrl.getPaymentURL());
        webView.setWebViewClient(new MyWebViewClient());
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            LogUtils.debug("Payment onPageFinished " + url);
            if (url.equalsIgnoreCase("intent://com.profive.android.view.activity.SplashScreenActivity")) {

                webView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                setResult(AppConstants.ResultCode.PAYMENT_SUCCESS);
                finish();
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Uri uri = Uri.parse(url);
            LogUtils.debug("Payment onLoadResource" + url);
            if (url.equalsIgnoreCase("intent://com.profive.android.view.activity.SplashScreenActivity")) {

                webView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                setResult(AppConstants.ResultCode.PAYMENT_SUCCESS);
                finish();
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }
    }
}

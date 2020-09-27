package com.p5m.me.view.activity.Main;


        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.view.View;
        import android.webkit.RenderProcessGoneDetail;
        import android.webkit.WebChromeClient;
        import android.webkit.WebResourceError;
        import android.webkit.WebResourceRequest;
        import android.webkit.WebResourceResponse;
        import android.webkit.WebSettings;
        import android.webkit.WebView;
        import android.webkit.WebViewClient;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import androidx.annotation.NonNull;

        import com.afollestad.materialdialogs.DialogAction;
        import com.afollestad.materialdialogs.MaterialDialog;
        import com.p5m.me.R;
        import com.p5m.me.analytics.FirebaseAnalysic;
        import com.p5m.me.analytics.IntercomEvents;
        import com.p5m.me.analytics.MixPanel;
        import com.p5m.me.data.UserPackageInfo;
        import com.p5m.me.data.main.ClassModel;
        import com.p5m.me.data.main.PaymentUrl;
        import com.p5m.me.eventbus.EventBroadcastHelper;
        import com.p5m.me.restapi.NetworkCommunicator;
        import com.p5m.me.storage.TempStorage;
        import com.p5m.me.utils.AppConstants;
        import com.p5m.me.utils.DateUtils;
        import com.p5m.me.utils.DialogUtils;
        import com.p5m.me.utils.LogUtils;
        import com.p5m.me.view.activity.base.BaseActivity;


        import butterknife.BindView;
        import butterknife.ButterKnife;

        import static com.p5m.me.utils.AppConstants.DataKey.REFERENCE_ID;
        import static com.p5m.me.utils.AppConstants.DataKey.USER_HAVE_PACKAGE;

public class WebViewActivity extends BaseActivity implements NetworkCommunicator.RequestListener {

    public static void open(Activity activity, String urlToLoad) {

        activity.startActivity(new Intent(activity, WebViewActivity.class)
                .putExtra(AppConstants.DataKey.URL_OBJECT_TO_LOAD, urlToLoad));
    }
    public static Intent createIntent(Context activity, String urlToLoad) {
        Intent intent = new Intent(activity, WebViewActivity.class);

        intent.putExtra(AppConstants.DataKey.URL_OBJECT_TO_LOAD, urlToLoad);
        return intent;
    }

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.layoutProgress)
    View layoutProgress;
    @BindView(R.id.layoutHide)
    View layoutHide;

    String urlToLoad;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ButterKnife.bind(activity);

        openPage(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        openPage(intent);
    }

    private void openPage(Intent intent) {

        urlToLoad = getIntent().getStringExtra(AppConstants.DataKey.URL_OBJECT_TO_LOAD);

        if (urlToLoad == null) {
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
        webView.getSettings().setAllowFileAccess(true);
        layoutProgress.setVisibility(View.VISIBLE);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                LogUtils.networkError("progress: "+progress);
            }
        });

        webView.loadUrl(urlToLoad);
        webView.setWebViewClient(new MyWebViewClient());


    }





    @Override
    public void onApiSuccess(Object response, int requestCode) {
        EventBroadcastHelper.sendPackagePurchased();

        webView.setVisibility(View.INVISIBLE);
        layoutProgress.setVisibility(View.VISIBLE);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        EventBroadcastHelper.sendPackagePurchased();

        webView.setVisibility(View.INVISIBLE);
        layoutProgress.setVisibility(View.VISIBLE);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);

    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            LogUtils.debug("Payment shouldOverrideUrlLoading " + url);

            return !url.startsWith("http:") && !url.startsWith("https:");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

            LogUtils.debug("Payment shouldOverrideUrlLoading " + request.toString());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return !request.getUrl().toString().startsWith("http:") && !request.getUrl().toString().startsWith("https:");
            }

            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            layoutProgress.setVisibility(View.GONE);
            LogUtils.debug("Web view onPageFinished " + url);
            webView.setVisibility(View.VISIBLE);
            layoutProgress.setVisibility(View.GONE);



        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            LogUtils.debug("Payment onLoadResource" + url);

        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            LogUtils.debug("Web View onReceivedHttpError");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            LogUtils.debug("web View onReceivedError");
            super.onReceivedError(view, request, error);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
   finish();
    }

    @Override
    protected void onDestroy() {
        networkCommunicator.getMyUser(this, false);
        super.onDestroy();
    }




}

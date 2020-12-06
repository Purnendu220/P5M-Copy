package com.p5m.me.view.activity.Main;


        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.CountDownTimer;
        import android.view.View;
        import android.webkit.JavascriptInterface;
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
        import android.widget.Toast;

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
        import com.p5m.me.utils.JsonUtils;
        import com.p5m.me.utils.LogUtils;
        import com.p5m.me.utils.ToastUtils;
        import com.p5m.me.view.activity.base.BaseActivity;


        import butterknife.BindView;
        import butterknife.ButterKnife;

        import static com.p5m.me.utils.AppConstants.DataKey.REFERENCE_ID;
        import static com.p5m.me.utils.AppConstants.DataKey.USER_HAVE_PACKAGE;

public class WebViewActivity extends BaseActivity implements NetworkCommunicator.RequestListener {

    public static void open(Context activity, String urlToLoad) {

        activity.startActivity(new Intent(activity, WebViewActivity.class)
                .putExtra(AppConstants.DataKey.URL_OBJECT_TO_LOAD, urlToLoad));
    }
    public static void open(Activity activity, String urlToLoad,String packageName,String couponCode,ClassModel classModel,boolean isFromSpecialProgram ) {

        activity.startActivityForResult(new Intent(activity, WebViewActivity.class)
                .putExtra(AppConstants.DataKey.URL_OBJECT_TO_LOAD, urlToLoad).putExtra(AppConstants.DataKey.IS_FROM_SPECIAL_PROGRAM,isFromSpecialProgram).putExtra(AppConstants.DataKey.PACKAGE_NAME_STRING,packageName).putExtra(AppConstants.DataKey.PROMO_CODE_STRING,couponCode).putExtra(AppConstants.DataKey.CLASS_MODEL,classModel),AppConstants.ResultCode.SUBSCRIPTION_SUCCESS);
    }
    public static void open(Activity activity, String urlToLoad,boolean isFromSpecialProgram ) {
        activity.startActivityForResult(new Intent(activity, WebViewActivity.class)
                .putExtra(AppConstants.DataKey.URL_OBJECT_TO_LOAD, urlToLoad).putExtra(AppConstants.DataKey.IS_FROM_CHECKOUT,isFromSpecialProgram),AppConstants.ResultCode.SUBSCRIPTION_SUCCESS);



    }
    public static void open(Context activity, String urlToLoad,boolean isFromSpecialProgram ) {
        activity.startActivity(new Intent(activity, WebViewActivity.class)
                .putExtra(AppConstants.DataKey.URL_OBJECT_TO_LOAD, urlToLoad).putExtra(AppConstants.DataKey.IS_FROM_CHECKOUT,isFromSpecialProgram));



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

    @BindView(R.id.text_back)
    TextView text_back;

    String urlToLoad,packageName,coupanCode;
    boolean isFromSpecialProgram,isFromCheckout,userHavePackage;
    ClassModel classModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ButterKnife.bind(activity);
        UserPackageInfo userPackageInfo = new UserPackageInfo(TempStorage.getUser());
        if(userPackageInfo.haveGeneralPackage&&userPackageInfo.userPackageGeneral!=null){
            userHavePackage = true;
        }
        openPage(getIntent());
        text_back.setOnClickListener(v -> DialogUtils.showBasicMessage(context, getString(R.string.are_you_sure), getString(R.string.to_join_the_program_you_should_stay_on_this_page),
                getString(R.string.stay_on_this_page), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                }, getString(R.string.leave_this_page), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
finish();
                    }
                }));

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        openPage(intent);
    }

    private void openPage(Intent intent) {

        urlToLoad = getIntent().getStringExtra(AppConstants.DataKey.URL_OBJECT_TO_LOAD);
        isFromSpecialProgram = getIntent().getBooleanExtra(AppConstants.DataKey.IS_FROM_SPECIAL_PROGRAM,false);
        isFromCheckout = getIntent().getBooleanExtra(AppConstants.DataKey.IS_FROM_CHECKOUT,false);
        coupanCode = getIntent().getStringExtra(AppConstants.DataKey.PROMO_CODE_STRING);
        packageName = getIntent().getStringExtra(AppConstants.DataKey.PACKAGE_NAME_STRING);
        classModel = (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_MODEL);
        if (urlToLoad == null) {
            finish();
            return;
        }

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

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
        webView.addJavascriptInterface(new WebAppInterface(0), "onExitWithoutPayment");
        webView.addJavascriptInterface(new WebAppInterface(2), "onPaymentSuccess");
        webView.addJavascriptInterface(new WebAppInterface(1), "onPaymentFail");




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
    public class WebAppInterface {
         int type;
        WebAppInterface(int type) {
            this.type = type;
        }

        @JavascriptInterface
        public void postMessage(String message) {
            LogUtils.debug(message);
            switch (type){
                case 0: //Exit Without Payment
                case 1://Payment Fail

                    ToastUtils.show(mContext,message);
                    finish();
                    break;
                    case 2://Payment Success
                     PaymentUrl paymentUrlResponse  =   JsonUtils.fromJson(message,PaymentUrl.class);
                     if(paymentUrlResponse.getCompleted()){
                         paymentSuccessful(paymentUrlResponse);
                     }
                     else{
                         Toast.makeText(mContext, "Subscription  failed", Toast.LENGTH_SHORT).show();
                         finish();

                     }

                    break;

            }

        }
    }

    private void paymentSuccessful(PaymentUrl paymentUrlResponse) {
        MixPanel.trackMembershipPurchase(coupanCode, packageName);
        FirebaseAnalysic.trackMembershipPurchase(coupanCode, packageName);
        if (classModel != null) {
            MixPanel.trackJoinClass(AppConstants.Tracker.PURCHASE_PLAN, classModel);
            FirebaseAnalysic.trackJoinClass(AppConstants.Tracker.PURCHASE_PLAN, classModel);
        }
        overridePendingTransition(0, 0);
        Intent returnIntent = getIntent();
        returnIntent.putExtra(REFERENCE_ID, paymentUrlResponse.getReferenceID());
        returnIntent.putExtra(USER_HAVE_PACKAGE,userHavePackage);
        setResult(RESULT_OK, returnIntent);
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
        if(isFromSpecialProgram){
            DialogUtils.showBasicMessage(context, getString(R.string.are_you_sure), getString(R.string.to_join_the_program_you_should_stay_on_this_page),
                    getString(R.string.stay_on_this_page), new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }, getString(R.string.leave_this_page), new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
        }
       else if(isFromCheckout){
           finish();
        }

        else{
            finish();

        }
    }

    @Override
    protected void onDestroy() {
        networkCommunicator.getMyUser(this, false);
        super.onDestroy();
    }




}

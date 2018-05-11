package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.main.Package;
import www.gymhop.p5m.data.main.PaymentUrl;
import www.gymhop.p5m.data.PromoCode;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.request.PaymentUrlRequest;
import www.gymhop.p5m.data.request.PromoCodeRequest;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.utils.ToastUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

import static www.gymhop.p5m.view.activity.Main.CheckoutActivity.CheckoutFor.CLASS_PURCHASE_WITH_PACKAGE;
import static www.gymhop.p5m.view.activity.Main.CheckoutActivity.CheckoutFor.PACKAGE;
import static www.gymhop.p5m.view.activity.Main.CheckoutActivity.CheckoutFor.SPECIAL_CLASS;

public class CheckoutActivity extends BaseActivity implements View.OnClickListener, NetworkCommunicator.RequestListener {

    /*
        if user is purchasing a package
        */
    public static void openActivity(Context context, Package aPackage) {
        CheckoutActivity.aPackage = aPackage;
        CheckoutActivity.checkoutFor = PACKAGE;

        openActivity(context);
    }

    /*
    if user is purchasing a class via package
     */
    public static void openActivity(Context context, Package aPackage, ClassModel classModel) {
        CheckoutActivity.aPackage = aPackage;
        CheckoutActivity.classModel = classModel;
        CheckoutActivity.checkoutFor = CLASS_PURCHASE_WITH_PACKAGE;

        openActivity(context);
    }

    /*
    if user is purchasing a special class
     */
    public static void openActivity(Context context, ClassModel specialClassModel) {
        CheckoutActivity.checkoutFor = SPECIAL_CLASS;
        CheckoutActivity.classModel = specialClassModel;

        openActivity(context);
    }

    private static void openActivity(Context context) {
        Intent intent = new Intent(context, CheckoutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public enum CheckoutFor {
        PACKAGE,
        SPECIAL_CLASS,
        CLASS_PURCHASE_WITH_PACKAGE
    }

    private static Package aPackage;
    private static ClassModel classModel;
    private static CheckoutFor checkoutFor;

    @BindView(R.id.textViewPackageName)
    TextView textViewPackageName;
    @BindView(R.id.textViewPrice)
    TextView textViewPrice;
    @BindView(R.id.textViewPackageClasses)
    TextView textViewPackageClasses;
    @BindView(R.id.textViewPackageValidity)
    TextView textViewPackageValidity;
    @BindView(R.id.textViewPackageInfo)
    TextView textViewPackageInfo;
    @BindView(R.id.textViewPromoCodePrice)
    TextView textViewPromoCodePrice;
    @BindView(R.id.textViewLimit)
    TextView textViewLimit;
    @BindView(R.id.textViewCancellationPolicyToggle)
    TextView textViewCancellationPolicyToggle;
    @BindView(R.id.textViewCancellationPolicy)
    TextView textViewCancellationPolicy;

    @BindView(R.id.textViewTotal)
    TextView textViewTotal;
    @BindView(R.id.textViewPay)
    TextView textViewPay;

    @BindView(R.id.imageViewPackageImage)
    ImageView imageViewPackageImage;

    @BindView(R.id.buttonPromoCode)
    Button buttonPromoCode;

    @BindView(R.id.layoutPromoCode)
    View layoutPromoCode;
    @BindView(R.id.layoutSpecialClassDetails)
    View layoutSpecialClassDetails;
    @BindView(R.id.layoutNormalClassDetails)
    View layoutNormalClassDetails;

    private PromoCode promoCode;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ButterKnife.bind(activity);

        setData();

        textViewPay.setOnClickListener(this);
        buttonPromoCode.setOnClickListener(this);
        textViewLimit.setOnClickListener(this);
        textViewCancellationPolicyToggle.setOnClickListener(this);
    }

    private void setData() {
        switch (checkoutFor) {
            case PACKAGE:
            case CLASS_PURCHASE_WITH_PACKAGE:
                layoutSpecialClassDetails.setVisibility(View.GONE);
                layoutNormalClassDetails.setVisibility(View.VISIBLE);
                layoutPromoCode.setVisibility(View.GONE);

                if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                    textViewPackageValidity.setText("Valid for " + aPackage.getDuration() + " " + aPackage.getValidityPeriod().toLowerCase());
                } else if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                    textViewPackageValidity.setText("Valid for " + aPackage.getGymName());
                }

                textViewPackageName.setText(Html.fromHtml("1X <b>" + aPackage.getName() + "</b>"));
                textViewPrice.setText(aPackage.getCost() + " " + context.getString(R.string.currency));
                textViewPackageInfo.setText(aPackage.getDescription());
                textViewPackageClasses.setText(aPackage.getNoOfClass() + " classes");
                buttonPromoCode.setVisibility(View.VISIBLE);
                Helper.setPackageImage(imageViewPackageImage, aPackage.getName());

                setPrice();

                break;
            case SPECIAL_CLASS:
                layoutSpecialClassDetails.setVisibility(View.VISIBLE);
                layoutNormalClassDetails.setVisibility(View.GONE);
                layoutPromoCode.setVisibility(View.GONE);
                buttonPromoCode.setVisibility(View.GONE);

                textViewPackageName.setText(Html.fromHtml("<b>" + classModel.getTitle() + "</b>"));
                textViewPrice.setText(classModel.getPrice() + " " + context.getString(R.string.currency));

                textViewCancellationPolicy.setText(classModel.getReminder());
                Helper.setPackageImage(imageViewPackageImage, "Ready");

                setPrice();

                break;
        }
    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewCancellationPolicyToggle:
                if (textViewCancellationPolicy.getVisibility() == View.VISIBLE) {
                    textViewCancellationPolicy.setVisibility(View.GONE);
                } else {
                    textViewCancellationPolicy.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.textViewLimit:
                PackageLimitsActivity.openActivity(context);
                break;
            case R.id.textViewPay:
                handlePayment();
                break;
            case R.id.buttonPromoCode:
                if (promoCode == null) {
                    // Dialog Promo Code
                    dialogPromoCode();
                } else {
                    // Remove Promo Code
                    promoCode = null;
                    layoutPromoCode.setVisibility(View.GONE);

                    setPrice();
                }
                break;
        }
    }

    private void setPrice() {

        switch (checkoutFor) {
            case PACKAGE:
            case CLASS_PURCHASE_WITH_PACKAGE:

                if (promoCode != null) {
                    textViewTotal.setText(promoCode.getPriceAfterDiscount() + " " + context.getString(R.string.currency));
                    textViewPay.setText("Pay " + promoCode.getPriceAfterDiscount() + " " + context.getString(R.string.currency));
                    textViewPromoCodePrice.setText("- " + (aPackage.getCost() - promoCode.getPriceAfterDiscount()) + " " + context.getString(R.string.currency));
                    layoutPromoCode.setVisibility(View.VISIBLE);
                    buttonPromoCode.setText(context.getString(R.string.remove_promo_code));

                    try {
                        materialDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.exception(e);
                    }

                } else {
                    layoutPromoCode.setVisibility(View.GONE);
                    textViewTotal.setText(aPackage.getCost() + " " + context.getString(R.string.currency));
                    textViewPay.setText("Pay " + aPackage.getCost() + " " + context.getString(R.string.currency));
                    buttonPromoCode.setText(context.getString(R.string.apply_promo_code));
                }
                break;

            case SPECIAL_CLASS:
                textViewTotal.setText(classModel.getPrice() + " " + context.getString(R.string.currency));
                textViewPay.setText("Pay " + classModel.getPrice() + " " + context.getString(R.string.currency));

                break;
        }
    }

    private void dialogPromoCode() {

        materialDialog = new MaterialDialog.Builder(context)
                .cancelable(false)
                .customView(R.layout.dialog_promo_code, false)
                .build();
        materialDialog.show();

        final TextView textViewError = (TextView) materialDialog.findViewById(R.id.textViewError);
        final TextView textViewOk = (TextView) materialDialog.findViewById(R.id.textViewOk);
        final TextView textViewCancel = (TextView) materialDialog.findViewById(R.id.textViewCancel);
        final EditText editTextPromoCode = (EditText) materialDialog.findViewById(R.id.editTextPromoCode);

        textViewError.setText("");

        editTextPromoCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                textViewError.setText("");
            }
        });

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.dismiss();
            }
        });

        textViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String promoCodeText = editTextPromoCode.getText().toString();

                if (promoCodeText.isEmpty()) {
                    textViewError.setText(context.getString(R.string.promo_code_required));
                    return;
                }
                switch (checkoutFor) {
                    case PACKAGE:
                        textViewOk.setVisibility(View.INVISIBLE);
                        networkCommunicator.applyPromoCode(
                                new PromoCodeRequest(promoCodeText, aPackage.getId(), TempStorage.getUser().getId()),
                                CheckoutActivity.this, false);
                        break;
                    case CLASS_PURCHASE_WITH_PACKAGE:
                        textViewOk.setVisibility(View.INVISIBLE);
                        networkCommunicator.applyPromoCode(
                                new PromoCodeRequest(classModel.getGymBranchDetail().getGymId(), promoCodeText, aPackage.getId(), TempStorage.getUser().getId()),
                                CheckoutActivity.this, false);
                        break;
                }
            }
        });
    }

    private void handlePayment() {

        switch (checkoutFor) {
            case PACKAGE:
                textViewPay.setText(context.getResources().getString(R.string.please_wait));
                textViewPay.setEnabled(false);
                networkCommunicator.purchasePackageForClass(new PaymentUrlRequest(TempStorage.getUser().getId(),
                        aPackage.getId()), this, false);
                break;
            case CLASS_PURCHASE_WITH_PACKAGE:
                textViewPay.setText(context.getResources().getString(R.string.please_wait));
                textViewPay.setEnabled(false);
                PaymentUrlRequest paymentUrlRequest = new PaymentUrlRequest(TempStorage.getUser().getId(),
                        aPackage.getId(), classModel.getClassSessionId(),
                        classModel.getGymBranchDetail().getGymId());

                if (promoCode != null) {
                    paymentUrlRequest.setPromoId(promoCode.getId());
                }

                networkCommunicator.purchasePackageForClass(paymentUrlRequest, this, false);
                break;
            case SPECIAL_CLASS:
                textViewPay.setText(context.getResources().getString(R.string.please_wait));
                textViewPay.setEnabled(false);
                networkCommunicator.purchasePackageForClass(new PaymentUrlRequest(TempStorage.getUser().getId(),
                        classModel.getClassSessionId(), classModel.getGymBranchDetail().getGymId()), this, false);
                break;
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.PROMO_CODE:
                try {
                    final TextView textViewOk = (TextView) materialDialog.findViewById(R.id.textViewOk);
                    textViewOk.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.exception(e);
                }

                promoCode = ((ResponseModel<PromoCode>) response).data;
                setPrice();

                break;
            case NetworkCommunicator.RequestCode.BUY_PACKAGE:
                setPrice();
                textViewPay.setEnabled(true);
                PaymentWebViewActivity.open(activity, ((ResponseModel<PaymentUrl>) response).data);
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.PROMO_CODE:
                try {
                    final TextView textViewOk = (TextView) materialDialog.findViewById(R.id.textViewOk);
                    textViewOk.setVisibility(View.VISIBLE);
                    final TextView textViewError = (TextView) materialDialog.findViewById(R.id.textViewError);
                    textViewError.setText(errorMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.exception(e);
                }
                break;
            case NetworkCommunicator.RequestCode.BUY_PACKAGE:
                setPrice();
                textViewPay.setEnabled(true);
                ToastUtils.showLong(context, errorMessage);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.ResultCode.PAYMENT_SUCCESS && resultCode == RESULT_OK) {

            switch (checkoutFor) {
                case PACKAGE:
                    EventBroadcastHelper.sendPackagePurchased();
                    finish();
                    break;
                case CLASS_PURCHASE_WITH_PACKAGE:
                    classModel.setUserJoinStatus(true);
                    EventBroadcastHelper.sendPackagePurchasedForClass(classModel);
                    HomeActivity.show(context, AppConstants.FragmentPosition.TAB_SCHEDULE);
                    break;
                case SPECIAL_CLASS:
                    classModel.setUserJoinStatus(true);
                    EventBroadcastHelper.sendClassPurchased(classModel);
                    HomeActivity.show(context, AppConstants.FragmentPosition.TAB_SCHEDULE);
                    break;
            }
        }
    }
}

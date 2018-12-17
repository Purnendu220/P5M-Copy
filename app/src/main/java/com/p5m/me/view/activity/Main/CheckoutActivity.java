package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.PromoCode;
import com.p5m.me.data.ValidityPackageList;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.PaymentUrl;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.data.request.PaymentUrlRequest;
import com.p5m.me.data.request.PromoCodeRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.p5m.me.view.activity.Main.CheckoutActivity.CheckoutFor.CLASS_PURCHASE_WITH_PACKAGE;
import static com.p5m.me.view.activity.Main.CheckoutActivity.CheckoutFor.EXTENSION;
import static com.p5m.me.view.activity.Main.CheckoutActivity.CheckoutFor.PACKAGE;
import static com.p5m.me.view.activity.Main.CheckoutActivity.CheckoutFor.SPECIAL_CLASS;

public class CheckoutActivity extends BaseActivity implements View.OnClickListener, NetworkCommunicator.RequestListener {

    private Handler handler;
    private Runnable nextScreenRunnable;

    /*
            if user is purchasing a package
            */
    public static void openActivity(Context context, Package aPackage) {
        CheckoutActivity.aPackage = aPackage;
        CheckoutActivity.classModel = null;
        CheckoutActivity.checkoutFor = PACKAGE;
        CheckoutActivity.mNumberOfPackagesToBuy=1;
        CheckoutActivity.friendsDetail=null;



        openActivity(context);
    }

    /*
    if user is purchasing a class via package
     */
    public static void openActivity(Context context, Package aPackage, ClassModel classModel) {
        CheckoutActivity.aPackage = aPackage;
        CheckoutActivity.classModel = classModel;
        CheckoutActivity.selectedPacakageFromList = null;
        CheckoutActivity.checkoutFor = CLASS_PURCHASE_WITH_PACKAGE;
        CheckoutActivity.mNumberOfPackagesToBuy = 1;
        CheckoutActivity.friendsDetail=null;



        openActivity(context);
    }

    public static void openActivity(Context context, Package aPackage, ClassModel classModel,int mNumberOfPackagesToBuy,BookWithFriendData friendsDetail) {
        CheckoutActivity.aPackage = aPackage;
        CheckoutActivity.classModel = classModel;
        CheckoutActivity.selectedPacakageFromList = null;
        CheckoutActivity.checkoutFor = CLASS_PURCHASE_WITH_PACKAGE;
        CheckoutActivity.mNumberOfPackagesToBuy=mNumberOfPackagesToBuy;
        CheckoutActivity.friendsDetail=friendsDetail;

        openActivity(context);
    }

    /*
    if user is purchasing a special class
     */
    public static void openActivity(Context context, ClassModel specialClassModel,int mNumberOfPackagesToBuy) {
        CheckoutActivity.aPackage = null;
        CheckoutActivity.selectedPacakageFromList = null;
        CheckoutActivity.classModel = specialClassModel;
        CheckoutActivity.checkoutFor = SPECIAL_CLASS;
        CheckoutActivity.mNumberOfPackagesToBuy = mNumberOfPackagesToBuy;
        CheckoutActivity.friendsDetail = null;




        openActivity(context);
    }
    public static void openActivity(Context context, ClassModel specialClassModel,int mNumberOfPackagesToBuy,BookWithFriendData friendsDetail) {
        CheckoutActivity.aPackage = null;
        CheckoutActivity.selectedPacakageFromList = null;
        CheckoutActivity.classModel = specialClassModel;
        CheckoutActivity.checkoutFor = SPECIAL_CLASS;
        CheckoutActivity.mNumberOfPackagesToBuy=mNumberOfPackagesToBuy;
        CheckoutActivity.friendsDetail=friendsDetail;




        openActivity(context);
    }
    public static void openActivity(Context context, ValidityPackageList selectedPacakageFromList, UserPackage userPackage,int navigatedFrom) {
        CheckoutActivity.aPackage = null;
        CheckoutActivity.classModel = null;
        CheckoutActivity.selectedPacakageFromList = selectedPacakageFromList;
        CheckoutActivity.checkoutFor = EXTENSION;
        CheckoutActivity.userPackage = userPackage;
        CheckoutActivity.navigatinFrom = navigatedFrom;
        CheckoutActivity.mNumberOfPackagesToBuy=1;
        CheckoutActivity.friendsDetail=null;



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
        CLASS_PURCHASE_WITH_PACKAGE,
        EXTENSION,
        BOOKWITHFRIEND
    }

    private static Package aPackage;
    private static Package aPackage1;
    private static int mNumberOfPackagesToBuy = 1;
    private static ClassModel classModel;
    private static CheckoutFor checkoutFor;
    private static ValidityPackageList selectedPacakageFromList;
    private static UserPackage userPackage;
    private static int navigatinFrom;
    private static BookWithFriendData friendsDetail;




    @BindView(R.id.textViewPackageName)
    TextView textViewPackageName;
    @BindView(R.id.textViewTopTitle)
    TextView textViewTopTitle;
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

    @BindView(R.id.textViewCancellationPolicyGeneralToggle)
    TextView textViewCancellationPolicyGeneralToggle;
    @BindView(R.id.textViewCancellationPolicyGenral)
    TextView textViewCancellationPolicyGenral;

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
    ViewGroup layoutSpecialClassDetails;
    @BindView(R.id.layoutNormalClassDetails)
    View layoutNormalClassDetails;

    @BindView(R.id.validityUnit)
    TextView validityUnit;

    @BindView(R.id.textViewPackageValidityExtend)
    TextView textViewPackageValidityExtend;

    @BindView(R.id.linearLayoutBookForFriend)
    LinearLayout linearLayoutBookForFriend;

    @BindView(R.id.imageViewPackageImageBWF)
    ImageView imageViewPackageImageBWF;

    @BindView(R.id.textViewPackageNameBWF)
    TextView textViewPackageNameBWF;

    @BindView(R.id.textViewPriceBWF)
    TextView textViewPriceBWF;

    @BindView(R.id.validityUnitBWF)
    TextView validityUnitBWF;

    @BindView(R.id.textViewPackageValidityExtendBWF)
    TextView textViewPackageValidityExtendBWF;

    @BindView(R.id.textViewCancellationPolicyToggleBWF)
    TextView textViewCancellationPolicyToggleBWF;

    @BindView(R.id.textViewCancellationPolicyBWF)
    TextView textViewCancellationPolicyBWF;








    private PromoCode promoCode;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ButterKnife.bind(activity);
        handler = new Handler();

        setData();

        textViewPay.setOnClickListener(this);
        buttonPromoCode.setOnClickListener(this);
        textViewLimit.setOnClickListener(this);
        textViewCancellationPolicyToggle.setOnClickListener(this);
        textViewCancellationPolicyGeneralToggle.setOnClickListener(this);

        MixPanel.trackCheckoutVisit(aPackage == null ? AppConstants.Tracker.SPECIAL : aPackage.getName());
    }

    private void setData() {
        switch (checkoutFor) {
            case PACKAGE:
            case CLASS_PURCHASE_WITH_PACKAGE:
                layoutSpecialClassDetails.setVisibility(View.GONE);
                layoutNormalClassDetails.setVisibility(View.VISIBLE);
                layoutPromoCode.setVisibility(View.GONE);

                if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                    String validityPeriod = aPackage.getValidityPeriod();
                    validityPeriod = Helper.capitalize(validityPeriod);
                    if (validityPeriod.charAt(validityPeriod.length() - 1) == 's') {
                        validityPeriod = validityPeriod.substring(0, validityPeriod.length() - 1);
                    }

                    textViewPackageValidity.setText("Valid for " + aPackage.getDuration() + " " + AppConstants.plural(validityPeriod, aPackage.getDuration()));
                    textViewLimit.setVisibility(View.VISIBLE);
                    textViewPackageClasses.setText(aPackage.getNoOfClass() + " " + AppConstants.pluralES("Class", aPackage.getNoOfClass()));


                } else if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                    textViewPackageValidity.setText("Valid for " + aPackage.getGymName());
                    textViewLimit.setVisibility(View.GONE);
                    textViewCancellationPolicyGeneralToggle.setVisibility(View.VISIBLE);
                    textViewCancellationPolicyGenral.setText(R.string.membership_drop_in_info);
                    textViewPackageClasses.setText(mNumberOfPackagesToBuy+" Class(s)");

                    textViewPackageClasses.setText(mNumberOfPackagesToBuy + " " + AppConstants.pluralES("Class", mNumberOfPackagesToBuy));

                }

                textViewPackageName.setText(Html.fromHtml(mNumberOfPackagesToBuy+"X <b>" + aPackage.getName() + "</b>"));
                textViewPrice.setText(aPackage.getCost() + " " + context.getString(R.string.currency));

                textViewPackageInfo.setVisibility(aPackage.getDescription().isEmpty() ? View.GONE : View.VISIBLE);
                textViewPackageInfo.setText(aPackage.getDescription());

                buttonPromoCode.setVisibility(View.VISIBLE);
                Helper.setPackageImage(imageViewPackageImage, aPackage.getId());

                setPrice();

                break;
            case SPECIAL_CLASS:
                layoutSpecialClassDetails.setVisibility(View.VISIBLE);
                layoutNormalClassDetails.setVisibility(View.GONE);
                layoutPromoCode.setVisibility(View.GONE);
                buttonPromoCode.setVisibility(View.GONE);
                textViewTopTitle.setVisibility(View.GONE);

                textViewPackageName.setText(Html.fromHtml("<b>" + classModel.getTitle() + "</b>"));
                if(mNumberOfPackagesToBuy>1){
                    textViewPrice.setText(mNumberOfPackagesToBuy+"x "+classModel.getPrice() + " " + context.getString(R.string.currency));

                }else{
                    textViewPrice.setText(classModel.getPrice() + " " + context.getString(R.string.currency));

                }

                textViewCancellationPolicy.setText(classModel.getReminder());
                Helper.setPackageImage(imageViewPackageImage, AppConstants.Values.SPECIAL_CLASS_ID);
                validityUnit.setText(mNumberOfPackagesToBuy+" "+AppConstants.pluralES("Class", mNumberOfPackagesToBuy));

                setPrice();

                break;
            case EXTENSION:
                String validUntill=DateUtils.getExtendedExpiryDate(userPackage.getExpiryDate(),selectedPacakageFromList.getDuration());
                String message=String.format(mContext.getString(R.string.valid_intil),validUntill);
                layoutSpecialClassDetails.setVisibility(View.VISIBLE);
                layoutNormalClassDetails.setVisibility(View.GONE);
                textViewPackageValidityExtend.setVisibility(View.VISIBLE);
                layoutPromoCode.setVisibility(View.GONE);
                buttonPromoCode.setVisibility(View.GONE);
                textViewTopTitle.setVisibility(View.GONE);
                textViewPackageName.setText(R.string.add_more_time);
                textViewCancellationPolicyToggle.setText(R.string.extention_policy);
                textViewPrice.setText(selectedPacakageFromList.getCost() + " " + context.getString(R.string.currency));
                int remainigExtension;
                if(userPackage.getTotalRemainingWeeks()!=null){
                    remainigExtension = userPackage.getTotalRemainingWeeks()-selectedPacakageFromList.getDuration();
                }else{
                    remainigExtension = selectedPacakageFromList.getDuration();
                }
                String extentTerm=String.format(mContext.getString(R.string.extend_term),selectedPacakageFromList.getDuration(),remainigExtension);
                textViewCancellationPolicy.setText(extentTerm);
                validityUnit.setText(selectedPacakageFromList.getDuration()+" Week(s) "+message);
                textViewPackageValidityExtend.setText("Valid for " + userPackage.getPackageName()+" Package");

                Helper.setPackageImage(imageViewPackageImage, AppConstants.Values.SPECIAL_CLASS_ID);
               setPrice();

               break;


        }
        if(aPackage!=null && aPackage.getPromoResponseDto()!=null && aPackage.getPromoResponseDto().getDiscountType()!=null){
            applyPromocode(aPackage.getPromoResponseDto());
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

            case R.id.textViewCancellationPolicyGeneralToggle:
                if (textViewCancellationPolicyGenral.getVisibility() == View.VISIBLE) {
                    textViewCancellationPolicyGenral.setVisibility(View.GONE);
                } else {
                    textViewCancellationPolicyGenral.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.textViewLimit:
                try {
                    PackageLimitsActivity.openActivity(context, aPackage.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.exception(e);
                }
                break;
            case R.id.textViewPay:
                handlePayment();
                MixPanel.trackPayButtonClick(aPackage == null ? AppConstants.Tracker.SPECIAL : aPackage.getName());

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
                    DecimalFormat numberFormat = new DecimalFormat("#.00");
                    textViewTotal.setText(promoCode.getPriceAfterDiscount() + " " + context.getString(R.string.currency));
                    textViewPay.setText("Pay " + promoCode.getPriceAfterDiscount() + " " + context.getString(R.string.currency));
                    textViewPromoCodePrice.setText("- " + numberFormat.format((promoCode.getPrice() - promoCode.getPriceAfterDiscount()))  + " " + context.getString(R.string.currency));
                    layoutPromoCode.setVisibility(View.VISIBLE);
                    buttonPromoCode.setText(context.getString(R.string.remove_promo_code));

                    try {
                        if(materialDialog!=null){
                            materialDialog.dismiss();

                        }
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
                textViewTotal.setText(mNumberOfPackagesToBuy*classModel.getPrice() + " " + context.getString(R.string.currency));
                textViewPay.setText("Pay " + mNumberOfPackagesToBuy*classModel.getPrice() + " " + context.getString(R.string.currency));

                break;
            case EXTENSION:
                textViewTotal.setText(selectedPacakageFromList.getCost() + " " + context.getString(R.string.currency));
                textViewPay.setText("Pay " + selectedPacakageFromList.getCost() + " " + context.getString(R.string.currency));

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
                                new PromoCodeRequest(promoCodeText, aPackage.getId(), TempStorage.getUser().getId(),mNumberOfPackagesToBuy),
                                CheckoutActivity.this, false);
                        break;
                    case CLASS_PURCHASE_WITH_PACKAGE:
                        textViewOk.setVisibility(View.INVISIBLE);
                        if(aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)){
                            networkCommunicator.applyPromoCode(
                                    new PromoCodeRequest(classModel.getGymBranchDetail().getGymId(), promoCodeText, aPackage.getId(), TempStorage.getUser().getId(),mNumberOfPackagesToBuy),
                                    CheckoutActivity.this, false);
                        }else{
                            networkCommunicator.applyPromoCode(
                                    new PromoCodeRequest(classModel.getGymBranchDetail().getGymId(), promoCodeText, aPackage.getId(), TempStorage.getUser().getId()),
                                    CheckoutActivity.this, false);
                        }

                        break;
                }
            }
        });
    }

    private void handlePayment() {

        switch (checkoutFor) {
            case PACKAGE: {
                textViewPay.setText(context.getResources().getString(R.string.please_wait));
                textViewPay.setEnabled(false);
                PaymentUrlRequest paymentUrlRequest = new PaymentUrlRequest(TempStorage.getUser().getId(),
                        aPackage.getId());

                if (promoCode != null) {
                    paymentUrlRequest.setPromoId(promoCode.getId());
                }

                networkCommunicator.purchasePackageForClass(paymentUrlRequest, this, false);
            }
            break;
            case CLASS_PURCHASE_WITH_PACKAGE: {
                textViewPay.setText(context.getResources().getString(R.string.please_wait));
                textViewPay.setEnabled(false);
                PaymentUrlRequest paymentUrlRequest;
                if(friendsDetail!=null){
                    List<BookWithFriendData> data = new ArrayList<>();
                    data.add(friendsDetail);
                    if(aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)){
                        paymentUrlRequest = new PaymentUrlRequest(TempStorage.getUser().getId(),
                                aPackage.getId(), classModel.getClassSessionId(),
                                classModel.getGymBranchDetail().getGymId(),data,aPackage.getNoOfClass());
                    }else{
                        paymentUrlRequest = new PaymentUrlRequest(TempStorage.getUser().getId(),
                                aPackage.getId(), classModel.getClassSessionId(),
                                classModel.getGymBranchDetail().getGymId(),data);
                    }


                }else{
                     paymentUrlRequest = new PaymentUrlRequest(TempStorage.getUser().getId(),
                            aPackage.getId(), classModel.getClassSessionId(),
                            classModel.getGymBranchDetail().getGymId());
                }


                if (promoCode != null) {
                    paymentUrlRequest.setPromoId(promoCode.getId());
                }

                networkCommunicator.purchasePackageForClass(paymentUrlRequest, this, false);
            }
            break;
            case SPECIAL_CLASS: {
                textViewPay.setText(context.getResources().getString(R.string.please_wait));
                textViewPay.setEnabled(false);
                if(friendsDetail!=null){
                    List<BookWithFriendData> data = new ArrayList<>();
                    data.add(friendsDetail);
                    networkCommunicator.purchasePackageForClass(new PaymentUrlRequest(TempStorage.getUser().getId(),
                            classModel.getClassSessionId(), classModel.getGymBranchDetail().getGymId(),data,mNumberOfPackagesToBuy), this, false);

                }else{
                    networkCommunicator.purchasePackageForClass(new PaymentUrlRequest(TempStorage.getUser().getId(),
                            classModel.getClassSessionId(), classModel.getGymBranchDetail().getGymId()), this, false);

                }
                           }
            break;
            case EXTENSION: {
                textViewPay.setText(context.getResources().getString(R.string.please_wait));
                textViewPay.setEnabled(false);
                PaymentUrlRequest paymentUrlRequest = new PaymentUrlRequest(TempStorage.getUser().getId(),selectedPacakageFromList.getId(),
                        userPackage.getId(),"Extension");

                networkCommunicator.purchasePackageForClass(paymentUrlRequest, this, false);
            }
            break;
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.PROMO_CODE:
                applyPromocode(((ResponseModel<PromoCode>) response).data);
                break;
            case NetworkCommunicator.RequestCode.BUY_PACKAGE:
                PaymentUrl paymentUrlResponse=((ResponseModel<PaymentUrl>) response).data;
                if(paymentUrlResponse.getCompleted()){
                    redirectOnResult();
                    }
                    else{
                    setPrice();
                    textViewPay.setEnabled(true);
                    String packageName = aPackage == null ? AppConstants.Tracker.SPECIAL : aPackage.getName();
                    String couponCode = promoCode == null ? AppConstants.Tracker.NO_COUPON : promoCode.code;
                    PaymentWebViewActivity.open(activity, couponCode, packageName, classModel, ((ResponseModel<PaymentUrl>) response).data);

                }
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
            redirectOnResult();

        }
    }

    private void redirectOnResult(){
        switch (checkoutFor) {
            case PACKAGE:
                EventBroadcastHelper.sendPackagePurchased();
                finish();
                break;
            case CLASS_PURCHASE_WITH_PACKAGE:
                classModel.setUserJoinStatus(true);
                EventBroadcastHelper.sendPackagePurchasedForClass(classModel);
                HomeActivity.show(context, AppConstants.Tab.TAB_SCHEDULE);
                sendAutoJoinEvent();
                break;
            case SPECIAL_CLASS:
                classModel.setUserJoinStatus(true);
                EventBroadcastHelper.sendClassPurchased(classModel);
                HomeActivity.show(context, AppConstants.Tab.TAB_SCHEDULE);
                sendAutoJoinEvent();
                break;
            case EXTENSION:
                if(navigatinFrom==AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE){
                    HomeActivity.show(context, AppConstants.Tab.TAB_MY_PROFILE);

                }
                else if(navigatinFrom==AppConstants.AppNavigation.NAVIGATION_FROM_MEMBERSHIP){
                    EventBroadcastHelper.sendPackagePurchased();

                }
                else{
                    HomeActivity.show(context, AppConstants.Tab.TAB_MY_PROFILE);

                }
                finish();
                break;
        }
    }
    private void sendAutoJoinEvent() {
        nextScreenRunnable = new Runnable() {
            @Override
            public void run() {
                EventBroadcastHelper.classAutoJoin(context,classModel);

            }
        };

        handler.postDelayed(nextScreenRunnable, 1000);
    }
    private void applyPromocode(PromoCode promo){
        try {
            if(materialDialog!=null){
                final TextView textViewOk = (TextView) materialDialog.findViewById(R.id.textViewOk);
                textViewOk.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        promoCode = promo;
        setPrice();
    }
}

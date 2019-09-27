package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.TestimonialsAdapter;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.Join5MinModel;
import com.p5m.me.data.PromoCode;
import com.p5m.me.data.Testimonials;
import com.p5m.me.data.ValidityPackageList;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.PaymentUrl;
import com.p5m.me.data.main.User;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.data.request.PaymentUrlRequest;
import com.p5m.me.data.request.PromoCodeRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.fxn.utility.Constants;
import com.p5m.me.helper.Helper;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.CustomAlertDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.p5m.me.fxn.utility.Constants.CheckoutFor.CLASS_PURCHASE_WITH_PACKAGE;
import static com.p5m.me.fxn.utility.Constants.CheckoutFor.EXTENSION;
import static com.p5m.me.fxn.utility.Constants.CheckoutFor.PACKAGE;
import static com.p5m.me.fxn.utility.Constants.CheckoutFor.SPECIAL_CLASS;
import static com.p5m.me.utils.LanguageUtils.numberConverter;


public class CheckoutActivity extends BaseActivity implements View.OnClickListener, NetworkCommunicator.RequestListener, CustomAlertDialog.OnAlertButtonAction, AdapterCallbacks {

    private static int mNumberOfClasses = 1;
    private Handler handler;
    private Runnable nextScreenRunnable;
    private String refId;
    private TestimonialsAdapter testimonialsAdapter;
    private List<Testimonials> default_testimonials;
    private List<Testimonials> testimonials;

    /*
            if user is purchasing a package
            */
    public static void openActivity(Context context, Package aPackage) {
        CheckoutActivity.aPackage = aPackage;
        CheckoutActivity.classModel = null;
        CheckoutActivity.checkoutFor = PACKAGE;
        CheckoutActivity.mNumberOfPackagesToBuy = 1;
        CheckoutActivity.friendsDetail = null;


        openActivity(context);
    }

    /*
    if user is purchasing a class via package
     */
    public static void openActivity(Context context, Package aPackage, ClassModel classModel, int mNumberOfPackagesToBuy, int mNumberOfClasses) {
        CheckoutActivity.aPackage = aPackage;
        CheckoutActivity.classModel = classModel;
        CheckoutActivity.selectedPacakageFromList = null;
        CheckoutActivity.checkoutFor = CLASS_PURCHASE_WITH_PACKAGE;
        CheckoutActivity.mNumberOfPackagesToBuy = mNumberOfPackagesToBuy;
        CheckoutActivity.mNumberOfClasses = mNumberOfClasses;
        CheckoutActivity.friendsDetail = null;


        openActivity(context);
    }


    ///////////////////
    public static void openActivity(Context context, Package aPackage, ClassModel classModel, int mNumberOfPackagesToBuy, BookWithFriendData friendsDetail, int mNumberOfClasses) {
        CheckoutActivity.aPackage = aPackage;
        CheckoutActivity.classModel = classModel;
        CheckoutActivity.selectedPacakageFromList = null;
        CheckoutActivity.checkoutFor = CLASS_PURCHASE_WITH_PACKAGE;
        CheckoutActivity.mNumberOfPackagesToBuy = mNumberOfPackagesToBuy;
        CheckoutActivity.mNumberOfClasses = mNumberOfClasses;
        CheckoutActivity.friendsDetail = friendsDetail;

        openActivity(context);
    }

    /*
    if user is purchasing a special class
     */
    public static void openActivity(Context context, ClassModel specialClassModel, int mNumberOfPackagesToBuy) {
        CheckoutActivity.aPackage = null;
        CheckoutActivity.selectedPacakageFromList = null;
        CheckoutActivity.classModel = specialClassModel;
        CheckoutActivity.checkoutFor = SPECIAL_CLASS;
        CheckoutActivity.mNumberOfPackagesToBuy = mNumberOfPackagesToBuy;
        CheckoutActivity.friendsDetail = null;


        openActivity(context);
    }

    public static void openActivity(Context context, ClassModel specialClassModel, int mNumberOfPackagesToBuy, BookWithFriendData friendsDetail) {
        CheckoutActivity.aPackage = null;
        CheckoutActivity.selectedPacakageFromList = null;
        CheckoutActivity.classModel = specialClassModel;
        CheckoutActivity.checkoutFor = SPECIAL_CLASS;
        CheckoutActivity.mNumberOfPackagesToBuy = mNumberOfPackagesToBuy;
        CheckoutActivity.friendsDetail = friendsDetail;


        openActivity(context);
    }

    public static void openActivity(Context context, ValidityPackageList selectedPacakageFromList, UserPackage userPackage, int navigatedFrom) {
        CheckoutActivity.aPackage = null;
        CheckoutActivity.classModel = null;
        CheckoutActivity.selectedPacakageFromList = selectedPacakageFromList;
        CheckoutActivity.checkoutFor = EXTENSION;
        CheckoutActivity.userPackage = userPackage;
        CheckoutActivity.navigatinFrom = navigatedFrom;
        CheckoutActivity.mNumberOfPackagesToBuy = 1;
        CheckoutActivity.friendsDetail = null;


        openActivity(context);
    }

    private static void openActivity(Context context) {
        Intent intent = new Intent(context, CheckoutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    private static Package aPackage;
    private static Package aPackage1;
    private static int mNumberOfPackagesToBuy = 1;
    private static ClassModel classModel;
    private static Constants.CheckoutFor checkoutFor;
    private static ValidityPackageList selectedPacakageFromList;
    private static UserPackage userPackage;
    private static int navigatinFrom;
    private static BookWithFriendData friendsDetail;
    private static User.WalletDto mWalletCredit;


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

    @BindView(R.id.textViewPackageExtendNoOfDays)
    TextView textViewPackageExtendNoOfDays;
    @BindView(R.id.textViewPlus)
    TextView textViewPlus;

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

    @BindView(R.id.textViewPromoCodeText)
    TextView textViewPromoCodeText;

    @BindView(R.id.textViewCancellationPolicyBWF)
    TextView textViewCancellationPolicyBWF;

    @BindView(R.id.layoutWalletCredit)
    LinearLayout layoutWalletCredit;

    @BindView(R.id.textViewWalletCreditPrice)
    TextView textViewWalletCreditPrice;

    @BindView(R.id.layoutUserWallet)
    LinearLayout mLayoutUserWallet;

    @BindView(R.id.textViewWalletAmount)
    TextView mTextViewWalletAmount;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.scrollview)
    ScrollView scrollView;


    private PromoCode promoCode;
    private MaterialDialog materialDialog;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ButterKnife.bind(activity);
        handler = new Handler();
        checkUserCredits();
        setData();
        scrollView.smoothScrollTo(0,0);
        textViewPay.setOnClickListener(this);
        buttonPromoCode.setOnClickListener(this);
        textViewLimit.setOnClickListener(this);
        textViewCancellationPolicyToggle.setOnClickListener(this);
        textViewCancellationPolicyGeneralToggle.setOnClickListener(this);
        mLayoutUserWallet.setOnClickListener(this);
        MixPanel.trackCheckoutVisit(aPackage == null ? AppConstants.Tracker.SPECIAL : aPackage.getName());
        FirebaseAnalysic.trackCheckoutVisit(aPackage == null ? AppConstants.Tracker.SPECIAL : aPackage.getName());
        IntercomEvents.trackCheckoutVisit(aPackage == null ? AppConstants.Tracker.SPECIAL : aPackage.getName());
        onTrackingNotification();
        setTestimonialAdapter();

    }

    private void checkUserCredits() {
        user = TempStorage.getUser();
        mWalletCredit = user.getWalletDto();
//        if(mWalletCredit!=null&&mWalletCredit.getBalance()>0){
//            mLayoutUserWallet.setVisibility(View.VISIBLE);
//            mTextViewWalletAmount.setText(LanguageUtils.numberConverter(mWalletCredit.getBalance(),2)+" "+context.getResources().getString(R.string.wallet_currency));
//        }else{
//            mLayoutUserWallet.setVisibility(View.GONE);
//
//        }
    }

    private void setData() {
        switch (checkoutFor) {
            case PACKAGE:
            case CLASS_PURCHASE_WITH_PACKAGE:
                layoutSpecialClassDetails.setVisibility(View.GONE);
                layoutNormalClassDetails.setVisibility(View.VISIBLE);
                layoutPromoCode.setVisibility(View.GONE);
                if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                    setTextValidityPeriod(aPackage);
                    textViewLimit.setVisibility(View.GONE);
                    textViewLimit.setText(RemoteConfigConst.GYM_VISIT_LIMIT_VALUE);
                    textViewPackageClasses.setText(numberConverter(aPackage.getNoOfClass()) + " " + AppConstants.pluralES(getString(R.string.one_class), aPackage.getNoOfClass()) + " " + context.getString(R.string.at_any_gym));
                    textViewCancellationPolicyGeneralToggle.setVisibility(View.GONE);
                    textViewPackageName.setText(Html.fromHtml(numberConverter(mNumberOfPackagesToBuy) + "X <b>" + aPackage.getName() + "</b>"));


                } else if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                    textViewPackageValidity.setText(context.getString(R.string.valid_for) + " " + DateUtils.getPackageClassDate(classModel.getClassDate()) + " -" + DateUtils.getClassTime(classModel.getFromTime(), classModel.getToTime()));

                    textViewLimit.setVisibility(View.GONE);
                    textViewCancellationPolicyGeneralToggle.setVisibility(View.VISIBLE);
                    textViewCancellationPolicyGenral.setText(R.string.membership_drop_in_info);
                    textViewPackageName.setText(Html.fromHtml(numberConverter(mNumberOfClasses) + "X <b>" + aPackage.getName() + "</b>"));

                   /* if (mNumberOfPackagesToBuy == 1) {
                        textViewPackageClasses.setText(context.getString(R.string.class_one_at)+" "+ classModel.getGymBranchDetail().getGymName() );
                    } else*/
                    textViewPackageClasses.setText(LanguageUtils.numberConverter(mNumberOfClasses) + " " + AppConstants.pluralES(context.getString(R.string.classs), mNumberOfClasses) + " " + context.getString(R.string.at) + " " + classModel.getGymBranchDetail().getGymName());

                }

//                textViewPackageName.setText(Html.fromHtml(numberConverter(mNumberOfClasses) + "X <b>" + aPackage.getName() + "</b>"));
                textViewPrice.setText(LanguageUtils.numberConverter(aPackage.getCost(), 2) + " " + context.getString(R.string.currency));

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
                if (mNumberOfPackagesToBuy > 1) {
                    textViewPrice.setText(numberConverter(mNumberOfPackagesToBuy) + "x " + LanguageUtils.numberConverter(classModel.getPrice(), 2) + " " + context.getString(R.string.currency));

                } else {
                    textViewPrice.setText(LanguageUtils.numberConverter(classModel.getPrice(), 2) + " " + context.getString(R.string.currency));

                }

                textViewCancellationPolicy.setText(classModel.getReminder());
                Helper.setPackageImage(imageViewPackageImage, AppConstants.Values.SPECIAL_CLASS_ID);
                validityUnit.setText(numberConverter(mNumberOfPackagesToBuy) + " " + AppConstants.pluralES(getString(R.string.one_class), mNumberOfPackagesToBuy) + " " + context.getString(R.string.at) + " " + classModel.getGymBranchDetail().getGymName());
                textViewCancellationPolicyGeneralToggle.setVisibility(View.VISIBLE);

                setPrice();

                break;
            case EXTENSION:
                String validUntill = DateUtils.getExtendedExpiryDate(userPackage.getExpiryDate(), selectedPacakageFromList.getDuration());
                String message = String.format(mContext.getString(R.string.valid_intil), validUntill);
                layoutSpecialClassDetails.setVisibility(View.VISIBLE);
                layoutNormalClassDetails.setVisibility(View.GONE);
                textViewPackageValidityExtend.setVisibility(View.VISIBLE);
                layoutPromoCode.setVisibility(View.GONE);
                buttonPromoCode.setVisibility(View.GONE);
                textViewTopTitle.setVisibility(View.GONE);
                textViewPackageName.setText(R.string.add_more_time);
                textViewCancellationPolicyToggle.setText(R.string.extention_policy);

                textViewPrice.setText(LanguageUtils.numberConverter(selectedPacakageFromList.getCost(), 2) + " " + context.getString(R.string.currency));
                int remainigExtension;
                if (userPackage.getTotalRemainingWeeks() != null) {
                    remainigExtension = userPackage.getTotalRemainingWeeks() - selectedPacakageFromList.getDuration();
                } else {
                    remainigExtension = selectedPacakageFromList.getDuration();
                }
                String extentTerm = String.format(mContext.getString(R.string.extend_term), selectedPacakageFromList.getDuration(), remainigExtension);
                textViewCancellationPolicy.setText(extentTerm);
                if (selectedPacakageFromList.getDuration() == 1)
                    validityUnit.setText(getString(R.string.a_week) + message);
                else
                    validityUnit.setText(String.format(numberConverter(selectedPacakageFromList.getDuration()) + " " + getString(R.string.weeks) + message));
                textViewPackageValidityExtend.setText(getString(R.string.valid_for) + " " + userPackage.getPackageName() + " " + context.getString(R.string.package_name));

                Helper.setPackageImage(imageViewPackageImage, AppConstants.Values.SPECIAL_CLASS_ID);
                setPrice();

                break;


        }
        textViewLimit.setVisibility(View.GONE);
//        textViewCancellationPolicyGeneralToggle.setVisibility(View.GONE);
//        textViewCancellationPolicyToggle.setVisibility(View.GONE);
        if (aPackage != null && aPackage.getPromoResponseDto() != null && aPackage.getPromoResponseDto().getDiscountType() != null) {
            applyPromocode(aPackage.getPromoResponseDto());
        }
    }

    private void setTextValidityPeriod(Package model) {
        if (model.getValidityPeriod().contains("MONTH")) {
            if (model.getDuration() == 1) {
                textViewPackageValidity.setText(context.getResources().getString(R.string.valid_for_a_months));
            } else
                textViewPackageValidity.setText(String.format(context.getResources().getString(R.string.valid_for_months), model.getDuration()));

//         textViewPackageValidity.setText(context.getString(R.string.valid_for) + " " + numberConverter(model.getDuration()) + " " + AppConstants.plural(validityPeriod, model.getDuration()));
//            textViewPackageValidity.setText(String.format(context.getResources().getString(R.string.valid_for_months), model.getDuration()));
        } else if (model.getValidityPeriod().contains("WEEK")) {
            if (model.getDuration() == 1) {
                textViewPackageValidity.setText(context.getResources().getString(R.string.valid_for_a_week));
            } else
                textViewPackageValidity.setText(String.format(context.getResources().getString(R.string.valid_for_weeks), model.getDuration()));

        }
//            textViewPackageValidity.setText(String.format(context.getResources().getString(R.string.valid_for_weeks), model.getDuration()));

    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        dialogBackPress();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewCancellationPolicyToggle:
//                textViewCancellationPolicy.setVisibility(View.GONE);
                if (textViewCancellationPolicy.getVisibility() == View.VISIBLE) {
                    textViewCancellationPolicy.setVisibility(View.GONE);
                } else {
                    textViewCancellationPolicy.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.textViewCancellationPolicyGeneralToggle:
//                textViewCancellationPolicyGenral.setVisibility(View.GONE);
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
            case R.id.layoutUserWallet:
                showWalletAlert();
                break;
        }
    }

    private void setNumberOfDaysPromo() {
        roundFigureOfDays(promoCode.getExtraNumberOfDays());
        textViewTotal.setText(LanguageUtils.numberConverter(aPackage.getCost(), 2) + " " + context.getString(R.string.currency));
        textViewPay.setText(getString(R.string.pay) + " " + LanguageUtils.numberConverter(aPackage.getCost(), 2) + " " + context.getString(R.string.currency));
        if (promoCode.getExtraNumberOfDays() != 0) {
            textViewPackageExtendNoOfDays.setVisibility(View.VISIBLE);
            textViewPlus.setVisibility(View.VISIBLE);
//            textViewPackageExtendNoOfDays.setText(" + " + promoCode.getExtraNumberOfDays() + " days");
            layoutPromoCode.setVisibility(View.VISIBLE);
            textViewPromoCodePrice.setVisibility(View.GONE);
            textViewPromoCodeText.setText(getResources().getString(R.string.time_offer_promo));
            buttonPromoCode.setText(context.getString(R.string.remove_promo_code));
        } else {
            layoutPromoCode.setVisibility(View.GONE);
        }
        try {
            if (materialDialog != null) {
                materialDialog.dismiss();

            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }


    }

    private void roundFigureOfDays(int extraNumberOfDays) {
        if (extraNumberOfDays >= 7 && extraNumberOfDays % 7 == 0) {
            if (extraNumberOfDays / 7 == 1)
                textViewPackageExtendNoOfDays.setText( getString(R.string.a_week));
            else
                textViewPackageExtendNoOfDays.setText(LanguageUtils.numberConverter(promoCode.getExtraNumberOfDays() / 7) + " " + getString(R.string.weeks));
        } else if (extraNumberOfDays >= 30 && extraNumberOfDays % 30 == 0) {
            if(extraNumberOfDays /30 ==1)
                textViewPackageExtendNoOfDays.setText(LanguageUtils.numberConverter(promoCode.getExtraNumberOfDays() / 30) + " " + getString(R.string.a_month));
            else
                textViewPackageExtendNoOfDays.setText(LanguageUtils.numberConverter(promoCode.getExtraNumberOfDays() / 30) + " " + getString(R.string.months));

        } else {
            String days;
            if(promoCode.getExtraNumberOfDays()==1){
                days = LanguageUtils.numberConverter(promoCode.getExtraNumberOfDays()) + " " + getString(R.string.day);

            }
            else
            days = LanguageUtils.numberConverter(promoCode.getExtraNumberOfDays()) + " " + getString(R.string.days);
            textViewPackageExtendNoOfDays.setText(days);

        }
    }


    private void setPrice() {

        switch (checkoutFor) {
            case PACKAGE:
            case CLASS_PURCHASE_WITH_PACKAGE:
                if (promoCode != null) {
                    if (promoCode.getDiscountType().equalsIgnoreCase(AppConstants.ApiParamKey.NUMBEROFDAYS)) {
                        setNumberOfDaysPromo();
                    } else if (promoCode.getDiscountType().equalsIgnoreCase(AppConstants.ApiParamKey.NUMBEROFCLASS)) {
                        setNumberOfClassPromo();
                    } else {
                        textViewPromoCodeText.setText(getResources().getString(R.string.discount_promo_applied));
                        textViewPackageExtendNoOfDays.setVisibility(View.GONE);
                        textViewPlus.setVisibility(View.GONE);
                        textViewTotal.setText(LanguageUtils.numberConverter(promoCode.getPriceAfterDiscount(), 2) + " " + context.getString(R.string.currency));
                        textViewPay.setText(getString(R.string.pay) + " " + LanguageUtils.numberConverter(promoCode.getPriceAfterDiscount(), 2) + " " + context.getString(R.string.currency));
                        if (promoCode.getDiscount() != 0) {
                            layoutPromoCode.setVisibility(View.VISIBLE);
                            double discountedPrice = promoCode.getPrice() - promoCode.getPriceAfterDiscount();
                            textViewPromoCodePrice.setVisibility(View.VISIBLE);
                            textViewPromoCodePrice.setText("- " + (LanguageUtils.numberConverter(discountedPrice, 2)) + " " + context.getString(R.string.currency));
                            buttonPromoCode.setText(context.getString(R.string.remove_promo_code));
                        } else {
                            layoutPromoCode.setVisibility(View.GONE);
                        }
                        try {
                            if (materialDialog != null) {
                                materialDialog.dismiss();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.exception(e);
                        }

                    }
                } else {
                    layoutPromoCode.setVisibility(View.GONE);
                    textViewPackageExtendNoOfDays.setVisibility(View.GONE);
                    textViewPlus.setVisibility(View.GONE);
                    if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL))
                        textViewPackageClasses.setText(numberConverter(aPackage.getNoOfClass()) + " " + AppConstants.pluralES(getString(R.string.one_class), aPackage.getNoOfClass()) + " " + context.getString(R.string.at_any_gym));
                    else if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {

                        textViewPackageClasses.setText(LanguageUtils.numberConverter(mNumberOfClasses) + " " + AppConstants.pluralES(context.getString(R.string.classs), mNumberOfClasses) + " " + context.getString(R.string.at) + " " + classModel.getGymBranchDetail().getGymName());
                    }
                    textViewTotal.setText(LanguageUtils.numberConverter(aPackage.getCost(), 2) + " " + context.getString(R.string.currency));
                    textViewPay.setText(getString(R.string.pay) + " " + LanguageUtils.numberConverter(aPackage.getCost(), 2) + " " + context.getString(R.string.currency));
                    buttonPromoCode.setText(context.getString(R.string.apply_promo_code));
                }

                applyCredit();
                break;

            case SPECIAL_CLASS:
                textViewTotal.setText(LanguageUtils.numberConverter(mNumberOfPackagesToBuy * classModel.getPrice(), 2) + " " + context.getString(R.string.currency));
                textViewPay.setText(getString(R.string.pay) + " " + LanguageUtils.numberConverter(mNumberOfPackagesToBuy * classModel.getPrice(), 2) + " " + context.getString(R.string.currency));
                applyCredit();

                break;
            case EXTENSION:
                textViewTotal.setText(LanguageUtils.numberConverter(selectedPacakageFromList.getCost(), 2) + " " + context.getString(R.string.currency));
                textViewPay.setText(getString(R.string.pay) + " " + LanguageUtils.numberConverter(selectedPacakageFromList.getCost(), 2) + " " + context.getString(R.string.currency));

                applyCredit();

                break;

        }

    }

    private void setNumberOfClassPromo() {
        textViewTotal.setText(LanguageUtils.numberConverter(aPackage.getCost(), 2) + " " + context.getString(R.string.currency));
        textViewPay.setText(getString(R.string.pay) + " " + LanguageUtils.numberConverter(aPackage.getCost(), 2) + " " + context.getString(R.string.currency));
        if (promoCode.getExtraNumberOfClass() != 0) {
            textViewPackageClasses.setVisibility(View.VISIBLE);
            layoutPromoCode.setVisibility(View.VISIBLE);
            int totalClasses = aPackage.getNoOfClass() + promoCode.getExtraNumberOfClass();
//            textViewPackageClasses.setText(numberConverter(totalClasses) + " " + AppConstants.pluralES(getString(R.string.one_class), aPackage.getNoOfClass()) + " " + context.getString(R.string.at_any_gym));
            textViewPackageClasses.setText(Html.fromHtml("<font color='#42A1ED'>" + numberConverter(totalClasses) + " " + AppConstants.pluralES(getString(R.string.one_class), aPackage.getNoOfClass()) + "</font>" + " " + context.getString(R.string.at_any_gym)));
            textViewPromoCodePrice.setVisibility(View.GONE);
            textViewPromoCodeText.setText(getResources().getString(R.string.class_offer_promo));
            buttonPromoCode.setText(context.getString(R.string.remove_promo_code));
        } else {
            layoutPromoCode.setVisibility(View.GONE);
        }
        try {
            if (materialDialog != null) {
                materialDialog.dismiss();

            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }


    }

    private void applyCredit() {
        double costAfterCreditApply = 0;
        double appliedCreditCost = 0;
        if (mWalletCredit != null && mWalletCredit.getBalance() > 0) {
            layoutWalletCredit.setVisibility(View.VISIBLE);
            switch (checkoutFor) {
                case PACKAGE:
                case CLASS_PURCHASE_WITH_PACKAGE:
                    if (promoCode != null && promoCode.getDiscountType().equalsIgnoreCase(AppConstants.ApiParamKey.PERCENTAGE)) {
                        if (mWalletCredit.getBalance() > promoCode.getPriceAfterDiscount()) {
                            costAfterCreditApply = promoCode.getPriceAfterDiscount() - promoCode.getPriceAfterDiscount();
                            appliedCreditCost = promoCode.getPriceAfterDiscount();
                        } else {
                            costAfterCreditApply = promoCode.getPriceAfterDiscount() - mWalletCredit.getBalance();
                            appliedCreditCost = mWalletCredit.getBalance();
                        }
                    } else if (promoCode != null && promoCode.getDiscountType().equalsIgnoreCase("AMOUNT")) {
                        if (mWalletCredit.getBalance() > promoCode.getPriceAfterDiscount()) {
                            costAfterCreditApply = promoCode.getPriceAfterDiscount() - promoCode.getPriceAfterDiscount();
                            appliedCreditCost = promoCode.getPriceAfterDiscount();
                        } else {
                            costAfterCreditApply = promoCode.getPriceAfterDiscount() - mWalletCredit.getBalance();
                            appliedCreditCost = mWalletCredit.getBalance();
                        }
                    } else {
                        if (mWalletCredit.getBalance() > aPackage.getCost()) {
                            costAfterCreditApply = aPackage.getCost() - aPackage.getCost();
                            appliedCreditCost = aPackage.getCost();
                        } else {
                            costAfterCreditApply = aPackage.getCost() - mWalletCredit.getBalance();
                            appliedCreditCost = mWalletCredit.getBalance();
                        }
                    }
                    break;

                case SPECIAL_CLASS:
                    if (mWalletCredit.getBalance() > mNumberOfPackagesToBuy * classModel.getPrice()) {
                        costAfterCreditApply = mNumberOfPackagesToBuy * classModel.getPrice() - mNumberOfPackagesToBuy * classModel.getPrice();
                        appliedCreditCost = mNumberOfPackagesToBuy * classModel.getPrice();
                    } else {
                        costAfterCreditApply = mNumberOfPackagesToBuy * classModel.getPrice() - mWalletCredit.getBalance();
                        appliedCreditCost = mWalletCredit.getBalance();
                    }
                    break;
                case EXTENSION:
                    if (mWalletCredit.getBalance() > selectedPacakageFromList.getCost()) {
                        costAfterCreditApply = selectedPacakageFromList.getCost() - selectedPacakageFromList.getCost();
                        appliedCreditCost = selectedPacakageFromList.getCost();
                    } else {
                        costAfterCreditApply = selectedPacakageFromList.getCost() - mWalletCredit.getBalance();
                        appliedCreditCost = mWalletCredit.getBalance();
                    }
                    break;

            }
            textViewTotal.setText(LanguageUtils.numberConverter(costAfterCreditApply, 2) + " " + context.getString(R.string.currency));
            textViewPay.setText(getString(R.string.pay) + " " + LanguageUtils.numberConverter(costAfterCreditApply, 2) + " " + context.getString(R.string.currency));

            if (appliedCreditCost > 0) {
                textViewWalletCreditPrice.setText("- " + LanguageUtils.numberConverter(appliedCreditCost, 2) + " " + context.getString(R.string.currency));
                //mTextViewWalletAmount.setText((LanguageUtils.numberConverter(((mWalletCredit.getBalance()-appliedCreditCost))))+" "+context.getString(R.string.currency));


            } else {
                layoutWalletCredit.setVisibility(View.GONE);

            }
        } else {
            layoutWalletCredit.setVisibility(View.GONE);

        }
    }

    private void dialogBackPress() {
        DialogUtils.showBasicMessage(context, "", context.getResources().getString(R.string.are_you_sure_leave_page),
                context.getResources().getString(R.string.ok),
                new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        CheckoutActivity.super.onBackPressed();
                    }
                }, context.getString(R.string.cancel), new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
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
                                new PromoCodeRequest(promoCodeText, aPackage.getId(), TempStorage.getUser().getId(), mNumberOfPackagesToBuy),
                                CheckoutActivity.this, false);
                        break;
                    case CLASS_PURCHASE_WITH_PACKAGE:
                        textViewOk.setVisibility(View.INVISIBLE);
                        if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                            networkCommunicator.applyPromoCode(
                                    new PromoCodeRequest(classModel.getGymBranchDetail().getGymId(), promoCodeText, aPackage.getId(), TempStorage.getUser().getId(), mNumberOfPackagesToBuy),
                                    CheckoutActivity.this, false);
                        } else {
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
                if (friendsDetail != null) {
                    List<BookWithFriendData> data = new ArrayList<>();
                    data.add(friendsDetail);
                    if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                        paymentUrlRequest = new PaymentUrlRequest(TempStorage.getUser().getId(),
                                aPackage.getId(), classModel.getClassSessionId(),
                                classModel.getGymBranchDetail().getGymId(), data, aPackage.getNoOfClass());
                    } else {
                        paymentUrlRequest = new PaymentUrlRequest(TempStorage.getUser().getId(),
                                aPackage.getId(), classModel.getClassSessionId(),
                                classModel.getGymBranchDetail().getGymId(), data);
                    }


                } else {
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
                if (friendsDetail != null) {
                    List<BookWithFriendData> data = new ArrayList<>();
                    data.add(friendsDetail);
                    networkCommunicator.purchasePackageForClass(new PaymentUrlRequest(TempStorage.getUser().getId(),
                            classModel.getClassSessionId(), classModel.getGymBranchDetail().getGymId(), data, mNumberOfPackagesToBuy), this, false);

                } else {
                    networkCommunicator.purchasePackageForClass(new PaymentUrlRequest(TempStorage.getUser().getId(),
                            classModel.getClassSessionId(), classModel.getGymBranchDetail().getGymId()), this, false);

                }
            }
            break;
            case EXTENSION: {

                textViewPay.setText(context.getResources().getString(R.string.please_wait));
                textViewPay.setEnabled(false);
                PaymentUrlRequest paymentUrlRequest = new PaymentUrlRequest(TempStorage.getUser().getId(), selectedPacakageFromList.getId(),
                        userPackage.getId(), "Extension");

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

                PaymentUrl paymentUrlResponse = ((ResponseModel<PaymentUrl>) response).data;
                if (paymentUrlResponse.getCompleted()) {
                    redirectOnResult();
                    networkCommunicator.getMyUser(CheckoutActivity.this, false);
                } else {

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
    public void onBackPressed() {
        dialogBackPress();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == AppConstants.ResultCode.PAYMENT_SUCCESS && resultCode == RESULT_OK) {

            refId = data.getStringExtra(AppConstants.DataKey.REFERENCE_ID);
            if (refId != null && refId.length() > 0) {
                redirectOnResult1();

            } else {
                redirectOnResult();

            }
        }
    }

    private void redirectOnResult1() {
        String couponCode = promoCode == null ? AppConstants.Tracker.NO_COUPON : promoCode.code;

//        PaymentConfirmationActivity.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS,refId,aPackage,classModel,checkoutFor,userPackage,selectedPacakageFromList);
        switch (checkoutFor) {
            case PACKAGE:

                PaymentConfirmationActivity.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS,
                        refId, aPackage, null, checkoutFor, null, null, mNumberOfPackagesToBuy,couponCode);

                break;
            case CLASS_PURCHASE_WITH_PACKAGE:
                PaymentConfirmationActivity.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS,
                        refId, aPackage, classModel, checkoutFor, null, null, mNumberOfPackagesToBuy,couponCode);

                break;
            case SPECIAL_CLASS:
                PaymentConfirmationActivity.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS,
                        refId, null, classModel, checkoutFor, null, selectedPacakageFromList, mNumberOfPackagesToBuy,couponCode);

                break;
            case EXTENSION:
                PaymentConfirmationActivity.openActivity(context, navigatinFrom,
                        refId, null, null, checkoutFor, userPackage, selectedPacakageFromList, mNumberOfPackagesToBuy,couponCode);

                break;
        }
        finish();

    }

    private void redirectOnResult() {
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
                if (navigatinFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE) {
                    HomeActivity.show(context, AppConstants.Tab.TAB_MY_PROFILE);

                } else if (navigatinFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MEMBERSHIP) {
                    EventBroadcastHelper.sendPackagePurchased();

                } else {
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
                EventBroadcastHelper.classAutoJoin(context, classModel);
                saved5MinClass(classModel);
            }
        };

        handler.postDelayed(nextScreenRunnable, 1000);
    }

    private void applyPromocode(PromoCode promo) {
        try {
            if (materialDialog != null) {
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

    private void showWalletAlert() {
        CustomAlertDialog mCustomAlertDialog = new CustomAlertDialog(context, context.getString(R.string.wallet_alert_title), context.getString(R.string.wallet_alert), 1, "", context.getResources().getString(R.string.ok), CustomAlertDialog.AlertRequestCodes.ALERT_REQUEST_WALLET_INFO, null, true, this);
        try {
            mCustomAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onOkClick(int requestCode, Object data) {

    }

    @Override
    public void onCancelClick(int requestCode, Object data) {

    }


    private void setTestimonialAdapter() {
        try {
            String testimonial = RemoteConfigConst.TESTIMONIALS_VALUE;
            if (testimonial != null && !testimonial.isEmpty()) {
                Gson g = new Gson();
                List<Testimonials> p = g.fromJson(testimonial, new TypeToken<List<Testimonials>>() {
                }.getType());
                testimonials = p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (testimonials == null) {
            recyclerView.setVisibility(View.GONE);
            return;
        } else {
            if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar") &&
                    !TextUtils.isEmpty(testimonials.get(0).getMessage_ar())) {
                setTestimonialVisible();
            } else if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("en") &&
                    !TextUtils.isEmpty(testimonials.get(0).getMessage_ar())) {
                setTestimonialVisible();
            } else {
                recyclerView.setVisibility(View.GONE);

            }
        }
    }
    private void setTestimonialVisible() {
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false));
        recyclerView.setHasFixedSize(false);

        testimonialsAdapter = new TestimonialsAdapter(context, this);
        recyclerView.setAdapter(testimonialsAdapter);

        testimonialsAdapter.addAll(testimonials);
        testimonialsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }

    private void saved5MinClass(ClassModel classModel) {
        Join5MinModel join5MinModel= new Join5MinModel();
        join5MinModel.setGetClassSessionId(classModel.getClassSessionId());
        join5MinModel.setJoiningTime(Calendar.getInstance().getTime());
        List<Join5MinModel> bookedClassList = MyPreferences.getInstance().getBookingTime();
        if (bookedClassList != null && bookedClassList.size() > 0) {
            bookedClassList.add(join5MinModel);
            MyPreferences.getInstance().saveBookingTime(bookedClassList);
            LogUtils.debug("Class Booked " + classModel.getTitle());
            return;

        } else {
            bookedClassList = new ArrayList<>();
            bookedClassList.add(join5MinModel);
            MyPreferences.getInstance().saveBookingTime(bookedClassList);

        }
    }
}

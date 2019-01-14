package com.p5m.me.view.activity.Main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.data.PaymentConfirmationResponse;
import com.p5m.me.data.ValidityPackageList;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.fxn.utility.Constants;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.fxn.utility.Constants.CheckoutFor.EXTENSION;
import static com.p5m.me.fxn.utility.Constants.CheckoutFor.PENDING_TRANSACTION;

public class PaymentConfirmationActivity extends BaseActivity implements NetworkCommunicator.RequestListener, View.OnClickListener {


    private static ClassModel classModel;
    private static UserPackage userPackage = null;
    private static ValidityPackageList selectedPacakageFromList;
    private static int mNumberOfClassesToBuy = 1;
    private PaymentConfirmationResponse paymentResponse;
    private Runnable nextScreenRunnable;
    private Handler handler;

    public static void openActivity(Context context, int navigationFrom, String refId,
                                    Package aPackage, ClassModel classModel,
                                    Constants.CheckoutFor checkoutFor,
                                    UserPackage userPackage,
                                    ValidityPackageList selectedPacakageFromList, int mNumberOfClassesToBuy) {
        Intent intent = new Intent(context, PaymentConfirmationActivity.class)

                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PaymentConfirmationActivity.userPackage = null;
        PaymentConfirmationActivity.aPackage = null;
        PaymentConfirmationActivity.classModel = null;
        PaymentConfirmationActivity.selectedPacakageFromList = null;
        PaymentConfirmationActivity.referenceId = Long.parseLong(refId);
        PaymentConfirmationActivity.aPackage = aPackage;
        PaymentConfirmationActivity.classModel = classModel;
        PaymentConfirmationActivity.checkoutFor = checkoutFor;
        PaymentConfirmationActivity.userPackage = userPackage;
        PaymentConfirmationActivity.selectedPacakageFromList = selectedPacakageFromList;
        PaymentConfirmationActivity.mNumberOfClassesToBuy = mNumberOfClassesToBuy;
        context.startActivity(intent);
    }


    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.imageViewPaymentStatus)
    public ImageView imageViewPaymentStatus;
    @BindView(R.id.textViewPaymentStatus)
    public TextView textViewPaymentStatus;
    @BindView(R.id.buttonContactUs)
    public Button buttonContactUs;
    @BindView(R.id.layoutPaymentStatus)
    public LinearLayout layoutPaymentStatus;
    @BindView(R.id.layoutConfirmation)
    public LinearLayout layoutConfirmation;
    @BindView(R.id.textViewPaymentReference)
    public TextView textViewPaymentReference;
    @BindView(R.id.textViewBookedDate)
    public TextView textViewBookedDate;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.cardView)
    public CardView cardView;
    @BindView(R.id.textViewPackageName)
    public TextView textViewPackageName;
    @BindView(R.id.textViewClassName)
    public TextView textViewClassName;
    @BindView(R.id.textViewClass)
    public TextView textViewClass;
    @BindView(R.id.textViewValidity)
    public TextView textViewValidity;
    @BindView(R.id.textViewAmount)
    public TextView textViewAmount;
    @BindView(R.id.textViewSubTitle)
    public TextView textViewSubTitle;
    @BindView(R.id.layoutClass)
    public LinearLayout layoutClass;
    @BindView(R.id.layoutNoData)
    public LinearLayout layoutNoData;
    @BindView(R.id.viewClass)
    public View viewClass;
    @BindView(R.id.textViewPaymentDetail)
    public TextView textViewPaymentDetail;
    @BindView(R.id.textViewPackageTitle)
    public TextView textViewPackageTitle;
    @BindView(R.id.buttonViewSchedule)
    public Button buttonViewSchedule;
    @BindView(R.id.buttonTryAgain)
    public Button buttonTryAgain;

    @BindView(R.id.progressBarDone)
    public ProgressBar progressBarDone;
    public static String BOOKED_ON = "";
    public static String PAYMENT_REFERENCE = "";
    public static String CONGRATULATION = "";
    public static String CLASS = "";
    public static String SUCCESSFULLY_BOOKED = "";

    public static long referenceId;
    private int navigatedFrom;
    private static PaymentStatus paymentStatus;
    private static Package aPackage = null;
    private static Constants.CheckoutFor checkoutFor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentData();
        callPaymentDetailApi();
        setContentView(R.layout.activity_payment_confirmation);
        ButterKnife.bind(activity);
        handler = new Handler();
        progressBarDone.setVisibility(View.VISIBLE);

        layoutConfirmation.setVisibility(View.GONE);
        setToolBar();
        handleClickEvent();

    }

    /* Set Toolbar */
    private void setToolBar() {

        BaseActivity activity = (BaseActivity) this.activity;
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_normal, null);

        v.findViewById(R.id.imageViewBack).setVisibility(View.GONE);

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.payment_confirmation));

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    /*
     * Get Intent Data
     * */
    private void getIntentData() {
        navigatedFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);
    }

    /* Initialize the Variables*/
    private void initializeStringVariables(String startTitle, String endTitle) {
        BOOKED_ON = getString(R.string.booked_on) + " ";
        PAYMENT_REFERENCE = getString(R.string.payment_reference);
        if (paymentResponse.getClassDetailDto() != null) {
            CONGRATULATION = startTitle + " ";
            CLASS = getString(R.string.classs);
        } else {
            CONGRATULATION = getString(R.string.congratulation_package) + " ";
            CLASS = getString(R.string.packages);


        }
        SUCCESSFULLY_BOOKED = " " + endTitle;
        if (checkoutFor.equals(EXTENSION)) {
            BOOKED_ON = getString(R.string.extend_on) + " ";
            SUCCESSFULLY_BOOKED = " has been extended for " + selectedPacakageFromList.getDuration() + " " + getString(R.string.weeks) + ".";
        }

    }


    /* Set Confirmation Booking Layout*/
    private void setConfirmBookingStyle() {
        layoutPaymentStatus.setBackgroundColor(getResources().getColor(R.color.light_green));
        if (paymentResponse.getClassDetailDto() != null)
            textViewPaymentStatus.setText(R.string.class_booked_successfully);
        else
            textViewPaymentStatus.setText(R.string.package_booked_successfully);

        textViewPaymentStatus.setTextColor(getResources().getColor(R.color.green));
        buttonContactUs.setVisibility(View.GONE);
        textViewPaymentReference.setVisibility(View.VISIBLE);
        textViewBookedDate.setVisibility(View.VISIBLE);
        imageViewPaymentStatus.setImageDrawable(getResources().getDrawable(R.drawable.success));
        setConfirmBookingTitle();

    }

    /*
     *  Set Congratulation Title
     * */
    private void setConfirmBookingTitle() {
        if (paymentResponse.getClassDetailDto() != null) {
            if (!TextUtils.isEmpty(paymentResponse.getClassDetailDto().getTitle())) {
                setTitleText(paymentResponse.getClassDetailDto().getTitle());
            }
        } else {

            if (userPackage != null) {
                setTitleText(userPackage.getPackageName());
            } else if (!TextUtils.isEmpty(paymentResponse.getPackageName()))
                setTitleText(paymentResponse.getPackageName());

        }
    }

    private void setTitleText(String title) {
        String paymentDetail = CONGRATULATION
                + title + SUCCESSFULLY_BOOKED;
        SpannableString spanBoldPayDetail = boldString(paymentDetail, CONGRATULATION.length(), CONGRATULATION.length() + title.length());
        textViewPaymentDetail.setText(spanBoldPayDetail);
    }


    private void setPendingBookingTitle() {
        if (paymentResponse.getClassDetailDto() != null) {
            SUCCESSFULLY_BOOKED = " is pending. You will receive a notice about booking during 2 days.";
            if (!TextUtils.isEmpty(paymentResponse.getClassDetailDto().getTitle())) {
                CONGRATULATION = "Class ";
                String paymentDetail = CONGRATULATION
                        + paymentResponse.getClassDetailDto().getTitle() + SUCCESSFULLY_BOOKED;
                SpannableString spanBoldPayDetail = boldString(paymentDetail, CONGRATULATION.length(), CONGRATULATION.length() + paymentResponse.getClassDetailDto().getTitle().length());
                textViewPaymentDetail.setText(spanBoldPayDetail);
            }
        } else {
            CONGRATULATION = "Package ";
            String paymentDetail = CONGRATULATION
                    + paymentResponse.getPackageName() + SUCCESSFULLY_BOOKED;
            SpannableString spanBoldPayDetail = boldString(paymentDetail, CONGRATULATION.length(), CONGRATULATION.length() + paymentResponse.getPackageName().length());
            textViewPaymentDetail.setText(spanBoldPayDetail);
        }
    }

    private void callPaymentDetailApi() {
        networkCommunicator.getPaymentDetail(referenceId, this, false);
    }

    private void handleClickEvent() {
        buttonViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectOnResult();
            }
        });
        buttonContactUs.setOnClickListener(this);
        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPaymentDetailApi();
            }
        });
    }

    private void redirectOnResult() {
        switch (checkoutFor) {
            case PACKAGE:

//                EventBroadcastHelper.sendPackagePurchased();
                HomeActivity.show(context, AppConstants.Tab.TAB_FIND_CLASS);
                finish();
                break;
            case CLASS_PURCHASE_WITH_PACKAGE:
                classModel.setUserJoinStatus(true);

                EventBroadcastHelper.sendPackagePurchasedForClass(classModel);
                HomeActivity.show(context, AppConstants.Tab.TAB_SCHEDULE);
//                sendAutoJoinEvent();
                break;
            case SPECIAL_CLASS:
                classModel.setUserJoinStatus(true);
                EventBroadcastHelper.sendClassPurchased(classModel);
                HomeActivity.show(context, AppConstants.Tab.TAB_SCHEDULE);
//                sendAutoJoinEvent();
                break;
            case EXTENSION:
//                if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE) {
                HomeActivity.show(context, AppConstants.Tab.TAB_FIND_CLASS);
//
//                } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MEMBERSHIP) {
//                EventBroadcastHelper.sendPackagePurchased();

//                } else {
//                    HomeActivity.show(context, AppConstants.Tab.TAB_MY_PROFILE);
//
//                }
                finish();
                break;
            case PENDING_TRANSACTION:
                TransactionHistoryActivity.openActivity(context);
                finish();
                break;
        }
    }


    @Override
    public void onClick(View view) {

    }

    public enum PaymentStatus {
        SUCCESS,
        FAILURE,
        PENDING
    }

    private void setAnimation() {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(550);
        cardView.startAnimation(anim);
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.PAYMENT_CONFIRMATION_DETAIL:
                progressBarDone.setVisibility(View.GONE);

                layoutConfirmation.setVisibility(View.VISIBLE);
                paymentResponse = ((ResponseModel<PaymentConfirmationResponse>) response).data;
                setAnimation();
                setData(PaymentStatus.valueOf(paymentResponse.getStatus()));

//                layoutNoData.setVisibility(View.VISIBLE);
        }
    }

    private void setData(PaymentStatus paymentStatus) {
        switch (paymentStatus) {
            case SUCCESS:
                initializeStringVariables(getString(R.string.congratulation_class), getString(R.string.is_successfully_booked));
                setConfirmBookingStyle();
                setStyle();
                break;
            case FAILURE:
                initializeStringVariables(getString(R.string.unfortunately_class), getString(R.string.is_failed));
                setFailBookingStyle();
                setStyle();
                break;
            case PENDING:
                initializeStringVariables(getString(R.string.classs), getString(R.string.is_pending));
                setPendingBookingStyle();
                setStyle();
                break;

        }
    }

    private void sendAutoJoinEvent() {
        nextScreenRunnable = new Runnable() {
            @Override
            public void run() {
                EventBroadcastHelper.classAutoJoin(context, classModel);
            }
        };

        handler.postDelayed(nextScreenRunnable, 1000);
    }


    /*
     * Handle Package Detail Data
     * */
    private void handlePackageDetails() {
        if (paymentResponse.getDate() != 0) {
            String bookedString = BOOKED_ON + DateUtils.getTransactionDate(paymentResponse.getDate());
            SpannableString spanBoldBooked = boldString(bookedString, BOOKED_ON.length(), bookedString.length());
            textViewBookedDate.setText(spanBoldBooked);
        }
        if (!TextUtils.isEmpty(paymentResponse.getReferenceId())) {
            String paymentReference = PAYMENT_REFERENCE + " " + '#' + paymentResponse.getReferenceId();
            SpannableString spanBoldReferenceID = boldString(paymentReference, PAYMENT_REFERENCE.length(), paymentReference.length());
            textViewPaymentReference.setText(spanBoldReferenceID);
        }
        if (!TextUtils.isEmpty(paymentResponse.getPackageName()))
            textViewPackageName.setText(paymentResponse.getPackageName());
        /*if (paymentResponse.getClassDetailDto() != null) {
            if (!TextUtils.isEmpty(paymentResponse.getClassDetailDto().getTitle()))
                textViewClassName.setText(paymentResponse.getClassDetailDto().getTitle());
        } else {*/
        if (userPackage != null) {
            textViewClassName.setText(String.valueOf(userPackage.getBalanceClass()) + " " + AppConstants.pluralES(context.getString(R.string.classs), userPackage.getBalanceClass()));
            textViewPackageName.setText(String.valueOf(userPackage.getPackageName()) + " " + AppConstants.pluralES(context.getString(R.string.classs), userPackage.getBalanceClass()));
        } else if (aPackage != null) {
            if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                textViewClassName.setText("1 Class");

            } else
                textViewClassName.setText(String.valueOf(aPackage.getNoOfClass()) + " " + AppConstants.pluralES(context.getString(R.string.classs), aPackage.getNoOfClass()));
        } else if (selectedPacakageFromList != null)
            textViewClassName.setText(String.valueOf(selectedPacakageFromList.getNoOfClass()) + " " + AppConstants.pluralES(context.getString(R.string.classs), selectedPacakageFromList.getNoOfClass()));
        else if (classModel != null) {
            textViewClassName.setText("1 Class");
            textViewValidity.setText(DateUtils.getPackageClassDate(classModel.getClassDate()) + " -" + DateUtils.getClassTime(classModel.getFromTime(), classModel.getToTime()));
        }

        if (!TextUtils.isEmpty(paymentResponse.getExpiryDate())) {
            textViewValidity.setText(DateUtils.getClassDate(paymentResponse.getExpiryDate()));

        } else if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
            if (paymentResponse.getClassDetailDto() != null)
                textViewValidity.setText(DateUtils.getPackageClassDate(paymentResponse.getClassDetailDto().getClassDate()) + " -" + DateUtils.getClassTime(paymentResponse.getClassDetailDto().getFromTime(), paymentResponse.getClassDetailDto().getToTime()));

        } else
            textViewValidity.setText(R.string.n_a);
        textViewAmount.setText(LanguageUtils.numberConverter(paymentResponse.getAmount()) + " " + context.getString(R.string.currency));
        buttonHandler();
        setStyle();
    }

    private void buttonHandler() {
        if (PaymentStatus.valueOf(paymentResponse.getStatus()) == PaymentStatus.SUCCESS) {
            if (paymentResponse.getClassDetailDto() != null) {
                buttonViewSchedule.setText(getString(R.string.view_schedule));
            } else {
                buttonViewSchedule.setText(getString(R.string.book_classes));
            }
        }
        if (PaymentStatus.valueOf(paymentResponse.getStatus()) == PaymentStatus.PENDING) {
            buttonViewSchedule.setText(getString(R.string.payment_history));
            checkoutFor = PENDING_TRANSACTION;
        }


    }

    @Override
    public void onBackPressed() {
        onRedirectBack();
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.PAYMENT_CONFIRMATION_DETAIL:
                progressBarDone.setVisibility(View.GONE);
                layoutNoData.setVisibility(View.VISIBLE);
//                layoutConfirmation.setVisibility(View.VISIBLE);
//                setData(PaymentStatus.valueOf("FAILURE"));

                ToastUtils.show(this, errorMessage);
        }
    }


    private void setPendingBookingStyle() {
        layoutPaymentStatus.setBackgroundColor(getResources().getColor(R.color.blue));
        if (paymentResponse.getClassDetailDto() != null)
            textViewPaymentStatus.setText(R.string.class_booking_is_pending);
        else
            textViewPaymentStatus.setText(R.string.package_booking_is_pending);
        textViewPaymentStatus.setTextColor(getResources().getColor(R.color.theme_book));
        buttonContactUs.setVisibility(View.GONE);
        textViewPaymentReference.setVisibility(View.VISIBLE);

        textViewBookedDate.setVisibility(View.VISIBLE);

        if (paymentResponse.getClassDetailDto() != null) {
            String paymentDetail = CLASS
                    + paymentResponse.getClassDetailDto().getTitle() + getString(R.string.pending_detail);
            SpannableString spanBoldPayDetail = boldString(paymentDetail, CONGRATULATION.length(), CONGRATULATION.length() + paymentResponse.getClassDetailDto().getTitle().length());
            textViewPaymentDetail.setText(spanBoldPayDetail);
        }
        imageViewPaymentStatus.setImageDrawable(getResources().getDrawable(R.drawable.pending));

        setPendingBookingTitle();

    }

    private void setFailBookingStyle() {
        imageViewPaymentStatus.setImageDrawable(getResources().getDrawable(R.drawable.failed));

        layoutPaymentStatus.setBackgroundColor(getResources().getColor(R.color.wewak));
        if (paymentResponse.getClassDetailDto() != null)
            textViewPaymentStatus.setText(R.string.class_booking_is_failed);
        else
            textViewPaymentStatus.setText(R.string.package_booking_is_failed);
        textViewPaymentStatus.setTextColor(getResources().getColor(R.color.red));
        buttonContactUs.setVisibility(View.VISIBLE);
        textViewPaymentReference.setVisibility(View.GONE);
        textViewBookedDate.setVisibility(View.GONE);
//        setConfirmBookingTitle();


    }

    private SpannableString boldString(String bookedDate, int start, int end) {
        SpannableString spannableStr = new SpannableString(bookedDate);
        StyleSpan styleSpanBold = new StyleSpan(Typeface.BOLD);
        spannableStr.setSpan(styleSpanBold, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableStr;
    }

    @SuppressLint("SetTextI18n")
    private void setStyle() {

        if (paymentResponse.getDate() != 0) {
            String bookedString = BOOKED_ON + DateUtils.getTransactionDate(paymentResponse.getDate());
            SpannableString spanBoldBooked = boldString(bookedString, BOOKED_ON.length(), bookedString.length());
            textViewBookedDate.setText(spanBoldBooked);
        }
        if (!TextUtils.isEmpty(paymentResponse.getReferenceId())) {
            String paymentReference = PAYMENT_REFERENCE + " " + '#' + paymentResponse.getReferenceId();
            SpannableString spanBoldReferenceID = boldString(paymentReference, PAYMENT_REFERENCE.length(), paymentReference.length());
            textViewPaymentReference.setText(spanBoldReferenceID);
        }
        if (!TextUtils.isEmpty(paymentResponse.getPackageName()))
            textViewPackageName.setText(paymentResponse.getPackageName());
        if (!TextUtils.isEmpty(paymentResponse.getExpiryDate())) {
            textViewValidity.setText(DateUtils.getClassDate(paymentResponse.getExpiryDate()));
        }
        textViewAmount.setText(LanguageUtils.numberConverter(paymentResponse.getAmount()) + " " + context.getString(R.string.currency));
        buttonHandler();

        switch (checkoutFor) {
            case PACKAGE:

            case CLASS_PURCHASE_WITH_PACKAGE:
                if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                    if (paymentResponse.getClassDetailDto() != null)
                        textViewValidity.setText(DateUtils.getPackageClassDate(paymentResponse.getClassDetailDto().getClassDate()) + "\n" + DateUtils.getClassTime(paymentResponse.getClassDetailDto().getFromTime(), paymentResponse.getClassDetailDto().getToTime()));
                } else {
                    if (!TextUtils.isEmpty(paymentResponse.getExpiryDate())) {
                        textViewValidity.setText(DateUtils.getClassDate(paymentResponse.getExpiryDate()));
                    } else
                        textViewValidity.setText(R.string.n_a);

                }
                break;
            case SPECIAL_CLASS:
                textViewSubTitle.setText(context.getString(R.string.booking_details));
                textViewPackageTitle.setText(R.string.class_name);
                textViewValidity.setText(DateUtils.getPackageClassDate(classModel.getClassDate()) + "\n" + DateUtils.getClassTime(classModel.getFromTime(), classModel.getToTime()));

                if (!TextUtils.isEmpty(paymentResponse.getClassDetailDto().getTitle()))
                    textViewPackageName.setText(paymentResponse.getClassDetailDto().getTitle());
                break;
            case EXTENSION:
                if (userPackage != null) {
                    textViewPackageName.setText(userPackage.getPackageName());
                    textViewClassName.setText(userPackage.getBalanceClass() + " " + AppConstants.pluralES(context.getString(R.string.classs), userPackage.getBalanceClass()));
                }
                textViewPaymentStatus.setText(context.getString(R.string.package_extended_successfully));

                break;

        }
        if (paymentResponse.getNumberOfClasses() != 0)
            textViewClassName.setText(paymentResponse.getNumberOfClasses() + " " + AppConstants.pluralES(context.getString(R.string.classs), paymentResponse.getNumberOfClasses()));

    }

    private void onRedirectBack() {
        switch (checkoutFor) {
            case PACKAGE:
                classModel.setUserJoinStatus(true);
                EventBroadcastHelper.sendPackagePurchased();
                finish();
                break;

            case CLASS_PURCHASE_WITH_PACKAGE:
                classModel.setUserJoinStatus(true);
                EventBroadcastHelper.sendPackagePurchasedForClass(classModel);
                finish();
                break;

            case SPECIAL_CLASS:
                classModel.setUserJoinStatus(true);
                EventBroadcastHelper.sendClassPurchased(classModel);
                finish();
                super.onBackPressed();
                break;

            case EXTENSION:
                if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE) {
                    HomeActivity.show(context, AppConstants.Tab.TAB_MY_PROFILE);

                } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MEMBERSHIP) {
                    EventBroadcastHelper.sendPackagePurchased();

                } else {
                    HomeActivity.show(context, AppConstants.Tab.TAB_MY_PROFILE);
                }
                finish();
                break;

        }
//        super.onBackPressed();
    }


}

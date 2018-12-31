package com.p5m.me.view.activity.Main;

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
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.p5m.me.R;
import com.p5m.me.data.PackageLimitModel;
import com.p5m.me.data.PaymentConfirmationResponse;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.fxn.utility.Constants;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.fxn.utility.Constants.CheckoutFor.CLASS_PURCHASE_WITH_PACKAGE;
import static com.p5m.me.fxn.utility.Constants.CheckoutFor.EXTENSION;
import static com.p5m.me.fxn.utility.Constants.CheckoutFor.PACKAGE;
import static com.p5m.me.fxn.utility.Constants.CheckoutFor.SPECIAL_CLASS;
import static com.p5m.me.utils.AppConstants.ApiParamValue.SUCCESS_RESPONSE_CODE;
import static com.p5m.me.view.activity.Main.PaymentConfirmationActivity.PaymentStatus.FAILURE;
import static com.p5m.me.view.activity.Main.PaymentConfirmationActivity.PaymentStatus.PENDING;
import static com.p5m.me.view.activity.Main.PaymentConfirmationActivity.PaymentStatus.SUCCESS;

public class PaymentConfirmationActivity extends BaseActivity implements NetworkCommunicator.RequestListener {


    private static ClassModel classModel;
    private PaymentConfirmationResponse paymentResponse;
    private Runnable nextScreenRunnable;
    private Handler handler;

    public static void openActivity(Context context, int navigationFrom, String refId, Package aPackage, ClassModel classModel, Constants.CheckoutFor checkoutFor) {
        Intent intent = new Intent(context, PaymentConfirmationActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);

        PaymentConfirmationActivity.referenceId = Long.parseLong(refId);
        PaymentConfirmationActivity.aPackage = aPackage;
        PaymentConfirmationActivity.classModel = classModel;
        PaymentConfirmationActivity.checkoutFor = checkoutFor;
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
    @BindView(R.id.textViewValidity)
    public TextView textViewValidity;
    @BindView(R.id.textViewAmount)
    public TextView textViewAmount;
    @BindView(R.id.textViewPaymentDetail)
    public TextView textViewPaymentDetail;
    @BindView(R.id.buttonViewSchedule)
    public Button buttonViewSchedule;

    public static long referenceId;
    private int navigatedFrom;
    private static PaymentStatus paymentStatus;
    private static Package aPackage;
    private static Constants.CheckoutFor checkoutFor;
    public static final String BOOKED_ON = "Booked On: ";
    public static final String PAYMENT_REFERENCE = "Payment Reference: ";
    public static final String CONGRATULATION = "Congratulations! Class  ";
    public static final String CLASS = "Class  ";
    public static final String SUCCESSFULLY_BOOKED = " is successfully booked.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);
        ButterKnife.bind(activity);
        handler = new Handler();
        setToolBar();
        setAnimation();
        getIntentData();
        handleClickEvent();
    }

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

        v.findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.payment_confirmation));

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    private void setFailBookingStyle() {
        layoutPaymentStatus.setBackgroundColor(getResources().getColor(R.color.wewak));
        textViewPaymentStatus.setText("Class booking is failed");
        textViewPaymentStatus.setTextColor(getResources().getColor(R.color.red));
        buttonContactUs.setVisibility(View.VISIBLE);
        textViewPaymentReference.setVisibility(View.GONE);
        textViewBookedDate.setVisibility(View.GONE);

    }

    private void setConfirmBookingStyle() {
        layoutPaymentStatus.setBackgroundColor(getResources().getColor(R.color.blue));
        textViewPaymentStatus.setText("Class booked successfully");
        textViewPaymentStatus.setTextColor(getResources().getColor(R.color.theme_book));
        buttonContactUs.setVisibility(View.GONE);
        textViewPaymentReference.setVisibility(View.VISIBLE);
        textViewBookedDate.setVisibility(View.VISIBLE);
//        String bookedDate=DateUtils.getTransactionDate(paymentResponse.getDate());
        String bookedString = BOOKED_ON + DateUtils.getTransactionDate(paymentResponse.getDate());
        SpannableString spanBoldBooked = boldBookedOn(bookedString, BOOKED_ON.length(), bookedString.length());
        textViewBookedDate.setText(spanBoldBooked);
        String paymentReference = PAYMENT_REFERENCE + paymentResponse.getReferenceId();
        SpannableString spanBoldReferenceID = boldBookedOn(paymentReference, PAYMENT_REFERENCE.length(), paymentReference.length());
        textViewPaymentReference.setText(spanBoldReferenceID);

        String paymentDetail = CONGRATULATION
                + paymentResponse.getClassDetailDto().getTitle() + SUCCESSFULLY_BOOKED;
        SpannableString spanBoldPayDetail = boldBookedOn(paymentDetail, CONGRATULATION.length(), CONGRATULATION.length() + paymentResponse.getClassDetailDto().getTitle().length());
        textViewPaymentDetail.setText(spanBoldPayDetail);

    }

    private void setPendingBookingStyle() {
        layoutPaymentStatus.setBackgroundColor(getResources().getColor(R.color.light_green));
        textViewPaymentStatus.setText("Class booking is pending");
        textViewPaymentStatus.setTextColor(getResources().getColor(R.color.green));
        buttonContactUs.setVisibility(View.GONE);
        textViewPaymentReference.setVisibility(View.VISIBLE);
        String paymentReference = PAYMENT_REFERENCE + paymentResponse.getReferenceId();
        SpannableString spanBoldReferenceID = boldBookedOn(paymentReference, PAYMENT_REFERENCE.length(), paymentReference.length());
        textViewPaymentReference.setText(spanBoldReferenceID);

        String bookedString = BOOKED_ON + paymentResponse.getClassDetailDto().getClassDate();
        SpannableString spanBoldBooked = boldBookedOn(bookedString, BOOKED_ON.length(), BOOKED_ON.length() + bookedString.length());
        textViewBookedDate.setText(spanBoldBooked);

        textViewBookedDate.setVisibility(View.VISIBLE);

        String paymentDetail = CLASS
                + paymentResponse.getClassDetailDto().getTitle() + getString(R.string.pending_detail);
        SpannableString spanBoldPayDetail = boldBookedOn(paymentDetail, CONGRATULATION.length(), CONGRATULATION.length() + paymentResponse.getClassDetailDto().getTitle().length());
        textViewPaymentDetail.setText(spanBoldPayDetail);

    }

    private SpannableString boldBookedOn(String bookedDate, int start, int end) {
        SpannableString spannableStr = new SpannableString(bookedDate);
        StyleSpan styleSpanBold = new StyleSpan(Typeface.BOLD);
        spannableStr.setSpan(styleSpanBold, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableStr;
    }

    private void getIntentData() {
        navigatedFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);
        callPaymentDetailApi();

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
        buttonContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
    }

    public enum PaymentStatus {
        SUCCESS,
        FAILURE,
        PENDING
    }

    private void setAnimation() {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(750);
        cardView.startAnimation(anim);
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.PAYMENT_CONFIRMATION_DETAIL:

                paymentResponse = ((ResponseModel<PaymentConfirmationResponse>) response).data;
//                if (paymentResponse.getStatus().equalsIgnoreCase("Success")) {
                    setConfirmBookingStyle();
                    handleData();
                /*} else if (paymentResponse.getStatus().equalsIgnoreCase("Failure"))
                {
                    setFailBookingStyle();
                    handleData();
                }
                else
                {
                    setPendingBookingStyle();
                    handleData();
                }*/

//                setData();
//                setConfirmBookingStyle();
//                handleData();
        }
    }

    private void setData() {
        switch (paymentStatus) {
            case SUCCESS:
                setConfirmBookingStyle();
                handleData();
                break;
            case FAILURE:
                setFailBookingStyle();
                break;
            case PENDING:
                setPendingBookingStyle();
                handleData();
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

    private void handleData() {
        textViewPackageName.setText(paymentResponse.getPackageName());
        textViewClassName.setText(paymentResponse.getClassDetailDto().getTitle());
        if (!TextUtils.isEmpty(aPackage.getValidityPeriod()))
            textViewValidity.setText(aPackage.getValidityPeriod());
        else
            textViewValidity.setText("N/A");
        textViewAmount.setText(String.valueOf(paymentResponse.getAmount()) + " KWD");
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.PAYMENT_CONFIRMATION_DETAIL:

                ToastUtils.show(this, errorMessage);
        }
    }
}

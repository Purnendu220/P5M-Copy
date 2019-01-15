package com.p5m.me.view.activity.Main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.data.PaymentConfirmationResponse;
import com.p5m.me.data.ValidityPackageList;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.fxn.utility.Constants;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

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
   @BindView(R.id.buttonInviteFriends)
    public Button buttonInviteFriends;
   @BindView(R.id.buttonSchedule)
    public Button buttonSchedule;
   @BindView(R.id.buttonsLayout)
    public LinearLayout buttonsLayout;

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
        enterFrom();
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
        ((TextView) v.findViewById(R.id.textViewTitle)).setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
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
            SUCCESSFULLY_BOOKED = " "+getString(R.string.has_been_extended_for) + selectedPacakageFromList.getDuration() + " " + getString(R.string.weeks) + ".";
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
            SUCCESSFULLY_BOOKED = " "+getString(R.string.pending_detail);
            if (!TextUtils.isEmpty(paymentResponse.getClassDetailDto().getTitle())) {
                CONGRATULATION = getString(R.string.classs)+" ";
                String paymentDetail = CONGRATULATION
                        + paymentResponse.getClassDetailDto().getTitle() + SUCCESSFULLY_BOOKED;
                SpannableString spanBoldPayDetail = boldString(paymentDetail, CONGRATULATION.length(), CONGRATULATION.length() + paymentResponse.getClassDetailDto().getTitle().length());
                textViewPaymentDetail.setText(spanBoldPayDetail);
            }
        } else {
            CONGRATULATION = getString(R.string.packages)+" ";
            String paymentDetail = CONGRATULATION
                    + paymentResponse.getPackageName() + SUCCESSFULLY_BOOKED;
            SpannableString spanBoldPayDetail = boldString(paymentDetail, CONGRATULATION.length(), CONGRATULATION.length() + paymentResponse.getPackageName().length());
            textViewPaymentDetail.setText(spanBoldPayDetail);
        }
    }
    private void setFailureBookingTitle() {
        if (paymentResponse.getClassDetailDto() != null) {
            SUCCESSFULLY_BOOKED = " "+getString(R.string.is_failed);
            if (!TextUtils.isEmpty(paymentResponse.getClassDetailDto().getTitle())) {
                CONGRATULATION = getString(R.string.unfortunately_class)+" "+getString(R.string.classs)+" ";
                String paymentDetail = CONGRATULATION
                        + paymentResponse.getClassDetailDto().getTitle() + SUCCESSFULLY_BOOKED;
                SpannableString spanBoldPayDetail = boldString(paymentDetail, CONGRATULATION.length(), CONGRATULATION.length() + paymentResponse.getClassDetailDto().getTitle().length());
                textViewPaymentDetail.setText(spanBoldPayDetail);
            }
        } else {
            CONGRATULATION = getString(R.string.unfortunately_class)+" "+getString(R.string.packages)+" ";
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
        buttonViewSchedule.setOnClickListener(this);
        buttonSchedule.setOnClickListener(this);
        buttonContactUs.setOnClickListener(this);
        buttonTryAgain.setOnClickListener(this);
        buttonInviteFriends.setOnClickListener(this);
    }

    private void redirectOnResult() {
        switch (checkoutFor) {
            case PACKAGE:

//                EventBroadcastHelper.sendPackagePurchased();
                HomeActivity.show(context, AppConstants.Tab.TAB_FIND_CLASS);
                buttonsLayout.setVisibility(View.GONE);
                buttonViewSchedule.setVisibility(View.VISIBLE);
                finish();
                break;
            case CLASS_PURCHASE_WITH_PACKAGE:
                classModel.setUserJoinStatus(true);

                EventBroadcastHelper.sendPackagePurchasedForClass(classModel);
                HomeActivity.show(context, AppConstants.Tab.TAB_SCHEDULE);
                buttonsLayout.setVisibility(View.VISIBLE);
                buttonViewSchedule.setVisibility(View.GONE);
//                sendAutoJoinEvent();
                break;
            case SPECIAL_CLASS:
                classModel.setUserJoinStatus(true);
                EventBroadcastHelper.sendClassPurchased(classModel);
                HomeActivity.show(context, AppConstants.Tab.TAB_SCHEDULE);
                buttonsLayout.setVisibility(View.VISIBLE);
                buttonViewSchedule.setVisibility(View.GONE);
//                sendAutoJoinEvent();
                break;
            case EXTENSION:
//                if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE) {
                HomeActivity.show(context, AppConstants.Tab.TAB_FIND_CLASS);
                buttonsLayout.setVisibility(View.GONE);

                buttonViewSchedule.setVisibility(View.VISIBLE);
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
                buttonsLayout.setVisibility(View.GONE);

                buttonViewSchedule.setVisibility(View.VISIBLE);
                finish();
                break;
        }
    }

 private void enterFrom() {
        switch (checkoutFor) {
            case PACKAGE:

                buttonInviteFriends.setVisibility(View.GONE);
//                buttonViewSchedule.setVisibility(View.VISIBLE);
                break;
            case CLASS_PURCHASE_WITH_PACKAGE:
                buttonInviteFriends.setVisibility(View.VISIBLE);
//                buttonViewSchedule.setVisibility(View.GONE);
                break;
            case SPECIAL_CLASS:
                buttonInviteFriends.setVisibility(View.VISIBLE);
//                buttonViewSchedule.setVisibility(View.GONE);
                break;
            case EXTENSION:
                buttonInviteFriends.setVisibility(View.GONE);

//                buttonViewSchedule.setVisibility(View.VISIBLE);
                break;
            case PENDING_TRANSACTION:
                buttonsLayout.setVisibility(View.GONE);

//                buttonViewSchedule.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonInviteFriends:
                Helper.shareClass(context, classModel.getClassSessionId(), classModel.getTitle());

                break;
            case R.id.buttonTryAgain:
                callPaymentDetailApi();
                break;

            case R.id.buttonViewSchedule:
            case R.id.buttonSchedule:

                redirectOnResult();
                break;
            case R.id.buttonContactUs:
                dialogContactUs();
                break;

        }
    }

    public enum PaymentStatus {
        SUCCESS,
        FAILURE,
        PENDING,
        INITIALIZE
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
//                paymentResponse.setStatus(PaymentStatus.FAILURE.name());
//                setData(PaymentStatus.FAILURE);

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
            case INITIALIZE:
                initializeStringVariables(getString(R.string.classs), getString(R.string.pending_detail));
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

    private void buttonHandler() {
        if (PaymentStatus.valueOf(paymentResponse.getStatus()) == PaymentStatus.SUCCESS) {
            if (paymentResponse.getClassDetailDto() != null) {
                buttonViewSchedule.setText(getString(R.string.view_schedule));
                buttonSchedule.setText(getString(R.string.view_schedule));
            } else {
                buttonViewSchedule.setText(getString(R.string.book_classes));
            }
        }
        if (PaymentStatus.valueOf(paymentResponse.getStatus()) == PaymentStatus.INITIALIZE) {
            buttonViewSchedule.setText(getString(R.string.transaction_history));
            buttonsLayout.setVisibility(View.GONE);
            buttonViewSchedule.setVisibility(View.VISIBLE);
            checkoutFor = PENDING_TRANSACTION;
        }
        if (PaymentStatus.valueOf(paymentResponse.getStatus()) == PaymentStatus.FAILURE) {
            buttonViewSchedule.setText(getString(R.string.transaction_history));
            buttonsLayout.setVisibility(View.GONE);
            buttonViewSchedule.setVisibility(View.VISIBLE);
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
        textViewValidity.setVisibility(View.GONE);
        imageViewPaymentStatus.setImageDrawable(getResources().getDrawable(R.drawable.pending));


        setPendingBookingTitle();

    }

    private void setFailBookingStyle() {
        imageViewPaymentStatus.setImageDrawable(getResources().getDrawable(R.drawable.failed));
        textViewValidity.setVisibility(View.GONE);

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
        setFailureBookingTitle();


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
    }

    private void dialogContactUs() {
        final List<String> items = new ArrayList<>();
        items.add(getString(R.string.mail_us));
        items.add(getString(R.string.make_a_call));

        DialogUtils.showBasicList(
                context,
                getString(R.string.contact_us),
                items,
                new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        dialog.dismiss();
                        if (position == 0) {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", "info@p5m.me", null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, R.string.contactUs);
                            startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));

                        } else if (position == 1) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:0096555028111"));
                            startActivity(intent);
                        }
                    }
                });
    }
}

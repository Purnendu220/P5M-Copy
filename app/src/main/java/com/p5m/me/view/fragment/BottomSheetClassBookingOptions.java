package com.p5m.me.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.RemoteConfigDataModel;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.view.activity.Main.CheckoutActivity;
import com.p5m.me.view.activity.Main.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BottomSheetClassBookingOptions extends BottomSheetDialogFragment implements View.OnClickListener {

    public static AdapterCallbacks<ClassModel> adapterCallbacks;

    public static BottomSheetClassBookingOptions newInstance(ClassModel classModel, BookWithFriendData bookWithFriendData, int noOfClasses, Package aPackage) {

        BottomSheetClassBookingOptions bottomSheetFragment = new BottomSheetClassBookingOptions();
        BottomSheetClassBookingOptions.classModel = classModel;
        BottomSheetClassBookingOptions.bookWithFriendData = bookWithFriendData;
        BottomSheetClassBookingOptions.noOfClasses = noOfClasses;
        BottomSheetClassBookingOptions.aPackage = aPackage;

        return bottomSheetFragment;
    }

    @BindView(R.id.textViewDropIn)
    TextView textViewDropIn;
    @BindView(R.id.textViewDropInDesc)
    TextView textViewDropInDesc;
    @BindView(R.id.textViewBuyMembership)
    TextView textViewBuyMembership;
    @BindView(R.id.textViewBuyMembershipDesc)
    TextView textViewBuyMembershipDesc;

    @BindView(R.id.textViewDialogDismiss)
    TextView textViewDialogDismiss;

    @BindView(R.id.bookWithP5mMembership)
    LinearLayout bookWithP5mMembership;
    @BindView(R.id.bookWithDropInPackage)
    LinearLayout bookWithDropInPackage;

    private static ClassModel classModel;
    private static BookWithFriendData bookWithFriendData;
    private static Package aPackage;
    private static int noOfClasses;
    private static boolean showDropInPrice = true;
    private static String mPackageDescription = "";
    private static String mPackageDescriptionDropIn = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.book_class_option, container,
                false);
        ButterKnife.bind(this, view);
        setRemoteConfigValue();
        if (showDropInPrice) {
            textViewDropIn.setText(String.format(getContext().getResources().getString(R.string.one_class_entry), LanguageUtils.numberConverter(aPackage.getCost(), 2) + " " + TempStorage.getUser().getCurrencyCode()));

        } else {
            textViewDropIn.setText(getContext().getResources().getString(R.string.one_class_entry_without_price));

        }
        handleClickEvent();

        if (mPackageDescription != null && !mPackageDescription.isEmpty()) {
            textViewBuyMembershipDesc.setVisibility(View.VISIBLE);
            textViewBuyMembershipDesc.setText(mPackageDescription);
        } else {
            textViewDropInDesc.setVisibility(View.GONE);

        }

        if (mPackageDescriptionDropIn != null && !mPackageDescriptionDropIn.isEmpty()) {
            textViewDropInDesc.setVisibility(View.VISIBLE);
            textViewDropInDesc.setText(mPackageDescriptionDropIn);
        } else {
            textViewDropInDesc.setVisibility(View.GONE);

        }

        return view;

    }

    private void setRemoteConfigValue() {
        String PLAN_DESCRIPTION_DROP_IN_VALUE = RemoteConfigConst.PLAN_DESCRIPTION_DROP_IN_VALUE;
        String PLAN_DESCRIPTION_VALUE = RemoteConfigConst.PLAN_DESCRIPTION_VALUE;

        try {
            if (RemoteConfigConst.DROP_IN_COST_VALUE != null && !RemoteConfigConst.DROP_IN_COST_VALUE.isEmpty()) {
                showDropInPrice = Boolean.valueOf(RemoteConfigConst.DROP_IN_COST_VALUE);
            }

            if (PLAN_DESCRIPTION_DROP_IN_VALUE != null && !PLAN_DESCRIPTION_DROP_IN_VALUE.isEmpty()) {
                Gson g = new Gson();
                RemoteConfigDataModel p = g.fromJson(PLAN_DESCRIPTION_DROP_IN_VALUE, new TypeToken<RemoteConfigDataModel>() {
                }.getType());
                if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("en") && !p.getEn().isEmpty()) {
                    mPackageDescriptionDropIn = p.getEn();

                } else if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar") && !p.getAr().isEmpty()) {
                    mPackageDescriptionDropIn = p.getAr();

                }
            }
            if (PLAN_DESCRIPTION_VALUE != null && !PLAN_DESCRIPTION_VALUE.isEmpty()) {
                Gson g = new Gson();
                RemoteConfigDataModel p = g.fromJson(PLAN_DESCRIPTION_VALUE, new TypeToken<RemoteConfigDataModel>() {
                }.getType());
                if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("en") && !p.getEn().isEmpty()) {
                    if (TempStorage.getUser().getCurrencyCode() != null &&
                            TempStorage.getUser().getCurrencyCode() == AppConstants.Currency.SAUDI_CURRENCY ||
                            TempStorage.getUser().getCurrencyCode() == AppConstants.Currency.SAUDI_CURRENCY_SHORT
                    )
                        mPackageDescription = p.getEn_ksa();
                    else
                        mPackageDescription = p.getEn();

                } else if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar") && !p.getAr().isEmpty()) {
                    if (TempStorage.getUser().getCurrencyCode() != null &&
                            TempStorage.getUser().getCurrencyCode() == AppConstants.Currency.SAUDI_CURRENCY ||
                            TempStorage.getUser().getCurrencyCode() == AppConstants.Currency.SAUDI_CURRENCY_SHORT
                    ) mPackageDescription = p.getAr_ksa();
                    else
                        mPackageDescription = p.getAr();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void handleClickEvent() {
        bookWithDropInPackage.setOnClickListener(this);
        bookWithP5mMembership.setOnClickListener(this);
        textViewDialogDismiss.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bookWithP5mMembership:
                if (bookWithFriendData != null) {
                    HomeActivity.show(getContext(), AppConstants.Tab.TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel, bookWithFriendData, aPackage.getNoOfClass());

                } else {
                    HomeActivity.show(getContext(), AppConstants.Tab.TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
                }
                MixPanel.trackBottomSheetChoosePackageButton();
                dismiss();

                break;
            case R.id.bookWithDropInPackage:
                if (bookWithFriendData != null) {
                    CheckoutActivity.openActivity(getContext(), aPackage, classModel, 2, bookWithFriendData, aPackage.getNoOfClass());

                } else {
                    CheckoutActivity.openActivity(getContext(), aPackage, classModel, 1, aPackage.getNoOfClass());
                }
                MixPanel.trackBottomSheetChoosePackageButton();
                dismiss();

                break;

            case R.id.textViewDialogDismiss:
                dismiss();
                break;

        }
    }
}


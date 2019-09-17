package com.p5m.me.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.eventbus.Events;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.view.activity.Main.CheckoutActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.base.BaseActivity;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.book_class_option, container,
                false);
        ButterKnife.bind(this, view);
//        textViewDropIn.setText(String.format(getContext().getResources().getString(R.string.one_class_entry),noOfClasses, LanguageUtils.numberConverter(aPackage.getCost(), 2)));
        if (bookWithFriendData != null && aPackage.getPackageType().equalsIgnoreCase(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN))
            textViewDropIn.setText(String.format(getContext().getResources().getString(R.string.one_class_entry), LanguageUtils.numberConverter(aPackage.getCost(), 2)));
        else
            textViewDropIn.setText(String.format(getContext().getResources().getString(R.string.one_class_entry), LanguageUtils.numberConverter(aPackage.getCost(), 2)));
        handleClickEvent();
        return view;

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
                dismiss();

                break;
            case R.id.bookWithDropInPackage:
                if (bookWithFriendData != null) {
                    CheckoutActivity.openActivity(getContext(), aPackage, classModel, 2, bookWithFriendData, aPackage.getNoOfClass());

                } else {
                    CheckoutActivity.openActivity(getContext(), aPackage, classModel, 1, aPackage.getNoOfClass());
                }
                dismiss();

                break;

            case R.id.textViewDialogDismiss:
                dismiss();
                break;

        }
    }
}


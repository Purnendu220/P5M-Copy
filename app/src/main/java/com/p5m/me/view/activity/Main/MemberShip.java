package com.p5m.me.view.activity.Main;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.MemberShipAdapter;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.UserPackageInfo;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.User;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.firebase_dynamic_link.FirebaseDynamicLinnk;
import com.p5m.me.helper.ClassListListenerHelper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.CustomAlertDialog;
import com.p5m.me.view.custom.PackageExtensionAlertDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemberShip extends BaseActivity implements AdapterCallbacks, NetworkCommunicator.RequestListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, CustomAlertDialog.OnAlertButtonAction {

    public static void openActivity(Context context, int navigationFrom) {
        context.startActivity(new Intent(context, MemberShip.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom));
    }

    public static void openActivity(Context context, int navigationFrom, ClassModel classModel) {
        Intent intent = new Intent(context, MemberShip.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        intent.putExtra(AppConstants.DataKey.CLASS_OBJECT, classModel);
        context.startActivity(intent);
    }
    public static void openActivity(Context context, int navigationFrom, ClassModel classModel, BookWithFriendData friendData,int numberOfPackagesToBuy) {
        Intent intent = new Intent(context, MemberShip.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        intent.putExtra(AppConstants.DataKey.CLASS_OBJECT, classModel);
        intent.putExtra(AppConstants.DataKey.BOOK_WITH_FRIEND_DATA,friendData);
        intent.putExtra(AppConstants.DataKey.NUMBER_OF_PACKAGES_TO_BUY,numberOfPackagesToBuy);
        context.startActivity(intent);
    }

    public static Intent createIntent(Context context, int navigationFrom) {
        return new Intent(context, MemberShip.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;



    private TextView mTextViewWalletAmount;
    private LinearLayout mLayoutUserWallet;
    private int navigatedFrom;
    private ClassModel classModel;
    private MemberShipAdapter memberShipAdapter;
    private UserPackageInfo userPackageInfo;
    private BookWithFriendData mFriendsData;
    private Package mAvailableDropInPackage;

    private Handler handler;
    private Runnable runnable;
    private int delay = 500;

    private User user;
    private boolean hasVisitedGymLimits;
    private boolean hasPurchased;
    private boolean hasClickedCheckout;
    private int mNumberOfPackagesToBuy;
    private static User.WalletDto mWalletCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship);

        ButterKnife.bind(activity);
        GlobalBus.getBus().register(this);
        setToolBar();

        handler = new Handler();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(true);
        FirebaseDynamicLinnk.getDynamicLink(this,getIntent());
        navigatedFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);
        classModel = (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_OBJECT);
        mFriendsData = (BookWithFriendData) getIntent().getSerializableExtra(AppConstants.DataKey.BOOK_WITH_FRIEND_DATA);
        mNumberOfPackagesToBuy=getIntent().getIntExtra(AppConstants.DataKey.NUMBER_OF_PACKAGES_TO_BUY,1);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        memberShipAdapter = new MemberShipAdapter(context, navigatedFrom, false, this);
        recyclerView.setAdapter(memberShipAdapter);

        try {
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        onRefresh();


        MixPanel.trackMembershipVisit(navigatedFrom);
        onTrackingNotification();
        FirebaseAnalysic.trackMembershipVisit(navigatedFrom);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatePackage(Events.UpdatePackage data) {
        refreshFromEvent();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPackagePurchased(Events.PackagePurchased packagePurchased) {
        refreshFromEvent();
        hasPurchased = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void packagePurchasedForClass(Events.PackagePurchasedForClass data) {
        refreshFromEvent();
        hasPurchased = true;
        finish();
    }

    private void refreshFromEvent() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }

        swipeRefreshLayout.setRefreshing(true);
        memberShipAdapter.clearAll();
        memberShipAdapter.notifyDataSetChanges();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        }, delay);

//        onRefresh();
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
        mTextViewWalletAmount=(TextView)v.findViewById(R.id.textViewWalletAmount);
        mLayoutUserWallet=(LinearLayout)v.findViewById(R.id.layoutUserWallet);
        mLayoutUserWallet.setOnClickListener(this);

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.membership));

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    private void checkPackages() {
        userPackageInfo = new UserPackageInfo(user);

        swipeRefreshLayout.setRefreshing(false);

        if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS) {
            memberShipAdapter.setClassModel(classModel);

            // show general, ready, and owned packages
            if (userPackageInfo.havePackages) {
                memberShipAdapter.addAllOwnedPackages(userPackageInfo.userPackageReady);

                if (userPackageInfo.haveGeneralPackage && !user.isBuyMembership()) {
                    // User have General package and may be also have dropins..
                    memberShipAdapter.addOwnedPackages(userPackageInfo.userPackageGeneral);
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_only_drop_in_package_heading_1),
                            context.getString(R.string.membership_only_drop_in_package_heading_2));
                }
                else if(userPackageInfo.haveGeneralPackage && user.isBuyMembership()){
                    memberShipAdapter.addOwnedPackages(userPackageInfo.userPackageGeneral);
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_no_package_heading_1),
                            context.getString(R.string.membership_only_drop_in_package_heading_2));
                }

                else {
                    // User don't have General package but may have dropins..
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_drop_in_package_heading_1), context.getString(R.string.membership_drop_in_package_heading_2));
                }
            } else {
                // User have no packages
                // Show offered packages
                memberShipAdapter.setHeaderText(context.getString(R.string.membership_no_package_heading_1), context.getString(R.string.membership_no_package_heading_2));
            }

            swipeRefreshLayout.setRefreshing(true);
            if(mFriendsData!=null){
                networkCommunicator.getPackagesForClass(user.getId(), classModel.getGymBranchDetail().getGymId(), classModel.getClassSessionId(),mNumberOfPackagesToBuy, this, false);

            }else{
                networkCommunicator.getPackagesForClass(user.getId(), classModel.getGymBranchDetail().getGymId(), classModel.getClassSessionId(),1, this, false);
            }

        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SETTING ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_DEEPLINK_ACTIVITY||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MEMBERSHIP) {

            if (userPackageInfo.havePackages) {

                memberShipAdapter.addAllOwnedPackages(userPackageInfo.userPackageReady);

                if (!userPackageInfo.haveGeneralPackage) {
                    // User don't have General package..
                    // get general and show owned packages
                    swipeRefreshLayout.setRefreshing(true);
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_drop_in_package_heading_1),
                            context.getString(R.string.membership_drop_in_package_heading_2));

                } else {
                    // User only have drop in packages..
                    // only Show owned packages..
                    memberShipAdapter.addOwnedPackages(userPackageInfo.userPackageGeneral);
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_general_package_heading_1), context.getString(R.string.membership_general_package_heading_2));
                    memberShipAdapter.notifyDataSetChanges();
                }
            }
            if(user.isBuyMembership()){
                swipeRefreshLayout.setRefreshing(true);
                networkCommunicator.getPackages(user.getId(), this, false);
                if(userPackageInfo.haveDropInPackage||userPackageInfo.haveGeneralPackage){
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_drop_in_package_heading_1),
                            context.getString(R.string.membership_drop_in_package_heading_2));
                }else{
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_drop_in_package_heading_1),
                            context.getString(R.string.membership_general_package_heading_1));
                }

            }
        }
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

        switch (view.getId()) {
            case R.id.textViewViewLimit: {

                String name = "";
                if (model instanceof Package) {
                    Package aPackage = (Package) model;
                    name = aPackage.getName();

                } else if (model instanceof UserPackage) {
                    UserPackage aPackage = (UserPackage) model;
                    name = aPackage.getPackageName();
                }
                PackageLimitsActivity.openActivity(context, name);
                hasVisitedGymLimits = true;
            }
            break;
            case R.id.button: {

                final Package aPackage = (Package) model;
                if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS) {
                    if(mFriendsData!=null && aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)){
                        CheckoutActivity.openActivity(context, aPackage, classModel,2,mFriendsData,aPackage.getNoOfClass());
                        return;
                        }
                    if(mFriendsData!=null && aPackage.getGymVisitLimit()==1){
                        DialogUtils.showBasicMessage(context,"",
                                getString(R.string.this_package_has)+" "+aPackage.getGymVisitLimit()+getString(R.string.limit_for_this_gym)
                                ,context.getResources().getString(R.string.continue_with), new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();

                                    }
                                });

                    }else{
                        CheckoutActivity.openActivity(context, aPackage, classModel,1,mFriendsData,aPackage.getNoOfClass());
                        return;
                    }

                } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SETTING ||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE ||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION ||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN ||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_DEEPLINK_ACTIVITY
                        ) {
                    CheckoutActivity.openActivity(context, aPackage);

                    MixPanel.trackPackagePreferred(aPackage.getName());
                    hasClickedCheckout = true;
                }
            }
            break;
            case R.id.imageViewInfo: {
                if (model instanceof Package) {
                    Package aPackage = (Package) model;
                    if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                        DialogUtils.showBasicMessage(context, aPackage.getName().toUpperCase(), context.getString(R.string.ok), R.string.membership_package_limit_info);
                    } else if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                        DialogUtils.showBasicMessage(context, aPackage.getName().toUpperCase(), context.getString(R.string.ok), R.string.membership_drop_in_info);
                    }
                } else if (model instanceof UserPackage) {
                    UserPackage aPackage = (UserPackage) model;
                    if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                        DialogUtils.showBasicMessage(context, aPackage.getPackageName().toUpperCase(), context.getString(R.string.ok), R.string.membership_package_limit_info);
                    } else if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                        DialogUtils.showBasicMessage(context, aPackage.getPackageName().toUpperCase(), context.getString(R.string.ok), R.string.membership_drop_in_info);
                    }
                }
            }
            break;

            case R.id.textViewExtendPackage:{
                if(model instanceof UserPackage){
                    try {
                        PackageExtensionAlertDialog dialog=new PackageExtensionAlertDialog(context,AppConstants.AppNavigation.NAVIGATION_FROM_MEMBERSHIP, (UserPackage) model) ;
                        dialog.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }


            }
            break;
        }
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
    }

    @Override
    public void onShowLastItem() {
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.PACKAGES_LIMIT:
            case NetworkCommunicator.RequestCode.PACKAGES_FOR_USER:

                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);

                List<Package> packagesTemp = ((ResponseModel<List<Package>>) response).data;
                if (packagesTemp != null && !packagesTemp.isEmpty()) {
                    List<Package> packages = new ArrayList<>(packagesTemp.size());

                    for (Package aPackage : packagesTemp) {
                        if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                            if (user.isBuyMembership()) {
                                packages.add(aPackage);
                            }
                        } else if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                            aPackage.setGymName(classModel.getGymBranchDetail().getGymName());
                            packages.add(aPackage);
                            mAvailableDropInPackage=aPackage;
                        }
                    }
                    if(mFriendsData!=null){
                        List<Package> packagesWithVisitLimit = new ArrayList<>();
                        for (Package aPackage : packages) {
                            aPackage.setBookingWithFriend(true);
                            if(aPackage.getGymVisitLimit()!=1){
                                packagesWithVisitLimit.add(aPackage);
                            }

                        }
                        memberShipAdapter.addAllOfferedPackages(packagesWithVisitLimit);


                    }else{
                        memberShipAdapter.addAllOfferedPackages(packages);

                    }


                }
                memberShipAdapter.notifyDataSetChanges();
                break;

//            case NetworkCommunicator.RequestCode.BUY_PACKAGE:
//
//                swipeRefreshLayout.setRefreshing(false);
//
//                PaymentWebViewActivity.open(activity, promoCode.code, packageName, ((ResponseModel<PaymentUrl>) response).data);
//                memberShipAdapter.notifyDataSetChanges();
//                break;

            case NetworkCommunicator.RequestCode.ME_USER:
                user = TempStorage.getUser();
                mWalletCredit= user.getWalletDto();
                if(mWalletCredit!=null&&mWalletCredit.getBalance()>0){
                    mLayoutUserWallet.setVisibility(View.VISIBLE);
                    mTextViewWalletAmount.setText(LanguageUtils.numberConverter(mWalletCredit.getBalance(),2)+" "+ mWalletCredit.getCurrencyCode());
                }else{
                    mLayoutUserWallet.setVisibility(View.GONE);

                }
                checkPackages();

                break;
        }
    }



    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(true);

//        switch (requestCode) {
//            case NetworkCommunicator.RequestCode.BUY_PACKAGE:
//                ToastUtils.showFailureResponse(context, errorMessage);
//                memberShipAdapter.notifyDataSetChanges();
//
//                break;
//        }
    }

    @Override
    public void onRefresh() {

        memberShipAdapter.clearAll();
        memberShipAdapter.notifyDataSetChanges();

        networkCommunicator.getMyUser(this, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!hasPurchased && hasVisitedGymLimits) {
            MixPanel.trackSequentialUpdate(AppConstants.Tracker.VIEW_LIMIT_NO_PURCHASE);
        }

        if (!hasVisitedGymLimits && !hasClickedCheckout) {
            MixPanel.trackSequentialUpdate(AppConstants.Tracker.NO_ACTION);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layoutUserWallet:
                showWalletAlert();
                break;

        }

    }

    private void showWalletAlert(){
        CustomAlertDialog mCustomAlertDialog = new CustomAlertDialog(context, context.getString(R.string.wallet_alert_title), context.getString(R.string.wallet_alert),1,"",context.getResources().getString(R.string.ok),CustomAlertDialog.AlertRequestCodes.ALERT_REQUEST_WALLET_INFO,null,true, this);
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
}

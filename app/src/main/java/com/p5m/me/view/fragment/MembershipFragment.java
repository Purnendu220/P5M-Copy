package com.p5m.me.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.MemberShipAdapter;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.IntercomEvents;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.UserPackageInfo;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.User;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.helper.Helper;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.Main.CheckoutActivity;
import com.p5m.me.view.activity.Main.MembershipInfoActivity;
import com.p5m.me.view.activity.Main.PackageLimitsActivity;
import com.p5m.me.view.custom.AlertP5MCreditInfo;
import com.p5m.me.view.custom.CustomAlertDialog;
import com.p5m.me.view.custom.OnAlertButtonAction;
import com.p5m.me.view.custom.PackageExtensionAlertDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.AppConstants.Pref.MEMBERSHIP_INFO_STATE_NO_PACKAGE;


public class MembershipFragment extends BaseFragment implements ViewPagerFragmentSelection, AdapterCallbacks, NetworkCommunicator.RequestListener,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, CustomAlertDialog.OnAlertButtonAction, OnAlertButtonAction {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.textGymVisitLimits)
    TextView textGymVisitLimits;

    @BindView(R.id.imageViewInfo)
    ImageView imageViewInfo;

    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;


    @BindView(R.id.textViewWalletAmount)
    public TextView mTextViewWalletAmount;
    @BindView(R.id.layoutUserWallet)
    public LinearLayout mLayoutUserWallet;
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
    private int mNumberOfPackagesToBuy;
    private static User.WalletDto mWalletCredit;
    private boolean isTabSelected=false;
    private boolean showChoosePackageOption=true;
    private boolean isBuyMoreCredits =false;


    public MembershipFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    public static MembershipFragment newInstance(int navigationFrom) {
        MembershipFragment fragment = new MembershipFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        fragment.setArguments(args);
        return fragment;
    }

    public static MembershipFragment newInstance(int navigationFrom, ClassModel classModel) {
        MembershipFragment fragment = new MembershipFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        args.putSerializable(AppConstants.DataKey.CLASS_OBJECT, classModel);
        fragment.setArguments(args);
        return fragment;
    }

    public static MembershipFragment newInstance(int navigationFrom, ClassModel classModel, BookWithFriendData friendData, int numberOfPackagesToBuy) {
        MembershipFragment fragment = new MembershipFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        args.putInt(AppConstants.DataKey.NUMBER_OF_PACKAGES_TO_BUY, numberOfPackagesToBuy);
        args.putSerializable(AppConstants.DataKey.CLASS_OBJECT, classModel);
        args.putSerializable(AppConstants.DataKey.BOOK_WITH_FRIEND_DATA, friendData);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_member_ship, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this, getView());

        handler = new Handler();

        swipeRefreshLayout.setOnRefreshListener(this);
        textGymVisitLimits.setOnClickListener(this);
        imageViewInfo.setOnClickListener(this);
        //FirebaseDynamicLinnk.getDynamicLink(this,getArguments());
        navigatedFrom = getArguments().getInt(AppConstants.DataKey.NAVIGATED_FROM_INT, AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS);
        classModel = (ClassModel) getArguments().getSerializable(AppConstants.DataKey.CLASS_OBJECT);
        mFriendsData = (BookWithFriendData) getArguments().getSerializable(AppConstants.DataKey.BOOK_WITH_FRIEND_DATA);
        mNumberOfPackagesToBuy = getArguments().getInt(AppConstants.DataKey.NUMBER_OF_PACKAGES_TO_BUY, 1);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);
        mLayoutUserWallet.setOnClickListener(this);
        memberShipAdapter = new MemberShipAdapter(context, navigatedFrom, false, this);
        recyclerView.setAdapter(memberShipAdapter);

        try {
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        onRefresh();


//        MixPanel.trackMembershipVisit(navigatedFrom);
        //  onTrackingNotification();
        FirebaseAnalysic.trackMembershipVisit(navigatedFrom);
        if(RemoteConfigConst.SHOW_SELECTION_OPTIONS_VALUE!=null && !RemoteConfigConst.SHOW_SELECTION_OPTIONS_VALUE.isEmpty()){
            showChoosePackageOption = Boolean.valueOf(RemoteConfigConst.SHOW_SELECTION_OPTIONS_VALUE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatePackage(Events.UpdatePackage data) {
        if (!swipeRefreshLayout.isRefreshing()) {
            this.navigatedFrom = -1;
            this.classModel = null;
            this.mFriendsData = null;
            this.mNumberOfPackagesToBuy = 1;
            refreshFromEvent();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPackagePurchased(Events.PackagePurchased packagePurchased) {
        if (!swipeRefreshLayout.isRefreshing()) {

            this.navigatedFrom = -1;
            this.classModel = null;
            this.mFriendsData = null;
            this.mNumberOfPackagesToBuy = 1;
            refreshFromEvent();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void packagePurchasedForClass(Events.PackagePurchasedForClass data) {
        if (!swipeRefreshLayout.isRefreshing()) {
            this.navigatedFrom = -1;
            this.classModel = null;
            this.mFriendsData = null;
            this.mNumberOfPackagesToBuy = 1;
            refreshFromEvent();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void classJoin(Events.ClassJoin data) {
        if (!swipeRefreshLayout.isRefreshing()) {
            this.navigatedFrom = -1;
            this.classModel = null;
            this.mFriendsData = null;
            this.mNumberOfPackagesToBuy = 1;
            refreshFromEvent();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUser(Events.UserUpdate userUpdate) {


    }

    public void refreshFragment(int navigatedFrom, ClassModel classModel, BookWithFriendData mFriendsData, int mNumberOfPackagesToBuy,boolean addCredits) {
        if (!swipeRefreshLayout.isRefreshing()) {
            if (this.navigatedFrom != navigatedFrom || this.classModel != classModel || this.mFriendsData != mFriendsData || this.mNumberOfPackagesToBuy != mNumberOfPackagesToBuy|| this.isBuyMoreCredits != addCredits) {
                this.navigatedFrom = navigatedFrom;
                this.classModel = classModel;
                this.mFriendsData = mFriendsData;
                this.mNumberOfPackagesToBuy = mNumberOfPackagesToBuy;
                this.isBuyMoreCredits = addCredits;
                refreshFromEvent();
            }else{
                memberShipAdapter.setClassModel(null);
                memberShipAdapter.notifyDataSetChanges();
            }
        }
        if (TempStorage.isOpenMembershipInfo() == MEMBERSHIP_INFO_STATE_NO_PACKAGE) {
//           MembershipInfoActivity.openActivity(context);
//            TempStorage.setOpenMembershipInfo(MEMBERSHIP_INFO_STATE_DONE);
        }
        MixPanel.trackMembershipVisit(this.navigatedFrom);
        //  onTrackingNotification();
        FirebaseAnalysic.trackMembershipVisit(this.navigatedFrom);
        IntercomEvents.trackMembershipVisit(this.navigatedFrom);
        setUserWalletDetail();
    }

    public void refreshFragmentBackGroung(int navigatedFrom, ClassModel classModel, BookWithFriendData mFriendsData, int mNumberOfPackagesToBuy) {
        if (!swipeRefreshLayout.isRefreshing()) {
            if (this.navigatedFrom != navigatedFrom || this.classModel != classModel || this.mFriendsData != mFriendsData || this.mNumberOfPackagesToBuy != mNumberOfPackagesToBuy) {
                this.navigatedFrom = navigatedFrom;
                this.classModel = classModel;
                this.mFriendsData = mFriendsData;
                this.mNumberOfPackagesToBuy = mNumberOfPackagesToBuy;
                refreshFromEvent();
            }else{
                memberShipAdapter.setClassModel(null);
                memberShipAdapter.notifyDataSetChanges();

            }
        }
        setUserWalletDetail();
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

        //onRefresh();
    }

    public void fragmentPaused(){
        if(isBuyMoreCredits){
            LogUtils.networkError("************"+isBuyMoreCredits+" ********* fragmentPaused");
            memberShipAdapter.clearAll();
            checkPackages();
        }

    }

    private void checkPackages(boolean ... buyMoreCredits) {
        isBuyMoreCredits = false;
        userPackageInfo = new UserPackageInfo(user);

        swipeRefreshLayout.setRefreshing(false);

        if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS) {
            memberShipAdapter.setClassModel(classModel);

            // show general, ready, and owned packages
            if (userPackageInfo.havePackages) {
                if (userPackageInfo.haveGeneralPackage && !user.isBuyMembership()) {
                    // User have General package and may be also have dropins..
                    if(classModel==null)
                    memberShipAdapter.addOwnedPackages(userPackageInfo.userPackageGeneral);

                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_only_drop_in_package_heading_1),
                            context.getString(R.string.membership_only_drop_in_package_heading_2));
                } else if (userPackageInfo.haveGeneralPackage && user.isBuyMembership()) {
                    if(classModel==null)
                        memberShipAdapter.addOwnedPackages(userPackageInfo.userPackageGeneral);

                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_no_package_heading_1),
                            context.getString(R.string.membership_only_drop_in_package_heading_2));
                } else {
                    // User don't have General package but may have dropins..
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_drop_in_package_heading_1), context.getString(R.string.membership_drop_in_package_heading_2));
                }
            } else {
                // User have no packages
                // Show offered packages
                memberShipAdapter.setHeaderText(context.getString(R.string.membership_no_package_heading_1), context.getString(R.string.membership_no_package_heading_2));
            }

            swipeRefreshLayout.setRefreshing(true);
            if (mFriendsData != null) {
                networkCommunicator.getPackagesForClass(user.getId(), classModel.getGymBranchDetail().getGymId(), classModel.getClassSessionId(), mNumberOfPackagesToBuy, this, false);

            } else {
                networkCommunicator.getPackagesForClass(user.getId(), classModel.getGymBranchDetail().getGymId(), classModel.getClassSessionId(), 1, this, false);

            }
            memberShipAdapter.notifyDataSetChanges();

        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SETTING ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_DEEPLINK_ACTIVITY ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MEMBERSHIP ||
                navigatedFrom == -1
        ) {
            memberShipAdapter.setClassModel(null);

            if (userPackageInfo.havePackages) {
                if(userPackageInfo.haveGeneralPackage){
                    memberShipAdapter.clearAllOwnedPackages();
                    if(buyMoreCredits==null||buyMoreCredits.length<1 || !buyMoreCredits[0] ){
                        memberShipAdapter.addOwnedPackages(userPackageInfo.userPackageGeneral);

                    }


                }
            }
            if (user.isBuyMembership()) {
                memberShipAdapter.clearAll();
                imageViewInfo.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(true);
                networkCommunicator.getPackages(user.getId(), this, false);
                if (userPackageInfo.haveDropInPackage || userPackageInfo.haveGeneralPackage) {
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_drop_in_package_heading_1),
                            context.getString(R.string.membership_drop_in_package_heading_2));
                } else {
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_drop_in_package_heading_1),
                            context.getString(R.string.membership_general_package_heading_1));
                }
                textGymVisitLimits.setVisibility(View.GONE);

            } else {
                textGymVisitLimits.setVisibility(View.GONE);
            }
            if(buyMoreCredits!=null&&buyMoreCredits.length>0&&buyMoreCredits[0]){
                swipeRefreshLayout.setRefreshing(true);
                networkCommunicator.getPackages(user.getId(), this, false);
                isBuyMoreCredits = buyMoreCredits[0];
            }
            memberShipAdapter.notifyDataSetChanges();

        }
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

        switch (view.getId()) {

            case R.id.layoutMainOfferedPackage: {
                final Package aPackage = (Package) model;
                if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS) {
                    if (mFriendsData != null && aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                        CheckoutActivity.openActivity(context, aPackage, classModel, 2, mFriendsData, aPackage.getNoOfClass(),aPackage.getCredits());
                        return;
                    }
                    if (mFriendsData != null && aPackage.getGymVisitLimit() == 1) {
                        DialogUtils.showBasicMessage(context, "",
                                getString(R.string.this_package_has) + " " + LanguageUtils.numberConverter(aPackage.getGymVisitLimit()) + getString(R.string.limit_for_this_gym)
                                , context.getResources().getString(R.string.continue_with), new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();

                                    }
                                });

                    } else {

                        CheckoutActivity.openActivity(context, aPackage, classModel, 1, mFriendsData, aPackage.getNoOfClass(), aPackage.getCredits());
                        return;
                    }

                } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SETTING ||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE ||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION ||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN ||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS ||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE ||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_DEEPLINK_ACTIVITY ||
                        navigatedFrom == -1
                ) {
                    CheckoutActivity.openActivity(context, aPackage);

                    MixPanel.trackPackagePreferred(aPackage.getName());
                }
            }
            break;

            case R.id.textViewExtendPackage: {
                if (model instanceof UserPackage) {
                    try {
                        PackageExtensionAlertDialog dialog = new PackageExtensionAlertDialog(context, AppConstants.AppNavigation.NAVIGATION_FROM_MEMBERSHIP, (UserPackage) model);
                        dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }
            break;

            case R.id.imageViewNotApplicableInfo: {
                if (model instanceof Package) {
                    Package classModel = (Package) model;
                    String message = "";
                    if (classModel.getValidityPeriod().contains("MONTH")) {
                        if (classModel.getDuration() == 1) {
                            message = context.getResources().getString(R.string.a_month);

                        } else
                            message = String.format(context.getResources().getString(R.string.months), classModel.getDuration());


                    } else if (classModel.getValidityPeriod().contains("WEEK")) {
                        if (classModel.getDuration() == 1) {
                            message = context.getResources().getString(R.string.a_week);
                        } else
                            message = String.format(context.getResources().getString(R.string.weeks_value), String.valueOf(LanguageUtils.numberConverter(classModel.getDuration())));

                    }
                    DialogUtils.showBasic(context, String.format(getString(R.string.clas_exceed), message),"", getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    });
                }
            }
            break;
            case R.id.txtPackageOffredClassesLimits:
                if (model instanceof Package) {
                    Package pkg = (Package) model;
                    AlertP5MCreditInfo alert = new AlertP5MCreditInfo(context,pkg,MembershipFragment.this);
                    alert.show();



                }
                break;
            case R.id.textViewBuyMoreCredits:
                checkPackages(true);
                break;

        }
        }

        @Override
        public void onAdapterItemLongClick (RecyclerView.ViewHolder viewHolder, View view, Object
        model,int position){
        }

        @Override
        public void onShowLastItem () {
        }

        @Override
        public void onApiSuccess (Object response,int requestCode){

            switch (requestCode) {
                case NetworkCommunicator.RequestCode.PACKAGES_LIMIT:
                case NetworkCommunicator.RequestCode.PACKAGES_FOR_USER:

                    swipeRefreshLayout.setRefreshing(false);

                    List<Package> packagesTemp = ((ResponseModel<List<Package>>) response).data;
                    if (packagesTemp != null && !packagesTemp.isEmpty()) {
                        List<Package> packages = new ArrayList<>(packagesTemp.size());

                        for (Package aPackage : packagesTemp) {
                            if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)&&!Helper.isPlanExpiring(classModel,aPackage)) {
                                if (user.isBuyMembership()||classModel!=null||isBuyMoreCredits) {
                                    if(classModel==null||(classModel!=null&& Helper.requiredCreditForClass(user,classModel,mFriendsData==null?1:2)<=aPackage.getCredits()))
                                    packages.add(aPackage);
                                }
                            }
                        }
                        if (mFriendsData != null) {
                            List<Package> packagesWithVisitLimit = new ArrayList<>();
                            for (Package aPackage : packages) {
                                aPackage.setBookingWithFriend(true);
                                if(classModel!=null&&classModel.getPriceModel().equalsIgnoreCase(AppConstants.PriceModels.CHARGABLE)){
                                    if (aPackage.getGymVisitLimit() != 1) {
                                        packagesWithVisitLimit.add(aPackage);
                                    }
                                }else{
                                    packagesWithVisitLimit.add(aPackage);

                                }



                            }
                            if(packagesWithVisitLimit!=null&&packagesWithVisitLimit.size()>0){
                                memberShipAdapter.clearAllOwnedPackages();
                            }
                            memberShipAdapter.addAllOfferedPackages(packagesWithVisitLimit);


                        }
                        else {
                            if(packages!=null&&packages.size()>0){
                                memberShipAdapter.clearAllOwnedPackages();
                                memberShipAdapter.clearAll();
                            }
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
                    setUserWalletDetail();
                    if(isBuyMoreCredits){
                        checkPackages(isBuyMoreCredits);
                    }else{
                        checkPackages();

                    }



                    break;
            }
        }


        @Override
        public void onApiFailure (String errorMessage,int requestCode){

            swipeRefreshLayout.setRefreshing(false);

//        switch (requestCode) {
//            case NetworkCommunicator.RequestCode.BUY_PACKAGE:
//                ToastUtils.showFailureResponse(context, errorMessage);
//                memberShipAdapter.notifyDataSetChanges();
//
//                break;
//        }
        }

        private void setUserWalletDetail(){
            user = TempStorage.getUser();
            mWalletCredit = user.getWalletDto();
            if (mWalletCredit != null && mWalletCredit.getBalance() > 0) {
                mLayoutUserWallet.setVisibility(View.VISIBLE);
                mTextViewWalletAmount.setText(LanguageUtils.numberConverter(mWalletCredit.getBalance(), 2) + " " + TempStorage.getUser().getCurrencyCode());
            } else {
                mLayoutUserWallet.setVisibility(View.GONE);

            }
        }

        @Override
        public void onRefresh () {

            memberShipAdapter.clearAll();
            memberShipAdapter.notifyDataSetChanges();
            networkCommunicator.getMyUser(this, false);

        }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        if (!hasPurchased && hasVisitedGymLimits) {
//            MixPanel.trackSequentialUpdate(AppConstants.Tracker.VIEW_LIMIT_NO_PURCHASE);
//        }
//
//        if (!hasVisitedGymLimits && !hasClickedCheckout) {
//            MixPanel.trackSequentialUpdate(AppConstants.Tracker.NO_ACTION);
//        }
//    }

        @Override
        public void onClick (View view){
            switch (view.getId()) {
                case R.id.layoutUserWallet:
                    showWalletAlert();
                    break;
                case R.id.textGymVisitLimits:
                    PackageLimitsActivity.openActivity(context, "");

                    break;
                case R.id.imageViewInfo:
                    MembershipInfoActivity.openActivity(context);

                    break;
            }

        }

        private void showWalletAlert () {
            CustomAlertDialog mCustomAlertDialog = new CustomAlertDialog(context, context.getString(R.string.wallet_alert_title), context.getString(R.string.wallet_alert), 1, "", context.getResources().getString(R.string.ok), CustomAlertDialog.AlertRequestCodes.ALERT_REQUEST_WALLET_INFO, null, true, this);
            try {
                mCustomAlertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onOkClick ( int requestCode, Object data){
        switch (requestCode){
            case AppConstants.AlertRequestCodes.ALERT_REQUEST_PURCHASE:
                if(data!=null&&data instanceof  Package){
                    Package modelPkg =(Package)data;
                    CheckoutActivity.openActivity(context, modelPkg);
                    MixPanel.trackPackagePreferred(modelPkg.getName());

                }
                break;
        }

        }

        @Override
        public void onCancelClick ( int requestCode, Object data){
            switch (requestCode){
                case AppConstants.AlertRequestCodes.ALERT_REQUEST_PURCHASE_CANCEL:
                    break;
            }
        }

        @Override
        public void onTabSelection ( int position){

        }

}

package com.p5m.me.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ClassMiniViewAdapter;
import com.p5m.me.adapters.MemberShipAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.UserPackageInfo;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.User;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.helper.ClassListListenerHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.CheckoutActivity;
import com.p5m.me.view.activity.Main.PackageLimitsActivity;
import com.p5m.me.view.activity.custom.MyRecyclerView;
import com.p5m.me.view.custom.CustomAlertDialog;
import com.p5m.me.view.custom.PackageExtensionAlertDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.AppConstants.AppNavigation.NAVIGATION_FROM_MEMBERSHIP;

/*ViewPagerFragmentSelection,
        MyRecyclerView.LoaderCallbacks,  View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterCallbacks<ClassModel>,
        NetworkCommunicator.RequestListener , CustomAlertDialog.OnAlertButtonAction*/
public class MembershipList extends BaseFragment implements
        AdapterCallbacks, NetworkCommunicator.RequestListener,ViewPagerFragmentSelection, MyRecyclerView.LoaderCallbacks,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, CustomAlertDialog.OnAlertButtonAction{


    private MemberShipAdapter memberShipAdapter;
    private int navigatedFrom;
    private UserPackageInfo userPackageInfo;
    private User user;
    private ClassModel classModel;
    private int mNumberOfPackagesToBuy;
    private BookWithFriendData mFriendsData;
    private boolean hasClickedCheckout;
    private Package mAvailableDropInPackage;
    private boolean hasVisitedGymLimits;

    public static Fragment createFragment(int position, int shownIn) {
        Fragment tabFragment = new MembershipList();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        bundle.putInt(AppConstants.DataKey.TAB_SHOWN_IN_INT, shownIn);
        bundle.putInt(AppConstants.DataKey.NAVIGATED_FROM_INT, NAVIGATION_FROM_MEMBERSHIP);

        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    public static Fragment createFragment(String queryString, int position, int shownIn, int navigatedFrom) {
        Fragment tabFragment = new MembershipList();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        bundle.putString(AppConstants.DataKey.QUERY_STRING, queryString);
        bundle.putInt(AppConstants.DataKey.TAB_SHOWN_IN_INT, shownIn);
        bundle.putInt(AppConstants.DataKey.NAVIGATED_FROM_INT, navigatedFrom);

        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    @BindView(R.id.recyclerView)
    public RecyclerView recycleView;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

   /* @BindView(R.id.layoutNoData)
    public View layoutNoData;
    @BindView(R.id.imageViewEmptyLayoutImage)
    public ImageView imageViewEmptyLayoutImage;
    @BindView(R.id.textViewEmptyLayoutText)
    public TextView textViewEmptyLayoutText;*/

    private int fragmentPositionInViewPager;
    private boolean isShownFirstTime = true;
    private boolean shouldRefresh = false;
    private int page;
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_INNER_CLASS_LIST;
    private String searchedKeywords;

    private int shownInScreen;

    public MembershipList() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        GlobalBus.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        GlobalBus.getBus().unregister(this);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_ship, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        fragmentPositionInViewPager = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);
        shownInScreen = getArguments().getInt(AppConstants.DataKey.TAB_SHOWN_IN_INT);
        navigatedFrom = getArguments().getInt(AppConstants.DataKey.NAVIGATED_FROM_INT);
        searchedKeywords = getArguments().getString(AppConstants.DataKey.QUERY_STRING, "");

        swipeRefreshLayout.setOnRefreshListener(this);

        recycleView.setLayoutManager(new LinearLayoutManager(activity));
        recycleView.setHasFixedSize(true);

        try {
            ((SimpleItemAnimator) recycleView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        memberShipAdapter = new MemberShipAdapter(context,shownInScreen, navigatedFrom, false, this);
        recycleView.setAdapter(memberShipAdapter);
        onRefresh();
    }

    @Override
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {
        LogUtils.debug("onShowLoader " + position);
    }



   /* @Override
    public void onShowLastItem() {
        page++;
        callApi();
    }
*/
   /* @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        classListAdapter.loaderReset();

        callApi();
    }

    private void callApi() {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_MEMBERSHIP_MULTI_GYM) {
            networkCommunicator.getScheduleList(TempStorage.getUser().getId(), page, pageSizeLimit, this, false);

        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_MEMBERSHIP_SINGLE_GYM) {
            networkCommunicator.getWishList(TempStorage.getUser().getId(), page, pageSizeLimit, this, false);
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS) {
            networkCommunicator.getSearchClassList(searchedKeywords, page, pageSizeLimit, this, false);
        }
    }*/

    @Override
    public void onTabSelection(int position) {

        onRefresh();
       /* if ((fragmentPositionInViewPager == position && isShownFirstTime) || shouldRefresh) {
            isShownFirstTime = false;
            shouldRefresh = false;

            onRefresh();
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST ||
                shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
            classListAdapter.getList().clear();
            classListAdapter.notifyDataSetChanged();
            onRefresh();

        } else {
            if (classListAdapter.getList().isEmpty()) {
                onRefresh();
            }
        }*/
    }

  /////////////////////////////////////

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
                navigatedFrom == NAVIGATION_FROM_MEMBERSHIP) {

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
                MixPanel.trackGymVisitLimitView(name);
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
                        PackageExtensionAlertDialog dialog=new PackageExtensionAlertDialog(context, NAVIGATION_FROM_MEMBERSHIP, (UserPackage) model) ;
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
             /*   mWalletCredit= user.getWalletDto();
                if(mWalletCredit!=null&&mWalletCredit.getBalance()>0){
                    mLayoutUserWallet.setVisibility(View.VISIBLE);
                    mTextViewWalletAmount.setText(LanguageUtils.numberConverter(mWalletCredit.getBalance(),2)+" "+context.getResources().getString(R.string.wallet_currency));
                }else{
                    mLayoutUserWallet.setVisibility(View.GONE);

                }*/
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

   /* @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!hasPurchased && hasVisitedGymLimits) {
            MixPanel.trackSequentialUpdate(AppConstants.Tracker.VIEW_LIMIT_NO_PURCHASE);
        }

        if (!hasVisitedGymLimits && !hasClickedCheckout) {
            MixPanel.trackSequentialUpdate(AppConstants.Tracker.NO_ACTION);
        }
    }*/

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

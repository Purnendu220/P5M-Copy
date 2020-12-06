package com.p5m.me.view.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.MyProfileAdapter;
import com.p5m.me.adapters.viewholder.ProfileHeaderTabViewHolder;
import com.p5m.me.data.SubscriptionConfigModal;
import com.p5m.me.data.UpdateSubscriptionRequest;
import com.p5m.me.data.UserPackageInfo;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.TrainerModel;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.helper.ClassListListenerHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.helper.TrainerListListenerHelper;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.EditProfileActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.Main.SettingActivity;
import com.p5m.me.view.activity.Main.VideoPlayerActivity;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.activity.custom.MyRecyclerView;
import com.p5m.me.view.custom.CustomAlertDialog;
import com.p5m.me.view.custom.OnSubscriptionUpdate;
import com.p5m.me.view.custom.PackageExtensionAlertDialog;
import com.p5m.me.view.custom.ProcessingDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyProfile extends BaseFragment implements ViewPagerFragmentSelection, AdapterCallbacks<Object>, MyRecyclerView.LoaderCallbacks, NetworkCommunicator.RequestListener, PopupMenu.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener, CustomAlertDialog.OnAlertButtonAction, OnClickBottomSheet, OnSubscriptionUpdate {
    public static Fragment createMyProfileFragment(int position) {
        Fragment tabFragment = new MyProfile();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private MyProfileAdapter myProfileAdapter;
    private int page = 0;
    private int tabPosition = ProfileHeaderTabViewHolder.TAB_1;
    private SubscriptionConfigModal mSubscriptionConfigModal;
    private UserPackageInfo userPackageInfo;

    public MyProfile() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatePackage(Events.UpdatePackage data) {
        onTabSelection(-1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUser(Events.UserUpdate userUpdate) {
        try {
            myProfileAdapter.setUser(TempStorage.getUser());
            myProfileAdapter.notifyItemChanged(0);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ClassRating(Events.ClassRating data) {
        refreshFimishedClass();
        LogUtils.debug(data + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void BannerUrlHandler(Events.BannerUrlHandler bannerTab) {
        tabPosition = bannerTab.innerTab;
        myProfileAdapter.onTabSelection(tabPosition);
        myProfileAdapter.notifyDataSetChanges();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void trainerFollowed(Events.TrainerFollowed trainerFollowed) {

        try {
            if (trainerFollowed.isFollowed) {
                myProfileAdapter.addTrainers(trainerFollowed.data);
                myProfileAdapter.notifyDataSetChanges();
            } else {
                if (myProfileAdapter.getTrainers().contains(trainerFollowed.data)) {
                    int index = myProfileAdapter.getTrainers().indexOf(trainerFollowed.data);
                    if (index != -1) {
                        TrainerModel trainerModel = myProfileAdapter.getTrainers().get(index);
                        trainerModel.setIsfollow(trainerFollowed.isFollowed);

                        myProfileAdapter.removeTrainer(index);

                        if (myProfileAdapter.getList().contains(trainerModel)) {
                            int indexTrainer = myProfileAdapter.getList().indexOf(trainerFollowed.data);

                            myProfileAdapter.remove(indexTrainer);
                            myProfileAdapter.notifyItemRemoved(indexTrainer);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscriptionUpdated(Events.SubscriptionUpdated subscriptionUpdated) {

        getUser();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tabPosition = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);

        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());
        tabPosition = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);

        swipeRefreshLayout.setOnRefreshListener(this);
        userPackageInfo = new UserPackageInfo(TempStorage.getUser());
        myProfileAdapter = new MyProfileAdapter(context, new TrainerListListenerHelper(context, activity, AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FAV_TRAINERS, this),
                new ClassListListenerHelper(context, activity, AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FINISHED, this), this);
        StickyLayoutManager layoutManager = new StickyLayoutManager(context, myProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));
        layoutManager.setAutoMeasureEnabled(false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(myProfileAdapter);
        myProfileAdapter.setUser(TempStorage.getUser());
        myProfileAdapter.notifyDataSetChanges();
        recyclerView.setHasFixedSize(true);
        LogUtils.debug(tabPosition + "sssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");

        layoutManager.setStickyHeaderListener(new StickyHeaderListener() {
            @Override
            public void headerAttached(View headerView, int adapterPosition) {
                maintainHeaderState(headerView);
            }

            @Override
            public void headerDetached(View headerView, int adapterPosition) {
                maintainHeaderState(headerView);
            }
        });

        try {
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        setToolBar();
        initSubscriptionConfig();


    }

    private void initSubscriptionConfig(){
        String SUBSCRIPTION_CONFIG = RemoteConfigure.getFirebaseRemoteConfig(context).getRemoteConfigValue(RemoteConfigConst.SUBSCRIPTION_CONFIG_VALUE);
        try{
            Gson g = new Gson();
            mSubscriptionConfigModal = g.fromJson(SUBSCRIPTION_CONFIG, new TypeToken<SubscriptionConfigModal>() {
            }.getType());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        getUser();
        networkCommunicator.getFavTrainerList(AppConstants.ApiParamValue.FOLLOW_TYPE_FOLLOWED, TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
        networkCommunicator.getFinishedClassList(TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_UNLIMITED, this, false);

    }

    private void getUser(){
        networkCommunicator.getMyUser(this, false);

    }

    boolean isLoadingFirstTime = true;

    @Override
    public void onTabSelection(int position) {
        if (isLoadingFirstTime) {
            isLoadingFirstTime = false;
            networkCommunicator.getFavTrainerList(AppConstants.ApiParamValue.FOLLOW_TYPE_FOLLOWED, TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
            networkCommunicator.getFinishedClassList(TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_UNLIMITED, this, false);
            if (RefrenceWrapper.getRefrenceWrapper(getActivity()).getMyProfileTabPosition() == ProfileHeaderTabViewHolder.TAB_2) {
                myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_2);
                RefrenceWrapper.getRefrenceWrapper(getActivity()).setMyProfileTabPosition(ProfileHeaderTabViewHolder.TAB_1);
            } else {
                myProfileAdapter.onTabSelection(tabPosition);
            }
            myProfileAdapter.clearTrainers();
            myProfileAdapter.clearClasses();
            myProfileAdapter.notifyDataSetChanges();
        } else {
            networkCommunicator.getMyUser(this, false);
        }
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

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_bar_my_profile, null);

        v.findViewById(R.id.imageViewOptions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.showPopMenu(context, view, MyProfile.this);
            }
        });

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    /**
     * To fix and maintain the sticky header tab selection state bug..
     */
    private void maintainHeaderState(View headerView) {
        ProfileHeaderTabViewHolder profileHeaderTabViewHolder;
        if (myProfileAdapter.getProfileHeaderTabViewHolder() != null) {
            profileHeaderTabViewHolder = myProfileAdapter.getProfileHeaderTabViewHolder();
        } else {
            profileHeaderTabViewHolder = new ProfileHeaderTabViewHolder(headerView);
        }

        switch (myProfileAdapter.getSelectedTab()) {
            case ProfileHeaderTabViewHolder.TAB_1:

                profileHeaderTabViewHolder.header1.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                profileHeaderTabViewHolder.header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                profileHeaderTabViewHolder.header2.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                profileHeaderTabViewHolder.header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                break;
            case ProfileHeaderTabViewHolder.TAB_2:

                profileHeaderTabViewHolder.header1.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                profileHeaderTabViewHolder.header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                profileHeaderTabViewHolder.header2.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                profileHeaderTabViewHolder.header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                break;
        }
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.header1: {
//                if (myProfileAdapter.getTrainers().isEmpty()) {
//                    networkCommunicator.getFavTrainerList(AppConstants.ApiParamValue.FOLLOW_TYPE_FOLLOWED, TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
//                }

                myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_1);
                myProfileAdapter.notifyDataSetChanges();
            }
            break;
            case R.id.header2: {
//                if (myProfileAdapter.getClasses().isEmpty()) {
//                    networkCommunicator.getFinishedClassList(TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
//                }

                myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_2);
                myProfileAdapter.notifyDataSetChanges();
            }
            break;
            case R.id.textViewMore:
            case R.id.textViewRecharge:
                // MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE);
                HomeActivity.show(context, AppConstants.Tab.TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE,true,false);
                break;
            case R.id.imageView:
                if (!myProfileAdapter.getUser().getProfileImage().isEmpty()) {
                    Helper.openImageViewer(context, activity, view, myProfileAdapter.getUser().getProfileImage(), AppConstants.ImageViewHolderType.PROFILE_IMAGE_HOLDER);
                }
                break;
            case R.id.textViewExtendPackage:
                if (myProfileAdapter.getUser().getUserPackageDetailDtoList() != null) {
                    try {
                        PackageExtensionAlertDialog dialog = new PackageExtensionAlertDialog(context, AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE, (UserPackage) model);
                        dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;

            case R.id.linearLayoutUserWallet:
                showWalletAlert();
                break;
            case R.id.textViewUpdateSubscription:{
                EditSubscriptionBottomSheet.newInstance(context,this).show(getFragmentManager(),"hffhgjh");
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
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CLASS_LIST:
                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (!classModels.isEmpty()) {
                    myProfileAdapter.clearClasses();

                    myProfileAdapter.addClasses(classModels);
                    myProfileAdapter.notifyDataSetChanges();
                } else {
                    checkListData();
                }

                break;
            case NetworkCommunicator.RequestCode.TRAINER_LIST:
                List<TrainerModel> trainerModels = ((ResponseModel<List<TrainerModel>>) response).data;

                if (!trainerModels.isEmpty()) {
                    myProfileAdapter.clearTrainers();

                    myProfileAdapter.addTrainers(trainerModels);
                    myProfileAdapter.notifyDataSetChanges();
                } else {
                    checkListData();
                }

                break;
            case NetworkCommunicator.RequestCode.CANCEL_SUBSCRIPTION:
                ToastUtils.show(getActivity(),mSubscriptionConfigModal.getCancelSubscriptionSuccess());
                networkCommunicator.getMyUser(this,false);

                break;

        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CANCEL_SUBSCRIPTION:
            case NetworkCommunicator.RequestCode.UPDATE_SUBSCRIPTION:
                ToastUtils.showFailureResponse(context, errorMessage);

                break;

        }
    }

    private void checkListData() {
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.actionSettings: {
                SettingActivity.openActivity(context);
                //                String url ="https://youtu.be/vM_WO2NJiZU";
//                String url ="https://s3-eu-west-1.amazonaws.com/p5m.prod.media/recording/192003/4678e4dd574b5aaf31b4c6954f2b239f_192003.m3u8";
//                VideoPlayerActivity.openActivity(context,url);

                return true;
            }
            case R.id.actionEditProfile: {
                EditProfileActivity.openActivity(context);
                return true;
            }
            default:
                return false;
        }
    }

    private void refreshFimishedClass() {
        networkCommunicator.getFinishedClassList(TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_UNLIMITED, this, false);

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

    @Override
    public void onClickBottomSheet(View view, Object object) {
        switch (view.getId()){
            case R.id.cancelSubscription:
                cancelSubscription();
                break;
            case R.id.updateSubscription:
                HomeActivity.show(context, AppConstants.Tab.TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE,true,true);
                break;
            case R.id.renewSubscription:
                renewSubscription(new UpdateSubscriptionRequest(AppConstants.SubscriptionAction.RENEW,userPackageInfo.userPackageGeneral.getId()));

                break;
        }
    }
    private void renewSubscription(UpdateSubscriptionRequest request){
        DialogUtils.showBasicMessage(getActivity(), mSubscriptionConfigModal.getRenewSubscriptionConfirmationTitle(), mSubscriptionConfigModal.getRenewSubscriptionConfirmation(),
                getString(R.string.yes), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        ProcessingDialog process = new ProcessingDialog(context,request,MyProfile.this);
                        process.show();
                        //networkCommunicator.updateSubscription(request,MyProfile.this);
                    }
                }, getString(R.string.no), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

    }

    private void cancelSubscription(){
        DialogUtils.showBasicMessage(getActivity(), mSubscriptionConfigModal.getCancelSubscriptionConfirmationTitle(), mSubscriptionConfigModal.getCancelSubscriptionConfirmation(),
                getString(R.string.yes), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        networkCommunicator.cancelSubscription(MyProfile.this);
                    }
                }, getString(R.string.no), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

    }

    @Override
    public void onUpdateSuccess(UpdateSubscriptionRequest request, ProcessingDialog.PaymentStatus status) {
         Helper.onSubscriptionUpdate(context,request,status);
    }
    @Override
    public void onFinishButtonClick(int type) {

    }
}

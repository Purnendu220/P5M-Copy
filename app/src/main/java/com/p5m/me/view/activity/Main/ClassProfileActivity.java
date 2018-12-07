package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ClassProfileAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.ClassRatingUserData;
import com.p5m.me.data.UserPackageInfo;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.User;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.data.request.JoinClassRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.helper.ClassListListenerHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.BookForAFriendPopup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.p5m.me.utils.AppConstants.Limit.PAGE_LIMIT_MAIN_CLASS_LIST;

public class ClassProfileActivity extends BaseActivity implements AdapterCallbacks, View.OnClickListener, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener {

    public static void open(Context context, ClassModel classModel, int navigationFrom) {
        context.startActivity(new Intent(context, ClassProfileActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstants.DataKey.CLASS_OBJECT, classModel));
    }

    public static void open(Context context, int classId, int navigationFrom) {
        context.startActivity(new Intent(context, ClassProfileActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstants.DataKey.CLASS_SESSION_ID_INT, classId));
    }

    public static Intent createIntent(Context context, int classId, int navigationFrom) {
        return new Intent(context, ClassProfileActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom)
                .putExtra(AppConstants.DataKey.CLASS_SESSION_ID_INT, classId);
    }

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.layoutButton)
    public View layoutButton;
    @BindView(R.id.textViewBook)
    public TextView textViewBook;
    @BindView(R.id.textViewBookWithFriend)
    public TextView textViewBookWithFriend;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    public View imageViewOptions;
    private ClassProfileAdapter classProfileAdapter;
    private ClassModel classModel;
    private int classSessionId;
    private int page;
    private boolean isNavigationFromSharing;
    private int navigationFrom;
    ClassRatingUserData ratingData;
    private boolean isBookWithFriendInProgress=false;
    private BookWithFriendData mBookWithFriendData;

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void classJoin(Events.ClassJoin data) {
        handleClassJoined(data.data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void packagePurchasedForClass(Events.PackagePurchasedForClass data) {
        handleClassJoined(data.data);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bookwithFriend(Events.BookWithFriend data) {
        isBookWithFriendInProgress=true;
        mBookWithFriendData=data.friendData;
        bookWithAFriend(data.friendData);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void classPurchased(Events.ClassPurchased data) {
        handleClassJoined(data.data);
    }

    private void handleClassJoined(ClassModel data) {
        try {
            classModel.setUserJoinStatus(data.isUserJoinStatus());
            try {
                classProfileAdapter.getClassModel().setAvailableSeat(data.getAvailableSeat());
                classProfileAdapter.notifyItemChanged(0);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
            Helper.setJoinStatusProfile(context, textViewBook,textViewBookWithFriend,classModel);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_profile);

        ButterKnife.bind(activity);
        GlobalBus.getBus().register(this);

        classModel = (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_OBJECT);
        classSessionId = getIntent().getIntExtra(AppConstants.DataKey.CLASS_SESSION_ID_INT, -1);
        navigationFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);

        if (classModel == null && classSessionId == -1) {
            finish();
            return;
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);

        classProfileAdapter = new ClassProfileAdapter(context, AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE, this);
        StickyLayoutManager layoutManager = new StickyLayoutManager(context, classProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(classProfileAdapter);

        if (classModel != null) {
            classSessionId = classModel.getClassSessionId();
            classProfileAdapter.setClass(classModel);
            classProfileAdapter.notifyDataSetChanged();

            Helper.setJoinStatusProfile(context, textViewBook,textViewBookWithFriend, classModel);
        } else {
            layoutButton.setVisibility(View.GONE);
        }

        onRefresh();

        try {
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        setToolBar();

//        if (classModel != null) {
//            if (classModel.isUserJoinStatus()) {
//                imageViewOptions.setVisibility(View.GONE);
//            }
//        }

        MixPanel.trackClassDetails();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        networkCommunicator.getClassDetail(classSessionId, this, false);

    }

    @OnClick(R.id.textViewBook)
    public void textViewBook() {
        isBookWithFriendInProgress=false;
        mBookWithFriendData=null;
        // Check if class is allowed for the gender..
        if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)
                && !Helper.isMalesAllowed(classModel)) {
            ToastUtils.show(context, "This is a Females only class");
            return;
        } else if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)
                && !Helper.isFemalesAllowed(classModel)) {
            ToastUtils.show(context, "This is a Males only class");
            return;
        }

        if (Helper.isSpecialClass(classModel)) {
            if (Helper.isFreeClass(classModel))
                joinClass();
            else {
                CheckoutActivity.openActivity(context, classModel,1);
            }
            return;
        }

        textViewBook.setText(context.getResources().getString(R.string.please_wait));
        textViewBook.setEnabled(false);

        networkCommunicator.getMyUser(new NetworkCommunicator.RequestListener() {
            @Override
            public void onApiSuccess(Object response, int requestCode) {
                performJoinProcess();
                Helper.setJoinStatusProfile(context, textViewBook,textViewBookWithFriend, classModel);
            }

            @Override
            public void onApiFailure(String errorMessage, int requestCode) {
                ToastUtils.show(context, errorMessage);
                Helper.setJoinStatusProfile(context, textViewBook,textViewBookWithFriend, classModel);
            }
        }, false);
    }

    @OnClick(R.id.textViewBookWithFriend)
    public void textViewBookWithFriend() {

        BookForAFriendPopup mBookForAFriendPopup = new BookForAFriendPopup(this,classModel,navigationFrom);
        try {
            mBookForAFriendPopup.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void bookWithAFriend(final BookWithFriendData data){

        // Check if class is allowed for the gender..
        if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)
                && !Helper.isMalesAllowed(classModel)) {
            ToastUtils.show(context, "This is a Females only class");
            return;
        } else if (TempStorage.getUser().getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)
                && !Helper.isFemalesAllowed(classModel)) {
            ToastUtils.show(context, "This is a Males only class");
            return;
        }
        if (data.getGender().equals(AppConstants.ApiParamValue.GENDER_MALE)
                && !Helper.isMalesAllowed(classModel)) {
            ToastUtils.show(context, "This is a Females only class");
            return;
        } else if (data.getGender().equals(AppConstants.ApiParamValue.GENDER_FEMALE)
                && !Helper.isFemalesAllowed(classModel)) {
            ToastUtils.show(context, "This is a Males only class");
            return;
        }


        if (Helper.isSpecialClass(classModel)) {
            if (Helper.isFreeClass(classModel))
                joinClassWithFriend(data);
            else {
                CheckoutActivity.openActivity(context, classModel,2,data);
            }
            return;
        }

        textViewBookWithFriend.setText(context.getResources().getString(R.string.please_wait));
        textViewBookWithFriend.setEnabled(false);

        networkCommunicator.getMyUser(new NetworkCommunicator.RequestListener() {
            @Override
            public void onApiSuccess(Object response, int requestCode) {
                performJoinProcessWithFriend(data);
                Helper.setJoinStatusProfile(context, textViewBook,textViewBookWithFriend, classModel);
            }

            @Override
            public void onApiFailure(String errorMessage, int requestCode) {
                ToastUtils.show(context, errorMessage);
                Helper.setJoinStatusProfile(context, textViewBook,textViewBookWithFriend, classModel);
            }
        }, false);
    }

    private void performJoinProcessWithFriend(BookWithFriendData dataFriend) {
        UserPackageInfo userPackageInfo = new UserPackageInfo(TempStorage.getUser());
        if (userPackageInfo.havePackages) {
            joinClassWithFriend(dataFriend);
        } else {
            // 3rt condition : have no packages..
           // MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
            MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel,mBookWithFriendData,2);

        }
    }
    private void joinClassWithFriend(BookWithFriendData data) {
        List<BookWithFriendData> friendList=new ArrayList<>();
        friendList.add(data);
        textViewBookWithFriend.setText(context.getResources().getString(R.string.please_wait));
        textViewBookWithFriend.setEnabled(false);
        networkCommunicator.joinClass(new JoinClassRequest(TempStorage.getUser().getId(), classModel.getClassSessionId(),friendList), this, false);
    }

    private void performJoinProcess() {
        boolean userHaveDropinForClass = false;
        boolean userHaveExpiredDropinForClass = false;
        boolean userHaveExpiredGeneralPackageForClass=false;


        boolean userHaveGeneralPackageForClass=false;
        ArrayList<Integer> list=new ArrayList<>();
        UserPackageInfo userPackageInfo = new UserPackageInfo(TempStorage.getUser());

        if (userPackageInfo.havePackages) {

            // 1st condition : have drop-in for class..
            if (userPackageInfo.haveDropInPackage && classModel.getGymBranchDetail() != null) {
                 userHaveDropinForClass=false;
                for (UserPackage userPackage : userPackageInfo.userPackageReady) {
                    if ((userPackage.getGymId() == classModel.getGymBranchDetail().getGymId())&&(userPackage.getExpiryDate()==null || DateUtils.canJoinClass(classModel.getClassDate(), userPackage.getExpiryDate()) >= 0)) {
                        userHaveDropinForClass=true;
                        list.add(userPackage.getGymId());
                        }
                    if ((userPackage.getGymId() == classModel.getGymBranchDetail().getGymId())&&(userPackage.getExpiryDate()!=null && DateUtils.canJoinClass(classModel.getClassDate(), userPackage.getExpiryDate()) < 0)) {
                            userHaveExpiredDropinForClass=true;
                        }
                }

            }
            // 2st condition : have class remaining in package..
            if (userPackageInfo.haveGeneralPackage) {
                if (userPackageInfo.userPackageGeneral != null) {

                    if (userPackageInfo.userPackageGeneral.getBalanceClass() == 0&&!userHaveDropinForClass) {
                        MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
                        return;
                    }

                    if (DateUtils.canJoinClass(classModel.getClassDate(), userPackageInfo.userPackageGeneral.getExpiryDate()) >= 0) {
                        userHaveGeneralPackageForClass=true;

                    }
                    if (DateUtils.canJoinClass(classModel.getClassDate(), userPackageInfo.userPackageGeneral.getExpiryDate()) < 0) {
                        userHaveExpiredGeneralPackageForClass=true;

                    }

                }
            }
            try{
                if(userHaveDropinForClass||userHaveGeneralPackageForClass){
                    joinClass();
                    return;
                }
                else {
                 if(userHaveExpiredGeneralPackageForClass){
                     DialogUtils.showBasic(context, getString(R.string.join_fail_date_expire), "Purchase", new MaterialDialog.SingleButtonCallback() {
                         @Override
                         public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                             dialog.dismiss();
                             MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
                         }
                     });
                     return;
                 }else{
                     joinClass();
                     return;
                 }

                }

            }
            catch(Exception e){
                e.printStackTrace();
                joinClass();
                return;
            }
        } else {
            // 3rt condition : have no packages..
            MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
        }
    }

    private void joinClass() {
        textViewBook.setText(context.getResources().getString(R.string.please_wait));
        textViewBook.setEnabled(false);
        networkCommunicator.joinClass(new JoinClassRequest(TempStorage.getUser().getId(), classModel.getClassSessionId()), this, false);
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

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_bar_class_profile, null);

        v.findViewById(R.id.imageViewBack).setOnClickListener(this);
        imageViewOptions = v.findViewById(R.id.imageViewOptions);
        imageViewOptions.setOnClickListener(this);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.imageViewClass:
                if (classModel.getClassMedia() != null) {
                    Helper.openImageViewer(context, activity, view, classModel.getClassMedia().getMediaUrl(),AppConstants.ImageViewHolderType.CLASS_IMAGE_HOLDER);
                }
                break;
            case R.id.layoutTrainer:
                if (model instanceof ClassModel) {
                    ClassModel data = (ClassModel) model;
                    if (data.getTrainerDetail() != null) {
                        TrainerProfileActivity.open(context, data.getTrainerDetail(), AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);
                    } else {
                        GymProfileActivity.open(context, data.getGymBranchDetail().getGymId(), AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);
                    }
                }
                break;
            case R.id.textViewLocation:
            case R.id.layoutLocation:
                if (model instanceof ClassModel) {
                    GymProfileActivity.open(context, ((ClassModel) model).getGymBranchDetail().getGymId(), AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);
                }
                break;

            case R.id.imageViewMap:
            case R.id.layoutMapClick:
                if (model instanceof ClassModel) {

                    ClassModel data = (ClassModel) model;

                    String label = "";
                    try {
                        label = data.getGymBranchDetail().getBranchName();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Helper.openMap(context, data.getGymBranchDetail().getLatitude(), data.getGymBranchDetail().getLongitude(), label);
                }
                break;
            case R.id.layoutSeeAllReview:
                if (model instanceof ClassModel) {
                    ViewClassRating.open(context, ((ClassModel) model), AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                onBackPressed();
                break;
            case R.id.imageViewOptions:
                ClassListListenerHelper.popupOptionsAdd(context, networkCommunicator, view, classModel, navigationFrom);
                break;
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.JOIN_CLASS:

                User user = ((ResponseModel<User>) response).data;

                EventBroadcastHelper.sendUserUpdate(context, user);

                classModel.setUserJoinStatus(true);
                EventBroadcastHelper.sendClassJoin(context, classModel);

                classModel.setUserJoinStatus(true);

                Helper.setJoinStatusProfile(context, textViewBook,textViewBookWithFriend, classModel);

                MixPanel.trackJoinClass(navigationFrom, classModel);
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                    DialogUtils.showBasicMessage(context,"",
                            "Successfully joined " + classModel.getTitle()
                          ,context.getResources().getString(R.string.invite_friends), new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    Helper.shareClass(context, classModel.getClassSessionId(), classModel.getTitle());
                                    finish();

                                }
                            },  context.getResources().getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });

                }
                break;

            case NetworkCommunicator.RequestCode.CLASS_DETAIL:
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(false);
                classModel = ((ResponseModel<ClassModel>) response).data;
                if(classModel.getAvailableSeat()<2){

                }else{

                }
                if (classModel != null) {
                    getCountRating();
                    classProfileAdapter.setClass(classModel);
                    classProfileAdapter.notifyDataSetChanged();
                }
                layoutButton.setVisibility(View.VISIBLE);
                Helper.setJoinStatusProfile(context, textViewBook,textViewBookWithFriend, classModel);
                if (Helper.isSpecialClass(classModel)) {
                    if (Helper.isFreeClass(classModel)){
                        textViewBookWithFriend.setVisibility(View.GONE);
                    }

                }

                break;
            case NetworkCommunicator.RequestCode.CLASS_RATING_LIST:
                 ratingData=((ResponseModel<ClassRatingUserData>) response).data;
                if(classModel!=null&&ratingData.getCount()>0){
                    classModel.setNumberOfRating(ratingData.getCount());
                    classProfileAdapter.setClass(classModel);
                    classProfileAdapter.notifyDataSetChanged();
                }

                break;

        }
    }

    @Override
    public void onApiFailure(final String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.JOIN_CLASS:
                if (errorMessage.equals("498")) {
                    if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                        DialogUtils.showBasic(context, getString(R.string.join_fail_limit_exhaust), "Purchase", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                if(isBookWithFriendInProgress&&mBookWithFriendData!=null){
                                    User errorResponse=MyPreferences.getInstance().getPaymentErrorResponse();
                                    //MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
                                    if(errorResponse!=null){
                                        MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel,mBookWithFriendData,errorResponse.getReadyPckSize());

                                    }else{
                                        ToastUtils.showLong(context, errorMessage);

                                    }

                                }else{
                                    MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);

                                }
                            }
                        });
                    }

                }
               else if(errorMessage.equals("405")){
                    if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                        DialogUtils.showBasic(context, getString(R.string.join_share_limit_exhaust), "Purchase", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                if(isBookWithFriendInProgress&&mBookWithFriendData!=null){
                                    User errorResponse=MyPreferences.getInstance().getPaymentErrorResponse();
                                    //MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
                                    if(errorResponse!=null){
                                        MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel,mBookWithFriendData,errorResponse.getReadyPckSize());

                                    }else{
                                        ToastUtils.showLong(context, errorMessage);

                                    }


                                }else{
                                    MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);

                                }
                            }
                        });
                    }

                }

                else if (errorMessage.equals("402")) {
                    if(isBookWithFriendInProgress&&mBookWithFriendData!=null){
                        final User errorResponse=MyPreferences.getInstance().getPaymentErrorResponse();
                        if(checkIfUserHaveExpiredPackage()){
                                DialogUtils.showBasic(context, getString(R.string.join_fail_date_expire), "Purchase", new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        if(errorResponse!=null){
                                            MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel,mBookWithFriendData,errorResponse.getReadyPckSize());

                                        }else{
                                            ToastUtils.showLong(context, errorMessage);

                                        }                                    }
                                });

                        }else{
                            if(errorResponse!=null){
                                MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel,mBookWithFriendData,errorResponse.getReadyPckSize());

                            }else{
                                ToastUtils.showLong(context, errorMessage);

                            }
                        }


                    }else{
                        MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);

                    }



                } else {
                    ToastUtils.showLong(context, errorMessage);
                }

                Helper.setJoinStatusProfile(context, textViewBook,textViewBookWithFriend, classModel);

                break;
            case NetworkCommunicator.RequestCode.CLASS_DETAIL:
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);

                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                    DialogUtils.showBasicMessage(context, errorMessage, "ok", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    });
                }

                break;
        }
    }

    private boolean checkIfUserHaveExpiredPackage(){
        boolean userHaveDropinForClass = false;
        boolean userHaveExpiredDropinForClass = false;
        boolean userHaveExpiredGeneralPackageForClass=false;


        boolean userHaveGeneralPackageForClass=false;
        ArrayList<Integer> list=new ArrayList<>();
        UserPackageInfo userPackageInfo = new UserPackageInfo(TempStorage.getUser());

        if (userPackageInfo.haveDropInPackage && classModel.getGymBranchDetail() != null) {
            userHaveDropinForClass=false;
            for (UserPackage userPackage : userPackageInfo.userPackageReady) {
                if ((userPackage.getGymId() == classModel.getGymBranchDetail().getGymId())&&(userPackage.getExpiryDate()==null || DateUtils.canJoinClass(classModel.getClassDate(), userPackage.getExpiryDate()) >= 0)) {
                    userHaveDropinForClass=true;
                    list.add(userPackage.getGymId());
                }
                if ((userPackage.getGymId() == classModel.getGymBranchDetail().getGymId())&&(userPackage.getExpiryDate()!=null && DateUtils.canJoinClass(classModel.getClassDate(), userPackage.getExpiryDate()) < 0)) {
                    userHaveExpiredDropinForClass=true;
                }
            }

        }
        if (userPackageInfo.haveGeneralPackage) {
            if (userPackageInfo.userPackageGeneral != null) {
                if (DateUtils.canJoinClass(classModel.getClassDate(), userPackageInfo.userPackageGeneral.getExpiryDate()) >= 0) {
                    userHaveGeneralPackageForClass=true;

                }
                if (userPackageInfo.userPackageGeneral.getBalanceClass() > 0 && DateUtils.canJoinClass(classModel.getClassDate(), userPackageInfo.userPackageGeneral.getExpiryDate()) < 0) {
                    userHaveExpiredGeneralPackageForClass=true;

                }

            }
        }
        return userHaveExpiredGeneralPackageForClass;
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void getCountRating(){
        networkCommunicator.getClassRatingList(classModel.getClassId(),page,PAGE_LIMIT_MAIN_CLASS_LIST,this,false);

    }

}

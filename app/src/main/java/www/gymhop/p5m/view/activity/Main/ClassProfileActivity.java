package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.ClassProfileAdapter;
import www.gymhop.p5m.data.UserPackageInfo;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.User;
import www.gymhop.p5m.data.main.UserPackage;
import www.gymhop.p5m.data.request.JoinClassRequest;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.eventbus.Events;
import www.gymhop.p5m.eventbus.GlobalBus;
import www.gymhop.p5m.helper.ClassListListenerHelper;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.DateUtils;
import www.gymhop.p5m.utils.DialogUtils;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.utils.ToastUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class ClassProfileActivity extends BaseActivity implements AdapterCallbacks, View.OnClickListener, NetworkCommunicator.RequestListener {

    public static void open(Context context, ClassModel classModel) {
        context.startActivity(new Intent(context, ClassProfileActivity.class)
                .putExtra(AppConstants.DataKey.CLASS_OBJECT, classModel));
    }

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.textViewBook)
    public TextView textViewBook;

    private ClassProfileAdapter classProfileAdapter;
    private ClassModel classModel;
    private int page;

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
    public void classPurchased(Events.ClassPurchased data) {
        handleClassJoined(data.data);
    }

    private void handleClassJoined(ClassModel data) {
        try {
            classModel.setUserJoinStatus(data.isUserJoinStatus());
            Helper.setJoinStatusProfile(context, textViewBook, classModel);

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

        if (classModel == null) {
            finish();
            return;
        }

        classProfileAdapter = new ClassProfileAdapter(context, AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE, this);
        StickyLayoutManager layoutManager = new StickyLayoutManager(context, classProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));
        layoutManager.setAutoMeasureEnabled(false);


        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(classProfileAdapter);
        classProfileAdapter.setClass(classModel);
        recyclerView.setHasFixedSize(true);

        try {
            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        Helper.setJoinStatusProfile(context, textViewBook, classModel);

        setToolBar();
    }

    @OnClick(R.id.textViewBook)
    public void textViewBook() {

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
                CheckoutActivity.openActivity(context, classModel);
            }
            return;
        }

        UserPackageInfo userPackageInfo = new UserPackageInfo(TempStorage.getUser());

        if (userPackageInfo.havePackages) {

            // 1st condition : have drop-in for class..
            if (userPackageInfo.haveDropInPackage && classModel.getGymBranchDetail() != null) {
                for (UserPackage userPackage : userPackageInfo.userPackageReady) {
                    if (userPackage.getGymId() == classModel.getGymBranchDetail().getGymId()) {
                        joinClass();
                        return;
                    }
                }
            }

            // 2st condition : have class remaining in package..
            if (userPackageInfo.haveGeneralPackage) {
                if (userPackageInfo.userPackageGeneral != null) {

                    if (userPackageInfo.userPackageGeneral.getBalanceClass() == 0) {
                        MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
                        return;
                    }

                    if (DateUtils.canJoinClass(classModel.getClassDate(), userPackageInfo.userPackageGeneral.getExpiryDate()) >= 0) {
                        joinClass();
                        return;
                    }
                }
            }
            MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);

        } else {
            // 3rt condition : have no packages..
            MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
        }
    }

    private void joinClass() {
        networkCommunicator.joinClass(new JoinClassRequest(TempStorage.getUser().getId(), classModel.getClassSessionId()), this, false);
        textViewBook.setText(context.getResources().getString(R.string.please_wait));
        textViewBook.setEnabled(false);
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
        v.findViewById(R.id.imageViewOptions).setOnClickListener(this);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.imageViewClass:
                if (classModel.getClassMedia() != null) {
                    Helper.openImageViewer(context, classModel.getClassMedia().getMediaUrl());
                }
                break;
            case R.id.layoutTrainer:
                if (model instanceof ClassModel) {
                    ClassModel data = (ClassModel) model;
                    if (data.getTrainerDetail() != null) {
                        TrainerProfileActivity.open(context, data.getTrainerDetail());
                    } else {
                        GymProfileActivity.open(context, data.getGymBranchDetail().getGymId());
                    }
                }
                break;
            case R.id.textViewLocation:
            case R.id.layoutLocation:
                if (model instanceof ClassModel) {
                    GymProfileActivity.open(context, ((ClassModel) model).getGymBranchDetail().getGymId());
                }
                break;
            case R.id.layoutMap:
            case R.id.imageViewMap:
                if (model instanceof ClassModel) {
                    ClassModel data = (ClassModel) model;
                    Helper.openMap(context, data.getGymBranchDetail().getLatitude(), data.getGymBranchDetail().getLongitude());
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
                finish();
                break;
            case R.id.imageViewOptions:
                ClassListListenerHelper.dialogOptionsAdd(context, networkCommunicator, view, classModel);
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

                classModel.setAvailableSeat(classModel.getAvailableSeat() - 1);
                classModel.setUserJoinStatus(true);

                Helper.setJoinStatusProfile(context, textViewBook, classModel);

                DialogUtils.showBasicMessage(context, "You have successfully joined \"" + classModel.getTitle() + "\" class",
                        "OK", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.JOIN_CLASS:

                if (errorMessage.equals("498")) {
                    DialogUtils.showBasic(context, getString(R.string.join_fail_date_expire), "Purchase", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
                        }
                    });

                } else if (errorMessage.equals("402")) {
                    DialogUtils.showBasic(context, getString(R.string.join_fail_limit_exhaust), "Purchase", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
                        }
                    });
                } else {
                    ToastUtils.showLong(context, errorMessage);
                }

                Helper.setJoinStatusProfile(context, textViewBook, classModel);

                break;
        }
    }
}

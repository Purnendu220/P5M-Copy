package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.GymProfileAdapter;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.GymDetailModel;
import www.gymhop.p5m.eventbus.Events;
import www.gymhop.p5m.eventbus.GlobalBus;
import www.gymhop.p5m.helper.ClassListListenerHelper;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.utils.ToastUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class GymProfileActivity extends BaseActivity implements AdapterCallbacks, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener {

    public static void open(Context context, int gymId) {
        context.startActivity(new Intent(context, GymProfileActivity.class)
                .putExtra(AppConstants.DataKey.GYM_ID_INT, gymId));
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerViewProfile)
    public RecyclerView recyclerViewProfile;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private GymProfileAdapter gymProfileAdapter;
    private int gymId;
    private GymDetailModel gymDetailModel;

    private int page;
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_INNER_CLASS_LIST;

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
            int index = gymProfileAdapter.getList().indexOf(data);
            if (index != -1) {
                Object obj = gymProfileAdapter.getList().get(index);
                if (obj instanceof ClassModel) {
                    ClassModel classModel = (ClassModel) obj;
                    Helper.setClassJoinEventData(classModel, data);

                    gymProfileAdapter.notifyItemChanged(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_gym_profile);

        ButterKnife.bind(activity);
        GlobalBus.getBus().register(this);

        gymId = getIntent().getIntExtra(AppConstants.DataKey.GYM_ID_INT, -1);

        if (gymId == -1) {
            finish();
            return;
        }

//        gymDetailModel = new GymDetailModel(gymId);

        swipeRefreshLayout.setOnRefreshListener(this);

        gymProfileAdapter = new GymProfileAdapter(context, AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE, true, this,
                new ClassListListenerHelper(context, activity, AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE, this));
        recyclerViewProfile.setAdapter(gymProfileAdapter);

        if (gymDetailModel != null) {
            gymProfileAdapter.setGymDetailModel(gymDetailModel);
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }

        StickyLayoutManager layoutManager = new StickyLayoutManager(context, gymProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));

        recyclerViewProfile.setLayoutManager(layoutManager);
        layoutManager.setStickyHeaderListener(new StickyHeaderListener() {
            @Override
            public void headerAttached(View headerView, int adapterPosition) {
                LogUtils.debug("Listener Attached with position: " + adapterPosition);
            }

            @Override
            public void headerDetached(View headerView, int adapterPosition) {
                LogUtils.debug("Listener Detached with position: " + adapterPosition);
            }
        });

        try {
            ((SimpleItemAnimator) recyclerViewProfile.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        onRefresh();
        setToolBar();
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
                finish();
            }
        });

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.gym_profile));

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.imageViewProfile:
                if (model != null && model instanceof GymDetailModel) {
                    GymDetailModel data = (GymDetailModel) model;
                    if (data != null && !data.getProfileImage().isEmpty()) {
                        Helper.openImageViewer(context, activity, view, gymDetailModel.getProfileImage());
                    }
                }
                break;
            case R.id.textViewTrainers:
                if (model != null && model instanceof GymDetailModel) {
                    GymDetailModel data = (GymDetailModel) model;
                    if (data.getNumberOfTrainer() > 0) {
                        TrainerListActivity.open(context, data.getId());
                    }
                }
                break;
            case R.id.layoutMap:
            case R.id.textViewMore:
                LocationListMapActivity.openActivity(context, gymProfileAdapter.getGymDetailModel().getGymBranchResponseList());
                break;
        }
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
    }

    @Override
    public void onShowLastItem() {
        page++;
        callApiClasses();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        gymProfileAdapter.loaderReset();

        callApiGym();
    }


    private void callApiClasses() {
        networkCommunicator.getUpcomingClasses(TempStorage.getUser().getId(), gymId, 0, page, pageSizeLimit, this, false);
    }

    private void callApiGym() {
        networkCommunicator.getGym(gymId, this, false);
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.UPCOMING_CLASSES:
                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (page == 0) {
                    gymProfileAdapter.clearAllClasses();
                }

                if (!classModels.isEmpty()) {
                    gymProfileAdapter.addAllClass(classModels);

                    if (classModels.size() < pageSizeLimit) {
                        gymProfileAdapter.loaderDone();
                    }
                    gymProfileAdapter.notifyDataSetChanges();
                } else {
                    gymProfileAdapter.loaderDone();
                    gymProfileAdapter.addAllClass(classModels);

                    gymProfileAdapter.notifyDataSetChanges();
                }
                break;

            case NetworkCommunicator.RequestCode.GYM:

                callApiClasses();
                swipeRefreshLayout.setRefreshing(false);

                gymDetailModel = ((ResponseModel<GymDetailModel>) response).data;

                gymProfileAdapter.setGymDetailModel(gymDetailModel);
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GYM:
                gymProfileAdapter.notifyDataSetChanges();

                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLong(context, errorMessage);
                break;
        }
    }
}

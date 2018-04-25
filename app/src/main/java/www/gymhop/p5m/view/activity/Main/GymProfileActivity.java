package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.GymProfileAdapter;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.data.temp.GymDetailModel;
import www.gymhop.p5m.helper.ClassMiniListListenerHelper;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class GymProfileActivity extends BaseActivity implements AdapterCallbacks, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener {

    public static void open(Context context, int gymId) {
        context.startActivity(new Intent(context, GymProfileActivity.class)
                .putExtra(AppConstants.DataKey.GYM_ID_INT, gymId));
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerViewTrainerProfile)
    public RecyclerView recyclerViewTrainerProfile;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private GymProfileAdapter gymProfileAdapter;
    private TrainerModel trainerModel;
    private int gymId;
    private GymDetailModel gymDetailModel;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile);

        ButterKnife.bind(activity);

//        trainerModel = (TrainerModel) getIntent().getSerializableExtra(AppConstants.DataKey.GYM_OBJECT);
        gymId = getIntent().getIntExtra(AppConstants.DataKey.GYM_ID_INT, -1);

        if (trainerModel == null && gymId == -1) {
            finish();
            return;
        }

//        gymDetailModel = new GymDetailModel();

        swipeRefreshLayout.setOnRefreshListener(this);

        gymProfileAdapter = new GymProfileAdapter(context, AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE, this,
                new ClassMiniListListenerHelper(context, activity));
        recyclerViewTrainerProfile.setAdapter(gymProfileAdapter);

        if (gymDetailModel != null) {
            gymProfileAdapter.setGymDetailModel(gymDetailModel);
            gymProfileAdapter.notifyDataSetChanges();
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }

        networkCommunicator.getUpcomingClasses(TempStorage.getUser().getId(), gymId, 0, page, AppConstants.Limit.PAGE_LIMIT_INNER_CLASS_LIST, this, false);
        networkCommunicator.getGym(gymId, this, false);

        StickyLayoutManager layoutManager = new StickyLayoutManager(context, gymProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));

        recyclerViewTrainerProfile.setLayoutManager(layoutManager);
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

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.gym));

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.imageViewProfile:
                if (gymDetailModel != null && !gymDetailModel.getProfileImage().isEmpty()) {
                    Helper.openImageViewer(context, gymDetailModel.getProfileImage());
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
            case NetworkCommunicator.RequestCode.UPCOMING_CLASSES:
                swipeRefreshLayout.setRefreshing(false);
                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (!classModels.isEmpty()) {
                    gymProfileAdapter.addAllClass(classModels);
                    gymProfileAdapter.notifyDataSetChanges();
                }
                break;

            case NetworkCommunicator.RequestCode.GYM:
                swipeRefreshLayout.setRefreshing(false);

                GymDetailModel gymDetailModel = ((ResponseModel<GymDetailModel>) response).data;

                gymProfileAdapter.setGymDetailModel(gymDetailModel);
                gymProfileAdapter.notifyDataSetChanges();
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

    }

    @Override
    public void onRefresh() {

    }
}

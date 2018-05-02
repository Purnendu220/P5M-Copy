package www.gymhop.p5m.view.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.MyProfileAdapter;
import www.gymhop.p5m.adapters.viewholder.ProfileHeaderTabViewHolder;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.eventbus.Events;
import www.gymhop.p5m.eventbus.GlobalBus;
import www.gymhop.p5m.helper.ClassListListenerHelper;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.helper.TrainerListListenerHelper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.Main.EditProfileActivity;
import www.gymhop.p5m.view.activity.Main.MemberShip;
import www.gymhop.p5m.view.activity.Main.SettingActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;
import www.gymhop.p5m.view.activity.custom.MyRecyclerView;

public class MyProfile extends BaseFragment implements ViewPagerFragmentSelection, AdapterCallbacks<Object>, MyRecyclerView.LoaderCallbacks, NetworkCommunicator.RequestListener, PopupMenu.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private MyProfileAdapter myProfileAdapter;
    private int page;

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
    public void trainerFollowed(Events.TrainerFollowed trainerFollowed) {

        try {
            if (trainerFollowed.isFollowed) {
                myProfileAdapter.addTrainers(trainerFollowed.data);
                myProfileAdapter.notifyDataSetChanges();
            } else {
                if (myProfileAdapter.getTrainers().contains(trainerFollowed.data)) {
                    int index = myProfileAdapter.getTrainers().indexOf(trainerFollowed.data);
                    if (index != -1) {
                        TrainerModel trainerModel = (TrainerModel) myProfileAdapter.getTrainers().get(index);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        swipeRefreshLayout.setOnRefreshListener(this);

        myProfileAdapter = new MyProfileAdapter(context, new TrainerListListenerHelper(context, activity, this),
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
    }

    @Override
    public void onRefresh() {
        networkCommunicator.getMyUser(this, false);
        networkCommunicator.getFavTrainerList(AppConstants.ApiParamValue.FOLLOW_TYPE_FOLLOWED, TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
        networkCommunicator.getFinishedClassList(TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
    }

    boolean isLoadingFirstTime = true;

    @Override
    public void onTabSelection(int position) {
        if (isLoadingFirstTime) {
            isLoadingFirstTime = false;
            networkCommunicator.getFavTrainerList(AppConstants.ApiParamValue.FOLLOW_TYPE_FOLLOWED, TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
            networkCommunicator.getFinishedClassList(TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
            
            myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_1);
            myProfileAdapter.clearTrainers();
            myProfileAdapter.clearClasses();
            myProfileAdapter.notifyDataSetChanges();
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
                MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE);
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
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void checkListData() {
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.actionSettings: {
                SettingActivity.openActivity(context);
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

}

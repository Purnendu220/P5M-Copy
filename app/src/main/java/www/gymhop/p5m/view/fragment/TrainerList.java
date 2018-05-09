package www.gymhop.p5m.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.TrainerListAdapter;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.eventbus.Events;
import www.gymhop.p5m.eventbus.GlobalBus;
import www.gymhop.p5m.helper.TrainerListListenerHelper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.custom.MyRecyclerView;

public class TrainerList extends BaseFragment implements ViewPagerFragmentSelection, AdapterCallbacks<TrainerModel>, MyRecyclerView.LoaderCallbacks, SwipeRefreshLayout.OnRefreshListener, NetworkCommunicator.RequestListener {

    public static Fragment createFragment(int activityId, int position, int shownIn) {
        Fragment tabFragment = new TrainerList();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        bundle.putInt(AppConstants.DataKey.TAB_ACTIVITY_ID_INT, activityId);
        bundle.putInt(AppConstants.DataKey.TAB_SHOWN_IN_INT, shownIn);
        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    public static Fragment createFragment(int gymId, int shownIn) {
        Fragment tabFragment = new TrainerList();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.GYM_ID_INT, gymId);
        bundle.putInt(AppConstants.DataKey.TAB_SHOWN_IN_INT, shownIn);
        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    @BindView(R.id.recyclerViewClass)
    public RecyclerView recyclerViewTrainers;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layoutNoData)
    public View layoutNoData;
    @BindView(R.id.imageViewEmptyLayoutImage)
    public ImageView imageViewEmptyLayoutImage;
    @BindView(R.id.textViewEmptyLayoutText)
    public TextView textViewEmptyLayoutText;

    private TrainerListAdapter trainerListAdapter;

    private int fragmentPositionInViewPager;
    private boolean isShownFirstTime = true;
    private int page;
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_MAIN_TRAINER_LIST;
    private int activityId;
    private int gymId;
    private int shownInScreen;

    public TrainerList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainer_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        fragmentPositionInViewPager = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);
        activityId = getArguments().getInt(AppConstants.DataKey.TAB_ACTIVITY_ID_INT);
        shownInScreen = getArguments().getInt(AppConstants.DataKey.TAB_SHOWN_IN_INT);
        gymId = getArguments().getInt(AppConstants.DataKey.GYM_ID_INT);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerViewTrainers.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewTrainers.setHasFixedSize(false);

        try {
            ((SimpleItemAnimator) recyclerViewTrainers.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        trainerListAdapter = new TrainerListAdapter(context, shownInScreen, true, new TrainerListListenerHelper(context, activity, this));
        recyclerViewTrainers.setAdapter(trainerListAdapter);

        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE_TRAINERS) {
            onRefresh();
        }
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
    public void trainerFollowed(Events.TrainerFollowed trainerFollowed) {
        try {
            if (trainerListAdapter.getList().contains(trainerFollowed.data)) {
                int index = trainerListAdapter.getList().indexOf(trainerFollowed.data);
                if (index != -1) {
                    TrainerModel trainerModel = (TrainerModel) trainerListAdapter.getList().get(index);
                    trainerModel.setIsfollow(trainerFollowed.isFollowed);

                    if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FAV_TRAINERS) {
                        trainerListAdapter.remove(index);
                        trainerListAdapter.notifyItemRemoved(index);
                    } else {
                        trainerListAdapter.notifyItemChanged(index);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, TrainerModel model, int position) {
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, TrainerModel model, int position) {
    }

    @Override
    public void onShowLastItem() {
        page++;
        callApiTrainers();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        trainerListAdapter.loaderReset();
        callApiTrainers();
    }

    private void callApiTrainers() {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE_TRAINERS) {
            networkCommunicator.getGymTrainerList(gymId, page, pageSizeLimit, this, false);
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_HOME_TRAINERS) {
            networkCommunicator.getTrainerList(activityId, page, pageSizeLimit, this, false);
        }
    }

    @Override
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {

    }

    @Override
    public void onTabSelection(int position) {
        if (fragmentPositionInViewPager == position && isShownFirstTime) {
            isShownFirstTime = false;

            onRefresh();
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.TRAINER_LIST:
                swipeRefreshLayout.setRefreshing(false);
                List<TrainerModel> classModels = ((ResponseModel<List<TrainerModel>>) response).data;

                if (page == 0) {
                    trainerListAdapter.clearAll();
                }

                if (!classModels.isEmpty()) {
                    trainerListAdapter.addAll(classModels);

                    if (classModels.size() < pageSizeLimit) {
                        trainerListAdapter.loaderDone();
                    }
                    trainerListAdapter.notifyDataSetChanged();
                } else {
                    trainerListAdapter.loaderDone();
                }

                checkListData();
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.TRAINER_LIST:

                swipeRefreshLayout.setRefreshing(false);
                checkListData();

                break;
        }
    }

    private void checkListData() {
        if (trainerListAdapter.getList().isEmpty()) {
            layoutNoData.setVisibility(View.VISIBLE);

            textViewEmptyLayoutText.setText(R.string.no_data_trainer_list_main);
            imageViewEmptyLayoutImage.setImageResource(R.drawable.stub_trainer);
        } else {
            layoutNoData.setVisibility(View.GONE);
        }
    }

}

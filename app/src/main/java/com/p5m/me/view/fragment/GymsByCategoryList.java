package com.p5m.me.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.GymByCategoryListAdapter;
import com.p5m.me.data.main.GymModel;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.helper.TrainerListListenerHelper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.custom.MyRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GymsByCategoryList extends BaseFragment implements ViewPagerFragmentSelection, AdapterCallbacks<GymModel>, MyRecyclerView.LoaderCallbacks, SwipeRefreshLayout.OnRefreshListener, NetworkCommunicator.RequestListener {

    public static Fragment createFragment(int activityId, int position, int shownIn) {
        Fragment tabFragment = new GymsByCategoryList();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        bundle.putInt(AppConstants.DataKey.TAB_ACTIVITY_ID_INT, activityId);
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

    private GymByCategoryListAdapter gymByCategoryListAdapter;

    private int fragmentPositionInViewPager;
    private boolean isShownFirstTime = true;
    private int page;
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_MAIN_TRAINER_LIST;
    private int activityId;
    private int shownInScreen;

    public GymsByCategoryList() {
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

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerViewTrainers.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewTrainers.setHasFixedSize(false);

        try {
            ((SimpleItemAnimator) recyclerViewTrainers.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        gymByCategoryListAdapter = new GymByCategoryListAdapter(context, shownInScreen, true, new TrainerListListenerHelper(context, activity, AppConstants.AppNavigation.SHOWN_IN_GYM, this));
        recyclerViewTrainers.setAdapter(gymByCategoryListAdapter);


        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_HOME_GYM) {
            onRefresh();
        }
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

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void trainerFollowed(Events.TrainerFollowed trainerFollowed) {
        try {
            if (GymByCategoryListAdapter.getList().contains(trainerFollowed.data)) {
                int index = GymByCategoryListAdapter.getList().indexOf(trainerFollowed.data);
                if (index != -1) {
                    GymModel GymModel = (GymModel) GymByCategoryListAdapter.getList().get(index);
                    GymModel.setIsfollow(trainerFollowed.isFollowed);

                    if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FAV_TRAINERS) {
                        GymByCategoryListAdapter.remove(index);
                        GymByCategoryListAdapter.notifyItemRemoved(index);
                    } else {
                        GymByCategoryListAdapter.notifyItemChanged(index);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }*/

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, GymModel model, int position) {
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, GymModel model, int position) {
    }

    @Override
    public void onShowLastItem() {
        page++;
        callApiGym();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        gymByCategoryListAdapter.loaderReset();
        callApiGym();
    }

    private void callApiGym() {
        networkCommunicator.getGymList(activityId, page, pageSizeLimit, this, false);

    }

    @Override
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {

    }

    @Override
    public void onTabSelection(int position) {
        if (fragmentPositionInViewPager == position && isShownFirstTime) {
            isShownFirstTime = false;

            onRefresh();
        } else {
            if (gymByCategoryListAdapter.getList().isEmpty()) {
                onRefresh();
            }
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GYM_LIST_BY_CATEGORY:
                swipeRefreshLayout.setRefreshing(false);
                List<GymModel> classModels = ((ResponseModel<List<GymModel>>) response).data;

                if (page == 0) {
                    gymByCategoryListAdapter.clearAll();

                }

                if (!classModels.isEmpty()) {
                    gymByCategoryListAdapter.addAll(classModels);

                    if (classModels.size() < pageSizeLimit) {
                        gymByCategoryListAdapter.loaderDone();
                    }
                    gymByCategoryListAdapter.notifyDataSetChanged();
                } else {
                    gymByCategoryListAdapter.loaderDone();
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
                ToastUtils.showLong(context, errorMessage);

                break;
        }
    }

    private void checkListData() {
        if (gymByCategoryListAdapter.getList().isEmpty()) {
            layoutNoData.setVisibility(View.VISIBLE);
            textViewEmptyLayoutText.setText(R.string.no_data_search_gym_list);
            imageViewEmptyLayoutImage.setImageResource(R.drawable.stub_trainer);

        } else {
            layoutNoData.setVisibility(View.GONE);
        }
    }

}

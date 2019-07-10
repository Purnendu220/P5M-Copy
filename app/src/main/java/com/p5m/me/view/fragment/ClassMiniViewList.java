package com.p5m.me.view.fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ClassMiniViewAdapter;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.helper.ClassListListenerHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.custom.MyRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassMiniViewList extends BaseFragment implements ViewPagerFragmentSelection, MyRecyclerView.LoaderCallbacks, SwipeRefreshLayout.OnRefreshListener, AdapterCallbacks<ClassModel>, NetworkCommunicator.RequestListener {

    public static Fragment createFragment(int position, int shownIn) {
        Fragment tabFragment = new ClassMiniViewList();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        bundle.putInt(AppConstants.DataKey.TAB_SHOWN_IN_INT, shownIn);
        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    public static Fragment createFragment(String queryString, int position, int shownIn) {
        Fragment tabFragment = new ClassMiniViewList();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        bundle.putString(AppConstants.DataKey.QUERY_STRING, queryString);
        bundle.putInt(AppConstants.DataKey.TAB_SHOWN_IN_INT, shownIn);
        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    @BindView(R.id.recyclerViewClass)
    public RecyclerView recyclerViewClass;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layoutNoData)
    public View layoutNoData;
    @BindView(R.id.imageViewEmptyLayoutImage)
    public ImageView imageViewEmptyLayoutImage;
    @BindView(R.id.textViewEmptyLayoutText)
    public TextView textViewEmptyLayoutText;

    private ClassMiniViewAdapter classListAdapter;

    private int fragmentPositionInViewPager;
    private boolean isShownFirstTime = true;
    private boolean shouldRefresh = false;
    private int page;
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_INNER_CLASS_LIST;
    private String searchedKeywords;

    private int shownInScreen;

    public ClassMiniViewList() {
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
    public void wishAdded(Events.WishAdded wishAdded) {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
            shouldRefresh = true;
            try {
                classListAdapter.addClassTop(wishAdded.data);
                classListAdapter.notifyItemInserted(0);

                checkListData();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void wishRemoved(Events.WishRemoved wishRemoved) {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
            try {
                int index = classListAdapter.getList().indexOf(wishRemoved.data);

                if (index != -1) {
                    classListAdapter.remove(index);
                    classListAdapter.notifyItemRemoved(index);
                }

                checkListData();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUpcomingClasses(Events.UpdateUpcomingClasses data) {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING ||
                shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
            classListAdapter.getList().clear();
            classListAdapter.notifyDataSetChanged();
            onRefresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void classJoin(Events.ClassJoin data) {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
            shouldRefresh = true;
            onTabSelection(fragmentPositionInViewPager);
            classListAdapter.getList().clear();
            classListAdapter.notifyDataSetChanged();
        } else {
            handleClassJoined(data.data);
            checkListData();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void waitlistJoin(Events.WaitlistJoin data) {
        handleWaitlistJoined(data.data);

    }

    private void handleWaitlistJoined(ClassModel data) {
        try {
            int index = classListAdapter.getList().indexOf(data);
            if (index != -1) {
                Object obj = classListAdapter.getList().get(index);
                if (obj instanceof ClassModel) {
                    ClassModel classModel = (ClassModel) obj;
                    Helper.setWaitlistAddData(classModel, data);

                    classListAdapter.notifyItemChanged(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void waitlistItemRemoved(Events.WaitlistItemRemoved data) {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
            shouldRefresh = true;
            onTabSelection(fragmentPositionInViewPager);
            classListAdapter.getList().clear();
            classListAdapter.notifyDataSetChanged();
        }
        else {
            handleWaitlistItemRemoved(data.data);
        }
    }
    private void handleWaitlistItemRemoved(ClassModel data) {
        try {
            int index = classListAdapter.getList().indexOf(data);

            if (index == -1) {
                Object obj = classListAdapter.getList().get(1);

                if (obj instanceof ClassModel) {
                    ClassModel classModel = (ClassModel) obj;
                    Helper.setWaitlistRemoveData(classModel, data);

                    classListAdapter.notifyItemChanged(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
            try {
                int index = classListAdapter.getList().indexOf(data);

                if (index != -1) {
                    classListAdapter.remove(index);
                    classListAdapter.notifyItemRemoved(index);
                    Object obj = classListAdapter.getList().get(index);

                    if (obj instanceof ClassModel) {
                        ClassModel classModel = (ClassModel) obj;
                        Helper.wishlistItemRemoved(classModel, data);

                        classListAdapter.notifyItemChanged(index);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPackagePurchased(Events.PackagePurchased packagePurchased) {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING)
            shouldRefresh = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void packagePurchasedForClass(Events.PackagePurchasedForClass data) {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
            shouldRefresh = true;
            onTabSelection(fragmentPositionInViewPager);
        }

        handleClassJoined(data.data);
        checkListData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void classPurchased(Events.ClassPurchased data) {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
            shouldRefresh = true;
            onTabSelection(fragmentPositionInViewPager);
        }

        handleClassJoined(data.data);
        checkListData();

    }

    private void handleClassJoined(ClassModel data) {
        try {
            int index = classListAdapter.getList().indexOf(data);

            if (index != -1) {
                Object obj = classListAdapter.getList().get(index);

                if (obj instanceof ClassModel) {
                    ClassModel classModel = (ClassModel) obj;
                    Helper.setClassJoinEventData(classModel, data);

                    classListAdapter.notifyItemChanged(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
            try {
                int index = classListAdapter.getList().indexOf(data);

                if (index != -1) {
                    classListAdapter.remove(index);
                    classListAdapter.notifyItemRemoved(index);
                    Object obj = classListAdapter.getList().get(index);

                    if (obj instanceof ClassModel) {
                        ClassModel classModel = (ClassModel) obj;
                        Helper.wishlistItemRemoved(classModel, data);

                        classListAdapter.notifyItemChanged(index);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_mini_view_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        fragmentPositionInViewPager = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);
        shownInScreen = getArguments().getInt(AppConstants.DataKey.TAB_SHOWN_IN_INT);
        searchedKeywords = getArguments().getString(AppConstants.DataKey.QUERY_STRING, "");

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerViewClass.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewClass.setHasFixedSize(true);

        try {
            ((SimpleItemAnimator) recyclerViewClass.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        classListAdapter = new ClassMiniViewAdapter(context, shownInScreen, true, new ClassListListenerHelper(context, activity, shownInScreen, this));
        recyclerViewClass.setAdapter(classListAdapter);
    }

    @Override
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {
        LogUtils.debug("onShowLoader " + position);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, ClassModel model, int position) {
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, ClassModel model, int position) {
    }

    @Override
    public void onShowLastItem() {
        page++;
        callApi();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        classListAdapter.loaderReset();

        callApi();
    }

    private void callApi() {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
            networkCommunicator.getScheduleList(TempStorage.getUser().getId(), page, pageSizeLimit, this, false);

        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
            networkCommunicator.getWishList(TempStorage.getUser().getId(), page, pageSizeLimit, this, false);
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS) {
            networkCommunicator.getSearchClassList(searchedKeywords, page, pageSizeLimit, this, false);
        }
    }

    @Override
    public void onTabSelection(int position) {
        if ((fragmentPositionInViewPager == position && isShownFirstTime) || shouldRefresh) {
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
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CLASS_LIST:
                swipeRefreshLayout.setRefreshing(false);
                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (page == 0) {
                    classListAdapter.clearAll();
                }

                if (!classModels.isEmpty()) {

                    if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
                        for (ClassModel classModel : classModels) {
                            classModel.setUserJoinStatus(true);
                        }
                    }

                    classListAdapter.addAllClass(classModels);

                    if (classModels.size() < pageSizeLimit) {
                        classListAdapter.loaderDone();
                    }

                } else {
                    classListAdapter.loaderDone();
                }

                checkListData();

                classListAdapter.notifyDataSetChanged();
                // filterList(classModels);


                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CLASS_LIST:

                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLong(context, errorMessage);

                checkListData();

                break;
        }
    }

    private void checkListData() {
        if (classListAdapter.isEmpty()) {
            layoutNoData.setVisibility(View.VISIBLE);

            if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
                textViewEmptyLayoutText.setText(R.string.no_data_schedule_wishist_list);
                imageViewEmptyLayoutImage.setImageResource(R.drawable.stub_class);
            } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
                textViewEmptyLayoutText.setText(R.string.no_data_schedule_upcoming_list);
                imageViewEmptyLayoutImage.setImageResource(R.drawable.stub_class);
            } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS) {
                textViewEmptyLayoutText.setText(R.string.no_data_search_class_list);
                imageViewEmptyLayoutImage.setImageResource(R.drawable.stub_class);
            } else {
                textViewEmptyLayoutText.setText("");
                imageViewEmptyLayoutImage.setImageResource(R.drawable.stub_class);
            }

        } else {
            layoutNoData.setVisibility(View.GONE);
        }
    }



}

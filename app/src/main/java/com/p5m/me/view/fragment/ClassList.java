package com.p5m.me.view.fragment;


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

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ClassListAdapter;
import com.p5m.me.adapters.RecommendedClassAdapter;
import com.p5m.me.data.CityLocality;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Filter;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.GymDataModel;
import com.p5m.me.data.request.ClassListRequest;
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
import com.p5m.me.view.activity.Main.ClassProfileActivity;
import com.p5m.me.view.activity.Main.GymProfileActivity;
import com.p5m.me.view.activity.Main.TrainerProfileActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassList extends BaseFragment implements ViewPagerFragmentSelection, AdapterCallbacks<ClassModel>, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener {

    public static Fragment createFragment(String date, int position, int shownIn) {
        Fragment tabFragment = new ClassList();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.DataKey.CLASS_DATE_STRING, date);
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        bundle.putInt(AppConstants.DataKey.TAB_SHOWN_IN_INT, shownIn);
        tabFragment.setArguments(bundle);

        return tabFragment;
    }

    @BindView(R.id.recyclerViewClass)
    public RecyclerView recyclerViewClass;
    @BindView(R.id.recyclerViewRecommendedClass)
    public RecyclerView recyclerViewRecommendedClass;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layoutNoData)
    public View layoutNoData;
    @BindView(R.id.imageViewEmptyLayoutImage)
    public ImageView imageViewEmptyLayoutImage;
    @BindView(R.id.textViewEmptyLayoutText)
    public TextView textViewEmptyLayoutText;

    private String date;
    private int page;
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_MAIN_CLASS_LIST;

    private ClassListRequest classListRequest;
    private ClassListAdapter classListAdapter;

    private int fragmentPositionInViewPager;
    private boolean isShownFirstTime = true;
    private boolean shouldRefresh = false;

    private int shownInScreen;
    private int currentPosition;

    public ClassList() {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newFilter(Events.NewFilter newFilter) {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_HOME_FIND_CLASSES) {
            shouldRefresh = true;
            classListAdapter.clearAll();
            classListAdapter.notifyDataSetChanged();

            if ((fragmentPositionInViewPager == FindClass.SELECTED_POSITION)) {
                onTabSelection(FindClass.SELECTED_POSITION);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newFilter(Events.RefreshClassList refreshClassList) {
        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_HOME_FIND_CLASSES) {
            shouldRefresh = true;
            classListAdapter.clearAll();
            classListAdapter.notifyDataSetChanged();

            if ((fragmentPositionInViewPager == FindClass.SELECTED_POSITION)) {
                onTabSelection(FindClass.SELECTED_POSITION);
            }
        }
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        swipeRefreshLayout.setOnRefreshListener(this);

        fragmentPositionInViewPager = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);
        shownInScreen = getArguments().getInt(AppConstants.DataKey.TAB_SHOWN_IN_INT);
        date = getArguments().getString(AppConstants.DataKey.CLASS_DATE_STRING, null);

        recyclerViewClass.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewClass.setHasFixedSize(false);
       // recyclerViewClass.scrollToPosition(0);

        try {
            ((SimpleItemAnimator) recyclerViewClass.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        classListAdapter = new ClassListAdapter(context, shownInScreen, true, this);
        recyclerViewClass.setAdapter(classListAdapter);

        setRecommendedClassView();
    }

    private void setRecommendedClassView() {
        recyclerViewRecommendedClass.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRecommendedClass.setHasFixedSize(true);

      /*  try {
            ((SimpleItemAnimator) recyclerViewRecommendedClass.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }*/

        RecommendedClassAdapter recommendedListAdapter = new RecommendedClassAdapter(context, shownInScreen, true, new ClassListListenerHelper(context, activity, shownInScreen, this));
        recyclerViewRecommendedClass.setAdapter(recommendedListAdapter);
    }

    private ClassListRequest generateRequest() {

        if (classListRequest == null) {
            classListRequest = new ClassListRequest();
        }

        classListRequest.setClassDate(date);
        classListRequest.setPage(page);
        classListRequest.setSize(pageSizeLimit);
        classListRequest.setUserId(TempStorage.getUser().getId());

        classListRequest.setActivityList(null);
        classListRequest.setGenderList(null);
        classListRequest.setTimingList(null);
        classListRequest.setLocationList(null);

        List<String> times = new ArrayList<>();
        List<String> activities = new ArrayList<>();
        List<String> gymList = new ArrayList<>();
        List<String> genders = new ArrayList<>();

        List<CityLocality> cityLocalities = new ArrayList<>();

        for (ClassesFilter classesFilter : TempStorage.getFilters()) {
            if (classesFilter.getObject() instanceof CityLocality) {
                cityLocalities.add((CityLocality) classesFilter.getObject());
            } else if (classesFilter.getObject() instanceof Filter.Time) {
                times.add(((Filter.Time) classesFilter.getObject()).getId());
            } else if (classesFilter.getObject() instanceof Filter.Gender) {
                genders.add(((Filter.Gender) classesFilter.getObject()).getId());
            } else if (classesFilter.getObject() instanceof ClassActivity) {
                activities.add(String.valueOf(((ClassActivity) classesFilter.getObject()).getId()));
            }
            else if (classesFilter.getObject() instanceof GymDataModel) {
                gymList.add(String.valueOf(((GymDataModel) classesFilter.getObject()).getId()));
            }
        }

        /******************************** To remove gender filter **********************************/
        genders.clear();
        genders.add(AppConstants.ApiParamValue.GENDER_BOTH);
        genders.add(TempStorage.getUser().getGender());
        /********************************************************************************/

        classListRequest.setActivityList(activities);
        classListRequest.setGenderList(genders);
        classListRequest.setTimingList(times);
        classListRequest.setLocationList(cityLocalities);
        classListRequest.setGymList(gymList);


        return classListRequest;
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, ClassModel model, int position) {
        switch (view.getId()) {
            case R.id.imageViewOptions:
                ClassListListenerHelper.popupOptionsAdd(context, networkCommunicator, view, model, shownInScreen);
                break;
            case R.id.textViewLocation:
            case R.id.layoutLocation:
                GymProfileActivity.open(context, model.getGymBranchDetail().getGymId(), shownInScreen);
                break;
            case R.id.layoutTrainer:
                if (model.getTrainerDetail() != null) {
                    TrainerProfileActivity.open(context, model.getTrainerDetail(), shownInScreen);
                } else {
                    GymProfileActivity.open(context, model.getGymBranchDetail().getGymId(), shownInScreen);
                }
                break;
            case R.id.buttonJoin:
            default:
                ClassProfileActivity.open(context, model, shownInScreen);
        }
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, ClassModel model, int position) {

    }

    @Override
    public void onShowLastItem() {
        page++;
        callApiClassList();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        classListAdapter.loaderReset();
        callApiClassList();
    }

    private void callApiClassList() {
        networkCommunicator.getClassList(generateRequest(), this, false);
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CLASS_LIST:
                swipeRefreshLayout.setRefreshing(false);

                if (page == 0) {
                    classListAdapter.clearAll();
                }

                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (!classModels.isEmpty()) {
                    classListAdapter.addAllClass(classModels);

                    if (classModels.size() < pageSizeLimit) {
                        classListAdapter.loaderDone();
                    }
                    classListAdapter.notifyDataSetChanged();
                } else {
                    classListAdapter.loaderDone();
                }

                checkListData();

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CLASS_LIST:

                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.showLong(context, errorMessage);

                break;
        }
    }

    private void checkListData() {
        if (classListAdapter.getList().isEmpty()) {
            layoutNoData.setVisibility(View.VISIBLE);
            classListAdapter.notifyDataSetChanged();
            textViewEmptyLayoutText.setText(R.string.no_data_class_list_main);
            imageViewEmptyLayoutImage.setImageResource(R.drawable.stub_class);
        } else {
            layoutNoData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTabSelection(int position) {
        LogUtils.debug("varun f " + fragmentPositionInViewPager + " onTabSelection " + position + " vp position " + FindClass.SELECTED_POSITION);

        currentPosition = position;
        if ((shouldRefresh && (fragmentPositionInViewPager == position))
                || ((fragmentPositionInViewPager == position) && isShownFirstTime)) {
            isShownFirstTime = false;
            shouldRefresh = false;

            onRefresh();
        } else {
            if (classListAdapter.getList().isEmpty()) {
                onRefresh();
            }
        }
    }
}

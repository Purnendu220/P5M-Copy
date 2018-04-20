package www.gymhop.p5m.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.ClassListAdapter;
import www.gymhop.p5m.data.CityLocality;
import www.gymhop.p5m.data.ClassActivity;
import www.gymhop.p5m.data.ClassesFilter;
import www.gymhop.p5m.data.Filter;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.request.ClassListRequest;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.Main.ClassProfileActivity;
import www.gymhop.p5m.view.activity.custom.MyRecyclerView;

public class ClassList extends BaseFragment implements ViewPagerFragmentSelection, MyRecyclerView.LoaderCallbacks, AdapterCallbacks<ClassModel>, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener {

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
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private String date;
    private int page;
    private ClassListRequest classListRequest;
    private ClassListAdapter classListAdapter;

    private int fragmentPositionInViewPager;
    private boolean isShownFirstTime = true;

    private int shownInScreen;

    public ClassList() {
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

        classListAdapter = new ClassListAdapter(context, shownInScreen, true, this);
        recyclerViewClass.setAdapter(classListAdapter);
    }

    private ClassListRequest generateRequest() {
        List<ClassesFilter> filters = TempStorage.getFilters();

        if (classListRequest == null) {
            classListRequest = new ClassListRequest();
        }

        classListRequest.setClassDate(date);
        classListRequest.setPage(page);
        classListRequest.setSize(AppConstants.Limit.PAGE_LIMIT_MAIN_CLASS_LIST);
        classListRequest.setUserId(TempStorage.getUser().getId());

        classListRequest.setActivityList(null);
        classListRequest.setGenderList(null);
        classListRequest.setTimingList(null);
        classListRequest.setLocationList(null);

        List<String> times = new ArrayList<>();
        List<String> activities = new ArrayList<>();
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
        }

        classListRequest.setActivityList(activities);
        classListRequest.setGenderList(genders);
        classListRequest.setTimingList(times);
        classListRequest.setLocationList(cityLocalities);

        return classListRequest;
    }

    @Override
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {
        LogUtils.debug("onShowLoader " + position);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, ClassModel model, int position) {
        switch (view.getId()) {
            case R.id.textViewLocation:
                break;
            case R.id.layoutTrainer:
                break;
            case R.id.buttonJoin:
            default:
                ClassProfileActivity.open(context, model);
        }
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, ClassModel model, int position) {
    }

    @Override
    public void onShowLastItem() {
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CLASS_LIST:
                swipeRefreshLayout.setRefreshing(false);
                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (!classModels.isEmpty()) {
                    classListAdapter.addAllClass(classModels);
                    classListAdapter.notifyDataSetChanged();
                } else {
                    checkListData();
                }
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CLASS_LIST:

                swipeRefreshLayout.setRefreshing(false);
                checkListData();

                break;
        }
    }

    private void checkListData() {
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        networkCommunicator.getClassList(generateRequest(), this, false);
    }

    @Override
    public void onTabSelection(int position) {
        if (fragmentPositionInViewPager == position && isShownFirstTime) {
            isShownFirstTime = false;

            onRefresh();
        }
    }
}

package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.FilterAdapter;
import www.gymhop.p5m.data.City;
import www.gymhop.p5m.data.CityLocality;
import www.gymhop.p5m.data.ClassActivity;
import www.gymhop.p5m.data.ClassesFilter;
import www.gymhop.p5m.data.Filter;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class FilterActivity extends BaseActivity implements NetworkCommunicator.RequestListener<ResponseModel>, AdapterCallbacks {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, FilterActivity.class));
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;

    private FilterAdapter filterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(activity);

        networkCommunicator.getCities(this, false);
        networkCommunicator.getActivities(this, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        filterAdapter = new FilterAdapter(context, this);
        recyclerView.setAdapter(filterAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setListAdapter();
    }

    private void setListAdapter() {
        List<ClassesFilter> classesFilterList = new ArrayList<>(4);

        classesFilterList.add(new ClassesFilter("Location", R.drawable.location, ClassesFilter.TYPE_HEADER));
        classesFilterList.add(new ClassesFilter("Activity", R.drawable.class_icon, ClassesFilter.TYPE_HEADER));
        classesFilterList.add(new ClassesFilter("Time", R.drawable.time, ClassesFilter.TYPE_HEADER));
        classesFilterList.add(new ClassesFilter("Gender", R.drawable.gender, ClassesFilter.TYPE_HEADER));

        filterAdapter.setClassesFilterList(classesFilterList);

        List<ClassesFilter> timeList = new ArrayList<>(4);
        addClassFilterTime(timeList, new Filter.Time("Morning"));
        addClassFilterTime(timeList, new Filter.Time("After Noon"));
        addClassFilterTime(timeList, new Filter.Time("Evening"));

        filterAdapter.getClassesFilterList().get(2).setList(timeList);

        List<ClassesFilter> genderList = new ArrayList<>(4);
        addClassFilterGender(genderList, new Filter.Gender("Male"));
        addClassFilterGender(genderList, new Filter.Gender("Female"));
        addClassFilterGender(genderList, new Filter.Gender("Both"));

        filterAdapter.getClassesFilterList().get(3).setList(genderList);

        filterAdapter.refreshList();
        filterAdapter.notifyDataSetChanged();
    }

    public void addClassFilterTime(List<ClassesFilter> classesFilters, Filter.Time time) {
        ClassesFilter filter = new ClassesFilter(time.getName(), 0, ClassesFilter.TYPE_ITEM);
        filter.setObject(time);

        classesFilters.add(filter);
    }

    public void addClassFilterGender(List<ClassesFilter> classesFilters, Filter.Gender gender) {
        ClassesFilter filter = new ClassesFilter(gender.getName(), 0, ClassesFilter.TYPE_ITEM);
        filter.setObject(gender);

        classesFilters.add(filter);
    }

    @Override
    public void onApiSuccess(ResponseModel response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.ALL_CITY: {
                final List<City> list = (List<City>) response.data;
                if (!list.isEmpty()) {

                    List<ClassesFilter> classesFilters = new ArrayList<>();
                    for (City city : list) {

                        if (city.isStatus()) {

                            ClassesFilter classesFilter = new ClassesFilter(city.getName(), 0, ClassesFilter.TYPE_SUB_HEADER);
                            classesFilter.setObject(city);
                            classesFilters.add(classesFilter);

                            List<ClassesFilter> filters = new ArrayList<>();
                            if (city.getLocality() != null && !city.getLocality().isEmpty()) {
                                for (CityLocality cityLocality : city.getLocality()) {
                                    ClassesFilter filter = new ClassesFilter(cityLocality.getName(), 0, ClassesFilter.TYPE_ITEM);
                                    filter.setObject(cityLocality);
                                    filter.setList(null);
                                    filters.add(filter);
                                }
                            }

                            classesFilter.setList(filters);
                        }
                    }

                    filterAdapter.getClassesFilterList().get(0).setList(classesFilters);
                    filterAdapter.refreshList();
                }
            }
            break;
            case NetworkCommunicator.RequestCode.ALL_CLASS_ACTIVITY: {
                final List<ClassActivity> list = (List<ClassActivity>) response.data;
                if (!list.isEmpty()) {
                    List<ClassesFilter> classesFilters = new ArrayList<>();
                    for (ClassActivity activity : list) {
                        ClassesFilter classesFilter = new ClassesFilter(activity.getName(), 0, ClassesFilter.TYPE_ITEM);
                        classesFilter.setObject(activity);
                        classesFilters.add(classesFilter);
                    }

                    filterAdapter.getClassesFilterList().get(1).setList(classesFilters);
                    filterAdapter.refreshList();
                }
            }
            break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

    }

    @Override
    public void onItemClick(View viewRoot, View view, Object model, int position) {
        ClassesFilter classesFilter = model instanceof ClassesFilter ? ((ClassesFilter) model) : null;

        if (classesFilter != null && (classesFilter.getType() == ClassesFilter.TYPE_HEADER || classesFilter.getType() == ClassesFilter.TYPE_SUB_HEADER)) {

            if (classesFilter.isExpanded()) {
                classesFilter.setExpanded(!classesFilter.isExpanded());

                filterAdapter.refreshList();
                filterAdapter.notifyDataSetChanged();

//                int index = filterAdapter.getList().indexOf(classesFilter);
//
//                if (index == -1) {
//                    filterAdapter.notifyDataSetChanged();
//                } else {
////                    filterAdapter.notifyItemChanged(index);
//                    filterAdapter.notifyItemRangeRemoved(index + 1, index + classesFilter.getList().size());
//                }
//                LogUtils.debug("FilterActivity onItemClick " + (index + 1) + " " + (index + classesFilter.getList().size()));

            } else {
                classesFilter.setExpanded(!classesFilter.isExpanded());
                filterAdapter.refreshList();
                int index = filterAdapter.getList().indexOf(classesFilter);
                filterAdapter.notifyDataSetChanged();

//                if (index == -1) {
//                    filterAdapter.notifyDataSetChanged();
//                } else {
////                    filterAdapter.notifyItemChanged(index);
//                    filterAdapter.notifyItemRangeInserted(index + 1, index + classesFilter.getList().size());
//                }
//                LogUtils.debug("FilterActivity onItemClick " + (index + 1) + " " + (index + classesFilter.getList().size()));
            }
        } else if (classesFilter != null && (classesFilter.getType() == ClassesFilter.TYPE_ITEM)) {
            classesFilter.setSelected(!classesFilter.isSelected());
            int index = filterAdapter.getList().indexOf(classesFilter);

            if (index == -1) {
                filterAdapter.notifyDataSetChanged();
            } else {
                filterAdapter.notifyItemChanged(index);
            }
//            LogUtils.debug("FilterActivity onItemClick " + index);
        }
    }

    @Override
    public void onItemLongClick(View viewRoot, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
}

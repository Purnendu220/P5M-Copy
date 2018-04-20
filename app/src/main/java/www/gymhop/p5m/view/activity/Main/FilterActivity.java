package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class FilterActivity extends BaseActivity implements NetworkCommunicator.RequestListener<ResponseModel>, AdapterCallbacks, View.OnClickListener {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, FilterActivity.class));
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;

    @BindView(R.id.textViewFindClasses)
    public View textViewFindClasses;

    private FilterAdapter filterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(activity);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        filterAdapter = new FilterAdapter(context, this);
        recyclerView.setAdapter(filterAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loaderFilters();

        textViewFindClasses.setOnClickListener(this);

        setListAdapter();

        networkCommunicator.getCities(this, true);
        networkCommunicator.getActivities(this, true);

        setToolBar();

    }

    private void loaderFilters() {
        if (!TempStorage.getFilters().isEmpty()) {
            filterAdapter.addSelection(TempStorage.getFilters());
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

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_bar_filter, null);
        v.findViewById(R.id.textViewClear).setOnClickListener(this);
        v.findViewById(R.id.imageViewBack).setOnClickListener(this);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    private void setListAdapter() {
        List<ClassesFilter> classesFilterList = new ArrayList<>(4);

        classesFilterList.add(new ClassesFilter("Location", R.drawable.location, ClassesFilter.TYPE_HEADER));
        classesFilterList.add(new ClassesFilter("Activity", R.drawable.class_icon, ClassesFilter.TYPE_HEADER));
        classesFilterList.add(new ClassesFilter("Time", R.drawable.time, ClassesFilter.TYPE_HEADER));
        classesFilterList.add(new ClassesFilter("Gender", R.drawable.gender, ClassesFilter.TYPE_HEADER));

        filterAdapter.setClassesFilterList(classesFilterList);

        List<ClassesFilter> timeList = new ArrayList<>(4);
        addClassFilterTime(timeList, new Filter.Time("MORNING", "Morning"));
        addClassFilterTime(timeList, new Filter.Time("AFTERNOON", "After Noon"));
        addClassFilterTime(timeList, new Filter.Time("EVENING", "Evening"));

        filterAdapter.getClassesFilterList().get(2).setList(timeList);

        List<ClassesFilter> genderList = new ArrayList<>(4);
        addClassFilterGender(genderList, new Filter.Gender("MALE", "Males Only"));
        addClassFilterGender(genderList, new Filter.Gender("FEMALE", "Females Only"));
        addClassFilterGender(genderList, new Filter.Gender("MIXED", "Both"));

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
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
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
//                LogUtils.debug("FilterActivity onAdapterItemClick " + (index + 1) + " " + (index + classesFilter.getList().size()));

            } else {
                classesFilter.setExpanded(!classesFilter.isExpanded());

                filterAdapter.refreshList();
                filterAdapter.notifyDataSetChanged();

//                int index = filterAdapter.getList().indexOf(classesFilter);

//                if (index == -1) {
//                    filterAdapter.notifyDataSetChanged();
//                } else {
////                    filterAdapter.notifyItemChanged(index);
//                    filterAdapter.notifyItemRangeInserted(index + 1, index + classesFilter.getList().size());
//                }
//                LogUtils.debug("FilterActivity onAdapterItemClick " + (index + 1) + " " + (index + classesFilter.getList().size()));
            }
        } else if (classesFilter != null && (classesFilter.getType() == ClassesFilter.TYPE_ITEM)) {

            classesFilter.setSelected(!classesFilter.isSelected());

            if (classesFilter.isSelected()) {
                filterAdapter.addSelection(classesFilter);
            } else {
                filterAdapter.removeSelection(classesFilter);
            }

            int index = filterAdapter.getList().indexOf(classesFilter);

            if (index == -1) {
                filterAdapter.notifyDataSetChanged();
            } else {
                filterAdapter.notifyItemChanged(index);
            }
//            LogUtils.debug("FilterActivity onAdapterItemClick " + index);
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
            case R.id.textViewClear:
                filterAdapter.clearExpansionAndSelection();
                TempStorage.setFilterList(new ArrayList<ClassesFilter>());
                break;
            case R.id.imageViewBack:
                finish();
                break;
            case R.id.textViewFindClasses:
                TempStorage.setFilterList(filterAdapter.getClassesFiltersSelected());
                finish();
                break;
        }
    }
}

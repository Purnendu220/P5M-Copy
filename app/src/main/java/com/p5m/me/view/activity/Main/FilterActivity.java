package com.p5m.me.view.activity.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.FilterAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.City;
import com.p5m.me.data.CityLocality;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Filter;
import com.p5m.me.data.Nationality;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.GymDataModel;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.activity.custom.MaxHeightScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends BaseActivity implements NetworkCommunicator.RequestListener<ResponseModel>, AdapterCallbacks, View.OnClickListener {

    public static void openActivity(Context context, Activity activity, View sharedElement) {

//        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElement, "filter");
//        context.startActivity(new Intent(context, FilterActivity.class), activityOptionsCompat.toBundle());

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

    @BindView(R.id.flexBoxLayout)
    public FlexboxLayout flexBoxLayout;
    @BindView(R.id.scrollView)
    public MaxHeightScrollView scrollView;

    private FilterAdapter filterAdapter;
    private HashMap<ClassesFilter, View> classesFilterViewHashMap;
    private HashMap<View, ClassesFilter> viewClassesFilterHashMap;
    private List<Filter.FitnessLevel> fitnessLevelList;
    private List<Filter.PriceModel> priceModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        ButterKnife.bind(activity);

        flexBoxLayout.setFlexDirection(FlexDirection.ROW);
        classesFilterViewHashMap = new HashMap<>();
        viewClassesFilterHashMap = new HashMap<>();

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
        networkCommunicator.getGymsList(this, true);

        setToolBar();
        onTrackingNotification();

    }

    public void checkTags() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(scrollView);
        }

        if (filterAdapter.getClassesFiltersSelected().isEmpty()) {
            if (scrollView.getVisibility() != View.GONE) {
                scrollView.setVisibility(View.GONE);
            }
        } else {
            if (scrollView.getVisibility() != View.VISIBLE) {
                scrollView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addTag(final ClassesFilter classesFilter) {
        final View view = LayoutInflater.from(context).inflate(R.layout.view_filter_tag, flexBoxLayout, false);
        ImageView imageLeft = view.findViewById(R.id.imageLeft);
        ImageView imageRight = view.findViewById(R.id.imageRight);
        TextView textView = view.findViewById(R.id.textView);

        if (classesFilter.getName().length() > 33) {
            textView.setText(classesFilter.getName().substring(0, 33) + "...");
        } else {
            textView.setText(classesFilter.getName());
        }

        classesFilterViewHashMap.put(classesFilter, view);
        viewClassesFilterHashMap.put(view, classesFilter);

        imageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View imageRight) {
                removeTag(view);
            }
        });

        checkTags();

        if (classesFilter.getObject() instanceof CityLocality) {
            imageLeft.setImageResource(R.drawable.filter_location);
        } else if (classesFilter.getObject() instanceof ClassActivity) {
            imageLeft.setImageResource(R.drawable.filter_activity);
        } else if (classesFilter.getObject() instanceof Filter.Time) {
            imageLeft.setImageResource(R.drawable.filter_time);
        } else if (classesFilter.getObject() instanceof GymDataModel) {
            imageLeft.setImageResource(R.drawable.filter_gym);
        } else if (classesFilter.getObject() instanceof Filter.PriceModel) {
            imageLeft.setImageResource(R.drawable.small_special_icon);
        } else if (classesFilter.getObject() instanceof Filter.FitnessLevel) {
            imageLeft.setImageResource(R.drawable.class_level_get);
        } else {
            imageLeft.setImageResource(R.drawable.filter_activity);
        }

        flexBoxLayout.addView(view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void removeTag(ClassesFilter classesFilter) {

        try {
            View view = classesFilterViewHashMap.get(classesFilter);
            classesFilterViewHashMap.remove(classesFilter);
            viewClassesFilterHashMap.remove(view);

            flexBoxLayout.removeView(view);

            classesFilter.setSelected(false);
            filterAdapter.removeSelection(classesFilter);
            checkTags();

            int index = filterAdapter.getList().indexOf(classesFilter);
            if (index != -1) {
                filterAdapter.getList().get(index).setSelected(false);
                filterAdapter.notifyItemChanged(index);
            }


        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    private void removeTag(View view) {

        try {
            ClassesFilter classesFilter = viewClassesFilterHashMap.get(view);
            classesFilterViewHashMap.remove(classesFilter);
            viewClassesFilterHashMap.remove(view);

            flexBoxLayout.removeView(view);

            classesFilter.setSelected(false);
            filterAdapter.removeSelection(classesFilter);
            checkTags();
            int index = filterAdapter.getList().indexOf(classesFilter);
            if (index != -1) {
                filterAdapter.getList().get(index).setSelected(false);
                filterAdapter.notifyItemChanged(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    private void loaderFilters() {
        if (!TempStorage.getFilters().isEmpty()) {
            filterAdapter.addSelection(TempStorage.getFilters());

            for (ClassesFilter classesFilter : TempStorage.getFilters()) {
                addTag(classesFilter);
            }
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
        List<ClassesFilter> classesFilterList = new ArrayList<>(5);

        classesFilterList.add(new ClassesFilter<CityLocality>("", true, "CityLocality", getString(R.string.location), R.drawable.filter_location_main, ClassesFilter.TYPE_HEADER));
        classesFilterList.add(new ClassesFilter<ClassActivity>("", true, "ClassActivity", getString(R.string.activity), R.drawable.filter_activity_main, ClassesFilter.TYPE_HEADER));
        classesFilterList.add(new ClassesFilter<Filter.Time>("", true, "Time", getString(R.string.time), R.drawable.filter_time_main, ClassesFilter.TYPE_HEADER));
        classesFilterList.add(new ClassesFilter<Filter.Gym>("", true, "Gym", getString(R.string.gym), R.drawable.filter_gym_main, ClassesFilter.TYPE_HEADER));
        classesFilterList.add(new ClassesFilter<Filter.FitnessLevel>("", true, "FitnessLevel", getString(R.string.fitness_level), R.drawable.class_level_get, ClassesFilter.TYPE_HEADER));
        classesFilterList.add(new ClassesFilter<Filter.PriceModel>("", true, "PriceModel", getString(R.string.priceModel), R.drawable.small_special_icon, ClassesFilter.TYPE_HEADER));

        filterAdapter.setClassesFilterList(classesFilterList);

        List<ClassesFilter> timeList = new ArrayList<>(4);
        addClassFilterTime(timeList, new Filter.Time("MORNING", getString(R.string.morning)));
        addClassFilterTime(timeList, new Filter.Time("AFTERNOON", getString(R.string.after_Noon)));
        addClassFilterTime(timeList, new Filter.Time("EVENING", getString(R.string.evening)));

        filterAdapter.getClassesFilterList().get(2).setList(timeList);
        getFitnessLevelAndPriceModel();


//        List<ClassesFilter> genderList = new ArrayList<>(4);
//        addClassFilterGender(genderList, new Filter.Gender(AppConstants.ApiParamValue.GENDER_MALE, "Males Only"));
//        addClassFilterGender(genderList, new Filter.Gender(AppConstants.ApiParamValue.GENDER_FEMALE, "Females Only"));
//        addClassFilterGender(genderList, new Filter.Gender(AppConstants.ApiParamValue.GENDER_BOTH, "Both"));
//
//        filterAdapter.getClassesFilterList().get(3).setList(genderList);

        filterAdapter.refreshList();
        filterAdapter.notifyDataSetChanged();
    }

    public void addClassFilterTime(List<ClassesFilter> classesFilters, Filter.Time time) {
        ClassesFilter filter = new ClassesFilter<Filter.Time>("", true, "Time", time.getName(), 0, ClassesFilter.TYPE_ITEM);
        filter.setObject(time);

        classesFilters.add(filter);
    }



    public void addClassFilterGender(List<ClassesFilter> classesFilters, Filter.Gender gender) {
        ClassesFilter filter = new ClassesFilter<Filter.Gender>("", true, "Gender", gender.getName(), 0, ClassesFilter.TYPE_ITEM);
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

                            ClassesFilter classesFilter = new ClassesFilter<City>("", true, "City", city.getName(), 0, ClassesFilter.TYPE_SUB_HEADER);
                            classesFilter.setObject(city);
                            classesFilters.add(classesFilter);

                            List<ClassesFilter> filters = new ArrayList<>();
                            if (city.getLocality() != null && !city.getLocality().isEmpty()) {
                                for (CityLocality cityLocality : city.getLocality()) {

                                    ClassesFilter filter = new ClassesFilter<CityLocality>(cityLocality.getId() + "", true, "CityLocality", cityLocality.getName(), 0, ClassesFilter.TYPE_ITEM);
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
                        if (activity.getStatus()) {
                            ClassesFilter classesFilter = new ClassesFilter(activity.getId() + "", true, "ClassActivity", activity.getName(), 0, ClassesFilter.TYPE_ITEM);
                            classesFilter.setObject(activity);

                            classesFilters.add(classesFilter);
                        }
                    }

                    filterAdapter.getClassesFilterList().get(1).setList(classesFilters);
                    filterAdapter.refreshList();
                }
            }
            break;
            case NetworkCommunicator.RequestCode.ALL_GYM_LIST: {
                final List<GymDataModel> list = (List<GymDataModel>) response.data;
                if (!list.isEmpty()) {
                    List<ClassesFilter> classesFilters = new ArrayList<>();
                    for (GymDataModel gymData : list) {
                        ClassesFilter classesFilter = new ClassesFilter(gymData.getId() + "", true, "Gym", gymData.getStudioName(), 0, ClassesFilter.TYPE_ITEM);
                        classesFilter.setObject(gymData);
                        classesFilters.add(classesFilter);
                    }
                    filterAdapter.getClassesFilterList().get(3).setList(classesFilters);
                    filterAdapter.refreshList();

                }

            }
           /* default: {
               getFitnessLevelAndPriceModel();
            }*/
            break;

        }
    }

    private void getFitnessLevelAndPriceModel() {
        try {
            fitnessLevelList = new Gson().fromJson(Helper.getFitnessLevelFromAsset(context), new TypeToken<List<Filter.FitnessLevel>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!fitnessLevelList.isEmpty()) {
            List<ClassesFilter> classesFilters = new ArrayList<>();
            for (Filter.FitnessLevel fitnessLevel : fitnessLevelList) {
                ClassesFilter classesFilter = new ClassesFilter(fitnessLevel.getId() + "", true, "FitnessLevel",fitnessLevel.getName(), 0, ClassesFilter.TYPE_ITEM);
                classesFilter.setObject(fitnessLevel);
                classesFilters.add(classesFilter);
            }
            filterAdapter.getClassesFilterList().get(4).setList(classesFilters);
            filterAdapter.refreshList();

        }
        try {
            priceModelList = new Gson().fromJson(Helper.getPriceModelFromAsset(context), new TypeToken<List<Filter.PriceModel>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!priceModelList.isEmpty()) {
            List<ClassesFilter> classesFilters = new ArrayList<>();
            for (Filter.PriceModel priceModel : priceModelList) {
                ClassesFilter classesFilter = new ClassesFilter(priceModel.getId() + "", true, "PriceModel", priceModel.getName(), 0, ClassesFilter.TYPE_ITEM);
                classesFilter.setObject(priceModel);
                classesFilters.add(classesFilter);
            }
            filterAdapter.getClassesFilterList().get(5).setList(classesFilters);
            filterAdapter.refreshList();

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
//                    filterAdapter.notifyItemChanged(index);
//                    filterAdapter.notifyItemRangeRemoved(index + 1, index + classesFilter.getList().size() - 1);
//                }
//                LogUtils.debug("FilterActivity onAdapterItemClick " + (index + 1) + " " + (index + classesFilter.getList().size()));

            } else {
                classesFilter.setExpanded(!classesFilter.isExpanded());

                filterAdapter.refreshList();

                filterAdapter.notifyDataSetChanged();

//                int index = filterAdapter.getList().indexOf(classesFilter);
//
//                if (index == -1) {
//                    filterAdapter.notifyDataSetChanged();
//                } else {
//                    filterAdapter.notifyItemRangeInserted(index + 1, index + classesFilter.getList().size() - 1);
//                    filterAdapter.notifyItemChanged(index);
//                }
//                LogUtils.debug("FilterActivity onAdapterItemClick " + (index + 1) + " " + (index + classesFilter.getList().size()));
            }
        } else if (classesFilter != null && (classesFilter.getType() == ClassesFilter.TYPE_ITEM)) {

            classesFilter.setSelected(!classesFilter.isSelected());

            if (classesFilter.isSelected()) {
                filterAdapter.addSelection(classesFilter);
                addTag(classesFilter);
            } else {
                filterAdapter.removeSelection(classesFilter);
                removeTag(classesFilter);
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
                flexBoxLayout.removeAllViews();
                checkTags();
                break;
            case R.id.imageViewBack:
                finish();
                break;
            case R.id.textViewFindClasses:
                TempStorage.setFilterList(filterAdapter.getClassesFiltersSelected());
                EventBroadcastHelper.sendNewFilterSet();
                MixPanel.trackFilters(TempStorage.getFilters());
                finish();
                break;
        }
    }
}

package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Filter;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.request.ChooseFocusRequest;
import com.p5m.me.data.request.UserInfoUpdate;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.activity.custom.MaxHeightScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.AppConstants.Tab.TAB_FIND_CLASS;

public class GetStartedActivity extends BaseActivity implements View.OnClickListener, AdapterCallbacks, NetworkCommunicator.RequestListener<ResponseModel> {

    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.textViewSkip)
    TextView textViewSkip;
    @BindView(R.id.textViewHeader)
    TextView textViewHeader;
    @BindView(R.id.buttonBottom)
    Button buttonBottom;
    @BindView(R.id.flexBoxLayout)
    public FlexboxLayout flexBoxLayout;
    @BindView(R.id.flexBoxLayoutTime)
    public FlexboxLayout flexBoxLayoutTime;

    final int FIRST_STEP = 1;
    final int SECOND_STEP = 2;
    final int THIRD_STEP = 3;
    int STEP = 1;
    private List<Filter.Time> timeList;
    private ClassesFilter<Filter.Time> timeFilter;
    private ClassesFilter<ClassActivity> classesFilter;
    private List<ClassesFilter> classesFilters;
    private List<ClassActivity> activities;
    private StringBuffer selectedTimeList;
    private UserInfoUpdate userInfoUpdate;


    public static void open(Context context) {
        Intent intent = new Intent(context, GetStartedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        ButterKnife.bind(activity);

        flexBoxLayout.setFlexDirection(FlexDirection.ROW);
        flexBoxLayoutTime.setFlexDirection(FlexDirection.ROW);
        flexBoxLayout.setVisibility(View.GONE);
        flexBoxLayoutTime.setVisibility(View.GONE);
        textViewHeader.setText(R.string.get_started_suggesstion);
        buttonBottom.setText(getString(R.string.get_started));
        classesFilters = new ArrayList<>();
        activities = new ArrayList<>();
        selectedTimeList = new StringBuffer();
        networkCommunicator.getActivities(this, true);

        setClickEvents();
        getTime();
    }

    private void setClickEvents() {
        buttonBottom.setOnClickListener(this);
        textViewSkip.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);
    }

    private void getTime() {
        try {
            timeList = new Gson().fromJson(Helper.getTimeFromAsset(context), new TypeToken<List<Filter.Time>>() {
            }.getType());
            for (Filter.Time time : timeList)
                addTimeTag(time);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBottom:
                STEP++;
                setView();
                break;
            case R.id.textViewSkip:
                HomeActivity.open(context);
                break;
            case R.id.imageViewBack:
                onBackPressed();
                break;
        }
    }

    private void setView() {
        switch (STEP) {
            case FIRST_STEP:
                imageViewBack.setVisibility(View.INVISIBLE);
                textViewHeader.setText(getString(R.string.get_started_suggesstion));
                buttonBottom.setText(getString(R.string.get_started));
                flexBoxLayout.setVisibility(View.GONE);
                flexBoxLayoutTime.setVisibility(View.GONE);

                break;
            case SECOND_STEP:
                textViewHeader.setText(getString(R.string.activity_selection_suggesstion));
                imageViewBack.setVisibility(View.VISIBLE);

                buttonBottom.setText(getString(R.string.next));
                flexBoxLayout.setVisibility(View.VISIBLE);
                flexBoxLayoutTime.setVisibility(View.GONE);
                break;
            case THIRD_STEP:
                imageViewBack.setVisibility(View.VISIBLE);
                textViewHeader.setText(getString(R.string.convenient_time));
                buttonBottom.setText(getString(R.string.finish));
                flexBoxLayout.setVisibility(View.GONE);
                flexBoxLayoutTime.setVisibility(View.VISIBLE);
                break;
            default:
                callApi(activities);
                HomeActivity.show(context, TAB_FIND_CLASS, AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE);
                break;
        }
    }

    private void addTagClassActivity(ClassActivity classActivity) {
        final View view = LayoutInflater.from(context).inflate(R.layout.view_tiles, flexBoxLayout, false);
        TextView textView = view.findViewById(R.id.textViewTiles);
        if (!TextUtils.isEmpty(classActivity.getName())) {
            String name = classActivity.getName();
            textView.setText(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
            flexBoxLayout.addView(view);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View imageRight) {
                handleActivityTagSelection(classActivity, view);
            }
        });

    }

    private void addTimeTag(Filter.Time time) {
        final View view = LayoutInflater.from(context).inflate(R.layout.view_tiles, flexBoxLayout, false);
        TextView textView = view.findViewById(R.id.textViewTiles);
        if (!TextUtils.isEmpty(time.getName())) {
            String name = time.getName();
            textView.setText(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());

//        textView.setText();
            flexBoxLayoutTime.addView(view);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View imageRight) {
                handleTimeTagSelection(time, view);
            }
        });

    }

    private void handleTimeTagSelection(Filter.Time time, View view) {
        TextView textView = view.findViewById(R.id.textViewTiles);
        textView.setTextColor(getResources().getColor(R.color.theme_dark_text));

        if (time.getStatus()) {
            time.setStatus(false);
            textView.setTextColor(getResources().getColor(R.color.theme_dark_text));
            textView.setBackground(getResources().getDrawable(R.drawable.button_white));

        } else {
            time.setStatus(true);
            timeFilter = new ClassesFilter(time.getId(), true, "Time", time.getName(), R.drawable.filter_time_main, ClassesFilter.TYPE_HEADER);
            timeFilter.setObject(time);
            timeFilter.setSelected(true);
            classesFilters.add(timeFilter);
            textView.setBackground(getResources().getDrawable(R.drawable.theme_bottom_text_button_book));
            textView.setTextColor(getResources().getColor(R.color.white));

        }
    }

    private void handleActivityTagSelection(ClassActivity classActivity, View view) {
        TextView textView = view.findViewById(R.id.textViewTiles);

        if (classActivity.getSelected()) {
            classActivity.setSelected(false);
            activities.remove(classActivity);
            textView.setTextColor(getResources().getColor(R.color.theme_dark_text));
            textView.setBackground(getResources().getDrawable(R.drawable.button_white));

        } else {
            classActivity.setSelected(true);
            classesFilter = new ClassesFilter(classActivity.getId() + "", true, "ClassActivity", classActivity.getName(), R.drawable.filter_activity, ClassesFilter.TYPE_ITEM);
            classesFilter.setObject(classActivity);
            classesFilter.setSelected(true);
            activities.add(classActivity);
            classesFilters.add(classesFilter);
            textView.setBackground(getResources().getDrawable(R.drawable.theme_bottom_text_button_book));
            textView.setTextColor(getResources().getColor(R.color.white));

        }
    }

    private void callApi(List<ClassActivity> selectedNow) {
        networkCommunicator.updateUserFocus(TempStorage.getUser().getId(), new ChooseFocusRequest(selectedNow, null), this, false);
        userInfoUpdate = new UserInfoUpdate(TempStorage.getUser().getId());
        for (Filter.Time ft : timeList) {
            if (ft.getStatus()) {
                if (TextUtils.isEmpty(selectedTimeList)) {
                    selectedTimeList.append(ft.getName());
                } else
                    selectedTimeList.append(",").append(ft.getName());
            }
        }
        userInfoUpdate.setTimePreference(String.valueOf(selectedTimeList));
        networkCommunicator.userInfoUpdate(TempStorage.getUser().getId(), userInfoUpdate, this, false);
    }

    @Override
    public void onBackPressed() {
        if (STEP > 1) {
            STEP--;
            setView();
        } else
           HomeActivity.open(context);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.textViewTiles:
                if (model instanceof Filter.Time) {
                    Filter.Time data = (Filter.Time) model;
                    timeFilter = new ClassesFilter(data.getId(), true, "Time", getString(R.string.time), R.drawable.filter_time_main, ClassesFilter.TYPE_HEADER);
                    timeFilter.setObject(data);
                    classesFilter.setSelected(true);
                    classesFilters.add(timeFilter);

                }

                if (model != null && model instanceof ClassActivity) {
                    ClassActivity data = (ClassActivity) model;
                    classesFilter = new ClassesFilter(data.getId() + "", true, "ClassActivity", data.getName(), R.drawable.filter_activity, ClassesFilter.TYPE_ITEM);
                    classesFilter.setObject(data);
                    classesFilter.setSelected(true);
                    classesFilters.add(classesFilter);


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
    public void onApiSuccess(ResponseModel response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.ALL_CLASS_ACTIVITY: {
                final List<ClassActivity> list = (List<ClassActivity>) response.data;
                if (!list.isEmpty()) {
                    for (ClassActivity activity : list) {
                        if (activity.getStatus()) {
                            activity.setSelected(false);
                            addTagClassActivity(activity);
                        }
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

    }
}


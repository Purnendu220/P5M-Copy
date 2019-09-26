package com.p5m.me.view.custom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ShowScheduleAdapter;
import com.p5m.me.data.CityLocality;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Filter;
import com.p5m.me.data.main.BranchModel;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.GymDataModel;
import com.p5m.me.data.main.ScheduleClassModel;
import com.p5m.me.data.request.ScheduleRequest;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.ClassProfileActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowSchedulesBootomDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener, NetworkCommunicator.RequestListener, AdapterCallbacks<Object> {

    public static AdapterCallbacks<BranchModel> adapterCallbacks;
    private static List<Integer> branchList;
    private static BranchModel branchModel;
    private static int branchModelId;
    private ShowScheduleAdapter showScheduleAdapter;
    public static Context context;
    public static String date;
    private NetworkCommunicator networkCommunicator;
    private ScheduleRequest scheduleRequest;
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_UNLIMITED;

    public static ShowSchedulesBootomDialogFragment newInstance(Context context,  String date,List<Integer> branchList,BranchModel branchModel,AdapterCallbacks<BranchModel> adapterCallbacks) {

        ShowSchedulesBootomDialogFragment bottomSheetFragment = new ShowSchedulesBootomDialogFragment();
        ShowSchedulesBootomDialogFragment.context = context;
        ShowSchedulesBootomDialogFragment.date = date;
        ShowSchedulesBootomDialogFragment.branchList = branchList;
        ShowSchedulesBootomDialogFragment.branchModel = branchModel;

        ShowSchedulesBootomDialogFragment.adapterCallbacks = adapterCallbacks;
        return bottomSheetFragment;
    }


   @BindView(R.id.recycleViewShowSchedule)
   public RecyclerView recycleViewShowSchedule;
    @BindView(R.id.layoutBottomSheet)
    public ConstraintLayout layoutBottomSheet;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.textViewGymBranch)
    public TextView textViewGymBranch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bootom_sheet_show_schedule, container,
                false);
        ButterKnife.bind(this, view);
        networkCommunicator = NetworkCommunicator.getInstance(context);
        setShowScheduleView();
        callApiScheduleList();
        setGymAndBranchName();
        return view;

    }

    private void setGymAndBranchName() {
        String gymAndBranchName=branchModel.getGymName().toUpperCase()+" - "+branchModel.getBranchName();
        textViewGymBranch.setText(gymAndBranchName);
    }

    private void callApiScheduleList() {
        progressBar.setVisibility(View.VISIBLE);
        networkCommunicator.getScheduleClassList(generateRequest(), this, false);
    }

    private ScheduleRequest generateRequest() {

        if (scheduleRequest == null) {
            scheduleRequest = new ScheduleRequest();
        }

        scheduleRequest.setUserId(TempStorage.getUser().getId());
        scheduleRequest.setClassDate(date);
        scheduleRequest.setActivityList(null);
        scheduleRequest.setGenderList(null);
        scheduleRequest.setTimingList(null);
        scheduleRequest.setLocationList(null);
        scheduleRequest.setSize(pageSizeLimit);
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
            } else if (classesFilter.getObject() instanceof GymDataModel) {
                gymList.add(String.valueOf(((GymDataModel) classesFilter.getObject()).getId()));
            }
        }

        /******************************** To remove gender filter **********************************/
        genders.clear();
        genders.add(AppConstants.ApiParamValue.GENDER_BOTH);
        genders.add(TempStorage.getUser().getGender());
        /********************************************************************************/

        scheduleRequest.setActivityList(activities);
        scheduleRequest.setGenderList(genders);
        scheduleRequest.setTimingList(times);
        scheduleRequest.setLocationList(cityLocalities);
        scheduleRequest.setGymList(gymList);
        scheduleRequest.setBranchList(branchList);



        return scheduleRequest;
    }
    private void setShowScheduleView() {
        recycleViewShowSchedule.setLayoutManager(new LinearLayoutManager(context));
        recycleViewShowSchedule.setHasFixedSize(true);
        showScheduleAdapter = new ShowScheduleAdapter(context, 2,true, this);
        recycleViewShowSchedule.setAdapter(showScheduleAdapter);
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        progressBar.setVisibility(View.GONE);
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.SCHEDULE_LIST:

                List<ScheduleClassModel> scheduleClassModels = ((ResponseModel<List<ScheduleClassModel>>) response).data;

                if (!scheduleClassModels.isEmpty()) {
                    showScheduleAdapter.clearAll();
                    showScheduleAdapter.addAllClass(scheduleClassModels);
                    if (scheduleClassModels.size() < pageSizeLimit) {
                        showScheduleAdapter.loaderDone();
                    }
                    showScheduleAdapter.notifyDataSetChanged();
                }
                else {
                    showScheduleAdapter.loaderDone();

//                    this.dismiss();
//                    ToastUtils.show(context, "Currently no class available");
                }


                break;

        }
    }


    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        progressBar.setVisibility(View.GONE);
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.SCHEDULE_LIST:
                ToastUtils.showLong(context, errorMessage);
                break;

        }
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.layoutScheduler:
                if(model instanceof ScheduleClassModel) {
                    ScheduleClassModel model1 = (ScheduleClassModel) model;
                    ClassProfileActivity.open(context, model1.getClassSessionId(), AppConstants.AppNavigation.NAVIGATION_FROM_SHOW_SCHEDULER);
                    this.dismiss();
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
}


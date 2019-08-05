package com.p5m.me.view.custom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
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
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.GymDataModel;
import com.p5m.me.data.request.ScheduleRequest;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowSchedulesBootomDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener, NetworkCommunicator.RequestListener, AdapterCallbacks<Object> {

    public static AdapterCallbacks<BranchModel> adapterCallbacks;
    private static List<Integer> branchList;
    private ShowScheduleAdapter showScheduleAdapter;
    public static Context context;
    public static String date;
    private NetworkCommunicator networkCommunicator;
    private ScheduleRequest scheduleRequest;

    public static ShowSchedulesBootomDialogFragment newInstance(Context context, BranchModel branchModel, String date,List<Integer> branchList,AdapterCallbacks<BranchModel> adapterCallbacks) {

        ShowSchedulesBootomDialogFragment bottomSheetFragment = new ShowSchedulesBootomDialogFragment();
        ShowSchedulesBootomDialogFragment.context = context;
        ShowSchedulesBootomDialogFragment.date = date;
        ShowSchedulesBootomDialogFragment.branchList = branchList;
        ShowSchedulesBootomDialogFragment.adapterCallbacks = adapterCallbacks;
        return bottomSheetFragment;
    }


   @BindView(R.id.recycleViewShowSchedule)
   public RecyclerView recycleViewShowSchedule;

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
        return view;

    }

    private void callApiScheduleList() {
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

        List<String> times = new ArrayList<>();
        List<String> activities = new ArrayList<>();
        List<String> gymList = new ArrayList<>();
        List<String> genders = new ArrayList<>();
        List<Integer> branchList = new ArrayList<>();
        branchList = this.branchList;

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


        return scheduleRequest;
    }
    private void setShowScheduleView() {
        recycleViewShowSchedule.setLayoutManager(new LinearLayoutManager(context));
        recycleViewShowSchedule.setHasFixedSize(true);
        showScheduleAdapter = new ShowScheduleAdapter(context, 2, this);
        recycleViewShowSchedule.setAdapter(showScheduleAdapter);
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.SCHEDULE_LIST:
                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (!classModels.isEmpty()) {
                    showScheduleAdapter.clearAll();
                    showScheduleAdapter.addAllClass(classModels);
                    showScheduleAdapter.notifyDataSetChanged();
                }
                break;

        }
    }


    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.SCHEDULE_LIST:
                ToastUtils.showLong(context, errorMessage);
                break;

        }
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
}


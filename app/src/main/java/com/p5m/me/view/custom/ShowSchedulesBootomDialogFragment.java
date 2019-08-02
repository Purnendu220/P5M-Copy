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
import com.p5m.me.data.main.ClassModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.BindsInstance;

public class ShowSchedulesBootomDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    public static AdapterCallbacks<ClassModel> adapterCallbacks;
    private ShowScheduleAdapter showScheduleAdapter;
    public static Context context;

    public static ShowSchedulesBootomDialogFragment newInstance(Context context, ClassModel classModel, AdapterCallbacks<ClassModel> adapterCallbacks) {

        ShowSchedulesBootomDialogFragment bottomSheetFragment = new ShowSchedulesBootomDialogFragment();
        ShowSchedulesBootomDialogFragment.context = context;
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
        setShowScheduleView();
        return view;

    }

    private void setShowScheduleView() {
        recycleViewShowSchedule.setLayoutManager(new LinearLayoutManager(context));
        recycleViewShowSchedule.setHasFixedSize(true);
        showScheduleAdapter = new ShowScheduleAdapter(context, 2, true);
        recycleViewShowSchedule.setAdapter(showScheduleAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}


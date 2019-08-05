package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.ScheduleClassModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowScheduleViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private int shownInScreen;
    @BindView(R.id.textViewClassName)
    TextView textViewClassName;
    @BindView(R.id.textViewTrainerName)
    TextView textViewTrainerName;
    @BindView(R.id.textViewClassTime)
    TextView textViewClassTime;


    public ShowScheduleViewHolder(View view, int shownInScreen) {
        super(view);
        context = view.getContext();
        ButterKnife.bind(this, view);
        this.shownInScreen = shownInScreen;
    }

    public void bind(Object data, AdapterCallbacks adapterCallbacks,final int position) {
        if(data!=null && data instanceof ClassModel) {
            final ScheduleClassModel model = (ScheduleClassModel) data;
            textViewTrainerName.setText(model.getTrainerDetail().getFirstName());

        }
    }
}

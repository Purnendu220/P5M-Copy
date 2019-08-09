package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.ScheduleClassModel;
import com.p5m.me.utils.DateUtils;

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
    @BindView(R.id.layoutScheduler)
    CardView layoutScheduler;


    public ShowScheduleViewHolder(View view, int shownInScreen) {
        super(view);
        context = view.getContext();
        ButterKnife.bind(this, view);
        this.shownInScreen = shownInScreen;
    }

    public void bind(Object data, AdapterCallbacks adapterCallbacks, final int position) {
        if (data != null && data instanceof ScheduleClassModel) {
            final ScheduleClassModel model = (ScheduleClassModel) data;
            if (model.getTrainerDetail() != null && model.getTrainerDetail().getFirstName() != null)
                textViewTrainerName.setText(model.getTrainerDetail().getFirstName());
            else
                textViewTrainerName.setVisibility(View.GONE);
            if (model.getTitle() != null)
                textViewClassName.setText(model.getTitle());
            else
                textViewClassName.setVisibility(View.GONE);

            textViewClassTime.setText(DateUtils.getClassTime(model.getFromTime(), model.getToTime()));
            layoutScheduler.setOnClickListener(v -> {
                adapterCallbacks.onAdapterItemClick(ShowScheduleViewHolder.this, layoutScheduler, model, position);
            });
        }
    }
}

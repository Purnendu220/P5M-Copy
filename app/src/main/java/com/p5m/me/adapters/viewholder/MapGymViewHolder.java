package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.ClassModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapGymViewHolder extends RecyclerView.ViewHolder {

    private final Context context;
    private int shownInScreen;
    @BindView(R.id.textViewShowSchedule)
    public TextView textViewShowSchedule;
    @BindView(R.id.imageViewOfGym)
    public ImageView imageViewOfGym;
    @BindView(R.id.textViewClassCategory)
    public TextView textViewClassCategory;
    @BindView(R.id.textViewGymName)
    public TextView textViewGymName;
    @BindView(R.id.textViewCategoryRating)
    public TextView textViewCategoryRating;
    @BindView(R.id.textViewRatingByUsers)
    public TextView textViewRatingByUsers;

    public MapGymViewHolder(View view, int shownInScreen) {
        super(view);
        context = view.getContext();
        ButterKnife.bind(this, view);
        this.shownInScreen = shownInScreen;
    }

    public void bind(Object data, AdapterCallbacks adapterCallbacks,final int position) {
        if(data!=null && data instanceof ClassModel) {
            final ClassModel model = (ClassModel) data;

            textViewGymName.setText(model.getGymBranchDetail().getGymName());
            textViewClassCategory.setText(model.getClassCategory());

            textViewShowSchedule.setOnClickListener(v -> {
                adapterCallbacks.onAdapterItemClick(MapGymViewHolder.this, textViewShowSchedule, "", position);
            });
        }
    }
}

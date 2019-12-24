package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.ExploreGymModel;
import com.p5m.me.data.WorkoutModel;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class GymViewHolder extends RecyclerView.ViewHolder {


    private final Context context;
    private int shownInScreen;

    @BindView(R.id.imageViewClass)
    ImageView imageViewClass;
    @BindView(R.id.name)
    TextView textViewGymName;
    @BindView(R.id.textViewPriceModel)
    TextView textViewPriceModel;
    @BindView(R.id.textViewWorkoutType)
    TextView textViewWorkoutType;
    @BindView(R.id.gym_location_icon)
    ImageView gym_location_icon;


    public GymViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        gym_location_icon.setVisibility(View.VISIBLE);

    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof ExploreGymModel) {
            final ExploreGymModel model = (ExploreGymModel) data;
            itemView.setVisibility(View.VISIBLE);

            imageViewClass.setVisibility(View.VISIBLE);
            textViewPriceModel.setVisibility(View.GONE);
            textViewGymName.setVisibility(View.VISIBLE);
            textViewWorkoutType.setVisibility(View.GONE);
            textViewGymName.setText(model.getStudioName().trim());
            if (model.getProfileImage() != null)
                ImageUtils.setImage(context, model.getProfileImage(), imageViewClass);
            itemView.setOnClickListener(v -> {
                adapterCallbacks.onAdapterItemClick(GymViewHolder.this,imageViewClass,model,position);
            });
            if(position==0){
                itemView.setPadding(8,0,0,0);
            }


        } else {
            itemView.setVisibility(View.GONE);

        }
    }

}

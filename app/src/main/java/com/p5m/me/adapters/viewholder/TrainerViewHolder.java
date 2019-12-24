package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.ExploreGymModel;
import com.p5m.me.data.ExploreTrainerModel;
import com.p5m.me.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class TrainerViewHolder extends RecyclerView.ViewHolder {


    private final Context context;
    private int shownInScreen;

    @BindView(R.id.imageViewClass)
    ImageView imageViewClass;
    @BindView(R.id.name)
    TextView trainerName;
    @BindView(R.id.textViewPriceModel)
    TextView textViewPriceModel;
    @BindView(R.id.textViewWorkoutType)
    TextView textViewWorkoutType;
    @BindView(R.id.gym_location_icon)
    ImageView gym_location_icon;




    public TrainerViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        gym_location_icon.setVisibility(View.GONE);

    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof ExploreTrainerModel) {
            final ExploreTrainerModel model = (ExploreTrainerModel) data;
            itemView.setVisibility(View.VISIBLE);

            imageViewClass.setVisibility(View.VISIBLE);
            textViewPriceModel.setVisibility(View.GONE);
            textViewWorkoutType.setVisibility(View.GONE);
            trainerName.setText(model.getTrainerName());
            if (model.getProfileImage() != null)
                ImageUtils.setImage(context, model.getProfileImage(), imageViewClass);
            itemView.setOnClickListener(v -> {
                adapterCallbacks.onAdapterItemClick(TrainerViewHolder.this,imageViewClass,model,position);
            });

        } else {
            itemView.setVisibility(View.GONE);

        }
    }

}

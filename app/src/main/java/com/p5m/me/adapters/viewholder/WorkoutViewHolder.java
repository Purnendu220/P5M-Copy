package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ImageListAdapter;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.PriceModel;
import com.p5m.me.data.WorkoutModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class WorkoutViewHolder extends RecyclerView.ViewHolder {


    private final Context context;
    private int shownInScreen;

    @BindView(R.id.imageViewClass)
    ImageView imageViewClass;
    @BindView(R.id.textViewGymName)
    TextView textViewGymName;
    @BindView(R.id.trainerName)
    TextView trainerName;
    @BindView(R.id.textViewPriceModel)
    TextView textViewPriceModel;
    @BindView(R.id.textViewWorkoutType)
    TextView textViewWorkoutType;



    public WorkoutViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof WorkoutModel) {
            final WorkoutModel model = (WorkoutModel) data;
            itemView.setVisibility(View.VISIBLE);

            imageViewClass.setVisibility(View.VISIBLE);
            textViewPriceModel.setVisibility(View.GONE);
            textViewGymName.setVisibility(View.GONE);
            trainerName.setVisibility(View.GONE);
            textViewWorkoutType.setVisibility(View.VISIBLE);
           /* if(LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")) {
                textViewWorkoutType.setText(model.getName());
            }
            else*/
            textViewWorkoutType.setText(model.getName());
            if (model.getImageUrl() != null)
                ImageUtils.setImage(context, model.getImageUrl(), imageViewClass);

            itemView.setOnClickListener(v->{
                adapterCallbacks.onAdapterItemClick(WorkoutViewHolder.this,imageViewClass,model,position);
            });

        } else {
            itemView.setVisibility(View.GONE);

        }
    }

}

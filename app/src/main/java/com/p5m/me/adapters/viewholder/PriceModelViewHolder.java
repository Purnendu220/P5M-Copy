package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ExplorePagePriceListAdapter;
import com.p5m.me.data.ExploreDataList;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.data.PriceModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class PriceModelViewHolder extends RecyclerView.ViewHolder {


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


    public PriceModelViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof PriceModel) {
            final PriceModel model = (PriceModel) data;
            itemView.setVisibility(View.VISIBLE);

            imageViewClass.setVisibility(View.VISIBLE);
            textViewPriceModel.setVisibility(View.VISIBLE);
            textViewGymName.setVisibility(View.GONE);
            trainerName.setVisibility(View.GONE);
            textViewWorkoutType.setVisibility(View.GONE);
            if (model.getImageUrl() != null)
                ImageUtils.setImage(context, model.getImageUrl(), imageViewClass);
            if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar"))
                textViewPriceModel.setText(model.getArName());
            else
                textViewPriceModel.setText(model.getName());

            itemView.setOnClickListener(v -> {
                adapterCallbacks.onAdapterItemClick(PriceModelViewHolder.this, imageViewClass, model, position);
            });
        } else {
            itemView.setVisibility(View.GONE);

        }
    }

}

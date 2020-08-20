package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.ExploreGymModel;
import com.p5m.me.data.ExploreRatedClassModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ExploreRatingViewHolder extends RecyclerView.ViewHolder {


    private final Context context;
    private int shownInScreen;

    @BindView(R.id.textViewRatingCount)
    TextView textViewRatingCount;
    @BindView(R.id.textViewClassName)
    TextView textViewClassName;
    @BindView(R.id.textViewTime)
    TextView textViewTime;
    @BindView(R.id.textViewLocation)
    TextView textViewLocation;
    @BindView(R.id.imgClass)
    ImageView imageView;


    public ExploreRatingViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof ExploreRatedClassModel) {
            final ExploreRatedClassModel model = (ExploreRatedClassModel) data;
            itemView.setVisibility(View.VISIBLE);
            textViewClassName.setText(model.getTitle());
            textViewTime.setText(DateUtils.getClassTime(model.getFromTime(), model.getToTime())+" • "+ AppConstants.Currency.ARABIC_STANDARD_TIME);
            if (model.getGymBranchDetail() != null) {
                textViewLocation.setText(model.getGymBranchDetail().getGymName());
            }
            if (model.getRating() != 0.0F && model.getRating() > 0) {
                textViewRatingCount.setVisibility(View.VISIBLE);
                textViewRatingCount.setText(LanguageUtils.numberConverter(model.getRating()) + "");
            } else {
                textViewRatingCount.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(v -> adapterCallbacks.onAdapterItemClick(ExploreRatingViewHolder.this, textViewClassName, model, position));

            if (model.getClassMedia()!=null && model.getClassMedia().getMediaUrl() != null) {
                ImageUtils.setImage(context, model.getClassMedia().getMediaUrl(), imageView);
            }
        } else {
            itemView.setVisibility(View.GONE);

        }
    }

}

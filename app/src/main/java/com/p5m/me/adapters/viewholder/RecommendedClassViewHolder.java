package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigSetUp;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.WordUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecommendedClassViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.textViewRatingCount)
    public TextView textViewRatingCount;

    @BindView(R.id.textViewClassName)
    public TextView textViewClassName;


    @BindView(R.id.textViewTime)
    public TextView textViewTime;

    @BindView(R.id.textViewLocation)
    public TextView textViewLocation;

    @BindView(R.id.imgClass)
    public ImageView imgClass;






    private final Context context;
    private int shownInScreen;

    public RecommendedClassViewHolder(View view, int shownInScreen) {
        super(view);

        context = view.getContext();
        ButterKnife.bind(this, view);
        this.shownInScreen = shownInScreen;
    }

    public void bind(Object item, final AdapterCallbacks adapterCallbacks, final int position) {
        if (item != null && item instanceof ClassModel) {
            itemView.setVisibility(View.VISIBLE);
            final ClassModel model = (ClassModel) item;
            textViewClassName.setText(model.getTitle());
            textViewTime.setText(DateUtils.getClassTime(model.getFromTime(), model.getToTime()));
            if (model.getClassMedia() != null) {
                ImageUtils.setImage(context,
                        model.getClassMedia().getMediaUrl(),
                        R.drawable.class_holder, imgClass);
            } else {
                ImageUtils.clearImage(context, imgClass);
            }
            if (model.getGymBranchDetail() != null) {
                textViewLocation.setText(model.getGymBranchDetail().getGymName() + ", " + model.getGymBranchDetail().getBranchName());
            }
            if(model.getRating()!=0.0F&&model.getRating()>0){
                textViewRatingCount.setVisibility(View.VISIBLE);
                textViewRatingCount.setText(model.getRating() + "");
            }else{
                textViewRatingCount.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(RecommendedClassViewHolder.this, itemView, model, position);
                }
            });


        }else{
            itemView.setVisibility(View.GONE);
        }
    }
}
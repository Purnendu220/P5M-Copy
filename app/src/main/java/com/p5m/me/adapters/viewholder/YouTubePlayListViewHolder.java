package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.Item;
import com.p5m.me.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YouTubePlayListViewHolder extends RecyclerView.ViewHolder {
    private  Context context;
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

    @BindView(R.id.constraintViewPlayListItem)
    ConstraintLayout constraintViewPlayListItem;



    public YouTubePlayListViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);

    }

    public void bind(Object data, final AdapterCallbacks adapterCallbacks, final int position){
        if(data!=null&&data instanceof Item){
            itemView.setVisibility(View.VISIBLE);
            Item model = (Item) data;
            imageViewClass.setVisibility(View.VISIBLE);
            textViewPriceModel.setVisibility(View.GONE);
            textViewGymName.setVisibility(View.GONE);
            trainerName.setVisibility(View.GONE);
            textViewWorkoutType.setVisibility(View.VISIBLE);
            textViewWorkoutType.setText(model.getSnippet().getTitle());
            if (model.getSnippet().getThumbnails().getHigh()!=null && model.getSnippet().getThumbnails().getHigh().getUrl() != null) {
                ImageUtils.setImage(context,model.getSnippet().getThumbnails().getHigh().getUrl(), imageViewClass);
            }
            itemView.setOnClickListener(v -> adapterCallbacks.onAdapterItemClick(YouTubePlayListViewHolder.this,constraintViewPlayListItem,data,position));


        }else{
            itemView.setVisibility(View.GONE);
        }
    }
}

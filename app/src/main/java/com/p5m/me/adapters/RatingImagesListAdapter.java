package com.p5m.me.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.RatingImageViewHolder;
import com.p5m.me.data.main.MediaModel;
import com.p5m.me.utils.LogUtils;

import java.util.List;



public class RatingImagesListAdapter extends RecyclerView.Adapter<RatingImageViewHolder> {
    private static final int ITEM = 0;

    private int dp;
    private List<MediaModel> list;
    private Context context;
    private int shownIn;

    public RatingImagesListAdapter(Context context, int shownIn, List<MediaModel> mediaResponseDtoList) {
        this.context = context;
        list = mediaResponseDtoList;
        this.shownIn = shownIn;
        Fresco.initialize(context);

        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM;
    }

    @Override
    public RatingImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rating_images, parent, false);
        return new RatingImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RatingImageViewHolder holder, final int position) {
        LogUtils.debug("onBindViewHolder " + position);

        holder.bind(getItem(position),this, position, shownIn);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<MediaModel> getList() {
        return list;
    }

    public MediaModel getItem(int position) {
        if (list != null && list.size() > 0)
            return list.get(position);
        else
            return null;
    }

}
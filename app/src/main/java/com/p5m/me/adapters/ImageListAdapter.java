package com.p5m.me.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.ImageViewHolder;
import com.p5m.me.data.main.MediaModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private static final int ITEM = 0;

    private int dp;
    private List<MediaModel> list;
    private Context context;
    private int shownIn;

    public ImageListAdapter(Context context, int shownIn, List<MediaModel> mediaResponseDtoList) {
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
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = new CardView(context);
        cardView.setCardBackgroundColor(Color.BLACK);
        cardView.setRadius(dp * 4);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(dp * 96, dp * 96);
        layoutParams.setMargins(dp * 4, dp * 4, 0, dp * 4);
        cardView.setLayoutParams(layoutParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setElevation(8f);
        }
        cardView.setClipToPadding(false);
        cardView.setContentPadding(0, 0, 0, 0);

        SimpleDraweeView imageView = new SimpleDraweeView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.image_holder);

        cardView.addView(imageView);

        return new ImageViewHolder(cardView, imageView);

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_image_list_gallery, parent, false);
//        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        LogUtils.debug("onBindViewHolder " + position);

        holder.bind(getItem(position), this, position, shownIn);
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
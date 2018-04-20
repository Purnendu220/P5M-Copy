package www.gymhop.p5m.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.viewholder.ImageViewHolder;
import www.gymhop.p5m.data.main.MediaModel;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.custom.SquareImageView;

public class ImageListAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private static final int ITEM = 0;

    private int dp;
    private List<MediaModel> list;
    Context context;

    public ImageListAdapter(Context context, List<MediaModel> mediaResponseDtoList) {
        this.context = context;
        list = mediaResponseDtoList;

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

        ImageView imageView = new SquareImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.image_holder);

        cardView.addView(imageView);
        return new ImageViewHolder(cardView, imageView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        LogUtils.debug("onBindViewHolder " + position);

        holder.bind(getItem(position), this, position);
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
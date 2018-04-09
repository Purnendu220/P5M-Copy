package www.gymhop.p5m.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.viewholder.ImageViewHolder;
import www.gymhop.p5m.data.Class;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.custom.SquareImageView;

public class ImageListAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private final int imageSize, margin;

    private List<Class> list;
    Context context;

    public ImageListAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
        imageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96, context.getResources().getDisplayMetrics());
    }

    @Override
    public int getItemViewType(int position) {
//        return (position == list.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        return ITEM;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = new CardView(context);

        cardView.setCardBackgroundColor(Color.BLACK);
        cardView.setRadius(margin / 2);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(imageSize, imageSize);
        layoutParams.setMargins(margin, margin, 0, margin);
        cardView.setLayoutParams(layoutParams);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardView.setElevation(8f);
        }
        cardView.setClipToPadding(false);
        cardView.setContentPadding(0, 0, 0, 0);

        ImageView imageView = new SquareImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.drawable.info_screen_1);

        cardView.addView(imageView);
        return new ImageViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        LogUtils.debug("onBindViewHolder " + position);

        holder.bind();
    }

    @Override
    public int getItemCount() {
//        return list == null ? 0 : list.size();
//        return list.size();
        return 50;
    }

    public Class getItem(int position) {
        if (list != null && list.size() > 0)
            return list.get(position);
        else
            return null;
    }

}
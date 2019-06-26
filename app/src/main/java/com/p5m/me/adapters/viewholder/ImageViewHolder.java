package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.ImageListAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.MediaModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ImageUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ImageViewHolder extends RecyclerView.ViewHolder {

//    @BindView(R.id.simpleDraweeView)
//    SimpleDraweeView imageView;

    private ImageView imageView;

    private Context context;

    public ImageViewHolder(View itemView, ImageView imageView) {
        super(itemView);
        context = itemView.getContext();

        this.imageView = imageView;
    }

//    public ImageViewHolder(View itemView) {
//        super(itemView);
//        context = itemView.getContext();
//
//        ButterKnife.bind(this, itemView);
//    }

    public void bind(MediaModel item, final ImageListAdapter imageListAdapter, final int position, final int showIn) {
        if (item != null && item.getMediaThumbNailUrl() != null && !item.getMediaThumbNailUrl().isEmpty()) {
            itemView.setVisibility(View.VISIBLE);

//            imageView.setImageURI(Uri.parse(item.getMediaUrl()));

            ImageUtils.setImage(context, item.getMediaUrl(), R.drawable.image_holder, imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helper.openImageListViewer(context, imageListAdapter.getList(), position,AppConstants.ImageViewHolderType.GALLERY_IMAGE_HOLDER);

                    if (showIn == AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE) {
                        MixPanel.trackGymProfileEvent(AppConstants.Tracker.LOOKING_GALLERY);
                    } else if (showIn == AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE) {
                        MixPanel.trackTrainerProfileEvent(AppConstants.Tracker.LOOKING_GALLERY);
                    }
                    else if(showIn == AppConstants.AppNavigation.SHOWN_IN_RATING_LIST){
                        MixPanel.trackRatingImageView();
                    }
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

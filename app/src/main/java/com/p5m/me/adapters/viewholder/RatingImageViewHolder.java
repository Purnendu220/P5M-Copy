package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.RatingImagesListAdapter;
import com.p5m.me.data.main.MediaModel;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RatingImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewRating)
    public ImageView imageViewRating;
    private Context context;

    public RatingImageViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);

    }


    public void bind(final MediaModel item, final RatingImagesListAdapter ratingImagesListAdapter, final int position, final int showIn) {
        if (item != null &&!item.getMediaThumbNailUrl().isEmpty()) {
            itemView.setVisibility(View.VISIBLE);

            ImageUtils.setImage(context,item.getMediaThumbNailUrl(),imageViewRating);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helper.openImageListViewer(context, ratingImagesListAdapter.getList(), position,AppConstants.ImageViewHolderType.RATING_IMAGE_HOLDER);

                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}


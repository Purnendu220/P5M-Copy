package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ImageListAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.MediaModel;
import com.p5m.me.data.request.SelectedFileData;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedImageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewSelectedImage)
    public ImageView imageViewSelectedImage;
    @BindView(R.id.imageViewCross)
    public ImageView imageViewCross;
    private Context context;

    public SelectedImageViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);

    }


    public void bind(final SelectedFileData item, final int position, final int showIn, final AdapterCallbacks<Object> adapterCallbacks) {
        if (item != null &&item.getFilepath()!=null&&!item.getFilepath().isEmpty()) {
            itemView.setVisibility(View.VISIBLE);
            if(item.getFilepath().startsWith("https://")){
                ImageUtils.setImage(context,
                        item.getFilepath(),
                        R.drawable.image_holder, imageViewSelectedImage);
            }else{
                imageViewSelectedImage.setImageURI(Uri.parse(item.getFilepath()));

            }
            imageViewCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(SelectedImageViewHolder.this, imageViewCross, item, position);
                }
            });
            } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

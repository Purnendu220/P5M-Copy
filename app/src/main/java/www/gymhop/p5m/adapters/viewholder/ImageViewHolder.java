package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.ImageListAdapter;
import www.gymhop.p5m.data.main.MediaModel;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.utils.ImageUtils;

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

    public void bind(MediaModel item, final ImageListAdapter imageListAdapter, final int position) {
        if (item != null && item.getMediaThumbNailUrl() != null && !item.getMediaThumbNailUrl().isEmpty()) {
            itemView.setVisibility(View.VISIBLE);

//            imageView.setImageURI(Uri.parse(item.getMediaUrl()));

            ImageUtils.setImage(context, item.getMediaUrl(), R.drawable.image_holder, imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Helper.openImageListViewer(context, imageListAdapter.getList(), position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

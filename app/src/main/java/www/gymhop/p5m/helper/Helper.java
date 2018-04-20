package www.gymhop.p5m.helper;

import android.content.Context;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.List;

import www.gymhop.p5m.R;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.MediaModel;
import www.gymhop.p5m.data.main.TrainerDetailModel;
import www.gymhop.p5m.data.main.TrainerModel;

/**
 * Created by Varun John on 4/20/2018.
 */

public class Helper {

    public static void setJoinButton(Context context, Button buttonJoin, ClassModel model) {
        if (model.isUserJoinStatus()) {
            buttonJoin.setText(context.getString(R.string.booked));
            buttonJoin.setBackgroundResource(R.drawable.joined_rect);
        } else if (model.getAvailableSeat() == model.getTotalSeat()) {
            buttonJoin.setText(context.getString(R.string.full));
            buttonJoin.setBackgroundResource(R.drawable.full_rect);
        } else {
            buttonJoin.setText(context.getString(R.string.book));
            buttonJoin.setBackgroundResource(R.drawable.join_rect);
        }
    }

    public static void setFavButton(Context context, Button buttonFav, TrainerModel model) {
        if (model.isIsfollow()) {
            buttonFav.setText(context.getString(R.string.favorited));
            buttonFav.setBackgroundResource(R.drawable.joined_rect);
        } else {
            buttonFav.setText(context.getString(R.string.favourite));
            buttonFav.setBackgroundResource(R.drawable.join_rect);
        }
    }

    public static void setFavButton(Context context, Button buttonFav, TrainerDetailModel model) {
        if (model.isIsfollow()) {
            buttonFav.setText(context.getString(R.string.favorited));
            buttonFav.setBackgroundResource(R.drawable.joined_rect);
        } else {
            buttonFav.setText(context.getString(R.string.favourite));
            buttonFav.setBackgroundResource(R.drawable.join_rect);
        }
    }

    public static void openImageListViewer(Context context, List<MediaModel> list, int position) {
        Fresco.initialize(context);

        GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                .setFailureImage(R.drawable.image_holder)
                .setPlaceholderImage(R.drawable.loading);

        new ImageViewer.Builder<>(context, list)
                .setStartPosition(position)
                .hideStatusBar(false)
                .setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                .setFormatter(new ImageViewer.Formatter<MediaModel>() {
                    @Override
                    public String format(MediaModel model) {
                        return model.getMediaUrl();
                    }
                })
                .show();
    }

    public static void openImageViewer(Context context, String url) {
        Fresco.initialize(context);

        GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                .setFailureImage(R.drawable.image_holder)
                .setPlaceholderImage(R.drawable.loading);

        new ImageViewer.Builder<>(context, new String[]{url})
                .hideStatusBar(false)
                .setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                .show();
    }
}

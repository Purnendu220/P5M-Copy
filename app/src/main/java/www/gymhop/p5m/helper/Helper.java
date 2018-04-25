package www.gymhop.p5m.helper;

import android.content.Context;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.lang.reflect.Field;
import java.util.List;

import www.gymhop.p5m.R;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.MediaModel;
import www.gymhop.p5m.data.main.TrainerDetailModel;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.data.temp.GymDetailModel;
import www.gymhop.p5m.utils.LogUtils;

/**
 * Created by Varun John on 4/20/2018.
 */

public class Helper {

    public static void setJoinButton(Context context, Button buttonJoin, ClassModel model) {
        if (model.isUserJoinStatus()) {
            buttonJoin.setText(context.getString(R.string.booked));
            buttonJoin.setBackgroundResource(R.drawable.joined_rect);
        } else if (model.getAvailableSeat() == 0) {
            buttonJoin.setText(context.getString(R.string.full));
            buttonJoin.setBackgroundResource(R.drawable.full_rect);
        } else {
            buttonJoin.setText(context.getString(R.string.book));
            buttonJoin.setBackgroundResource(R.drawable.join_rect);
        }
    }

    public static void setJoinStatusProfile(Context context, TextView view, ClassModel model) {
        if (model.isUserJoinStatus()) {
            view.setText(context.getString(R.string.booked));
            view.setBackgroundResource(R.drawable.theme_bottom_text_button_booked);
            view.setEnabled(false);
        } else if (model.getAvailableSeat() == 0) {
            view.setText(context.getString(R.string.full));
            view.setBackgroundResource(R.drawable.theme_bottom_text_button_full);
            view.setEnabled(false);
        } else {
            view.setText(context.getString(R.string.reserve_class));
            view.setBackgroundResource(R.drawable.theme_bottom_text_button_book);
            view.setEnabled(true);
        }
    }

    public static void setFavButton(Context context, Button buttonFav, TrainerModel model) {
        setFavButton(context, buttonFav, model.isIsfollow());
    }

    public static void setFavButton(Context context, Button buttonFav, TrainerDetailModel model) {
        setFavButton(context, buttonFav, model.isIsfollow());
    }

    public static void setFavButton(Context context, Button buttonFav, GymDetailModel model) {
        setFavButton(context, buttonFav, model.isIsfollow());
    }

    private static void setFavButton(Context context, Button buttonFav, boolean isFollow) {
        if (isFollow) {
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

    public static void showPopMenu(Context context, View view, PopupMenu.OnMenuItemClickListener onMenuItemClickListener) {
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.action_profile, popup.getMenu());

        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popup);
            argTypes = new Class[]{boolean.class};
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            LogUtils.exception(e);
        }

        popup.setOnMenuItemClickListener(onMenuItemClickListener);
        popup.show();
    }

}

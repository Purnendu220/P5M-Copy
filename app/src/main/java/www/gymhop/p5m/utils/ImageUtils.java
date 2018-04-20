package www.gymhop.p5m.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Varun John on 4/11/2018.
 */

public class ImageUtils {

    public static String generateMapImageUrl(double latitude, double longitude) {
        return "https://maps.googleapis.com/maps/api/staticmap?zoom=13&size=200x240" +
                "&maptype=roadmap&markers=color:red%7C" + latitude + "," + longitude + "&key=AIzaSyCrAO08EU2sNMocuMZv03sSkOj6NQrOzSs";
    }

    public static void setImage(Context context, String url, int placeHolder, ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .placeholder(placeHolder)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(placeHolder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .placeholder(placeHolder)
                    .into(imageView);
        }
    }

    public static void setImage(Context context, int url, int placeHolder, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .placeholder(placeHolder)
                .into(imageView);
    }
}

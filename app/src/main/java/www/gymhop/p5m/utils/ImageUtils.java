package www.gymhop.p5m.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Varun John on 4/11/2018.
 */

public class ImageUtils {

    public static String generateMapImageUrlClassDetail(double latitude, double longitude) {
        return "https://maps.googleapis.com/maps/api/staticmap?zoom=13&size=200x240" +
                "&maptype=roadmap&scale=2&markers=color:red%7C" + latitude + "," + longitude + "&key=AIzaSyCrAO08EU2sNMocuMZv03sSkOj6NQrOzSs";
    }

    public static String generateMapImageUrGymDetail(double latitude, double longitude) {
        return "https://maps.googleapis.com/maps/api/staticmap?zoom=13&size=500x240" +
                "&maptype=roadmap&scale=2&markers=color:red%7C" + latitude + "," + longitude + "&key=AIzaSyCrAO08EU2sNMocuMZv03sSkOj6NQrOzSs";
    }

    public static void setImage(Context context, String url, int placeHolder, ImageView imageView) {
//        if (url != null && !url.isEmpty()) {
//            Glide.with(context)
//                    .load(url)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .crossFade()
//                    .placeholder(placeHolder)
//                    .into(imageView);
//        } else {
//            clearImage(context, imageView);
//        }
    }

    public static void setImage(Context context, String url, ImageView imageView) {
//        if (url != null && !url.isEmpty()) {
//            Glide.with(context)
//                    .load(url)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .crossFade()
//                    .into(imageView);
//        } else {
//            clearImage(context, imageView);
//        }
    }

    public static void setImageDelay(final Context context, String url, final ImageView imageView) {
//        if (url != null && !url.isEmpty()) {
//            Glide.with(context)
//                    .load(url)
//                    .fitCenter()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(new SimpleTarget<Drawable>() {
//
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                            imageView.setImageDrawable(resource);
//                        }
//                    });
//        } else {
//            clearImage(context, imageView);
//        }
    }

    public static void clearImage(Context context, ImageView imageView) {
        Glide.with(context).clear(imageView);
    }

    public static void setImage(Context context, int url, int placeHolder, ImageView imageView) {
//        Glide.with(context)
//                .load(url)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .crossFade()
//                .placeholder(placeHolder)
//                .into(imageView);
    }

    public static String getImagePathFromUri(Context context, Uri uri) {

        if (uri != null) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);

            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                return cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return "";
    }
}

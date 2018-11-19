package com.p5m.me.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.p5m.me.helper.GlideApp;


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
        if (url != null && !url.isEmpty()) {
            GlideApp.with(context)
                    .load(url).transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeHolder)
                    .into(imageView);
        } else {
            clearImage(context, imageView);
        }
    }

    public static void setImage(Context context, String url, ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            GlideApp.with(context)
                    .load(url).transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } else {
            clearImage(context, imageView);
        }
    }

    public static void setImageDelay(final Context context, String url, final ImageView imageView) {
        if (url != null && !url.isEmpty()) {
            GlideApp.with(context)
                    .load(url)
                    .fitCenter().transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Drawable>() {

                        @Override
                        public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {
                            imageView.setImageDrawable(resource);
                        }
                    });
        } else {
            clearImage(context, imageView);
        }
    }

    public static void clearImage(Context context, ImageView imageView) {
        GlideApp.with(context).clear(imageView);
    }

    public static void setImage(Context context, int url, int placeHolder, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .centerCrop().transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache( true )
                .placeholder(placeHolder)
                .into(imageView);
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

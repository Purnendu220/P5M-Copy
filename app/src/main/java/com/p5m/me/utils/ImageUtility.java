package com.p5m.me.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;


import com.fxn.pix.Pix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Mobikasa on 7/26/2016.
 */
public class ImageUtility {

    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_GALLERY = 2;
    public static final  int IMAGE_SELECTION_LIMIT=4;
    public static int positon=-1;

    private static Animator mCurrentAnimator;
    View rootView;
    private static int mShortAnimationDuration;

    public static void showAddPictureAlert(final Context context, final int numberOfImage) {

        final CharSequence[] Type = {"Take New Photo", "Choose Existing", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add picture");
        builder.setItems(Type, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                boolean result = Utility.checkPermission(context);
                positon = pos;
                if (pos == 0) {
                    if (result)
                        ImageUtility.openCamera(context);
                } else if (pos == 1) {
                    if (result)
                        ImageUtility.openGallery(context,numberOfImage);
                }
            }
        });
        builder.show();
    }
    public static void openCameraGallery(final Context context, final int numberOfImage) {
        boolean result = Utility.checkPermission(context);
          if(result){
              Pix.start((AppCompatActivity) context,REQUEST_CODE_GALLERY, numberOfImage);
          }

    }




    public static final String APP_DIR_MAIN = "p5m/images/";
    public static final String FILE_NAME = "pic_temp";

    public static void openCamera(Context context) {
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            File file = new File(createDirectory(), FILE_NAME + ".png");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent.putExtra("android.intent.extras.LENS_FACING_FRONT", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            } else {
                intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
            //intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
             ((Activity) context).startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }

    public static void openGallery(Context context,int numberOfImage) {
       // Pix.start((AppCompatActivity) context,REQUEST_CODE_GALLERY, numberOfImage);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }


    public static File createDirectory() {
        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isSDPresent && isSDCardWritable()) {
            File mainDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), APP_DIR_MAIN);
            if (!mainDir.exists()) {
                mainDir.mkdirs();
            }
            return mainDir;
        } else {
            File mainDir = new File(Environment.getRootDirectory().getAbsolutePath(), APP_DIR_MAIN);
            if (!mainDir.exists()) {
                mainDir.mkdirs();
            }
            return mainDir;
        }
    }

    public static boolean isSDCardWritable() {
        String status = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(status)) {
            return true;
        }
        return false;
    }

    public static String compressImage(String filePath) {
        String filename;
        try {
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

//      setting inSampleSize value allows to load a scaled down version of the original image
            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
//          load the bitmap from its path
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileOutputStream out = null;
            filename = getFilename();
            try {
                out = new FileOutputStream(filename);
//          write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return filename;

        } catch (Exception e) {
            return null;
        }


    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(),"IMAGE FILE");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static String getRealPathFromURI(Uri contentUri, Context context) {
        // can post image
        Log.e("Uri", contentUri + "");
        String[] proj = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = ((Activity) context).managedQuery(contentUri, proj, // Which
                // columns
                // to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public static Uri getImageURIForLollyPop(Uri selectedImage) {
        if (Build.VERSION.SDK_INT == 22) {
            if (selectedImage != null && selectedImage.toString().length() > 0) {
                try {
                    final String extractUriFrom = selectedImage.toString();
                    String firstExtraction = extractUriFrom.contains("com.google.android.apps.photos.contentprovider") ? extractUriFrom.split("/1/")[1] : extractUriFrom;
                    firstExtraction = firstExtraction.contains("/ACTUAL") ? firstExtraction.replace("/ACTUAL", "").toString() : firstExtraction;

                    String secondExtraction = URLDecoder.decode(firstExtraction, "UTF-8");
                    return Uri.parse(secondExtraction);
                } catch (UnsupportedEncodingException e) {

                } catch (Exception e) {

                }
            }
        } else {
            return selectedImage;
        }
        return null;
    }

    public static Bitmap createScaledImage(File mFile) {
        Bitmap bitmap = null;
        ExifInterface exif;
        try {
            exif = new ExifInterface(mFile.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix mat = new Matrix();
            mat.postRotate(angle);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(mFile), null, options);
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
        } catch (Exception e) {
            LogUtils.exception(e);
        }
        if (bitmap != null)
            return bitmap;

        else
            return null;
    }

    public static File showGalleryImage(Context mContext, Uri selectedImage) {
        File file = ImageUtility.getFileFromUri(mContext, selectedImage);
        return file;
    }

    public static File getFileFromUri(Context context, Uri uri) {

        if (uri == null) {
            return null;
        }
        File file = null;

        if (uri.toString().contains("content")) {
            String fileName = getRealPathFromURI(uri, context);
            file = new File(fileName);
            return file;
        }

        String[] filePath = {MediaStore.Images.Media.DATA};

        Cursor c = context.getContentResolver().query(uri, filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String picturePath = c.getString(columnIndex);
        file = new File(picturePath);
        c.close();


        return file;
    }




}

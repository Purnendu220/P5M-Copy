package com.p5m.me.view.custom;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;

import com.p5m.me.R;

import com.p5m.me.utils.ImageUtils;

import com.p5m.me.view.fragment.BaseFragment;
import com.p5m.me.view.fragment.ViewPagerFragmentSelection;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaGalleryFragment extends BaseFragment implements ViewPagerFragmentSelection {

    @BindView(R.id.imageViewImage)
    ImageView imageViewImage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    int mViewHolder=R.drawable.class_holder;


    public MediaGalleryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_gallery, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String uri = getArguments().getString("uri", "");
        String viewHolder = getArguments().getString("viewHolder", "");



        if (uri == null && !uri.isEmpty()) {
            return;
        }
        try{
            if (viewHolder != null && !viewHolder.isEmpty()) {
                switch (viewHolder){
                    case "CLASS_IMAGE_HOLDER":
                        mViewHolder=R.drawable.class_holder;
                        break;
                    case "PROFILE_IMAGE_HOLDER":
                        mViewHolder=R.drawable.profile_holder;
                        break;
                    case "GALLERY_IMAGE_HOLDER":
                        mViewHolder=R.drawable.class_holder;
                        break;
                    case "RATING_IMAGE_HOLDER":
                        mViewHolder=R.drawable.class_holder;
                        break;
                    case "COVER_IMAGE_HOLDER":
                        mViewHolder=R.drawable.class_holder;
                        break;
                    default:
                        mViewHolder=R.drawable.class_holder;

                        break;

                }
            }
        }catch (Exception e){

        }






        progressBar.setVisibility(View.GONE);
        ImageUtils.setImage(context,
                uri,
                mViewHolder, imageViewImage);
        imageViewImage.setOnTouchListener(new ImageMatrixTouchHandler(context));


//        RequestBuilder<Bitmap> requestBuilder = GlideApp.with(context).asBitmap().load(uri)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .fitCenter().transition(BitmapTransitionOptions.withCrossFade());
//        requestBuilder.preload(1080, 1080);
//        requestBuilder.into(new SimpleTarget<Bitmap>() {
//
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        progressBar.setVisibility(View.GONE);
//                        try{
//                            imageViewImage.setImageBitmap(getResizedBitmap(resource,(resource.getHeight()*50)/100,(resource.getWidth()*50)/100));
//                            imageViewImage.setOnTouchListener(new ImageMatrixTouchHandler(context));
//                        }catch (Exception e){
//                            e.printStackTrace();
//                            ToastUtils.show(context,getString(R.string.unable_to_load));
//                        }
//
//                    }
//
//                });



    }

//    public void setTransitionName(String imageViewTransition) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            imageViewImage.setTransitionName(imageViewTransition);
//        }
//    }

    @Override
    public void onTabSelection(int position) {

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {  try{
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }catch (Exception e){
       return null;
    }

    }
}

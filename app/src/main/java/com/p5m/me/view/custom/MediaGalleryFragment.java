package com.p5m.me.view.custom;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.p5m.me.R;
import com.p5m.me.helper.GlideApp;
import com.p5m.me.view.fragment.BaseFragment;
import com.p5m.me.view.fragment.ViewPagerFragmentSelection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaGalleryFragment extends BaseFragment implements ViewPagerFragmentSelection {

    @BindView(R.id.imageViewImage)
    ImageView imageViewImage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

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

        if (uri == null && !uri.isEmpty()) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

//        GlideApp.with(context)
//                .load(R.drawable.placeholder_glide)
//                .into(imageViewImageLoader);

//        GlideApp.with(context).load(uri)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .fitCenter().transition(DrawableTransitionOptions.withCrossFade())
//                .into(new SimpleTarget<Drawable>() {
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        progressBar.setVisibility(View.GONE);
//                        imageViewImage.setImageDrawable(resource);
//                        imageViewImage.setOnTouchListener(new ImageMatrixTouchHandler(context));
//                    }
//                });

        RequestBuilder<Drawable> requestBuilder = GlideApp.with(context).asDrawable().load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter().transition(DrawableTransitionOptions.withCrossFade());
        requestBuilder.preload(1080, 1080);
        requestBuilder.into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        progressBar.setVisibility(View.GONE);
                        imageViewImage.setImageDrawable(resource);
                        imageViewImage.setOnTouchListener(new ImageMatrixTouchHandler(context));
                    }
                });
    }

//    public void setTransitionName(String imageViewTransition) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            imageViewImage.setTransitionName(imageViewTransition);
//        }
//    }

    @Override
    public void onTabSelection(int position) {

    }
}

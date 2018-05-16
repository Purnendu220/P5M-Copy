package www.gymhop.p5m.view.custom;


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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.helper.GlideApp;
import www.gymhop.p5m.view.fragment.BaseFragment;
import www.gymhop.p5m.view.fragment.ViewPagerFragmentSelection;

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

        GlideApp.with(context).load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(new SimpleTarget<Drawable>() {
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

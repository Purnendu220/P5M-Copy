package www.gymhop.p5m.view.custom;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.view.fragment.BaseFragment;
import www.gymhop.p5m.view.fragment.ViewPagerFragmentSelection;

public class MediaGalleryFragment extends BaseFragment implements ViewPagerFragmentSelection {

    @BindView(R.id.imageViewImage)
    ImageView imageViewImage;
    @BindView(R.id.imageViewImageLoader)
    ImageView imageViewImageLoader;

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

        imageViewImageLoader.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(R.drawable.placeholder_glide)
                .asGif()
                .crossFade()
                .into(imageViewImageLoader);

        Glide.with(context).load(uri)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imageViewImageLoader.setVisibility(View.GONE);
                        imageViewImage.setImageDrawable(resource);
                        return true;
                    }
                })
                .into(imageViewImage);

        imageViewImage.setOnTouchListener(new ImageMatrixTouchHandler(context));
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

package com.p5m.me.view.custom;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    ImageView imageView;

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.textViewCounter)
    TextView textViewCounter;
    @BindView(R.id.root)
    View root;

    private GalleryMediaSlider viewPagerAdapter;
    public static List<String> images = new ArrayList<>();
    public static int DEFAULT_SELECTED_POSITION;
    public static String mViewHolderType;

    private Animation slideUpAnimation, slideDownAnimation;

    public static void openActivity(Context context, Activity activity, View sharedElement, int selectedPosition, List<String> images,String viewHolderType) {
        GalleryActivity.DEFAULT_SELECTED_POSITION = selectedPosition;
        GalleryActivity.images = images;
        GalleryActivity.mViewHolderType = viewHolderType;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            sharedElement.setTransitionName("imageViewTransition");
//        }
//
//        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElement, "searchIcon");
//        context.startActivity(new Intent(context, GalleryActivity.class), activityOptionsCompat.toBundle());

        context.startActivity(new Intent(context, GalleryActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        showActionBar = false;
        super.onCreate(savedInstanceState);

//        ActivityCompat.postponeEnterTransition(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery);

        ButterKnife.bind(activity);

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                android.R.anim.fade_in);

        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                android.R.anim.fade_out);

        startSlideUpAnimation();

        viewPagerAdapter = new GalleryMediaSlider(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        viewPager.setCurrentItem(DEFAULT_SELECTED_POSITION);

        viewPager.addOnPageChangeListener(this);

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(DEFAULT_SELECTED_POSITION);
                viewPager.setCurrentItem(DEFAULT_SELECTED_POSITION);

//                viewPager.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            try {
//                                viewPagerAdapter.mediaGalleryFragments.get(viewPager.getCurrentItem()).setTransitionName("imageViewTransition");
//                                ActivityCompat.startPostponedEnterTransition(activity);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
            }
        });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {
        if (images.size() <= 1) {
            textViewCounter.setVisibility(View.GONE);
        } else {
            textViewCounter.setVisibility(View.VISIBLE);
            textViewCounter.setText( LanguageUtils.numberConverter(viewPager.getCurrentItem() +1) + "/" + LanguageUtils.numberConverter(images.size()));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class GalleryMediaSlider extends FragmentStatePagerAdapter {
        public List<MediaGalleryFragment> mediaGalleryFragments;

        public GalleryMediaSlider(FragmentManager fm) {
            super(fm);
            mediaGalleryFragments = new ArrayList<>(images.size());
            for (int size = 0; size < images.size(); size++) {
                mediaGalleryFragments.add(size, null);
            }
        }

        @Override
        public MediaGalleryFragment getItem(int position) {
            MediaGalleryFragment galleryMediaFragment = new MediaGalleryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("uri", images.get(position));
            bundle.putString("viewHolder",GalleryActivity.mViewHolderType);
            galleryMediaFragment.setArguments(bundle);
            mediaGalleryFragments.set(position, galleryMediaFragment);
            return galleryMediaFragment;
        }

        @Override
        public int getCount() {
            return images.size();
        }
    }

    @Override
    public void onBackPressed() {
        startSlideDownAnimation();
    }

    public void startSlideUpAnimation() {

        textViewCounter.setVisibility(View.INVISIBLE);

        int colorFrom = ContextCompat.getColor(context, R.color.layer);
        int colorTo = Color.BLACK;
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(800);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                root.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();

        viewPager.startAnimation(slideUpAnimation);

        slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textViewCounter.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    boolean isPerformingSlideDownAnimation;

    public void startSlideDownAnimation() {
        if (isPerformingSlideDownAnimation) {
            return;
        }

        finish();
        overridePendingTransition(0, 0);

        isPerformingSlideDownAnimation = true;
        textViewCounter.setVisibility(View.INVISIBLE);
    }
}

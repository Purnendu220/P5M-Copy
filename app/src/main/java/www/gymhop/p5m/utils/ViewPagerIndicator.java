package www.gymhop.p5m.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class ViewPagerIndicator {

    private ViewPager viewPager;
    private int selectedImageResource, unselectedImageResource;
    private int indicatorCount;
    private ViewGroup indicatorView;
    private List<ImageView> indicatorList;
    private int indicatorPosition;

    public void setup(Context context, ViewPager viewPager, ViewGroup indicatorView, int selectedImageResource, int unselectedImageResource) {
        this.viewPager = viewPager;
        this.indicatorView = indicatorView;
        this.selectedImageResource = selectedImageResource;
        this.unselectedImageResource = unselectedImageResource;

        indicatorCount = viewPager.getAdapter().getCount();
        indicatorList = new ArrayList<>(indicatorCount);

        final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());
        int indicatorSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(padding, padding, padding, padding);

        indicatorView.addView(linearLayout);

        for (int count = 0; count < indicatorCount; count++) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(indicatorSize, indicatorSize));
            imageView.setPadding(padding, padding, padding, padding);
            linearLayout.addView(imageView);
            indicatorList.add(imageView);
        }

        indicatorPosition = viewPager.getCurrentItem();
        setIndicator(indicatorPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                indicatorPosition = position;
                setIndicator(indicatorPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setIndicator(int position) {
        for (int index = 0; index < indicatorList.size(); index++) {
            ImageView indicator = indicatorList.get(index);
            if (index == position)
                indicator.setImageResource(selectedImageResource);
            else
                indicator.setImageResource(unselectedImageResource);
        }
    }
}

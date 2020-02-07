package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.inappmessaging.model.BannerMessage;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.BannerAdapter;
import com.p5m.me.data.BannerData;
import com.p5m.me.data.ExploreDataList;
import com.p5m.me.data.ExploreGymModel;
import com.p5m.me.utils.ViewPagerIndicator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class BannerViewHolder extends RecyclerView.ViewHolder implements ViewPager.OnPageChangeListener{


    private final Context context;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.layoutIndicator)
    public LinearLayout layoutIndicator;


    public BannerViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind(final List<BannerData> data, int shownInScreen, final AdapterCallbacks adapterCallbacks, final int position) {
        if (data != null  && data.size()>0) {
            itemView.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            layoutIndicator.setVisibility(View.VISIBLE);
            layoutIndicator.removeAllViews();
             // Pager Setup..
            BannerAdapter bannerAdapter = new BannerAdapter(context,data,adapterCallbacks);
            viewPager.setAdapter(bannerAdapter);

            // Indicator setup..
            new ViewPagerIndicator(context, ViewPagerIndicator.STYLE_NORMAL).setup(viewPager, layoutIndicator, R.drawable.circle_white, R.drawable.circle_grey);

        }else{
            itemView.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            layoutIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
       /* if (position == viewPager.getAdapter().getCount() - 1) {
            buttonRegister.setBackgroundResource(R.drawable.join_rect);
            buttonRegister.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            buttonRegister.setBackgroundResource(R.drawable.button_white);
            buttonRegister.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
        }*/
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

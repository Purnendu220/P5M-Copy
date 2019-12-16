package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.p5m.me.R;
import com.p5m.me.data.BannerData;
import com.p5m.me.utils.ImageUtils;

import java.util.List;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class BannerAdapter extends PagerAdapter {
    private final List<BannerData> bannerDataList;
    private LayoutInflater layoutInflater;

    private Context context;


    public BannerAdapter(Context context, List<BannerData> model) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bannerDataList = model;
        this.context = context;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.view_banner_screens, container, false);
        container.addView(view);
            BannerData bannerData = (BannerData) bannerDataList.get(position);
            ImageView image = view.findViewById(R.id.image);
            if (bannerData.getImagePath() != null)
                ImageUtils.setImage(context, bannerData.getImagePath(), image);
            return view;

    }

    @Override
    public int getCount() {
        return bannerDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}

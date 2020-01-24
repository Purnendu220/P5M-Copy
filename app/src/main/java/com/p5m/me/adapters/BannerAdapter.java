package com.p5m.me.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.p5m.me.R;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BannerData;
import com.p5m.me.notifications.HandleNotificationDeepLink;
import com.p5m.me.utils.ImageUtils;

import java.util.List;

import static com.p5m.me.utils.AppConstants.ExploreViewType.BANNER_CAROUSAL_VIEW;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class BannerAdapter extends PagerAdapter {
    private final List<BannerData> bannerDataList;
    private final AdapterCallbacks adapterCallbacks;
    private LayoutInflater layoutInflater;

    private Context context;


    public BannerAdapter(Context context, List<BannerData> model, AdapterCallbacks adapterCallbacks) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bannerDataList = model;
        this.context = context;
        this.adapterCallbacks = adapterCallbacks;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.view_banner_screens, container, false);
        container.addView(view);
        BannerData bannerData = (BannerData) bannerDataList.get(position);
        ImageView image = view.findViewById(R.id.image);
        image.setOnClickListener(v -> {
            if (bannerDataList.get(position).getUrl() != null && !TextUtils.isEmpty(bannerDataList.get(position).getUrl())) {
                onBannerClick(position);
                MixPanel.trackExplore(BANNER_CAROUSAL_VIEW, bannerDataList.get(position).getUrl());
            }

        });
        if (bannerData.getImagePath() != null)
            ImageUtils.setImage(context, bannerData.getImagePath(), image);
        return view;

    }

    private void onBannerClick(int position) {
        Intent navigationIntent = HandleNotificationDeepLink.handleNotificationDeeplinking(context, bannerDataList.get(position).getUrl());
        context.startActivity(navigationIntent);

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

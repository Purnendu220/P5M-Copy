package com.p5m.me.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.p5m.me.R;
import com.p5m.me.data.InfoScreenData;
import com.p5m.me.helper.GlideApp;
import com.p5m.me.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class InfoScreenAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    private List<InfoScreenData> infoScreenDataList;
    private Context context;

    public InfoScreenAdapter(Context context, Activity activity, List<InfoScreenData> infoScreenDataList) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.infoScreenDataList = infoScreenDataList;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.view_info_screens, container, false);
        container.addView(view);

        ImageView image = view.findViewById(R.id.image);
        TextView text = view.findViewById(R.id.text);
        InfoScreenData infoScreenData = infoScreenDataList.get(position);

        if (infoScreenData.image != null){
            GlideApp.with(context)
                    .load( infoScreenData.image)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image);
        }
//            ImageUtils.setImage(context, infoScreenData.image, image);
        else
            image.setImageResource(infoScreenData.resource);

        text.setText(infoScreenData.text);

        return view;
    }

    @Override
    public int getCount() {
        return infoScreenDataList.size();
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

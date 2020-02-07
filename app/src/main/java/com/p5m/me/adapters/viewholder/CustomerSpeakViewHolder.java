package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.images.WebImage;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.BannerAdapter;
import com.p5m.me.adapters.CustomerSpeakAdapter;
import com.p5m.me.data.BannerData;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.CustomerSpeaksData;
import com.p5m.me.data.ExploreDataList;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class CustomerSpeakViewHolder extends RecyclerView.ViewHolder implements ViewPager.OnPageChangeListener {


    private final Context context;
    private Gson gson;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.textViewCustomerSpeaks)
    TextView textViewHeader;
    @BindView(R.id.layoutIndicator)
    public LinearLayout layoutIndicator;
    @BindView(R.id.layoutTestinomials)
    public ConstraintLayout layoutTestinomials;
    @BindView(R.id.view)
    public View view;
    private List<CustomerSpeaksData> testominalList;

    public CustomerSpeakViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        gson = new Gson();
        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, int shownInScreen, final AdapterCallbacks adapterCallbacks, final int position) {


        if (data != null && data instanceof ExploreDataModel) {
            final ExploreDataModel model = (ExploreDataModel) data;
            if (model.getData() != null) {
                ExploreDataList exploreDataList = new ExploreDataList(model.getData());
                String listString = convertorToModelClassList(exploreDataList);
                testominalList = gson.fromJson(listString, new TypeToken<List<CustomerSpeaksData>>() {
                }.getType());
            }
            if (testominalList != null && testominalList.size() > 0) {
                itemView.setVisibility(View.VISIBLE);
                itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                CustomerSpeakAdapter customerSpeakAdapter = new CustomerSpeakAdapter(context, testominalList);
                viewPager.setAdapter(customerSpeakAdapter);
                if (model.getHeader() != null) {
                    if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")) {
                        if (!TextUtils.isEmpty(model.getHeader().getTitleAr()))
                            textViewHeader.setText(model.getHeader().getTitleAr());
                        else
                            textViewHeader.setVisibility(View.GONE);
                    } else {
                        if (!TextUtils.isEmpty(model.getHeader().getTitle()))
                            textViewHeader.setText(model.getHeader().getTitle());
                        else
                            textViewHeader.setVisibility(View.GONE);

                    }

                }
                layoutIndicator.removeAllViews();
                // Indicator setup..
                new ViewPagerIndicator(context, ViewPagerIndicator.STYLE_NORMAL).setup(viewPager, layoutIndicator, R.drawable.circle_black, R.drawable.circle_white);
                if (model.isShowDivider()) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layoutTestinomials.getLayoutParams();
                    params.topMargin = 10;
                }
            } else {
                itemView.setVisibility(View.GONE);
                itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        } else {
            itemView.setVisibility(View.GONE);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }

    }

    private String convertorToModelClassList(ExploreDataList exploreDataList) {
        return gson.toJson(
                exploreDataList.getexploreDataList(),
                new TypeToken<List<LinkedTreeMap>>() {
                }.getType());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
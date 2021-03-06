package com.p5m.me.view.activity.LoginRegister;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.p5m.me.R;
import com.p5m.me.adapters.InfoScreenAdapter;
import com.p5m.me.data.FAQ;
import com.p5m.me.data.InfoScreenData;
import com.p5m.me.data.OnBoardingData;
import com.p5m.me.data.main.StoreApiModel;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.utils.ViewPagerIndicator;
import com.p5m.me.view.activity.Main.GetStartedActivity;
import com.p5m.me.view.activity.Main.LocationSelectionActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoScreen extends BaseActivity implements ViewPager.OnPageChangeListener, NetworkCommunicator.RequestListener {

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.layoutIndicator)
    public LinearLayout layoutIndicator;

    @BindView(R.id.buttonRegister)
    public Button buttonRegister;

    private InfoScreenAdapter infoScreenAdapter;
    private List<OnBoardingData> onBoardingDataList;

    public static void open(Context context) {
        Intent intent = new Intent(context, InfoScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
//        context.startActivity(new Intent(context, InfoScreen.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_screen);

        ButterKnife.bind(activity);

        setViewPagerAdapter();
//        setDefaultViewPagerAdapter();
        callApi();
        viewPager.addOnPageChangeListener(this);
    }

    private void setViewPagerAdapter() {
        try {

            String onBoardingDataValue = RemoteConfigConst.ON_BOARDING_DATA_VALUE;
            if (onBoardingDataValue != null && !onBoardingDataValue.isEmpty()) {
                Gson g = new Gson();
                List<OnBoardingData> p = g.fromJson(onBoardingDataValue, new TypeToken<List<OnBoardingData>>() {
                }.getType());
                onBoardingDataList = p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (onBoardingDataList == null) {
            setDefaultViewPagerAdapter();
            return;
        } else {
            if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar") &&
                    !TextUtils.isEmpty(onBoardingDataList.get(0).getText_ar())) {
                setViewPager();
            } else if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("en") &&
                    !TextUtils.isEmpty(onBoardingDataList.get(0).getText_en())) {
                setViewPager();
            } else {
                setDefaultViewPagerAdapter();
            }
        }
    }

    private void callApi() {
        networkCommunicator.getStoreData(this, false);
    }

    private void setViewPager() {
        List<InfoScreenData> infoScreenDataList = new ArrayList<InfoScreenData>();
        if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")) {
            for (OnBoardingData data : onBoardingDataList) {
                infoScreenDataList.add(new InfoScreenData(data.getText_ar(), data.getImage()));
            }
        } else {
            for (OnBoardingData data : onBoardingDataList) {
                infoScreenDataList.add(new InfoScreenData(data.getText_en(), data.getImage()));
            }
        }
        // Pager Setup..

        infoScreenAdapter = new InfoScreenAdapter(context, activity, infoScreenDataList);
        viewPager.setAdapter(infoScreenAdapter);

        // Indicator setup..
        new ViewPagerIndicator(context, ViewPagerIndicator.STYLE_SMALL).setup(viewPager, layoutIndicator, R.drawable.circle_black, R.drawable.circle_grey);

    }

    private void setDefaultViewPagerAdapter() {
        // Create data..
        List<InfoScreenData> infoScreenDataList = new ArrayList<>(4);
        infoScreenDataList.add(new InfoScreenData(context.getString(R.string.info_screen_desc_1), R.drawable.info_screen_1));
        infoScreenDataList.add(new InfoScreenData(context.getString(R.string.info_screen_desc_2), R.drawable.info_screen_2));
        infoScreenDataList.add(new InfoScreenData(context.getString(R.string.info_screen_desc_3), R.drawable.info_screen_3));
        infoScreenDataList.add(new InfoScreenData(context.getString(R.string.info_screen_desc_4), R.drawable.info_screen_4));

        // Pager Setup..
        infoScreenAdapter = new InfoScreenAdapter(context, activity, infoScreenDataList);
        viewPager.setAdapter(infoScreenAdapter);

        // Indicator setup..
        new ViewPagerIndicator(context, ViewPagerIndicator.STYLE_SMALL).setup(viewPager, layoutIndicator, R.drawable.circle_black, R.drawable.circle_grey);
    }

    @OnClick(R.id.textViewLogin)
    public void login() {
        LoginActivity.open(context);
    }

    @OnClick(R.id.buttonRegister)
    public void register() {

        LocationSelectionActivity.open(context);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == viewPager.getAdapter().getCount() - 1) {
            buttonRegister.setBackgroundResource(R.drawable.join_rect);
            buttonRegister.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            buttonRegister.setBackgroundResource(R.drawable.button_white);
            buttonRegister.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                List<StoreApiModel> model = ((ResponseModel<List<StoreApiModel>>) response).data;
                TempStorage.setCountries(model);
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.GET_STORE_DATA:
                break;
        }
    }

}

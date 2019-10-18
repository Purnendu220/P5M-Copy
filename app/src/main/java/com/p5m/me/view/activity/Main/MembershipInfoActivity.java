package com.p5m.me.view.activity.Main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.p5m.me.FAQAdapter;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.FAQ;
import com.p5m.me.data.RemoteConfigValueModel;
import com.p5m.me.data.Testimonials;
import com.p5m.me.helper.Helper;
import com.p5m.me.notifications.HandleNotificationDeepLink;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MembershipInfoActivity extends BaseActivity implements View.OnClickListener, AdapterCallbacks {

    private FAQAdapter faqAdapter;
    private List<FAQ> defaultFAQList;
    private List<FAQ> faqList;
    private RemoteConfigValueModel remoteConfigdata;

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, MembershipInfoActivity.class));

    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.buyClassesLayout)
    public LinearLayout buyClassesLayout;

    @BindView(R.id.textViewFrequentlyAskedQuestions)
    public TextView textViewFrequentlyAskedQuestions;

    @BindView(R.id.recyclerViewFAQ)
    public RecyclerView recyclerView;

    @BindView(R.id.scroll_view)
    public ScrollView scrollView;

    @BindView(R.id.textViewWelcome)
    public TextView textViewWelcome;

    @BindView(R.id.textViewWelcomeDetail)
    public TextView textViewWelcomeDetail;
    @BindView(R.id.textViewMembershipFeatures)
    public TextView textViewMembershipFeatures;
    @BindView(R.id.textViewVarietyActivity)
    public TextView textViewVarietyActivity;
    @BindView(R.id.textViewVarietyActivityDetail)
    public TextView textViewVarietyActivityDetail;

    @BindView(R.id.textViewVisitGym)
    public TextView textViewVisitGym;
    @BindView(R.id.textViewVisitGymDetail)
    public TextView textViewVisitGymDetail;
    @BindView(R.id.textViewSave)
    public TextView textViewSave;
    @BindView(R.id.textViewSaveDetail)
    public TextView textViewSaveDetail;
    @BindView(R.id.textViewInstantBooking)
    public TextView textViewInstantBooking;
    @BindView(R.id.textViewInstantBookingDetail)
    public TextView textViewInstantBookingDetail;
    @BindView(R.id.textViewHowItWorks)
    public TextView textViewHowItWorks;
    @BindView(R.id.textViewExplore)
    public TextView textViewExplore;
    @BindView(R.id.textViewExploreDetail)
    public TextView textViewExploreDetail;
    @BindView(R.id.textViewPickPlan)
    public TextView textViewPickPlan;
    @BindView(R.id.textViewPickPlanDetail)
    public TextView textViewPickPlanDetail;
    @BindView(R.id.textViewTrain)
    public TextView textViewTrain;
    @BindView(R.id.textViewTrainDetail)
    public TextView textViewTrainDetail;

    @BindView(R.id.textViewDiscount)
    public TextView textViewDiscount;
    @BindView(R.id.textViewDiscountDetail)
    public TextView textViewDiscountDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_membership_info);

        ButterKnife.bind(activity);
        buyClassesLayout.setOnClickListener(this);
        setToolBar();
        setFAQAdapter();
        setValues();
    }

    private void setValues() {
        textViewWelcome.setText(getValueFromConfig(RemoteConfigConst.WELCOME_P5M_VALUE,context.getResources().getString(R.string.welcome_to_p5m)));
        textViewWelcomeDetail.setText(getValueFromConfig(RemoteConfigConst.WELCOME_P5M_CONTAIN_VALUE,getString(R.string.on_p5m_you)));
        textViewMembershipFeatures.setText(getValueFromConfig(RemoteConfigConst.SECTION_ONE_TITLE_VALUE,getString(R.string.with_the_p5m_membership_you_can)));
        textViewVarietyActivity.setText(getValueFromConfig(RemoteConfigConst.SECTION_ONE_SUB_ONE_VALUE,getString(R.string.variety_of_activities)));
        textViewVarietyActivityDetail.setText(getValueFromConfig(RemoteConfigConst.SECTION_ONE_SUB_ONE_CONTAIN_VALUE,getString(R.string.variety_of_activity_details)));
        textViewVisitGym.setText(getValueFromConfig(RemoteConfigConst.SECTION_ONE_SUB_TWO_VALUE,getString(R.string.visit_gym_feature)));
        textViewVisitGymDetail.setText(getValueFromConfig(RemoteConfigConst.SECTION_ONE_SUB_TWO_CONTAIN_VALUE,getString(R.string.visit_gym_detail)));
        textViewSave.setText(getValueFromConfig(RemoteConfigConst.SECTION_ONE_SUB_THREE_VALUE,getString(R.string.save_percent)));
        textViewSaveDetail.setText(getValueFromConfig(RemoteConfigConst.SECTION_ONE_SUB_THREE_DETAIL_VALUE,getString(R.string.save_detail)));
        textViewInstantBooking.setText(getValueFromConfig(RemoteConfigConst.SECTION_ONE_SUB_FOUR_VALUE,getString(R.string.instant_booking)));
        textViewInstantBookingDetail.setText(getValueFromConfig(RemoteConfigConst.SECTION_ONE_SUB_FOUR_DETAIL_VALUE,getString(R.string.instant_booking_detail)));
        textViewHowItWorks.setText(getValueFromConfig(RemoteConfigConst.SECTION_TWO_TITLE_VALUE,getString(R.string.how_its_work)));
        textViewExplore.setText(getValueFromConfig(RemoteConfigConst.SECTION_TWO_SUB_ONE_VALUE,getString(R.string.explore)));
        textViewExploreDetail.setText(getValueFromConfig(RemoteConfigConst.SECTION_TWO_SUB_ONE_DETAIL_VALUE,getString(R.string.exploreDetail)));
        textViewPickPlan.setText(getValueFromConfig(RemoteConfigConst.SECTION_TWO_SUB_TWO_VALUE,getString(R.string.pick_a_plan)));
        textViewPickPlanDetail.setText(getValueFromConfig(RemoteConfigConst.SECTION_TWO_SUB_TWO_DETAIL_VALUE,getString(R.string.that_work_for_you)));
        textViewTrain.setText(getValueFromConfig(RemoteConfigConst.SECTION_TWO_SUB_THREE_VALUE,getString(R.string.train)));
        textViewTrainDetail.setText(getValueFromConfig(RemoteConfigConst.SECTION_TWO_SUB_THREE_DETAIL_VALUE,getString(R.string.workout_any_gym)));
        textViewDiscount.setText(getValueFromConfig(RemoteConfigConst.SECTION_TWO_SUB_FOUR_VALUE,getString(R.string.get_discount)));
        textViewDiscountDetail.setText(getValueFromConfig(RemoteConfigConst.SECTION_TWO_SUB_FOUR_DETAIL_VALUE,getString(R.string.go_to_different_gym)));

    }

    private String getValueFromConfig(String remoteConfigValue,String defaultValue){
        try {
            String value = remoteConfigValue;

            if (value != null && !value.isEmpty()) {
                Gson g = new Gson();
                RemoteConfigValueModel p = g.fromJson(value, new com.google.gson.reflect.TypeToken<RemoteConfigValueModel>() {
                }.getType());
                remoteConfigdata = p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (remoteConfigdata == null) {
            return defaultValue;
        } else {
            if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar") &&
                    !TextUtils.isEmpty(remoteConfigdata.getAr())) {
                return remoteConfigdata.getAr();
            } else if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("en") &&
                    !TextUtils.isEmpty(remoteConfigdata.getEn())) {
                return remoteConfigdata.getEn();
            }
            else
                return defaultValue;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    protected void onPause() {
        super.onPause();

    }

    private void setToolBar() {

        BaseActivity activity = (BaseActivity) this.activity;
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_normal, null);

        v.findViewById(R.id.imageViewBack).setOnClickListener(view -> onBackPressed());
//        mTextViewWalletAmount= v.findViewById(R.id.textViewWalletAmount);
//        mLayoutUserWallet= v.findViewById(R.id.layoutUserWallet);
//        mLayoutUserWallet.setOnClickListener(this);

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.membership_info));

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buyClassesLayout:
                finish();
                break;
        }
    }

    private void setFAQAdapter() {
        try {

            String faqValue = RemoteConfigConst.FAQ_VALUE;
            if (faqValue != null && !faqValue.isEmpty()) {
                Gson g = new Gson();
                List<FAQ> p = g.fromJson(faqValue, new TypeToken<List<FAQ>>() {
                }.getType());
                faqList = p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (faqList == null) {
            textViewFrequentlyAskedQuestions.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            return;
        } else {
            if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar") &&
                    !TextUtils.isEmpty(faqList.get(0).getArabic_question())) {
                setVisibleAdapter();
            } else if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("en") &&
                    !TextUtils.isEmpty(faqList.get(0).getEnglish_question())) {
                setVisibleAdapter();
            } else {
                textViewFrequentlyAskedQuestions.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);

            }

        }
    }

    private void setVisibleAdapter() {
        {
            recyclerView.setVisibility(View.VISIBLE);
            textViewFrequentlyAskedQuestions.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
            recyclerView.setHasFixedSize(false);

            faqAdapter = new FAQAdapter(context, MembershipInfoActivity.this);

            faqAdapter.addAll(faqList);
            recyclerView.setAdapter(faqAdapter);

            faqAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.textViewAnswer:
                if (model != null && model instanceof FAQ) {
                    FAQ data = (FAQ) model;
                    overridePendingTransition(0, 0);
                    Intent navigationIntent = HandleNotificationDeepLink.handleNotificationDeeplinking(this, data.getRedirect_android_link());
                    startActivity(navigationIntent);
                }
                break;
            case R.id.textViewQuestions:
//                scrollView.setSmoothScrollingEnabled(true);
//                scrollView.smoothScrollTo(0,scrollView.getMaxScrollAmount());
//                scrollView.fullScroll(View.FOCUS_DOWN);
                scrollView.fullScroll(View.FOCUS_DOWN);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 10);
                scrollView.fullScroll(View.FOCUS_DOWN);


        }
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

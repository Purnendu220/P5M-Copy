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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.p5m.me.FAQAdapter;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.FAQ;
import com.p5m.me.data.Testimonials;
import com.p5m.me.helper.Helper;
import com.p5m.me.notifications.HandleNotificationDeepLink;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MembershipInfoActivity extends BaseActivity implements View.OnClickListener, AdapterCallbacks {

    private FAQAdapter faqAdapter;
    private List<FAQ> defaultFAQList;
    private List<FAQ> faqList;

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, MembershipInfoActivity.class));

    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.buyClassesLayout)
    public LinearLayout buyClassesLayout;

    @BindView(R.id.recyclerViewFAQ)
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_membership_info);

        ButterKnife.bind(activity);
        buyClassesLayout.setOnClickListener(this);
        setToolBar();
        setFAQAdapter();

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
            defaultFAQList = new Gson().fromJson(Helper.getTestimonialsFileFromAsset(context), new TypeToken<List<Testimonials>>() {
            }.getType());
            String faqValue = RemoteConfigConst.FAQ_VALUE;
            if (faqValue != null && !faqValue.isEmpty()) {
                Gson g = new Gson();
                List<FAQ> p = g.fromJson(faqValue, new TypeToken<List<FAQ>>() {
                }.getType());
                faqList = p;
            } else {
                faqList = defaultFAQList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (faqList == null) {
            recyclerView.setVisibility(View.GONE);
            return;
        }
        recyclerView.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(false);

        faqAdapter = new FAQAdapter(context, MembershipInfoActivity.this);
        recyclerView.setAdapter(faqAdapter);

        faqAdapter.addAll(faqList);
        faqAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.textViewAnswer:
                if (model != null && model instanceof FAQ) {
                    FAQ data = (FAQ) model;
                        overridePendingTransition(0, 0);
                    Intent navigationIntent= HandleNotificationDeepLink.handleNotificationDeeplinking(this,data.getRedirect_android_link());
                    startActivity(navigationIntent);
                }
                break;
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

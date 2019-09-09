package com.p5m.me.view.activity.Main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MembershipInfoActivity extends BaseActivity implements View.OnClickListener {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, MembershipInfoActivity.class));

    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.buyClassesLayout)
    public LinearLayout buyClassesLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_membership_info);

        ButterKnife.bind(activity);
        buyClassesLayout.setOnClickListener(this);
        setToolBar();
        TempStorage.setOpenMembershipInfo(false);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    protected void onPause()
    {
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

        v.findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
        switch (v.getId()){
            case R.id.buyClassesLayout:
                finish();
                break;
        }
    }
}

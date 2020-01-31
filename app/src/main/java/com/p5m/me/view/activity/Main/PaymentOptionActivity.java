package com.p5m.me.view.activity.Main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.NotificationsAdapter;
import com.p5m.me.adapters.PaymentOptionAdapter;
import com.p5m.me.adapters.TrainerProfileAdapter;
import com.p5m.me.adapters.viewholder.PaymentOptionViewHolder;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentOptionActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, NetworkCommunicator.RequestListener, AdapterCallbacks {


    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private PaymentOptionAdapter paymentOptionAdapter;

    public static void open(Context context) {
        context.startActivity(new Intent(context,PaymentOptionActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);
        ButterKnife.bind(activity);
        swipeRefreshLayout.setOnRefreshListener(this);
        setToolBar();
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        paymentOptionAdapter = new PaymentOptionAdapter(context, true, this);
        recyclerView.setAdapter(paymentOptionAdapter);


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
                finish();
            }
        });

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.payment_options));

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

    }
}

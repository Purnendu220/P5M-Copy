package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.PackageLimitAdapter;
import com.p5m.me.data.PackageLimitListItem;
import com.p5m.me.data.PackageLimitModel;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PackageLimitsActivity extends BaseActivity implements NetworkCommunicator.RequestListener, AdapterCallbacks, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static void openActivity(Context context, String packageName) {
        context.startActivity(new Intent(context, PackageLimitsActivity.class).putExtra(AppConstants.DataKey.PACKAGE_NAME_STRING, packageName));
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private PackageLimitAdapter packageLimitAdapter;
    private String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_limits);

        ButterKnife.bind(activity);

        packageName = getIntent().getStringExtra(AppConstants.DataKey.PACKAGE_NAME_STRING);

        if (packageName == null) {
            packageName = "";
        }

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        packageLimitAdapter = new PackageLimitAdapter(context, this);
        recyclerView.setAdapter(packageLimitAdapter);

        onRefresh();
        setToolBar();
        onTrackingNotification();
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        swipeRefreshLayout.setRefreshing(false);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.PACKAGES_LIMIT:

                swipeRefreshLayout.setEnabled(false);
                List<PackageLimitModel> models = ((ResponseModel<List<PackageLimitModel>>) response).data;

                if (!models.isEmpty()) {

                    // To sort the NumberOfVisit like 1, 2, 3.... 0(Unlimited) to generate the right tabs..
                    Collections.sort(models, new Comparator<PackageLimitModel>() {
                        @Override
                        public int compare(PackageLimitModel left, PackageLimitModel right) {

                            if (left.getNumberOfVisit() == 0) {
                                return 1;
                            } else if (right.getNumberOfVisit() == 0) {
                                return -1;
                            }

                            return left.getNumberOfVisit() - right.getNumberOfVisit();
                        }
                    });

                    List<PackageLimitListItem> packageLimitListItems = new ArrayList<>();

                    for (PackageLimitModel model : models) {
                        PackageLimitListItem packageLimitListItem = new PackageLimitListItem(
                                model.getPackageType() + model.getPackageName(),
                                model.getPackageName(), PackageLimitListItem.TYPE_HEADER);

                        if (packageLimitListItem.getName().equals(packageName)) {
                            packageLimitListItem.setExpanded(true);
                        }

                        if (packageLimitListItems.contains(packageLimitListItem)) {
                            packageLimitListItem = packageLimitListItems.get(packageLimitListItems.indexOf(packageLimitListItem));
                        } else {
                            packageLimitListItem.setPackageLimitModel(model);
                            packageLimitListItems.add(packageLimitListItem);
                        }

                        packageLimitListItem.getList().add(model);
                    }

//                    packageLimitAdapter.addHeader(context.getString(R.string.package_limit_header));
                    packageLimitAdapter.addHeader(RemoteConfigConst.GYM_VISIT_LIMIT_DETAIL_TEXT_VALUE);
                    packageLimitAdapter.addAll(packageLimitListItems);
                    packageLimitAdapter.notifyDataSetChanges();

                } else {
                    checkListData();
                }

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);
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

    private void checkListData() {
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

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_bar_package_limits, null);
        v.findViewById(R.id.imageViewBack).setOnClickListener(this);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        networkCommunicator.getPackagesLimit(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL, this, false);
    }
}

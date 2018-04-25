package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.PackageLimitAdapter;
import www.gymhop.p5m.data.PackageLimitListItem;
import www.gymhop.p5m.data.PackageLimitModel;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class PackageLimitsActivity extends BaseActivity implements NetworkCommunicator.RequestListener, AdapterCallbacks, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, PackageLimitsActivity.class));
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    public PackageLimitAdapter packageLimitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_limits);

        ButterKnife.bind(activity);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        packageLimitAdapter = new PackageLimitAdapter(context, this);
        recyclerView.setAdapter(packageLimitAdapter);

        onRefresh();

        setToolBar();
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

                        if (packageLimitListItems.contains(packageLimitListItem)) {
                            packageLimitListItem = packageLimitListItems.get(packageLimitListItems.indexOf(packageLimitListItem));
                        } else {
                            packageLimitListItem.setPackageLimitModel(model);
                            packageLimitListItems.add(packageLimitListItem);
                        }

                        packageLimitListItem.getList().add(model);
                    }

                    packageLimitAdapter.addHeader(context.getString(R.string.package_limit_header));
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

    private void checkListData() {
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

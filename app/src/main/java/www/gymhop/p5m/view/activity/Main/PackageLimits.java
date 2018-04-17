package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
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

public class PackageLimits extends BaseActivity implements NetworkCommunicator.RequestListener, AdapterCallbacks {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, PackageLimits.class));
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;

    public PackageLimitAdapter packageLimitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_limits);

        ButterKnife.bind(activity);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        packageLimitAdapter = new PackageLimitAdapter(context, this);
        recyclerView.setAdapter(packageLimitAdapter);

        networkCommunicator.getPackagesLimit(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL, this, false);
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.PACKAGES_FOR_CLASS:
                List<PackageLimitModel> models = ((ResponseModel<List<PackageLimitModel>>) response).data;

                if (!models.isEmpty()) {

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

    private void checkListData() {
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

    }

    @Override
    public void onAdapterItemClick(View viewRoot, View view, Object model, int position) {

    }

    @Override
    public void onAdapterItemLongClick(View viewRoot, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
}

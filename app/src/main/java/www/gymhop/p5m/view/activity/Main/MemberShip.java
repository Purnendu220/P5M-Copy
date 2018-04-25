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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.MemberShipAdapter;
import www.gymhop.p5m.adapters.viewholder.MemberShipViewHolder;
import www.gymhop.p5m.data.Package;
import www.gymhop.p5m.data.PaymentUrl;
import www.gymhop.p5m.data.User;
import www.gymhop.p5m.data.UserPackage;
import www.gymhop.p5m.data.UserPackageInfo;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.request.PaymentUrlRequest;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.DialogUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class MemberShip extends BaseActivity implements AdapterCallbacks, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener {

    public static void openActivity(Context context, int navigationFrom) {
        context.startActivity(new Intent(context, MemberShip.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom));
    }

    public static void openActivity(Context context, int navigationFrom, ClassModel classModel) {
        Intent intent = new Intent(context, MemberShip.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        intent.putExtra(AppConstants.DataKey.CLASS_OBJECT, classModel);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private int navigatedFrom;
    private ClassModel classModel;
    private MemberShipAdapter memberShipAdapter;
    private UserPackageInfo userPackageInfo;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship);

        ButterKnife.bind(activity);

        swipeRefreshLayout.setOnRefreshListener(this);

        navigatedFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);
        classModel = (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_OBJECT);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        memberShipAdapter = new MemberShipAdapter(context, navigatedFrom, false, this);
        recyclerView.setAdapter(memberShipAdapter);

        user = TempStorage.getUser();

        onRefresh();

        setToolBar();
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

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.membership));

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    private void checkPackages() {
        userPackageInfo = new UserPackageInfo(user);

        if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS) {
            memberShipAdapter.setClassModel(classModel);

            // show general, ready, and owned packages
            if (userPackageInfo.havePackages) {
                memberShipAdapter.addAllOwnedPackages(userPackageInfo.userPackageReady);

                if (userPackageInfo.haveGeneralPackage) {
                    // User have General package and may be also have dropins..
                    memberShipAdapter.addOwnedPackages(userPackageInfo.userPackageGeneral);
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_only_drop_in_package_heading_1),
                            context.getString(R.string.membership_only_drop_in_package_heading_2));
                } else {
                    // User don't have General package but may have dropins..
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_drop_in_package_heading_1), context.getString(R.string.membership_drop_in_package_heading_2));
                }
            } else {
                // User have no packages
                // Show offered packages
                memberShipAdapter.setHeaderText(context.getString(R.string.membership_no_package_heading_1),
                        context.getString(R.string.membership_no_package_heading_2));
            }

            networkCommunicator.getPackagesForClass(user.getId(), classModel.getGymBranchDetail().getGymId(), classModel.getClassSessionId(), this, false);

        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SETTING ||
                navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE) {

            if (userPackageInfo.havePackages) {

                memberShipAdapter.addAllOwnedPackages(userPackageInfo.userPackageReady);

                if (!userPackageInfo.haveGeneralPackage) {
                    // User don't have General package..
                    // get general and show owned packages
                    networkCommunicator.getPackages(user.getId(), this, false);
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_drop_in_package_heading_1),
                            context.getString(R.string.membership_drop_in_package_heading_2));

                } else {
                    // User only have drop in packages..
                    // only Show owned packages..
                    memberShipAdapter.addOwnedPackages(userPackageInfo.userPackageGeneral);
                    memberShipAdapter.setHeaderText(context.getString(R.string.membership_general_package_heading_1), context.getString(R.string.membership_general_package_heading_2));
                    memberShipAdapter.notifyDataSetChanges();
                }
            } else {
                // User have no packages
                // Show offered packages
                networkCommunicator.getPackages(user.getId(), this, false);
                memberShipAdapter.setHeaderText(context.getString(R.string.membership_no_package_heading_1),
                        context.getString(R.string.membership_no_package_heading_2));
            }
        }
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

        switch (view.getId()) {
            case R.id.textViewViewLimit: {
                PackageLimitsActivity.openActivity(context);
            }
            break;
            case R.id.button: {

                if (viewHolder instanceof MemberShipViewHolder) {
                    ((MemberShipViewHolder) viewHolder).button.setText(context.getResources().getString(R.string.please_wait));
                    ((MemberShipViewHolder) viewHolder).button.setEnabled(false);
                }

                Package aPackage = (Package) model;
                if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS) {
                    networkCommunicator.purchasePackageForClass(new PaymentUrlRequest(TempStorage.getUser().getId(),
                            aPackage.getId(), classModel.getClassSessionId(),
                            classModel.getGymBranchDetail().getGymId()), this, false);

                } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SETTING ||
                        navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE) {
                    networkCommunicator.purchasePackageForClass(new PaymentUrlRequest(TempStorage.getUser().getId(),
                            aPackage.getId()), this, false);
                }
            }
            break;
            case R.id.imageViewInfo: {
                if (model instanceof Package) {
                    Package aPackage = (Package) model;
                    if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                        DialogUtils.showBasicMessage(context, aPackage.getName(), "Ok", R.string.membership_package_limit_info);
                    } else if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                        DialogUtils.showBasicMessage(context, aPackage.getName(), "Ok", R.string.membership_drop_in_info);
                    }
                } else if (model instanceof UserPackage) {
                    UserPackage aPackage = (UserPackage) model;
                    if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                        DialogUtils.showBasicMessage(context, aPackage.getPackageName(), "Ok", R.string.membership_package_limit_info);
                    } else if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                        DialogUtils.showBasicMessage(context, aPackage.getPackageName(), "Ok", R.string.membership_drop_in_info);
                    }
                }
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
    public void onApiSuccess(Object response, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.PACKAGES_LIMIT:
            case NetworkCommunicator.RequestCode.PACKAGES_FOR_USER:

                swipeRefreshLayout.setEnabled(false);

                List<Package> packagesTemp = ((ResponseModel<List<Package>>) response).data;

                if (packagesTemp != null && !packagesTemp.isEmpty()) {
                    List<Package> packages = new ArrayList<>(packagesTemp.size());

                    for (Package aPackage : packagesTemp) {
                        if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                            if (!userPackageInfo.haveGeneralPackage) {
                                packages.add(aPackage);
                            }
                        } else if (aPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                            aPackage.setGymName(classModel.getGymBranchDetail().getGymName());
                            packages.add(aPackage);
                        }
                    }

                    memberShipAdapter.addAllOfferedPackages(packages);
                }
                memberShipAdapter.notifyDataSetChanges();
                break;

            case NetworkCommunicator.RequestCode.BUY_PACKAGE:

                PaymentWebViewActivity.open(context, ((ResponseModel<PaymentUrl>) response).data);
                memberShipAdapter.notifyDataSetChanges();
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.BUY_PACKAGE:
                memberShipAdapter.notifyDataSetChanges();
                break;
        }
    }

    @Override
    public void onRefresh() {

        swipeRefreshLayout.setRefreshing(true);
        checkPackages();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}

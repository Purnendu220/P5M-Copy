package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.brandongogetap.stickyheaders.StickyLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.ClassProfileAdapter;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.ToastUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class ClassProfileActivity extends BaseActivity implements AdapterCallbacks {

    public static void open(Context context, ClassModel classModel) {
        context.startActivity(new Intent(context, ClassProfileActivity.class)
                .putExtra(AppConstants.DataKey.CLASS_OBJECT, classModel));
    }

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private ClassProfileAdapter classProfileAdapter;
    private ClassModel classModel;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_profile);

        ButterKnife.bind(activity);

        classModel = (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_OBJECT);

        if (classModel == null) {
            finish();
            return;
        }

        setToolBar();

        classProfileAdapter = new ClassProfileAdapter(context, AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE, this);
        StickyLayoutManager layoutManager = new StickyLayoutManager(context, classProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));
        layoutManager.setAutoMeasureEnabled(false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(classProfileAdapter);
        classProfileAdapter.setClass(classModel);
        recyclerView.setHasFixedSize(true);
    }

    @OnClick(R.id.textViewBook)
    public void textViewBook() {
        MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
        return;

//        UserPackageInfo userPackageInfo = new UserPackageInfo(TempStorage.getUser());
//
//        if (userPackageInfo.havePackages) {
//
//            // 1st condition : have drop-in for class
//            if (userPackageInfo.haveDropInPackage && classModel.getGymBranchDetail() != null) {
//                for (UserPackage userPackage : userPackageInfo.userPackageReady) {
//                    if (userPackage.getGymId() == classModel.getGymBranchDetail().getGymId()) {
//                        joinClass();
//                        return;
//                    }
//                }
//            }
//
//            // 2st condition : have class remaining in package
//            if (userPackageInfo.haveGeneralPackage) {
//                if (userPackageInfo.userPackageGeneral != null) {
//                    if (DateUtils.canJoinClass(classModel.getClassDate(), userPackageInfo.userPackageGeneral.getExpiryDate()) >= 0) {
//                        joinClass();
//                        return;
//                    }
//                }
//            }
//            MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
//
//        } else {
//            MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS, classModel);
//        }
    }

    private void joinClass() {
        ToastUtils.show(context, "Join class");
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

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_bar_class_profile, null);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.imageViewClass:
                if (classModel.getClassMedia() != null) {
                    Helper.openImageViewer(context, classModel.getClassMedia().getMediaUrl());
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
}

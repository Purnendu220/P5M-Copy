package www.gymhop.p5m.view.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.MyProfileAdapter;
import www.gymhop.p5m.adapters.viewholder.ProfileHeaderTabViewHolder;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.helper.ClassMiniListListenerHelper;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.helper.TrainerListListenerHelper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.activity.Main.EditProfileActivity;
import www.gymhop.p5m.view.activity.Main.LocationListMapActivity;
import www.gymhop.p5m.view.activity.Main.MemberShip;
import www.gymhop.p5m.view.activity.Main.SettingActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;
import www.gymhop.p5m.view.activity.custom.MyRecyclerView;

public class MyProfile extends BaseFragment implements AdapterCallbacks<Object>, MyRecyclerView.LoaderCallbacks, NetworkCommunicator.RequestListener, PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private MyProfileAdapter myProfileAdapter;
    private int page;

    public MyProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        myProfileAdapter = new MyProfileAdapter(context, new TrainerListListenerHelper(context, activity),
                new ClassMiniListListenerHelper(context, activity), this);
        StickyLayoutManager layoutManager = new StickyLayoutManager(context, myProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));
        layoutManager.setAutoMeasureEnabled(false);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(myProfileAdapter);
        myProfileAdapter.setUser(TempStorage.getUser());
        myProfileAdapter.notifyDataSetChanges();
        recyclerView.setHasFixedSize(true);

        layoutManager.setStickyHeaderListener(new StickyHeaderListener() {
            @Override
            public void headerAttached(View headerView, int adapterPosition) {
                maintainHeaderState(headerView);
            }

            @Override
            public void headerDetached(View headerView, int adapterPosition) {
                maintainHeaderState(headerView);
            }
        });

        if (myProfileAdapter.getTrainers().isEmpty()) {
            networkCommunicator.getFavTrainerList(AppConstants.ApiParamValue.FOLLOW_TYPE_FOLLOWED, TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
            myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_1);
        }

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

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_bar_my_profile, null);

        v.findViewById(R.id.imageViewOptions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.showPopMenu(context, view, MyProfile.this);
            }
        });

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    /**
     * To fix and maintain the sticky header tab selection state bug..
     */
    private void maintainHeaderState(View headerView) {
        ProfileHeaderTabViewHolder profileHeaderTabViewHolder;
        if (myProfileAdapter.getProfileHeaderTabViewHolder() != null) {
            profileHeaderTabViewHolder = myProfileAdapter.getProfileHeaderTabViewHolder();
        } else {
            profileHeaderTabViewHolder = new ProfileHeaderTabViewHolder(headerView);
        }

        switch (myProfileAdapter.getSelectedTab()) {
            case ProfileHeaderTabViewHolder.TAB_1:

                profileHeaderTabViewHolder.header1.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                profileHeaderTabViewHolder.header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                profileHeaderTabViewHolder.header2.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                profileHeaderTabViewHolder.header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                break;
            case ProfileHeaderTabViewHolder.TAB_2:

                profileHeaderTabViewHolder.header1.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                profileHeaderTabViewHolder.header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                profileHeaderTabViewHolder.header2.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                profileHeaderTabViewHolder.header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                break;
        }
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.header1: {
                if (myProfileAdapter.getTrainers().isEmpty()) {
                    networkCommunicator.getFavTrainerList(AppConstants.ApiParamValue.FOLLOW_TYPE_FOLLOWED, TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
                }

                myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_1);
                myProfileAdapter.notifyDataSetChanges();
            }
            break;
            case R.id.header2: {
                if (myProfileAdapter.getClasses().isEmpty()) {
                    networkCommunicator.getFinishedClassList(TempStorage.getUser().getId(), page, AppConstants.Limit.PAGE_LIMIT_INNER_TRAINER_LIST, this, false);
                }

                myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_2);
                myProfileAdapter.notifyDataSetChanges();
            }
            break;
            case R.id.textViewMore:
            case R.id.textViewRecharge:
                MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE);
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
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CLASS_LIST:
                List<ClassModel> classModels = ((ResponseModel<List<ClassModel>>) response).data;

                if (!classModels.isEmpty()) {
                    myProfileAdapter.addClasses(classModels);
                    myProfileAdapter.notifyDataSetChanges();
                } else {
                    checkListData();
                }

                break;
            case NetworkCommunicator.RequestCode.TRAINER_LIST:
                List<TrainerModel> trainerModels = ((ResponseModel<List<TrainerModel>>) response).data;

                if (!trainerModels.isEmpty()) {
                    myProfileAdapter.addTrainers(trainerModels);
                    myProfileAdapter.notifyDataSetChanges();
                } else {
                    checkListData();
                }

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
    }

    private void checkListData() {
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.actionSettings: {
                SettingActivity.openActivity(context);
                return true;
            }
            case R.id.actionEditProfile: {
                EditProfileActivity.openActivity(context);
                LocationListMapActivity.openActivity(context);
                return true;
            }
            default:
                return false;
        }
    }
}

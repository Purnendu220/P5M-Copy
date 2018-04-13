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
import android.view.View;
import android.view.ViewGroup;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.MyProfileAdapter;
import www.gymhop.p5m.adapters.viewholder.ProfileHeaderTabViewHolder;
import www.gymhop.p5m.data.HeaderSticky;
import www.gymhop.p5m.data.gym_class.ClassModel;
import www.gymhop.p5m.data.gym_class.TrainerModel;
import www.gymhop.p5m.view.activity.base.BaseActivity;
import www.gymhop.p5m.view.activity.custom.MyRecyclerView;

public class MyProfile extends BaseFragment implements AdapterCallbacks<Object>, MyRecyclerView.LoaderCallbacks {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private MyProfileAdapter myProfileAdapter;

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

        setToolBar();

        myProfileAdapter = new MyProfileAdapter(context, this);
        recyclerView.setAdapter(myProfileAdapter);

        StickyLayoutManager layoutManager = new StickyLayoutManager(context, myProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));

        recyclerView.setLayoutManager(layoutManager);
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

        List<Object> data = new ArrayList<>();
        for (int count = 0; count < 10; count++) {
            if (count == 1)
                data.add(new HeaderSticky(""));
            else
                data.add(new TrainerModel());
        }

        myProfileAdapter.setData(data);

        myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_1);
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

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(View viewRoot, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.header1: {
                myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_1);

                List<Object> data = new ArrayList<>();
                for (int count = 0; count < 10; count++) {
                    if (count == 1)
                        data.add(new HeaderSticky(""));
                    else
                        data.add(new TrainerModel());
                }

                myProfileAdapter.setData(data);
                myProfileAdapter.notifyDataSetChanged();
            }
            break;
            case R.id.header2: {
                myProfileAdapter.onTabSelection(ProfileHeaderTabViewHolder.TAB_2);

                List<Object> data = new ArrayList<>();
                for (int count = 0; count < 10; count++) {
                    if (count == 1)
                        data.add(new HeaderSticky(""));
                    else
                        data.add(new ClassModel());
                }

                myProfileAdapter.setData(data);
                myProfileAdapter.notifyDataSetChanged();
            }
            break;
        }
    }

    @Override
    public void onAdapterItemLongClick(View viewRoot, View view, Object model, int position) {
    }

    @Override
    public void onShowLastItem() {
    }

    @Override
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {
    }
}

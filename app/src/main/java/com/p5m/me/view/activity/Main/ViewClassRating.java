package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ClassListAdapter;
import com.p5m.me.adapters.RatingListAdapter;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.ClassRatingUserData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.SimpleDividerItemDecoration;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.AppConstants.Limit.PAGE_LIMIT_MAIN_CLASS_LIST;

public class ViewClassRating extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, NetworkCommunicator.RequestListener,AdapterCallbacks<ClassRatingListData> {
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerViewProfile)
    public RecyclerView recyclerViewProfile;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private ClassModel model;
    private int page;
    private int showInScreen;
    private RatingListAdapter ratingListAdapter;
    public static void open(Context context,ClassModel model,int shoInScreen) {
        Intent intent = new Intent(context, ViewClassRating.class);
        intent.putExtra(AppConstants.DataKey.CLASS_MODEL,model);
        intent.putExtra(AppConstants.DataKey.TAB_SHOWN_IN_INT,shoInScreen);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_class_rating);
        ButterKnife.bind(activity);
        setToolBar();
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerViewProfile.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewProfile.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerViewProfile.setHasFixedSize(true);
        model= (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_MODEL);
        showInScreen =getIntent().getIntExtra(AppConstants.DataKey.TAB_SHOWN_IN_INT,-1);
        ratingListAdapter = new RatingListAdapter(context, 1, true, this);
        recyclerViewProfile.setAdapter(ratingListAdapter);

        onRefresh();
        onTrackingNotification();
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

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.rating_view));

        ImageView imageViewShare = v.findViewById(R.id.imageViewShare);

        imageViewShare.setVisibility(View.GONE);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        page=0;
        ratingListAdapter.loaderReset();
        networkCommunicator.getClassRatingList(model.getClassId(),page,PAGE_LIMIT_MAIN_CLASS_LIST,this,false);

    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode){
            case NetworkCommunicator.RequestCode.CLASS_RATING_LIST:
                swipeRefreshLayout.setRefreshing(false);
                ClassRatingUserData ratingData=((ResponseModel<ClassRatingUserData>) response).data;

                List<ClassRatingListData> classModels = ratingData.getRatingResList();
                if (page == 0) {
                    ratingListAdapter.clearAll();
                }


                if (!classModels.isEmpty()) {
                    ratingListAdapter.addAllClass(classModels);

                    if (classModels.size() < PAGE_LIMIT_MAIN_CLASS_LIST) {
                        ratingListAdapter.loaderDone();
                    }
                } else {
                    ratingListAdapter.loaderDone();
                }
                ratingListAdapter.notifyDataSetChanged();

                break;
        }

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode){
            case NetworkCommunicator.RequestCode.CLASS_RATING_LIST:
                break;
        }
    }


    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, ClassRatingListData model, int position) {

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, ClassRatingListData model, int position) {

    }

    @Override
    public void onShowLastItem() {
        page++;
        networkCommunicator.getClassRatingList(model.getClassId(),page,PAGE_LIMIT_MAIN_CLASS_LIST,this,false);


    }
}

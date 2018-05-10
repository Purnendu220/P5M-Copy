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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.NotificationsAdapter;
import www.gymhop.p5m.data.main.NotificationModel;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class NotificationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, NetworkCommunicator.RequestListener, AdapterCallbacks {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, NotificationActivity.class));
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.layoutNoData)
    public View layoutNoData;
    @BindView(R.id.imageViewEmptyLayoutImage)
    public ImageView imageViewEmptyLayoutImage;
    @BindView(R.id.textViewEmptyLayoutText)
    public TextView textViewEmptyLayoutText;

    private int page;
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_NOTIFICATIONS;

    private NotificationsAdapter notificationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ButterKnife.bind(activity);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        notificationsAdapter = new NotificationsAdapter(context, true, this);
        recyclerView.setAdapter(notificationsAdapter);

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

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.notifications));

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            default:
                if (model instanceof NotificationModel) {
                    NotificationModel notificationModel = (NotificationModel) model;

                    // Navigation..
                    switch (notificationModel.getNotificationType()) {

                        case "OnClassUpdateByGYM":
                        case "OnClassUpdateByTrainer":
                        case "OnSessionUpdateByGYM":
                        case "OnSessionUpdateByTrainerOfGym":
                        case "OnSessionUpdateByTrainer":
                        case "OnSessionUpdateByGymOfTrainer":
                        case "OnClassUpdateByCms":
                        case "OnGroupClassUpdateByCms":
                            // Class Details..
//                            ClassProfileActivity.open();
                            break;

                        case "OnClassCreation":
                            //Trainer Profile..
                            TrainerProfileActivity.open(context, notificationModel.getObjectDataId());
                            break;

                        case "OnClassRefund":
                            //Membership..
                            MemberShip.openActivity(context, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);
                            break;

                        case "OnSessionDeleteByGYM":
                        case "OnClassDeleteByTrainer":
                        case "OnSessionDeleteByGymOfTrainer":
                        case "OnSessionDeleteByTrainerOfGym":
                        case "OnSessionDeleteByTrainer":
                        case "OnClassDeleteByGym":
                        case "OnClassDeleteByTrainerOfGym":
                        case "OnClassInActive":
                        case "OnSlotDeleteByTrainer":
                        case "OnSlotDeleteByGymOfTrainer":
                        case "OnSlotDeleteByGym":
                        case "OnSlotDeletByTrainerOfGym":
                        case "OnPaymentSuccess":
                        case "OnClassCancelByCMS":
                        case "OnBookingCancelToCustomerByCMS":
                        case "OnBookingCancelToGymByCMS":
                        case "OnBookingCancelToTrainerByCMS":
                            // Find my class..
                            HomeActivity.show(context, AppConstants.FragmentPosition.TAB_FIND_CLASS);
                            break;
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
        page++;
        callApiNotifications();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        page = 0;
        notificationsAdapter.loaderReset();
        callApiNotifications();
    }

    private void callApiNotifications() {
        networkCommunicator.getNotifications(page, pageSizeLimit, this, false);
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.NOTIFICATIONS:

                swipeRefreshLayout.setRefreshing(false);
                List<NotificationModel> notificationModels = ((ResponseModel<List<NotificationModel>>) response).data;

                if (page == 0) {
                    notificationsAdapter.clearAll();
                }

                if (!notificationModels.isEmpty()) {
                    notificationsAdapter.addAll(notificationModels);

                    if (notificationModels.size() < pageSizeLimit) {
                        notificationsAdapter.loaderDone();
                    }
                    notificationsAdapter.notifyDataSetChanged();
                } else {
                    notificationsAdapter.loaderDone();
                }

                checkListData();
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.NOTIFICATIONS:

                swipeRefreshLayout.setRefreshing(false);
                if (page == 0) {
                    notificationsAdapter.clearAll();
                    notificationsAdapter.notifyDataSetChanged();
                }

                checkListData();

                break;
        }
    }

    private void checkListData() {
        if (notificationsAdapter.getList().isEmpty()) {
            layoutNoData.setVisibility(View.VISIBLE);

            textViewEmptyLayoutText.setText(R.string.no_data_notifications);
            imageViewEmptyLayoutImage.setImageResource(R.drawable.stub_notification);
        } else {
            layoutNoData.setVisibility(View.GONE);
        }
    }

}

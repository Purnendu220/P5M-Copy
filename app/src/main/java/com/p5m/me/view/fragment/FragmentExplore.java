package com.p5m.me.view.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.p5m.me.BuildConfig;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ExplorePageAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.data.ExploreGymModel;
import com.p5m.me.data.ExploreRatedClassModel;
import com.p5m.me.data.ExploreTrainerModel;
import com.p5m.me.data.Item;
import com.p5m.me.data.PriceModel;
import com.p5m.me.data.WorkoutModel;
import com.p5m.me.data.YoutubeResponse;
import com.p5m.me.data.main.GymModel;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DividerItemDecoration;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.ClassProfileActivity;
import com.p5m.me.view.activity.Main.Gym;
import com.p5m.me.view.activity.Main.GymProfileActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.Main.TrainerProfileActivity;
import com.p5m.me.view.activity.Main.Trainers;
import com.p5m.me.view.activity.Main.VideoPlayerActivity;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.activity.custom.MyRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.intercom.android.sdk.Intercom;

import static com.p5m.me.utils.AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE;
import static com.p5m.me.utils.AppConstants.AppNavigation.SHOWN_IN_EXPLORE_PAGE;
import static com.p5m.me.utils.AppConstants.ExploreViewType.CATEGORY_CAROUSEL_VIEW;
import static com.p5m.me.utils.AppConstants.ExploreViewType.GYM_VIEW;
import static com.p5m.me.utils.AppConstants.ExploreViewType.PRICE_MODEL_CAROUSEL_VIEW;
import static com.p5m.me.utils.AppConstants.ExploreViewType.TEXT_WITH_BUTTONS;
import static com.p5m.me.utils.AppConstants.ExploreViewType.TEXT_WITH_BUTTONS_2;
import static com.p5m.me.utils.AppConstants.ExploreViewType.TOP_RATED_CLASSES;
import static com.p5m.me.utils.AppConstants.ExploreViewType.TRAINER_CAROUSAL_VIEW;
import static com.p5m.me.utils.AppConstants.ExploreViewType.TRAINER_VIEW;
import static com.p5m.me.utils.AppConstants.Tab.TAB_FIND_CLASS;
import static com.p5m.me.utils.AppConstants.Tab.TAB_MY_MEMBERSHIP;

public class FragmentExplore extends BaseFragment implements ViewPagerFragmentSelection, AdapterCallbacks<Object>, MyRecyclerView.LoaderCallbacks, NetworkCommunicator.RequestListener, SwipeRefreshLayout.OnRefreshListener {
    private int pageSizeLimit = AppConstants.Limit.PAGE_LIMIT_EXPLORE_PAGE;
    private ClassesFilter<PriceModel> filter;
    private ClassesFilter<WorkoutModel> classesFilter;
    private YoutubeResponse youTubeResponseModel;

    public static Fragment createExploreFragment() {
        Fragment tabFragment = new FragmentExplore();
        return tabFragment;
    }

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.appBarLayout)
    public AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    private ExplorePageAdapter explorePageAdapter;
    private List<ClassesFilter> classesFilters;
    private String mixPannelSection;
    private String mixPannelValue;
    String api_key,platlistId;
    private int youTubeModelIndex =-1;

    public FragmentExplore() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_explore_page, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        swipeRefreshLayout.setOnRefreshListener(this);

        explorePageAdapter = new ExplorePageAdapter(context, SHOWN_IN_EXPLORE_PAGE, false, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(context));
        recyclerView.setAdapter(explorePageAdapter);
        explorePageAdapter.notifyDataSetChanges();
         platlistId = RemoteConfigure.getFirebaseRemoteConfig(context).getRemoteConfigValue(RemoteConfigConst.YOUTUBE_PLAYLIST_ID);
         api_key = BuildConfig.YOUTUBE_API_KEY;
        networkCommunicator.getYoutubePlayList(platlistId,api_key,null,this);
        callApi();
        setToolBar();
    }

    private void callApi() {
        networkCommunicator.getExploreData(this, false);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void exploreApiUpdate(Events.ExploreApiUpdate exploreApiUpdate) {
        callApi();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatePackage(Events.UpdatePackage data) {
        callApi();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUser(Events.UserUpdate userUpdate) {
        callApi();

    }

    @Override
    public void onRefresh() {
        callApi();
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

        v.findViewById(R.id.imageViewOptions).setVisibility(View.GONE);
        ((TextView) (v.findViewById(R.id.textViewTitle))).setText(getActivity().getResources().getString(R.string.explore_page));
        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
        activity.getSupportActionBar().hide();
    }


    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {

            case R.id.explorePlans:
                mixPannelSection = TEXT_WITH_BUTTONS;
                mixPannelValue = AppConstants.MixPanelValue.MEMBERSHIP;
                HomeActivity.show(context, TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE);
                break;
            case R.id.imageViewClass:
                if (model != null && model instanceof ExploreGymModel) {
                    ExploreGymModel data = (ExploreGymModel) model;
                    mixPannelSection = GYM_VIEW;
                    mixPannelValue = data.getStudioName();
                    GymProfileActivity.open(context, Integer.parseInt(data.getGymId()), AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE);
                }

                if (model != null && model instanceof ExploreTrainerModel) {
                    ExploreTrainerModel data = (ExploreTrainerModel) model;
                    mixPannelSection = TRAINER_VIEW;
                    mixPannelValue = data.getTrainerName();
                    TrainerProfileActivity.open(context, data.getTrainerId(), AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE);
                }
                if (model != null && model instanceof PriceModel) {
                    PriceModel data = (PriceModel) model;
                    mixPannelSection = PRICE_MODEL_CAROUSEL_VIEW;
                    mixPannelValue = data.getName();
                    classesFilters = new ArrayList<>();
                    if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar"))
                        filter = new ClassesFilter("", true, "PriceModel", data.getArName(), R.drawable.multiple_users_grey_fill, ClassesFilter.TYPE_ITEM);
                    else
                        filter = new ClassesFilter("", true, "PriceModel", data.getName(), R.drawable.multiple_users_grey_fill, ClassesFilter.TYPE_ITEM);
                    filter.setObject(data);

                    filter.setSelected(true);
                    classesFilters.add(filter);
                    TempStorage.setFilterList(classesFilters);
                    EventBroadcastHelper.sendNewFilterSet();
                    HomeActivity.show(context, TAB_FIND_CLASS, AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE);

                }

                if (model != null && model instanceof WorkoutModel) {
                    classesFilters = new ArrayList<>();
                    WorkoutModel data = (WorkoutModel) model;
                    mixPannelValue = data.getName();
                    mixPannelSection = CATEGORY_CAROUSEL_VIEW;

                    if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar"))

                        classesFilter = new ClassesFilter(data.getId() + "", true, "ClassActivity", data.getArName(), R.drawable.filter_activity, ClassesFilter.TYPE_ITEM);
                    else
                        classesFilter = new ClassesFilter(data.getId() + "", true, "ClassActivity", data.getName(), R.drawable.filter_activity, ClassesFilter.TYPE_ITEM);

                    classesFilter.setObject(data);

                    classesFilters.add(classesFilter);

                    TempStorage.setFilterList(classesFilters);
                    EventBroadcastHelper.sendNewFilterSet();
                    HomeActivity.show(context, TAB_FIND_CLASS, AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE);

                }
                break;
            case R.id.textViewMore:
               mixPannelValue = AppConstants.MixPanelValue.LIST;
                if (model != null && model instanceof ExploreDataModel) {
                    ExploreDataModel data = (ExploreDataModel) model;
                    if (data.getViewType().equalsIgnoreCase(GYM_VIEW)) {
                        Gym.open(context);
                        mixPannelSection = GYM_VIEW;
                    } else if (data.getViewType().equalsIgnoreCase(TRAINER_VIEW)) {
                        mixPannelSection = TRAINER_VIEW;
                        Trainers.open(context, NAVIGATION_FROM_EXPLORE);
                    } else if (data.getViewType().equalsIgnoreCase(CATEGORY_CAROUSEL_VIEW)) {
                        try {
                            mixPannelSection = CATEGORY_CAROUSEL_VIEW;
                            int index = explorePageAdapter.getList().indexOf(data);
                            if (index != -1) {
                                Object obj = explorePageAdapter.getList().get(index);
                                if (obj instanceof ExploreDataModel) {
                                    ExploreDataModel exploreData = (ExploreDataModel) obj;
                                    if (exploreData.isMoreActivityShow()) {
                                        exploreData.setMoreActivityShow(false);

                                    } else {
                                        exploreData.setMoreActivityShow(true);

                                    }
                                    explorePageAdapter.notifyItemChanged(index);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.exception(e);
                        }
                    }
                }
                if(model!=null&&model instanceof YoutubeResponse){
                    YoutubeResponse data = (YoutubeResponse) model;
                    int index = explorePageAdapter.getList().indexOf(data);
                    if(index!=-1){
                        Object obj = explorePageAdapter.getList().get(index);
                        if (obj instanceof YoutubeResponse) {
                            YoutubeResponse youtubeData = (YoutubeResponse) obj;
                            if (youtubeData.isShowMore()) {
                                youtubeData.setShowMore(false);

                            } else {
                                youtubeData.setShowMore(true);

                            }
                            explorePageAdapter.notifyItemChanged(index);

                        }
                    }
                }
                recyclerView.smoothScrollToPosition(position);
                break;
            case R.id.buttonContactUs:
                mixPannelSection = TEXT_WITH_BUTTONS_2;
                mixPannelValue = AppConstants.MixPanelValue.INTERCOM;
                Intercom.client().displayMessenger();
                break;
            case R.id.textViewTrainerName:
                if (model != null && model instanceof GymModel) {
                    mixPannelSection = TRAINER_CAROUSAL_VIEW;
                    GymModel data = (GymModel) model;
                    mixPannelValue = data.getGymNames();

                    GymProfileActivity.open(context, data.getId(), AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE);
                }
                break;

            case R.id.textViewClassName:
                // Rated Class Clicked
                if (model != null && model instanceof ExploreRatedClassModel) {
                    mixPannelSection = TOP_RATED_CLASSES;
                    ExploreRatedClassModel data = (ExploreRatedClassModel) model;
                    mixPannelValue = data.getTitle();

                    ClassProfileActivity.open(context, data.getClassSessionId(), NAVIGATION_FROM_EXPLORE);
                }
                break;
            case R.id.constraintViewPlayListItem:
                if (model != null && model instanceof Item) {
                    Item data = (Item) model;
                    mixPannelSection = "Recorded video";
                    mixPannelValue = data.getSnippet().getTitle();
                    VideoPlayerActivity.openActivityYoutube(context,data.getSnippet().getResourceId().getVideoId());
                }
                break;


        }

        MixPanel.trackExplore(mixPannelSection,mixPannelValue);
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
        swipeRefreshLayout.setRefreshing(false);

        switch (requestCode) {

            case NetworkCommunicator.RequestCode.GET_EXPLORE_DATA:
                List<ExploreDataModel> exploreModels = ((ResponseModel<List<ExploreDataModel>>) response).data;
                if (!exploreModels.isEmpty()) {
                    Collections.sort(exploreModels);
                    explorePageAdapter.addAll(exploreModels);
                    explorePageAdapter.notifyDataSetChanged();
                    for (int i=0;i<exploreModels.size();i++) {
                        ExploreDataModel data =exploreModels.get(i);
                          if(data.getWidgetType().equalsIgnoreCase("YOUTUBE")&&data.isShowDivider()){
                              youTubeModelIndex = data.getOrder();
                          }
                    }
                } else {
                    explorePageAdapter.loaderDone();
                }
                setUpYouTubeResponse();

                checkListData();
                break;

        case NetworkCommunicator.RequestCode.GET_YOUTUBE_PLAYLIST:
            youTubeResponseModel = (YoutubeResponse) response;
            setUpYouTubeResponse();
         break;
        }
    }
    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);
        switch (requestCode) {
                case NetworkCommunicator.RequestCode.GET_YOUTUBE_PLAYLIST:
                case NetworkCommunicator.RequestCode.GET_EXPLORE_DATA:
                ToastUtils.show(context,errorMessage);
                break;
        }

    }


    private void checkListData() {
       /* if (explorePageAdapter.getList().isEmpty()) {
//            layoutNoData.setVisibility(View.VISIBLE);

            textViewEmptyLayoutText.setText(R.string.no_data_notifications);
            imageViewEmptyLayoutImage.setImageResource(R.drawable.stub_notification);
        } else {
            layoutNoData.setVisibility(View.GONE);

        }*/
    }

    @Override
    public void onTabSelection(int position) {

    }

    private void setUpYouTubeResponse(){
        try{
            if(explorePageAdapter!=null&&explorePageAdapter.getItemCount()>0&&youTubeResponseModel!=null&&youTubeResponseModel.getItems()!=null&&youTubeResponseModel.getItems().size()>0&&youTubeModelIndex>-1){
                explorePageAdapter.addYouTubePlayList(youTubeModelIndex,youTubeResponseModel);
                explorePageAdapter.notifyDataSetChanged();

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }


}
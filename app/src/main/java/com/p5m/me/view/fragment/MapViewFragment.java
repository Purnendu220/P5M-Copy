package com.p5m.me.view.fragment;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.MapGymAdapter;
import com.p5m.me.data.CityLocality;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Filter;
import com.p5m.me.data.MapData;
import com.p5m.me.data.main.BranchModel;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.GymDataModel;
import com.p5m.me.data.request.BranchListRequest;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.GymProfileActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.custom.ShowSchedulesBootomDialogFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MapViewFragment extends BaseFragment implements OnMapReadyCallback,
        AdapterCallbacks,
        NetworkCommunicator.RequestListener,
        FindClass.TabClickListener, ViewPagerFragmentSelection,
        ClusterManager.OnClusterClickListener<MapData>,
        ClusterManager.OnClusterItemClickListener<MapData> {

    private GoogleMap mMap;
    @BindView(R.id.mapView)
    public MapView mapView;
    @BindView(R.id.processingProgressBar)
    public ProgressBar processingProgressBar;
    private ClusterManager<MapData> mClusterManager;

    private static AdapterCallbacks adapterCallbacks;
    private Handler handler;
    private Runnable nextScreenRunnable;
    @BindView(R.id.recyclerViewNearerClass)
    public RecyclerView recyclerViewNearerClass;

    List<LatLng> points;
    private LocationManager locationManager;
    private MapGymAdapter mapGymAdapter;
    private String date;
    private List<BranchModel> branchModel;
    private List<MapData> mapData;
    private BranchListRequest branchListRequest;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int showInScreen;
    private int fragmentPositionInViewPager;
    private boolean isShownFirstTime = true;
    private int currentPosition;
    private CustomClusterRenderer customClusterRenderer;
    private Marker mSelectedMarker;
    private Marker mLastMarker;

    public static Fragment createFragment(String date, int position, int shownIn) {
        Fragment tabFragment = new MapViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.DataKey.CLASS_DATE_STRING, date);
        bundle.putInt(AppConstants.DataKey.TAB_POSITION_INT, position);
        bundle.putInt(AppConstants.DataKey.TAB_SHOWN_IN_INT, shownIn);
        tabFragment.setArguments(bundle);

        return tabFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        initializeMapView();
               date = getArguments().getString(AppConstants.DataKey.CLASS_DATE_STRING, null);
        showInScreen = getArguments().getInt(AppConstants.DataKey.TAB_SHOWN_IN_INT, 0);
        fragmentPositionInViewPager = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);
        setNearerGymView();
        callNearerGymApi();

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

    private void callNearerGymApi() {
        processingProgressBar.setVisibility(View.VISIBLE);
        networkCommunicator.getBranchList(generateRequest(), this, false);
    }

    private BranchListRequest generateRequest() {

        if (branchListRequest == null) {
            branchListRequest = new BranchListRequest();
        }
        branchListRequest.setClassDate(date);
        branchListRequest.setUserId(TempStorage.getUser().getId());

        branchListRequest.setActivityList(null);
        branchListRequest.setGenderList(null);
        branchListRequest.setTimingList(null);
        branchListRequest.setLocationList(null);

        List<String> times = new ArrayList<>();
        List<String> activities = new ArrayList<>();
        List<String> gymList = new ArrayList<>();
        List<String> genders = new ArrayList<>();
        List<String> branches = new ArrayList<>();

        List<CityLocality> cityLocalities = new ArrayList<>();

        for (ClassesFilter classesFilter : TempStorage.getFilters()) {
            if (classesFilter.getObject() instanceof CityLocality) {
                cityLocalities.add((CityLocality) classesFilter.getObject());
            } else if (classesFilter.getObject() instanceof Filter.Time) {
                times.add(((Filter.Time) classesFilter.getObject()).getId());
            } else if (classesFilter.getObject() instanceof Filter.Gender) {
                genders.add(((Filter.Gender) classesFilter.getObject()).getId());
            } else if (classesFilter.getObject() instanceof ClassActivity) {
                activities.add(String.valueOf(((ClassActivity) classesFilter.getObject()).getId()));
            } else if (classesFilter.getObject() instanceof GymDataModel) {
                gymList.add(String.valueOf(((GymDataModel) classesFilter.getObject()).getId()));
            }
        }

        /******************************** To remove gender filter **********************************/
        genders.clear();
        genders.add(AppConstants.ApiParamValue.GENDER_BOTH);
        genders.add(TempStorage.getUser().getGender());
        /********************************************************************************/

        branchListRequest.setActivityList(activities);
        branchListRequest.setGenderList(genders);
        branchListRequest.setTimingList(times);
        branchListRequest.setLocationList(cityLocalities);
        branchListRequest.setGymList(gymList);
        branchListRequest.setBranchList(branches);

        return branchListRequest;
    }


    private void setNearerGymView() {
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);

        recyclerViewNearerClass.setLayoutManager(staggeredGridLayoutManager);
        mapGymAdapter = new MapGymAdapter(context, 2, true, this);
        recyclerViewNearerClass.setAdapter(mapGymAdapter);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(context.getApplicationContext());
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
        mClusterManager = new ClusterManager<MapData>(context, mMap);
        mClusterManager.setRenderer(new CustomClusterRenderer());
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setAnimation(true);
    }

    public void initializeMapView() {
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }


    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.textViewShowSchedule:
                ShowSchedulesBootomDialogFragment showSchedulesBootomDialogFragment =
                        ShowSchedulesBootomDialogFragment.newInstance(context, date, Collections.singletonList(branchModel.get(position).getBranchId()), branchModel.get(position), this);
                showSchedulesBootomDialogFragment.show(((HomeActivity) context).getSupportFragmentManager(),
                        "show_schedule");
                break;

            case R.id.imageViewOfGym:
            case R.id.textViewBranchName:
            case R.id.textViewGymName:
                if (model instanceof BranchModel) {
                    BranchModel branchModel = (BranchModel) model;
                    GymProfileActivity.open(context, branchModel.getGymId(), AppConstants.AppNavigation.SHOWN_IN_MAP_VIEW);
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
    public void onTabClick(int position, String selectedDate) {
        date = selectedDate;
        currentPosition = position;
        if (((fragmentPositionInViewPager == position))
                || ((fragmentPositionInViewPager == position) && isShownFirstTime)) {
            isShownFirstTime = false;

            callNearerGymApi();
        } else {
            callNearerGymApi();
        }
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        processingProgressBar.setVisibility(View.GONE);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.BRANCH_LIST:
                mapGymAdapter.clearAll();
                mMap.clear();
                mClusterManager.clearItems();
                branchModel = ((ResponseModel<List<BranchModel>>) response).data;
                if (!branchModel.isEmpty()) {
                    setUpClusterer();
                    mapGymAdapter.addAllClass(branchModel);
                }
                break;
        }
    }


    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        processingProgressBar.setVisibility(View.GONE);
        switch (requestCode) {

            case NetworkCommunicator.RequestCode.RCOMENDED_CLASS_LIST:
                ToastUtils.showLong(context, errorMessage);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newFilter(Events.NewFilter newFilter) {
        if (showInScreen == AppConstants.AppNavigation.SHOWN_IN_HOME_MAP_CLASSES) {
            mapGymAdapter.clearAll();
            mapGymAdapter.notifyDataSetChanged();
            mMap.clear();

//            if ((fragmentPositionInViewPager == currentPosition)) {
            onTabSelection(currentPosition);
//                onTabClick(FindClass.SELECTED_POSITION);
//            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newFilter(Events.RefreshClassList refreshClassList) {
        if (showInScreen == AppConstants.AppNavigation.SHOWN_IN_HOME_MAP_CLASSES) {
            mapGymAdapter.clearAll();
            mapGymAdapter.notifyDataSetChanged();
            mMap.clear();
//            if ((fragmentPositionInViewPager == currentPosition)) {
            onTabSelection(currentPosition);
//            }
        }
    }

    @Override
    public void onTabSelection(int position) {
        LogUtils.debug("Fragment Position: " + fragmentPositionInViewPager + " onTabSelection " + position + " vp position " + FindClass.SELECTED_POSITION);

        currentPosition = position;
        if (((fragmentPositionInViewPager == position))
                || ((fragmentPositionInViewPager == position) && isShownFirstTime)) {
            isShownFirstTime = false;

            callNearerGymApi();
        } else {
            if (mapGymAdapter.getList().isEmpty()) {
                callNearerGymApi();
            }
        }
    }


    private void setUpClusterer() {
        if (branchModel.size() > 2)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(branchModel.get(0).getLatitude(), branchModel.get(0).getLongitude()), 1));
        else {
            LatLngBounds.Builder builder = LatLngBounds.builder();
            for (BranchModel item : branchModel) {
                LatLng latLng = new LatLng(item.getLatitude(), item.getLongitude());
                builder.include(latLng);
            }
            final LatLngBounds bounds = builder.build();

            try {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        addItems();
        mClusterManager.cluster();

    }

    private void addItems() {
        for (int i = 0; i < branchModel.size(); i++) {
            MapData data = new MapData(branchModel.get(i).getLatitude(),
                    branchModel.get(i).getLongitude(), branchModel.get(i).getBranchName());
            mClusterManager.addItem(data);

        }
    }

    @Override
    public boolean onClusterClick(Cluster<MapData> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onClusterItemClick(MapData mapData) {

        int position = -1;
        for (int i = 0; i < branchModel.size(); i++) {
            if ((branchModel.get(i).getLatitude()) == mapData.getPosition().latitude &&
                    (branchModel.get(i).getLongitude() == mapData.getPosition().longitude)) {
                position = i;
                break;
            }
        }
      /*  customClusterRenderer = new CustomClusterRenderer();
        mSelectedMarker = customClusterRenderer.getMarker(mapData);
        updateSelectedMarker();
*/
        staggeredGridLayoutManager.smoothScrollToPosition(recyclerViewNearerClass, new RecyclerView.State(), position);
        return false;
    }

    private void updateSelectedMarker() {
        if (mSelectedMarker != null) {
            mSelectedMarker.setIcon(
                    BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_RED));
        }
       if (mLastMarker != null) {
            mLastMarker.setIcon(
                    BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_BLUE));
        }
        mLastMarker=mSelectedMarker;

    }

    private class CustomClusterRenderer extends DefaultClusterRenderer<MapData> {

        public CustomClusterRenderer() {
            super(getApplicationContext(), mMap, mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MapData mapData, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MapData> cluster, MarkerOptions markerOptions) {
            super.onBeforeClusterRendered(cluster, markerOptions);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            return cluster.getSize() > 2;
        }
    }
}

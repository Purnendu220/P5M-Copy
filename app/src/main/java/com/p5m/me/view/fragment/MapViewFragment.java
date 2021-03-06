package com.p5m.me.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.MapGymAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.CityLocality;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Filter;
import com.p5m.me.data.MapData;
import com.p5m.me.data.PriceModel;
import com.p5m.me.data.WorkoutModel;
import com.p5m.me.data.main.BranchModel;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.GymDataModel;
import com.p5m.me.data.request.BranchListRequest;
import com.p5m.me.eventbus.Events;
import com.p5m.me.eventbus.GlobalBus;
import com.p5m.me.fxn.utility.Constants;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.GymProfileActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.custom.ShowSchedulesBootomDialogFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.p5m.me.utils.Utility.REQUEST_LOCATION;

public class MapViewFragment extends BaseFragment implements OnMapReadyCallback,
        AdapterCallbacks,
        NetworkCommunicator.RequestListener,
        FindClass.TabClickListener, ViewPagerFragmentSelection,
        ClusterManager.OnClusterClickListener<MapData>,
        ClusterManager.OnClusterItemClickListener<MapData>,
        LocationListener {

    private static final int LOCATION_REQUEST_PERMISSION = 111;
    private GoogleMap mMap;
    @BindView(R.id.mapView)
    public MapView mapView;
    @BindView(R.id.processingProgressBar)
    public ProgressBar processingProgressBar;
    private ClusterManager<MapData> mClusterManager;

    @BindView(R.id.recyclerViewNearerClass)
    public RecyclerView recyclerViewNearerClass;

    private MapGymAdapter mapGymAdapter;
    private String date;
    private List<BranchModel> branchModel;
    private BranchListRequest branchListRequest;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private int showInScreen;
    private int fragmentPositionInViewPager;
    private int currentPosition;
    private CustomClusterRenderer customClusterRenderer;
    private Marker mSelectedMarker;
    private Marker mLastMarker;
    private boolean isShownFirstTime = true;
    private LocationManager locationManager;
    private List<MapData> mapDataList;
    private Circle mCircle;
    private LatLng position = null;
    private int pastVisiblesItems;
    private boolean isMarkerClick = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 1 minute
    private String dateEnglish;


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
        return inflater.inflate(R.layout.fragment_map_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());
        initializeMapView();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        SnapHelper mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(recyclerViewNearerClass);
        date = getArguments().getString(AppConstants.DataKey.CLASS_DATE_STRING, null);
        showInScreen = getArguments().getInt(AppConstants.DataKey.TAB_SHOWN_IN_INT, 0);
        fragmentPositionInViewPager = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);
        position = RefrenceWrapper.getRefrenceWrapper(context).getLatLng();
        setNearerGymView();
        MixPanel.trackFindClass(showInScreen, false);
    }

    private void checkLocation() {
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_PERMISSION);


        } else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
                return;
            }


        }
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
        if (Constants.LANGUAGE == Locale.ENGLISH) {
            branchListRequest.setClassDate(date);

        } else {
            branchListRequest.setClassDate(DateUtils.dateConverter(date));
        }
        branchListRequest.setUserId(TempStorage.getUser().getId());

        branchListRequest.setActivityList(null);
        branchListRequest.setGenderList(null);
        branchListRequest.setTimingList(null);
        branchListRequest.setLocationList(null);
        branchListRequest.setFitnessLevelList(null);
        branchListRequest.setPriceModelList(null);

        List<String> times = new ArrayList<>();
        List<String> activities = new ArrayList<>();
        List<String> gymList = new ArrayList<>();
        List<String> genders = new ArrayList<>();
        List<String> branches = new ArrayList<>();
        List<String> priceModelList = new ArrayList<>();
        List<String> fitnessLevelList = new ArrayList<>();

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
            } else if (classesFilter.getObject() instanceof PriceModel) {
                priceModelList.add(String.valueOf(((PriceModel) classesFilter.getObject()).getValue()));
            } else if (classesFilter.getObject() instanceof Filter.FitnessLevel) {
                fitnessLevelList.add(String.valueOf(((Filter.FitnessLevel) classesFilter.getObject()).getLevel()));
            } else if (classesFilter.getObject() instanceof WorkoutModel) {
                activities.add(String.valueOf(((WorkoutModel) classesFilter.getObject()).getId()));
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
        branchListRequest.setFitnessLevelList(fitnessLevelList);
        branchListRequest.setPriceModelList(priceModelList);

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
        mapDataList = new ArrayList<MapData>();
        mSelectedMarker = null;
        mLastMarker = null;
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
        checkLocation();
        callNearerGymApi();


    }

    public void initializeMapView() {
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
            callNearerGymApi();
        }

    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()) {
            case R.id.textViewShowSchedule:
                MixPanel.trackMapViewClick("schedule", branchModel.get(position));
                if (Constants.LANGUAGE == Locale.ENGLISH) {
                    dateEnglish = date;

                } else {
                    dateEnglish=DateUtils.dateConverter(date);
                }
                ShowSchedulesBootomDialogFragment showSchedulesBootomDialogFragment =
                        ShowSchedulesBootomDialogFragment.newInstance(context, dateEnglish, Collections.singletonList(branchModel.get(position).getBranchId()), branchModel.get(position), this);
                showSchedulesBootomDialogFragment.show(((HomeActivity) context).getSupportFragmentManager(),
                        "show_schedule");
                break;

            case R.id.imageViewOfGym:
            case R.id.textViewBranchName:
            case R.id.textViewGymName:
                if (model instanceof BranchModel) {
                    BranchModel branchModel = (BranchModel) model;
                    MixPanel.trackMapViewClick("visit_gym_profile", branchModel);
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
        callNearerGymApi();
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        processingProgressBar.setVisibility(View.GONE);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.BRANCH_LIST:
                mapGymAdapter.clearAll();
                mapDataList.clear();
                if (mMap != null)
                    mMap.clear();
                mSelectedMarker = null;
                mLastMarker = null;
                mClusterManager.clearItems();

                branchModel = ((ResponseModel<List<BranchModel>>) response).data;
                if (!branchModel.isEmpty()) {
                    addItems();
                    setUpCluster();
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
            onTabSelection(currentPosition);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void newFilter(Events.RefreshClassList refreshClassList) {
        if (showInScreen == AppConstants.AppNavigation.SHOWN_IN_HOME_MAP_CLASSES) {
            mapGymAdapter.clearAll();
            mapGymAdapter.notifyDataSetChanged();
            mMap.clear();
            onTabSelection(currentPosition);
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


    private void setUpCluster() {
        {
            LatLngBounds.Builder builder = LatLngBounds.builder();
            if (mapDataList.size() > 0) {
                for (MapData item : mapDataList) {
                    builder.include(item.getPosition());
                }
                try {
                    final LatLngBounds bounds = builder.build();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                LatLng latLng;
                for (BranchModel item : branchModel) {
                    latLng = new LatLng(item.getLatitude(), item.getLongitude());
                    builder.include(latLng);
                }
                try {
                    final LatLngBounds bounds = builder.build();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        mClusterManager.cluster();
    }

    private void addItems() {
        if (position != null) {
            CircleOptions circleOptions = new CircleOptions().center(position).radius(50000).strokeColor(Color.TRANSPARENT)
                    .fillColor(Color.TRANSPARENT);
            mCircle = mMap.addCircle(circleOptions);
        } else {
            LogUtils.debug("Position is null");
        }
        for (int i = 0; i < branchModel.size(); i++) {
            {
                MapData data = new MapData(branchModel.get(i).getLatitude(),
                        branchModel.get(i).getLongitude(), branchModel.get(i).getBranchName());
                mClusterManager.addItem(data);

                if (mCircle != null) {
                    float[] distance = new float[2];
                    Location.distanceBetween(branchModel.get(i).getLatitude(), branchModel.get(i).getLongitude(),
                            mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);

                    if (distance[0] > mCircle.getRadius()) {
                        LogUtils.debug("MAp data is greater");

                    } else {
                        LogUtils.debug("MAp data is less");

                        mapDataList.add(data);
                    }
                }


            }
        }

        mapGymAdapter.addAllClass(branchModel);
        findScrolledItem();

    }

    private void findScrolledItem() {

        recyclerViewNearerClass.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] firstVisibleItems = null;
                if (!isMarkerClick) {
                    firstVisibleItems = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(firstVisibleItems);
                    if (firstVisibleItems != null && firstVisibleItems.length > 0 &&
                            firstVisibleItems[0] != -1 && firstVisibleItems[0] != pastVisiblesItems) {

                        pastVisiblesItems = firstVisibleItems[0];
                        Collection<Marker> markers = mClusterManager.getMarkerCollection().getMarkers();

                        if (markers != null)
                            for (Marker m : markers) {

                                if (branchModel.get(pastVisiblesItems).getLatitude() == m.getPosition().latitude &&
                                        branchModel.get(pastVisiblesItems).getLongitude() == m.getPosition().longitude) {
                                    mSelectedMarker = m;
                                    if (mSelectedMarker != mLastMarker)
                                        updateSelectedMarker();
                                    break;
                                }
                            }
                    }
                } else
                    isMarkerClick = false;
            }

        });
    }

    @Override
    public boolean onClusterClick(Cluster<MapData> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        try {
            final LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));

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
        staggeredGridLayoutManager.scrollToPosition(position);
        customClusterRenderer = (CustomClusterRenderer) mClusterManager.getRenderer();
        isMarkerClick = true;
        mSelectedMarker = customClusterRenderer.getMarker(mapData);

        if (mSelectedMarker != mLastMarker)
            updateSelectedMarker();
        return false;
    }

    private void updateSelectedMarker() {
        try {
            if (mSelectedMarker != null) {
                mSelectedMarker.setIcon(
                        BitmapDescriptorFactory.fromResource(R.drawable.red_marker));


            }
            if (mLastMarker != null) {
                mLastMarker.setIcon(
                        BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));
            }
            mLastMarker = mSelectedMarker;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getLocation() {
        Double lat = null;
        Double lang = null;
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
            List<String> providers = locationManager.getProviders(true);
            Location loc = null;
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (loc == null || l.getAccuracy() < loc.getAccuracy()) {
                    loc = l;
                }
            }

//            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lang = loc.getLongitude();
                TempStorage.setLat(lat + "");
                TempStorage.setLng(lang + "");
                position = new LatLng(lat, lang);
            }

        } catch (SecurityException e) {
            e.printStackTrace();

        }


    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            double lat = location.getLatitude();
            double lang = location.getLongitude();
            position = new LatLng(lat, lang);
            CircleOptions circleOptions = new CircleOptions().center(position).radius(50000).strokeColor(Color.TRANSPARENT)
                    .fillColor(Color.TRANSPARENT);
            mCircle = mMap.addCircle(circleOptions);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private class CustomClusterRenderer extends DefaultClusterRenderer<MapData> {

        public CustomClusterRenderer() {
            super(getApplicationContext(), mMap, mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MapData mapData, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MapData> cluster, MarkerOptions markerOptions) {
            super.onBeforeClusterRendered(cluster, markerOptions);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            return cluster.getSize() > cluster.getSize();
        }
    }

}

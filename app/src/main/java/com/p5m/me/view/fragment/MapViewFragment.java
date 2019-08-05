package com.p5m.me.view.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.MapGymAdapter;
import com.p5m.me.data.CityLocality;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Filter;
import com.p5m.me.data.main.BranchModel;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.GymDataModel;
import com.p5m.me.data.request.BranchListRequest;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.custom.ShowSchedulesBootomDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapViewFragment extends BaseFragment implements OnMapReadyCallback ,AdapterCallbacks, NetworkCommunicator.RequestListener,
        FindClass.TabClickListener {

    private GoogleMap mMap;
    @BindView(R.id.mapView)
    public MapView mapView;
    @BindView(R.id.processingProgressBar)
    public ProgressBar processingProgressBar;

    private static AdapterCallbacks adapterCallbacks;

    @BindView(R.id.recyclerViewNearerClass)
    public RecyclerView recyclerViewNearerClass;

    List<LatLng> points;
    private LocationManager locationManager;
    private MapGymAdapter mapGymAdapter;
    private String date;
    private List<BranchModel> branchModel;
    private BranchListRequest branchListRequest;

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
//        setProgressBarAnimation();
        initializeMapView();

        date = getArguments().getString(AppConstants.DataKey.CLASS_DATE_STRING, null);
        setNearerGymView();
        getLocation();
    }

    private void setProgressBarAnimation() {
        processingProgressBar = new android.widget.ProgressBar(
                context,
                null,
                android.R.attr.progressBarStyle);

        processingProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
    }


    private void getLocation() {
        Double lat = null;
        Double lang = null;
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                lat = loc.getLatitude();
                lang = loc.getLongitude();
            }

        } catch (SecurityException e) {
            e.printStackTrace();

        }
            callNearerGymApi();



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
        recyclerViewNearerClass.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNearerClass.setHasFixedSize(true);
        mapGymAdapter = new MapGymAdapter(context, 2, true,this);
        recyclerViewNearerClass.setAdapter(mapGymAdapter);
    }

    private void addMarker(LatLng latLng, String title) {
        if(mMap!=null)
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker())
                );

    }

    private void moveCamera(LatLng latLng) {
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                latLng, 8);
        mMap.animateCamera(location);
    }

    private void createLatLngList() {
        points = new ArrayList<LatLng>();

        for (int i = 0; i < branchModel.size(); i++) {
            points.add(new LatLng(branchModel.get(i).getLatitude(), branchModel.get(i).getLongitude()));
        }
        for (int i = 0; i < points.size(); i++) {
            addMarker(points.get(i), branchModel.get(i).getBranchName());
        }
        moveCamera(points.get(0));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(context.getApplicationContext());
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);

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
                BranchModel branchModel =(BranchModel) model;
                ShowSchedulesBootomDialogFragment showSchedulesBootomDialogFragment =
                        ShowSchedulesBootomDialogFragment.newInstance(context,branchModel,date, Collections.singletonList(branchModel.getBranchId()),this );
                showSchedulesBootomDialogFragment.show(((HomeActivity) context).getSupportFragmentManager(),
                        "show_schedule");


                ToastUtils.show(getActivity(),"Click Show Schedule");
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
    public void onTabClick(String selectedDate) {
        date = selectedDate;
        getLocation();
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        processingProgressBar.setVisibility(View.GONE);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.BRANCH_LIST:

                mapGymAdapter.clearAll();
                 branchModel = ((ResponseModel<List<BranchModel>>) response).data;
                if (!branchModel.isEmpty()) {
                    createLatLngList();
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

}

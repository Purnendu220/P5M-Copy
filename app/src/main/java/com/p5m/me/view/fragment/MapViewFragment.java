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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.p5m.me.data.RecomendedClassData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.helper.CancelBookingBottomDialogFragment;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.custom.ShowSchedulesBootomDialogFragment;

import java.util.ArrayList;
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

    double pointY[] = {105.45526, 105.57364, 105.53505, 105.45523, 105.51962, 105.77320};
    double pointX[] = {9.99222, 9.88347, 9.84184, 9.77197, 9.55501, 9.67768};
    List<LatLng> points;
    private LocationManager locationManager;
    private MapGymAdapter mapGymAdapter;
    private String date;
    private List<ClassModel> classModels;

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
            callNearerGymApi(lat, lang);



    }


    private void callNearerGymApi(Double latitude, Double longitude) {
        processingProgressBar.setVisibility(View.VISIBLE);
        networkCommunicator.getRcomendedClassList(date, latitude, longitude, this, false);
    }



    private void setNearerGymView() {
        recyclerViewNearerClass.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNearerClass.setHasFixedSize(true);
        mapGymAdapter = new MapGymAdapter(context, 2, true,this);
        recyclerViewNearerClass.setAdapter(mapGymAdapter);
    }

    private void addMarker(LatLng latLng, String title, String snippet) {
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

        for (int i = 0; i < pointX.length; i++) {
            points.add(new LatLng(pointX[i], pointY[i]));
        }
        for (int i = 0; i < points.size(); i++) {
            addMarker(points.get(i), "n", "njk");
        }
        moveCamera(points.get(0));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(context.getApplicationContext());
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
        createLatLngList();
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
                        ShowSchedulesBootomDialogFragment.newInstance(context,classModels.get(0), this );
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
            case NetworkCommunicator.RequestCode.RCOMENDED_CLASS_LIST:

                mapGymAdapter.clearAll();
                 classModels = ((ResponseModel<List<ClassModel>>) response).data;
                if (!classModels.isEmpty()) {
                    mapGymAdapter.addAllClass(classModels);
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

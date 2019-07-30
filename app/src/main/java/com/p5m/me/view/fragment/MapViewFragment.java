package com.p5m.me.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapViewFragment extends BaseFragment implements OnMapReadyCallback ,AdapterCallbacks{

    private GoogleMap mMap;
    @BindView(R.id.mapView)
    public MapView mapView;

    private static AdapterCallbacks adapterCallbacks;

    @BindView(R.id.recyclerViewNearerClass)
    public RecyclerView recyclerViewNearerClass;
    double pointY[] = {105.45526, 105.57364, 105.53505, 105.45523, 105.51962, 105.77320};
    double pointX[] = {9.99222, 9.88347, 9.84184, 9.77197, 9.55501, 9.67768};
    List<LatLng> points;

    public MapViewFragment() {
//        MapViewFragment.adapterCallbacks = adapterCallbacks;
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


        setNearerGymView();
    }



    private void setNearerGymView() {
        recyclerViewNearerClass.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNearerClass.setHasFixedSize(true);
        MapGymAdapter mapGymAdapter = new MapGymAdapter(context, 2, true,this);
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

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
}

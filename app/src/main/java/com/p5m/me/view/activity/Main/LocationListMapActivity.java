package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.BranchMapListAdapter;
import com.p5m.me.data.main.GymBranchDetail;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationListMapActivity extends BaseActivity implements OnMapReadyCallback, AdapterCallbacks {

    public static void openActivity(Context context, List<GymBranchDetail> list) {
        LocationListMapActivity.list = list;
        context.startActivity(new Intent(context, LocationListMapActivity.class));
    }

    @BindView(R.id.imageViewBack)
    public ImageView imageViewBack;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    public static List<GymBranchDetail> list;
    private HashMap<GymBranchDetail, LatLng> gymBranchDetailLatLngHashMap;
    private BranchMapListAdapter branchMapListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list_map);

        ButterKnife.bind(activity);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        List<GymBranchDetail> gymBranchDetails = new ArrayList<>(list.size());

        for (GymBranchDetail gymBranchDetail : list) {
            if (gymBranchDetail.getLatitude() != 0 && gymBranchDetail.getLongitude() != 0) {
                gymBranchDetails.add(gymBranchDetail);
            }
        }

        list = gymBranchDetails;
        gymBranchDetailLatLngHashMap = new HashMap<>(list.size());

        branchMapListAdapter = new BranchMapListAdapter(context, false, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        recyclerView.setAdapter(branchMapListAdapter);

        branchMapListAdapter.addAll(list);
        branchMapListAdapter.notifyDataSetChanged();
        onTrackingNotification();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        try {
            for (GymBranchDetail gymBranchDetail : list) {
                LatLng latLng = new LatLng(gymBranchDetail.getLatitude(), gymBranchDetail.getLongitude());
                gymBranchDetailLatLngHashMap.put(gymBranchDetail, latLng);

                googleMap.addMarker(new MarkerOptions().position(latLng)
                        .title(gymBranchDetail.getBranchName().toUpperCase()).anchor(0.5f, 0.5f));
            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    gymBranchDetailLatLngHashMap.get(list.get(0)), 15));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        finish();
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        try {
            if (model instanceof GymBranchDetail) {
                GymBranchDetail gymBranchDetail = (GymBranchDetail) model;
                branchMapListAdapter.setSelectedGymBranchDetail(gymBranchDetail);
                branchMapListAdapter.notifyDataSetChanged();

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        gymBranchDetailLatLngHashMap.get(gymBranchDetail), 15));

                Helper.openMap(context, gymBranchDetail.getLatitude(), gymBranchDetail.getLongitude(), gymBranchDetail.getBranchName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
    }

    @Override
    public void onShowLastItem() {
    }
}

package com.p5m.me.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.NearerGymAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapViewFragment extends BaseFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static AdapterCallbacks adapterCallbacks;

    @BindView(R.id.recyclerViewNearerClass)
    public RecyclerView recyclerViewNearerClass;

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

        ButterKnife.bind(this,getView());

        if(getActivity()!=null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(this);
            }
        }
        setNearerGymView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void setNearerGymView() {
        recyclerViewNearerClass.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNearerClass.setHasFixedSize(true);
        NearerGymAdapter nearerGymAdapter = new NearerGymAdapter(context, 2, true);
        recyclerViewNearerClass.setAdapter(nearerGymAdapter);
    }
}

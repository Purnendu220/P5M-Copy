package com.p5m.me.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapData implements ClusterItem {
    private final LatLng mPosition;
    private String mTitle = null;
//    private String mSnippet = null;

    public MapData(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    public MapData(double lat, double lng, String title) {
        mPosition = new LatLng(lat, lng);
//        mTitle = title;
//        mSnippet = snippet;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}

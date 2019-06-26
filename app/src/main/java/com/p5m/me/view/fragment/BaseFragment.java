package com.p5m.me.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.p5m.me.restapi.NetworkCommunicator;

public class BaseFragment extends Fragment {

    public Activity activity;
    public Context context;

    public NetworkCommunicator networkCommunicator;

    public BaseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        context = getActivity();

        networkCommunicator = NetworkCommunicator.getInstance(context);

    }

}

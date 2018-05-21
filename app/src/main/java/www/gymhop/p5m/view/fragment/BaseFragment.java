package www.gymhop.p5m.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import www.gymhop.p5m.restapi.NetworkCommunicator;

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

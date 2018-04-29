package www.gymhop.p5m.helper;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import www.gymhop.p5m.adapters.AdapterCallbacks;

/**
 * Created by Varun John on 4/19/2018.
 */

public class ClassMiniListListenerHelper implements AdapterCallbacks{

    public Context context;
    public Activity activity;
    private final AdapterCallbacks adapterCallbacks;

    public ClassMiniListListenerHelper(Context context, Activity activity, AdapterCallbacks adapterCallbacks) {
        this.context = context;
        this.activity = activity;
        this.adapterCallbacks = adapterCallbacks;
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {
        adapterCallbacks.onShowLastItem();
    }
}

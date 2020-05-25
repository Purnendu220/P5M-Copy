// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.fragment;

import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.gms.maps.MapView;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MapViewFragment_ViewBinding implements Unbinder {
  private MapViewFragment target;

  @UiThread
  public MapViewFragment_ViewBinding(MapViewFragment target, View source) {
    this.target = target;

    target.mapView = Utils.findRequiredViewAsType(source, R.id.mapView, "field 'mapView'", MapView.class);
    target.processingProgressBar = Utils.findRequiredViewAsType(source, R.id.processingProgressBar, "field 'processingProgressBar'", ProgressBar.class);
    target.recyclerViewNearerClass = Utils.findRequiredViewAsType(source, R.id.recyclerViewNearerClass, "field 'recyclerViewNearerClass'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MapViewFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mapView = null;
    target.processingProgressBar = null;
    target.recyclerViewNearerClass = null;
  }
}

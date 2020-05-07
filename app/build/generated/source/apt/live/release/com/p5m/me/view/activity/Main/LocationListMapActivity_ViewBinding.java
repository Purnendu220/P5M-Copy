// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LocationListMapActivity_ViewBinding implements Unbinder {
  private LocationListMapActivity target;

  private View view7f0a0169;

  @UiThread
  public LocationListMapActivity_ViewBinding(LocationListMapActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LocationListMapActivity_ViewBinding(final LocationListMapActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.imageViewBack, "field 'imageViewBack' and method 'imageViewBack'");
    target.imageViewBack = Utils.castView(view, R.id.imageViewBack, "field 'imageViewBack'", ImageView.class);
    view7f0a0169 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imageViewBack(p0);
      }
    });
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LocationListMapActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewBack = null;
    target.recyclerView = null;

    view7f0a0169.setOnClickListener(null);
    view7f0a0169 = null;
  }
}

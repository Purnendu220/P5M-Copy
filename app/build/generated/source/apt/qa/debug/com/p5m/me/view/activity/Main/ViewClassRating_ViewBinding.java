// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ViewClassRating_ViewBinding implements Unbinder {
  private ViewClassRating target;

  @UiThread
  public ViewClassRating_ViewBinding(ViewClassRating target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ViewClassRating_ViewBinding(ViewClassRating target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.recyclerViewProfile = Utils.findRequiredViewAsType(source, R.id.recyclerViewProfile, "field 'recyclerViewProfile'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipeRefreshLayout, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ViewClassRating target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recyclerViewProfile = null;
    target.swipeRefreshLayout = null;
  }
}

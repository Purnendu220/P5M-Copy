// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.fragment;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyProfile_ViewBinding implements Unbinder {
  private MyProfile target;

  @UiThread
  public MyProfile_ViewBinding(MyProfile target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipeRefreshLayout, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.appBarLayout, "field 'appBarLayout'", AppBarLayout.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MyProfile target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.swipeRefreshLayout = null;
    target.appBarLayout = null;
    target.toolbar = null;
  }
}

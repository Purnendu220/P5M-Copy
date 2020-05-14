// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.p5m.me.R;
import com.p5m.me.view.activity.custom.MaxHeightScrollView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FilterActivity_ViewBinding implements Unbinder {
  private FilterActivity target;

  @UiThread
  public FilterActivity_ViewBinding(FilterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FilterActivity_ViewBinding(FilterActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.appBarLayout, "field 'appBarLayout'", AppBarLayout.class);
    target.textViewFindClasses = Utils.findRequiredView(source, R.id.textViewFindClasses, "field 'textViewFindClasses'");
    target.flexBoxLayout = Utils.findRequiredViewAsType(source, R.id.flexBoxLayout, "field 'flexBoxLayout'", FlexboxLayout.class);
    target.scrollView = Utils.findRequiredViewAsType(source, R.id.scrollView, "field 'scrollView'", MaxHeightScrollView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FilterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recyclerView = null;
    target.appBarLayout = null;
    target.textViewFindClasses = null;
    target.flexBoxLayout = null;
    target.scrollView = null;
  }
}

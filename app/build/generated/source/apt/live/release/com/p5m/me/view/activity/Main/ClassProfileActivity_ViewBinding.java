// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClassProfileActivity_ViewBinding implements Unbinder {
  private ClassProfileActivity target;

  private View view7f0a03b4;

  private View view7f0a03b5;

  @UiThread
  public ClassProfileActivity_ViewBinding(ClassProfileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ClassProfileActivity_ViewBinding(final ClassProfileActivity target, View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.appBarLayout = Utils.findRequiredViewAsType(source, R.id.appBarLayout, "field 'appBarLayout'", AppBarLayout.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.layoutBottomTabs = Utils.findRequiredViewAsType(source, R.id.layoutBottomTabs, "field 'layoutBottomTabs'", LinearLayout.class);
    target.layoutButton = Utils.findRequiredView(source, R.id.layoutButton, "field 'layoutButton'");
    view = Utils.findRequiredView(source, R.id.textViewBook, "field 'textViewBook' and method 'textViewBook'");
    target.textViewBook = Utils.castView(view, R.id.textViewBook, "field 'textViewBook'", TextView.class);
    view7f0a03b4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.textViewBook();
      }
    });
    view = Utils.findRequiredView(source, R.id.textViewBookWithFriend, "field 'textViewBookWithFriend' and method 'textViewBookWithFriend'");
    target.textViewBookWithFriend = Utils.castView(view, R.id.textViewBookWithFriend, "field 'textViewBookWithFriend'", TextView.class);
    view7f0a03b5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.textViewBookWithFriend();
      }
    });
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipeRefreshLayout, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ClassProfileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.appBarLayout = null;
    target.toolbar = null;
    target.layoutBottomTabs = null;
    target.layoutButton = null;
    target.textViewBook = null;
    target.textViewBookWithFriend = null;
    target.swipeRefreshLayout = null;

    view7f0a03b4.setOnClickListener(null);
    view7f0a03b4 = null;
    view7f0a03b5.setOnClickListener(null);
    view7f0a03b5 = null;
  }
}

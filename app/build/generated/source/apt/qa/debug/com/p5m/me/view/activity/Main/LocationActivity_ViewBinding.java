// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LocationActivity_ViewBinding implements Unbinder {
  private LocationActivity target;

  @UiThread
  public LocationActivity_ViewBinding(LocationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LocationActivity_ViewBinding(LocationActivity target, View source) {
    this.target = target;

    target.root = Utils.findRequiredView(source, R.id.root, "field 'root'");
  }

  @Override
  @CallSuper
  public void unbind() {
    LocationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.root = null;
  }
}

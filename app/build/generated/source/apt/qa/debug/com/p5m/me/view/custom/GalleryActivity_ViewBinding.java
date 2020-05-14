// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.custom;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GalleryActivity_ViewBinding implements Unbinder {
  private GalleryActivity target;

  @UiThread
  public GalleryActivity_ViewBinding(GalleryActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public GalleryActivity_ViewBinding(GalleryActivity target, View source) {
    this.target = target;

    target.viewPager = Utils.findRequiredViewAsType(source, R.id.pager, "field 'viewPager'", ViewPager.class);
    target.textViewCounter = Utils.findRequiredViewAsType(source, R.id.textViewCounter, "field 'textViewCounter'", TextView.class);
    target.root = Utils.findRequiredView(source, R.id.root, "field 'root'");
  }

  @Override
  @CallSuper
  public void unbind() {
    GalleryActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewPager = null;
    target.textViewCounter = null;
    target.root = null;
  }
}

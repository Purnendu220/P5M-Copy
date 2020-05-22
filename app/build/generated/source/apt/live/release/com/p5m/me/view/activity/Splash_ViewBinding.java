// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Splash_ViewBinding implements Unbinder {
  private Splash target;

  @UiThread
  public Splash_ViewBinding(Splash target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public Splash_ViewBinding(Splash target, View source) {
    this.target = target;

    target.imageViewImage = Utils.findRequiredViewAsType(source, R.id.imageViewImage, "field 'imageViewImage'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Splash target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewImage = null;
  }
}

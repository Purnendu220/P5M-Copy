// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.LoginRegister;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.p5m.me.R;
import com.p5m.me.view.activity.custom.MyViewPager;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RegistrationActivity_ViewBinding implements Unbinder {
  private RegistrationActivity target;

  private View view7f0a016a;

  @UiThread
  public RegistrationActivity_ViewBinding(RegistrationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RegistrationActivity_ViewBinding(final RegistrationActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.imageViewBack, "field 'imageViewBack' and method 'imageViewBack'");
    target.imageViewBack = Utils.castView(view, R.id.imageViewBack, "field 'imageViewBack'", ImageView.class);
    view7f0a016a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imageViewBack(p0);
      }
    });
    target.viewPager = Utils.findRequiredViewAsType(source, R.id.viewPager, "field 'viewPager'", MyViewPager.class);
    target.layoutIndicator = Utils.findRequiredViewAsType(source, R.id.layoutIndicator, "field 'layoutIndicator'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RegistrationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewBack = null;
    target.viewPager = null;
    target.layoutIndicator = null;

    view7f0a016a.setOnClickListener(null);
    view7f0a016a = null;
  }
}

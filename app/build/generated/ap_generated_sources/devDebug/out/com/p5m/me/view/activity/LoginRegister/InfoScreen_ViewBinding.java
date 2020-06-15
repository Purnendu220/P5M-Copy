// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.LoginRegister;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class InfoScreen_ViewBinding implements Unbinder {
  private InfoScreen target;

  private View view7f0a0087;

  private View view7f0a0425;

  @UiThread
  public InfoScreen_ViewBinding(InfoScreen target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public InfoScreen_ViewBinding(final InfoScreen target, View source) {
    this.target = target;

    View view;
    target.viewPager = Utils.findRequiredViewAsType(source, R.id.viewPager, "field 'viewPager'", ViewPager.class);
    target.layoutIndicator = Utils.findRequiredViewAsType(source, R.id.layoutIndicator, "field 'layoutIndicator'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.buttonRegister, "field 'buttonRegister' and method 'register'");
    target.buttonRegister = Utils.castView(view, R.id.buttonRegister, "field 'buttonRegister'", Button.class);
    view7f0a0087 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.register();
      }
    });
    view = Utils.findRequiredView(source, R.id.textViewLogin, "method 'login'");
    view7f0a0425 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.login();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    InfoScreen target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewPager = null;
    target.layoutIndicator = null;
    target.buttonRegister = null;

    view7f0a0087.setOnClickListener(null);
    view7f0a0087 = null;
    view7f0a0425.setOnClickListener(null);
    view7f0a0425 = null;
  }
}

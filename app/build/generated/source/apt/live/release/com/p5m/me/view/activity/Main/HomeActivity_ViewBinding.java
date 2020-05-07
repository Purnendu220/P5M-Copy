// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomeActivity_ViewBinding implements Unbinder {
  private HomeActivity target;

  @UiThread
  public HomeActivity_ViewBinding(HomeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HomeActivity_ViewBinding(HomeActivity target, View source) {
    this.target = target;

    target.viewPager = Utils.findRequiredViewAsType(source, R.id.viewPager, "field 'viewPager'", ViewPager.class);
    target.layoutBottomTabs = Utils.findRequiredViewAsType(source, R.id.layoutBottomTabs, "field 'layoutBottomTabs'", LinearLayout.class);
    target.buyClassesLayout = Utils.findRequiredViewAsType(source, R.id.buyClassesLayout, "field 'buyClassesLayout'", LinearLayout.class);
    target.buyClasses = Utils.findRequiredViewAsType(source, R.id.buyClasses, "field 'buyClasses'", TextView.class);
    target.availableCredit = Utils.findRequiredViewAsType(source, R.id.availableCredit, "field 'availableCredit'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HomeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewPager = null;
    target.layoutBottomTabs = null;
    target.buyClassesLayout = null;
    target.buyClasses = null;
    target.availableCredit = null;
  }
}

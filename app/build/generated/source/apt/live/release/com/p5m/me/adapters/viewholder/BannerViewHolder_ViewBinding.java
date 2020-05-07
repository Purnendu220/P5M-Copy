// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BannerViewHolder_ViewBinding implements Unbinder {
  private BannerViewHolder target;

  @UiThread
  public BannerViewHolder_ViewBinding(BannerViewHolder target, View source) {
    this.target = target;

    target.viewPager = Utils.findRequiredViewAsType(source, R.id.viewPager, "field 'viewPager'", ViewPager.class);
    target.layoutIndicator = Utils.findRequiredViewAsType(source, R.id.layoutIndicator, "field 'layoutIndicator'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    BannerViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewPager = null;
    target.layoutIndicator = null;
  }
}

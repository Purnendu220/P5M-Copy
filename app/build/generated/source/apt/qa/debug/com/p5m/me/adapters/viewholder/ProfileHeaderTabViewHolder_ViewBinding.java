// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProfileHeaderTabViewHolder_ViewBinding implements Unbinder {
  private ProfileHeaderTabViewHolder target;

  @UiThread
  public ProfileHeaderTabViewHolder_ViewBinding(ProfileHeaderTabViewHolder target, View source) {
    this.target = target;

    target.header1 = Utils.findRequiredViewAsType(source, R.id.header1, "field 'header1'", TextView.class);
    target.header2 = Utils.findRequiredViewAsType(source, R.id.header2, "field 'header2'", TextView.class);
    target.header1Indicator = Utils.findRequiredView(source, R.id.header1Indicator, "field 'header1Indicator'");
    target.header2Indicator = Utils.findRequiredView(source, R.id.header2Indicator, "field 'header2Indicator'");
  }

  @Override
  @CallSuper
  public void unbind() {
    ProfileHeaderTabViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.header1 = null;
    target.header2 = null;
    target.header1Indicator = null;
    target.header2Indicator = null;
  }
}

// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CustomerSpeakViewHolder_ViewBinding implements Unbinder {
  private CustomerSpeakViewHolder target;

  @UiThread
  public CustomerSpeakViewHolder_ViewBinding(CustomerSpeakViewHolder target, View source) {
    this.target = target;

    target.viewPager = Utils.findRequiredViewAsType(source, R.id.viewPager, "field 'viewPager'", ViewPager.class);
    target.textViewHeader = Utils.findRequiredViewAsType(source, R.id.textViewCustomerSpeaks, "field 'textViewHeader'", TextView.class);
    target.layoutIndicator = Utils.findRequiredViewAsType(source, R.id.layoutIndicator, "field 'layoutIndicator'", LinearLayout.class);
    target.layoutTestinomials = Utils.findRequiredViewAsType(source, R.id.layoutTestinomials, "field 'layoutTestinomials'", ConstraintLayout.class);
    target.view = Utils.findRequiredView(source, R.id.view, "field 'view'");
  }

  @Override
  @CallSuper
  public void unbind() {
    CustomerSpeakViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewPager = null;
    target.textViewHeader = null;
    target.layoutIndicator = null;
    target.layoutTestinomials = null;
    target.view = null;
  }
}

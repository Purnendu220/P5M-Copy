// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TryP5MViewHolder_ViewBinding implements Unbinder {
  private TryP5MViewHolder target;

  @UiThread
  public TryP5MViewHolder_ViewBinding(TryP5MViewHolder target, View source) {
    this.target = target;

    target.textViewHeader = Utils.findRequiredViewAsType(source, R.id.textViewTryP5M, "field 'textViewHeader'", TextView.class);
    target.textViewSubHeader = Utils.findRequiredViewAsType(source, R.id.textViewDescP5M, "field 'textViewSubHeader'", TextView.class);
    target.explorePlans = Utils.findRequiredViewAsType(source, R.id.explorePlans, "field 'explorePlans'", Button.class);
    target.layout = Utils.findRequiredViewAsType(source, R.id.layout, "field 'layout'", ConstraintLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TryP5MViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewHeader = null;
    target.textViewSubHeader = null;
    target.explorePlans = null;
    target.layout = null;
  }
}

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

public class StillConfusedViewHolder_ViewBinding implements Unbinder {
  private StillConfusedViewHolder target;

  @UiThread
  public StillConfusedViewHolder_ViewBinding(StillConfusedViewHolder target, View source) {
    this.target = target;

    target.buttonContactUs = Utils.findRequiredViewAsType(source, R.id.buttonContactUs, "field 'buttonContactUs'", Button.class);
    target.layout = Utils.findRequiredViewAsType(source, R.id.layout, "field 'layout'", ConstraintLayout.class);
    target.textViewHeader = Utils.findRequiredViewAsType(source, R.id.textViewHeader, "field 'textViewHeader'", TextView.class);
    target.textViewSubHeader = Utils.findRequiredViewAsType(source, R.id.textViewSubHeader, "field 'textViewSubHeader'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    StillConfusedViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.buttonContactUs = null;
    target.layout = null;
    target.textViewHeader = null;
    target.textViewSubHeader = null;
  }
}

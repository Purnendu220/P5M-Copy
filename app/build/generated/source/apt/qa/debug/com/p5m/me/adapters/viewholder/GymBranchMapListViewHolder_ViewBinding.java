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

public class GymBranchMapListViewHolder_ViewBinding implements Unbinder {
  private GymBranchMapListViewHolder target;

  @UiThread
  public GymBranchMapListViewHolder_ViewBinding(GymBranchMapListViewHolder target, View source) {
    this.target = target;

    target.textViewName = Utils.findRequiredViewAsType(source, R.id.textViewName, "field 'textViewName'", TextView.class);
    target.textViewAddress = Utils.findRequiredViewAsType(source, R.id.textViewAddress, "field 'textViewAddress'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GymBranchMapListViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewName = null;
    target.textViewAddress = null;
  }
}

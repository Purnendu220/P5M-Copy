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

public class UserPackageDetailViewHolder_ViewBinding implements Unbinder {
  private UserPackageDetailViewHolder target;

  @UiThread
  public UserPackageDetailViewHolder_ViewBinding(UserPackageDetailViewHolder target, View source) {
    this.target = target;

    target.textGymNames = Utils.findRequiredViewAsType(source, R.id.textGymNames, "field 'textGymNames'", TextView.class);
    target.textGymVisits = Utils.findRequiredViewAsType(source, R.id.textGymVisits, "field 'textGymVisits'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UserPackageDetailViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textGymNames = null;
    target.textGymVisits = null;
  }
}

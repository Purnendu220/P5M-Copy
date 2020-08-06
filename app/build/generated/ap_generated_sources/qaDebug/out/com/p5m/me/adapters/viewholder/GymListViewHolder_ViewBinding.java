// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GymListViewHolder_ViewBinding implements Unbinder {
  private GymListViewHolder target;

  @UiThread
  public GymListViewHolder_ViewBinding(GymListViewHolder target, View source) {
    this.target = target;

    target.textViewName = Utils.findRequiredViewAsType(source, R.id.textViewName, "field 'textViewName'", TextView.class);
    target.textViewSubtitle = Utils.findRequiredViewAsType(source, R.id.textViewSubtitle, "field 'textViewSubtitle'", TextView.class);
    target.layoutLocation = Utils.findRequiredView(source, R.id.layoutLocation, "field 'layoutLocation'");
    target.imageViewProfile = Utils.findRequiredViewAsType(source, R.id.imageViewProfile, "field 'imageViewProfile'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GymListViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewName = null;
    target.textViewSubtitle = null;
    target.layoutLocation = null;
    target.imageViewProfile = null;
  }
}

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

public class NotificationsViewHolder_ViewBinding implements Unbinder {
  private NotificationsViewHolder target;

  @UiThread
  public NotificationsViewHolder_ViewBinding(NotificationsViewHolder target, View source) {
    this.target = target;

    target.textViewTime = Utils.findRequiredViewAsType(source, R.id.textViewTime, "field 'textViewTime'", TextView.class);
    target.textViewDetails = Utils.findRequiredViewAsType(source, R.id.textViewDetails, "field 'textViewDetails'", TextView.class);
    target.imageViewProfile = Utils.findRequiredViewAsType(source, R.id.imageViewProfile, "field 'imageViewProfile'", ImageView.class);
    target.imageViewProfileStatus = Utils.findRequiredViewAsType(source, R.id.imageViewProfileStatus, "field 'imageViewProfileStatus'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NotificationsViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewTime = null;
    target.textViewDetails = null;
    target.imageViewProfile = null;
    target.imageViewProfileStatus = null;
  }
}

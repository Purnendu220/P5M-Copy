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

public class GymListByCategoryViewHolder_ViewBinding implements Unbinder {
  private GymListByCategoryViewHolder target;

  @UiThread
  public GymListByCategoryViewHolder_ViewBinding(GymListByCategoryViewHolder target, View source) {
    this.target = target;

    target.textViewTrainerName = Utils.findRequiredViewAsType(source, R.id.textViewName, "field 'textViewTrainerName'", TextView.class);
    target.textViewSubtitle = Utils.findRequiredViewAsType(source, R.id.textViewSubtitle, "field 'textViewSubtitle'", TextView.class);
    target.imageViewProfile = Utils.findRequiredViewAsType(source, R.id.imageViewProfile, "field 'imageViewProfile'", ImageView.class);
    target.imageViewSubtitle = Utils.findRequiredViewAsType(source, R.id.imageViewSubtitle, "field 'imageViewSubtitle'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    GymListByCategoryViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewTrainerName = null;
    target.textViewSubtitle = null;
    target.imageViewProfile = null;
    target.imageViewSubtitle = null;
  }
}

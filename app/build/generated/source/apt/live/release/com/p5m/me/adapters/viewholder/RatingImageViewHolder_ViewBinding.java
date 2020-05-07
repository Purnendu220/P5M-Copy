// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RatingImageViewHolder_ViewBinding implements Unbinder {
  private RatingImageViewHolder target;

  @UiThread
  public RatingImageViewHolder_ViewBinding(RatingImageViewHolder target, View source) {
    this.target = target;

    target.imageViewRating = Utils.findRequiredViewAsType(source, R.id.imageViewRating, "field 'imageViewRating'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RatingImageViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewRating = null;
  }
}

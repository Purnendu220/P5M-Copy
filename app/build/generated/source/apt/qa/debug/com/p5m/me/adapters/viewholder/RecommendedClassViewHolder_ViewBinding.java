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

public class RecommendedClassViewHolder_ViewBinding implements Unbinder {
  private RecommendedClassViewHolder target;

  @UiThread
  public RecommendedClassViewHolder_ViewBinding(RecommendedClassViewHolder target, View source) {
    this.target = target;

    target.textViewRatingCount = Utils.findRequiredViewAsType(source, R.id.textViewRatingCount, "field 'textViewRatingCount'", TextView.class);
    target.textViewClassName = Utils.findRequiredViewAsType(source, R.id.textViewClassName, "field 'textViewClassName'", TextView.class);
    target.textViewTime = Utils.findRequiredViewAsType(source, R.id.textViewTime, "field 'textViewTime'", TextView.class);
    target.textViewLocation = Utils.findRequiredViewAsType(source, R.id.textViewLocation, "field 'textViewLocation'", TextView.class);
    target.imgClass = Utils.findRequiredViewAsType(source, R.id.imgClass, "field 'imgClass'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RecommendedClassViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewRatingCount = null;
    target.textViewClassName = null;
    target.textViewTime = null;
    target.textViewLocation = null;
    target.imgClass = null;
  }
}

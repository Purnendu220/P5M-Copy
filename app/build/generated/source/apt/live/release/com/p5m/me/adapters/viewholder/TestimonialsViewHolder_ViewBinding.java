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

public class TestimonialsViewHolder_ViewBinding implements Unbinder {
  private TestimonialsViewHolder target;

  @UiThread
  public TestimonialsViewHolder_ViewBinding(TestimonialsViewHolder target, View source) {
    this.target = target;

    target.textViewName = Utils.findRequiredViewAsType(source, R.id.textViewName, "field 'textViewName'", TextView.class);
    target.textViewComment = Utils.findRequiredViewAsType(source, R.id.textViewComment, "field 'textViewComment'", TextView.class);
    target.imageViewProfile = Utils.findRequiredViewAsType(source, R.id.imageViewProfile, "field 'imageViewProfile'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TestimonialsViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewName = null;
    target.textViewComment = null;
    target.imageViewProfile = null;
  }
}

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

public class SelectedImageViewHolder_ViewBinding implements Unbinder {
  private SelectedImageViewHolder target;

  @UiThread
  public SelectedImageViewHolder_ViewBinding(SelectedImageViewHolder target, View source) {
    this.target = target;

    target.imageViewSelectedImage = Utils.findRequiredViewAsType(source, R.id.imageViewSelectedImage, "field 'imageViewSelectedImage'", ImageView.class);
    target.imageViewCross = Utils.findRequiredViewAsType(source, R.id.imageViewCross, "field 'imageViewCross'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SelectedImageViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageViewSelectedImage = null;
    target.imageViewCross = null;
  }
}

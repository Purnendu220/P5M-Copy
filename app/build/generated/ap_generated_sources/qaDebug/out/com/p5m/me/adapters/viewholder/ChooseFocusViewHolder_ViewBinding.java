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

public class ChooseFocusViewHolder_ViewBinding implements Unbinder {
  private ChooseFocusViewHolder target;

  @UiThread
  public ChooseFocusViewHolder_ViewBinding(ChooseFocusViewHolder target, View source) {
    this.target = target;

    target.imageView = Utils.findRequiredViewAsType(source, R.id.imageView, "field 'imageView'", ImageView.class);
    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
    target.layoutItem = Utils.findRequiredView(source, R.id.layoutItem, "field 'layoutItem'");
  }

  @Override
  @CallSuper
  public void unbind() {
    ChooseFocusViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
    target.textView = null;
    target.layoutItem = null;
  }
}

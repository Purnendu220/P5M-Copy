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

public class FilterListItemViewHolder_ViewBinding implements Unbinder {
  private FilterListItemViewHolder target;

  @UiThread
  public FilterListItemViewHolder_ViewBinding(FilterListItemViewHolder target, View source) {
    this.target = target;

    target.imageLeft = Utils.findRequiredViewAsType(source, R.id.imageLeft, "field 'imageLeft'", ImageView.class);
    target.imageRight = Utils.findRequiredViewAsType(source, R.id.imageRight, "field 'imageRight'", ImageView.class);
    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
    target.layoutAlignment = Utils.findRequiredView(source, R.id.layoutAlignment, "field 'layoutAlignment'");
  }

  @Override
  @CallSuper
  public void unbind() {
    FilterListItemViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageLeft = null;
    target.imageRight = null;
    target.textView = null;
    target.layoutAlignment = null;
  }
}

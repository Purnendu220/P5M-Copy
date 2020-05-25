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

public class HeaderViewHolder_ViewBinding implements Unbinder {
  private HeaderViewHolder target;

  @UiThread
  public HeaderViewHolder_ViewBinding(HeaderViewHolder target, View source) {
    this.target = target;

    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HeaderViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;
  }
}

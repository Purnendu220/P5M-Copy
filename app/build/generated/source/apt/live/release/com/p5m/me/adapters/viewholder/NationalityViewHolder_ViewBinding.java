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

public class NationalityViewHolder_ViewBinding implements Unbinder {
  private NationalityViewHolder target;

  @UiThread
  public NationalityViewHolder_ViewBinding(NationalityViewHolder target, View source) {
    this.target = target;

    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
    target.layoutItem = Utils.findRequiredView(source, R.id.layoutItem, "field 'layoutItem'");
  }

  @Override
  @CallSuper
  public void unbind() {
    NationalityViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;
    target.layoutItem = null;
  }
}

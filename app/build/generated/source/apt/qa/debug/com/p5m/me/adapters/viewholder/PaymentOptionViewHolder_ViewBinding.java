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

public class PaymentOptionViewHolder_ViewBinding implements Unbinder {
  private PaymentOptionViewHolder target;

  @UiThread
  public PaymentOptionViewHolder_ViewBinding(PaymentOptionViewHolder target, View source) {
    this.target = target;

    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
    target.layoutItem = Utils.findRequiredView(source, R.id.layoutItem, "field 'layoutItem'");
    target.imageView = Utils.findRequiredViewAsType(source, R.id.imageView, "field 'imageView'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PaymentOptionViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;
    target.layoutItem = null;
    target.imageView = null;
  }
}

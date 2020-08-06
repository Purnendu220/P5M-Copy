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

public class TransactionViewHolder_ViewBinding implements Unbinder {
  private TransactionViewHolder target;

  @UiThread
  public TransactionViewHolder_ViewBinding(TransactionViewHolder target, View source) {
    this.target = target;

    target.textViewRefId = Utils.findRequiredViewAsType(source, R.id.textViewRefId, "field 'textViewRefId'", TextView.class);
    target.textViewPackageName = Utils.findRequiredViewAsType(source, R.id.textViewPackageName, "field 'textViewPackageName'", TextView.class);
    target.textViewPackagePrice = Utils.findRequiredViewAsType(source, R.id.textViewPackagePrice, "field 'textViewPackagePrice'", TextView.class);
    target.textViewPackageDate = Utils.findRequiredViewAsType(source, R.id.textViewPackageDate, "field 'textViewPackageDate'", TextView.class);
    target.textViewPackageStatus = Utils.findRequiredViewAsType(source, R.id.textViewPackageStatus, "field 'textViewPackageStatus'", TextView.class);
    target.textViewAmount = Utils.findRequiredViewAsType(source, R.id.textViewAmount, "field 'textViewAmount'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TransactionViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewRefId = null;
    target.textViewPackageName = null;
    target.textViewPackagePrice = null;
    target.textViewPackageDate = null;
    target.textViewPackageStatus = null;
    target.textViewAmount = null;
  }
}

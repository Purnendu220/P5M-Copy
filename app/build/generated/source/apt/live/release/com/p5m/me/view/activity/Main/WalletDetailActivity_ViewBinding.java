// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WalletDetailActivity_ViewBinding implements Unbinder {
  private WalletDetailActivity target;

  @UiThread
  public WalletDetailActivity_ViewBinding(WalletDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WalletDetailActivity_ViewBinding(WalletDetailActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.walletHistory = Utils.findRequiredViewAsType(source, R.id.walletHistory, "field 'walletHistory'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WalletDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.walletHistory = null;
  }
}

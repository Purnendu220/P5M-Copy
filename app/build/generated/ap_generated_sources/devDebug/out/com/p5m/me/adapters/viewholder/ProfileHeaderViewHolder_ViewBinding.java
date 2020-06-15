// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProfileHeaderViewHolder_ViewBinding implements Unbinder {
  private ProfileHeaderViewHolder target;

  @UiThread
  public ProfileHeaderViewHolder_ViewBinding(ProfileHeaderViewHolder target, View source) {
    this.target = target;

    target.imageView = Utils.findRequiredViewAsType(source, R.id.imageView, "field 'imageView'", ImageView.class);
    target.textViewName = Utils.findRequiredViewAsType(source, R.id.textViewName, "field 'textViewName'", TextView.class);
    target.textViewPackage = Utils.findRequiredViewAsType(source, R.id.textViewPackage, "field 'textViewPackage'", TextView.class);
    target.textViewValidity = Utils.findRequiredViewAsType(source, R.id.textViewValidity, "field 'textViewValidity'", TextView.class);
    target.textViewRecharge = Utils.findRequiredViewAsType(source, R.id.textViewRecharge, "field 'textViewRecharge'", TextView.class);
    target.textViewMore = Utils.findRequiredViewAsType(source, R.id.textViewMore, "field 'textViewMore'", TextView.class);
    target.textViewExtendPackage = Utils.findRequiredViewAsType(source, R.id.textViewExtendPackage, "field 'textViewExtendPackage'", TextView.class);
    target.linearLayoutUserWallet = Utils.findRequiredViewAsType(source, R.id.linearLayoutUserWallet, "field 'linearLayoutUserWallet'", LinearLayout.class);
    target.textViewWalletBalance = Utils.findRequiredViewAsType(source, R.id.textViewWalletBalance, "field 'textViewWalletBalance'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProfileHeaderViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
    target.textViewName = null;
    target.textViewPackage = null;
    target.textViewValidity = null;
    target.textViewRecharge = null;
    target.textViewMore = null;
    target.textViewExtendPackage = null;
    target.linearLayoutUserWallet = null;
    target.textViewWalletBalance = null;
  }
}

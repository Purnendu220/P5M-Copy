// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MembershipFragment_ViewBinding implements Unbinder {
  private MembershipFragment target;

  @UiThread
  public MembershipFragment_ViewBinding(MembershipFragment target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipeRefreshLayout, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.textGymVisitLimits = Utils.findRequiredViewAsType(source, R.id.textGymVisitLimits, "field 'textGymVisitLimits'", TextView.class);
    target.imageViewInfo = Utils.findRequiredViewAsType(source, R.id.imageViewInfo, "field 'imageViewInfo'", ImageView.class);
    target.constraintLayout = Utils.findRequiredViewAsType(source, R.id.constraintLayout, "field 'constraintLayout'", ConstraintLayout.class);
    target.mTextViewWalletAmount = Utils.findRequiredViewAsType(source, R.id.textViewWalletAmount, "field 'mTextViewWalletAmount'", TextView.class);
    target.mLayoutUserWallet = Utils.findRequiredViewAsType(source, R.id.layoutUserWallet, "field 'mLayoutUserWallet'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MembershipFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.swipeRefreshLayout = null;
    target.textGymVisitLimits = null;
    target.imageViewInfo = null;
    target.constraintLayout = null;
    target.mTextViewWalletAmount = null;
    target.mLayoutUserWallet = null;
  }
}

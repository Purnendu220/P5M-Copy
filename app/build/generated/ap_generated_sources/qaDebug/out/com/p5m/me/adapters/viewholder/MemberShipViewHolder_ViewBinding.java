// Generated code from Butter Knife. Do not modify!
package com.p5m.me.adapters.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MemberShipViewHolder_ViewBinding implements Unbinder {
  private MemberShipViewHolder target;

  @UiThread
  public MemberShipViewHolder_ViewBinding(MemberShipViewHolder target, View source) {
    this.target = target;

    target.mainLayoutUserPakages = Utils.findRequiredViewAsType(source, R.id.mainLayoutUserPakages, "field 'mainLayoutUserPakages'", RelativeLayout.class);
    target.textViewBuyMoreCredits = Utils.findRequiredViewAsType(source, R.id.textViewBuyMoreCredits, "field 'textViewBuyMoreCredits'", TextView.class);
    target.packageTitle = Utils.findRequiredViewAsType(source, R.id.packageTitle, "field 'packageTitle'", TextView.class);
    target.packageUsage = Utils.findRequiredViewAsType(source, R.id.packageUsage, "field 'packageUsage'", TextView.class);
    target.textViewSoFarYouVisited = Utils.findRequiredViewAsType(source, R.id.textViewSoFarYouVisited, "field 'textViewSoFarYouVisited'", TextView.class);
    target.layoutSoFarYouVisited = Utils.findRequiredViewAsType(source, R.id.layoutSoFarYouVisited, "field 'layoutSoFarYouVisited'", LinearLayout.class);
    target.recyclerSoFarYouVisited = Utils.findRequiredViewAsType(source, R.id.recyclerSoFarYouVisited, "field 'recyclerSoFarYouVisited'", RecyclerView.class);
    target.textViewActiveDropIn = Utils.findRequiredViewAsType(source, R.id.textViewActiveDropIn, "field 'textViewActiveDropIn'", TextView.class);
    target.button = Utils.findRequiredViewAsType(source, R.id.button, "field 'button'", Button.class);
    target.mainLayoutActivePackageDropin = Utils.findRequiredViewAsType(source, R.id.mainLayoutActivePackageDropin, "field 'mainLayoutActivePackageDropin'", LinearLayout.class);
    target.packageValidForOwn = Utils.findRequiredViewAsType(source, R.id.packageValidForOwn, "field 'packageValidForOwn'", TextView.class);
    target.textViewExtendPackage = Utils.findRequiredViewAsType(source, R.id.textViewExtendPackage, "field 'textViewExtendPackage'", TextView.class);
    target.txtPackageOffer = Utils.findRequiredViewAsType(source, R.id.txtPackageOffer, "field 'txtPackageOffer'", TextView.class);
    target.txtPackageOffredCredits = Utils.findRequiredViewAsType(source, R.id.txtPackageOffredCredits, "field 'txtPackageOffredCredits'", TextView.class);
    target.txtPackageOffredClassesLimits = Utils.findRequiredViewAsType(source, R.id.txtPackageOffredClassesLimits, "field 'txtPackageOffredClassesLimits'", TextView.class);
    target.txtPriceBeforeOffer = Utils.findRequiredViewAsType(source, R.id.txtPriceBeforeOffer, "field 'txtPriceBeforeOffer'", TextView.class);
    target.txtPriceAfterOffer = Utils.findRequiredViewAsType(source, R.id.txtPriceAfterOffer, "field 'txtPriceAfterOffer'", TextView.class);
    target.packageValidFor = Utils.findRequiredViewAsType(source, R.id.packageValidFor, "field 'packageValidFor'", TextView.class);
    target.layoutMainOfferedPackage = Utils.findRequiredViewAsType(source, R.id.layoutMainOfferedPackage, "field 'layoutMainOfferedPackage'", LinearLayout.class);
    target.packageLayout = Utils.findRequiredViewAsType(source, R.id.packageLayout, "field 'packageLayout'", LinearLayout.class);
    target.layoutNotApplicable = Utils.findRequiredViewAsType(source, R.id.layoutNotApplicable, "field 'layoutNotApplicable'", RelativeLayout.class);
    target.imageViewNotApplicableInfo = Utils.findRequiredViewAsType(source, R.id.imageViewNotApplicableInfo, "field 'imageViewNotApplicableInfo'", ImageView.class);
    target.layoutTimeClassPromo = Utils.findRequiredViewAsType(source, R.id.layoutTimeClassPromo, "field 'layoutTimeClassPromo'", LinearLayout.class);
    target.textViewSpecialOffer = Utils.findRequiredViewAsType(source, R.id.textViewSpecialOffer, "field 'textViewSpecialOffer'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MemberShipViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mainLayoutUserPakages = null;
    target.textViewBuyMoreCredits = null;
    target.packageTitle = null;
    target.packageUsage = null;
    target.textViewSoFarYouVisited = null;
    target.layoutSoFarYouVisited = null;
    target.recyclerSoFarYouVisited = null;
    target.textViewActiveDropIn = null;
    target.button = null;
    target.mainLayoutActivePackageDropin = null;
    target.packageValidForOwn = null;
    target.textViewExtendPackage = null;
    target.txtPackageOffer = null;
    target.txtPackageOffredCredits = null;
    target.txtPackageOffredClassesLimits = null;
    target.txtPriceBeforeOffer = null;
    target.txtPriceAfterOffer = null;
    target.packageValidFor = null;
    target.layoutMainOfferedPackage = null;
    target.packageLayout = null;
    target.layoutNotApplicable = null;
    target.imageViewNotApplicableInfo = null;
    target.layoutTimeClassPromo = null;
    target.textViewSpecialOffer = null;
  }
}

// Generated code from Butter Knife. Do not modify!
package com.p5m.me.view.activity.Main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.p5m.me.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CheckoutActivity_ViewBinding implements Unbinder {
  private CheckoutActivity target;

  private View view7f0a017f;

  @UiThread
  public CheckoutActivity_ViewBinding(CheckoutActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CheckoutActivity_ViewBinding(final CheckoutActivity target, View source) {
    this.target = target;

    View view;
    target.textViewPackageName = Utils.findRequiredViewAsType(source, R.id.textViewPackageName, "field 'textViewPackageName'", TextView.class);
    target.textViewTopTitle = Utils.findRequiredViewAsType(source, R.id.textViewTopTitle, "field 'textViewTopTitle'", TextView.class);
    target.textViewPrice = Utils.findRequiredViewAsType(source, R.id.textViewPrice, "field 'textViewPrice'", TextView.class);
    target.textViewPackageClasses = Utils.findRequiredViewAsType(source, R.id.textViewPackageClasses, "field 'textViewPackageClasses'", TextView.class);
    target.textViewPackageValidity = Utils.findRequiredViewAsType(source, R.id.textViewPackageValidity, "field 'textViewPackageValidity'", TextView.class);
    target.textViewPackageInfo = Utils.findRequiredViewAsType(source, R.id.textViewPackageInfo, "field 'textViewPackageInfo'", TextView.class);
    target.textViewPromoCodePrice = Utils.findRequiredViewAsType(source, R.id.textViewPromoCodePrice, "field 'textViewPromoCodePrice'", TextView.class);
    target.textViewLimit = Utils.findRequiredViewAsType(source, R.id.textViewLimit, "field 'textViewLimit'", TextView.class);
    target.textViewCancellationPolicyToggle = Utils.findRequiredViewAsType(source, R.id.textViewCancellationPolicyToggle, "field 'textViewCancellationPolicyToggle'", TextView.class);
    target.textViewCancellationPolicy = Utils.findRequiredViewAsType(source, R.id.textViewCancellationPolicy, "field 'textViewCancellationPolicy'", TextView.class);
    target.textViewCancellationPolicyGeneralToggle = Utils.findRequiredViewAsType(source, R.id.textViewCancellationPolicyGeneralToggle, "field 'textViewCancellationPolicyGeneralToggle'", TextView.class);
    target.textViewCancellationPolicyGenral = Utils.findRequiredViewAsType(source, R.id.textViewCancellationPolicyGenral, "field 'textViewCancellationPolicyGenral'", TextView.class);
    target.textViewTotal = Utils.findRequiredViewAsType(source, R.id.textViewTotal, "field 'textViewTotal'", TextView.class);
    target.textViewPay = Utils.findRequiredViewAsType(source, R.id.textViewPay, "field 'textViewPay'", TextView.class);
    target.imageViewPackageImage = Utils.findRequiredViewAsType(source, R.id.imageViewPackageImage, "field 'imageViewPackageImage'", ImageView.class);
    target.buttonPromoCode = Utils.findRequiredViewAsType(source, R.id.buttonPromoCode, "field 'buttonPromoCode'", Button.class);
    target.layoutPromoCode = Utils.findRequiredView(source, R.id.layoutPromoCode, "field 'layoutPromoCode'");
    target.layoutSpecialClassDetails = Utils.findRequiredViewAsType(source, R.id.layoutSpecialClassDetails, "field 'layoutSpecialClassDetails'", ViewGroup.class);
    target.layoutNormalClassDetails = Utils.findRequiredView(source, R.id.layoutNormalClassDetails, "field 'layoutNormalClassDetails'");
    target.validityUnit = Utils.findRequiredViewAsType(source, R.id.validityUnit, "field 'validityUnit'", TextView.class);
    target.textViewPackageExtendNoOfDays = Utils.findRequiredViewAsType(source, R.id.textViewPackageExtendNoOfDays, "field 'textViewPackageExtendNoOfDays'", TextView.class);
    target.textViewPlus = Utils.findRequiredViewAsType(source, R.id.textViewPlus, "field 'textViewPlus'", TextView.class);
    target.textViewPackageValidityExtend = Utils.findRequiredViewAsType(source, R.id.textViewPackageValidityExtend, "field 'textViewPackageValidityExtend'", TextView.class);
    target.linearLayoutBookForFriend = Utils.findRequiredViewAsType(source, R.id.linearLayoutBookForFriend, "field 'linearLayoutBookForFriend'", LinearLayout.class);
    target.imageViewPackageImageBWF = Utils.findRequiredViewAsType(source, R.id.imageViewPackageImageBWF, "field 'imageViewPackageImageBWF'", ImageView.class);
    target.textViewPackageNameBWF = Utils.findRequiredViewAsType(source, R.id.textViewPackageNameBWF, "field 'textViewPackageNameBWF'", TextView.class);
    target.textViewPriceBWF = Utils.findRequiredViewAsType(source, R.id.textViewPriceBWF, "field 'textViewPriceBWF'", TextView.class);
    target.validityUnitBWF = Utils.findRequiredViewAsType(source, R.id.validityUnitBWF, "field 'validityUnitBWF'", TextView.class);
    target.textViewPackageValidityExtendBWF = Utils.findRequiredViewAsType(source, R.id.textViewPackageValidityExtendBWF, "field 'textViewPackageValidityExtendBWF'", TextView.class);
    target.textViewCancellationPolicyToggleBWF = Utils.findRequiredViewAsType(source, R.id.textViewCancellationPolicyToggleBWF, "field 'textViewCancellationPolicyToggleBWF'", TextView.class);
    target.textViewPromoCodeText = Utils.findRequiredViewAsType(source, R.id.textViewPromoCodeText, "field 'textViewPromoCodeText'", TextView.class);
    target.textViewCancellationPolicyBWF = Utils.findRequiredViewAsType(source, R.id.textViewCancellationPolicyBWF, "field 'textViewCancellationPolicyBWF'", TextView.class);
    target.layoutWalletCredit = Utils.findRequiredViewAsType(source, R.id.layoutWalletCredit, "field 'layoutWalletCredit'", LinearLayout.class);
    target.textViewWalletCreditPrice = Utils.findRequiredViewAsType(source, R.id.textViewWalletCreditPrice, "field 'textViewWalletCreditPrice'", TextView.class);
    target.mLayoutUserWallet = Utils.findRequiredViewAsType(source, R.id.layoutUserWallet, "field 'mLayoutUserWallet'", LinearLayout.class);
    target.mTextViewWalletAmount = Utils.findRequiredViewAsType(source, R.id.textViewWalletAmount, "field 'mTextViewWalletAmount'", TextView.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.scrollView = Utils.findRequiredViewAsType(source, R.id.scrollview, "field 'scrollView'", ScrollView.class);
    target.textViewOtherCountryInfo = Utils.findRequiredViewAsType(source, R.id.textViewOtherCountryInfo, "field 'textViewOtherCountryInfo'", TextView.class);
    view = Utils.findRequiredView(source, R.id.imageViewBack, "method 'imageViewBack'");
    view7f0a017f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.imageViewBack(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    CheckoutActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textViewPackageName = null;
    target.textViewTopTitle = null;
    target.textViewPrice = null;
    target.textViewPackageClasses = null;
    target.textViewPackageValidity = null;
    target.textViewPackageInfo = null;
    target.textViewPromoCodePrice = null;
    target.textViewLimit = null;
    target.textViewCancellationPolicyToggle = null;
    target.textViewCancellationPolicy = null;
    target.textViewCancellationPolicyGeneralToggle = null;
    target.textViewCancellationPolicyGenral = null;
    target.textViewTotal = null;
    target.textViewPay = null;
    target.imageViewPackageImage = null;
    target.buttonPromoCode = null;
    target.layoutPromoCode = null;
    target.layoutSpecialClassDetails = null;
    target.layoutNormalClassDetails = null;
    target.validityUnit = null;
    target.textViewPackageExtendNoOfDays = null;
    target.textViewPlus = null;
    target.textViewPackageValidityExtend = null;
    target.linearLayoutBookForFriend = null;
    target.imageViewPackageImageBWF = null;
    target.textViewPackageNameBWF = null;
    target.textViewPriceBWF = null;
    target.validityUnitBWF = null;
    target.textViewPackageValidityExtendBWF = null;
    target.textViewCancellationPolicyToggleBWF = null;
    target.textViewPromoCodeText = null;
    target.textViewCancellationPolicyBWF = null;
    target.layoutWalletCredit = null;
    target.textViewWalletCreditPrice = null;
    target.mLayoutUserWallet = null;
    target.mTextViewWalletAmount = null;
    target.recyclerView = null;
    target.scrollView = null;
    target.textViewOtherCountryInfo = null;

    view7f0a017f.setOnClickListener(null);
    view7f0a017f = null;
  }
}

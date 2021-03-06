package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.UserPackageInfo;
import com.p5m.me.data.main.User;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.helper.Helper;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.LanguageUtils.numberConverter;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ProfileHeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageView)
    public ImageView imageView;

    @BindView(R.id.textViewName)
    public TextView textViewName;
    @BindView(R.id.textViewPackage)
    public TextView textViewPackage;
    @BindView(R.id.textViewValidity)
    public TextView textViewValidity;
    @BindView(R.id.textViewRecharge)
    public TextView textViewRecharge;
    @BindView(R.id.textViewMore)
    public TextView textViewMore;

    @BindView(R.id.textViewExtendPackage)
    public TextView textViewExtendPackage;
    @BindView(R.id.linearLayoutUserWallet)
    public LinearLayout linearLayoutUserWallet;

    @BindView(R.id.textViewWalletBalance)
    public TextView textViewWalletBalance;

    private final Context context;
    public UserPackage userpackageToExtend;
    private User.WalletDto mWalletCredit;

    public ProfileHeaderViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks<Object> adapterCallbacks, final int position) {

        if (data != null && data instanceof User) {
            final User user = (User) data;
            mWalletCredit=user.getWalletDto();
            itemView.setVisibility(View.VISIBLE);

            textViewName.setText(user.getFirstName() + " " + user.getLastName());
            UserPackageInfo userPackageInfo = new UserPackageInfo(user);
            textViewRecharge.setText(Html.fromHtml("<u>"+context.getString(R.string.add_credits)+"</u>"));
            ImageUtils.setImage(context,
                    user.getProfileImage(),
                    R.drawable.profile_holder_big, imageView);
            if(mWalletCredit!=null&&mWalletCredit.getBalance()>0){
                linearLayoutUserWallet.setVisibility(View.VISIBLE);
                textViewWalletBalance.setText(LanguageUtils.numberConverter(mWalletCredit.getBalance(),2)+" "+ TempStorage.getUser().getCurrencyCode());

            }
            else{
                linearLayoutUserWallet.setVisibility(View.GONE);

            }

            if (userPackageInfo.havePackages) {
                if(userPackageInfo.haveGeneralPackage&&userPackageInfo.userPackageGeneral!=null){
                    if(userPackageInfo.userPackageGeneral.getBalance()<= Helper.getBaseCreditValue()){
                        textViewRecharge.setVisibility(View.VISIBLE);
                    }else{
                        textViewRecharge.setVisibility(View.GONE);

                    }
                }


               if (user.isBuyMembership()) {
                    if (userPackageInfo.haveDropInPackage) {
                        textViewExtendPackage.setVisibility(View.VISIBLE);

                    } else {
                        textViewExtendPackage.setVisibility(View.GONE);

                    }

                } else {
                    textViewExtendPackage.setVisibility(View.VISIBLE);

                }

                /*************************3 CONDITION ON PACKAGE**************************/
                textViewMore.setVisibility(View.GONE);

                if (userPackageInfo.haveGeneralPackage && !userPackageInfo.haveDropInPackage) {
                    textViewValidity.setVisibility(View.VISIBLE);

                  //  String message = userPackageInfo.userPackageGeneral.getBalance() != 1 ? context.getString(R.string.classes) : context.getString(R.string.one_class);

                    LanguageUtils.setText(textViewPackage, userPackageInfo.userPackageGeneral.getBalance(), context.getString(R.string.p5m_credits) + " " + context.getString(R.string.remaining));

                    int daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageInfo.userPackageGeneral.getExpiryDate());
                    if (daysLeftFromPackageExpiryDate >= 0) {
                        textViewValidity.setText(Html.fromHtml("<b>" + numberConverter(daysLeftFromPackageExpiryDate) + " " +
                                AppConstants.plural(context.getString(R.string.day), daysLeftFromPackageExpiryDate) + "</b> " +
                                context.getString(R.string.profile_package_expiry)));
                    } else {
                        textViewValidity.setVisibility(View.GONE);
                        textViewExtendPackage.setVisibility(View.GONE);

                    }

                    userpackageToExtend = userPackageInfo.userPackageGeneral;

                } else if (userPackageInfo.haveGeneralPackage && userPackageInfo.haveDropInPackage) {
                    textViewValidity.setVisibility(View.VISIBLE);
                    int daysLeftFromPackageExpiryDate;
                    if (user.isBuyMembership()) {

                        //////////////////
                        String one = numberConverter(userPackageInfo.userPackageReady.get(0).getBalance());


                        textViewPackage.setText(Html.fromHtml("<b>" + one + "</b>" + " " + context.getString(R.string.p5m_credits)+" "+context.getString(R.string.class_for) + " "
                                + userPackageInfo.userPackageReady.get(0).getGymName()));



                        daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageInfo.userPackageReady.get(0).getExpiryDate());

                        textViewValidity.setText(Html.fromHtml("<b>" + LanguageUtils.numberConverter(daysLeftFromPackageExpiryDate) + "</b> " +
                                " " + AppConstants.plural(context.getString(R.string.day), daysLeftFromPackageExpiryDate) + context.getString(R.string.profile_package_expiry)));
                        userpackageToExtend = userPackageInfo.userPackageReady.get(0);

                        if (userPackageInfo.dropInPackageCount + userPackageInfo.generalPackageCount > 1) {
                            textViewMore.setVisibility(View.VISIBLE);
                            String more = "+" + numberConverter(userPackageInfo.dropInPackageCount + userPackageInfo.generalPackageCount - 1) + " " + context.getString(R.string.more) + " " +
                                    AppConstants.plural(context.getString(R.string.package_name), (userPackageInfo.dropInPackageCount - 1));
                            textViewMore.setText(Html.fromHtml("<b><u>" + more + "</u></b> "));
                        }
                    } else {


                        textViewPackage.setText(Html.fromHtml("<b>" + numberConverter(userPackageInfo.userPackageGeneral.getBalance()) +
                                "</b> " + context.getString(R.string.p5m_credits) + " " + context.getString(R.string.remaining)));

                        daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageInfo.userPackageGeneral.getExpiryDate());
                        if (daysLeftFromPackageExpiryDate >= 0) {
                            textViewValidity.setText(Html.fromHtml("<b>" + numberConverter(daysLeftFromPackageExpiryDate) + " " +
                                    AppConstants.plural(context.getString(R.string.day), daysLeftFromPackageExpiryDate) + "</b> " +
                                    context.getString(R.string.profile_package_expiry)));
                        } else {
                            textViewValidity.setVisibility(View.GONE);
                            textViewExtendPackage.setVisibility(View.GONE);

                        }
                        textViewValidity.setText(Html.fromHtml("<b>" + numberConverter(daysLeftFromPackageExpiryDate) + " " +
                                AppConstants.plural(context.getString(R.string.day), daysLeftFromPackageExpiryDate) + "</b> " +
                                context.getString(R.string.profile_package_expiry)));
                        userpackageToExtend = userPackageInfo.userPackageGeneral;


                        textViewMore.setVisibility(View.VISIBLE);
                        String more = "+" + numberConverter(userPackageInfo.dropInPackageCount) + " " + context.getString(R.string.more) + " " +
                                AppConstants.plural(context.getString(R.string.package_name), userPackageInfo.dropInPackageCount);
                        textViewMore.setText(Html.fromHtml("<b><u>" + more + "</u></b> "));
                    }


                } else if (!userPackageInfo.haveGeneralPackage && userPackageInfo.haveDropInPackage) {
                    textViewValidity.setVisibility(View.VISIBLE);
                    int balanceClasses = userPackageInfo.userPackageReady.get(0).getBalance() != 0 ? userPackageInfo.userPackageReady.get(0).getBalance() : 1;
//                    String message=userPackageInfo.userPackageReady.get(0).getBalanceClass()>1?"classes ":"class";
                    String message = AppConstants.pluralES(context.getString(R.string.one_class), userPackageInfo.userPackageReady.get(0).getBalance());


                    textViewPackage.setText(Html.fromHtml("<b>" + numberConverter(balanceClasses) + "</b> " + message + " " + context.getString(R.string.for_key) + " " + userPackageInfo.userPackageReady.get(0).getGymName()));


                    int daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageInfo.userPackageReady.get(0).getExpiryDate());
                    if (daysLeftFromPackageExpiryDate >= 0) {
                        textViewValidity.setText(Html.fromHtml("<b>" + numberConverter(daysLeftFromPackageExpiryDate) + "</b> " +
                                " " + AppConstants.plural(context.getString(R.string.day), daysLeftFromPackageExpiryDate) + context.getString(R.string.profile_package_expiry)));

                    } else {
                        textViewValidity.setVisibility(View.GONE);
                        textViewExtendPackage.setVisibility(View.GONE);

                    }
                    userpackageToExtend = userPackageInfo.userPackageReady.get(0);

                    if (userPackageInfo.dropInPackageCount > 1) {
                        textViewMore.setVisibility(View.VISIBLE);
                        String more = "+" + numberConverter(userPackageInfo.dropInPackageCount - 1) + " " + context.getString(R.string.more) + " " +
                                AppConstants.plural(context.getString(R.string.package_name), (userPackageInfo.dropInPackageCount - 1));
                        textViewMore.setText(Html.fromHtml("<b><u>" + more + "</u></b> "));
                    }
                }
                /************************************************************************/
            } else {
                textViewExtendPackage.setVisibility(View.GONE);

                /************************NO PACKAGE******************************/
                textViewPackage.setText(Html.fromHtml("<b>" + numberConverter(0) + "</b> " + context.getString(R.string.p5m_credits).toLowerCase()));
                textViewRecharge.setVisibility(View.VISIBLE);
                textViewMore.setVisibility(View.GONE);
                textViewValidity.setVisibility(View.GONE);
                textViewValidity.setText(R.string.profile_validiy_no_package);
                /***************************************************************/
            }
            if (userpackageToExtend != null && userpackageToExtend.getTotalRemainingWeeks() != null && userpackageToExtend.getTotalRemainingWeeks() < 1) {
                textViewExtendPackage.setVisibility(View.GONE);

            }

            textViewRecharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ProfileHeaderViewHolder.this, textViewRecharge, user, position);
                }
            });

            textViewExtendPackage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ProfileHeaderViewHolder.this, textViewExtendPackage, userpackageToExtend, position);
                }
            });
            linearLayoutUserWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ProfileHeaderViewHolder.this, linearLayoutUserWallet, userpackageToExtend, position);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ProfileHeaderViewHolder.this, imageView, user, position);
                }
            });

            textViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ProfileHeaderViewHolder.this, textViewMore, user, position);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ProfileHeaderViewHolder.this, itemView, user, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

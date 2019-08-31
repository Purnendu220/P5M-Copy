package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.helper.Helper;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigSetUp;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LanguageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.p5m.me.utils.LanguageUtils.currencyConverter;
import static com.p5m.me.utils.LanguageUtils.numberConverter;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class MemberShipViewHolder extends RecyclerView.ViewHolder {






    private final Context context;
    private int shownInScreen;

/* User Existig Pakages Layout Declaration*/

    @BindView(R.id.mainLayoutUserPakages)
    RelativeLayout mainLayoutUserPakages;

    @BindView(R.id.packageTitle)
    TextView packageTitle;

    @BindView(R.id.packageUsage)
    TextView packageUsage;

    @BindView(R.id.layoutSoFarYouVisited)
    LinearLayout layoutSoFarYouVisited;

    @BindView(R.id.recyclerSoFarYouVisited)
    RecyclerView recyclerSoFarYouVisited;

    /* User Existig Pakages Layout Declaration*/

    /* User Existig Pakages DROP IN Layout Declaration*/
    @BindView(R.id.textViewActiveDropIn)
    TextView textViewActiveDropIn;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.mainLayoutActivePackageDropin)
    LinearLayout mainLayoutActivePackageDropin;

    /* User Existig Pakages DROP IN Layout Declaration*/

    /* User Offered Pakages   Layout Declaration*/
    @BindView(R.id.txtPackageName)
    TextView txtPackageName;

    @BindView(R.id.txtIsPackagePopular)
    TextView txtIsPackagePopular;
    @BindView(R.id.txtPackageOffer)
    TextView txtPackageOffer;

    @BindView(R.id.txtPackageOffredClasses)
    TextView txtPackageOffredClasses;

    @BindView(R.id.txtPriceBeforeOffer)
    TextView txtPriceBeforeOffer;

    @BindView(R.id.txtPriceAfterOffer)
    TextView txtPriceAfterOffer;

    @BindView(R.id.packageValidFor)
    TextView packageValidFor;

    @BindView(R.id.layoutMainOfferedPackage)
    LinearLayout layoutMainOfferedPackage;

    /* User Offered Pakages   Layout Declaration*/


    public MemberShipViewHolder(View itemView, int shownInScreen) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
       // RemoteConfigSetUp.setBackgroundColor(linearLayoutOffer, RemoteConfigConst.MEMBERSHIP_OFFER_COLOR_VALUE, context.getResources().getColor(R.color.green));

        this.shownInScreen = shownInScreen;
    }

    public void bind(ClassModel classModel, final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && (data instanceof UserPackage || data instanceof Package)) {
//            textViewViewLimit.setText(Html.fromHtml(RemoteConfigConst.GYM_VISIT_LIMIT_VALUE));
//
//
//            itemView.setVisibility(View.VISIBLE);
//            imageViewInfo.setVisibility(View.GONE);
//
//            button.setEnabled(true);
//            button.setBackgroundResource(R.drawable.join_rect);
//            button.setTextColor(ContextCompat.getColor(context, R.color.white));

            // Package owned..
            if (data instanceof UserPackage) {
                UserPackage model = (UserPackage) data;
                if(model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)){
                    mainLayoutActivePackageDropin.setVisibility(View.GONE);
                    mainLayoutUserPakages.setVisibility(View.VISIBLE);
                    layoutMainOfferedPackage.setVisibility(View.GONE);
                    packageTitle.setText(model.getPackageName());
                    packageUsage.setText(String.format(context.getResources().getString(R.string.classess_remaining),model.getBalanceClass(),model.getTotalNumberOfClass()));
                    packageValidFor.setText(context.getString(R.string.valid_till) + " " + DateUtils.getPackageClassDate(model.getExpiryDate()));
                }
                else{
                    mainLayoutActivePackageDropin.setVisibility(View.VISIBLE);
                    mainLayoutUserPakages.setVisibility(View.GONE);
                    layoutMainOfferedPackage.setVisibility(View.GONE);
                    textViewActiveDropIn.setText(String.format(context.getResources().getString(R.string.drop_in_text),model.getGymName(),DateUtils.getDaysLeftFromPackageExpiryDate(model.getExpiryDate())));
                }


//                textViewPackagePrice.setVisibility(View.GONE);
//                linearLayoutOffer.setVisibility(View.GONE);
//                textViewPackagePriceStrike.setVisibility(View.GONE);
//                button.setText(R.string.your_current_plan);
//                if (model.getBalanceClass() > 0) {
//                    textViewExtendPackage.setVisibility(View.VISIBLE);
//                } else {
//                    textViewExtendPackage.setVisibility(View.GONE);
//
//                }
//                if (model != null && model.getTotalRemainingWeeks() < 1) {
//                    textViewExtendPackage.setVisibility(View.GONE);
//                }
//                button.setEnabled(false);
//                button.setBackgroundResource(R.drawable.button_disabled);
//                button.setTextColor(ContextCompat.getColor(context, R.color.theme_light_text));
//
//                Helper.setPackageImage(imageViewHeader, model.getPackageId());
//                //imageViewHeader.setImageResource(R.drawable.set_icon);
//
//                if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
//                    textViewPackageName.setText(model.getPackageName());
//
//                    textViewPageTitle.setText(LanguageUtils.numberConverter(model.getBalanceClass()) + " " + AppConstants.pluralES(context.getString(R.string.classs), model.getBalanceClass()) + " " + context.getString(R.string.remaining));
//
//                    textViewPackageValidity.setText(context.getString(R.string.valid_till) + " " + DateUtils.getPackageClassDate(model.getExpiryDate()));
//
//                    textViewViewLimit.setVisibility(View.VISIBLE);
//                    if (classModel != null) {
//                        int numberOfDays = DateUtils.getDaysLeftFromPackageExpiryDate(model.getExpiryDate());
//
//                        if (DateUtils.getDaysLeftFromPackageExpiryDate(classModel.getClassDate()) > numberOfDays) {
//                            imageViewInfo.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                } else if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
//                    textViewOr.setVisibility(View.GONE);
//                    viewOr.setVisibility(View.GONE);
//
//                    textViewPackageName.setText(model.getPackageName());
//                    LanguageUtils.setText(textViewPageTitle, model.getBalanceClass(), " " +AppConstants.pluralES(context.getString(R.string.classs), model.getBalanceClass())+" " + context.getString(R.string.class_remaining_for) + " " + model.getGymName());
//
//                    textViewPackageValidity.setText(context.getString(R.string.valid_till) + " " + DateUtils.getPackageClassDate(model.getExpiryDate()));
//                    if (model.getExpiryDate() == null || model.getExpiryDate().trim().length() == 0) {
//                        textViewPackageValidity.setVisibility(View.GONE);
//                        textViewExtendPackage.setVisibility(View.GONE);
//                    }
//                    textViewViewLimit.setVisibility(View.GONE);
//                    imageViewInfo.setVisibility(View.VISIBLE);
//                }
            } else
                // Packages offered..
                if (data instanceof Package) {
                    Package model = (Package) data;
//                    textViewPackagePrice.setVisibility(View.VISIBLE);
//                    textViewExtendPackage.setVisibility(View.GONE);
//
////                    button.setText(R.string.select_plan);
//                    button.setText(RemoteConfigConst.SELECT_PLAN_TEXT_VALUE);
//                    RemoteConfigSetUp.setBackgroundColor(button, RemoteConfigConst.SELECT_PLAN_COLOR_VALUE, context.getResources().getColor(R.color.theme_book));
//                    button.setBackgroundResource(R.drawable.join_rect);
//                    Helper.setPackageImage(imageViewHeader, model.getId());
//                    if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
//
//                        textViewPackageName.setText(model.getName());
//                        textViewPageTitle.setText(numberConverter(model.getNoOfClass()) + " " + AppConstants.pluralES(context.getString(R.string.classs), model.getNoOfClass()) + " " + context.getString(R.string.at_any_gym));
////                        LanguageUtils.setText(textViewPageTitle,model.getNoOfClass()," " + AppConstants.pluralES(context.getString(R.string.classs)
////                                , model.getNoOfClass())+" "+context.getString(R.string.at_any_gym));
//                      /*  validityPeriod = Helper.capitalize(validityPeriod);
//
//                        // Removing s from last
//                        if (validityPeriod.charAt(validityPeriod.length() - 1) == 's') {
//                            validityPeriod = validityPeriod.substring(0, validityPeriod.length() - 1);
//                        }*/
//
//                        setTextValidityPeriod(model);
//                        textViewPackagePrice.setText(LanguageUtils.numberConverter(model.getCost(),2) + " " + context.getString(R.string.currency).toUpperCase());
//
//                        textViewViewLimit.setVisibility(View.VISIBLE);
//
//                        if (classModel != null) {
//                            int numberOfDays = model.getDuration();
//
//                            switch (model.getValidityPeriod()) {
//                                case "DAYS":
//                                    numberOfDays *= 1;
//                                    break;
//                                case "WEEKS":
//                                    numberOfDays *= 7;
//                                    break;
//                                case "MONTHS":
//                                    numberOfDays *= 30;
//                                    break;
//                                case "YEARS":
//                                    numberOfDays *= 365;
//                                    break;
//                            }
//
//                            if (DateUtils.getDaysLeftFromPackageExpiryDate(classModel.getClassDate()) > numberOfDays) {
//                                imageViewInfo.setVisibility(View.VISIBLE);
//                                button.setEnabled(false);
//                                button.setBackgroundResource(R.drawable.button_disabled);
//                                button.setTextColor(ContextCompat.getColor(context, R.color.theme_light_text));
//                            }
//                        }
//
//                    } else if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
//                        textViewOr.setVisibility(View.VISIBLE);
//                        viewOr.setVisibility(View.VISIBLE);
//                        textViewPackageName.setText(model.getName());
//                        if (model.isBookingWithFriend() && model.getNoOfClass() == 1) {
//                            textViewOr.setVisibility(View.GONE);
//                            viewOr.setVisibility(View.GONE);
//
//                            textViewPageTitle.setText(model.getNoOfClass() + " " + AppConstants.pluralES(context.getString(R.string.classs), model.getNoOfClass()) + " for friend");
//                        } else {
//                            textViewPageTitle.setText(LanguageUtils.numberConverter(model.getNoOfClass()) + " " + AppConstants.pluralES(context.getString(R.string.classs), model.getNoOfClass()) + " " + context.getString(R.string.at_any_gym));
//
//                        }
//                        if (model.getNoOfClass() == 1) {
//
//                            textViewPageTitle.setText(context.getString(R.string.class_one_at) + " " + model.getGymName());
//                        } else
//                            textViewPageTitle.setText(LanguageUtils.numberConverter(model.getNoOfClass()) + " " + AppConstants.pluralES(context.getString(R.string.classs), model.getNoOfClass()) + " " + context.getString(R.string.at) + " " + model.getGymName());
//
//                        textViewPackageValidity.setText(context.getString(R.string.valid_for) + " " + DateUtils.getPackageClassDate(classModel.getClassDate()) + " -" + DateUtils.getClassTime(classModel.getFromTime(), classModel.getToTime()));
//                        textViewPackagePrice.setText(LanguageUtils.numberConverter(model.getCost(),2) + " " + context.getString(R.string.currency).toUpperCase());
//
//                        textViewViewLimit.setVisibility(View.GONE);
//
//                        imageViewInfo.setVisibility(View.VISIBLE);
//                    }
//                    if (model.getPromoResponseDto() != null ) {
//                        if( model.getPromoResponseDto().getDiscountType().equalsIgnoreCase(AppConstants.ApiParamKey.NUMBEROFCLASS))
//                            setClassPromo(model);
//                        else if( model.getPromoResponseDto().getDiscountType().equalsIgnoreCase(AppConstants.ApiParamKey.NUMBEROFDAYS))
//                            setClassPromo(model);
//                        else {
//                            setPackagePriceAfterDiscount(model, textViewPackagePrice, textViewPackagePriceStrike);
//                        }
//                    } else {
//                        linearLayoutOffer.setVisibility(View.GONE);
//                        textViewPackagePriceStrike.setVisibility(View.GONE);
//                        textViewOr.setVisibility(View.GONE);
//                        viewOr.setVisibility(View.GONE);
//
//                    }

                }
//
//            textViewViewLimit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    adapterCallbacks.onAdapterItemClick(MemberShipViewHolder.this, textViewViewLimit, data, position);
//                }
//            });
//
//            textViewExtendPackage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    adapterCallbacks.onAdapterItemClick(MemberShipViewHolder.this, textViewExtendPackage, data, position);
//                }
//            });
//
//            imageViewInfo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    adapterCallbacks.onAdapterItemClick(MemberShipViewHolder.this, imageViewInfo, data, position);
//                }
//            });
//
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    adapterCallbacks.onAdapterItemClick(MemberShipViewHolder.this, button, data, position);
//                }
//            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }

//    private void setClassPromo(Package model) {
//        if(model.getPromoResponseDto().getPromoDesc()!=null) {
//            layoutTimeClassPromo.setVisibility(View.VISIBLE);
//            textViewSpecialOffer.setText(Html.fromHtml("<font color='#f34336'>" + context.getResources().getString(R.string.special_offer)
//                    + "</font>" + " " + model.getPromoResponseDto().getPromoDesc()));
//
//        }
//    }
//
//    private void setTextValidityPeriod(Package model) {
//        if (model.getValidityPeriod().contains("MONTH"))
////         textViewPackageValidity.setText(context.getString(R.string.valid_for) + " " + numberConverter(model.getDuration()) + " " + AppConstants.plural(validityPeriod, model.getDuration()));
////            textViewPackageValidity.setText(String.format(context.getResources().getString(R.string.valid_for_months), model.getDuration()));
//        {
//            if(model.getDuration()==1) {
//                textViewPackageValidity.setText(context.getResources().getString(R.string.valid_for_a_months));
//            }
//            else
//                textViewPackageValidity.setText(String.format(context.getResources().getString(R.string.valid_for_months), model.getDuration()));
//
//        }
//        else if (model.getValidityPeriod().contains("WEEK")) {
//            if(model.getDuration()==1) {
//                textViewPackageValidity.setText(context.getResources().getString(R.string.valid_for_a_week));
//            }
//            else
//                textViewPackageValidity.setText(String.format(context.getResources().getString(R.string.valid_for_weeks), model.getDuration()));
//
//        }
//    }
//
//    private void setPackagePriceAfterDiscount(Package model, TextView textViewPackagePrice, TextView textViewPackagePriceStrike) {
//        linearLayoutOffer.setVisibility(View.VISIBLE);
//        textViewPackagePriceStrike.setVisibility(View.VISIBLE);
//        String offerText;
//        if (model.getPromoResponseDto().getDiscountType().equalsIgnoreCase(AppConstants.ApiParamValue.PACKAGE_OFFER_PERCENTAGE)) {
//            offerText = context.getString(R.string.package_offer_percentage);
//        } else {
//            offerText = context.getString(R.string.package_offer_kwd);
//
//        }
//        try {
//            if (Math.round(model.getPromoResponseDto().getDiscount()) == 0) {
//                textViewPackagePriceStrike.setVisibility(View.GONE);
//                linearLayoutOffer.setVisibility(View.GONE);
//            } else {
//                linearLayoutOffer.setVisibility(View.VISIBLE);
//                textViewOffer.setText(currencyConverter(Math.round(model.getPromoResponseDto().getDiscount())) + offerText);
//            }
//        } catch (Exception e) {
//            textViewOffer.setText(LanguageUtils.numberConverter(model.getPromoResponseDto().getDiscount(),2) + offerText);
//
//        }
//
//        textViewPackagePrice.setText(LanguageUtils.numberConverter(model.getPromoResponseDto().getPriceAfterDiscount(),2) + " " + context.getString(R.string.currency).toUpperCase());
//        textViewPackagePriceStrike.setText(LanguageUtils.numberConverter(model.getPromoResponseDto().getPrice(),2) + " " + context.getString(R.string.currency).toUpperCase());
//        textViewPackagePriceStrike.setPaintFlags(textViewPackagePriceStrike.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//
//    }

}

package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.UserPackageDetailAdapter;
import com.p5m.me.data.PackageTags;
import com.p5m.me.data.UserPackageDetail;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.Main.GymProfileActivity;

import java.util.List;

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
    private List<PackageTags> listPackageTags;

    /* User Existig Pakages Layout Declaration*/

    @BindView(R.id.mainLayoutUserPakages)
    RelativeLayout mainLayoutUserPakages;

    @BindView(R.id.packageTitle)
    TextView packageTitle;

    @BindView(R.id.packageUsage)
    TextView packageUsage;

    @BindView(R.id.credits)
    TextView credits;

    @BindView(R.id.textViewSoFarYouVisited)
    TextView textViewSoFarYouVisited;

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

    @BindView(R.id.packageValidForOwn)
    TextView packageValidForOwn;

    @BindView(R.id.textViewExtendPackage)
    TextView textViewExtendPackage;


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
    @BindView(R.id.packageLayout)
    LinearLayout packageLayout;
    @BindView(R.id.layoutNotApplicable)
    RelativeLayout layoutNotApplicable;
    @BindView(R.id.imageViewNotApplicableInfo)
    ImageView imageViewNotApplicableInfo;

    @BindView(R.id.layoutTimeClassPromo)
    LinearLayout layoutTimeClassPromo;

    @BindView(R.id.textViewSpecialOffer)
    TextView textViewSpecialOffer;


    private int numberOfDays;

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

            itemView.setVisibility(View.VISIBLE);
            textViewExtendPackage.setVisibility(View.GONE);
            // Package owned..
            if (data instanceof UserPackage) {
                UserPackage model = (UserPackage) data;
                if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                    textViewExtendPackage.setVisibility(View.VISIBLE);
                    if (model != null && model.getTotalRemainingWeeks() != null && model.getTotalRemainingWeeks() < 1) {
                        textViewExtendPackage.setVisibility(View.GONE);

                    }

                    textViewExtendPackage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            adapterCallbacks.onAdapterItemClick(MemberShipViewHolder.this, textViewExtendPackage, model, position);
                        }
                    });
                    mainLayoutActivePackageDropin.setVisibility(View.GONE);
                    mainLayoutUserPakages.setVisibility(View.VISIBLE);
                    layoutMainOfferedPackage.setVisibility(View.GONE);
                    packageTitle.setText(model.getPackageName());
                    packageUsage.setText(String.format(context.getResources().getString(R.string.classess_remaining), Integer.parseInt(LanguageUtils.numberConverter(model.getBalanceClass())), Integer.parseInt(LanguageUtils.numberConverter(model.getTotalNumberOfClass()))));
                    packageValidForOwn.setText(context.getString(R.string.valid_till) + " " + DateUtils.getPackageClassDate(model.getExpiryDate()));
                    setPackageTags(model.getPackageId()
                    );
                } else {
                    mainLayoutActivePackageDropin.setVisibility(View.VISIBLE);
                    textViewExtendPackage.setVisibility(View.GONE);
                    mainLayoutUserPakages.setVisibility(View.GONE);
                    layoutMainOfferedPackage.setVisibility(View.GONE);
                    if (model.getBalanceClass() == 2) {
                        if (model.getExpiryDate() == null || TextUtils.isEmpty(model.getExpiryDate()))
                            textViewActiveDropIn.setText(String.format(context.getResources().getString(R.string.drop_in_unlimited), model.getGymName()));
                        else
                            textViewActiveDropIn.setText(String.format(context.getResources().getString(R.string._two_drop_in_text), model.getGymName(), DateUtils.getPackageClassDate(model.getExpiryDate())));
                    } else {
                        if (model.getExpiryDate() == null || TextUtils.isEmpty(model.getExpiryDate()))
                            textViewActiveDropIn.setText(String.format(context.getResources().getString(R.string.drop_in_unlimited), model.getGymName()));
                        else
                            textViewActiveDropIn.setText(String.format(context.getResources().getString(R.string.drop_in_text), model.getGymName(), DateUtils.getPackageClassDate(model.getExpiryDate())));
                    }
                    if (classModel != null && model.getExpiryDate() != null && DateUtils.isDateisPast(model.getExpiryDate(), classModel.getClassDate())) {
                        mainLayoutActivePackageDropin.setAlpha(0.5f);


                    } else {
                        mainLayoutActivePackageDropin.setAlpha(1.0f);

                    }
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (classModel != null && model.getExpiryDate() != null && DateUtils.isDateisPast(model.getExpiryDate(), classModel.getClassDate())) {
                                ToastUtils.show(context, context.getResources().getString(R.string.you_cant_use_package));
                            } else {
                                GymProfileActivity.open(context, model.getGymId(), AppConstants.AppNavigation.NAVIGATION_FROM_MEMBERSHIP);

                            }
                        }
                    });
                }
                setUpUserPackageDetail(model.getId(), context, adapterCallbacks, recyclerSoFarYouVisited);
            } else
                // Packages offered..
                if (data instanceof Package) {
                    Package model = (Package) data;
                    mainLayoutActivePackageDropin.setVisibility(View.GONE);
                    mainLayoutUserPakages.setVisibility(View.GONE);
                    layoutMainOfferedPackage.setVisibility(View.VISIBLE);
                    handleCreditText(model);
                    if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                        txtPackageName.setText(model.getName());
                        txtPackageOffredClasses.setText(numberConverter(model.getNoOfClass()) + " " + AppConstants.pluralES(context.getString(R.string.classs_one), model.getNoOfClass()) + " " + context.getString(R.string.at_any_gym));
                        setTextValidityPeriod(model);
                        txtPriceAfterOffer.setText(LanguageUtils.numberConverter(model.getCost(), 2) + " " + (TempStorage.getUser().getCurrencyCode()).toUpperCase());
                        setPackageTags(model.getId());


                    } else if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                        setPackageTags(model.getId());

                        txtPackageName.setText(model.getName());
                        if (model.isBookingWithFriend() && model.getNoOfClass() == 1) {

                            txtPackageOffredClasses.setText(model.getNoOfClass() + " " + AppConstants.pluralES(context.getString(R.string.classs), model.getNoOfClass()) + " for friend");
                        } else {
                            txtPackageOffredClasses.setText(LanguageUtils.numberConverter(model.getNoOfClass()) + " " + AppConstants.pluralES(context.getString(R.string.classs), model.getNoOfClass()) + " " + context.getString(R.string.at_any_gym));

                        }
                        if (model.getNoOfClass() == 1) {

                            txtPackageOffredClasses.setText(context.getString(R.string.class_one_at) + " " + model.getGymName());
                        } else
                            txtPackageOffredClasses.setText(LanguageUtils.numberConverter(model.getNoOfClass()) + " " + AppConstants.pluralES(context.getString(R.string.classs), model.getNoOfClass()) + " " + context.getString(R.string.at) + " " + model.getGymName());

                        packageValidFor.setText(context.getString(R.string.valid_for) + " " + DateUtils.getPackageClassDate(classModel.getClassDate()) + " -" + DateUtils.getClassTime(classModel.getFromTime(), classModel.getToTime()));
                        txtPriceAfterOffer.setText(LanguageUtils.numberConverter(model.getCost(), 2) + " " + (TempStorage.getUser().getCurrencyCode()).toUpperCase());


                    }

                    manageViews(classModel, model);
                    if (model.getPromoResponseDto() != null) {
                        if (model.getPromoResponseDto().getDiscountType().equalsIgnoreCase(AppConstants.ApiParamKey.NUMBEROFCLASS))
                            setClassPromo(model);
                        else if (model.getPromoResponseDto().getDiscountType().equalsIgnoreCase(AppConstants.ApiParamKey.NUMBEROFDAYS))
                            setClassPromo(model);
                        else {
                            setPackagePriceAfterDiscount(model, txtPriceAfterOffer, txtPackageOffer, txtPriceBeforeOffer);
                        }
                    } else {
                        txtPackageOffer.setVisibility(View.GONE);
                        txtPriceBeforeOffer.setVisibility(View.GONE);

                    }
                    layoutMainOfferedPackage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL) &&
                                    classModel != null && DateUtils.getDaysLeftFromPackageExpiryDate(classModel.getClassDate()) > numberOfDays) {

                            } else
                                adapterCallbacks.onAdapterItemClick(MemberShipViewHolder.this, layoutMainOfferedPackage, data, position);

                        }
                    });
                    imageViewNotApplicableInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (classModel != null && DateUtils.getDaysLeftFromPackageExpiryDate(classModel.getClassDate()) > numberOfDays) {
                                adapterCallbacks.onAdapterItemClick(MemberShipViewHolder.this, imageViewNotApplicableInfo, data, position);

                            }
                        }
                    });
                    txtPackageOffredClasses.setVisibility(View.GONE);

                }


        } else {
            itemView.setVisibility(View.GONE);
        }
    }

    private void handleCreditText(Package model) {
        String creditString = String.format(context.getResources().getString(R.string.include_credits), Integer.parseInt(LanguageUtils.numberConverter(model.getCredits()))," 1-"+LanguageUtils.numberConverter(model.getCredits()/5));

        SpannableString ss = new SpannableString(creditString);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
//Showing popup
//                startActivity(new Intent(MyActivity.this, NextActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 22, ss.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        credits.setText(ss);
        credits.setMovementMethod(LinkMovementMethod.getInstance());
        credits.setHighlightColor(Color.TRANSPARENT);
    }

    private void manageViews(ClassModel classModel, Package model) {

        if (classModel != null) {
            numberOfDays = model.getDuration();


            switch (model.getValidityPeriod()) {
                case "DAYS":
                    numberOfDays *= 1;
                    break;
                case "WEEKS":
                    numberOfDays *= 7;
                    break;
                case "MONTHS":
                    numberOfDays *= 30;
                    break;
                case "YEARS":
                    numberOfDays *= 365;
                    break;
            }

            if (numberOfDays > 0 && DateUtils.getDaysLeftFromPackageExpiryDate(classModel.getClassDate()) > numberOfDays) {
                mainLayoutActivePackageDropin.setVisibility(View.GONE);
                mainLayoutUserPakages.setVisibility(View.GONE);
                layoutMainOfferedPackage.setVisibility(View.VISIBLE);
                layoutMainOfferedPackage.setClickable(true);
                layoutMainOfferedPackage.setAlpha(0.5f);
                layoutNotApplicable.setVisibility(View.VISIBLE);
//                                packageLayout.setBackgroundColor(context.getResources().getColor(R.color.grey));

            } else {
                mainLayoutActivePackageDropin.setVisibility(View.GONE);
                mainLayoutUserPakages.setVisibility(View.GONE);
                layoutMainOfferedPackage.setVisibility(View.VISIBLE);
                layoutMainOfferedPackage.setClickable(true);
                layoutMainOfferedPackage.setAlpha(1.0f);
                layoutNotApplicable.setVisibility(View.GONE);
//                                packageLayout.setBackgroundColor(context.getResources().getColor(R.color.white));

            }
        } else {
            mainLayoutActivePackageDropin.setVisibility(View.GONE);
            mainLayoutUserPakages.setVisibility(View.GONE);
            layoutMainOfferedPackage.setVisibility(View.VISIBLE);
            layoutMainOfferedPackage.setClickable(true);
            layoutMainOfferedPackage.setAlpha(1.0f);
            layoutNotApplicable.setVisibility(View.GONE);
        }
    }


    private void setClassPromo(Package model) {
        if (model.getPromoResponseDto().getPromoDesc() != null) {
            layoutTimeClassPromo.setVisibility(View.VISIBLE);
            textViewSpecialOffer.setText(Html.fromHtml("<font color='#f34336'>" + context.getResources().getString(R.string.special_offer)
                    + "</font>" + " " + model.getPromoResponseDto().getPromoDesc()));

        } else {
            layoutTimeClassPromo.setVisibility(View.GONE);

        }
        txtPackageOffer.setVisibility(View.GONE);
        txtPriceBeforeOffer.setVisibility(View.GONE);

    }

    private void setTextValidityPeriod(Package model) {
        if (model.getValidityPeriod().contains("MONTH")) {
            if (model.getDuration() == 1) {
                packageValidFor.setText(context.getResources().getString(R.string.valid_for_a_months));
            } else
                packageValidFor.setText(String.format(context.getResources().getString(R.string.valid_for_months), model.getDuration()));

        } else if (model.getValidityPeriod().contains("WEEK")) {
            if (model.getDuration() == 1) {
                packageValidFor.setText(context.getResources().getString(R.string.valid_for_a_week));
            } else
                packageValidFor.setText(String.format(context.getResources().getString(R.string.valid_for_weeks), model.getDuration()));

        }
    }

    private void setPackagePriceAfterDiscount(Package model, TextView textViewPackagePrice, TextView textViewOffer, TextView textViewPackagePriceStrike) {
        textViewOffer.setVisibility(View.VISIBLE);
        textViewPackagePriceStrike.setVisibility(View.VISIBLE);
        textViewOffer.setVisibility(View.VISIBLE);

        String offerText;
        if (model.getPromoResponseDto().getDiscountType().equalsIgnoreCase(AppConstants.ApiParamValue.PACKAGE_OFFER_PERCENTAGE)) {
            offerText = context.getString(R.string.package_offer_percentage);
        } else {
            if (TempStorage.getUser().getCurrencyCode().equalsIgnoreCase(AppConstants.Currency.SAUDI_CURRENCY) ||
                    TempStorage.getUser().getCurrencyCode().equalsIgnoreCase(AppConstants.Currency.SAUDI_CURRENCY_SHORT))
                offerText = context.getString(R.string.sar_off);
            else
                offerText = context.getString(R.string.package_offer_kwd);

        }
        try {
            if (Math.round(model.getPromoResponseDto().getDiscount()) == 0) {
                textViewPackagePriceStrike.setVisibility(View.GONE);
                textViewOffer.setVisibility(View.GONE);
            } else {
                textViewOffer.setVisibility(View.VISIBLE);
                textViewOffer.setText(currencyConverter(Math.round(model.getPromoResponseDto().getDiscount())) + offerText);
            }
        } catch (Exception e) {
            textViewOffer.setText(LanguageUtils.numberConverter(model.getPromoResponseDto().getDiscount(), 2) + offerText);

        }

        textViewPackagePrice.setText(LanguageUtils.numberConverter(model.getPromoResponseDto().getPriceAfterDiscount(), 2) + " " + (TempStorage.getUser().getCurrencyCode()).toUpperCase());
        textViewPackagePriceStrike.setText(LanguageUtils.numberConverter(model.getPromoResponseDto().getPrice(), 2) + " " + (TempStorage.getUser().getCurrencyCode()).toUpperCase());
        textViewPackagePriceStrike.setPaintFlags(textViewPackagePriceStrike.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


    }

    private void setUpUserPackageDetail(long packageId, Context context, AdapterCallbacks adapterCallbacks, RecyclerView recyclerView) {
        NetworkCommunicator networkCommunicator = NetworkCommunicator.getInstance(context);
        networkCommunicator.getUserPackageDetails(TempStorage.getUser().getId(), packageId, new NetworkCommunicator.RequestListener() {
            @Override
            public void onApiSuccess(Object response, int requestCode) {
                List<UserPackageDetail> packageDetail = (List<UserPackageDetail>) response;
                if (packageDetail != null && packageDetail.size() > 0) {
                    layoutSoFarYouVisited.setVisibility(View.VISIBLE);

                    UserPackageDetailAdapter adapter = new UserPackageDetailAdapter(context, adapterCallbacks);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setHasFixedSize(false);
                    adapter.addAll(packageDetail);
                    recyclerView.setAdapter(adapter);
                } else {

                    layoutSoFarYouVisited.setVisibility(View.GONE);

                }


            }

            @Override
            public void onApiFailure(String errorMessage, int requestCode) {
                ToastUtils.show(context, errorMessage);
                layoutSoFarYouVisited.setVisibility(View.GONE);

            }
        }, false);


    }

    private void setPackageTags(int packageId) {
        txtIsPackagePopular.setVisibility(View.GONE);
        try {
            String packageTags = RemoteConfigConst.PACKAGE_TAGS_VALUE;
            if (packageTags != null && !packageTags.isEmpty()) {
                Gson g = new Gson();
                List<PackageTags> p = g.fromJson(packageTags, new TypeToken<List<PackageTags>>() {
                }.getType());
                listPackageTags = p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (listPackageTags != null && listPackageTags.size() > 0) {
            for (PackageTags p : listPackageTags) {
                if (p.getId() == packageId) {
                    if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("en")
                            && !p.getTag_en().isEmpty()) {
                        txtIsPackagePopular.setText(p.getTag_en());
                        txtIsPackagePopular.setVisibility(View.VISIBLE);

                    } else if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")
                            && !p.getTag_ar().isEmpty()) {
                        txtIsPackagePopular.setText(p.getTag_ar());
                        txtIsPackagePopular.setVisibility(View.VISIBLE);

                    }


                }

            }

        }

    }
}

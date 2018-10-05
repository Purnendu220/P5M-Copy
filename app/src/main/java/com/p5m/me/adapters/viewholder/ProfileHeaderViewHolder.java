package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.UserPackageInfo;
import com.p5m.me.data.main.User;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private final Context context;

    public ProfileHeaderViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks<Object> adapterCallbacks, final int position) {

        if (data != null && data instanceof User) {
            final User user = (User) data;
            itemView.setVisibility(View.VISIBLE);

            textViewName.setText(user.getFirstName()+" "+user.getLastName());
            UserPackageInfo userPackageInfo = new UserPackageInfo(user);

            ImageUtils.setImage(context,
                    user.getProfileImage(),
                    R.drawable.profile_holder_big, imageView);

            if (userPackageInfo.havePackages) {
                if(user.isBuyMembership()){
                    textViewRecharge.setVisibility(View.VISIBLE);

                }else{
                    textViewRecharge.setVisibility(View.GONE);

                }

                /*************************3 CONDITION ON PACKAGE**************************/
                textViewMore.setVisibility(View.GONE);

                if (userPackageInfo.haveGeneralPackage && !userPackageInfo.haveDropInPackage) {
                    textViewValidity.setVisibility(View.VISIBLE);

                    String message=userPackageInfo.userPackageGeneral.getBalanceClass()!=1?"classes":"class";
                    textViewPackage.setText(Html.fromHtml("<b>" + userPackageInfo.userPackageGeneral.getBalanceClass() +
                            "</b> " + message+" remaining"));

                    int daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageInfo.userPackageGeneral.getExpiryDate());

                    textViewValidity.setText(Html.fromHtml("<b>" + daysLeftFromPackageExpiryDate + " " +
                            AppConstants.plural("day", daysLeftFromPackageExpiryDate) + "</b> " +
                            context.getString(R.string.profile_package_expiry)));

                } else if (userPackageInfo.haveGeneralPackage && userPackageInfo.haveDropInPackage) {
                    textViewValidity.setVisibility(View.VISIBLE);
                    int daysLeftFromPackageExpiryDate;
                    if(user.isBuyMembership()){
                        textViewPackage.setText(Html.fromHtml("<b>1</b>" + " class for " + userPackageInfo.userPackageReady.get(0).getGymName()));

                         daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageInfo.userPackageReady.get(0).getExpiryDate());

                        textViewValidity.setText(Html.fromHtml("<b>" + daysLeftFromPackageExpiryDate + "</b> " +
                                " " + AppConstants.plural("day", daysLeftFromPackageExpiryDate) + context.getString(R.string.profile_package_expiry)));

                        if (userPackageInfo.dropInPackageCount+userPackageInfo.generalPackageCount > 1) {
                            textViewMore.setVisibility(View.VISIBLE);
                            String more = "+" + (userPackageInfo.dropInPackageCount+userPackageInfo.generalPackageCount-1 ) + " more " +
                                    AppConstants.plural("package", (userPackageInfo.dropInPackageCount - 1));
                            textViewMore.setText(Html.fromHtml("<b><u>" + more + "</u></b> "));
                        }
                    }else{

                        String message=userPackageInfo.userPackageGeneral.getBalanceClass()!=1?"classes ":"class";

                        textViewPackage.setText(Html.fromHtml("<b>" + userPackageInfo.userPackageGeneral.getBalanceClass() +
                                "</b> " + message+" remaining"));

                         daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageInfo.userPackageGeneral.getExpiryDate());

                        textViewValidity.setText(Html.fromHtml("<b>" + daysLeftFromPackageExpiryDate + " " +
                                AppConstants.plural("day", daysLeftFromPackageExpiryDate) + "</b> " +
                                context.getString(R.string.profile_package_expiry)));

                        textViewMore.setVisibility(View.VISIBLE);
                        String more = "+" + userPackageInfo.dropInPackageCount + " more " +
                                AppConstants.plural("package", userPackageInfo.dropInPackageCount);
                        textViewMore.setText(Html.fromHtml("<b><u>" + more + "</u></b> "));
                    }


                } else if (!userPackageInfo.haveGeneralPackage && userPackageInfo.haveDropInPackage) {
                    textViewValidity.setVisibility(View.VISIBLE);
                    textViewPackage.setText(Html.fromHtml("<b>1</b>" + " class for " + userPackageInfo.userPackageReady.get(0).getGymName()));

                    int daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageInfo.userPackageReady.get(0).getExpiryDate());

                    textViewValidity.setText(Html.fromHtml("<b>" + daysLeftFromPackageExpiryDate + "</b> " +
                            " " + AppConstants.plural("day", daysLeftFromPackageExpiryDate) + context.getString(R.string.profile_package_expiry)));

                    if (userPackageInfo.dropInPackageCount > 1) {
                        textViewMore.setVisibility(View.VISIBLE);
                        String more = "+" + (userPackageInfo.dropInPackageCount - 1) + " more " +
                                AppConstants.plural("package", (userPackageInfo.dropInPackageCount - 1));
                        textViewMore.setText(Html.fromHtml("<b><u>" + more + "</u></b> "));
                    }
                }
                /************************************************************************/
            } else {

                /************************NO PACKAGE******************************/
                textViewPackage.setText(Html.fromHtml("<b>" + 0 + "</b> " + context.getString(R.string.classes).toLowerCase()));
                textViewRecharge.setVisibility(View.VISIBLE);
                textViewMore.setVisibility(View.GONE);
                textViewValidity.setVisibility(View.GONE);
                textViewValidity.setText(R.string.profile_validiy_no_package);
                /***************************************************************/
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
                    adapterCallbacks.onAdapterItemClick(ProfileHeaderViewHolder.this, textViewExtendPackage, user, position);
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

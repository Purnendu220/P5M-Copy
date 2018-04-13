package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.User;
import www.gymhop.p5m.data.UserPackage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.DateUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ProfileHeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewClass)
    public ImageView imageViewClass;

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

            textViewName.setText(user.getFirstName());
            if (user.getUserPackageDetailDtoList() != null && !user.getUserPackageDetailDtoList().isEmpty()) {
                textViewRecharge.setVisibility(View.GONE);

                boolean haveGeneralPackage = false;
                boolean haveDropInPackage = false;
                int dropInPackageCount = 0;

                UserPackage userPackageGeneral = null;
                UserPackage userPackageReady = null;

                for (UserPackage userPackage : user.getUserPackageDetailDtoList()) {
                    if (userPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {
                        haveGeneralPackage = true;
                        userPackageGeneral = userPackage;
                    } else if (userPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {
                        haveDropInPackage = true;
                        if (userPackageReady != null) {
                            userPackageReady = userPackage;
                        }
                        dropInPackageCount++;
                    }
                }

                /*************************3 CONDITION ON PACKAGE**************************/
                textViewRecharge.setVisibility(View.GONE);
                textViewMore.setVisibility(View.GONE);

                if (haveGeneralPackage && !haveDropInPackage) {
                    textViewPackage.setText(Html.fromHtml("<b>" + userPackageGeneral.getBalanceClass() +
                            " of " + userPackageGeneral.getTotalNumberOfClass() + "</b> " + "classes remaining"));

                    int daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageGeneral.getExpiryDate());

                    textViewValidity.setText(Html.fromHtml("<b>" + daysLeftFromPackageExpiryDate + " " +
                            AppConstants.plural("day", daysLeftFromPackageExpiryDate) + "</b> " +
                            context.getString(R.string.profile_package_expiry)));

                } else if (haveGeneralPackage && haveDropInPackage) {
                    textViewPackage.setText(Html.fromHtml("<b>" + userPackageGeneral.getBalanceClass() +
                            " of " + userPackageGeneral.getTotalNumberOfClass() + "</b> " + "classes remaining"));

                    int daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageGeneral.getExpiryDate());

                    textViewValidity.setText(Html.fromHtml("<b>" + daysLeftFromPackageExpiryDate + " " +
                            AppConstants.plural("day", daysLeftFromPackageExpiryDate) + "</b> " +
                            context.getString(R.string.profile_package_expiry)));

                    textViewMore.setVisibility(View.VISIBLE);
                    textViewMore.setText("<b><u>" + "+" + daysLeftFromPackageExpiryDate + " more " +
                            AppConstants.plural("package", daysLeftFromPackageExpiryDate) + "</u></b> ");

                } else if (!haveGeneralPackage && haveDropInPackage) {
                    textViewPackage.setText(Html.fromHtml("<b>" + userPackageGeneral.getBalanceClass() + "</b> " +
                            "class of " + userPackageGeneral.getGymName() + "</b> " +
                            context.getString(R.string.classes).toLowerCase()) + " remaining");

                    int daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageGeneral.getExpiryDate());

                    textViewValidity.setText(Html.fromHtml("<b>" + daysLeftFromPackageExpiryDate + "</b> " +
                            " of " + AppConstants.plural("day", daysLeftFromPackageExpiryDate) + context.getString(R.string.profile_package_expiry)));

                    if (dropInPackageCount > 1) {
                        textViewMore.setVisibility(View.VISIBLE);
                        textViewMore.setText("<b><u>" + "+" + (daysLeftFromPackageExpiryDate - 1) + " more " +
                                AppConstants.plural("package", daysLeftFromPackageExpiryDate) + "</u></b> ");
                    }
                }
                /************************************************************************/
            } else {

                /************************NO PACKAGE******************************/
                textViewPackage.setText(Html.fromHtml("<b>" + 0 + "</b> " + context.getString(R.string.classes).toLowerCase()));
                textViewRecharge.setVisibility(View.VISIBLE);
                textViewValidity.setText("Buy packages");
                /***************************************************************/
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(itemView, itemView, user, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

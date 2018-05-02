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
import www.gymhop.p5m.data.main.User;
import www.gymhop.p5m.data.UserPackageInfo;
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
            UserPackageInfo userPackageInfo = new UserPackageInfo(user);

            if (userPackageInfo.havePackages) {
                textViewRecharge.setVisibility(View.GONE);

                /*************************3 CONDITION ON PACKAGE**************************/
                textViewRecharge.setVisibility(View.GONE);
                textViewMore.setVisibility(View.GONE);

                if (userPackageInfo.haveGeneralPackage && !userPackageInfo.haveDropInPackage) {
                    textViewPackage.setText(Html.fromHtml("<b>" + userPackageInfo.userPackageGeneral.getBalanceClass() +
                            " of " + userPackageInfo.userPackageGeneral.getTotalNumberOfClass() + "</b> " + "classes remaining"));

                    int daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageInfo.userPackageGeneral.getExpiryDate());

                    textViewValidity.setText(Html.fromHtml("<b>" + daysLeftFromPackageExpiryDate + " " +
                            AppConstants.plural("day", daysLeftFromPackageExpiryDate) + "</b> " +
                            context.getString(R.string.profile_package_expiry)));

                } else if (userPackageInfo.haveGeneralPackage && userPackageInfo.haveDropInPackage) {
                    textViewPackage.setText(Html.fromHtml("<b>" + userPackageInfo.userPackageGeneral.getBalanceClass() +
                            " of " + userPackageInfo.userPackageGeneral.getTotalNumberOfClass() + "</b> " + "classes remaining"));

                    int daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageInfo.userPackageGeneral.getExpiryDate());

                    textViewValidity.setText(Html.fromHtml("<b>" + daysLeftFromPackageExpiryDate + " " +
                            AppConstants.plural("day", daysLeftFromPackageExpiryDate) + "</b> " +
                            context.getString(R.string.profile_package_expiry)));

                    textViewMore.setVisibility(View.VISIBLE);
                    String more = "+" + userPackageInfo.dropInPackageCount + " more " +
                            AppConstants.plural("package", userPackageInfo.dropInPackageCount);
                    textViewMore.setText(Html.fromHtml("<b><u>" + more + "</u></b> "));

                } else if (!userPackageInfo.haveGeneralPackage && userPackageInfo.haveDropInPackage) {
                    textViewPackage.setText(Html.fromHtml("<b>1</b>" + " class for " + userPackageInfo.userPackageReady.get(0).getGymName()));

                    int daysLeftFromPackageExpiryDate = DateUtils.getDaysLeftFromPackageExpiryDate(userPackageInfo.userPackageReady.get(0).getExpiryDate());

                    textViewValidity.setText(Html.fromHtml("<b>" + daysLeftFromPackageExpiryDate + "</b> " +
                            " of " + AppConstants.plural("day", daysLeftFromPackageExpiryDate) + context.getString(R.string.profile_package_expiry)));

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
                textViewValidity.setText(R.string.profile_validiy_no_package);
                /***************************************************************/
            }

            textViewRecharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ProfileHeaderViewHolder.this, textViewRecharge, user, position);
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

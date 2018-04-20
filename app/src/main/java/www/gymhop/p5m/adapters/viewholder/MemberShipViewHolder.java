package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.Package;
import www.gymhop.p5m.data.UserPackage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.DateUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class MemberShipViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageViewHeader)
    public ImageView imageViewHeader;
    @BindView(R.id.imageViewInfo)
    public ImageView imageViewInfo;

    @BindView(R.id.textViewPackageName)
    public TextView textViewPackageName;
    @BindView(R.id.textViewViewLimit)
    public TextView textViewViewLimit;
    @BindView(R.id.textViewPackageValidity)
    public TextView textViewPackageValidity;
    @BindView(R.id.textViewPackagePrice)
    public TextView textViewPackagePrice;
    @BindView(R.id.textViewPageTitle)
    public TextView textViewPageTitle;

    @BindView(R.id.button)
    public Button button;

    private final Context context;
    private int shownInScreen;

    public MemberShipViewHolder(View itemView, int shownInScreen) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);

        this.shownInScreen = shownInScreen;
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && (data instanceof UserPackage || data instanceof Package)) {
            itemView.setVisibility(View.VISIBLE);
            imageViewInfo.setVisibility(View.GONE);

            // Package owned..
            if (data instanceof UserPackage) {
                UserPackage model = (UserPackage) data;
                textViewPackagePrice.setVisibility(View.GONE);
                button.setText(R.string.your_current_plan);
                button.setBackgroundResource(R.drawable.button_white);

                if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {

                    textViewPackageName.setText(model.getPackageName());
                    textViewPageTitle.setText(Html.fromHtml(model.getBalanceClass() +
                            " of " + model.getTotalNumberOfClass() + " " + "classes remaining"));
                    textViewPackageValidity.setText("Valid till " + DateUtils.getClassDate(model.getExpiryDate()));

                    textViewViewLimit.setVisibility(View.VISIBLE);

                } else if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {

                    textViewPackageName.setText(model.getPackageName());
                    textViewPageTitle.setText(model.getBalanceClass() + " class remaining");
                    textViewPackageValidity.setText("Valid for " + model.getGymName());

                    textViewViewLimit.setVisibility(View.GONE);
                    imageViewInfo.setVisibility(View.VISIBLE);
                }
            } else
                // Packages offered..
                if (data instanceof Package) {
                    Package model = (Package) data;
                    textViewPackagePrice.setVisibility(View.VISIBLE);
                    button.setText(R.string.select_plan);
                    button.setBackgroundResource(R.drawable.join_rect);

                    if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)) {

                        textViewPackageName.setText(model.getName());
                        textViewPageTitle.setText(model.getNoOfClass() + " " + context.getString(R.string.classes));
                        textViewPackageValidity.setText("Valid for " + model.getDuration() + " " + model.getValidityPeriod().toLowerCase());
                        textViewPackagePrice.setText(model.getCost() + " " + context.getString(R.string.currency).toUpperCase() + " " + context.getString(R.string.memebership_price_postfix));

                        textViewViewLimit.setVisibility(View.GONE);

                    } else if (model.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN)) {

                        textViewPackageName.setText(model.getName());
                        textViewPageTitle.setText(model.getNoOfClass() + " " + context.getString(R.string.classes));
                        textViewPackageValidity.setText("Valid for " + model.getGymName());
                        textViewPackagePrice.setText(model.getCost() + " " + context.getString(R.string.currency).toUpperCase() + " " + context.getString(R.string.memebership_price_postfix));

                        textViewViewLimit.setVisibility(View.VISIBLE);
                        imageViewInfo.setVisibility(View.VISIBLE);
                    }
                }

            imageViewInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(MemberShipViewHolder.this, imageViewInfo, data, position);
                }
            });

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(MemberShipViewHolder.this, button, data, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

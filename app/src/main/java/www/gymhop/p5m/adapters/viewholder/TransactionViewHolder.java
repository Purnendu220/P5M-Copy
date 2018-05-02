package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.main.Transaction;
import www.gymhop.p5m.utils.DateUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewRefId)
    public TextView textViewRefId;
    @BindView(R.id.textViewPackageName)
    public TextView textViewPackageName;
    @BindView(R.id.textViewPackagePrice)
    public TextView textViewPackagePrice;
    @BindView(R.id.textViewPackageDate)
    public TextView textViewPackageDate;
    @BindView(R.id.textViewPackageStatus)
    public TextView textViewPackageStatus;

    private final Context context;

    public TransactionViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof Transaction) {
            itemView.setVisibility(View.VISIBLE);

            Transaction model = (Transaction) data;

            textViewRefId.setText(model.getReferenceId());
            textViewPackageName.setText(model.getPackageName());
            textViewPackagePrice.setText(model.getAmount() + "");
            textViewPackageDate.setText(DateUtils.getTransactionDate(model.getDate()));
            textViewPackageStatus.setText(model.getStatus().toUpperCase());

        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

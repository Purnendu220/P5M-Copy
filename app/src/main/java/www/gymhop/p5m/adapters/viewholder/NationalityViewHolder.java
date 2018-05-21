package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.Nationality;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class NationalityViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textView)
    public TextView textView;
    @BindView(R.id.layoutItem)
    public View layoutItem;

    private final Context context;

    public NationalityViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof Nationality) {
            itemView.setVisibility(View.VISIBLE);

            Nationality model = (Nationality) data;

            textView.setText(model.getNationality());

            layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(NationalityViewHolder.this, itemView, data, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import www.gymhop.p5m.adapters.AdapterCallbacks;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class TrainerListViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

    public TrainerListViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object model, final AdapterCallbacks adapterCallbacks, final int position) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterCallbacks.onItemClick(itemView, itemView, model, position);
            }
        });
    }
}

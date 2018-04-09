package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.Class;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ClassViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

    public ClassViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Class model, final AdapterCallbacks<Class> adapterCallbacks, final int position) {

        if (model != null) {
            itemView.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onItemClick(itemView, itemView, model, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }

    }
}

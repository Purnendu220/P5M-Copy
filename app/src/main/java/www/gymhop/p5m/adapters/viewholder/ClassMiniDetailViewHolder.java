package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import www.gymhop.p5m.adapters.AdapterCallbacks;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ClassMiniDetailViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    public ClassMiniDetailViewHolder(View view) {
        super(view);

        context = view.getContext();
        ButterKnife.bind(this, view);
    }

    public void bind(Object model, AdapterCallbacks adapterCallbacks, int position) {
    }
}

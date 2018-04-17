package www.gymhop.p5m.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.PackageLimitListItem;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class PackageLimitTabsItemsViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public PackageLimitTabsItemsViewHolder(View itemView, TextView textView) {
        super(itemView);

        this.textView = textView;
    }

    public void bind(Object data, AdapterCallbacks adapterCallbacks, int position) {
        if (data != null && data instanceof PackageLimitListItem.Gym) {
            itemView.setVisibility(View.VISIBLE);

            PackageLimitListItem.Gym model = (PackageLimitListItem.Gym) data;

            textView.setText(model.name);

        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

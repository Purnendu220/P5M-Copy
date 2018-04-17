package www.gymhop.p5m.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.PackageLimitListItem;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class PackageLimitHeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public PackageLimitHeaderViewHolder(View itemView, TextView textView) {
        super(itemView);
        this.textView = textView;
    }

    public void bind(Object data, AdapterCallbacks adapterCallbacks, int position) {
        if (data != null && data instanceof PackageLimitListItem) {
            itemView.setVisibility(View.VISIBLE);
            PackageLimitListItem model = (PackageLimitListItem) data;

            textView.setText(Html.fromHtml("<b>" + model.getPackageLimitModel().getPackageName() + "</b>" +
                    " (" + model.getPackageLimitModel().getTotalClassPerPackage() + " Classes)"));
        } else {
            itemView.setVisibility(View.GONE);


        }
    }
}

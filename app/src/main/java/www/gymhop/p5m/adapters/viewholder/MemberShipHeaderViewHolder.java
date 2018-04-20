package www.gymhop.p5m.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.HeaderSticky;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class MemberShipHeaderViewHolder extends RecyclerView.ViewHolder {
    private TextView textView;

    public MemberShipHeaderViewHolder(View itemView, TextView textView) {
        super(itemView);
        this.textView = textView;
    }

    public void bind(Object data, AdapterCallbacks adapterCallbacks, int position) {
        if (data != null && data instanceof HeaderSticky) {
            itemView.setVisibility(View.VISIBLE);
            HeaderSticky text = (HeaderSticky) data;

            if (!text.getTitle().isEmpty()) {
                textView.setText(text.getTitle());
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }
        } else {
            itemView.setVisibility(View.GONE);

        }
    }
}

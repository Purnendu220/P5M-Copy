package www.gymhop.p5m.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import www.gymhop.p5m.adapters.AdapterCallbacks;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class SearchViewMoreViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public SearchViewMoreViewHolder(View itemView, TextView textView) {
        super(itemView);
        this.textView = textView;
    }

    public void bind(Object data, final AdapterCallbacks adapterCallbacks, final int position) {
        if (data != null && data instanceof String) {
            itemView.setVisibility(View.VISIBLE);
            final String text = (String) data;

            textView.setText(text);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(SearchViewMoreViewHolder.this, textView, text, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.HeaderSticky;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textView)
    public TextView textView;

    private Context context;

    public HeaderViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind(Object data, int position) {
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

package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.ClassActivity;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ChooseFocusViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageView)
    public ImageView imageView;
    @BindView(R.id.textView)
    public TextView textView;
    @BindView(R.id.layoutItem)
    public View layoutItem;

    private final Context context;

    public ChooseFocusViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        itemView.setClickable(true);

        ButterKnife.bind(this, itemView);
    }

    public void bind(List<ClassActivity> selected, final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof ClassActivity) {
            itemView.setVisibility(View.VISIBLE);

            ClassActivity model = (ClassActivity) data;

            textView.setText(model.getName());

            if (selected.contains(model)) {
                imageView.setImageResource(R.drawable.list_selected);
            } else {
                imageView.setImageResource(R.drawable.list_unselected);
            }

            layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ChooseFocusViewHolder.this, itemView, data, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

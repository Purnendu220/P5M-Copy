package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.CityLocality;
import www.gymhop.p5m.data.ClassesFilter;


public class FilterListItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageLeft)
    ImageView imageLeft;
    @BindView(R.id.imageRight)
    ImageView imageRight;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.layoutAlignment)
    View layoutAlignment;

    public int DP;

    private final Context context;

    public FilterListItemViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();
        DP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object model, final AdapterCallbacks adapterCallbacks, final int position) {
        ClassesFilter classesFilter = model instanceof ClassesFilter ? ((ClassesFilter) model) : null;

        if (classesFilter != null) {
            textView.setText(classesFilter.getName());

            if (classesFilter.getType() == ClassesFilter.TYPE_HEADER) {

                imageLeft.setImageResource(classesFilter.getIconResource());

                imageRight.setVisibility(View.VISIBLE);

                if (classesFilter.isExpanded()) {
                    imageRight.setImageResource(R.drawable.list_arrow_up);
                } else {
                    imageRight.setImageResource(R.drawable.list_arrow_down);
                }

                layoutAlignment.setPadding(0, 0, 0, 0);

            } else if (classesFilter.getType() == ClassesFilter.TYPE_SUB_HEADER) {

                if (classesFilter.isExpanded()) {
                    imageLeft.setImageResource(R.drawable.list_sub_up);
                } else {
                    imageLeft.setImageResource(R.drawable.list_sub_down);
                }

                imageRight.setVisibility(View.GONE);

                layoutAlignment.setPadding(DP * 20, 0, 0, 0);

            } else if (classesFilter.getType() == ClassesFilter.TYPE_ITEM) {

                if (classesFilter.isSelected()) {
                    imageLeft.setImageResource(R.drawable.list_selected);
                } else {
                    imageLeft.setImageResource(R.drawable.list_unselected);
                }

                imageRight.setVisibility(View.GONE);

                if (classesFilter.getObject() instanceof CityLocality) {
                    layoutAlignment.setPadding(DP * 32, 0, 0, 0);
                } else {
                    layoutAlignment.setPadding(DP * 20, 0, 0, 0);
                }
            }

            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(FilterListItemViewHolder.this, FilterListItemViewHolder.this.itemView, model, position);
                }
            });
        }

    }
}

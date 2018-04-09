package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class ProfileHeaderTabViewHolder extends RecyclerView.ViewHolder {

    public static final int TAB_1 = 1;
    public static final int TAB_2 = 2;

    @BindView(R.id.header1)
    public TextView header1;
    @BindView(R.id.header2)
    public TextView header2;

    @BindView(R.id.header1Indicator)
    public View header1Indicator;
    @BindView(R.id.header2Indicator)
    public View header2Indicator;

    private final Context context;

    public ProfileHeaderTabViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind(final int selectedTab, final Object model, final AdapterCallbacks<Object> adapterCallbacks, final int position) {

        switch (selectedTab) {
            case TAB_1:

                header1.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                header2.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                break;
            case TAB_2:

                header1.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                header2.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                break;
        }

        header1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != TAB_1) {
                    adapterCallbacks.onItemClick(itemView, header1, model, position);
                    header1.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                    header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                    header2.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                    header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                }
            }
        });
        header2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTab != TAB_2) {
                    adapterCallbacks.onItemClick(itemView, header2, model, position);
                    header1.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                    header1Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                    header2.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                    header2Indicator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                }
            }
        });
    }
}

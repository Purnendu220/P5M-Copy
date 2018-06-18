package com.p5m.me.adapters.viewholder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.PackageLimitAdapter;
import com.p5m.me.data.PackageLimitListItem;
import com.p5m.me.data.PackageLimitModel;

import java.util.List;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class PackageLimitTabsViewHolder extends RecyclerView.ViewHolder {

    private List<Tab> tabs;
    private Context context;

    public PackageLimitTabsViewHolder(View itemView, List<Tab> tabs) {
        super(itemView);

        this.tabs = tabs;
        context = itemView.getContext();
    }

    public void bind(final Object data, final PackageLimitAdapter packageLimitAdapter, int position) {
        if (data != null && data instanceof PackageLimitListItem) {
            itemView.setVisibility(View.VISIBLE);
            final PackageLimitListItem model = (PackageLimitListItem) data;

            for (int index = 0; index < tabs.size(); index++) {
                Tab tab = tabs.get(index);

                if (index + 1 > model.getList().size()) {
                    tab.view.setVisibility(View.GONE);
                } else {
                    tab.view.setVisibility(View.VISIBLE);

                    final PackageLimitModel packageLimitModel = model.getList().get(index);

                    String text = "";
                    if (packageLimitModel.getNumberOfVisit() == 1) {
                        text = packageLimitModel.getNumberOfVisit() + " TIME";
                    } else if (packageLimitModel.getNumberOfVisit() == 0) {
                        text = "UNLIMITED";
                    } else {
                        text = packageLimitModel.getNumberOfVisit() + " TIMES";
                    }

                    if (model.getSelectedTab() == index) {
                        tab.title.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                        tab.separator.setBackgroundColor(ContextCompat.getColor(context, R.color.theme_accent_text));
                        tab.title.setText(Html.fromHtml("<b>" + text + "</b>"));
                    } else {
                        tab.title.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
                        tab.separator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
                        tab.title.setText(text);
                    }

                    final int finalIndex = index;
                    tab.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (finalIndex != model.getPackageLimitListItem().getSelectedTab()) {
                                model.getPackageLimitListItem().setSelectedTab(finalIndex);
                                packageLimitAdapter.notifyDataSetChanges();
                            }
                        }
                    });
                }
            }
        } else {
            itemView.setVisibility(View.GONE);
        }
    }

    public static class Tab {
        public View view;
        public TextView title;
        public LinearLayout separator;

        public Tab(View view, TextView title, LinearLayout separator) {
            this.view = view;
            this.title = title;
            this.separator = separator;
        }
    }
}

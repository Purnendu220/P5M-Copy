package com.p5m.me.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.p5m.me.adapters.PackageLimitAdapter;
import com.p5m.me.data.PackageLimitListItem;
import com.p5m.me.helper.Helper;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class PackageLimitHeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public PackageLimitHeaderViewHolder(View itemView, TextView textView) {
        super(itemView);
        this.textView = textView;
    }

    public void bind(Object data, final PackageLimitAdapter packageLimitAdapter, int position) {
        if (data != null && data instanceof PackageLimitListItem) {
            itemView.setVisibility(View.VISIBLE);
            final PackageLimitListItem model = (PackageLimitListItem) data;

            String packageName = Helper.capitalize(model.getPackageLimitModel().getPackageName());

            textView.setText(Html.fromHtml("<b>" + packageName + "</b>" +
                    " (" + model.getPackageLimitModel().getTotalClassPerPackage() + " Classes)"));

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean state = model.isExpanded();

                    for (PackageLimitListItem packageLimitListItem : packageLimitAdapter.getPackageLimitListItems()) {
                        packageLimitListItem.setExpanded(false);
                    }
                    model.setExpanded(!state);

                    packageLimitAdapter.notifyDataSetChanges();
                }
            });
        } else {
            itemView.setVisibility(View.GONE);


        }
    }
}

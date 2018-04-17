package www.gymhop.p5m.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.PackageLimitListItem;
import www.gymhop.p5m.data.PackageLimitModel;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class PackageLimitTabsViewHolder extends RecyclerView.ViewHolder {
    public List<Tab> tabs;

    public PackageLimitTabsViewHolder(View itemView, List<Tab> tabs) {
        super(itemView);

        this.tabs = tabs;
    }

    public void bind(Object data, AdapterCallbacks adapterCallbacks, int position) {
        if (data != null && data instanceof PackageLimitListItem) {
            itemView.setVisibility(View.VISIBLE);
            PackageLimitListItem model = (PackageLimitListItem) data;

            for (int index = 0; index < tabs.size(); index++) {
                Tab tab = tabs.get(index);

                if (index + 1 > model.getList().size()) {
                    tab.view.setVisibility(View.GONE);
                } else {
                    tab.view.setVisibility(View.VISIBLE);

                    PackageLimitModel packageLimitModel = model.getList().get(index);

                    if (packageLimitModel.getNumberOfVisit() == 1) {
                        tab.title.setText(packageLimitModel.getNumberOfVisit() + " TIME");
                    } else if (packageLimitModel.getNumberOfVisit() == 0) {
                        tab.title.setText("UNLIMITED");
                    } else {
                        tab.title.setText(packageLimitModel.getNumberOfVisit() + " TIMES");
                    }
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

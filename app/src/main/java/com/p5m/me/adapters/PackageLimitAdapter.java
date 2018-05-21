package com.p5m.me.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.PackageLimitHeaderViewHolder;
import com.p5m.me.adapters.viewholder.PackageLimitMainHeaderViewHolder;
import com.p5m.me.adapters.viewholder.PackageLimitTabsItemsViewHolder;
import com.p5m.me.adapters.viewholder.PackageLimitTabsViewHolder;
import com.p5m.me.data.Empty;
import com.p5m.me.data.PackageLimitListItem;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class PackageLimitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_PACKAGE_LIMIT_MAIN_HEADER = 1;
    private static final int VIEW_TYPE_PACKAGE_LIMIT_HEADER = 2;
    private static final int VIEW_TYPE_PACKAGE_LIMIT_TAB = 3;
    private static final int VIEW_TYPE_PACKAGE_LIMIT_MODEL = 4;

    private final int dp;

    private Context context;
    private final AdapterCallbacks adapterCallbacks;

    private List<PackageLimitListItem> packageLimitListItems;
    private List<Object> list;
    private String header;

    public PackageLimitAdapter(Context context, AdapterCallbacks adapterCallbacks) {
        this.context = context;
        list = new ArrayList<>();
        packageLimitListItems = new ArrayList<>();
        this.adapterCallbacks = adapterCallbacks;

        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public List<PackageLimitListItem> getPackageLimitListItems() {
        return packageLimitListItems;
    }

    public List<Object> getList() {
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_PACKAGE_LIMIT_MODEL) {

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            LinearLayout linearLayoutSeparator = new LinearLayout(context);
            linearLayoutSeparator.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp));
            linearLayoutSeparator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));

            TextView textView = new TextView(context);
            textView.setPadding(dp * 16, dp * 12, dp * 16, dp * 12);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));

            linearLayout.addView(textView);
            linearLayout.addView(linearLayoutSeparator);

            return new PackageLimitTabsItemsViewHolder(linearLayout, textView);

        } else if (viewType == VIEW_TYPE_PACKAGE_LIMIT_HEADER) {

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            LinearLayout linearLayoutSeparator = new LinearLayout(context);
            linearLayoutSeparator.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp));
            linearLayoutSeparator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));

            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(dp * 16, dp * 16, dp * 16, dp * 16);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setBackgroundResource(R.drawable.click_highlight);
            textView.setClickable(true);
            textView.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));

            linearLayout.addView(textView);
            linearLayout.addView(linearLayoutSeparator);

            return new PackageLimitHeaderViewHolder(linearLayout, textView);

        } else if (viewType == VIEW_TYPE_PACKAGE_LIMIT_MAIN_HEADER) {

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            LinearLayout linearLayoutSeparator = new LinearLayout(context);
            linearLayoutSeparator.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp));
            linearLayoutSeparator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));

            TextView textView = new TextView(context);
            textView.setPadding(dp * 16, dp * 20, dp * 16, dp * 20);
            textView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6.0f, context.getResources().getDisplayMetrics()), 1.0f);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));

            linearLayout.addView(textView);
            linearLayout.addView(linearLayoutSeparator);

            return new PackageLimitMainHeaderViewHolder(linearLayout, textView);

        } else if (viewType == VIEW_TYPE_PACKAGE_LIMIT_TAB) {

            List<PackageLimitTabsViewHolder.Tab> tabs = new ArrayList<>(4);

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            for (int index = 0; index < AppConstants.Limit.PACKAGE_LIMIT_SCREEN_TABS; index++) {

                LinearLayout linearLayoutTab = new LinearLayout(context);
                linearLayoutTab.setOrientation(LinearLayout.VERTICAL);
                linearLayoutTab.setGravity(Gravity.CENTER);
                linearLayoutTab.setBackgroundColor(Color.WHITE);
                linearLayoutTab.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

                TextView textView = new TextView(context);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.click_highlight);
                textView.setPadding(dp * 12, dp * 12, dp * 16, dp * 12);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                textView.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));

                LinearLayout linearLayoutSeparator = new LinearLayout(context);
                linearLayoutSeparator.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2 * dp));
                linearLayoutSeparator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));

                linearLayoutTab.addView(textView);
                linearLayoutTab.addView(linearLayoutSeparator);
                linearLayout.addView(linearLayoutTab);
                tabs.add(new PackageLimitTabsViewHolder.Tab(linearLayoutTab, textView, linearLayoutSeparator));
            }

            return new PackageLimitTabsViewHolder(linearLayout, tabs);
        }

        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        LogUtils.debug("FilterActivity onBindViewHolder " + position);

        if (holder instanceof PackageLimitTabsItemsViewHolder) {
            ((PackageLimitTabsItemsViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        } else if (holder instanceof PackageLimitTabsViewHolder) {
            ((PackageLimitTabsViewHolder) holder).bind(getItem(position), this, position);
        } else if (holder instanceof PackageLimitMainHeaderViewHolder) {
            ((PackageLimitMainHeaderViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        } else if (holder instanceof PackageLimitHeaderViewHolder) {
            ((PackageLimitHeaderViewHolder) holder).bind(getItem(position), this, position);
        }
    }

    @Override
    public int getItemViewType(int position) {

        int itemViewType = VIEW_TYPE_UNKNOWN;

        Object item = getItem(position);
        if (item instanceof PackageLimitListItem.Gym) {
            itemViewType = VIEW_TYPE_PACKAGE_LIMIT_MODEL;
        } else if (item instanceof PackageLimitListItem) {
            PackageLimitListItem packageLimitListItem = (PackageLimitListItem) item;
            if (packageLimitListItem.getType() == PackageLimitListItem.TYPE_HEADER) {
                itemViewType = VIEW_TYPE_PACKAGE_LIMIT_HEADER;
            } else if (packageLimitListItem.getType() == PackageLimitListItem.TYPE_TAB) {
                itemViewType = VIEW_TYPE_PACKAGE_LIMIT_TAB;
            }
        } else if (item instanceof String) {
            itemViewType = VIEW_TYPE_PACKAGE_LIMIT_MAIN_HEADER;
        }

        return itemViewType;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Object getItem(int position) {
        if (list != null && list.size() > 0)
            return list.get(position);
        else
            return new Empty();
    }

    public void addAll(List<PackageLimitListItem> data) {
        packageLimitListItems.clear();
        packageLimitListItems.addAll(data);
    }

    public void notifyDataSetChanges() {
        list.clear();
        list.add(header);

        for (PackageLimitListItem packageLimitListItem : packageLimitListItems) {
            list.add(packageLimitListItem);

            if (packageLimitListItem.isExpanded()) {
                list.add(new PackageLimitListItem(packageLimitListItem.getList(), PackageLimitListItem.TYPE_TAB, packageLimitListItem.getSelectedTab(), packageLimitListItem));

                if (packageLimitListItem.getList().get(packageLimitListItem.getSelectedTab()) != null) {
                    for (String gymName : packageLimitListItem.getList().get(packageLimitListItem.getSelectedTab()).getGymNames().split(",")) {
                        list.add(new PackageLimitListItem.Gym(0, gymName, ""));
                    }
                }
            }
        }

        notifyDataSetChanged();
    }

    public void addHeader(String header) {
        this.header = header;
    }
}
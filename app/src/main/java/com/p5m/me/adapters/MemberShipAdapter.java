package com.p5m.me.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.LoaderViewHolder;
import com.p5m.me.adapters.viewholder.MemberShipHeaderViewHolder;
import com.p5m.me.adapters.viewholder.MemberShipViewHolder;
import com.p5m.me.data.HeaderSticky;
import com.p5m.me.data.ListLoader;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.UserPackage;

import java.util.ArrayList;
import java.util.List;

public class MemberShipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_MEMBERSHIP_HEADER = 1;
    private static final int VIEW_TYPE_MEMBERSHIP = 2;
    private static final int VIEW_TYPE_LOADER = 3;

    private final AdapterCallbacks adapterCallbacks;
    private final int dp;

    private List<Object> list;
    private Context context;

    private List<UserPackage> ownedPackages;
    private List<Package> offeredPackages;

    private HeaderSticky headerSticky1, headerSticky2;

    private final int navigatedFrom;

    private boolean showLoader;
    private ListLoader listLoader;
    private ClassModel classModel;

    public MemberShipAdapter(Context context, int navigatedFrom, boolean showLoader, AdapterCallbacks adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        ownedPackages = new ArrayList<>();
        offeredPackages = new ArrayList<>();

        this.navigatedFrom = navigatedFrom;
        this.showLoader = showLoader;
        listLoader = new ListLoader();

        headerSticky1 = new HeaderSticky("");
        headerSticky2 = new HeaderSticky("");

        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public void addAllOfferedPackages(List<Package> data) {
        offeredPackages.addAll(data);
    }

    public void addOfferedPackages(Package data) {
        offeredPackages.add(data);
    }

    public void addOwnedPackages(UserPackage data) {
        ownedPackages.add(data);
    }

    public void addAllOwnedPackages(List<UserPackage> data) {
        ownedPackages.addAll(data);
    }

    public List<UserPackage> getOwnedPackages() {
        return ownedPackages;
    }

    public List<Package> getOfferedPackages() {
        return offeredPackages;
    }

    public void add(Package model) {
        list.add(model);
        addLoader();
    }

    public void clearAll() {
        setHeaderText("", "");
        offeredPackages.clear();
        ownedPackages.clear();
        list.clear();
    }

    public void setHeaderText(String text1, String text2) {
        headerSticky1.setTitle(text1);
        headerSticky2.setTitle(text2);
    }

    public void notifyDataSetChanges() {
        list.clear();

        if (!headerSticky1.getTitle().isEmpty()) {
            list.add(headerSticky1);
        }

        if (!offeredPackages.isEmpty()) {
            list.addAll(offeredPackages);
        }

        list.add(headerSticky2);
        list.addAll(ownedPackages);

        notifyDataSetChanged();
    }

    public void addAll(List<Package> models) {
        list.addAll(models);
        addLoader();
    }

    private void addLoader() {
        if (showLoader) {
            list.remove(listLoader);
            list.add(listLoader);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = VIEW_TYPE_UNKNOWN;

        Object item = getItem(position);
        if (item instanceof UserPackage || item instanceof Package) {
            itemViewType = VIEW_TYPE_MEMBERSHIP;
        } else if (item instanceof HeaderSticky) {
            itemViewType = VIEW_TYPE_MEMBERSHIP_HEADER;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MEMBERSHIP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_membership, parent, false);
            return new MemberShipViewHolder(view, navigatedFrom);
        } else if (viewType == VIEW_TYPE_MEMBERSHIP_HEADER) {

            TextView textView = new TextView(context);
            textView.setPadding(dp * 16, dp * 20, dp * 16, dp * 20);
            textView.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6.0f, context.getResources().getDisplayMetrics()), 1.0f);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(ContextCompat.getColor(context, R.color.theme_dark_text));

            return new MemberShipHeaderViewHolder(textView, textView);
        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }

        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MemberShipViewHolder) {
            ((MemberShipViewHolder) holder).bind(classModel, getItem(position), adapterCallbacks, position);

        } else if (holder instanceof MemberShipHeaderViewHolder) {
            ((MemberShipHeaderViewHolder) holder).bind(getItem(position), adapterCallbacks, position);

        } else if (holder instanceof LoaderViewHolder) {
            ((LoaderViewHolder) holder).bind(listLoader, adapterCallbacks);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Object getItem(int position) {
        if (list != null && list.size() > 0)
            return list.get(position);
        else
            return null;
    }

    public void setClassModel(ClassModel classModel) {
        this.classModel = classModel;
    }
}
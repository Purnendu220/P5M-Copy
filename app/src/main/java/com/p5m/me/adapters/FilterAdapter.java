package com.p5m.me.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.FilterListItemViewHolder;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Empty;
import com.p5m.me.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;

    private final int dp;

    private Context context;
    private final AdapterCallbacks adapterCallbacks;

    private List<ClassesFilter> classesFilterList;
    private List<ClassesFilter> list;

    private List<ClassesFilter> classesFiltersSelected;

    public FilterAdapter(Context context, AdapterCallbacks adapterCallbacks) {
        this.context = context;
        list = new ArrayList<>();
        classesFiltersSelected = new ArrayList<>();
        this.adapterCallbacks = adapterCallbacks;
        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public List<ClassesFilter> getList() {
        return list;
    }

    public List<ClassesFilter> getClassesFilterList() {
        return classesFilterList;
    }

    public void setClassesFilterList(List<ClassesFilter> classesFilterList) {
        this.classesFilterList = classesFilterList;

        refreshList();
    }

    public void addSelection(List<ClassesFilter> filters) {
        classesFiltersSelected.addAll(filters);
    }

    public void addSelection(ClassesFilter filter) {
        classesFiltersSelected.add(filter);
    }

    public void removeSelection(ClassesFilter filter) {
        classesFiltersSelected.remove(filter);
    }
    public void removeAllSelection() {
        classesFiltersSelected.clear();
    }

    public List<ClassesFilter> getClassesFiltersSelected() {
        return classesFiltersSelected;
    }

    public void removeSelected(ClassesFilter classesFilter) {
        classesFiltersSelected.remove(classesFilter);
    }

    public void refreshList() {
        list.clear();
        setList(classesFilterList);
    }

    public boolean isFilterSelected(ClassesFilter filter) {
        if (classesFiltersSelected != null) {
            return classesFiltersSelected.contains(filter);
        }
        return false;
    }

    public void clearExpansionAndSelection() {
        list.clear();
        classesFiltersSelected.clear();
        clearSelected(classesFilterList);
        notifyDataSetChanged();
    }

    private void clearSelected(List<ClassesFilter> classesFilterList) {
        if (classesFilterList != null) {
            for (ClassesFilter classesFilter : classesFilterList) {
                if (classesFilter.getType() == ClassesFilter.TYPE_HEADER) {
                    list.add(classesFilter);
                    classesFilter.setExpanded(false);
                    clearSelected(classesFilter.getList());
                } else if (classesFilter.getType() == ClassesFilter.TYPE_SUB_HEADER) {
                    classesFilter.setExpanded(false);
                    clearSelected(classesFilter.getList());
                } else if (classesFilter.getType() == ClassesFilter.TYPE_ITEM) {
                    classesFilter.setSelected(false);
                }
            }
        }
    }

    private void setList(List<ClassesFilter> classesFilterList) {
        if (classesFilterList != null) {
            for (ClassesFilter classesFilter : classesFilterList) {
                if (classesFilter.getType() == ClassesFilter.TYPE_HEADER || classesFilter.getType() == ClassesFilter.TYPE_SUB_HEADER) {
                    list.add(classesFilter);
                    if (classesFilter.isExpanded()) {
                        setList(classesFilter.getList());
                    }
                } else if (classesFilter.getType() == ClassesFilter.TYPE_ITEM) {
                    classesFilter.setSelected(isFilterSelected(classesFilter));
                    list.add(classesFilter);
                }
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        TextView view = new TextView(context);
//        view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        view.setPadding(16 * dp, 12 * dp, 16 * dp, 12 * dp);
//        view.setTextColor(ContextCompat.getColor(context, R.color.theme_medium_text));
//        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        view.setCompoundDrawablePadding(DP * 16);
//        view.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
//
//        return new FilterListItemViewHolder(view);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_filter, parent, false);
        return new FilterListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        LogUtils.debug("FilterActivity onBindViewHolder " + position);

        if (holder instanceof FilterListItemViewHolder) {
            ((FilterListItemViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM;
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
}
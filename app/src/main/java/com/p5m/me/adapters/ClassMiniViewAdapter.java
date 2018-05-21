package com.p5m.me.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.ClassMiniDetailViewHolder;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.LoaderViewHolder;
import com.p5m.me.data.ListLoader;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ClassMiniViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_CLASS = 1;
    private static final int VIEW_TYPE_LOADER = 2;

    private final AdapterCallbacks<ClassModel> adapterCallbacks;

    private List<Object> list;
    private Context context;

    private int shownInScreen;

    private boolean showLoader;
    private ListLoader listLoader;

    public ClassMiniViewAdapter(Context context, int shownInScreen, boolean showLoader, AdapterCallbacks<ClassModel> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        this.shownInScreen = shownInScreen;
        this.showLoader = showLoader;

        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING ||
                shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
            listLoader = new ListLoader();
        } else {
            listLoader = new ListLoader();
        }
    }

    public List<Object> getList() {
        return list;
    }

    public void addClassTop(ClassModel model) {
        list.add(0, model);
        addLoader();
    }

    public void remove(int position) {
        list.remove(position);
    }

    public void addClass(ClassModel model) {
        list.add(model);
        addLoader();
    }

    public void addAllClass(List<ClassModel> models) {
        list.addAll(models);
        addLoader();
    }

    public void clearAll() {
        list.clear();
    }

    public void loaderDone() {
        listLoader.setFinish(true);
        try {
            notifyItemChanged(list.indexOf(listLoader));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public void loaderReset() {
        listLoader.setFinish(false);
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
        if (item instanceof ClassModel) {
            itemViewType = VIEW_TYPE_CLASS;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CLASS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_class_mini, parent, false);
            return new ClassMiniDetailViewHolder(view, shownInScreen);

        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }

        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ClassMiniDetailViewHolder) {
            ((ClassMiniDetailViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        } else if (holder instanceof LoaderViewHolder) {
            ((LoaderViewHolder) holder).bind(listLoader, adapterCallbacks);
            if (position == getItemCount() - 1 && !listLoader.isFinish()) {
                adapterCallbacks.onShowLastItem();
            }
        } else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).bind();
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

    public boolean isEmpty() {
        if (list.isEmpty() || (list.size() == 1 && list.contains(listLoader))) {
            return true;
        }
        return false;
    }
}
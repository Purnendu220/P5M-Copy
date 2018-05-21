package com.p5m.me.view.activity.custom;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.LoaderViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varun John on 3/15/2018.
 */

public class MyRecyclerView<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> list = new ArrayList<>(1);

    public static int VIEW_TYPE_LOADER = 1114;
    public static int VIEW_TYPE_UNKNOWN = -1;

    private LoaderItem loaderItem;
    private Loader loader = new Loader();
    private boolean showLoader, showLoaderText;
    private String loaderText = "";
    private LoaderCallbacks loaderCallbacks;

    public interface LoaderItem {
    }

    public static class Loader {
        public String enableStatusText = "";
        public String disableStatusText = "";
        public boolean enableLoader = false;
        public boolean enableText = false;

        public Loader(String enableStatusText, String disableStatusText, boolean enableLoader, boolean enableText) {
            this.enableStatusText = enableStatusText;
            this.disableStatusText = disableStatusText;
            this.enableLoader = enableLoader;
            this.enableText = enableText;
        }

        public Loader() {
        }
    }

    public interface LoaderCallbacks<T> {
        void onShowLoader(LoaderItem loaderItem, Loader loader, View view, int position);
    }

    public void setData(List<T> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        } else
            return new RecyclerView.ViewHolder(new LinearLayout(parent.getContext())) {
            };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LoaderViewHolder) {
            if (showLoader && loader.enableLoader && loaderCallbacks != null) {
                loaderCallbacks.onShowLoader(loaderItem, loader, holder.itemView, position);
            }
//            ((LoaderViewHolder) holder).bind(listL, holder.itemView, holder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLoader(boolean showLoader, boolean enableText,
                          String enableStatusText, String disStatusText,
                          LoaderItem loaderItem, LoaderCallbacks loaderCallbacks) {
        this.showLoader = showLoader;
        loader.enableStatusText = enableStatusText;
        loader.disableStatusText = disStatusText;
        this.loaderCallbacks = loaderCallbacks;
        this.loaderItem = loaderItem;

        enableLoader();
    }

    public void hideLoader() {
        list.remove(loaderItem);
    }

    public void enableLoader() {
        loader.enableLoader = true;
        if (!list.contains(loaderItem)) {
            list.add((T) loaderItem);
        }
    }

    public void disableLoader() {
        loader.enableLoader = false;
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        T item = list.get(position);
        if (item instanceof LoaderItem) {
            return VIEW_TYPE_LOADER;
        } else {
            return VIEW_TYPE_UNKNOWN;
        }
    }
}

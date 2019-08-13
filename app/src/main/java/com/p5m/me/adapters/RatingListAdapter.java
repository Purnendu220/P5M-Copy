package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.LoaderViewHolder;
import com.p5m.me.adapters.viewholder.RatingViewHolder;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.ListLoader;
import com.p5m.me.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;



public class RatingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_CLASS = 1;
    private static final int VIEW_TYPE_LOADER = 2;

    private final AdapterCallbacks<ClassRatingListData> adapterCallbacks;

    private List<Object> list;
    private Context context;

    private int shownInScreen;

    private boolean showLoader;
    private ListLoader listLoader;

    public RatingListAdapter(Context context, int shownInScreen, boolean showLoader, AdapterCallbacks<ClassRatingListData> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        this.shownInScreen = shownInScreen;
        this.showLoader = showLoader;

        listLoader = new ListLoader(true, context.getString(R.string.no_more_ratings));
    }

    public List<Object> getList() {
        return list;
    }

    public void addClass(ClassRatingListData model) {
        list.add(model);
        addLoader();
    }

    public void addAllClass(List<ClassRatingListData> models) {
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
        if (item instanceof ClassRatingListData) {
            itemViewType = VIEW_TYPE_CLASS;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CLASS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_user_rating, parent, false);
            return new RatingViewHolder(view, shownInScreen);
            } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }
        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RatingViewHolder) {
            ((RatingViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
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
}

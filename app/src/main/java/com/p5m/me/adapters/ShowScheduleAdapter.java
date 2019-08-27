package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.LoaderViewHolder;
import com.p5m.me.adapters.viewholder.ShowScheduleViewHolder;
import com.p5m.me.data.ListLoader;
import com.p5m.me.data.RecomendedClassData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.ScheduleClassModel;
import com.p5m.me.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


public class ShowScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_CLASS = 1;
    private static final int VIEW_TYPE_LOADER = 2;


    private List<Object> list;
    private Context context;
    private final AdapterCallbacks<Object> adapterCallbacks;

    private int shownInScreen;
    private ListLoader listLoader;
    private boolean showLoader;



    public ShowScheduleAdapter(Context context, int shownInScreen,boolean showLoader, AdapterCallbacks<Object> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        this.shownInScreen = shownInScreen;
        this.showLoader = showLoader;
        listLoader = new ListLoader(true, context.getString(R.string.no_more_classes));

    }

    public List<Object> getList() {
        return list;
    }

    public void addClass(ScheduleClassModel model) {
        list.add(model);
        addLoader();
    }

    public void addAllClass(List<ScheduleClassModel> models) {
        list.addAll(models);
        addLoader();
        notifyDataSetChanged();
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
        if (item instanceof ScheduleClassModel) {
            itemViewType = VIEW_TYPE_CLASS;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CLASS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_map_schedule, parent, false);
            return new ShowScheduleViewHolder(view, shownInScreen);

        }  else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }
        return new EmptyViewHolder(new LinearLayout(context));


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ShowScheduleViewHolder) {
            ((ShowScheduleViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        }
        else if (holder instanceof LoaderViewHolder) {
            ((LoaderViewHolder) holder).bind(listLoader, adapterCallbacks);
            if (position == getItemCount() - 1 && !listLoader.isFinish()) {
                adapterCallbacks.onShowLastItem();
            }
        }
        else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).bind();
        }
    }

    private void setFadeAnimation(View view) {
        Animation anim = new ScaleAnimation(0f, 1f,
                0f, 1f,
                Animation.ABSOLUTE, 1,
                Animation.ABSOLUTE, 0);
        anim.setDuration(750);

        view.startAnimation(anim);
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

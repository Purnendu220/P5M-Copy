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
import com.p5m.me.adapters.viewholder.WorkoutViewHolder;
import com.p5m.me.data.ListLoader;
import com.p5m.me.data.WorkoutModel;
import com.p5m.me.utils.LogUtils;

import java.util.List;


public class ExplorePageWorkoutListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_LIST = 1;
    private static final int VIEW_TYPE_LOADER = 2;
    private static final int VIEW_TYPE_UNKNOWN = 3;


    private final AdapterCallbacks<Object> adapterCallbacks;

    private List<WorkoutModel> list;
    private Context context;

    private int shownInScreen;
    private boolean showLoader;
    private ListLoader listLoader;
    private boolean isShowAllWorkouts;


    public ExplorePageWorkoutListAdapter(Context context, int shownInScreen, List<WorkoutModel> list, Boolean isShowAllWorkouts, AdapterCallbacks<Object> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        this.list = list;
        this.shownInScreen = shownInScreen;
        this.showLoader = showLoader;
        listLoader = new ListLoader(true, context.getString(R.string.no_more_gyms));
    }

    public List<WorkoutModel> getList() {
        return list;
    }

    public void addClass(WorkoutModel model) {
        list.add(model);
    }

    public void addAllClass(List<WorkoutModel> models) {
        list.addAll(models);
        notifyDataSetChanged();
    }

    public void clearAll() {
        list.clear();
    }

    private void addLoader() {
        if (showLoader) {
            list.remove(listLoader);
        }
    }

    public void showAllWorkout(Boolean b) {
       this.isShowAllWorkouts = b;
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


    @Override
    public int getItemViewType(int position) {
        int itemViewType = VIEW_TYPE_UNKNOWN;

        Object item = getItem(position);
        if (item instanceof WorkoutModel) {
            itemViewType = VIEW_TYPE_LIST;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_workout_explore, parent, false);
            return new WorkoutViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }

        return new EmptyViewHolder(new LinearLayout(context));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof WorkoutViewHolder) {
            ((WorkoutViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        }
    }

    @Override
    public int getItemCount() {
        if (list.size() > 4 && !isShowAllWorkouts)
            return 4;
        else
            return list.size();
    }

    public Object getItem(int position) {
        if (list != null && list.size() > 0) {
            return list.get(position);

        } else
            return null;
    }
}

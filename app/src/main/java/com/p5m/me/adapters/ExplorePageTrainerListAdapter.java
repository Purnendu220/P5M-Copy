package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.GymViewHolder;
import com.p5m.me.adapters.viewholder.LoaderViewHolder;
import com.p5m.me.adapters.viewholder.TrainerListViewHolder;
import com.p5m.me.adapters.viewholder.TrainerViewHolder;
import com.p5m.me.data.ExploreTrainerModel;
import com.p5m.me.data.ListLoader;
import com.p5m.me.utils.LogUtils;

import java.util.List;

import static com.p5m.me.view.activity.custom.MyRecyclerView.VIEW_TYPE_LOADER;
import static com.p5m.me.view.activity.custom.MyRecyclerView.VIEW_TYPE_UNKNOWN;


public class ExplorePageTrainerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_TYPE_LIST = 1;
    private final AdapterCallbacks<Object> adapterCallbacks;

    private List<ExploreTrainerModel> list;
    private Context context;

    private int shownInScreen;
    private boolean showLoader;
    private ListLoader listLoader;


    public ExplorePageTrainerListAdapter(Context context, int shownInScreen, List<ExploreTrainerModel> list, AdapterCallbacks<Object> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        this.list = list;
        this.shownInScreen = shownInScreen;
        listLoader = new ListLoader(true, context.getString(R.string.no_more_trainers));

    }
    public List<ExploreTrainerModel> getList() {
        return list;
    }

    public void addClass(ExploreTrainerModel model) {
        list.add(model);
    }

    public void addAllClass(List<ExploreTrainerModel> models) {
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
        if (item instanceof ExploreTrainerModel) {
            itemViewType = VIEW_TYPE_LIST;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gym_trainer_explore, parent, false);
            return new TrainerViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }

        return new EmptyViewHolder(new LinearLayout(context));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TrainerViewHolder) {
            ((TrainerViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        }
    }

    @Override
    public int getItemCount() {
        if(list.size()>10)
            return 10;
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

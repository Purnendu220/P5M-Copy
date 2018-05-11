package www.gymhop.p5m.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.viewholder.EmptyViewHolder;
import www.gymhop.p5m.adapters.viewholder.GymListViewHolder;
import www.gymhop.p5m.adapters.viewholder.LoaderViewHolder;
import www.gymhop.p5m.data.ListLoader;
import www.gymhop.p5m.data.main.GymDetailModel;
import www.gymhop.p5m.helper.GymListListenerHelper;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;

public class GymListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_GYM_LIST = 1;
    private static final int VIEW_TYPE_LOADER = 2;
    private static final int VIEW_TYPE_UNKNOWN = 3;

    private final AdapterCallbacks adapterCallbacks;
    private final GymListListenerHelper gymListListenerHelper;

    private List<Object> list;
    private Context context;

    private final int shownInScreen;

    private boolean showLoader;
    private ListLoader listLoader;

    public GymListAdapter(Context context, Activity activity, int shownInScreen, boolean showLoader, AdapterCallbacks adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        this.shownInScreen = shownInScreen;
        this.showLoader = showLoader;
        listLoader = new ListLoader(true, "No more gyms");
        gymListListenerHelper = new GymListListenerHelper(context, activity, AppConstants.AppNavigation.SHOWN_IN_SEARCH, adapterCallbacks);
    }

    public List<Object> getList() {
        return list;
    }

    public void remove(int index) {
        list.remove(index);
    }

    public void add(GymDetailModel model) {
        list.add(model);
        addLoader();
    }

    public void addAll(List<GymDetailModel> models) {
        list.addAll(models);
        addLoader();
    }

    private void addLoader() {
        if (showLoader) {
            list.remove(listLoader);
            list.add(listLoader);
        }
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

    @Override
    public int getItemViewType(int position) {
        int itemViewType = VIEW_TYPE_UNKNOWN;

        Object item = getItem(position);
        if (item instanceof GymDetailModel) {
            itemViewType = VIEW_TYPE_GYM_LIST;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_GYM_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gym_list, parent, false);
            return new GymListViewHolder(view, shownInScreen);
        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }

        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof GymListViewHolder) {
            ((GymListViewHolder) holder).bind(getItem(position), gymListListenerHelper, position);
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
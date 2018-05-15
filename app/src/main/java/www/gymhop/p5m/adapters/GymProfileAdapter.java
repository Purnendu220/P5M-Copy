package www.gymhop.p5m.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.viewholder.ClassMiniDetailViewHolder;
import www.gymhop.p5m.adapters.viewholder.EmptyViewHolder;
import www.gymhop.p5m.adapters.viewholder.GymProfileViewHolder;
import www.gymhop.p5m.adapters.viewholder.HeaderViewHolder;
import www.gymhop.p5m.adapters.viewholder.LoaderViewHolder;
import www.gymhop.p5m.data.HeaderSticky;
import www.gymhop.p5m.data.ListLoader;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.GymDetailModel;
import www.gymhop.p5m.utils.LogUtils;

public class GymProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderHandler {

    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_GYM_PROFILE = 1;
    private static final int VIEW_TYPE_UPCOMING_CLASSES = 2;
    private static final int VIEW_TYPE_UPCOMING_CLASSES_HEADER = 3;
    private static final int VIEW_TYPE_LOADER = 4;

    private Context context;
    private int shownIn;
    private final AdapterCallbacks adapterCallbacksTrainerProfile;
    private final AdapterCallbacks adapterCallbacksClasses;
    private List<Object> list;

    private GymDetailModel gymDetailModel;
    private List<ClassModel> classModels;
    private HeaderSticky headerSticky;

    private boolean showLoader;
    private ListLoader listLoader;

    public GymProfileAdapter(Context context, int shownIn, boolean showLoader, AdapterCallbacks adapterCallbacksTrainerProfile, AdapterCallbacks adapterCallbacksClasses) {
        this.adapterCallbacksTrainerProfile = adapterCallbacksTrainerProfile;
        this.adapterCallbacksClasses = adapterCallbacksClasses;

        this.context = context;
        this.shownIn = shownIn;
        list = new ArrayList<>();
        classModels = new ArrayList<>();
        headerSticky = new HeaderSticky(context.getString(R.string.upcoming_classes));
        this.showLoader = showLoader;

        listLoader = new ListLoader(true, "No more upcoming classes");
    }

    public GymDetailModel getGymDetailModel() {
        return gymDetailModel;
    }

    public void setGymDetailModel(GymDetailModel gymDetailModel) {
        this.gymDetailModel = gymDetailModel;

        try {
            if (list.isEmpty()) {
                notifyDataSetChanges();
            } else {
                list.set(0, gymDetailModel);
                notifyItemChanged(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAllClass(List<ClassModel> classModels) {
        this.classModels.addAll(classModels);
    }

    public void clearAllClasses() {
        classModels.clear();
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

    public List<Object> getList() {
        return list;
    }

    public void notifyDataSetChanges() {

        list.clear();

        list.add(gymDetailModel);

        if (!classModels.isEmpty()) {
            list.add(headerSticky);
            list.addAll(classModels);
            addLoader();
        } else {
            list.add(headerSticky);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        int itemViewType = VIEW_TYPE_UNKNOWN;

        Object item = getItem(position);
        if (getItem(position) instanceof ClassModel) {
            itemViewType = VIEW_TYPE_UPCOMING_CLASSES;
        } else if (getItem(position) instanceof GymDetailModel) {
            itemViewType = VIEW_TYPE_GYM_PROFILE;
        } else if (getItem(position) instanceof HeaderSticky) {
            itemViewType = VIEW_TYPE_UPCOMING_CLASSES_HEADER;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_GYM_PROFILE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gym_profile, parent, false);
            return new GymProfileViewHolder(view);
        } else if (viewType == VIEW_TYPE_UPCOMING_CLASSES) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_class_mini, parent, false);
            return new ClassMiniDetailViewHolder(view, shownIn);
        } else if (viewType == VIEW_TYPE_UPCOMING_CLASSES_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }
        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof GymProfileViewHolder) {
            ((GymProfileViewHolder) holder).bind(list.get(position), adapterCallbacksTrainerProfile, position);
        } else if (holder instanceof ClassMiniDetailViewHolder) {
            ((ClassMiniDetailViewHolder) holder).bind(list.get(position), adapterCallbacksClasses, position);
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind(list.get(position), classModels.isEmpty());
        } else if (holder instanceof LoaderViewHolder) {
            ((LoaderViewHolder) holder).bind(listLoader, adapterCallbacksClasses);
            if (position == getItemCount() - 1 && !listLoader.isFinish()) {
                adapterCallbacksClasses.onShowLastItem();
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

    @Override
    public List<?> getAdapterData() {
        return list;
    }

}
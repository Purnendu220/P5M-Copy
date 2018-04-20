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
import www.gymhop.p5m.adapters.viewholder.HeaderViewHolder;
import www.gymhop.p5m.adapters.viewholder.LoaderViewHolder;
import www.gymhop.p5m.adapters.viewholder.TrainerProfileViewHolder;
import www.gymhop.p5m.data.HeaderSticky;
import www.gymhop.p5m.data.ListLoader;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.TrainerDetailModel;

public class TrainerProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderHandler {

    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_TRAINER_INFO = 1;
    private static final int VIEW_TYPE_UPCOMING_CLASSES = 2;
    private static final int VIEW_TYPE_UPCOMING_CLASSES_HEADER = 3;
    private static final int VIEW_TYPE_LOADER = 4;

    private Context context;
    private int shownIn;
    private final AdapterCallbacks adapterCallbacksTrainerProfile;
    private final AdapterCallbacks adapterCallbacksClasses;
    private List<Object> list;

    private TrainerDetailModel trainerDetailModel;
    private List<ClassModel> classModels;
    private HeaderSticky headerSticky;

    public TrainerProfileAdapter(Context context, int shownIn, AdapterCallbacks adapterCallbacksTrainerProfile, AdapterCallbacks adapterCallbacksClasses) {
        this.adapterCallbacksTrainerProfile = adapterCallbacksTrainerProfile;
        this.adapterCallbacksClasses = adapterCallbacksClasses;

        this.context = context;
        this.shownIn = shownIn;
        list = new ArrayList<>();
        classModels = new ArrayList<>();
        headerSticky = new HeaderSticky(context.getString(R.string.upcoming_classes));
    }

    public TrainerDetailModel getTrainerModel() {
        return trainerDetailModel;
    }

    public void setTrainerModel(TrainerDetailModel trainerDetailModel) {
        this.trainerDetailModel = trainerDetailModel;
    }

    public void notifyDataSetChanges() {

        list.clear();

        list.add(trainerDetailModel);
        list.add(headerSticky);
        list.addAll(classModels);

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        int itemViewType = VIEW_TYPE_UNKNOWN;

        Object item = getItem(position);
        if (getItem(position) instanceof ClassModel) {
            itemViewType = VIEW_TYPE_UPCOMING_CLASSES;
        } else if (getItem(position) instanceof TrainerDetailModel) {
            itemViewType = VIEW_TYPE_TRAINER_INFO;
        } else if (getItem(position) instanceof HeaderSticky) {
            itemViewType = VIEW_TYPE_UPCOMING_CLASSES_HEADER;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TRAINER_INFO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_trainer_profile, parent, false);
            return new TrainerProfileViewHolder(view);
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
        if (holder instanceof TrainerProfileViewHolder) {
            ((TrainerProfileViewHolder) holder).bind(list.get(position), adapterCallbacksTrainerProfile, position);
        } else if (holder instanceof ClassMiniDetailViewHolder) {
            ((ClassMiniDetailViewHolder) holder).bind(list.get(position), adapterCallbacksClasses, position);
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind(list.get(position), adapterCallbacksTrainerProfile, position);
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

    public void addAllClass(List<ClassModel> classModels) {
        this.classModels = classModels;
    }
}
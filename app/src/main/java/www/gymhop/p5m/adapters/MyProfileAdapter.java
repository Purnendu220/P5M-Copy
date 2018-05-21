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
import www.gymhop.p5m.adapters.viewholder.ProfileHeaderTabViewHolder;
import www.gymhop.p5m.adapters.viewholder.ProfileHeaderViewHolder;
import www.gymhop.p5m.adapters.viewholder.TrainerListViewHolder;
import www.gymhop.p5m.data.HeaderSticky;
import www.gymhop.p5m.data.main.User;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.utils.AppConstants;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class MyProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderHandler {

    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_PROFILE_INFO = 0;
    private static final int VIEW_TYPE_CLASSES = 1;
    private static final int VIEW_TYPE_TRAINERS = 2;
    private static final int VIEW_TYPE_TAB_HEADER = 3;

    private Context context;
    private final AdapterCallbacks<Object> adapterCallbacksTrainers;
    private final AdapterCallbacks<Object> adapterCallbacksClasses;
    private final AdapterCallbacks<Object> adapterCallbacksProfile;
    private List<Object> list;

    private List<TrainerModel> trainers;
    private List<ClassModel> classes;
    private User user;
    private HeaderSticky headerSticky;

    private int selectedTab = ProfileHeaderTabViewHolder.TAB_1;

    private ProfileHeaderTabViewHolder profileHeaderTabViewHolder;

    public MyProfileAdapter(Context context, AdapterCallbacks<Object> adapterCallbacksTrainers, AdapterCallbacks<Object> adapterCallbacksClasses, AdapterCallbacks<Object> adapterCallbacksProfile) {
        this.context = context;
        this.adapterCallbacksTrainers = adapterCallbacksTrainers;
        this.adapterCallbacksClasses = adapterCallbacksClasses;
        this.adapterCallbacksProfile = adapterCallbacksProfile;

        list = new ArrayList<>();
        trainers = new ArrayList<>();
        classes = new ArrayList<>();
        headerSticky = new HeaderSticky("");
    }

    public void notifyDataSetChanges() {
        list.clear();
        list.add(user);
        list.add(headerSticky);
        if (selectedTab == ProfileHeaderTabViewHolder.TAB_1) {
            list.addAll(trainers);
        } else if (selectedTab == ProfileHeaderTabViewHolder.TAB_2) {
            list.addAll(classes);
        }
        notifyDataSetChanged();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        list.remove(this.user);
        this.user = user;
        list.add(0, user);
    }

    public List<Object> getList() {
        return list;
    }

    public List<TrainerModel> getTrainers() {
        return trainers;
    }

    public void remove(int position) {
        list.remove(position);
    }

    public void removeTrainer(int position) {
        trainers.remove(position);
    }

    public void addTrainers(List<TrainerModel> trainers) {
        this.trainers.addAll(trainers);
    }

    public void addTrainers(TrainerModel trainer) {
        this.trainers.add(trainer);
    }

    public List<ClassModel> getClasses() {
        return classes;
    }

    public void addClasses(List<ClassModel> classes) {
        this.classes.addAll(classes);
    }

    public void clearTrainers(){
        trainers.clear();
    }

    public void clearClasses(){
        classes.clear();
    }

    public void setData(List<Object> data) {
        list.clear();
        list.addAll(data);
    }

    public int getSelectedTab() {
        return selectedTab;
    }

    public void onTabSelection(int id) {
        selectedTab = id;
    }

    public ProfileHeaderTabViewHolder getProfileHeaderTabViewHolder() {
        return profileHeaderTabViewHolder;
    }

    @Override
    public int getItemViewType(int position) {

        int itemViewType = VIEW_TYPE_UNKNOWN;

        if (getItem(position) instanceof ClassModel) {
            itemViewType = VIEW_TYPE_CLASSES;
        } else if (getItem(position) instanceof TrainerModel) {
            itemViewType = VIEW_TYPE_TRAINERS;
        } else if (getItem(position) instanceof User) {
            itemViewType = VIEW_TYPE_PROFILE_INFO;
        } else if (getItem(position) instanceof HeaderSticky) {
            itemViewType = VIEW_TYPE_TAB_HEADER;
        }
        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PROFILE_INFO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_profile, parent, false);
            return new ProfileHeaderViewHolder(view);
        } else if (viewType == VIEW_TYPE_TAB_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pofile_header_tab, parent, false);
            return new ProfileHeaderTabViewHolder(view);
        } else if (viewType == VIEW_TYPE_CLASSES) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_class_mini, parent, false);
            return new ClassMiniDetailViewHolder(view, AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FINISHED);
        } else if (viewType == VIEW_TYPE_TRAINERS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_trainer_list, parent, false);
            return new TrainerListViewHolder(view, AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FAV_TRAINERS);
        }
        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ProfileHeaderViewHolder) {
            ((ProfileHeaderViewHolder) holder).bind(list.get(position), adapterCallbacksProfile, position);
        } else if (holder instanceof ProfileHeaderTabViewHolder) {
            profileHeaderTabViewHolder = (ProfileHeaderTabViewHolder) holder;
            ((ProfileHeaderTabViewHolder) holder).bind(selectedTab, list.get(position), adapterCallbacksProfile, position);
        } else if (holder instanceof ClassMiniDetailViewHolder) {
            ((ClassMiniDetailViewHolder) holder).bind(list.get(position), adapterCallbacksClasses, position);
        } else if (holder instanceof TrainerListViewHolder) {
            ((TrainerListViewHolder) holder).bind(list.get(position), adapterCallbacksTrainers, position);
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
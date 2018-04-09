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
import www.gymhop.p5m.data.Class;
import www.gymhop.p5m.data.TrainerDetail;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class MyProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderHandler {
    private static final int PROFILE_INFO = 0;
    private static final int CLASSES = 1;
    private static final int TRAINERS = 2;
    private static final int TAB_HEADER = 3;

    private final AdapterCallbacks<Object> adapterCallbacks;
    private List<Object> list;
    private Context context;

    private int selectedTab = ProfileHeaderTabViewHolder.TAB_1;
    private ProfileHeaderTabViewHolder profileHeaderTabViewHolder;

    public MyProfileAdapter(Context context, AdapterCallbacks<Object> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
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

        int itemViewType = -1;

        if (position == 0) {
            itemViewType = PROFILE_INFO;
        } else if (position == 1) {
            itemViewType = TAB_HEADER;
        } else if (getItem(position) instanceof Class) {
            itemViewType = CLASSES;
        } else if (getItem(position) instanceof TrainerDetail) {
            itemViewType = TRAINERS;
        }
        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PROFILE_INFO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_profile, parent, false);
            return new ProfileHeaderViewHolder(view);
        } else if (viewType == TAB_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_pofile_header_tab, parent, false);
            return new ProfileHeaderTabViewHolder(view);
        } else if (viewType == CLASSES) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_class_mini, parent, false);
            return new ClassMiniDetailViewHolder(view);
        } else if (viewType == TRAINERS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_trainer_list, parent, false);
            return new TrainerListViewHolder(view);
        }
        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ProfileHeaderViewHolder) {
            ((ProfileHeaderViewHolder) holder).bind(list.get(position), adapterCallbacks, position);
        } else if (holder instanceof ProfileHeaderTabViewHolder) {
            profileHeaderTabViewHolder = (ProfileHeaderTabViewHolder) holder;
            ((ProfileHeaderTabViewHolder) holder).bind(selectedTab, list.get(position), adapterCallbacks, position);
        } else if (holder instanceof ClassMiniDetailViewHolder) {
            ((ClassMiniDetailViewHolder) holder).bind(list.get(position), adapterCallbacks, position);
        } else if (holder instanceof TrainerListViewHolder) {
            ((TrainerListViewHolder) holder).bind(list.get(position), adapterCallbacks, position);
        } else {
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
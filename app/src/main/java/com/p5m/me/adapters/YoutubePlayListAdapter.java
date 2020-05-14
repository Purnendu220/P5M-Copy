package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.ExploreRatingViewHolder;
import com.p5m.me.adapters.viewholder.YouTubePlayListViewHolder;
import com.p5m.me.data.ExploreRatedClassModel;
import com.p5m.me.data.Item;
import com.p5m.me.data.ListLoader;

import java.util.List;

import static com.p5m.me.view.activity.custom.MyRecyclerView.VIEW_TYPE_LOADER;
import static com.p5m.me.view.activity.custom.MyRecyclerView.VIEW_TYPE_UNKNOWN;


public class YoutubePlayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int VIEW_TYPE_LIST = 1;
    private final AdapterCallbacks<Object> adapterCallbacks;

    private List<Item> list;
    private Context context;

    private int shownInScreen;
    private boolean showLoader;


    public YoutubePlayListAdapter(Context context, int shownInScreen, List<Item>  list, AdapterCallbacks<Object> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        this.list = list;
        this.shownInScreen = shownInScreen;

    }
    public List<Item> getList() {
        return list;
    }

    public void addClass(Item model) {
        list.add(model);
    }

    public void addAllClass(List<Item> models) {
        list.addAll(models);
        notifyDataSetChanged();
    }

    public void clearAll() {
        list.clear();
    }



    @Override
    public int getItemViewType(int position) {
        int itemViewType = VIEW_TYPE_UNKNOWN;

        Object item = getItem(position);
        if (item instanceof Item) {
            itemViewType = VIEW_TYPE_LIST;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_youtube_playlist, parent, false);
            return new YouTubePlayListViewHolder(view);
        }

        return new EmptyViewHolder(new LinearLayout(context));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof YouTubePlayListViewHolder) {
            ((YouTubePlayListViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Object getItem(int position) {
        if (list != null && list.size() > 0) {
            return list.get(position);

        } else
            return null;
    }
}

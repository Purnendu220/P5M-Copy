package www.gymhop.p5m.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.viewholder.ClassViewHolder;
import www.gymhop.p5m.data.Class;
import www.gymhop.p5m.view.activity.custom.MyRecyclerView;

public class ClassMiniViewAdapter extends MyRecyclerView<Class> {

    private static final int VIEW_TYPE_CLASS = 0;
    private final AdapterCallbacks<Class> adapterCallbacks;

    private List<Class> model;
    private boolean isLoadingAdded = false;
    private Context context;

    public ClassMiniViewAdapter(Context context, AdapterCallbacks<Class> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        model = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {

        int itemViewType = super.getItemViewType(position);

        if (itemViewType == MyRecyclerView.VIEW_TYPE_UNKNOWN) {
            itemViewType = VIEW_TYPE_CLASS;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CLASS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_class_mini, parent, false);
            return new ClassViewHolder(view);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ClassViewHolder) {
            ((ClassViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public Class getItem(int position) {
        if (model != null && model.size() > 0)
            return model.get(position);
        else
            return null;
    }

}
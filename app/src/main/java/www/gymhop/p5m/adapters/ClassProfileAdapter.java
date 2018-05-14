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
import www.gymhop.p5m.adapters.viewholder.ClassProfileViewHolder;
import www.gymhop.p5m.adapters.viewholder.EmptyViewHolder;
import www.gymhop.p5m.data.main.ClassModel;

public class ClassProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderHandler {
    private static final int CLASS_INFO = 0;
    private static final int UPCOMING_CLASSES = 1;
    private static final int UPCOMING_CLASSES_HEADER = 2;

    private int shownIn;
    private final AdapterCallbacks adapterCallbacks;
    private List<Object> list;
    private Context context;

    private ClassModel classModel;

    public ClassProfileAdapter(Context context, int shownIn, AdapterCallbacks adapterCallbacks) {
        this.context = context;
        this.adapterCallbacks = adapterCallbacks;
        this.shownIn = shownIn;
        list = new ArrayList<>();
    }

    public void setData(List<Object> data) {
        list.clear();
        list.addAll(data);
    }

    public ClassModel getClassModel() {
        return classModel;
    }

    @Override
    public int getItemViewType(int position) {
        return CLASS_INFO;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == CLASS_INFO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_class_profile, parent, false);
            return new ClassProfileViewHolder(view);
        }
        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ClassProfileViewHolder) {
            ((ClassProfileViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        } else if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
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

    public void setClass(ClassModel classModel) {
        this.classModel = classModel;
        list.add(classModel);
    }
}
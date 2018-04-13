package www.gymhop.p5m.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.viewholder.ClassMiniDetailViewHolder;
import www.gymhop.p5m.adapters.viewholder.HeaderViewHolder;
import www.gymhop.p5m.adapters.viewholder.TrainerProfileViewHolder;

public class TrainerProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderHandler {
    private static final int TRAINER_INFO = 0;
    private static final int UPCOMING_CLASSES = 1;
    private static final int UPCOMING_CLASSES_HEADER = 2;

    private int shownIn;
    private final AdapterCallbacks adapterCallbacks;
    private List<Object> list;
    private Context context;

    public TrainerProfileAdapter(Context context, int shownIn, AdapterCallbacks adapterCallbacks) {
        this.context = context;
        this.adapterCallbacks = adapterCallbacks;
        this.shownIn = shownIn;
        list = new ArrayList<>();
    }

    public void setData(List<Object> data) {
        list.clear();
        list.addAll(data);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TRAINER_INFO;
        }
        if (position == 1) {
            return UPCOMING_CLASSES_HEADER;
        } else
            return UPCOMING_CLASSES;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TRAINER_INFO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_trainer_profile, parent, false);
            return new TrainerProfileViewHolder(view);
        }
        if (viewType == UPCOMING_CLASSES) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_class_mini, parent, false);
            return new ClassMiniDetailViewHolder(view, shownIn);
        }
        if (viewType == UPCOMING_CLASSES_HEADER) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false);
            return new HeaderViewHolder(view);

//            LinearLayout linearLayout = new LinearLayout(context);
//            linearLayout.setOrientation(LinearLayout.VERTICAL);
//            linearLayout.setBackgroundColor(Color.WHITE);
//            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
//
//            LinearLayout linearLayoutSeparator = new LinearLayout(context);
//            linearLayoutSeparator.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp));
//            linearLayoutSeparator.setBackgroundColor(ContextCompat.getColor(context, R.color.separator));
//
//            TextView view = new TextView(context);
//            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            view.setPadding(16 * dp, 12 * dp, 16 * dp, 12 * dp);
//            view.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));
//            view.setText(context.getString(R.string.upcoming_classes));
//            view.setTextSize(context.getResources().getDimension(R.dimen.text_list_header));
//            view.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
//
//            linearLayout.addView(view);
//            linearLayout.addView(linearLayoutSeparator);

//            return new HeaderViewHolder(linearLayout);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TrainerProfileViewHolder) {
            ((TrainerProfileViewHolder) holder).bind();
        } else if (holder instanceof ClassMiniDetailViewHolder) {
            ((ClassMiniDetailViewHolder) holder).bind(list.get(position), adapterCallbacks, position);
        } else {
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
}
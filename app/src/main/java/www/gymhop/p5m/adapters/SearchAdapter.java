package www.gymhop.p5m.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.viewholder.ClassMiniDetailViewHolder;
import www.gymhop.p5m.adapters.viewholder.EmptyViewHolder;
import www.gymhop.p5m.adapters.viewholder.GymListViewHolder;
import www.gymhop.p5m.adapters.viewholder.HeaderViewHolder;
import www.gymhop.p5m.adapters.viewholder.LoaderViewHolder;
import www.gymhop.p5m.adapters.viewholder.SearchViewMoreViewHolder;
import www.gymhop.p5m.adapters.viewholder.TrainerListViewHolder;
import www.gymhop.p5m.data.HeaderSticky;
import www.gymhop.p5m.data.ListLoader;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.GymDetailModel;
import www.gymhop.p5m.data.main.SearchResults;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.helper.ClassListListenerHelper;
import www.gymhop.p5m.helper.GymListListenerHelper;
import www.gymhop.p5m.helper.TrainerListListenerHelper;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaderHandler {

    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_LOADER = 1;
    private static final int VIEW_TYPE_CLASS = 2;
    private static final int VIEW_TYPE_TRAINER = 3;
    private static final int VIEW_TYPE_GYM = 4;
    private static final int VIEW_TYPE_HEADER = 5;
    private static final int VIEW_TYPE_BOTTOM_VIEW_ALL = 6;

    private final AdapterCallbacks adapterCallbacks;
    private final TrainerListListenerHelper trainerListListenerHelper;
    private final ClassListListenerHelper classListListenerHelper;
    private final GymListListenerHelper gymListListenerHelper;

    private final int dp;

    private List<Object> list;
    private Context context;

    private int navigationFrom;

    private boolean showLoader;
    private ListLoader listLoader;
    private SearchResults searchResult;

    public SearchAdapter(Context context, Activity activity, int navigationFrom, boolean showLoader, AdapterCallbacks adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        this.navigationFrom = navigationFrom;
        this.showLoader = showLoader;
        trainerListListenerHelper = new TrainerListListenerHelper(context, activity, adapterCallbacks);
        classListListenerHelper = new ClassListListenerHelper(context, activity, AppConstants.AppNavigation.SHOWN_IN_SEARCH, adapterCallbacks);
        gymListListenerHelper = new GymListListenerHelper(context, activity, AppConstants.AppNavigation.SHOWN_IN_SEARCH, adapterCallbacks);

        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public SearchResults getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResults searchResult) {
        this.searchResult = searchResult;
    }

    public void notifyDataSetChanges() {
        list.clear();

        if (searchResult != null) {
            if (navigationFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS) {

                if (searchResult.getClassDetailList() != null && !searchResult.getClassDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.classes)));
                    list.addAll(searchResult.getClassDetailList());
                }

                if (searchResult.getTrainerDetailList() != null && !searchResult.getTrainerDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.trainers)));
                    list.addAll(searchResult.getTrainerDetailList());
                }

                if (searchResult.getGymDetailList() != null && !searchResult.getGymDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.gyms)));
                    list.addAll(searchResult.getGymDetailList());
                }
            } else if (navigationFrom == AppConstants.AppNavigation.NAVIGATION_FROM_TRAINERS) {

                if (searchResult.getTrainerDetailList() != null && !searchResult.getTrainerDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.trainers)));
                    list.addAll(searchResult.getTrainerDetailList());
                }

                if (searchResult.getGymDetailList() != null && !searchResult.getGymDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.gyms)));
                    list.addAll(searchResult.getGymDetailList());
                }

                if (searchResult.getClassDetailList() != null && !searchResult.getClassDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.classes)));
                    list.addAll(searchResult.getClassDetailList());
                }

            } else if (navigationFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SCHEDULE) {

                if (searchResult.getGymDetailList() != null && !searchResult.getGymDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.gyms)));
                    list.addAll(searchResult.getGymDetailList());
                }

                if (searchResult.getClassDetailList() != null && !searchResult.getClassDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.classes)));
                    list.addAll(searchResult.getClassDetailList());
                }

                if (searchResult.getTrainerDetailList() != null && !searchResult.getTrainerDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.trainers)));
                    list.addAll(searchResult.getTrainerDetailList());
                }

            } else {

                if (searchResult.getClassDetailList() != null && !searchResult.getClassDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.classes)));
                    list.addAll(searchResult.getClassDetailList());
                }

                if (searchResult.getTrainerDetailList() != null && !searchResult.getTrainerDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.trainers)));
                    list.addAll(searchResult.getTrainerDetailList());
                }

                if (searchResult.getGymDetailList() != null && !searchResult.getGymDetailList().isEmpty()) {
                    list.add(new HeaderSticky(context.getString(R.string.gyms)));
                    list.addAll(searchResult.getGymDetailList());
                }
            }
        }

        if (!list.isEmpty()) {
            list.add("View All Results");
        }

        notifyDataSetChanged();
    }

    public List<Object> getList() {
        return list;
    }

    public void remove(int position) {
        list.remove(position);
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

    private void addLoader() {
        if (showLoader) {
            list.remove(listLoader);
            list.add(listLoader);
        }
    }

    @Override
    public int getItemViewType(int position) {

        int itemViewType = VIEW_TYPE_UNKNOWN;

        Object item = getItem(position);
        if (item instanceof ClassModel) {
            itemViewType = VIEW_TYPE_CLASS;
        } else if (item instanceof TrainerModel) {
            itemViewType = VIEW_TYPE_TRAINER;
        } else if (item instanceof GymDetailModel) {
            itemViewType = VIEW_TYPE_GYM;
        } else if (item instanceof String) {
            itemViewType = VIEW_TYPE_BOTTOM_VIEW_ALL;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        } else if (getItem(position) instanceof HeaderSticky) {
            itemViewType = VIEW_TYPE_HEADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CLASS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_class_mini, parent, false);
            return new ClassMiniDetailViewHolder(view, AppConstants.AppNavigation.SHOWN_IN_SEARCH);

        } else if (viewType == VIEW_TYPE_TRAINER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_trainer_list, parent, false);
            return new TrainerListViewHolder(view, AppConstants.AppNavigation.SHOWN_IN_SEARCH);

        } else if (viewType == VIEW_TYPE_GYM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gym_list, parent, false);
            return new GymListViewHolder(view, AppConstants.AppNavigation.SHOWN_IN_SEARCH);

        } else if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header_search, parent, false);
            return new HeaderViewHolder(view);

        } else if (viewType == VIEW_TYPE_BOTTOM_VIEW_ALL) {

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView textView = new TextView(context);
            textView.setId(1);
            textView.setGravity(Gravity.RIGHT);
            textView.setPadding(dp * 16, dp * 16, dp * 16, dp * 16);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setBackgroundResource(R.drawable.click_highlight);
            textView.setClickable(true);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setTextColor(ContextCompat.getColor(context, R.color.theme_accent_text));

            linearLayout.addView(textView);

            return new SearchViewMoreViewHolder(linearLayout, textView);

        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }

        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ClassMiniDetailViewHolder) {
            ((ClassMiniDetailViewHolder) holder).bind(getItem(position), classListListenerHelper, position);
        } else if (holder instanceof TrainerListViewHolder) {
            ((TrainerListViewHolder) holder).bind(getItem(position), trainerListListenerHelper, position);
        } else if (holder instanceof GymListViewHolder) {
            ((GymListViewHolder) holder).bind(getItem(position), gymListListenerHelper, position);
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind(list.get(position), false);
        } else if (holder instanceof SearchViewMoreViewHolder) {
            ((SearchViewMoreViewHolder) holder).bind(list.get(position), adapterCallbacks, position);
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

    @Override
    public List<?> getAdapterData() {
        return list;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }
}
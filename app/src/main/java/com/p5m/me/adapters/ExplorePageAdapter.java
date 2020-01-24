package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.BannerViewHolder;
import com.p5m.me.adapters.viewholder.CustomerSpeakViewHolder;
import com.p5m.me.adapters.viewholder.EmptyViewHolder;
import com.p5m.me.adapters.viewholder.ExplorePageGymListViewHolder;
import com.p5m.me.adapters.viewholder.ExplorePagePriceModelViewHolder;
import com.p5m.me.adapters.viewholder.ExplorePageRatingListViewHolder;
import com.p5m.me.adapters.viewholder.ExplorePageTrainerListViewHolder;
import com.p5m.me.adapters.viewholder.ExplorePageWorkoutViewHolder;
import com.p5m.me.adapters.viewholder.StillConfusedViewHolder;
import com.p5m.me.adapters.viewholder.TryP5MViewHolder;
import com.p5m.me.data.BannerData;
import com.p5m.me.data.CustomerSpeakModel;
import com.p5m.me.data.ExploreDataList;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.data.ListLoader;
import com.p5m.me.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.p5m.me.utils.AppConstants.ExploreViewType.BANNER_CAROUSAL_VIEW;
import static com.p5m.me.utils.AppConstants.ExploreViewType.CATEGORY_CAROUSEL_VIEW;
import static com.p5m.me.utils.AppConstants.ExploreViewType.GYM_VIEW;
import static com.p5m.me.utils.AppConstants.ExploreViewType.PRICE_MODEL_CAROUSEL_VIEW;
import static com.p5m.me.utils.AppConstants.ExploreViewType.TEXT_CAROUSAL_VIEW;
import static com.p5m.me.utils.AppConstants.ExploreViewType.TEXT_WITH_BUTTONS;
import static com.p5m.me.utils.AppConstants.ExploreViewType.TEXT_WITH_BUTTONS_2;
import static com.p5m.me.utils.AppConstants.ExploreViewType.TOP_RATED_CLASSES;
import static com.p5m.me.utils.AppConstants.ExploreViewType.TRAINER_CAROUSAL_VIEW;


/**
 * Created by MyU10 on 3/10/2018.
 */

public class ExplorePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_LOADER = 0;
    private static int VIEW_TYPE = -1;

    private static final int BANNER_CAROUSAL_VIEW_VALUE = 1;
    private static final int TEXT_WITH_BUTTONS_VALUE = 2;
    private static final int TEXT_CAROUSAL_VIEW_VALUE = 3;
    private static final int PRICE_MODEL_CAROUSEL_VIEW_VALUE = 4;
    private static final int CATEGORY_CAROUSEL_VIEW_VALUE = 5;
    private static final int TRAINER_CAROUSAL_VIEW_VALUE = 6;
    private static final int GYM_CAROUSAL_VIEW_VALUE = 7;
    private static final int TOP_RATED_CLASSES_VALUE = 8;
    private static final int TEXT_WITH_BUTTONS_2_VALUE = 9;
    private final boolean showLoader;

    private Context context;
    private int shownInScreen;
    private ListLoader listLoader;
    private final AdapterCallbacks<Object> adapterCallbacksExplore;
    private List<Object> list;
    private List<BannerData> bannerDataList;
    private List<CustomerSpeakModel> testomialList;
    private Gson gson;
    private ExploreDataList exploreDataList;

    public ExplorePageAdapter(Context context, int shownInScreen, boolean showLoader, AdapterCallbacks<Object> adapterCallbacksExplore) {
        this.context = context;
        this.adapterCallbacksExplore = adapterCallbacksExplore;
        this.shownInScreen = shownInScreen;
        this.showLoader = showLoader;
        list = new ArrayList<>();
        gson = new Gson();
        listLoader = new ListLoader(true, context.getString(R.string.no_more_notification));

    }

    public void notifyDataSetChanges() {
        list.clear();
        notifyDataSetChanged();
    }

    public List<Object> getList() {
        return list;
    }

    public void remove(int index) {
        list.remove(index);
    }

    public void add(ExploreDataModel model) {
        list.add(model);
        addLoader();
    }


    public void addAll(List<ExploreDataModel> models) {
        list.clear();
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

        Object item = getItem(position);
        if (item instanceof ExploreDataModel) {
            String listString;
            exploreDataList = new ExploreDataList(((ExploreDataModel) item).getData());
            switch (((ExploreDataModel) item).getViewType()) {
                case BANNER_CAROUSAL_VIEW:
                    if (exploreDataList != null) {
                        listString = convertorToModelClassList();
                        bannerDataList = gson.fromJson(listString, new TypeToken<List<BannerData>>() {
                        }.getType());
                    }
                    VIEW_TYPE = BANNER_CAROUSAL_VIEW_VALUE;
                    break;
                case TEXT_WITH_BUTTONS:
                    VIEW_TYPE = TEXT_WITH_BUTTONS_VALUE;
                    break;
                case TEXT_CAROUSAL_VIEW:
                    VIEW_TYPE = TEXT_CAROUSAL_VIEW_VALUE;
                    listString = convertorToModelClassList();
                    testomialList = gson.fromJson(listString, new TypeToken<List<CustomerSpeakModel>>() {
                    }.getType());

                    break;
                case PRICE_MODEL_CAROUSEL_VIEW:
                    VIEW_TYPE = PRICE_MODEL_CAROUSEL_VIEW_VALUE;
                    break;
                case CATEGORY_CAROUSEL_VIEW:
                    VIEW_TYPE = CATEGORY_CAROUSEL_VIEW_VALUE;
                    break;
                case TRAINER_CAROUSAL_VIEW:
                    VIEW_TYPE = TRAINER_CAROUSAL_VIEW_VALUE;
                    break;
                case GYM_VIEW:
                    VIEW_TYPE = GYM_CAROUSAL_VIEW_VALUE;
                    break;
                case TOP_RATED_CLASSES:
                    VIEW_TYPE = TOP_RATED_CLASSES_VALUE;
                    break;
                case TEXT_WITH_BUTTONS_2:
                    VIEW_TYPE = TEXT_WITH_BUTTONS_2_VALUE;
                    break;

            }
        }
        return VIEW_TYPE;
    }

    private String convertorToModelClassList() {
        return gson.toJson(
                exploreDataList.getexploreDataList(),
                new TypeToken<List<LinkedTreeMap>>() {
                }.getType());


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == BANNER_CAROUSAL_VIEW_VALUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_banner, parent, false);
            return new BannerViewHolder(view);
        }
        if (viewType == TEXT_WITH_BUTTONS_VALUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_try_p5m, parent, false);
            return new TryP5MViewHolder(view);
        }
        if (viewType == TEXT_CAROUSAL_VIEW_VALUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_customer_speaks, parent, false);
            return new CustomerSpeakViewHolder(view);
        }
        if (viewType == PRICE_MODEL_CAROUSEL_VIEW_VALUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gyms, parent, false);
            return new ExplorePagePriceModelViewHolder(view);
        }

        if (viewType == CATEGORY_CAROUSEL_VIEW_VALUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_workout, parent, false);
            return new ExplorePageWorkoutViewHolder(view);
        }
        if (viewType == TRAINER_CAROUSAL_VIEW_VALUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gyms, parent, false);
            return new ExplorePageTrainerListViewHolder(view);
        } else if (viewType == GYM_CAROUSAL_VIEW_VALUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gyms, parent, false);
            return new ExplorePageGymListViewHolder(view);
        } else if (viewType == TOP_RATED_CLASSES_VALUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gyms, parent, false);
            return new ExplorePageRatingListViewHolder(view);
        } else if (viewType == TEXT_WITH_BUTTONS_2_VALUE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_still_confused, parent, false);
            return new StillConfusedViewHolder(view);
        }
        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BannerViewHolder) {
            ((BannerViewHolder) holder).bind(bannerDataList, shownInScreen, adapterCallbacksExplore, position);
        } else if (holder instanceof TryP5MViewHolder) {
            ((TryP5MViewHolder) holder).bind(list.get(position), shownInScreen, adapterCallbacksExplore, position);
        } else if (holder instanceof CustomerSpeakViewHolder) {
            ((CustomerSpeakViewHolder) holder).bind(list.get(position), shownInScreen, adapterCallbacksExplore, position);
        } else if (holder instanceof ExplorePagePriceModelViewHolder) {
            ((ExplorePagePriceModelViewHolder) holder).bind(list.get(position), adapterCallbacksExplore, position);
        } else if (holder instanceof ExplorePageGymListViewHolder) {
            ((ExplorePageGymListViewHolder) holder).bind(list.get(position), adapterCallbacksExplore, position);
        } else if (holder instanceof ExplorePageTrainerListViewHolder) {
            ((ExplorePageTrainerListViewHolder) holder).bind(list.get(position), adapterCallbacksExplore, position);
        } else if (holder instanceof ExplorePageWorkoutViewHolder) {
            ((ExplorePageWorkoutViewHolder) holder).bind(list.get(position), adapterCallbacksExplore, position);
        } else if (holder instanceof StillConfusedViewHolder) {
            ((StillConfusedViewHolder) holder).bind(list.get(position), shownInScreen, adapterCallbacksExplore, position);
        } else if (holder instanceof ExplorePageRatingListViewHolder) {
            ((ExplorePageRatingListViewHolder) holder).bind(list.get(position), adapterCallbacksExplore, position);
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
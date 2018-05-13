package www.gymhop.p5m.view.activity.Main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.SearchAdapter;
import www.gymhop.p5m.adapters.SearchPagerAdapter;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.GymDetailModel;
import www.gymhop.p5m.data.main.SearchResults;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.eventbus.Events;
import www.gymhop.p5m.eventbus.GlobalBus;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.KeyboardUtils;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;
import www.gymhop.p5m.view.fragment.ViewPagerFragmentSelection;

public class SearchActivity extends BaseActivity implements NetworkCommunicator.RequestListener, AdapterCallbacks, View.OnClickListener, ViewPager.OnPageChangeListener {

    public static void openActivity(Context context, Activity activity, View sharedElement, int navigationFrom) {

        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElement, "searchIcon");

        context.startActivity(new Intent(context, SearchActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom), activityOptionsCompat.toBundle());
    }

    @BindView(R.id.imageViewSearchCancel)
    public ImageView imageViewSearchCancel;
    @BindView(R.id.imageViewImageSearch)
    public ImageView imageViewImageSearch;
    @BindView(R.id.progressBarDone)
    public ProgressBar progressBarDone;
    @BindView(R.id.editTextSearch)
    public EditText editTextSearch;
    @BindView(R.id.recyclerViewSearch)
    public RecyclerView recyclerViewSearch;

    @BindView(R.id.viewPager)
    public ViewPager viewPager;
    @BindView(R.id.tabs)
    public TabLayout tabLayout;

    @BindView(R.id.layoutSearchDetails)
    public View layoutSearchDetails;
    @BindView(R.id.layoutSearch)
    public View layoutSearch;
    @BindView(R.id.layoutSearchBar)
    public View layoutSearchBar;

    private Handler handlerUI;
    private Handler handlerBG;
    private int navigatedFrom;
    private SearchAdapter searchAdapter;

    private SearchPagerAdapter searchPagerAdapter;
    private String[] titleTabs = new String[]{
            "Classes\n(" + 0 + ")",
            "Trainers\n(" + 0 + ")",
            "Gyms\n(" + 0 + ")"
    };
    private int TOTAL_TABS = 3;

    private Runnable runnableSearch;
    private Call searchCall;
    private int dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(activity);
        GlobalBus.getBus().register(this);

        dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        navigatedFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);

        setupHandlers();

        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewSearch.setHasFixedSize(false);

        searchAdapter = new SearchAdapter(context, activity, navigatedFrom, false, this);
        recyclerViewSearch.setAdapter(searchAdapter);

        setupSearch();

        imageViewImageSearch.setOnClickListener(this);
        imageViewSearchCancel.setOnClickListener(this);
        layoutSearch.setOnClickListener(this);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(context, R.color.date_tabs));

        viewPager.addOnPageChangeListener(this);

        try {
            ((SimpleItemAnimator) recyclerViewSearch.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    viewResults();
                }
                return false;
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Transition sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
            sharedElementEnterTransition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    handlerUI.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            editTextSearch.setVisibility(View.VISIBLE);
                            editTextSearch.requestFocus();
                            KeyboardUtils.open(editTextSearch, context);

                        }
                    }, 100);
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void classJoin(Events.ClassJoin data) {
        handleClassJoined(data.data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void packagePurchasedForClass(Events.PackagePurchasedForClass data) {
        handleClassJoined(data.data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void classPurchased(Events.ClassPurchased data) {
        handleClassJoined(data.data);
    }

    private void handleClassJoined(ClassModel data) {

        try {
            int index = searchAdapter.getList().indexOf(data);

            if (index != -1) {
                Object obj = searchAdapter.getList().get(index);

                if (obj instanceof ClassModel) {
                    ClassModel classModel = (ClassModel) obj;
                    Helper.setClassJoinEventData(classModel, data);

                    searchAdapter.notifyItemChanged(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

    }

    private void setupSearch() {

        editTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus) {
//                    recyclerViewSearch.setVisibility(View.VISIBLE);
                    layoutSearch.setVisibility(View.VISIBLE);
                }
            }
        });

        editTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                recyclerViewSearch.setVisibility(View.VISIBLE);
                layoutSearch.setVisibility(View.VISIBLE);
                return false;
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (editable.toString().length() > 2) {

                    if (runnableSearch != null) {
                        handlerBG.removeCallbacks(runnableSearch);
                    }

                    if (searchCall != null) {
                        searchCall.cancel();
                    }

                    startSearching(editable.toString());
                } else {

                    if (runnableSearch != null) {
                        handlerBG.removeCallbacks(runnableSearch);
                    }

                    if (searchCall != null) {
                        searchCall.cancel();
                    }

                    clearSearch();
                }
            }
        });
    }

    private void startSearching(final String searchText) {
        runnableSearch = new Runnable() {
            @Override
            public void run() {
                handlerUI.post(new Runnable() {
                    @Override
                    public void run() {
                        imageViewImageSearch.setVisibility(View.GONE);
                        progressBarDone.setVisibility(View.VISIBLE);
                    }
                });
                searchCall = networkCommunicator.search(searchText, "user", SearchActivity.this, false);
            }
        };

        handlerBG.post(runnableSearch);
    }

    private void clearSearch() {

        searchAdapter.setSearchResult(null);

        handlerUI.post(new Runnable() {
            @Override
            public void run() {
                searchAdapter.notifyDataSetChanges();
//                layoutSearch.setVisibility(View.GONE);
                imageViewImageSearch.setVisibility(View.VISIBLE);
                progressBarDone.setVisibility(View.GONE);
            }
        });
    }

    private void setupHandlers() {
        handlerUI = new Handler(Looper.getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handlerBG = new Handler();
                Looper.loop();
            }
        }).start();
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.SEARCH_ALL:

                SearchResults data = ((ResponseModel<SearchResults>) response).data;

                searchAdapter.setSearchResult(data);

                handlerUI.post(new Runnable() {
                    @Override
                    public void run() {
                        searchAdapter.notifyDataSetChanges();
                        imageViewImageSearch.setVisibility(View.VISIBLE);
                        progressBarDone.setVisibility(View.GONE);
                    }
                });

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.SEARCH_ALL:

                clearSearch();

//                ToastUtils.showLong(context, errorMessage);

                break;
        }
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

        switch (view.getId()) {
            case 1: //View All Results
                viewResults();

                break;
        }
    }

    private void viewResults() {
        SearchResults searchResult = searchAdapter.getSearchResult();

        if (searchResult == null) {
            searchResult = new SearchResults();
            searchResult.setGymDetailList(new ArrayList<GymDetailModel>());
            searchResult.setClassDetailList(new ArrayList<ClassModel>());
            searchResult.setTrainerDetailList(new ArrayList<TrainerModel>());
        }

        titleTabs = new String[]{
                "Classes\n(" + searchResult.getClassCount() + ")",
                "Trainers\n(" + searchResult.getTrainerCount() + ")",
                "Gyms\n(" + searchResult.getGymCount() + ")"
        };

        searchPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager(), titleTabs);
        searchPagerAdapter.setSearchKeywords(editTextSearch.getText().toString());

        viewPager.setAdapter(searchPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);

//                generateTabs(searchResult);

        searchPagerAdapter.notifyDataSetChanged();
        layoutSearchDetails.setVisibility(View.VISIBLE);

//                recyclerViewSearch.setVisibility(View.GONE);
        layoutSearch.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerViewSearch.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);

        KeyboardUtils.close(editTextSearch, context);

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageSelected(0);
            }
        });
    }

    private void generateTabs(SearchResults searchResult) {
        for (int index = 0; index < titleTabs.length; index++) {

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.CENTER);

            TextView textViewTitle = new TextView(context);
            textViewTitle.setGravity(Gravity.CENTER);
            textViewTitle.setTypeface(Typeface.SANS_SERIF);
            textViewTitle.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            textViewTitle.setAllCaps(true);
            textViewTitle.setTextColor(ContextCompat.getColorStateList(context, R.color.date_tabs));
            textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            TextView textViewSubtitle = new TextView(context);
            textViewSubtitle.setPadding(dp, dp * 2, dp, dp);
            textViewTitle.setGravity(Gravity.CENTER);
            textViewSubtitle.setTypeface(Typeface.SANS_SERIF);
            textViewSubtitle.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
            textViewSubtitle.setTextColor(ContextCompat.getColorStateList(context, R.color.date_tabs));
            textViewSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);

            linearLayout.addView(textViewTitle);
            linearLayout.addView(textViewSubtitle);

            tabLayout.getTabAt(index).setCustomView(linearLayout);

            String titleTab = titleTabs[index];
            String[] split = titleTab.split("\n");

            try {
                textViewTitle.setText(split[0]);
                textViewSubtitle.setText(split[1]);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);

                textViewTitle.setText(titleTab);
                textViewSubtitle.setText("(0)");
            }
        }
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewImageSearch:
                break;

            case R.id.imageViewSearchCancel:

                if (editTextSearch.getText().toString().isEmpty()) {
                    onBackPressed();
                } else
                    editTextSearch.setText("");

                break;

            case R.id.layoutSearch:
                layoutSearch.setVisibility(View.GONE);
                KeyboardUtils.close(editTextSearch, context);

                if (searchAdapter.isEmpty() && searchPagerAdapter == null) {
                    onBackPressed();
                }

                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        try {
            ((ViewPagerFragmentSelection) searchPagerAdapter.getFragments().get(position)).onTabSelection(position);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

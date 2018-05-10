package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.SearchAdapter;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.SearchResults;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.KeyboardUtils;
import www.gymhop.p5m.utils.ToastUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class SearchActivity extends BaseActivity implements NetworkCommunicator.RequestListener, AdapterCallbacks<ClassModel>, View.OnClickListener {


    public static void openActivity(Context context, int navigationFrom) {
        context.startActivity(new Intent(context, SearchActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom));
    }

    @BindView(R.id.imageViewSearchCancel)
    public ImageView imageViewSearchCancel;
    @BindView(R.id.imageViewImageSearch)
    public ImageView imageViewImageSearch;
    @BindView(R.id.editTextSearch)
    public EditText editTextSearch;
    @BindView(R.id.recyclerViewSearch)
    public RecyclerView recyclerViewSearch;

    @BindView(R.id.layoutSearchDetails)
    public View layoutSearchDetails;

    private Handler handlerUI;
    private Handler handlerBG;
    private int navigatedFrom;
    private SearchAdapter searchAdapter;

    private Runnable runnableSearch;
    private Call searchCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(activity);

        navigatedFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);

        setupHandlers();

        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewSearch.setHasFixedSize(false);

        searchAdapter = new SearchAdapter(context, activity, navigatedFrom, false, this);
        recyclerViewSearch.setAdapter(searchAdapter);

        setupSearch();

        imageViewImageSearch.setOnClickListener(this);
        imageViewSearchCancel.setOnClickListener(this);

        editTextSearch.requestFocus();
        KeyboardUtils.open(editTextSearch, context);
    }

    private void setupSearch() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (editable.toString().length() > 1) {

                    if (runnableSearch != null) {
                        handlerBG.removeCallbacks(runnableSearch);
                    }

                    if (searchCall != null) {
                        searchCall.cancel();
                    }

                    runnableSearch = new Runnable() {
                        @Override
                        public void run() {
                            handlerUI.post(new Runnable() {
                                @Override
                                public void run() {
                                    imageViewImageSearch.setVisibility(View.GONE);
                                }
                            });
                            searchCall = networkCommunicator.search(editable.toString(), "user", SearchActivity.this, false);
                        }
                    };

                    handlerBG.post(runnableSearch);
                } else {
                    if (runnableSearch != null) {
                        handlerBG.removeCallbacks(runnableSearch);
                    }

                    if (searchCall != null) {
                        searchCall.cancel();
                    }

                    searchAdapter.setSearchResult(null);

                    handlerUI.post(new Runnable() {
                        @Override
                        public void run() {
                            searchAdapter.notifyDataSetChanges();
                            imageViewImageSearch.setVisibility(View.VISIBLE);
                        }
                    });
                }
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
                    }
                });

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.SEARCH_ALL:

                searchAdapter.setSearchResult(null);

                handlerUI.post(new Runnable() {
                    @Override
                    public void run() {
                        searchAdapter.notifyDataSetChanges();
                        imageViewImageSearch.setVisibility(View.VISIBLE);
                    }
                });

                ToastUtils.showLong(context, errorMessage);

                break;
        }
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, ClassModel model, int position) {

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, ClassModel model, int position) {

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
                editTextSearch.setText("");
                break;
        }
    }
}

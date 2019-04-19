package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.TransactionsAdapter;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.main.Transaction;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransactionHistoryActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterCallbacks, NetworkCommunicator.RequestListener {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, TransactionHistoryActivity.class));
    }
    public static Intent createIntent(Context context, int navigationFrom) {
        return new Intent(context, TransactionHistoryActivity.class)
                .putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
    }

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.textViewEmptyLayoutText)
    public View textViewEmptyLayoutText;

    private TransactionsAdapter transactionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        ButterKnife.bind(activity);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(false);

        transactionsAdapter = new TransactionsAdapter(context, this);
        recyclerView.setAdapter(transactionsAdapter);

        onRefresh();
    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        finish();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        networkCommunicator.getTransactions(TempStorage.getUser().getId(), this, false);
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
    }

    @Override
    public void onShowLastItem() {
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.TRANSACTION:

                swipeRefreshLayout.setEnabled(false);

                List<Transaction> list = ((ResponseModel<List<Transaction>>) response).data;

                if (list != null && !list.isEmpty()) {
                    transactionsAdapter.addAll(list);
                    transactionsAdapter.notifyDataSetChanged();
                }

                checkListData();

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.TRANSACTION:
                transactionsAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void checkListData() {
        if (transactionsAdapter.getList().isEmpty()) {
            textViewEmptyLayoutText.setVisibility(View.VISIBLE);
        } else {
            textViewEmptyLayoutText.setVisibility(View.GONE);
        }
    }
}

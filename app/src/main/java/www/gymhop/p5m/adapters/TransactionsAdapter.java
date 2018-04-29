package www.gymhop.p5m.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.viewholder.EmptyViewHolder;
import www.gymhop.p5m.adapters.viewholder.LoaderViewHolder;
import www.gymhop.p5m.adapters.viewholder.TransactionViewHolder;
import www.gymhop.p5m.data.ListLoader;
import www.gymhop.p5m.data.Transaction;

public class TransactionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_LOADER = 2;
    private static final int VIEW_TYPE_UNKNOWN = 3;

    private final AdapterCallbacks adapterCallbacks;

    private List<Transaction> list;
    private Context context;

    private boolean showLoader;
    private ListLoader listLoader;

    public TransactionsAdapter(Context context, AdapterCallbacks adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        this.showLoader = false;
        listLoader = new ListLoader();
    }

    public void add(Transaction model) {
        list.add(model);
        addLoader();
    }

    public void addAll(List<Transaction> models) {
        list.addAll(models);
        addLoader();
    }

    private void addLoader() {
//        if (showLoader && list.contains(listLoader)) {
//            list.remove(listLoader);
//            list.add(listLoader);
//        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_transactions, parent, false);
            return new TransactionViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }

        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TransactionViewHolder) {
            ((TransactionViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        } else if (holder instanceof LoaderViewHolder) {
            ((LoaderViewHolder) holder).bind(listLoader, adapterCallbacks);
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
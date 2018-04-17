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
import www.gymhop.p5m.adapters.viewholder.MemberShipHeaderViewHolder;
import www.gymhop.p5m.adapters.viewholder.MemberShipViewHolder;
import www.gymhop.p5m.data.HeaderSticky;
import www.gymhop.p5m.data.ListLoader;
import www.gymhop.p5m.data.Package;
import www.gymhop.p5m.data.UserPackage;

public class MemberShipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_UNKNOWN = -1;
    private static final int VIEW_TYPE_MEMBERSHIP_HEADER = 1;
    private static final int VIEW_TYPE_MEMBERSHIP = 2;
    private static final int VIEW_TYPE_LOADER = 3;

    private final AdapterCallbacks adapterCallbacks;

    private List<Object> list;
    private Context context;

    private List<UserPackage> ownedPackages;
    private List<Package> offeredPackages;

    private HeaderSticky headerSticky1, headerSticky2;


    private final int navigatedFrom;

    private boolean showLoader;
    private ListLoader listLoader;

    public MemberShipAdapter(Context context, int navigatedFrom, boolean showLoader, AdapterCallbacks adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        ownedPackages = new ArrayList<>();
        offeredPackages = new ArrayList<>();

        this.navigatedFrom = navigatedFrom;
        this.showLoader = showLoader;
        listLoader = new ListLoader();

        headerSticky1 = new HeaderSticky("");
        headerSticky2 = new HeaderSticky("");
    }

    public void addAllOfferedPackages(List<Package> data) {
        offeredPackages.addAll(data);
    }

    public void addOfferedPackages(Package data) {
        offeredPackages.add(data);
    }

    public void addOwnedPackages(UserPackage data) {
        ownedPackages.add(data);
    }

    public void addAllOwnedPackages(List<UserPackage> data) {
        ownedPackages.addAll(data);
    }

    public List<UserPackage> getOwnedPackages() {
        return ownedPackages;
    }

    public List<Package> getOfferedPackages() {
        return offeredPackages;
    }

    public void add(Package model) {
        list.add(model);
        addLoader();
    }

    public void setHeaderText(String text1, String text2) {

    }

    public void notifyDataSetChanges() {
        list.clear();

        list.add(headerSticky1);
        list.addAll(offeredPackages);
        list.add(headerSticky2);
        list.addAll(ownedPackages);

        notifyDataSetChanged();
    }

    public void addAll(List<Package> models) {
        list.addAll(models);
        addLoader();
    }

    private void addLoader() {
        if (showLoader && list.contains(listLoader)) {
            list.remove(listLoader);
            list.add(listLoader);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = VIEW_TYPE_UNKNOWN;

        Object item = getItem(position);
        if (item instanceof UserPackage) {
            itemViewType = VIEW_TYPE_MEMBERSHIP;
        } else if (item instanceof String) {
            itemViewType = VIEW_TYPE_MEMBERSHIP_HEADER;
        } else if (item instanceof ListLoader) {
            itemViewType = VIEW_TYPE_LOADER;
        }

        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MEMBERSHIP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_membership, parent, false);
            return new MemberShipViewHolder(view, navigatedFrom);
        } else if (viewType == VIEW_TYPE_MEMBERSHIP_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new MemberShipHeaderViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_progress, parent, false);
            return new LoaderViewHolder(view);
        }

        return new EmptyViewHolder(new LinearLayout(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MemberShipViewHolder) {
            ((MemberShipViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        } else if (holder instanceof MemberShipHeaderViewHolder) {
//            ((MemberShipHeaderViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
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
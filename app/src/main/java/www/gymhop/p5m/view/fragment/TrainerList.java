package www.gymhop.p5m.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.TrainerListAdapter;
import www.gymhop.p5m.adapters.loader.Loader;
import www.gymhop.p5m.data.Class;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.view.activity.custom.MyRecyclerView;

public class TrainerList extends BaseFragment implements AdapterCallbacks<Class>,MyRecyclerView.LoaderCallbacks {

    @BindView(R.id.recyclerViewClass)
    public RecyclerView recyclerViewTrainers;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private TrainerListAdapter classListAdapter;

    public TrainerList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainer_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());
        int position = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);

        recyclerViewTrainers.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewTrainers.setHasFixedSize(false);

        classListAdapter  = new TrainerListAdapter(context, this);
        recyclerViewTrainers.setAdapter(classListAdapter);

        List<Class> classes = new ArrayList<>();
        for (int count = 0; count < 10; count++) {
            classes.add(new Class());
        }

        classListAdapter.setData(classes);
        classListAdapter.setLoader(true, true,
                getString(R.string.loading), getString(R.string.no_more_data), new Loader.ClassLoader(), this);
        classListAdapter.enableLoader();
    }

    @Override
    public void onAdapterItemClick(View viewRoot, View view, Class model, int position) {

    }

    @Override
    public void onAdapterItemLongClick(View viewRoot, View view, Class model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }

    @Override
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {

    }
}

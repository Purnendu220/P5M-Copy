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
import www.gymhop.p5m.adapters.ClassMiniViewAdapter;
import www.gymhop.p5m.adapters.loader.Loader;
import www.gymhop.p5m.data.Class;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.custom.MyRecyclerView;

public class ClassMiniViewList extends BaseFragment implements AdapterCallbacks<Object>, MyRecyclerView.LoaderCallbacks {

    @BindView(R.id.recyclerViewClass)
    public RecyclerView recyclerViewClass;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private ClassMiniViewAdapter classListAdapter;

    public ClassMiniViewList() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_mini_view_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());
        int position = getArguments().getInt(AppConstants.DataKey.TAB_POSITION_INT);

        recyclerViewClass.setLayoutManager(new LinearLayoutManager(activity));
        recyclerViewClass.setHasFixedSize(false);

        classListAdapter = new ClassMiniViewAdapter(context, this);
        recyclerViewClass.setAdapter(classListAdapter);

        List<Class> classes = new ArrayList<>();
        for (int count = 0; count < 5; count++) {
            classes.add(new Class());
        }

        classListAdapter.setData(classes);
        classListAdapter.setLoader(true, true,
                getString(R.string.loading), getString(R.string.no_more_data), new Loader.ClassLoader(), this);
        classListAdapter.enableLoader();
    }

    @Override
    public void onShowLoader(MyRecyclerView.LoaderItem loaderItem, MyRecyclerView.Loader loader, View view, int position) {
        LogUtils.debug("onShowLoader " + position);
    }

    @Override
    public void onAdapterItemClick(View viewRoot, View view, Object model, int position) {
        www.gymhop.p5m.view.activity.Main.TrainerProfile.open(context);
    }

    @Override
    public void onAdapterItemLongClick(View viewRoot, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
}

package www.gymhop.p5m.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brandongogetap.stickyheaders.StickyLayoutManager;
import com.brandongogetap.stickyheaders.exposed.StickyHeaderListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.adapters.TrainerProfileAdapter;
import www.gymhop.p5m.data.HeaderSticky;
import www.gymhop.p5m.data.TrainerDetail;

public class TrainerProfile extends BaseFragment implements AdapterCallbacks {

    @BindView(R.id.recyclerViewTrainerProfile)
    public RecyclerView recyclerViewTrainerProfile;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    private TrainerProfileAdapter trainerProfileAdapter;

    public TrainerProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainer_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ButterKnife.bind(this, getView());

        trainerProfileAdapter = new TrainerProfileAdapter(context, this);
        recyclerViewTrainerProfile.setAdapter(trainerProfileAdapter);

        StickyLayoutManager layoutManager = new StickyLayoutManager(context, trainerProfileAdapter);
        layoutManager.elevateHeaders(true);
        layoutManager.elevateHeaders((int) context.getResources().getDimension(R.dimen.view_separator_elevation));

        recyclerViewTrainerProfile.setLayoutManager(layoutManager);
        layoutManager.setStickyHeaderListener(new StickyHeaderListener() {
            @Override
            public void headerAttached(View headerView, int adapterPosition) {
                Log.d("Listener", "Attached with position: " + adapterPosition);
            }

            @Override
            public void headerDetached(View headerView, int adapterPosition) {
                Log.d("Listener", "Detached with position: " + adapterPosition);
            }
        });

        List<Object> data = new ArrayList<>();
        for (int count = 0; count < 7; count++) {
            if (count == 1)
                data.add(new HeaderSticky(""));
            else
                data.add(new TrainerDetail());
        }

        trainerProfileAdapter.setData(data);
    }

    @Override
    public void onItemClick(View viewRoot, View view, Object model, int position) {

    }

    @Override
    public void onItemLongClick(View viewRoot, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
}

package com.p5m.me.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.recyclerview.widget.RecyclerView;

import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.ShowScheduleViewHolder;
import com.p5m.me.data.main.ScheduleClassModel;

import java.util.ArrayList;
import java.util.List;


public class ShowScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> list;
    private Context context;
    private final AdapterCallbacks<Object> adapterCallbacks;

    private int shownInScreen;


    public ShowScheduleAdapter(Context context, int shownInScreen, AdapterCallbacks<Object> adapterCallbacks) {
        this.adapterCallbacks = adapterCallbacks;
        this.context = context;
        list = new ArrayList<>();
        this.shownInScreen = shownInScreen;

    }

    public List<Object> getList() {
        return list;
    }

    public void addClass(ScheduleClassModel model) {
        list.add(model);
    }

    public void addAllClass(List<ScheduleClassModel> models) {
        list.addAll(models);
        notifyDataSetChanged();
    }

    public void clearAll() {
        list.clear();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_map_schedule, parent, false);
        return new ShowScheduleViewHolder(view, shownInScreen);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ShowScheduleViewHolder) {
//            setFadeAnimation(holder.itemView);
            ((ShowScheduleViewHolder) holder).bind(getItem(position), adapterCallbacks, position);
        }
    }

    private void setFadeAnimation(View view) {
        Animation anim = new ScaleAnimation(0f, 1f,
                0f, 1f,
                Animation.ABSOLUTE, 1,
                Animation.ABSOLUTE, 0);
        anim.setDuration(750);

        view.startAnimation(anim);
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

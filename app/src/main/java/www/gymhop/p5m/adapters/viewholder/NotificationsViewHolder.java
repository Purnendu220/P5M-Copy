package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gymhop.p5m.R;
import www.gymhop.p5m.adapters.AdapterCallbacks;
import www.gymhop.p5m.data.main.NotificationModel;
import www.gymhop.p5m.utils.DateUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class NotificationsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewTime)
    public TextView textViewTime;
    @BindView(R.id.textViewDetails)
    public TextView textViewDetails;

    @BindView(R.id.imageViewProfile)
    public ImageView imageViewProfile;
    @BindView(R.id.imageViewProfileStatus)
    public ImageView imageViewProfileStatus;

    private final Context context;

    public NotificationsViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);

    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {

        if (data != null && data instanceof NotificationModel) {
            itemView.setVisibility(View.VISIBLE);

            final NotificationModel model = (NotificationModel) data;

            Map<String, String> message = new Gson().fromJson(
                    model.getMessage(), new TypeToken<HashMap<String, String>>() {}.getType()
            );

            textViewTime.setText(DateUtils.getClassDateNotification(message.get("classDate")));
            textViewDetails.setText(message.get("activityMsg"));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(NotificationsViewHolder.this, itemView, data, position);
                }
            });
        } else {
            itemView.setVisibility(View.GONE);
        }
    }
}

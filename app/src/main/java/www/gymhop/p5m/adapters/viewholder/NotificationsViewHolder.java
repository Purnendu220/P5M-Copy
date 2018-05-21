package www.gymhop.p5m.adapters.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import www.gymhop.p5m.utils.ImageUtils;
import www.gymhop.p5m.utils.LogUtils;

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
                    model.getMessage(), new TypeToken<HashMap<String, String>>() {
                    }.getType()
            );

            if (((NotificationModel) data).getUserWhoFiredEvent() != null) {
                ImageUtils.setImage(context, ((NotificationModel) data).getUserWhoFiredEvent().getProfileImage(), R.drawable.profile_holder, imageViewProfile);
            } else {
                ImageUtils.clearImage(context, imageViewProfile);
            }

            String info = message.get("activityMsg");

            for (Map.Entry<String, String> entry : message.entrySet()) {

                try {
                    if (!entry.getValue().equals("activityMsg")) {
                        if (info.contains("$" + entry.getKey() + "$")) {
                            info = info.replace("$" + entry.getKey() + "$", "<b>" + entry.getValue() + "</b>");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.debug(" message.get(\"activityMsg\") " + info);
                    LogUtils.exception(e);
                }
            }

            textViewTime.setText(DateUtils.getClassDateNotification(model.getCreatedAt()));
            textViewDetails.setText(Html.fromHtml(info));

            switch (model.getNotificationType()) {
                case "FollowUser":
                case "OnUserComingClass":
                case "OnUserWishListComing":
                    imageViewProfileStatus.setImageResource(R.drawable.heart_icon);
                    break;

                case "CustomerJoinClassOfTrainer":
                case "CustomerJoinClassOfGym":
                case "OnClassCreation":
                case "OnSessionUpdateByTrainer":
                case "OnSessionUpdateByGYM":
                case "OnSessionUpdateByTrainerOfGym":
                case "OnSessionUpdateByGymOfTrainer":
                case "OnPaymentSuccess":
                case "OnClassRefund":
                case "OnClassUpdateByTrainer":
                case "OnClassUpdateByGYM":
                case "OnClassUpdateByTrainerOfGym":
                case "OnClassUpdateByGYMOfTrainer":
                case "OnClassCreationNotifyGym":
                case "OnClassUpdateByCms":
                case "OnClassUpdateByCMS":
                case "OnNewTrainerAssign":
                case "OnGroupClassUpdateByCms":
                case "OnEmailVerification":
                    imageViewProfileStatus.setImageResource(R.drawable.add_icon);
                    break;

                case "CustomerCancelClass":
                case "CustomerCancelClassOfTrainer":
                case "CustomerCancelClassOfGym":
                case "OnClassDeleteByGym":
                case "OnClassDeleteByTrainer":
                case "OnClassDeleteByTrainerOfGym":
                case "OnClassDeleteByGymOfTrainer":
                case "OnSessionDeleteByTrainer":
                case "OnSessionDeleteByGYM":
                case "OnSessionDeleteByTrainerOfGym":
                case "OnSessionDeleteByGymOfTrainer":
                case "OnFinishedPackage":
                case "OnSlotDeleteByTrainer":
                case "OnSlotDeleteByGym":
                case "OnSlotDeletByTrainerOfGym":
                case "OnSlotDeleteByGymOfTrainer":
                case "OnClassInActive":
                case "OnClassCancelByCMS":
                case "OnBookingCancelToCustomerByCMS":
                case "OnBookingCancelToGymByCMS":
                case "OnBookingCancelToTrainerByCMS":
                case "OnBookingCancelByCMS":
                    imageViewProfileStatus.setImageResource(R.drawable.delete_red);
                    break;
            }

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

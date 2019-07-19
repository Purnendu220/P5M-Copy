package com.p5m.me.helper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.view.activity.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CancelBookingBottomDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    public static AdapterCallbacks<ClassModel> adapterCallbacks;
    public static CancelBookingBottomDialogFragment newInstance(ClassModel classModel,boolean isBookingWithFriend, AdapterCallbacks<ClassModel> adapterCallbacks) {

        CancelBookingBottomDialogFragment bottomSheetFragment = new CancelBookingBottomDialogFragment();
        CancelBookingBottomDialogFragment.classModel = classModel;
        CancelBookingBottomDialogFragment.adapterCallbacks = adapterCallbacks;
        CancelBookingBottomDialogFragment.isBookingWithFriend = isBookingWithFriend;
        return bottomSheetFragment;
    }

    @BindView(R.id.textViewCancelFriendBooking)
    TextView textViewCancelFriendBooking;
    @BindView(R.id.textViewDialogDismiss)
    TextView textViewDialogDismiss;
   @BindView(R.id.textViewCancelBothBooking)
    TextView textViewCancelBothBooking;
     @BindView(R.id.textViewCancelBooking)
    TextView textViewCancelBooking;
    private static ClassModel classModel;
    private static boolean isBookingWithFriend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_fragment, container,
                false);
        ButterKnife.bind(this, view);
        handleView();
        handleClickEvent();
        return view;

    }

    private void handleView() {
        if(isBookingWithFriend){
            textViewCancelBooking.setVisibility(View.GONE);
            textViewCancelFriendBooking.setVisibility(View.VISIBLE);
            textViewCancelBothBooking.setVisibility(View.VISIBLE);

        }
        else
        {
            textViewCancelBooking.setVisibility(View.VISIBLE);
            textViewCancelFriendBooking.setVisibility(View.GONE);
            textViewCancelBothBooking.setVisibility(View.GONE);
        }
    }

    private void handleClickEvent() {
        textViewCancelFriendBooking.setOnClickListener(this);
        textViewCancelBothBooking.setOnClickListener(this);
        textViewCancelBooking.setOnClickListener(this);
        textViewDialogDismiss.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        ClassListListenerHelper classListListenerHelper= new ClassListListenerHelper(getContext(), getActivity(), AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING, adapterCallbacks);

        switch (v.getId()) {
            case R.id.textViewCancelFriendBooking:
                classListListenerHelper.dialogConfirmUnJoinBookWithFriend(getContext(), ((BaseActivity)getContext()).networkCommunicator, classModel, classModel.getRefBookingId(), AppConstants.Values.UNJOIN_FRIEND_CLASS);
                dismiss();

                break;
                case R.id.textViewCancelBooking:
                    classListListenerHelper.dialogConfirmUnJoin(getContext(), ((BaseActivity) getContext()).networkCommunicator, classModel, classModel.getJoinClassId());
                    dismiss();

                break;

            case R.id.textViewCancelBothBooking:
                classListListenerHelper.dialogConfirmUnJoinBookWithFriend(getContext(), ((BaseActivity)getContext()).networkCommunicator, classModel, classModel.getJoinClassId(), AppConstants.Values.UNJOIN_BOTH_CLASS);
                dismiss();
                break;
            case R.id.textViewDialogDismiss:
                dismiss();
                break;
        }
    }
}

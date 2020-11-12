package com.p5m.me.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.R;
import com.p5m.me.data.BookWithFriendData;
import com.p5m.me.data.SubscriptionConfigModal;
import com.p5m.me.data.UserPackageInfo;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     EditSubscriptionBottomSheet.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class EditSubscriptionBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    public EditSubscriptionBottomSheet(Context mContext,OnClickBottomSheet onBottomSheetCallback) {
        this.onBottomSheetCallback = onBottomSheetCallback;
        this.mContext = mContext;
    }

    public static EditSubscriptionBottomSheet newInstance(Context mContext,OnClickBottomSheet onBottomSheetCallback ) {

        EditSubscriptionBottomSheet bottomSheetFragment = new EditSubscriptionBottomSheet(mContext,onBottomSheetCallback);

        return bottomSheetFragment;
    }

    @BindView(R.id.textViewUpdateSubscription)
    TextView textViewUpdateSubscription;
    @BindView(R.id.textViewUpdateSubscriptionDesc)
    TextView textViewUpdateSubscriptionDesc;
    @BindView(R.id.textViewCancelSubscription)
    TextView textViewCancelSubscription;
    @BindView(R.id.textViewCancelSubscriptionDesc)
    TextView textViewCancelSubscriptionDesc;




    @BindView(R.id.textViewDialogDismiss)
    TextView textViewDialogDismiss;

    @BindView(R.id.cancelSubscription)
    LinearLayout cancelSubscription;
    @BindView(R.id.updateSubscription)
    LinearLayout updateSubscription;


    @BindView(R.id.renewSubscription)
    LinearLayout renewSubscription;
    @BindView(R.id.textViewRenewSubscription)
    TextView textViewRenewSubscription;
    @BindView(R.id.textViewRenewSubscriptionDesc)
    TextView textViewRenewSubscriptionDesc;



    NetworkCommunicator mNetworkCommunicator;

    OnClickBottomSheet onBottomSheetCallback;
    private SubscriptionConfigModal mSubscriptionConfigModal;
    private Context mContext;
    private boolean addCredits = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_subscription_bottom_sheet_list_dialog, container,
                false);
        ButterKnife.bind(this, view);
        mNetworkCommunicator = NetworkCommunicator.getInstance(getContext());
        cancelSubscription.setOnClickListener(this);
        updateSubscription.setOnClickListener(this);
        renewSubscription.setOnClickListener(this);
        UserPackageInfo userPackageInfo = new UserPackageInfo(TempStorage.getUser());
        if(userPackageInfo.haveGeneralPackage&&userPackageInfo.userPackageGeneral!=null){
            if(userPackageInfo.userPackageGeneral.getBalance()<10){
                addCredits = true;

            }else{
                addCredits = false;

            }
        }
        initSubscriptionConfig();

        return view;
    }

    private void initSubscriptionConfig(){
        String SUBSCRIPTION_CONFIG = RemoteConfigure.getFirebaseRemoteConfig(mContext).getRemoteConfigValue(RemoteConfigConst.SUBSCRIPTION_CONFIG_VALUE);
        try{
            Gson g = new Gson();
            mSubscriptionConfigModal = g.fromJson(SUBSCRIPTION_CONFIG, new TypeToken<SubscriptionConfigModal>() {
            }.getType());
        }catch (Exception e){
            e.printStackTrace();
        }
        textViewUpdateSubscription.setText(mSubscriptionConfigModal.getUpdateSubscriptionOption());
        textViewCancelSubscription.setText(mSubscriptionConfigModal.getCancelSubscriptionOption());

        if(mSubscriptionConfigModal.getCancelSubscriptionOptionDesc()!=null&&mSubscriptionConfigModal.getCancelSubscriptionOptionDesc().length()>0){
            textViewCancelSubscriptionDesc.setVisibility(View.VISIBLE);
            textViewCancelSubscriptionDesc.setText(mSubscriptionConfigModal.getCancelSubscriptionOptionDesc());
        }else{
            textViewCancelSubscriptionDesc.setVisibility(View.GONE);

        }

        if(mSubscriptionConfigModal.getUpdateSubscriptionOptionDesc()!=null&&mSubscriptionConfigModal.getUpdateSubscriptionOptionDesc().length()>0){
            textViewUpdateSubscriptionDesc.setVisibility(View.VISIBLE);
            textViewUpdateSubscriptionDesc.setText(mSubscriptionConfigModal.getUpdateSubscriptionOptionDesc());
        }
        else{
            textViewUpdateSubscriptionDesc.setVisibility(View.GONE);

        }

        if(addCredits){
            if(mSubscriptionConfigModal.getRenewSubscriptionOption()!=null&&mSubscriptionConfigModal.getRenewSubscriptionOption().length()>0){
                renewSubscription.setVisibility(View.VISIBLE);
                textViewRenewSubscription.setText(mSubscriptionConfigModal.getRenewSubscriptionOption());

                if(mSubscriptionConfigModal.getRenewSubscriptionOptionDesc()!=null&&mSubscriptionConfigModal.getRenewSubscriptionOptionDesc().length()>0){
                    textViewRenewSubscriptionDesc.setVisibility(View.VISIBLE);
                    textViewRenewSubscriptionDesc.setText(mSubscriptionConfigModal.getRenewSubscriptionOptionDesc());
                }
                else{
                    textViewRenewSubscriptionDesc.setVisibility(View.GONE);

                }


            }else{
                renewSubscription.setVisibility(View.GONE);

            }




        }else{
            renewSubscription.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelSubscription:
                onBottomSheetCallback.onClickBottomSheet(cancelSubscription,"Cancel");
                dismiss();
                break;
            case R.id.updateSubscription:
                onBottomSheetCallback.onClickBottomSheet(updateSubscription,"Update");
                dismiss();
                break;
            case R.id.renewSubscription:
                onBottomSheetCallback.onClickBottomSheet(renewSubscription,"Renew");
                dismiss();
                break;
        }
    }




}
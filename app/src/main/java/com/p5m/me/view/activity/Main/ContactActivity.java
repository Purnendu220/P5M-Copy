package com.p5m.me.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.p5m.me.R;
import com.p5m.me.data.ContactRequestModel;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactActivity extends BaseActivity implements View.OnClickListener,
        NetworkCommunicator.RequestListener, RadioGroup.OnCheckedChangeListener {

    private int senderId;
    private int receiverId;
    private ContactRequestModel contactRequestModel;
    private int classSessionId;
    private Spanned msg;

    public static void openActivity(Context context, ClassModel classModel) {
        Intent intent = new Intent(context, ContactActivity.class);
        context.startActivity(intent);
        ContactActivity.classModel = classModel;
    }

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.radioGroupSender)
    public RadioGroup radioGroupSender;
    @BindView(R.id.radioButtonSendGym)
    public RadioButton radioButtonSendGym;
    @BindView(R.id.radioButtonSendTrainer)
    public RadioButton radioButtonSendTrainer;
    @BindView(R.id.etMessage)
    public EditText etMessage;
    @BindView(R.id.imageViewClass)
    public ImageView imageViewClass;
    @BindView(R.id.buttonSendMessage)
    public Button buttonSendMessage;
    @BindView(R.id.textViewClassName)
    public TextView textViewClassName;

    private static ClassModel classModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(activity);
        setToolBar();
        onClickListener();
        getterSetter();
        setRadioButtonVisibility();
        radioGroupSender.setOnCheckedChangeListener(this);
    }

    private void setRadioButtonVisibility() {
        if (classModel.getTrainerDetail() == null) {
            radioButtonSendTrainer.setEnabled(false);
            radioButtonSendGym.setChecked(true);
            receiverId = classModel.getGymBranchDetail().getGymId();
            msg=Html.fromHtml(String.format(context.getResources().getString(R.string.message_is_successfully_send),  classModel.getGymBranchDetail().getGymName()));

        }
    }

    private void getterSetter() {
       /* Glide.with(context)
                .load(classModel.getGymBranchDetail().getMediaUrl())
                .into(imageViewClass);*/
        if (classModel.getClassMedia() != null) {
            ImageUtils.setImage(context,
                    classModel.getClassMedia().getMediaThumbNailUrl(),
                    R.drawable.image_holder, imageViewClass);
        } else {
            ImageUtils.clearImage(context, imageViewClass);
        }
        senderId = TempStorage.getUser().getId();
        classSessionId = classModel.getClassSessionId();
        textViewClassName.setText(classModel.getTitle());
    }

    private void onClickListener() {
        buttonSendMessage.setOnClickListener(this);
    }

    /* Set Toolbar */
    private void setToolBar() {

        BaseActivity activity = (BaseActivity) this.activity;
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_normal, null);

        v.findViewById(R.id.imageViewBack).setVisibility(View.VISIBLE);
        v.findViewById(R.id.imageViewBack).setOnClickListener(this);

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.contact_gym_trainer));
        ((TextView) v.findViewById(R.id.textViewTitle)).setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                finish();
                break;
            case R.id.buttonSendMessage:
                setValidations();
                break;
        }
    }

    private void setValidations() {
        if (TextUtils.isEmpty(etMessage.getText().toString().trim()) ) {
            ToastUtils.show(context, R.string.please_provide_your_query);
        } else if (radioGroupSender.getCheckedRadioButtonId() == -1) {
            ToastUtils.show(context, R.string.please_select_sender);
        } else {
            setRequestModel();
            networkCommunicator.sendQuery(contactRequestModel, this, false);
        }
    }

    private void setRequestModel() {
        contactRequestModel = new ContactRequestModel();
        contactRequestModel.setSenderId(senderId);
        contactRequestModel.setReceiverId(receiverId);
        contactRequestModel.setClassSessionId(classSessionId);
        contactRequestModel.setMessage(etMessage.getText().toString());

    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        if (requestCode == NetworkCommunicator.RequestCode.SUPPORT_RESPONSE_CONTACT) {

//            DialogUtils.showBasicMessage(context, "", context.getString(R.string.ok), R.string.message_is_successfullt_send);
            DialogUtils.showBasicMessage(context, String.valueOf(msg), context.getResources().getString(R.string.ok), new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                    finish();
                }
            },false);
            etMessage.getText().clear();
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        ToastUtils.show(context, context.getString(R.string.please_try_again));
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radioButtonSendTrainer:
                 msg=Html.fromHtml(String.format(context.getResources().getString(R.string.message_is_successfully_send),  classModel.getTrainerDetail().getFirstName()));

                receiverId = classModel.getTrainerDetail().getId();
                break;
            case R.id.radioButtonSendGym:
                 msg=Html.fromHtml(String.format(context.getResources().getString(R.string.message_is_successfully_send),  classModel.getGymBranchDetail().getGymName()));


                receiverId = classModel.getGymBranchDetail().getGymId();
                break;

        }
    }
}

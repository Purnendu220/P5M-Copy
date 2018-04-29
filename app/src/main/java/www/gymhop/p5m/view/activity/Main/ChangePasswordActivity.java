package www.gymhop.p5m.view.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.gymhop.p5m.R;
import www.gymhop.p5m.data.ChangePasswordRequest;
import www.gymhop.p5m.helper.Helper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.ToastUtils;
import www.gymhop.p5m.view.activity.base.BaseActivity;

public class ChangePasswordActivity extends BaseActivity implements NetworkCommunicator.RequestListener {

    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, ChangePasswordActivity.class));
    }

    @BindView(R.id.imageViewDone)
    public ImageView imageViewDone;
    @BindView(R.id.progressBarDone)
    public ProgressBar progressBarDone;

    @BindView(R.id.textInputLayoutCurrPass)
    public TextInputLayout textInputLayoutCurrPass;
    @BindView(R.id.textInputLayoutNewPass)
    public TextInputLayout textInputLayoutNewPass;
    @BindView(R.id.textInputConfirmPass)
    public TextInputLayout textInputConfirmPass;

    @BindView(R.id.editTextConfirmPass)
    public EditText editTextConfirmPass;
    @BindView(R.id.editTextNewPass)
    public EditText editTextNewPass;
    @BindView(R.id.editTextCurrPass)
    public EditText editTextCurrPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ButterKnife.bind(activity);

        Helper.setupErrorWatcher(editTextCurrPass, textInputLayoutCurrPass);
        Helper.setupErrorWatcher(editTextNewPass, textInputLayoutNewPass);
        Helper.setupErrorWatcher(editTextConfirmPass, textInputConfirmPass);
    }

    @OnClick(R.id.imageViewDone)
    public void imageViewDone(View view) {
        String curPass = editTextCurrPass.getText().toString().trim();
        String newPass = editTextNewPass.getText().toString().trim();
        String confirmPass = editTextConfirmPass.getText().toString().trim();

        if (curPass.isEmpty()) {
            textInputLayoutCurrPass.setError("Current password is required");
            return;
        }

        if (newPass.isEmpty()) {
            textInputLayoutNewPass.setError("New password is required");
            return;
        }

        if (confirmPass.isEmpty()) {
            textInputConfirmPass.setError("Confirm password is required");
            return;
        }

        if (!Helper.validatePass(newPass)) {
            textInputLayoutNewPass.setError(context.getResources().getString(R.string.password_weak));
            return;
        }

        if (!newPass.equals(confirmPass)) {
            textInputConfirmPass.setError(context.getResources().getString(R.string.confirm_password_not_matched));
            return;
        }

        imageViewDone.setVisibility(View.GONE);
        progressBarDone.setVisibility(View.VISIBLE);

        networkCommunicator.changePass(new ChangePasswordRequest(curPass, newPass, confirmPass, TempStorage.getUser().getId()), this, false);
    }

    @OnClick(R.id.imageViewBack)
    public void imageViewBack(View view) {
        finish();
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

        imageViewDone.setVisibility(View.VISIBLE);
        progressBarDone.setVisibility(View.GONE);
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CHANGE_PASS:
                ToastUtils.show(context, ((ResponseModel<String>) response).data);
                finish();
                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

        imageViewDone.setVisibility(View.VISIBLE);
        progressBarDone.setVisibility(View.GONE);

        switch (requestCode) {
            case NetworkCommunicator.RequestCode.CHANGE_PASS:
                ToastUtils.show(context, errorMessage);
                break;
        }
    }
}

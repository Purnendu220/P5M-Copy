package com.p5m.me.view.activity.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fxn.pix.Pix;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.p5m.me.R;
import com.p5m.me.adapters.AdapterCallbacks;
import com.p5m.me.adapters.ImageListAdapter;
import com.p5m.me.adapters.SelectedImageListAdapter;
import com.p5m.me.data.ClassRatingListData;
import com.p5m.me.data.RatingFeedbackAreaResList;
import com.p5m.me.data.RatingParamModel;
import com.p5m.me.data.RatingResponseModel;
import com.p5m.me.data.UnratedClassData;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.MediaModel;
import com.p5m.me.data.request.ClassRatingRequest;
import com.p5m.me.data.request.RatingFeedbackAreaList;
import com.p5m.me.data.request.SelectedFileData;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.ratemanager.RateAlarmReceiver;
import com.p5m.me.ratemanager.ScheduleAlarmManager;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.CommonUtillity;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.ImageUtility;
import com.p5m.me.utils.ImageUtils;
import com.p5m.me.utils.KeyboardUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.RefrenceWrapper;
import com.p5m.me.utils.ToastUtils;
import com.p5m.me.view.activity.base.BaseActivity;
import com.p5m.me.view.custom.CustomDialogThankYou;
import com.p5m.me.view.custom.CustomRateAlertDialog;
import com.robertlevonyan.components.picker.ItemModel;
import com.robertlevonyan.components.picker.OnPickerCloseListener;
import com.robertlevonyan.components.picker.PickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullRatingActivity extends BaseActivity implements View.OnClickListener,AdapterCallbacks<Object>,NetworkCommunicator.RequestListener {
    private long ratingId=-1;

    public static void open(Context context, int navigationFrom, ClassModel classModel, int rating) {
        Intent intent = new Intent(context, FullRatingActivity.class);
        intent.putExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, navigationFrom);
        intent.putExtra(AppConstants.DataKey.CLASS_MODEL, classModel);
        intent.putExtra(AppConstants.DataKey.RATING_VALUE,rating);
        context.startActivity(intent);
        }
    private PickerDialog pickerDialog;

    @BindView(R.id.textViewHowWasExperiance)
    public TextView textViewHowWasExperiance;

    @BindView(R.id.textViewWhatCouldBeImproved)
    public TextView textViewWhatCouldBeImproved;

    @BindView(R.id.flexBoxLayoutOptions)
    public FlexboxLayout flexBoxLayoutOptions;

    @BindView(R.id.textInputLayoutComment)
    public TextInputLayout textInputLayoutComment;

    @BindView(R.id.editTextComment)
    public EditText editTextComment;

    @BindView(R.id.imageViewChooseImage)
    public ImageView imageViewChooseImage;

    @BindView(R.id.ratingStar1)
    public ImageView ratingStar1;

    @BindView(R.id.ratingStar2)
    public ImageView ratingStar2;

    @BindView(R.id.ratingStar3)
    public ImageView ratingStar3;

    @BindView(R.id.ratingStar4)
    public ImageView ratingStar4;

    @BindView(R.id.ratingStar5)
    public ImageView ratingStar5;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @BindView(R.id.selectedImageRecyclerView)
    public RecyclerView selectedImageRecyclerView;

    @BindView(R.id.rateClasses)
    public Button rateClasses;

    @BindView(R.id.progressBarRating)
    public ProgressBar progressBarRating;

    @BindView(R.id.parentLayout)
    public RelativeLayout parentLayout;



    ArrayList<SelectedFileData> fileList = new ArrayList<>();
    int ratingValue;
    private ClassModel model;
    private List<RatingParamModel> selectedRatingParam=new ArrayList<>();
    int navigatinFrom;
    private SelectedImageListAdapter adapter;
    private boolean isThereClassForRating=false;
    private List<Long> deletedMediaList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_rating);
        ButterKnife.bind(activity);
        setToolBarForRating();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ratingValue = getIntent().getIntExtra(AppConstants.DataKey.RATING_VALUE, -1);
        navigatinFrom = getIntent().getIntExtra(AppConstants.DataKey.NAVIGATED_FROM_INT, -1);
        model= (ClassModel) getIntent().getSerializableExtra(AppConstants.DataKey.CLASS_MODEL);
        flexBoxLayoutOptions.setFlexDirection(FlexDirection.ROW);
        setListeners();
        if(model!=null){
            CharSequence text = String.format(mContext.getString(R.string.how_was_your_class_v_two),model.getTitle());
            textViewHowWasExperiance.setText(text);
        }

        getUnRatedClassList();
        editTextComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LogUtils.debug("Focus Changed "+hasFocus);
                if (!hasFocus) {
                    KeyboardUtils.close(v,mContext);
                }
            }
        });
        if(model.getRatingResDto()!=null&&model.getRatingResDto().getRating()>0){
            ratingId=model.getRatingResDto().getId();
            ratingValue=(int)model.getRatingResDto().getRating().intValue();
            setStarRating(ratingValue);
            if(TempStorage.getRatingParams()!=null&&TempStorage.getRatingParams().size()>0){
                createRatingParamList();
                }else{
                networkCommunicator.getRatingParameters(this,true);
                }
                if(model.getRatingResDto().getMediaList()!=null&&model.getRatingResDto().getMediaList().size()>0){
                    fileList.addAll(createSelectedFileDataList(model.getRatingResDto().getMediaList()));

                }
                setImagesToView();
                if(model.getRatingResDto().getRemark()!=null&&model.getRatingResDto().getRemark().length()>0){
                    editTextComment.setText(model.getRatingResDto().getRemark());
                }


        }
        else{
            if(ratingValue>-1){ setStarRating(ratingValue); }
            refrenceWrapper.getCustomRateAlertDialog().dismiss();
            if(TempStorage.getRatingParams()!=null&&TempStorage.getRatingParams().size()>0){
                createRatingParamList();
            }else{
                networkCommunicator.getRatingParameters(this,true);
            }
        }

    }



    public void setListeners(){
        imageViewChooseImage.setOnClickListener(this);
        ratingStar1.setOnClickListener(this);
        ratingStar2.setOnClickListener(this);
        ratingStar3.setOnClickListener(this);
        ratingStar4.setOnClickListener(this);
        ratingStar5.setOnClickListener(this);
        rateClasses.setOnClickListener(this);
    }

    private void setToolBarForRating() {

        BaseActivity activity = (BaseActivity) this.activity;
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimaryDark)));
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setDisplayUseLogoEnabled(true);

        View v = LayoutInflater.from(context).inflate(R.layout.view_tool_normal, null);

        v.findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((TextView) v.findViewById(R.id.textViewTitle)).setText(context.getResources().getText(R.string.rate_class_tilte));

        ImageView imageViewShare = v.findViewById(R.id.imageViewShare);

        imageViewShare.setVisibility(View.GONE);

        activity.getSupportActionBar().setCustomView(v, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));
        activity.getSupportActionBar().setDisplayShowCustomEnabled(true);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewChooseImage:
                imagePicker();

                break;
            case R.id.imageViewBack:

                break;
            case R.id.rateClasses:
                handleSubmitButton(mContext.getResources().getString(R.string.submiting_review),false,View.VISIBLE);
                if(model.getRatingResDto()!=null&&model.getRatingResDto().getRating()>0){
                    updateRateClass();
                }else{
                    if(ratingId>0){
                        if(isAllResponseReceived()){
                            if(isFileUploadCompleteSuccess()){
                                networkCommunicator.publishClassRating(ratingId,new ClassRatingRequest(1),this,false);
                                return;
                            }
                        }
                        processFileUpload();
                    }
                    else{
                        rateClass();
                    }
                }

                break;
            case R.id.ratingStar1:{
                setStarRating(1);

            }
            break;
            case R.id.ratingStar2:{
                setStarRating(2);


            }
            break;
            case R.id.ratingStar3:{
                setStarRating(3);


            }
            break;
            case R.id.ratingStar4:{
                setStarRating(4);



            }
            break;
            case R.id.ratingStar5:{
                setStarRating(5);

            }
            break;
            case R.id.buttonParam:{

            }
            break;
        }
    }

    public void setStarRatingsFilled(Context context, View... view) {
        for (View v : view) {
            ((ImageView) v).setImageResource(R.drawable.star_filled);
        }
    }
    public void setStarRatingsBlank(Context context, View... view) {
        for (View v : view) {
            ((ImageView) v).setImageResource(R.drawable.star_blank);
        }
    }


    private void imagePicker() {
        ImageUtility.openCameraGallery(mContext,(ImageUtility.IMAGE_SELECTION_LIMIT-fileList.size()));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0, len = permissions.length; i < len; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                if (Manifest.permission.CAMERA.equals(permission)) {
                    showPermissionImportantAlert(context.getResources().getString(R.string.permission_message_camera));
                    return;
                }
                else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                    showPermissionImportantAlert(context.getResources().getString(R.string.permission_message_media));
                    return;
                }
                else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                    showPermissionImportantAlert(context.getResources().getString(R.string.permission_message_media));

                    return;
                }
            }
        }
        ImageUtility.openCameraGallery(mContext,(ImageUtility.IMAGE_SELECTION_LIMIT-fileList.size()));
        }
    private void showPermissionImportantAlert(String message){
        DialogUtils.showBasicMessage(context,context.getResources().getString(R.string.permission_alert), message,
                context.getResources().getString(R.string.go_to_settings), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + context.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        context.startActivity(i);
                        dialog.dismiss();
                    }
                },context.getResources().getString(R.string.cancel), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == ImageUtility.REQUEST_CODE_GALLERY) {
             fileList.addAll(createSelectedFileDataList(data.getStringArrayListExtra(Pix.IMAGE_RESULTS)));
             setImagesToView();
        }

    }

    private List<SelectedFileData> createSelectedFileDataList(ArrayList<String> returnValue){
     List<SelectedFileData> fileDataList=new ArrayList<>();
        for (String path:returnValue) {
            fileDataList.add(new SelectedFileData(path,0,0,System.currentTimeMillis(), CommonUtillity.getnerateUniqueToken(this)));
        }
      return fileDataList;
    }
    private List<SelectedFileData> createSelectedFileDataList(List<MediaModel> mediaList) {
        List<SelectedFileData> fileDataList=new ArrayList<>();
        for (MediaModel mediaModel:mediaList) {
            fileDataList.add(new SelectedFileData(mediaModel.getMediaThumbNailUrl(),100,2,mediaModel.getMediaId(), CommonUtillity.getnerateUniqueToken(this)));
        }
        return fileDataList;
    }


    private void setImagesToView(){
        adapter = new SelectedImageListAdapter(context, AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE,fileList,this);
        selectedImageRecyclerView.setAdapter(adapter);
        selectedImageRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        selectedImageRecyclerView.setLayoutManager(layoutManager);

        adapter.notifyDataSetChanged();
        if(fileList.size()>=ImageUtility.IMAGE_SELECTION_LIMIT){
            imageViewChooseImage.setVisibility(View.GONE);
        }
        else{
            imageViewChooseImage.setVisibility(View.VISIBLE);

        }


    }
    private void createRatingParamList(){
        if(model.getRatingResDto()!=null&&model.getRatingResDto().getRatingFeedbackAreaResList()!=null&&model.getRatingResDto().getRatingFeedbackAreaResList().size()>0){
            List<RatingParamModel> ratingParamModels=new ArrayList<>();
            for (RatingParamModel param:TempStorage.getRatingParams()) {
                for (RatingFeedbackAreaResList ratingFeedbackAreaResList:model.getRatingResDto().getRatingFeedbackAreaResList()) {
                    if(param.getId()==ratingFeedbackAreaResList.getParameterId()){
                        param.setSelected(true);
                    }
                }
                ratingParamModels.add(param);
            }
            addRatingParams(ratingParamModels);

        }else{
            addRatingParams(TempStorage.getRatingParams());

        }
    }

    private void addRatingParams(final List<RatingParamModel> ratingParamModelList) {
        flexBoxLayoutOptions.removeAllViews();
        selectedRatingParam = new ArrayList<>();
        int i=0;
        for(i=0;i<ratingParamModelList.size();i++){
            if((((int) ratingParamModelList.get(i).getRating()))==ratingValue){
                final View view = LayoutInflater.from(context).inflate(R.layout.layout_rating_param_item, flexBoxLayoutOptions, false);
                final Button textView = view.findViewById(R.id.buttonParam);
                textView.setText(ratingParamModelList.get(i).getParameter());
                if(ratingParamModelList.get(i).isSelected()){
                    textView.setTextColor(mContext.getResources().getColor(R.color.white));
                    textView.setBackgroundResource(R.drawable.bg_rounded_selected);
                    ratingParamModelList.get(i).setSelected(true);
                    selectedRatingParam.add(ratingParamModelList.get(i));

                }
                flexBoxLayoutOptions.addView(view);
                final int finalI = i;
                textView.setOnClickListener(this);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ratingParamModelList.get(finalI).isSelected()){
                            selectedRatingParam.remove(ratingParamModelList.get(finalI));
                            textView.setTextColor(mContext.getResources().getColor(R.color.black));
                            textView.setBackgroundResource(R.drawable.bg_rounded_unselected);
                            ratingParamModelList.get(finalI).setSelected(false);

                        }else{
                            textView.setTextColor(mContext.getResources().getColor(R.color.white));
                            textView.setBackgroundResource(R.drawable.bg_rounded_selected);
                            ratingParamModelList.get(finalI).setSelected(true);
                            selectedRatingParam.add(ratingParamModelList.get(finalI));
                        }

                    }
                });
            }
        }
    }
    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {
        switch (view.getId()){
            case R.id.imageViewCross:
                if(fileList.get(position).getFileUploadStatus()!=2){
                    fileList.remove(position);
                    setImagesToView();
                }else{
                    deletedMediaList.add(fileList.get(position).getFileId());
                    fileList.remove(position);
                    setImagesToView();
                    }

                break;

        }

    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, Object model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
    private void setStarRating(int rating){
        ratingValue=rating;
        createRatingParamList();
        switch (rating){
            case 1:
                setStarRatingsFilled(mContext,ratingStar1);
                setStarRatingsBlank(mContext,ratingStar2,ratingStar3,ratingStar4,ratingStar5);
                break;
            case 2:
                setStarRatingsFilled(mContext,ratingStar1,ratingStar2);
                setStarRatingsBlank(mContext,ratingStar3,ratingStar4,ratingStar5);
                break;
            case 3:
                setStarRatingsFilled(mContext,ratingStar1,ratingStar2,ratingStar3);
                setStarRatingsBlank(mContext,ratingStar4,ratingStar5);
                break;
            case 4:
                setStarRatingsFilled(mContext,ratingStar1,ratingStar2,ratingStar3,ratingStar4);
                setStarRatingsBlank(mContext,ratingStar5);
                break;
            case 5:
                setStarRatingsFilled(mContext,ratingStar1,ratingStar2,ratingStar3,ratingStar4,ratingStar5);
                break;
        }

    }
    private void updateRateClass(){
        if(deletedMediaList!=null&&deletedMediaList.size()>0){
            for (Long meidaId:deletedMediaList) {
                networkCommunicator.deleteMedia(meidaId,this,true);

            }
        }
        ClassRatingRequest request =new ClassRatingRequest();
        request.setmRating(ratingValue);
        request.setmObjectTypeId(5);
        request.setmObjectDataId(model.getClassSessionId());
        request.setmUserId(TempStorage.getUser().getId());
//        if(fileList!=null&&fileList.size()>0){
//            request.setmIsImage(fileList.size());
//        }
        if(editTextComment.getText().toString()!=null&&editTextComment.getText().toString().trim().length()>0){
            request.setRemark(editTextComment.getText().toString());
        }
        if(selectedRatingParam!=null&&selectedRatingParam.size()>0){
            List<RatingFeedbackAreaList> ratingFeedbackAreaList=new ArrayList<>();
            for (RatingParamModel modelRatingParam:selectedRatingParam) {
                RatingFeedbackAreaList feedbackAreaList = new RatingFeedbackAreaList();
                feedbackAreaList.setRatingParameterId(modelRatingParam.getId()+"");
                ratingFeedbackAreaList.add(feedbackAreaList);
            }
            request.setRatingFeedbackAreaList(ratingFeedbackAreaList);
            }
//        if(model.getRatingResDto()!=null&&model.getRatingResDto().getRatingFeedbackAreaResList()!=null&&model.getRatingResDto().getRatingFeedbackAreaResList().size()>0){
//            for (RatingFeedbackAreaResList data:model.getRatingResDto().getRatingFeedbackAreaResList()) {
//                boolean isDeleted=true;
//                for (RatingParamModel modelRatingParam:selectedRatingParam) {
//                    if(data.getParameterId()==modelRatingParam.getId()){
//                        isDeleted=false;
//                    }
//                }
//                if(isDeleted){
//                    deletedRatingFeedbackIds.add(data.getParameterId());
//                }
//            }
//        }
//        if(deletedRatingFeedbackIds!=null&&deletedRatingFeedbackIds.size()>0){
//            request.setDeletedRatingFeedbackIds(deletedRatingFeedbackIds);
//        }
        if((fileList==null||fileList.size()==0)&&(editTextComment.getText().toString()==null||editTextComment.getText().toString().trim().length()==0)){
            request.setIsPublish(1);
            request.setStatus(1);
        }
        else{
            request.setStatus(0);
            request.setIsPublish(0);
        }

        networkCommunicator.updateClassRating(ratingId,request,this,false);

    }
    private void rateClass(){
        ClassRatingRequest request =new ClassRatingRequest();
        request.setmRating(ratingValue);
        request.setmObjectTypeId(5);
        request.setmObjectDataId(model.getClassSessionId());
        request.setmUserId(TempStorage.getUser().getId());
//        if(fileList!=null&&fileList.size()>0){
//            request.setmIsImage(fileList.size());
//        }
        if(editTextComment.getText().toString()!=null&&editTextComment.getText().toString().trim().length()>0){
            request.setRemark(editTextComment.getText().toString());
        }
        if(selectedRatingParam!=null&&selectedRatingParam.size()>0){
            List<RatingFeedbackAreaList> ratingFeedbackAreaList=new ArrayList<>();
            for (RatingParamModel modelRatingParam:selectedRatingParam) {
                RatingFeedbackAreaList feedbackAreaList = new RatingFeedbackAreaList();
                feedbackAreaList.setRatingParameterId(modelRatingParam.getId()+"");
                ratingFeedbackAreaList.add(feedbackAreaList);
            }
            request.setRatingFeedbackAreaList(ratingFeedbackAreaList);
        }
        if((fileList==null||fileList.size()==0)&&(editTextComment.getText().toString()==null||editTextComment.getText().toString().trim().length()==0)){
            request.setIsPublish(1);
            request.setStatus(1);
        }
    else{
            if(isFileUploadCompleteSuccess()){
              if(editTextComment.getText().toString()!=null&&editTextComment.getText().toString().length()>0){
                  request.setIsPublish(1);
                  request.setStatus(1);
              }else{
                  request.setIsPublish(0);
                  request.setStatus(0);
              }
            }else{
                request.setIsPublish(0);
                request.setStatus(0);
            }

        }

        networkCommunicator.submitClassRating(request,this,false);

    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode){
            case NetworkCommunicator.RequestCode.CLASS_RATING:
                RatingResponseModel responseModel = ((ResponseModel<RatingResponseModel>) response).data;
                ratingId=responseModel.getId();
                processFileUpload();
                    TempStorage.removeSavedClass(model.getClassSessionId(),context);
                    break;
            case NetworkCommunicator.RequestCode.CLASS_RATING_UPDATE:
                RatingResponseModel responseModelUpdate = ((ResponseModel<RatingResponseModel>) response).data;
                processFileUpload();
                break;
            case NetworkCommunicator.RequestCode.CLASS_RATING_PUBLISH:
                handleSubmitButton(mContext.getResources().getString(R.string.submit_review),true,View.GONE);
                showTahnkYou(isThereClassForRating);
                break;
            case NetworkCommunicator.RequestCode.PHOTO_UPLOAD:
               SelectedFileData fileData= (SelectedFileData) response;
               int indexOfFileData = adapter.getList().indexOf(fileData);
               if(indexOfFileData!=-1){
                   Object obj = adapter.getList().get(indexOfFileData);
                   if (obj instanceof SelectedFileData) {
                       SelectedFileData selectedFileDataModel = (SelectedFileData) obj;
                       selectedFileDataModel.setFileUploadStatus(2);
                       adapter.notifyItemChanged(indexOfFileData);
                       fileList.get(indexOfFileData).setFileUploadStatus(2);

                   }
               }
               publishReview();



                break;
            case NetworkCommunicator.RequestCode.IMAGE_UPLOAD_PROGRESS:
                SelectedFileData fileDataProgress = (SelectedFileData) response;
                int indexOfFileDataProgress = adapter.getList().indexOf(fileDataProgress);
                if(indexOfFileDataProgress!=-1){
                    Object obj = adapter.getList().get(indexOfFileDataProgress);
                    if (obj instanceof SelectedFileData) {
//                        SelectedFileData selectedFileDataModel = (SelectedFileData) obj;
//                        selectedFileDataModel.setFileUploadStatus(2);
//                        adapter.notifyItemChanged(indexOfFileDataProgress);
//                        fileList.get(indexOfFileDataProgress).setFileUploadStatus(2);

                    }
                }
                break;
            case NetworkCommunicator.RequestCode.IMAGE_UPLOAD_FAILED:
                SelectedFileData fileDataFailed = (SelectedFileData) response;
                int indexOfFileDataFailed = adapter.getList().indexOf(fileDataFailed);
                if(indexOfFileDataFailed!=-1){
                    Object obj = adapter.getList().get(indexOfFileDataFailed);
                    if (obj instanceof SelectedFileData) {
                        SelectedFileData selectedFileDataModel = (SelectedFileData) obj;
                        selectedFileDataModel.setFileUploadStatus(3);
                        adapter.notifyItemChanged(indexOfFileDataFailed);
                        fileList.get(indexOfFileDataFailed).setFileUploadStatus(3);

                    }
                }
                if(isAllResponseReceived()){
                    ToastUtils.show(mContext,"Image upload failed ");
                    handleSubmitButton(mContext.getResources().getString(R.string.retry_review),true,View.GONE);


                }

                break;
            case NetworkCommunicator.RequestCode.UNRATED_CLASS_COUNT:
                UnratedClassData classModels = ((ResponseModel<UnratedClassData>) response).data;
                if(model.getRatingResDto()!=null&&model.getRatingResDto().getRating()>0){
                    if(classModels!=null&&classModels.getCount()>0){
                        isThereClassForRating=true;
                    }
                }else{
                    if(classModels!=null&&classModels.getCount()>1){
                        isThereClassForRating=true;
                    }
                }


                break;
            case NetworkCommunicator.RequestCode.RATING_PARAMS:
                createRatingParamList();
                break;
            case NetworkCommunicator.RequestCode.MEDIA_DELETE:


                break;
                }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
        switch (requestCode){
                case NetworkCommunicator.RequestCode.CLASS_RATING:
                handleSubmitButton(mContext.getResources().getString(R.string.retry_review),true,View.GONE);
                ToastUtils.show(mContext,errorMessage);
                break;
                case NetworkCommunicator.RequestCode.CLASS_RATING_UPDATE:
                handleSubmitButton(mContext.getResources().getString(R.string.retry_review),true,View.GONE);
                ToastUtils.show(mContext,errorMessage);
                break;
                case NetworkCommunicator.RequestCode.CLASS_RATING_PUBLISH:
                    ToastUtils.show(mContext,errorMessage);
                     handleSubmitButton(mContext.getResources().getString(R.string.retry_review),true,View.GONE);
                     break;
            case NetworkCommunicator.RequestCode.MEDIA_DELETE:
                ToastUtils.show(mContext,errorMessage);
                    break;

        }
    }

    private boolean isFileUploadCompleteSuccess(){
        boolean isUploadCompleted=true;
        if(fileList!=null&&fileList.size()>0){
            for (int i=0;i<fileList.size();i++){
            if(fileList.get(i).getFileUploadStatus()!=2){
                isUploadCompleted=false;
            }
        }
        }
        return isUploadCompleted;
    }

    private boolean isAllResponseReceived(){
       boolean isAllResponseReceived=false;
       if(fileList!=null&&fileList.size()>0){
           for (int i=0;i<fileList.size();i++){
               if(fileList.get(i).getFileUploadStatus()==2||fileList.get(i).getFileUploadStatus()==3){
                   isAllResponseReceived=true;
               }else{
                   return false;
               }
           }
       }else{
           isAllResponseReceived=true;

       }

      return isAllResponseReceived;
    }

    private void processFileUpload(){
      boolean isProcessComplete=true;
        if(fileList!=null&&fileList.size()>0){
            for (SelectedFileData file:fileList) {
                if(file.getFileUploadStatus()!=2){
                    networkCommunicator.uploadRatingImages(mContext,file,(int)ratingId,new File(file.getFilepath()),this,false);
                  isProcessComplete=false;
                }
            }
        }
        if(isProcessComplete){
            if(editTextComment.getText().toString()==null||editTextComment.getText().toString().length()==0){
                showTahnkYou(isThereClassForRating);
                handleSubmitButton(mContext.getResources().getString(R.string.submit_review),true,View.GONE);
            }else{
               publishReview();
            }

            }
    }

    private void publishReview(){
        if(isAllResponseReceived()){
            if(isFileUploadCompleteSuccess()){
                networkCommunicator.publishClassRating(ratingId,new ClassRatingRequest(1),this,false);
                return;
            }
            handleSubmitButton(mContext.getResources().getString(R.string.retry_review),true,View.GONE);
        }
    }




    private void showTahnkYou(boolean isThereClassForRating){
        EventBroadcastHelper.sendclassRating(context,model.getTitle());
        CustomDialogThankYou mCustomThankyouDialog = new CustomDialogThankYou(RefrenceWrapper.getRefrenceWrapper(this).getActivity(),isThereClassForRating,navigatinFrom);
        try {
            mCustomThankyouDialog.show();
            }catch (Exception e){
            e.printStackTrace();
        }
        finish();
    }

    private void getUnRatedClassList(){
        networkCommunicator.getUnratedClassList(0,AppConstants.Limit.PAGE_LIMIT_MAIN_CLASS_LIST,this,true);
    }
    private void handleSubmitButton(String message,boolean isClikable,int visibility){
        rateClasses.setText(message);
        rateClasses.setClickable(isClikable);
        parentLayout.setEnabled(isClikable);
        progressBarRating.setVisibility(visibility);
    }

}

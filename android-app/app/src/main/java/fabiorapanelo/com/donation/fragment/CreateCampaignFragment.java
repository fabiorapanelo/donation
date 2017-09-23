package fabiorapanelo.com.donation.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.activity.PickLocationActivity;
import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.GeoPointLocation;
import fabiorapanelo.com.donation.model.ImageUpload;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.utils.PermissionUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static fabiorapanelo.com.donation.services.UserService.CACHE_KEY_USER_SERVICE_FIND;

public class CreateCampaignFragment extends BaseFragment implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int REQUEST_CODE_ADD_PHOTO1 = 1;
    public static final int REQUEST_CODE_ADD_PHOTO2 = 2;
    public static final int REQUEST_CODE_ADD_PHOTO3 = 3;
    public static final int REQUEST_CODE_PICK_LOCATION = 4;

    @Bind(R.id.input_campaign_name)
    protected EditText campaignName;

    @Bind(R.id.btn_create_campaign)
    protected Button createCampaign;

    @Bind(R.id.image_view_photo1)
    protected ImageView mImageViewPhoto1;

    @Bind(R.id.image_view_photo2)
    protected ImageView mImageViewPhoto2;

    @Bind(R.id.image_view_photo3)
    protected ImageView mImageViewPhoto3;

    @Bind(R.id.btn_pick_location)
    protected ImageButton mPickLocation;

    @Bind(R.id.text_selected_location)
    protected TextView mTxtSelectedLocation;

    @Bind(R.id.create_campaign_message_layout)
    protected LinearLayout mCreateCampaignMessageLayout;

    @Bind(R.id.campaign_fields_layout)
    protected LinearLayout mCampaignFieldsLayout;

    @Bind(R.id.create_campaign_layout)
    protected LinearLayout mCreateCampaignLayout;


    protected Double mLatitude;
    protected Double mLongitude;
    protected boolean mLocationSelected = false;

    protected String image1;
    protected String image2;
    protected String image3;

    public static CreateCampaignFragment newInstance() {
        CreateCampaignFragment fragment = new CreateCampaignFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_campaign, container, false);

        ButterKnife.bind(this, view);



        this.setupListeners();
        this.reloadOnCreateView();

        return view;
    }

    protected void setupListeners(){
        createCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateCampaignFragment.this.createCampaign(view);
            }
        });

        mImageViewPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateCampaignFragment.this.uploadPhoto(REQUEST_CODE_ADD_PHOTO1);
            }
        });

        mImageViewPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateCampaignFragment.this.uploadPhoto(REQUEST_CODE_ADD_PHOTO2);
            }
        });

        mImageViewPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateCampaignFragment.this.uploadPhoto(REQUEST_CODE_ADD_PHOTO3);
            }
        });

        mPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateCampaignFragment.this.pickLocation(view);
            }
        });
    }

    protected void reloadOnCreateView(){

        if(mLatitude != null && mLongitude != null){
            this.setLocation(mLatitude, mLongitude);
        }

        if(StringUtils.isNotEmpty(image1)){
            Bitmap photo = BitmapFactory.decodeFile(image1);
            mImageViewPhoto1.setImageBitmap(photo);
        }

        if(StringUtils.isNotEmpty(image2)){
            Bitmap photo = BitmapFactory.decodeFile(image2);
            mImageViewPhoto1.setImageBitmap(photo);
        }

        if(StringUtils.isNotEmpty(image3)){
            Bitmap photo = BitmapFactory.decodeFile(image3);
            mImageViewPhoto1.setImageBitmap(photo);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void uploadPhoto(int requestCode){
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, requestCode);

        } else {
            PermissionUtils.requestPermission(mActivity, requestCode, Manifest.permission.READ_EXTERNAL_STORAGE, false);
        }
    }

    /**
     * Currently this method is never called because it is calling the Activity method onRequestPermissionsResult, not the fragment
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ADD_PHOTO1 ||
                requestCode == REQUEST_CODE_ADD_PHOTO2 ||
                requestCode == REQUEST_CODE_ADD_PHOTO3) {

            if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                uploadPhoto(requestCode);
            } else {
                PermissionUtils.PermissionDeniedDialog.newInstance(true).show(mActivity.getSupportFragmentManager(), "dialog");
            }

        }

    }

    protected void pickLocation(View view){
        Intent intent = new Intent(mActivity, PickLocationActivity.class);
        startActivityForResult(intent, REQUEST_CODE_PICK_LOCATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_LOCATION && resultCode == RESULT_OK) {

            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            this.setLocation(latitude, longitude);

        } else if((requestCode == REQUEST_CODE_ADD_PHOTO1 ||
                requestCode == REQUEST_CODE_ADD_PHOTO2 ||
                requestCode == REQUEST_CODE_ADD_PHOTO3) && resultCode == Activity.RESULT_OK) {

            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = mActivity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap photo = BitmapFactory.decodeFile(picturePath);

            if(requestCode == REQUEST_CODE_ADD_PHOTO1){
                mImageViewPhoto1.setImageBitmap(photo);
                image1 = picturePath;
            } else if(requestCode == REQUEST_CODE_ADD_PHOTO2){
                mImageViewPhoto2.setImageBitmap(photo);
                image2 = picturePath;
            } else if(requestCode == REQUEST_CODE_ADD_PHOTO3){
                mImageViewPhoto3.setImageBitmap(photo);
                image3 = picturePath;
            }
        }

    }

    protected void createCampaign(View view){

        if(!mLocationSelected){
            Toast.makeText(mActivity, "Localização deve ser selecionada!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(StringUtils.isEmpty(image1) && StringUtils.isEmpty(image2) && StringUtils.isEmpty(image3)){
            Toast.makeText(mActivity, "Uma image deve ser selecionada!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> images = new ArrayList<>();
        if(StringUtils.isNotEmpty(image1)){
            images.add(image1);
        }

        if(StringUtils.isNotEmpty(image2)){
            images.add(image2);
        }

        if(StringUtils.isNotEmpty(image3)){
            images.add(image3);
        }

        final User user = userDao.find();

        imageService.upload(user, images, new Callback<ImageUpload>() {
            @Override
            public void onResponse(Call<ImageUpload> call, Response<ImageUpload> response) {

                if(response.isSuccessful()){

                    Campaign campaign = new Campaign();

                    String name = campaignName.getText().toString();
                    campaign.setName(name);

                    GeoPointLocation location = new GeoPointLocation();
                    location.setType("Point");
                    location.getCoordinates().add(mLongitude);
                    location.getCoordinates().add(mLatitude);
                    campaign.setLocation(location);

                    campaign.setUserId(user.getId().toString());

                    ImageUpload imageUpload = response.body();
                    campaign.setImages(imageUpload.getImages());

                    campaignService.save(campaign, new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if(response.isSuccessful()){

                                mCreateCampaignMessageLayout.setVisibility(View.VISIBLE);
                                mCampaignFieldsLayout.setVisibility(View.GONE);
                                mCreateCampaignLayout.setVisibility(View.GONE);

                                CreateCampaignFragment.this.resetFields();
                                cacheManager.evict(CACHE_KEY_USER_SERVICE_FIND);

                            } else {
                                Toast.makeText(mActivity, "Falha ao cadastrar campanha!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(mActivity, "Falha ao cadastrar campanha!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(mActivity, "Falha ao enviar imagem!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {
                Toast.makeText(mActivity, "Falha ao enviar imagem!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void setLocation(double latitude, double longitude){
        mLatitude = latitude;
        mLongitude = longitude;
        mTxtSelectedLocation.setText(mLatitude + ":" + mLongitude);
        mLocationSelected = true;
    }

    protected void resetFields(){
        mLatitude = null;
        mLongitude = null;
        mLocationSelected = false;
        image1 = null;
        image2 = null;
        image3 = null;
        campaignName.setText("");
    }

}

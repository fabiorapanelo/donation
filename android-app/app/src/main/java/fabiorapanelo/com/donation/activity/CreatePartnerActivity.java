package fabiorapanelo.com.donation.activity;

import android.Manifest;
import android.app.Activity;
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
import android.view.View;
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
import fabiorapanelo.com.donation.model.GeoPointLocation;
import fabiorapanelo.com.donation.model.ImageUpload;
import fabiorapanelo.com.donation.model.Partner;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.services.PartnerService;
import fabiorapanelo.com.donation.utils.LocationUtils;
import fabiorapanelo.com.donation.utils.PermissionUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fabio on 24/10/2017.
 */

public class CreatePartnerActivity extends BaseActivity implements
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_partner);

        ButterKnife.bind(this);

        this.setupToolbar();

        this.setupListeners();
        this.reloadOnCreateView();
    }

    protected void setupListeners(){
        createCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePartnerActivity.this.createPartner(view);
            }
        });

        mImageViewPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePartnerActivity.this.uploadPhoto(REQUEST_CODE_ADD_PHOTO1);
            }
        });

        mImageViewPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePartnerActivity.this.uploadPhoto(REQUEST_CODE_ADD_PHOTO2);
            }
        });

        mImageViewPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePartnerActivity.this.uploadPhoto(REQUEST_CODE_ADD_PHOTO3);
            }
        });

        mPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePartnerActivity.this.pickLocation(view);
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

    protected void uploadPhoto(int requestCode){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, requestCode);

        } else {
            PermissionUtils.requestPermission(this, requestCode, Manifest.permission.READ_EXTERNAL_STORAGE, false);
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
                PermissionUtils.PermissionDeniedDialog.newInstance(true).show(this.getSupportFragmentManager(), "dialog");
            }

        }

    }

    protected void pickLocation(View view){
        Intent intent = new Intent(this, PickLocationActivity.class);
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
            Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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

    protected void createPartner(View view){

        if(!mLocationSelected){
            Toast.makeText(this, "Localização deve ser selecionada!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(StringUtils.isEmpty(image1) && StringUtils.isEmpty(image2) && StringUtils.isEmpty(image3)){
            Toast.makeText(this, "Uma image deve ser selecionada!", Toast.LENGTH_SHORT).show();
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

                    Partner partner = new Partner();

                    String name = campaignName.getText().toString();
                    partner.setName(name);

                    GeoPointLocation location = new GeoPointLocation();
                    location.setType("Point");
                    location.getCoordinates().add(mLongitude);
                    location.getCoordinates().add(mLatitude);
                    partner.setLocation(location);

                    partner.setUserId(user.getId().toString());

                    ImageUpload imageUpload = response.body();
                    partner.setImages(imageUpload.getImages());

                    partnerService.save(partner, new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if(response.isSuccessful()){

                                mCreateCampaignMessageLayout.setVisibility(View.VISIBLE);
                                mCampaignFieldsLayout.setVisibility(View.GONE);
                                mCreateCampaignLayout.setVisibility(View.GONE);

                                CreatePartnerActivity.this.resetFields();
                                cacheManager.evict(PartnerService.Factory.getCACHE_KEY_PARTNER_SERVICE_FIND());
                                cacheManager.evict(PartnerService.Factory.getCACHE_KEY_PARTNER_SERVICE_FIND_MY_PARTNERS());

                            } else {
                                Toast.makeText(CreatePartnerActivity.this, "Falha ao cadastrar parceiro!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(CreatePartnerActivity.this, "Falha ao cadastrar parceiro!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(CreatePartnerActivity.this, "Falha ao enviar imagem!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageUpload> call, Throwable t) {
                Toast.makeText(CreatePartnerActivity.this, "Falha ao enviar imagem!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void setLocation(double latitude, double longitude){
        mLatitude = latitude;
        mLongitude = longitude;
        String locationName = LocationUtils.getLocationName(this, longitude, latitude);

        mTxtSelectedLocation.setText(locationName);
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

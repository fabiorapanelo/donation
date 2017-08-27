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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import fabiorapanelo.com.donation.R;
import fabiorapanelo.com.donation.activity.PickLocationActivity;
import fabiorapanelo.com.donation.database.UserDao;
import fabiorapanelo.com.donation.model.Campaign;
import fabiorapanelo.com.donation.model.User;
import fabiorapanelo.com.donation.services.CampaignService;
import fabiorapanelo.com.donation.services.ImageService;
import fabiorapanelo.com.donation.services.UserService;
import fabiorapanelo.com.donation.utils.PermissionUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class CreateCampaignFragment extends Fragment implements
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

    protected String mLatitude;
    protected String mLongitude;
    protected boolean mLocationSelected = false;

    protected String image1;
    protected String image2;
    protected String image3;

    protected CampaignService campaignService;

    protected UserDao userDao;

    protected AppCompatActivity mActivity;

    protected ImageService imageService;

    public static CreateCampaignFragment newInstance() {
        CreateCampaignFragment fragment = new CreateCampaignFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivity = (AppCompatActivity) this.getActivity();

        View view = inflater.inflate(R.layout.fragment_create_campaign, container, false);

        ButterKnife.bind(this, view);

        campaignService = CampaignService.getInstance();
        imageService = ImageService.getInstance();

        userDao = new UserDao(mActivity);

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

        return view;
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

            mLatitude = String.valueOf(data.getDoubleExtra("latitude", 0));
            mLongitude = String.valueOf(data.getDoubleExtra("longitude", 0));
            mTxtSelectedLocation.setText(mLatitude + ":" + mLongitude);
            mLocationSelected = true;

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

        if(StringUtils.isEmpty(image1)){
            Toast.makeText(mActivity, "Uma image deve ser selecionada!", Toast.LENGTH_SHORT).show();
            return;
        }

        final User user = userDao.find();

        imageService.upload(user, image1, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){

                    Campaign campaign = new Campaign();

                    String name = campaignName.getText().toString();
                    campaign.setName(name);
                    campaign.setLatitude(mLatitude);
                    campaign.setLongitude(mLongitude);
                    campaign.setCreatedBy(UserService.getUrlForUser(user));

                    campaignService.save(campaign, new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if(response.isSuccessful()){
                                Toast.makeText(mActivity, "Campanha cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(mActivity, "Falha ao enviar imagem!", Toast.LENGTH_SHORT).show();
            }
        });



    }

}
